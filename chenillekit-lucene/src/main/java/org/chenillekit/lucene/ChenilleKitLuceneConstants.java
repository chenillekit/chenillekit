/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.lucene;

import org.apache.lucene.analysis.Analyzer;

/**
 *
 *
 * @version $Id$
 */
public class ChenilleKitLuceneConstants
{
	/**
	 * File name for configuring Lucene services
	 */
	public static final String CONFIG_KEY_PROPERTIES = "lucene.properties";
	
	/**
	 * Folder name where to store the index
	 */
    public static final String PROPERTIES_KEY_IF = "search.index.folder";
    
    /**
     * Property to override an already present index in the index store directory
     */
    public static final String PROPERTIES_KEY_OIF = "search.overwrite.index.folder";
    
    /**
     * The fully qualified class name to use as the {@link Analyzer}
     * for index operations
     */
    public static final String PROPERTIES_KEY_ACN = "search.analyzer.class.name";
    
    /**
     * The max field length value for index stored properties.
     */
    public static final String PROPERTIES_KEY_MFL = "search.max.field.length";
    
    /**
     * Enable or disable Lucene's output
     */
    public static final String PROPERTIES_KEY_ELO = "enable.lucene.log.output";

}
