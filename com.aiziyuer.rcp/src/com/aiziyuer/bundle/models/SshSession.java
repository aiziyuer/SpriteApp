package com.aiziyuer.bundle.models;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.aiziyuer.framework.models.AbstractModel;

public class SshSession extends AbstractModel {

	/** 别名 */
	private String alias;

	/** 目标主机 */
	private String host;

	/** 目标主机端口 */
	private int port = 22;

	/** 连接用户名 */
	private String userName;

	/** 连接用密码 */
	private String userPassword;

	/** ssh连接状态 */
	private int status;

	private transient List<SshTunnel> tunnels;

	public String getLabel() {
		if (StringUtils.isNotEmpty(alias))
			return alias;
		return String.format("%s@%s:%d", userName, host, port);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<SshTunnel> getTunnels() {
		return tunnels;
	}

	public void setTunnels(List<SshTunnel> tunnels) {
		this.tunnels = tunnels;
	}

}
