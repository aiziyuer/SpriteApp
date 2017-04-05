package com.aiziyuer.framework.common.ui;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

/**
 *
 */
public class CommonTableCellModifier implements ICellModifier {

	private TableViewer tv;

	public CommonTableCellModifier(TableViewer tv) {
		super();
		this.tv = tv;
	}

	@Override
	public boolean canModify(Object element, String property) {
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {

		Object ret = null;

		try {
			ret = PropertyUtils.getProperty(element, property);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
		}

		return ret;
	}

	@Override
	public void modify(Object element, String property, Object value) {

		try {

			// 如果数据没有发生改变则不作处理直接返回
			TableItem item = (TableItem) element;
			if (StringUtils.equalsIgnoreCase(item.getText(), String.valueOf(value))) {
				return;
			}

			Object data = item.getData();

			// 通过反射修改原始的数据
			PropertyUtils.setProperty(data, property, value);

			tv.refresh();

		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
		}

	}
}
