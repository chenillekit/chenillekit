/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2009 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.tapestry.core.models;

import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.util.AbstractSelectModel;

/**
 * @author <a href="mailto:shomburg@depot120.dpd.de">S.Homburg</a>
 * @version $Id$
 */
public class DaySelectModel extends AbstractSelectModel
{
	/**
	 * The list of groups, each containing some number of individual options.
	 *
	 * @return the groups, or null
	 */
	public List<OptionGroupModel> getOptionGroups()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * The list of ungrouped options, which appear after any grouped options. Generally, a model will either provide
	 * option groups or ungrouped options, but not both.
	 *
	 * @return the ungrouped options, or null
	 */
	public List<OptionModel> getOptions()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
