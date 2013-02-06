/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2008-2010 by chenillekit.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.chenillekit.demo.services;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Dispatcher;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;

import freemarker.template.Configuration;
import org.chenillekit.core.services.ImageService;
import org.chenillekit.demo.data.Track;
import org.chenillekit.google.ChenilleKitGoogleConstants;
import org.chenillekit.tapestry.core.services.ThumbNailService;
import org.chenillekit.tapestry.core.services.impl.ThumbNailServiceImpl;
import org.chenillekit.tapestry.core.services.impl.URIDispatcher;
import org.chenillekit.template.services.impl.FreeMarkerServiceImpl;
import org.chenillekit.template.services.impl.VelocityServiceImpl;
import org.slf4j.Logger;

/**
 * @version $Id$
 */
public class DemoModule
{
    public void contributeMasterDispatcher(OrderedConfiguration<Dispatcher> configuration,
                                           ObjectLocator locator)
    {
        configuration.add("URIAsset", locator.autobuild(URIDispatcher.class), "after:Asset");
    }

    /**
     * Contributes factory defaults that map be overridden.
     *
     * @param configuration configuration map
     */
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,it,de,fr");
        configuration.add(ChenilleKitGoogleConstants.GOOGLE_REFERER, "www.chenillekit.org");

        boolean isProductionMode = Boolean.valueOf(System.getProperty(SymbolConstants.PRODUCTION_MODE, "true"));
        if (isProductionMode)
            // Google-API Key "www.chenillekit.org"
            configuration.add(ChenilleKitGoogleConstants.GOOGLE_KEY, "ABQIAAAAi_YFwIW0ZYz1tm9NA5dpjxQdr9K2fBzQ2nv81qbqohyufO_eixS_5MtSz15QdDu7FnDcaswUkcFzOQ");
        else
            // Google-API Key "localhost"
            configuration.add(ChenilleKitGoogleConstants.GOOGLE_KEY, "ABQIAAAAi_YFwIW0ZYz1tm9NA5dpjxQgrEgu6vWt7HL-5aFrx0YLtTRf-hQe7xlPB5qe4SL3L7K1LcW221_now");
    }

    public static MusicLibrary buildMusicLibrary(Logger log)
    {
        URL library = DemoModule.class.getResource("iTunes.xml");

        final List<Track> tracks = new MusicLibraryParser(log).parseTracks(library);

        final Map<Long, Track> idToTrack = CollectionFactory.newMap();

        for (Track t : tracks)
        {
            idToTrack.put(t.getId(), t);
        }

        return new MusicLibrary()
        {
            public Track getById(long id)
            {
                Track result = idToTrack.get(id);

                if (result != null) return result;

                throw new IllegalArgumentException(String.format("No track with id #%d.", id));
            }

            public List<Track> getTracks()
            {
                return tracks;
            }

            public List<Track> findByMatchingTitle(String title)
            {
                String titleLower = title.toLowerCase();

                List<Track> result = CollectionFactory.newList();

                for (Track t : tracks)
                {
                    if (t.getTitle().toLowerCase().startsWith(titleLower)) result.add(t);
                }

                return result;
            }
        };
    }

    /**
     * This is a service definition, the service will be named TimingFilter. The interface,
     * RequestFilter, is used within the RequestHandler service pipeline, which is built from the
     * RequestHandler service configuration. Tapestry IoC is responsible for passing in an
     * appropriate Log instance. Requests for static resources are handled at a higher level, so
     * this filter will only be invoked for Tapestry related requests.
     *
     * @param logger system logger
     * @return the request filter
     */
    public RequestFilter buildTimingFilter(final Logger logger)
    {
        return new RequestFilter()
        {
            public boolean service(Request request, Response response, RequestHandler handler)
                    throws IOException
            {
                long startTime = System.currentTimeMillis();

                try
                {
                    // The reponsibility of a filter is to invoke the corresponding method
                    // in the handler. When you chain multiple filters together, each filter
                    // received a handler that is a bridge to the next filter.

                    return handler.service(request, response);
                }
                finally
                {
                    long elapsed = System.currentTimeMillis() - startTime;

                    logger.info(String.format("Request time: %d ms", elapsed));
                }
            }
        };
    }

    /**
     * This is a contribution to the RequestHandler service configuration. This is how we extend
     * Tapestry using the timing filter. A common use for this kind of filter is transaction
     * management or security.
     *
     * @param configuration ordered configuration
     * @param filter        the request filter
     */
    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
                                         RequestFilter filter)
    {
        // Each contribution to an ordered configuration has a name, When necessary, you may
        // set constraints to precisely control the invocation order of the contributed filter
        // within the pipeline.
        configuration.add("Timing", filter);
    }

    public static ThumbNailService buildThumbnailService(Logger logger,
                                                         ImageService imageService,
                                                         Context context,
                                                         @InjectService("ContextAssetFactory") AssetFactory assetFactory)
    {
        return new ThumbNailServiceImpl(logger, imageService, context, assetFactory);
    }

    /**
     * contribute configuration to velocity template service.
     */
    public static void contributeVelocityService(MappedConfiguration<String, Resource> configuration)
    {
        configuration.add(VelocityServiceImpl.CONFIG_RESOURCE_KEY, new ClasspathResource("velocity.properties"));
    }

    /**
     * contribute configuration to freemarker template service.
     */
    public static void contributeFreeMarkerService(MappedConfiguration<String, Configuration> configuration)
    {
        freemarker.template.Configuration config = new freemarker.template.Configuration();
        configuration.add(FreeMarkerServiceImpl.CONFIG_RESOURCE_KEY, config);
    }
}
