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
package org.eclipse.xwt.javabean.metadata;

import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.xwt.IXWTLoader;
import org.eclipse.xwt.internal.core.Binding;
import org.eclipse.xwt.internal.core.DynamicBinding;
import org.eclipse.xwt.javabean.metadata.properties.TableItemProperty;
import org.eclipse.xwt.jface.JFacesHelper;

/**
 * 
 * @author yyang (yves.yang@soyatec.com)
 */
public class BindingMetaclass extends Metaclass {
	public BindingMetaclass(IXWTLoader xwtLoader) {
		this(Binding.class, xwtLoader);
	}

	public BindingMetaclass(Class<?> type, IXWTLoader xwtLoader) {
		super(type, null, xwtLoader);
	}

	@Override
	public Object newInstance(Object[] parameters) {
		DynamicBinding newInstance = (DynamicBinding) super.newInstance(parameters);
		if (JFacesHelper.isViewer(parameters[0]))
			newInstance.setControl(parameters[0]);
		else if (parameters[0] instanceof Control)
			newInstance.setControl((Control) parameters[0]);
		else if (parameters[0] instanceof TableItemProperty.Cell)
			newInstance.setControl(((TableItemProperty.Cell) parameters[0]).getParent());
		else if (parameters[0] instanceof Item)
			newInstance.setControl((Item) parameters[0]);
		else if (parameters[0] instanceof ViewerColumn) {
			newInstance.setControl((ViewerColumn) parameters[0]);
		}
		newInstance.setXWTLoader(xwtLoader);
		return newInstance;
	}
}
