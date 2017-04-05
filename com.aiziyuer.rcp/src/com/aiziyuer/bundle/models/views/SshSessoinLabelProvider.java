package com.aiziyuer.bundle.models.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.aiziyuer.bundle.models.SshSession;
import com.aiziyuer.bundle.models.SshTunnel;

public class SshSessoinLabelProvider extends LabelProvider {

	public Image getImage(Object element) {
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