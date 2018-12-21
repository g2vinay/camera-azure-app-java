package com.microsoft.camera.cameraApp.interfaces;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public interface ICamPanel {
	
	public ICamPanel add(Component comp);
	
	public JPanel getPanel();
}
