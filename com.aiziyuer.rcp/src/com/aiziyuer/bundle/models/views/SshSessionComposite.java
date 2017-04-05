package com.aiziyuer.bundle.models.views;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.aiziyuer.framework.common.ui.AbstractComposite;

public class SshSessionComposite extends AbstractComposite {

	public SshSessionComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}

}
