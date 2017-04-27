package com.aiziyuer.bundle.manager.ssh;

import java.io.IOException;
import java.util.HashMap;
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

	/**
	 * 根据传入的BO对象信息来获取session(优先从缓存中获取)
	 * 
	 * @param sessionInfoBO
	 *            session的连接参数信息
	 */
	public static ClientSession startSession(SshSession sessionInfoBO) {
		return getSession(sessionInfoBO, true);
	}

	/**
	 * 根据传入的BO对象信息来获取session(优先从缓存中获取)
	 * 
	 * @param sessionInfoBO
	 *            session的连接参数信息
	 * @param force
	 *            是否强制获取一个session, 及没有缓存就创建一个放入缓存
	 */
	private static ClientSession getSession(SshSession sessionInfoBO, boolean force) {

		String keyStr = String.format("%s@%s:%d", sessionInfoBO.getUserName(), sessionInfoBO.getHost(),
				sessionInfoBO.getPort());

		ClientSession session = SESSION_MAP.get(keyStr);

		// 如果是强制需要获取一个Session, 则在session为空的情况下新创建一个session
		if (force && (session == null || session.isClosed())) {

			SshClient client = SshClient.setUpDefaultClient();
			client.start();
			client.addSessionListener(new SshSessionListener(sessionInfoBO));
			client.addPortForwardingEventListener(new SshPortForwardListener(sessionInfoBO));

			try {

				session = client.connect(sessionInfoBO.getUserName(), sessionInfoBO.getHost(), sessionInfoBO.getPort())
						.verify(7L, TimeUnit.SECONDS).getSession();

				session.addPasswordIdentity(sessionInfoBO.getUserPassword());
				session.auth().verify(5L, TimeUnit.SECONDS);

				// 如果登陆成功就记录session
				if (session.isAuthenticated())
					SESSION_MAP.put(keyStr, session);

			} catch (IOException e) {
				log.error("createSession has error.", e);
				IoUtils.closeQuietly(client);
			}

		}

		return session;
	}

	/**
	 * 启动隧道
	 * 
	 * @param sessionInfoBO
	 *            session的连接参数信息
	 * @param tunnelBO
	 *            隧道的连接参数信息
	 */
	public static void startTunnel(SshSession sessionInfoBO, SshTunnel tunnelBO) {

		ClientSession session = getSession(sessionInfoBO, true);
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

	/**
	 * 根据传入的session连接信息停止Session
	 * 
	 * @param sessionInfoBO
	 *            session的连接参数信息
	 */
	public static void stopSession(SshSession sessionInfoBO) {
		ClientSession session = getSession(sessionInfoBO, false);
		if (session == null) {
			log.warn("session is null, ignore stopSession.");
			return;
		}

		IoUtils.closeQuietly(session);

	}

	/**
	 * 根据传入的参数停止隧道
	 * 
	 * @param sessionInfoBO
	 *            session的连接参数信息
	 * @param tunnelBO
	 *            隧道的连接参数信息
	 */
	public static void stopTunnel(SshSession sessionInfoBO, SshTunnel tunnelBO) {
		ClientSession session = getSession(sessionInfoBO, false);
		if (session == null) {
			log.warn("session is null, ignore stopSession.");
			return;
		}

		if (tunnelBO == null) {
			log.warn("tunnelBO is null, ignore stopTunnel.");
			return;
		}

		try {
			if (tunnelBO.isLocal())
				session.stopLocalPortForwarding(
						new SshdSocketAddress(tunnelBO.getLocalTunnelHost(), tunnelBO.getLocalTunnelPort()));
			else
				session.stopRemotePortForwarding(
						new SshdSocketAddress(tunnelBO.getRemoteTunnelHost(), tunnelBO.getRemoteTunnelPort()));
		} catch (IOException e) {
			log.error("stopTunnel has error.", e);
		}
	}

}
