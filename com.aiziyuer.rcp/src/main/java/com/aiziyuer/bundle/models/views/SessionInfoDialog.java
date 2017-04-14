package com.aiziyuer.bundle.models.views;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xwt.XWT;
import org.eclipse.xwt.annotation.UI;
import org.eclipse.xwt.converters.ObjectToString;
import org.eclipse.xwt.converters.StringToInteger;

import com.aiziyuer.bundle.framework.common.ui.AbstractWindow;
import com.aiziyuer.bundle.models.SshSession;

public class SessionInfoDialog extends AbstractWindow {

	private Logger log = Logger.getLogger(getClass());

	@UI
	private Shell shell;

	@UI
	private Text sessionPortText;

	private SshSession modle;

	@Override
	protected void dataInit() {
		modle = (SshSession) XWT.getDataContext(shell);
		log.info("data init fininshed.");
	}

	@Override
	protected void addListener() {

	}

	@Override
	protected void addDataBinding() {

		XWT.getBindingContext(shell).bindValue(WidgetProperties.text(SWT.Modify).observe(sessionPortText),
				BeanProperties.value("port").observe(modle),
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE).setConverter(new StringToInteger()),
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE)
						.setConverter(new ObjectToString(Integer.class)));
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
