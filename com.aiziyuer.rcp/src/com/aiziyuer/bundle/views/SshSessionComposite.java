package com.aiziyuer.bundle.views;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.aiziyuer.bundle.common.AbstractComposite;

public class SshSessionComposite extends AbstractComposite {

	public SshSessionComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}

}
