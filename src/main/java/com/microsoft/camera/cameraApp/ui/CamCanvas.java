package com.microsoft.camera.cameraApp.ui;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;

import com.microsoft.camera.cameraApp.interfaces.ICamCanvas;
import com.microsoft.camera.cameraApp.interfaces.ICamPanel;

public class CamCanvas implements ICamCanvas {
	
	private CanvasFrame canvas;
	
	public CamCanvas(String frameName) {
		canvas = new CanvasFrame(frameName);
	}

	@Override
	public ICamCanvas setDefaultCloseOperation(int jFrame) {
		canvas.setDefaultCloseOperation(jFrame);
		return this;
	}

	@Override
	public ICamCanvas setLayout(LayoutManager layoutManager) {
		canvas.setLayout(layoutManager);
		return this;
	}

	@Override
	public ICamCanvas add(ICamPanel camPanel) {
		canvas.add(camPanel.getPanel());
		return this;
	}

	@Override
	public ICamCanvas showImage(Frame frame) {
		canvas.showImage(frame);
		return this;
	}

}
