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

package org.chenillekit.image.services;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import org.apache.tapestry5.ioc.Resource;

/**
 * some image based helpers.
 *
 * @author <a href="mailto:homburgs@googlemail.com">shomburg</a>
 * @version $Id: ImageService.java 682 2008-05-20 22:00:02Z homburgs $
 */
public interface ImageService
{
    /**
     * reduce the quality of an image.
     *
     * @param image   the original image
     * @param quality quality
     * @param output  data stream of the quality reduced image
     */
    void reduceImageQuality(BufferedImage image, float quality, OutputStream output);

    /**
     * scale image object to a new size.
     *
     * @param imageResource image resource to scale
     * @param height        scale to height
     *
     * @return scaled image
     */
    BufferedImage scaleImage(Resource imageResource, int height);
}
