package com.aiziyuer.bundle.models.views;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.SelectObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xwt.XWT;
import org.eclipse.xwt.annotation.UI;

import com.aiziyuer.bundle.models.SshTunnel;
import com.aiziyuer.framework.common.ui.AbstractWindow;

public class TunnelInfoDialog extends AbstractWindow {

	private Logger log = Logger.getLogger(getClass());

	@UI
	private Shell shell;

	@UI
	private Text sessionPortText;

	@UI
	private Button okBtn, localBtn, remoteBtn;

	private SshTunnel modle;

	@Override
	protected void dataInit() {
		modle = (SshTunnel) XWT.getDataContext(shell);
		log.info("data init fininshed.");
	}

	@Override
	protected void reLayout() {
		super.reLayout();

		// 对话框默认是确认键
		shell.setDefaultButton(okBtn);
	}

	@Override
	protected void addListener() {

	}

	@Override
	protected void addDataBinding() {

		// 绑定boolean字段的属性到radio的按钮组
		XWT.getBindingContext(shell).bindValue(new SelectObservableValue(Boolean.TYPE) {
			{
				addOption(Boolean.TRUE, WidgetProperties.selection().observe(localBtn));
				addOption(Boolean.FALSE, WidgetProperties.selection().observe(remoteBtn));
			}
		}, PojoProperties.value("local").observe(modle));

	}

	public void onOKButtonSelection(Event event) {
		result = SWT.OK;
		shell.dispose();
	}

	public void onCancelButtonSelection(Event event) {
		result = SWT.CANCEL;
		shell.dispose();
	}
}
