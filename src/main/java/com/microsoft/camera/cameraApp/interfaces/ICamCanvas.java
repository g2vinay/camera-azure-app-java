package com.microsoft.camera.cameraApp.interfaces;

import java.awt.Component;
import java.awt.LayoutManager;

import org.bytedeco.javacv.Frame;

public interface ICamCanvas {
	
	public ICamCanvas setDefaultCloseOperation(int jFrame);
	
	public ICamCanvas setLayout(LayoutManager layoutManager);
	
	public ICamCanvas add(ICamPanel panel);
	
	public ICamCanvas showImage(Frame frame);
}
