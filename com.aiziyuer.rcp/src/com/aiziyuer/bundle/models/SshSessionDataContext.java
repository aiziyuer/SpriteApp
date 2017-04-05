package com.aiziyuer.bundle.models;

import org.eclipse.core.databinding.observable.list.WritableList;

public class SshSessionDataContext {
	
	private final WritableList sshSesssionList = new WritableList();
	
	public WritableList getSshSesssionList() {
		return sshSesssionList;
	}

}
