package org.eclipse.xwt.internal;

import org.eclipse.xwt.IXWTInitializer;
import org.eclipse.xwt.IXWTLoader;
import org.eclipse.xwt.XWT;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator, IXWTInitializer {

	private boolean initialized;

	public void start(BundleContext context) throws Exception {
		initialized = false;
		XWT.addInitializer(this);
		XWT.runOnUIThread(new Runnable() {
			public void run() {
			}
		});
	}

	public void initialize(IXWTLoader loader) {
		initialized = true;		
	}

	public void stop(BundleContext context) throws Exception {
	}
	
	public boolean isInitialized() {
		return initialized;
	}
}
