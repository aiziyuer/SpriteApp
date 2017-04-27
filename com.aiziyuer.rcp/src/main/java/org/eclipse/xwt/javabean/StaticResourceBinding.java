/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.xwt.javabean;

import java.util.Map;

import org.eclipse.swt.widgets.Widget;
import org.eclipse.xwt.XWTException;
import org.eclipse.xwt.core.IBinding;
import org.eclipse.xwt.internal.utils.UserData;

public class StaticResourceBinding implements IBinding {
	protected Object widget;
	protected String key;

	public StaticResourceBinding(Object widget, String key) {
		this.widget = widget;
		this.key = key;
	}

	public Object getValue(Class<?> targetType) {
		Widget parent = UserData.getWidget(widget);
		while (parent != null) {
			Map<String, Object> dico = UserData.getLocalResources(parent);
			if (dico != null && dico.containsKey(key)) {
				Object data = dico.get(key);
				if (data instanceof IBinding) {
					return ((IBinding) data).getValue(targetType);
				}
				return data;
			}
			parent = (Widget) UserData.getParent(parent);
		}
		throw new XWTException("Key " + key + " is not found.");			
	}
	
	public void reset() {
	}
}
