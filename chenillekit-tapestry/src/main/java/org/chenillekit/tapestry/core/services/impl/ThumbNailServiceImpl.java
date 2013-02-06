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

package org.chenillekit.tapestry.core.services.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.internal.services.ContextResource;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.Context;

import org.chenillekit.core.services.ImageService;
import org.chenillekit.tapestry.core.ChenilleKitCoreConstants;
import org.chenillekit.tapestry.core.services.ThumbNailService;
import org.slf4j.Logger;

/**
 * @version $Id$
 */
public class ThumbNailServiceImpl implements ThumbNailService
{
    private Logger logger;
    private ImageService imageService;
    private File thumbnailDirectory;
    private Context context;
    private AssetFactory assetFactory;

    public ThumbNailServiceImpl(Logger logger, ImageService imageService, Context context, AssetFactory assetFactory)
    {
        this.logger = logger;
        this.imageService = imageService;
        this.context = context;
        this.assetFactory = assetFactory;

        thumbnailDirectory = context.getRealFile(ChenilleKitCoreConstants.__THUMBNAL_DIRECTORY__);
        if (!thumbnailDirectory.exists())
            throw new RuntimeException("path: '" + thumbnailDirectory.getAbsolutePath() + "' not found.");

        if (this.logger.isDebugEnabled())
            this.logger.debug("thumbnail location: {}", thumbnailDirectory.getPath());
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
        Asset thumbnailAsset;
        FileOutputStream output = null;
        String thumbnailFileName = String.valueOf(buildChecksumFromURI(originalAsset.toClientURL(), height, quality).getValue());

        try
        {
            if (logger.isDebugEnabled())
                logger.debug("original image: {}", originalAsset.toClientURL());

            File outputFile = new File(thumbnailDirectory.getAbsolutePath() + "/" + thumbnailFileName);
            if (!outputFile.exists())
            {
                output = new FileOutputStream(outputFile, false);
                BufferedImage bufferedImage = imageService.scaleImage(originalAsset.getResource(), height);
                imageService.reduceImageQuality(bufferedImage, quality, output);
                output.flush();
            }
            else
            {
                if (logger.isDebugEnabled())
                    logger.debug("cached thumbnail '{}'", outputFile.toURI().toASCIIString());
            }

            Resource thumbRes = new ContextResource(context, ChenilleKitCoreConstants.__THUMBNAL_DIRECTORY__ + "/" + thumbnailFileName);

            if (logger.isDebugEnabled())
                logger.debug("thumbnailed image: {}", thumbRes.toURL().toExternalForm());

            return assetFactory.createAsset(thumbRes);
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
        return thumbnailDirectory.getAbsolutePath();
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
