package com.aiziyuer.bundle.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.session.SessionListener;
import org.apache.sshd.common.util.io.IoUtils;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			sessionInfoBO.setStatus(0);

			log.info("session %s disconnect.", session);
		}

		@Override
		public void sessionEvent(Session session, Event event) {

			if (Event.Authenticated == event) {
				// 设置为认证通过状态
				sessionInfoBO.setStatus(1);

				log.info("session %s authenticated.", session);
			}

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

			} catch (IOException e) {
				log.error("createTunnel has error.", e);
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
			IoUtils.closeQuietly(session);
		}

	}

}
