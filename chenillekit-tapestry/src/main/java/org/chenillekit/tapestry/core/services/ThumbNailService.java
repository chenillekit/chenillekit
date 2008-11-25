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

package org.chenillekit.tapestry.core.services;

import org.apache.tapestry5.Asset;

/**
 * @version $Id: ThumbNailService.java 682 2008-05-20 22:00:02Z homburgs $
 */
public interface ThumbNailService
{
    /**
     * converts the original image <em>originalAsset</em> to a thumbnail.
     *
     * @param originalAsset the original image
     * @param height        scale down to <em>height</em>
     * @param quality       reduce to the given image quality
     *
     * @return the thumbnail of the original image
     */
    public Asset convertToThumbnail(Asset originalAsset, int height, float quality);

    /**
     * get the path where generated thumbnail should stored.
     *
     * @return thumbnail path
     */
    String getThumbnailPath();
}
