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

package org.chenillekit.tapestry.core.services.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.internal.services.ContextResource;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.Context;

import org.chenillekit.core.resources.URIResource;
import org.chenillekit.image.services.ImageService;
import org.chenillekit.tapestry.core.ChenilleKitCoreConstants;
import org.chenillekit.tapestry.core.services.ThumbNailService;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:homburgs@gmail.com">shomburg</a>
 * @version $Id: ThumbNailServiceImpl.java 701 2008-07-01 20:17:47Z homburgs $
 */
public class ThumbNailServiceImpl implements ThumbNailService
{
    private Logger _logger;
    private ImageService _imageService;
    private Resource _thumbnailDirectory;
    private Context _context;
    private AssetFactory _assetFactory;

    public ThumbNailServiceImpl(Logger logger, ImageService imageService, Context context, AssetFactory assetFactory)
    {
        _logger = logger;
        _imageService = imageService;
        _context = context;
        _assetFactory = assetFactory;

        try
        {
            URL url = context.getResource("/" + ChenilleKitCoreConstants.__THUMBNAL_DIRECTORY__);
            File baseDir = new File(url.toURI());
            try
            {
                if (!baseDir.exists())
                    baseDir.createNewFile();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            _thumbnailDirectory = new URIResource(url.toURI());
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }

        if (_logger.isDebugEnabled())
            _logger.debug("thumbnail location: {}", _thumbnailDirectory.getPath());
    }

    /**
     * converts the original image <em>originalAsset</em> to a thumbnail
     *
     * @param originalAsset the original image
     * @param height        scale down to <em>height</em>
     * @param quality       reduce to the given image quality
     *
     * @return the thumbnail of the original image
     */
    public Asset convertToThumbnail(Asset originalAsset, int height, float quality)
    {
        Resource thumbRes = null;
        Asset thumbnailAsset;
        FileOutputStream output = null;
        String thumbnailFileName = String.valueOf(buildChecksumFromURI(originalAsset.toClientURL(), height, quality).getValue());

        try
        {
            if (_logger.isDebugEnabled())
                _logger.debug("original image: {}", originalAsset.toClientURL());

            File outputFile = new File(new URI(_thumbnailDirectory.getPath() + "/" + thumbnailFileName));
            if (!outputFile.exists())
            {
                output = new FileOutputStream(outputFile, false);
                BufferedImage bufferedImage = _imageService.scaleImage(originalAsset.getResource(), height);
                _imageService.reduceImageQuality(bufferedImage, quality, output);
                output.flush();
            }
            else
            {
                if (_logger.isDebugEnabled())
                    _logger.debug("cached thumbnail '{}'", outputFile.toURL().toExternalForm());
            }

            thumbRes = new ContextResource(_context, ChenilleKitCoreConstants.__THUMBNAL_DIRECTORY__ + "/" + thumbnailFileName);

            if (_logger.isDebugEnabled())
                _logger.debug("thumbnailed image: {}", thumbRes.toURL().toExternalForm());

            return _assetFactory.createAsset(thumbRes);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            if (output != null)
                InternalUtils.close(output);
        }
    }

    /**
     * get the path where generated thumbnail should stored.
     *
     * @return thumbnail path
     */
    public String getThumbnailPath()
    {
        try
        {
            return _thumbnailDirectory.toURL().toURI().toString();
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * computes the crc32 checksum from the original asset client url.
     *
     * @param clientURL the original asset client url
     * @param height    the height of the image
     * @param quality   the quality of the image
     *
     * @return the computed checksum
     */
    private Checksum buildChecksumFromURI(String clientURL, int height, float quality)
    {
        CRC32 checksum = new CRC32();
        checksum.update(String.format("%s_%d_%f", clientURL, height, quality).getBytes());
        return checksum;
    }
}
