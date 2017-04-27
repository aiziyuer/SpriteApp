/*******************************************************************************
 * Copyright (c) 2006, 2015 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.xwt.core;

/**
 * @author Nguyen Viet Hoa from Soyatec
 * 
 */
public class ValidationStatus {

	/** reference to the parent Binding. */
	private Object parent;

	public Object getParent() {
		return parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	/**
	 * The direct widget that contains the validation status
	 */
	private Object control;

	/**
	 * The name of the binding whose binding context's status will be the source
	 * data of the validationstatus's binding
	 */
	private String sourceName;

	public Object getControl() {
		return control;
	}

	public void setControl(Object control) {
		this.control = control;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
}
