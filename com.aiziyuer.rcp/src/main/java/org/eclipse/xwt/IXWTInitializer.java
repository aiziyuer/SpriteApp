package org.eclipse.xwt;

public interface IXWTInitializer {
	void initialize(IXWTLoader loader);
	
	boolean isInitialized();
}
