package com.aiziyuer.bundle.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.aiziyuer.bundle.common.CompositesFactory;

public class SshSessionViewPart extends ViewPart {

	public static final String ID = "com.aiziyuer.bundle.views.SshSessionViewPart"; //$NON-NLS-1$
	IToolBarManager toolbarManager;
	IMenuManager menuManager;

	public SshSessionViewPart() {
	}

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		
		CompositesFactory.buildUI(parent, SshSessionComposite.class, null);

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		toolbarManager = getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		menuManager = getViewSite().getActionBars().getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
}
