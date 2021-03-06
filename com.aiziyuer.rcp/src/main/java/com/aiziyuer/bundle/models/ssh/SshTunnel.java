package com.aiziyuer.bundle.models.ssh;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.aiziyuer.bundle.framework.models.AbstractModel;

public class SshTunnel extends AbstractModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 别名 */
	private String alias;

	/** 本地隧道主机 */
	private String localTunnelHost;

	/** 本地隧道端口 */
	private int localTunnelPort;

	/**
	 * 是否是正向隧道 <br>
	 * true: 正向隧道,
	 * 相当于<code>ssh -NfL LOCAL_HOST:LOCAL_PORT:REMOTE_HOST:REMOTE_PORT lc@127.0.0.1 -p 2202</code><br>
	 * false: 反向隧道,
	 * 相当于<code>ssh -NfR 8080:127.0.0.1:80 lc@127.0.0.1 -p 2202</code>
	 */
	private boolean local;

	/** 远端隧道主机 */
	private String remoteTunnelHost;

	/** 远端隧道口 */
	private int remoteTunnelPort;

	/** ssh连接状态 */
	private ConnectStatus status = ConnectStatus.DIS_CONNECTED;

	private transient SshSession sshSession;

	public SshTunnel() {

	}

	public SshTunnel(String localTunnelHost, int localTunnelPort, boolean local, String remoteTunnelHost,
			int remoteTunnelPort) {
		this.localTunnelHost = localTunnelHost;
		this.localTunnelPort = localTunnelPort;
		this.local = local;
		this.remoteTunnelHost = remoteTunnelHost;
		this.remoteTunnelPort = remoteTunnelPort;
	}

	public SshSession getSshSession() {
		return sshSession;
	}

	public void setSshSession(SshSession sshSession) {
		this.sshSession = sshSession;
	}

	public ConnectStatus getStatus() {
		return status;
	}

	public void setStatus(ConnectStatus status) {
		Object oldValue = this.status;
		this.status = status;
		propertySupport.firePropertyChange("status", oldValue, this.status);
	}

	public String getLabel() {
		if (StringUtils.isNotEmpty(alias))
			return alias;
		return String.format("%s:%d %s %s:%d", localTunnelHost, localTunnelPort, local ? "->" : "<-", remoteTunnelHost,
				remoteTunnelPort);
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getLocalTunnelHost() {
		return localTunnelHost;
	}

	public void setLocalTunnelHost(String localTunnelHost) {
		this.localTunnelHost = localTunnelHost;
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public String getRemoteTunnelHost() {
		return remoteTunnelHost;
	}

	public void setRemoteTunnelHost(String remoteTunnelHost) {
		this.remoteTunnelHost = remoteTunnelHost;
	}

	public int getLocalTunnelPort() {
		return localTunnelPort;
	}

	public void setLocalTunnelPort(int localTunnelPort) {
		this.localTunnelPort = localTunnelPort;
	}

	public int getRemoteTunnelPort() {
		return remoteTunnelPort;
	}

	public void setRemoteTunnelPort(int remoteTunnelPort) {
		this.remoteTunnelPort = remoteTunnelPort;
	}


}
