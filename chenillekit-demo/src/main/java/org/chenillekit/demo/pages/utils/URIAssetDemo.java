package org.chenillekit.demo.pages.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.corelib.components.OutputRaw;
import org.apache.tapestry5.ioc.annotations.Inject;

import org.chenillekit.demo.components.LeftSideMenu;

/**
 * @author <a href="mailto:homburgs@googlemail.com">sven</a>
 * @version $Id$
 */
public class URIAssetDemo
{
	@Component(parameters = {"menuName=utils"})
	private LeftSideMenu menu;

	@Inject
	@Path("uri:http://www.chenillekit.org/css/print.css")
	private Asset testAsset1;

	@Inject
	@Path("uri:file:///home/sven/xx.sh")
	private Asset testAsset2;

	@Component(parameters = {"value=asset1Content"})
	private OutputRaw asset1Output;

	public String getAsset1Content() throws IOException
	{
		return streamToString(testAsset1);
	}

	public String getAsset2Content() throws IOException
	{
		return streamToString(testAsset2);
	}

	private String streamToString(Asset asset) throws IOException
	{
		StringBuffer stringBuffer = new StringBuffer();
		String readedLine;
		LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(asset.getResource().openStream()));

		while ((readedLine = lineNumberReader.readLine()) != null)
			stringBuffer.append(readedLine).append("<br/>");

		return stringBuffer.toString();
	}
}
