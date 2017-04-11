package com.aiziyuer.bundle.models.views;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.xwt.XWT;
import org.eclipse.xwt.annotation.UI;

import com.aiziyuer.bundle.models.SshSession;
import com.aiziyuer.bundle.models.SshSessionDataContext;
import com.aiziyuer.framework.common.ui.AbstractComposite;

public class SshSessionComposite extends AbstractComposite {

	@UI
	private TreeViewer sessionTreeViewer;

	private SshSessionDataContext context;

	public SshSessionComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout());
	}

	@Override
	protected void dataInit() {
		context = (SshSessionDataContext) XWT.getDataContext(this);
	}

	@Override
	protected void addListener() {

	}

	@Override
	protected void addDataBinding() {

		XWT.getBindingContext(this).bindValue(ViewersObservables.observeSingleSelection(sessionTreeViewer),
				BeanProperties.value("singleSelectItems").observe(context));

		XWT.getBindingContext(this).bindList(ViewersObservables.observeMultiPostSelection(sessionTreeViewer),
				context.getMultiSelectedItems());

	}

	public void onMouseDoubleClick(Event event) {


		Object selectedItem = context.getSingleSelectItems();
		if(selectedItem instanceof SshSession)
		{
			((SshSession) selectedItem).setStatus(1);
		}

		sessionTreeViewer.refresh();

	}
}
