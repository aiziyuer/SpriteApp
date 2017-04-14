package com.aiziyuer.bundle.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.aiziyuer.bundle.models.SshSession;
import com.aiziyuer.bundle.models.SshTunnel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshSessionManager {

	public static final SshSessionManager INSTANCE = new SshSessionManager();

	private Logger log = Logger.getLogger(getClass());

	private SshSessionManager() {
	}

	/** Session的映射 */
	private Map<String, Session> sessionMap = new HashMap<String, Session>();

	public void createTunnels(SshSession sessionInfoBO, List<SshTunnel> tunnelBOs) {

		for (SshTunnel tunnelBO : tunnelBOs)
			createTunnel(sessionInfoBO, tunnelBO);

	}

	public void createTunnel(SshSession sessionInfoBO, SshTunnel tunnelBO) {

		String name = sessionInfoBO.getUserName();
		String host = sessionInfoBO.getHost();
		int port = sessionInfoBO.getPort();
		String keyStr = String.format("%s@%s:%d", name, host, port);

		Session session = sessionMap.get(keyStr);
		if (session == null) {
			JSch jsch = new JSch();
			try {
				session = jsch.getSession(name, host, port);

				// 设置不检查hostKey
				Properties config = new Properties();
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);

				// 设置密码
				session.setPassword(sessionInfoBO.getUserPassword());

				// 设置超时
				int timeout = 3000;
				session.connect(timeout);

				log.info(String.format("create session(%s) success.", keyStr));

				sessionMap.put(keyStr, session);
				sessionInfoBO.setStatus(1);
			} catch (JSchException e) {
				log.error("create session error:", e);
			}
		}
		
		if(session == null || tunnelBO == null)
			return;

		if (tunnelBO.isLocal())
			createForwardTunnel(session, tunnelBO);
		else
			craeteReverseTunnel(session, tunnelBO);
	}

	/**
	 * 创建一条正向隧道<br>
	 * 
	 * 创建一条隧道(外部可以通过2202访问虚拟机的ssh服务), 使得外部可以访问虚拟机的80端口<br>
	 * <code>ssh -NfL 80:localhost:80 lc@127.0.0.1 -p 2202</code>
	 * 
	 */
	private void createForwardTunnel(Session session, SshTunnel tunnelBO) {

		// 隧道入口
		String localHost = tunnelBO.getLocalTunnelHost();
		int localPort = tunnelBO.getLocalTunnelPort();

		// 隧道出口
		String remoteHost = tunnelBO.getRemoteTunnelHost();
		int remotePort = tunnelBO.getRemoteTunnelPort();

		try {
			int assinged_port = session.setPortForwardingL(localHost, localPort, remoteHost, remotePort);
			log.info(String.format("ForwardTunnel: local(%s:%d) -> remote(%s:%d)", localHost, assinged_port, remoteHost,
					remotePort));
			tunnelBO.setStatus(1);
		} catch (JSchException e) {
			log.error("create tunnel error:", e);
		}
	}

	/**
	 * 创建一条反向隧道<br>
	 * 创建一条反向隧道, 虚拟机可以访问外部的80端口服务<br>
	 * <code>ssh -NfR 8080:localhost:80 lc@127.0.0.1 -p 2202</code>
	 */
	private void craeteReverseTunnel(Session session, SshTunnel tunnelBO) {

		// 隧道入口
		String localHost = tunnelBO.getLocalTunnelHost();
		int localPort = tunnelBO.getLocalTunnelPort();

		// 隧道出口
		String remoteHost = tunnelBO.getRemoteTunnelHost();
		int remotePort = tunnelBO.getRemoteTunnelPort();

		try {
			session.setPortForwardingR(remoteHost, remotePort, localHost, localPort);
			log.info(String.format("ReverseTunnel: local(%s:%d) <- remote(%s:%d)", localHost, localPort, remoteHost,
					remotePort));
			tunnelBO.setStatus(1);
		} catch (JSchException e) {
			log.error("create tunnel error:", e);
		}
	}

}
