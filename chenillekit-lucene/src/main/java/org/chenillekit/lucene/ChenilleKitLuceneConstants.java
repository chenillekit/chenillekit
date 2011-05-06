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

import org.apache.lucene.queryParser.QueryParser;
import org.apache.tapestry5.ioc.services.FactoryDefaults;

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
     * The max field length value for index stored properties.
     */
    public static final String PROPERTIES_KEY_MFL = "search.max.field.length";
    
    /**
     * Enable or disable Lucene's output
     */
    public static final String PROPERTIES_KEY_ELO = "enable.lucene.log.output";
    
    /**
     * The version which the Lucene {@link QueryParser} should be compatibile with.
     * By default {@link FactoryDefaults} is <code>LUCENE_CURRENT</code>.
     */
    public static final String LUCENE_COMPATIBILITY_VERSION = "ck.lucene.compatibility.version";
    
    /**
     * Determine if Lucene's NRT capabilities should be used or not.
     * For more info see:
     * <a href="http://wiki.apache.org/lucene-java/NearRealtimeSearch">
     * http://wiki.apache.org/lucene-java/NearRealtimeSearch
     * </a>
     */
    public static final String LUCENE_NEAR_REAL_TIME = "ck.lucene.near-real-time";

}
