package com.aiziyuer.bundle.models.views;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.xwt.XWT;
import org.eclipse.xwt.annotation.UI;

import com.aiziyuer.bundle.manager.SshSessionManager;
import com.aiziyuer.bundle.models.SshSession;
import com.aiziyuer.bundle.models.SshSessionDataContext;
import com.aiziyuer.bundle.models.SshTunnel;
import com.aiziyuer.framework.common.ui.AbstractComposite;

public class SshSessionComposite extends AbstractComposite {

	private Logger log = Logger.getLogger(getClass());

	@UI
	private TreeViewer sessionTreeViewer;

	@UI
	private MenuItem connectMenuItem, addMenuItem, editMenuItem, deleteMenuItem, disconnectMenuItem;

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

		connectMenuItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				log.info("connectMenuItem clicked.");

				Object selectedItem = context.getSingleSelectItems();
				if (selectedItem instanceof SshSession) {
					SshSession sshSession = (SshSession) selectedItem;
					SshSessionManager.INSTANCE.createTunnel(sshSession, null);
					sessionTreeViewer.refresh(sshSession);
				}

				else if (selectedItem instanceof SshTunnel) {
					SshTunnel sshTunnel = (SshTunnel) selectedItem;

					SshSessionManager.INSTANCE.createTunnel(sshTunnel.getSshSession(), sshTunnel);
					sessionTreeViewer.refresh(sshTunnel.getSshSession());
				}
			}

		});

		addMenuItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				log.info("addMenuItem clicked.");
			}

		});

		editMenuItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				log.info("editMenuItem clicked.");
			}

		});

		deleteMenuItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				log.info("deleteMenuItem clicked.");
			}

		});

		disconnectMenuItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				log.info("disconnectMenuItem clicked.");
			}

		});

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

		ITreeContentProvider provider = (ITreeContentProvider) sessionTreeViewer.getContentProvider();
		if (!provider.hasChildren(selectedItem))
			return;

		if (sessionTreeViewer.getExpandedState(selectedItem))
			sessionTreeViewer.collapseToLevel(selectedItem, AbstractTreeViewer.ALL_LEVELS);
		else
			sessionTreeViewer.expandToLevel(selectedItem, 1);
	}
}
