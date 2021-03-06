package com.aiziyuer.bundle.framework.common.ui;

import org.eclipse.swt.SWT;

/**
 * 对话框的抽象类, 每个对话框都会有一个结果
 *
 */
public class AbstractWindow {

	/** 对话框的返回值标示对话框的确认结果 */
	protected int result = SWT.OK;
	

	public int getResult() {
		return result;
	}

	/**
	 * 在完成xwt界面布局后再做一次布局调整
	 */
	protected void reLayout() {
	}

	/**
	 * 添加监听器
	 */
	protected void addListener() {
	}

	/**
	 * 添加数据绑定
	 */
	protected void addDataBinding() {
	}

	/**
	 * 数据初始化
	 */
	protected void dataInit() {

	}

	public final void doLast() {

		dataInit();

		reLayout();

		addListener();

		addDataBinding();
	}
}
