package com.aiziyuer.bundle.models;

import org.eclipse.core.databinding.observable.list.WritableList;

import com.aiziyuer.bundle.framework.models.AbstractModel;

public class SshSessionDataContext extends AbstractModel{
	
	private final WritableList sshSesssionList = new WritableList();
	
	private final WritableList multiSelectedItems = new WritableList();
	
	private Object singleSelectItems;
	
	public WritableList getSshSesssionList() {
		return sshSesssionList;
	}

	public Object getSingleSelectItems() {
		return singleSelectItems;
	}

	public void setSingleSelectItems(Object singleSelectItems) {
		this.singleSelectItems = singleSelectItems;
	}

	public WritableList getMultiSelectedItems() {
		return multiSelectedItems;
	}
	
}
