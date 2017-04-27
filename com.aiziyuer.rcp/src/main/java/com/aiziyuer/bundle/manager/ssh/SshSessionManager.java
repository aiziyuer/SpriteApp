package com.aiziyuer.bundle.manager.ssh;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.util.io.IoUtils;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aiziyuer.bundle.manager.ssh.listener.SshPortForwardListener;
import com.aiziyuer.bundle.manager.ssh.listener.SshSessionListener;
import com.aiziyuer.bundle.models.ssh.SshSession;
import com.aiziyuer.bundle.models.ssh.SshTunnel;

public class SshSessionManager {

	public static final SshSessionManager INSTANCE = new SshSessionManager();

	private static final Logger log = LoggerFactory.getLogger(SshSessionManager.class);

	// /** Session的映射 */
	private static final Map<String, ClientSession> SESSION_MAP = new HashMap<String, ClientSession>();

	public static void createTunnels(SshSession sessionInfoBO, List<SshTunnel> tunnelBOs) {
		for (SshTunnel tunnelBO : tunnelBOs)
			createTunnel(sessionInfoBO, tunnelBO);
	}

	public static void createTunnel(SshSession sessionInfoBO, SshTunnel tunnelBO) {

		String keyStr = String.format("%s@%s:%d", sessionInfoBO.getUserName(), sessionInfoBO.getHost(),
				sessionInfoBO.getPort());

		ClientSession session = SESSION_MAP.get(keyStr);

		if (session == null || session.isClosed()) {

			SshClient client = SshClient.setUpDefaultClient();
			client.start();
			client.addSessionListener(new SshSessionListener(sessionInfoBO));

			try {

				session = client.connect(sessionInfoBO.getUserName(), sessionInfoBO.getHost(), sessionInfoBO.getPort())
						.verify(7L, TimeUnit.SECONDS).getSession();

				session.addPasswordIdentity(sessionInfoBO.getUserPassword());
				session.auth().verify(5L, TimeUnit.SECONDS);

				// 如果登陆成功就记录session
				if (session.isAuthenticated())
					SESSION_MAP.put(keyStr, session);

				session.addPortForwardingEventListener(new SshPortForwardListener(sessionInfoBO));

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
