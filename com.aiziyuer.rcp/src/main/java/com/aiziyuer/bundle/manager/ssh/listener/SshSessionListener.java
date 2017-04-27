package com.aiziyuer.bundle.manager.ssh.listener;

import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aiziyuer.bundle.models.ssh.ConnectStatus;
import com.aiziyuer.bundle.models.ssh.SshSession;

public class SshSessionListener implements SessionListener {

	Logger log = LoggerFactory.getLogger(getClass());

	private final SshSession sessionInfoBO;

	public SshSessionListener(SshSession sessionInfoBO) {
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