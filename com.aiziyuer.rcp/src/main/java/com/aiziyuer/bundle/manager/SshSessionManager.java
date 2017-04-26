package com.aiziyuer.bundle.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.forward.PortForwardingEventListener;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.session.SessionListener;
import org.apache.sshd.common.util.io.IoUtils;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aiziyuer.bundle.models.ssh.ConnectStatus;
import com.aiziyuer.bundle.models.ssh.SshSession;
import com.aiziyuer.bundle.models.ssh.SshTunnel;

public class SshSessionManager {

	public static final SshSessionManager INSTANCE = new SshSessionManager();

	private Logger log = LoggerFactory.getLogger(getClass());

	private SshSessionManager() {
	}

	// /** Session的映射 */
	private Map<String, ClientSession> sessionMap = new HashMap<String, ClientSession>();

	public void createTunnels(SshSession sessionInfoBO, List<SshTunnel> tunnelBOs) {

		for (SshTunnel tunnelBO : tunnelBOs)
			createTunnel(sessionInfoBO, tunnelBO);

	}

	public class MySessionListener implements SessionListener {

		private final SshSession sessionInfoBO;

		public MySessionListener(SshSession sessionInfoBO) {
			this.sessionInfoBO = sessionInfoBO;
		}

		@Override
		public void sessionClosed(Session session) {

			// 设置成离线状态
			sessionInfoBO.setStatus(ConnectStatus.DIS_CONNECTED);

			log.info("session %s disconnect.", session);
		}

		@Override
		public void sessionEvent(Session session, Event event) {

			if (Event.Authenticated == event) {
				// 设置为认证通过状态
				sessionInfoBO.setStatus(ConnectStatus.CONNECTED);

				log.info("session %s authenticated.", session);
			}

		}
	}

	public class MyPortForwardListener implements PortForwardingEventListener {
		private final SshSession sessionInfoBO;

		public MyPortForwardListener(SshSession sessionInfoBO) {
			this.sessionInfoBO = sessionInfoBO;
		}

		@Override
		public void establishedExplicitTunnel(Session session, SshdSocketAddress local, SshdSocketAddress remote,
				boolean localForwarding, SshdSocketAddress boundAddress, Throwable reason) throws IOException {

			String localHostName = local.getHostName();
			int localport = local.getPort();
			String remoteHostName = remote.getHostName();
			int remotePort = remote.getPort();

			SshTunnel foundSshTunnel = null;
			Iterator<SshTunnel> it = sessionInfoBO.getTunnels().iterator();
			while (it.hasNext()) {
				SshTunnel tmpSshTunnel = it.next();
				boolean isSameLocalHost = StringUtils.equals(tmpSshTunnel.getLocalTunnelHost(), localHostName);
				boolean isSameLocalPort = tmpSshTunnel.getLocalTunnelPort() == localport;
				boolean isSameDirection = tmpSshTunnel.isLocal() == localForwarding;
				boolean isSameRemoteHost = StringUtils.equals(tmpSshTunnel.getRemoteTunnelHost(), remoteHostName);
				boolean isSameRemotePort = tmpSshTunnel.getRemoteTunnelPort() == remotePort;
				boolean isSameTunnel = isSameLocalHost && isSameLocalPort && isSameDirection && isSameRemoteHost
						&& isSameRemotePort;

				if (isSameTunnel) {
					foundSshTunnel = tmpSshTunnel;
					break;
				}
			}

			if (foundSshTunnel == null) {
				log.warn("(%s:%d %s %s:%d) not found, so don't update state.", local.getHostName(), local.getPort(),
						localForwarding ? "->" : "<-", remote.getHostName(), remote.getPort());
				return;
			}

			foundSshTunnel.setStatus(ConnectStatus.CONNECTED);

		}

		@Override
		public void tornDownExplicitTunnel(Session session, SshdSocketAddress address, boolean localForwarding,
				Throwable reason) throws IOException {

			String localHostName = address.getHostName();
			int localport = address.getPort();

			SshTunnel foundSshTunnel = null;
			Iterator<SshTunnel> it = sessionInfoBO.getTunnels().iterator();
			while (it.hasNext()) {
				SshTunnel tmpSshTunnel = it.next();
				boolean isSameHost = StringUtils.equals(tmpSshTunnel.getLocalTunnelHost(), localHostName);
				boolean isSamePort = tmpSshTunnel.getLocalTunnelPort() == localport;
				boolean isSameDirection = tmpSshTunnel.isLocal() == localForwarding;
				boolean isSameTunnel = isSameHost && isSamePort && isSameDirection;
				if (isSameTunnel) {
					foundSshTunnel = tmpSshTunnel;
					break;
				}
			}

			if (foundSshTunnel == null) {
				log.warn("%s:%d not found, so don't update status.", localHostName, localport);
			}

			foundSshTunnel.setStatus(ConnectStatus.DIS_CONNECTED);
		}

	}

	public void createTunnel(SshSession sessionInfoBO, SshTunnel tunnelBO) {

		String keyStr = String.format("%s@%s:%d", sessionInfoBO.getUserName(), sessionInfoBO.getHost(),
				sessionInfoBO.getPort());

		ClientSession session = sessionMap.get(keyStr);

		if (session == null) {

			SshClient client = SshClient.setUpDefaultClient();
			client.start();
			client.addSessionListener(new MySessionListener(sessionInfoBO));

			try {

				session = client.connect(sessionInfoBO.getUserName(), sessionInfoBO.getHost(), sessionInfoBO.getPort())
						.verify(7L, TimeUnit.SECONDS).getSession();

				session.addPasswordIdentity(sessionInfoBO.getUserPassword());
				session.auth().verify(5L, TimeUnit.SECONDS);

				// 如果登陆成功就记录session
				if (session.isAuthenticated())
					sessionMap.put(keyStr, session);

				session.addPortForwardingEventListener(new MyPortForwardListener(sessionInfoBO));

			} catch (IOException e) {
				log.error("createSession has error.", e);
				IoUtils.closeQuietly(client);
			}

		}

		if (session == null)
			log.error("session is null.");

		if (tunnelBO == null)
			return;

		try {
			if (tunnelBO.isLocal())
				session.startLocalPortForwarding(
						new SshdSocketAddress(tunnelBO.getLocalTunnelHost(), tunnelBO.getLocalTunnelPort()),
						new SshdSocketAddress(tunnelBO.getRemoteTunnelHost(), tunnelBO.getRemoteTunnelPort()));
			else
				session.startRemotePortForwarding(
						new SshdSocketAddress(tunnelBO.getRemoteTunnelHost(), tunnelBO.getRemoteTunnelPort()),
						new SshdSocketAddress(tunnelBO.getLocalTunnelHost(), tunnelBO.getLocalTunnelPort()));
		} catch (IOException e) {
			log.error("createTunnel has error.", e);
		}

	}

}
