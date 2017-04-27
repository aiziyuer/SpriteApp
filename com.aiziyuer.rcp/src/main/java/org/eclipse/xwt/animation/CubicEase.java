/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.xwt.animation;

import org.eclipse.xwt.XWTException;

/**
 * 
 * @author yyang
 */
public class CubicEase extends EasingFunctionBase {
	public double ease(double normalizedTime) {
		switch (getEasingMode()) {
		case EaseIn:
			return normalizedTime * normalizedTime * normalizedTime;
		case EaseOut:
			normalizedTime = 1 - normalizedTime;
			return 1 - normalizedTime * normalizedTime * normalizedTime;
		case EaseInOut:
			normalizedTime /= 0.5;
			if (normalizedTime < 1)
				return normalizedTime * normalizedTime * normalizedTime / 2;
			normalizedTime = 2 - normalizedTime;
			return ( 2 - normalizedTime * normalizedTime * normalizedTime) / 2;
		default:
			throw new XWTException(getEasingMode().name() + " is supported.");
		}
	}
}