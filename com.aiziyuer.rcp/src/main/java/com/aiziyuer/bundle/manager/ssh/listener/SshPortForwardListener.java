package com.aiziyuer.bundle.manager.ssh.listener;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.sshd.common.forward.PortForwardingEventListener;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aiziyuer.bundle.models.ssh.ConnectStatus;
import com.aiziyuer.bundle.models.ssh.SshSession;
import com.aiziyuer.bundle.models.ssh.SshTunnel;

public class SshPortForwardListener implements PortForwardingEventListener {
	
	Logger log = LoggerFactory.getLogger(getClass());

	private final SshSession sessionInfoBO;

	public SshPortForwardListener(SshSession sessionInfoBO) {
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