package com.aiziyuer.framework.common.ui;

import org.apache.commons.beanutils.PropertyUtils;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;

public class CommonTableLabelProvider implements ITableLabelProvider {

	protected TableViewer tv;

	public CommonTableLabelProvider(TableViewer tv) {
		super();
		this.tv = tv;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {

		String ret = null;

		try {
			ret = String
					.valueOf(PropertyUtils.getProperty(element, String.valueOf(tv.getColumnProperties()[columnIndex])));
		} catch (Exception e) {
//			log.error(e);
		}

		if (ret == null);
//			log.warn(String.format("cannot handle element:%s, columnIndex:%d.", String.valueOf(element), columnIndex));

		return ret;
	}

}
