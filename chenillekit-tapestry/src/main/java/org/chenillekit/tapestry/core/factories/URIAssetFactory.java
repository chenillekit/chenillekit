package org.chenillekit.tapestry.core.factories;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.internal.services.ResourceCache;
import org.apache.tapestry5.ioc.Resource;
import static org.apache.tapestry5.ioc.internal.util.CollectionFactory.newConcurrentMap;
import org.apache.tapestry5.services.AssetFactory;

import org.chenillekit.core.resources.URIResource;
import org.chenillekit.tapestry.core.services.URIAssetAliasManager;

/**
 * @version $Id$
 */
public class URIAssetFactory implements AssetFactory
{
	private final ResourceCache cache;

	private final URIAssetAliasManager aliasManager;

	private final Map<Resource, String> resourceToClientURL = newConcurrentMap();

	public URIAssetFactory(final ResourceCache cache,
						   final URIAssetAliasManager aliasManager)
	{
		this.cache = cache;
		this.aliasManager = aliasManager;
	}

	/**
	 * Returns the Resource representing the root folder of the domain this factory is responsible for.
	 */
	public Resource getRootResource()
	{
		return new URIResource("/");
	}

	private String clientURL(Resource resource)
	{
		String clientURL = resourceToClientURL.get(resource);

		if (clientURL == null)
		{
			clientURL = buildClientURL(resource);
			resourceToClientURL.put(resource, clientURL);
		}

		// The path generated is partially request-dependent and therefore can't be cached, it will even
		// vary from request to the next.

		return aliasManager.toClientURL(clientURL);
	}

	private String buildClientURL(Resource resource)
	{
		boolean requiresDigest = cache.requiresDigest(resource);

		String path = null;

		try
		{
			path = URLEncoder.encode(resource.getPath(), "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException(e);
		}

		if (requiresDigest)
		{
			// Resources with extensions go from foo/bar/baz.txt --> foo/bar/baz.CHECKSUM.txt

			int lastdotx = path.lastIndexOf('.');

			path = path.substring(0, lastdotx + 1) + cache.getDigest(resource) + path.substring(lastdotx);
		}

		return path;
	}

	public Asset createAsset(final Resource resource)
	{
		return new Asset()
		{
			public Resource getResource()
			{
				return resource;
			}

			public String toClientURL()
			{
				return clientURL(resource);
			}

			@Override
			public String toString()
			{
				return toClientURL();
			}
		};
	}
}
