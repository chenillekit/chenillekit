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

package org.chenillekit.demo.pages.tapcomp;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.demo.utils.PersonBean;
import org.chenillekit.tapestry.core.components.BeanSelect;

/**
 * @author <a href="mailto:homburgs@googlemail.com">homburgs</a>
 * @version $Id$
 */
public class BeanSelectDemo
{
	@Persist
	private PersonBean selectedBean;

	@Component(parameters = {"menuName=demo"})
	private LeftSideMenu menu;

	@Component(parameters = {"blankOption=literal:NEVER", "list=someBeans", "labelField=name", "valueField=id",
			"value=selectedBean"})
	private BeanSelect beanSelect;

	private List<PersonBean> someBeans;

	void setupRender()
	{
		someBeans = new ArrayList<PersonBean>(10);

		for (int x = 0; x < 10; x++)
			someBeans.add(new PersonBean(x, "Person_" + x, "city" + x));
	}

	public PersonBean getSelectedBean()
	{
		return selectedBean;
	}

	public void setSelectedBean(PersonBean selectedBean)
	{
		this.selectedBean = selectedBean;
	}

	public boolean isSelectedBeanNotNull()
	{
		return selectedBean != null;
	}

	public List<PersonBean> getSomeBeans()
	{
		return someBeans;
	}
}