/*******************************************************************************
 * Copyright (c) 2010, 2011 SAP AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package com.aiziyuer.bundle;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(400, 300));
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
		// configurer.setShowPerspectiveBar(true);
		configurer.setTitle("RCP Application");
	}

	@Override
	public void postWindowCreate() {
		// remove unwanted menu entries
		List<String> unwantedItems = Arrays.asList(//
				"org.eclipse.ui.openLocalFile", //
				"converstLineDelimitersTo", //
				"org.eclipse.ui.cheatsheets.actions.CheatSheetHelpMenuAction", //
				"org.eclipse.debug.ui.actions.BreakpointTypesContribution", //
				"ExternalToolsGroup", //
				"org.eclipse.ui.externaltools.ExternalToolMenuDelegateMenu", //
				"navigate", //
				"org.eclipse.search.menu", //
				"org.eclipse.ui.run" //
		);

		IMenuManager menuManager = getWindowConfigurer().getActionBarConfigurer().getMenuManager();
		removeUnwantedItems(unwantedItems, menuManager);

	}

	private void removeUnwantedItems(final List<String> unwantedItems, final IMenuManager menuManager) {
		IContributionItem[] items = menuManager.getItems();

		for (IContributionItem item : items) {
			if (item instanceof IMenuManager) {
				removeUnwantedItems(unwantedItems, (IMenuManager) item);
			}

			if (unwantedItems.contains(item.getId())) {
				menuManager.remove(item);
			}
		}
	}

}
