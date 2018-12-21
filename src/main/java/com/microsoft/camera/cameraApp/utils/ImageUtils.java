package com.microsoft.camera.cameraApp.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

public class ImageUtils {
	
	public static  BufferedImage toBufferedImage(Frame frame) {
	  Java2DFrameConverter bimConverter = new Java2DFrameConverter();
	  BufferedImage img = bimConverter. convert(frame);
	  return img;
  	}
	
	public static byte[] imageToByes(BufferedImage image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] imageInByte = null;
		try {
				ImageIO.write( image, "jpg", baos );
				baos.flush();
		 		imageInByte = baos.toByteArray();
		 		baos.close();
		 		return imageInByte;
		} catch (IOException e1) {
				e1.printStackTrace();
		}
		return imageInByte;
	}

}
