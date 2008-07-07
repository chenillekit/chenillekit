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

package org.chenillekit.image.services.impl;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.tapestry5.ioc.Resource;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.chenillekit.image.services.ImageService;

/**
 * some image based helpers.
 *
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id: ImageServiceImpl.java 682 2008-05-20 22:00:02Z homburgs $
 */
public class ImageServiceImpl implements ImageService
{
    /**
     * converts an image resource to an AWT image.
     *
     * @param imageResource image resource
     *
     * @return AWT image
     */
    private Image toAwtImage(Resource imageResource)
    {
        Image image = Toolkit.getDefaultToolkit().getImage(imageResource.toURL());
        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(image, 0);

        try
        {
            mediaTracker.waitForID(0);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        return image;
    }

    /**
     * reduce the quality of an image.
     *
     * @param image   the original image
     * @param quality quality
     * @param output  data stream of the quality reduced image
     */
    public void reduceImageQuality(BufferedImage image, float quality, OutputStream output)
    {
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
        quality = Math.max(0, Math.min(quality, 100));
        param.setQuality(quality / 100.0f, false);
        encoder.setJPEGEncodeParam(param);

        try
        {
            encoder.encode(image);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * scale image object to a new size.
     *
     * @param imageResource image resource to scale
     * @param height        scale to height
     *
     * @return scaled image
     */
    public BufferedImage scaleImage(Resource imageResource, int height)
    {
        int thumbWidth;
        Image image = toAwtImage(imageResource);

        double imageRatio = (double) image.getWidth(null) / (double) image.getHeight(null);
        thumbWidth = (int) (height * imageRatio);

        BufferedImage thumbImage = new BufferedImage(thumbWidth, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, thumbWidth, height, null);

        return thumbImage;
    }
}
