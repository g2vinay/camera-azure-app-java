package com.microsoft.camera.cameraApp;

import static org.bytedeco.javacpp.opencv_core.cvFlip;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.VideoInputFrameGrabber;

import com.microsoft.camera.cameraApp.interfaces.ICamCanvas;

public class Camera implements Runnable {
	private final int INTERVAL = 100;
	Frame frame;
	FrameGrabber grabber; 
	OpenCVFrameConverter.ToIplImage converter;
	ICamCanvas canvas;
	    
	public Camera(ICamCanvas canvas) {
		grabber = new VideoInputFrameGrabber(0); // 1 for next camera
    	converter = new OpenCVFrameConverter.ToIplImage();
    	this.canvas = canvas;
	}


	/**
	 * Starts the camera as a separate thread.
	 * Continuously grabs frames from video input.
	 * Displays the frames in the canvas sequentially.
	 */
	public void run() {
	        IplImage img;
	        try {
	            grabber.start();
	            while (true) {
	                frame = grabber.grab();

	                img = converter.convert(frame);
	                cvFlip(img, img, 1);// l-r = 90_degrees_steps_anti_clockwise

	                canvas.showImage(converter.convert(img));

	                Thread.sleep(INTERVAL);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	/**
	 * Gets the current Frame being displayed in canvas.
 	 * @return the Frame currently being displayed.
	 */
	public Frame getFrame() {
		 return frame;
	 }
}
