package com.aiziyuer.bundle.models.views;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.aiziyuer.bundle.models.SshSession;

public class SshSessionTreeContentProvider implements ITreeContentProvider {

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List)
			return ((List<?>) inputElement).toArray();
		return new Object[0];
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof SshSession) {
			SshSession sshSession = (SshSession) parentElement;
			return sshSession.getTunnels().toArray();
		}
		return new Object[0];
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}
}