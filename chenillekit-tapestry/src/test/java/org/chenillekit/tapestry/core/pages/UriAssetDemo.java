package org.chenillekit.tapestry.core.pages;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * @author <a href="mailto:homburgs@googlemail.com">sven</a>
 * @version $Id$
 */
public class UriAssetDemo
{
//	@Component(parameters = {"value=assetContent"})
//	private OutputRaw outputRaw;

	@Inject
	@Path("uri:http://www.google.com")
	private Asset uriAsset;

	public String getClientUrl()
	{
		return uriAsset.toClientURL();
	}

	public String getAssetContent() throws IOException
	{
		Reader r = new InputStreamReader(uriAsset.getResource().openStream());
		StringWriter sw = new StringWriter();

		char[] buffer = new char[1024];
		for (int n; (n = r.read(buffer)) != -1;)
			sw.write(buffer, 0, n);

		return sw.toString();
	}
}
