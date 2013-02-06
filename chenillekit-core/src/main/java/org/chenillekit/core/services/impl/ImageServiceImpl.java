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

package org.chenillekit.core.services.impl;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;

import org.apache.tapestry5.ioc.Resource;

import org.chenillekit.core.services.ImageService;

/**
 * some image based helpers.
 *
 * @version $Id$
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
		ImageWriter encoder = (ImageWriter) ImageIO.getImageWritersByFormatName("JPEG").next();
		JPEGImageWriteParam param = new JPEGImageWriteParam(null);

		quality = Math.max(0, Math.min(quality, 100));
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(quality / 100.0f);

		encoder.setOutput(output);

		try
		{
			encoder.write((IIOMetadata) null, new IIOImage(image, null, null), param);
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
