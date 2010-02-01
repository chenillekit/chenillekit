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
import org.apache.tapestry5.annotations.Property;

import org.chenillekit.demo.components.LeftSideMenu;
import org.chenillekit.tapestry.core.components.ProtoTube;
import org.chenillekit.tapestry.core.utils.ProtoTubeIdHolder;

/**
 * @version $Id$
 */
public class ProtoTubeDemo
{
	@Property
	private List<ProtoTubeIdHolder> youtubeIds;

	@Component(parameters = {"menuName=demo"})
	private LeftSideMenu menu;

	@Component(parameters = {"youtubeIds=youtubeIds"})
	private ProtoTube protoTube;

	/**
	 * Tapestry render phase method.
	 * Initialize temporary instance variables here.
	 */
	void setupRender()
	{
		youtubeIds = new ArrayList<ProtoTubeIdHolder>();
		youtubeIds.add(new ProtoTubeIdHolder("NTI6qzaGtuY", "Caracho - In Hamburg sagt man JaJa"));
		youtubeIds.add(new ProtoTubeIdHolder("bbMYEPecCqY", "Nena, Olli & Remmler - Ich kann nix dafür"));
		youtubeIds.add(new ProtoTubeIdHolder("hHkhzNLHC4g", "Rammstein & Tatu - Moskau"));
		youtubeIds.add(new ProtoTubeIdHolder("_k-vPv-XEpg", "Rammstein - Keine Lust"));
		youtubeIds.add(new ProtoTubeIdHolder("QHkqIyOtuzU", "Rammstein - Zwitter"));
		youtubeIds.add(new ProtoTubeIdHolder("md2UNybl8PY", "Marilyn Manson - Tainted Love"));
		youtubeIds.add(new ProtoTubeIdHolder("L2GaCnAiuvo", "Depeche Mode - Wrong"));
		youtubeIds.add(new ProtoTubeIdHolder("eBQBUURLtZU", "Großstadtgeflüster - Haufenweise Scheiße by Jenny"));
		youtubeIds.add(new ProtoTubeIdHolder("rznn6bgq5nA", "Marilyn Manson - The Beautiful People"));
	}
}