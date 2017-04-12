package com.aiziyuer.bundle.models.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.SWTResourceManager;

import com.aiziyuer.bundle.models.SshSession;
import com.aiziyuer.bundle.models.SshTunnel;

public class SshSessoinLabelProvider extends LabelProvider {

	public Image getImage(Object element) {

		if (element instanceof SshSession) {
			SshSession sshSession = (SshSession) element;
			switch (sshSession.getStatus()) {
			case 0:
				return SWTResourceManager.getImage(getClass(), "icons/connecting16x16.png");
			case 1:
				return SWTResourceManager.getImage(getClass(), "icons/connect_success16x16.png");
			}
		}

		if (element instanceof SshTunnel) {
			SshTunnel sshTunnel = (SshTunnel) element;
			switch (sshTunnel.getStatus()) {
			case 0:
				return SWTResourceManager.getImage(getClass(), "icons/tunnel_unknown16x16.png");
			case 1:
				return SWTResourceManager.getImage(getClass(), "icons/tunnel_success16x16.png");
			}
		}

		return super.getImage(element);
	}

	public String getText(Object element) {

		if (element instanceof SshSession)
			return ((SshSession) element).getLabel();

		if (element instanceof SshTunnel)
			return ((SshTunnel) element).getLabel();

		return super.getText(element);
	}

}