/*******************************************************************************
 * Copyright (c) 2014 CEA and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Christian W. Damus (CEA) - Initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.xwt.internal.xml;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * The default implementation of the cache of parsed XML elements for XWT source files.
 */
public class DefaultElementCache implements IElementCache {

	/** The default cache size (currently {@value}). */
	static int DEFAULT_CACHE_SIZE = 128;

	private final int cacheSize;

	private final Map<String, Element> cache = new LinkedHashMap<String, Element>() {

		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Map.Entry<String, Element> eldest) {
			return size() > cacheSize;
		}
	};


	/**
	 * Initializes me with a cache of the {@linkplain #DEFAULT_CACHE_SIZE default size}.
	 */
	public DefaultElementCache() {
		this(DEFAULT_CACHE_SIZE);
	}

	/**
	 * Initializes me with a specific size of cache.
	 * 
	 * @param cacheSize
	 *        the maximal number of cached XML elements to keep
	 * 
	 * @throws IllegalArgumentException
	 *         if the {@code cacheSize} is not positive
	 */
	public DefaultElementCache(int cacheSize) {
		super();

		if (cacheSize <= 0) {
			throw new IllegalArgumentException("non-positive cacheSize"); //$NON-NLS-1$
		}

		this.cacheSize = cacheSize;
	}

	public Element getElement(URL url) {
		return cache.get(url.toExternalForm());
	}

	public void cache(URL url, Element element) {
		cache.put(url.toExternalForm(), element);
	}
}
