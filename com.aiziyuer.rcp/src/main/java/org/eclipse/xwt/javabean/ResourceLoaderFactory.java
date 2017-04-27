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

import org.eclipse.xwt.IXWTLoader;
import org.eclipse.xwt.core.IElementLoaderFactory;
import org.eclipse.xwt.core.IRenderingContext;
import org.eclipse.xwt.core.IVisualElementLoader;

public class ResourceLoaderFactory implements IElementLoaderFactory {
	public ResourceLoaderFactory() {
	}

	public IVisualElementLoader createElementLoader(IRenderingContext context, IXWTLoader loader) {
		return new ResourceLoader(context, loader);
	}
}
