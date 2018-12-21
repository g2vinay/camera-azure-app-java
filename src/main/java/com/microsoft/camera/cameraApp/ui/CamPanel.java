package com.microsoft.camera.cameraApp.ui;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import com.microsoft.camera.cameraApp.interfaces.ICamPanel;

public class CamPanel implements ICamPanel {
	
	private JPanel panel;
	
	public CamPanel(LayoutManager layoutManager) {
		panel = new JPanel(layoutManager);
	}

	@Override
	public ICamPanel add(Component comp) {
		getPanel().add(comp);
		return this;
	}

	public JPanel getPanel() {
		return panel;
	}



}
