package com.aiziyuer.bundle.common;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.xwt.DefaultLoadingContext;
import org.eclipse.xwt.IConstants;
import org.eclipse.xwt.IXWTLoader;
import org.eclipse.xwt.XWT;

public class CompositesFactory {

	public static <T extends AbstractComposite> Composite buildUI(Composite parent, Class<T> klass,
			Object dataContext) {

		AbstractComposite area = null;

		XWT.setLoadingContext(new DefaultLoadingContext(klass.getClassLoader()));

		// load XWT
		String name = klass.getSimpleName() + IConstants.XWT_EXTENSION_SUFFIX;
		try {

			URL url = klass.getResource(name);
			Map<String, Object> options = new HashMap<String, Object>();
			options.put(IXWTLoader.CONTAINER_PROPERTY, parent);
			options.put(IXWTLoader.DATACONTEXT_PROPERTY, dataContext);

			area = (AbstractComposite) XWT.loadWithOptions(url, options);
			area.doLast();

		} catch (Throwable e) {
//			log.error("Unable to load " + name, e);
		}

		return area;
	}

}
