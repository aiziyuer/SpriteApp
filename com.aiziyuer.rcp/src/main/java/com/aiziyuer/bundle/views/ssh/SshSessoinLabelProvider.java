package com.aiziyuer.bundle.views.ssh;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.SWTResourceManager;

import com.aiziyuer.bundle.models.ssh.SshSession;
import com.aiziyuer.bundle.models.ssh.SshTunnel;

public class SshSessoinLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {

		if (element instanceof SshSession) {
			SshSession sshSession = (SshSession) element;
			switch (sshSession.getStatus()) {
			case DIS_CONNECTED:
				return SWTResourceManager.getImage(getClass(), "icons/connecting16x16.png");
			case CONNECTED:
				return SWTResourceManager.getImage(getClass(), "icons/connect_success16x16.png");
			default:
				return SWTResourceManager.getImage(getClass(), "icons/connecting16x16.png");
			}
		}

		if (element instanceof SshTunnel) {
			SshTunnel sshTunnel = (SshTunnel) element;
			switch (sshTunnel.getStatus()) {
			case DIS_CONNECTED:
				return SWTResourceManager.getImage(getClass(), "icons/tunnel_unknown16x16.png");
			case CONNECTED:
				return SWTResourceManager.getImage(getClass(), "icons/tunnel_success16x16.png");
			default:
				return SWTResourceManager.getImage(getClass(), "icons/tunnel_unknown16x16.png");
			}
		}

		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {

		if (element instanceof SshSession)
			return ((SshSession) element).getLabel();

		if (element instanceof SshTunnel)
			return ((SshTunnel) element).getLabel();

		return super.getText(element);
	}

}