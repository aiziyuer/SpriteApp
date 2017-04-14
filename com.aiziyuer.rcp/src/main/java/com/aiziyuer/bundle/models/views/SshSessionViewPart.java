package com.aiziyuer.bundle.models.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.aiziyuer.bundle.framework.common.ui.CompositesFactory;
import com.aiziyuer.bundle.models.SshSession;
import com.aiziyuer.bundle.models.SshSessionDataContext;
import com.aiziyuer.bundle.models.SshTunnel;

public class SshSessionViewPart extends ViewPart {

	private SshSessionDataContext context = new SshSessionDataContext();

	public SshSessionViewPart() {

	}

	public static final String ID = "com.aiziyuer.bundle.views.SshSessionViewPart"; //$NON-NLS-1$
	IToolBarManager toolbarManager;
	IMenuManager menuManager;

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {

		List<SshSession> sshSessions = new ArrayList<SshSession>();
		{
			SshSession sshSession = new SshSession();
			sshSessions.add(sshSession);

			sshSession.setHost("127.0.0.1");
			sshSession.setPort(32768);
			sshSession.setUserName("root");
			sshSession.setUserPassword("root");

			SshTunnel sshTunnel = new SshTunnel();
			sshTunnel.setSshSession(sshSession);
			sshTunnel.setLocalTunnelHost("localhost");
			sshTunnel.setLocalTunnelPort(8080);
			sshTunnel.setRemoteTunnelHost("localhost");
			sshTunnel.setRemoteTunnelPort(8080);
			sshTunnel.setLocal(false);

			List<SshTunnel> sshTunnels = new ArrayList<SshTunnel>();
			sshTunnels.add(sshTunnel);
			sshSession.setTunnels(sshTunnels);
		}
		
		{
			SshSession sshSession = new SshSession();
			sshSessions.add(sshSession);

			sshSession.setHost("127.0.0.1");
			sshSession.setPort(23);
			sshSession.setUserName("lc");
			sshSession.setUserPassword("password");

			SshTunnel sshTunnel = new SshTunnel();
			sshTunnel.setSshSession(sshSession);
			sshTunnel.setLocalTunnelHost("localhost");
			sshTunnel.setLocalTunnelPort(8081);
			sshTunnel.setRemoteTunnelHost("localhost");
			sshTunnel.setRemoteTunnelPort(8081);
			sshTunnel.setLocal(true);

			List<SshTunnel> sshTunnels = new ArrayList<SshTunnel>();
			sshTunnels.add(sshTunnel);
			sshSession.setTunnels(sshTunnels);
		}
		
		{
			SshSession sshSession = new SshSession();
			sshSessions.add(sshSession);

			sshSession.setHost("127.0.0.1");
			sshSession.setPort(24);
			sshSession.setUserName("lc");
			sshSession.setUserPassword("password");

			SshTunnel sshTunnel = new SshTunnel();
			sshTunnel.setSshSession(sshSession);
			sshTunnel.setLocalTunnelHost("localhost");
			sshTunnel.setLocalTunnelPort(8082);
			sshTunnel.setRemoteTunnelHost("localhost");
			sshTunnel.setRemoteTunnelPort(8082);
			sshTunnel.setLocal(true);

			List<SshTunnel> sshTunnels = new ArrayList<SshTunnel>();
			sshTunnels.add(sshTunnel);
			sshSession.setTunnels(sshTunnels);
		}

		context.getSshSesssionList().addAll(sshSessions);

		CompositesFactory.buildUI(parent, SshSessionComposite.class, context);

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
