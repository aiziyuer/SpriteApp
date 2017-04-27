package com.aiziyuer.bundle.framework.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean2;

/**
 * 带过滤功能的BeanUtils
 * 
 * @author aiziyuer
 *
 */
public class CustomizedBeanUtilsBean extends BeanUtilsBean2 {

	/** 需要过滤的字段名称合集 */
	private final Set<String> ignoredFieldNameSet;

	/**
	 * 带过滤功能的BeanUtils
	 * 
	 * @param ignoredFieldNames
	 *            需要过滤的字段名称
	 */
	public CustomizedBeanUtilsBean(String... ignoredFieldNames) {
		this.ignoredFieldNameSet = new HashSet<String>(Arrays.asList(ignoredFieldNames));
	}

	@Override
	public void copyProperty(Object bean, String name, Object value)
			throws IllegalAccessException, InvocationTargetException {

		// 如果是需要过滤的属性就过滤
		if (ignoredFieldNameSet.contains(name))
			return;

		super.copyProperty(bean, name, value);
	}

}
