package com.microsoft.camera.cameraApp;

import org.bytedeco.javacv.*;

import com.microsoft.azure.storage.blob.models.BlobItem;
import com.microsoft.camera.cameraApp.interfaces.ICamCanvas;
import com.microsoft.camera.cameraApp.interfaces.ICamPanel;
import com.microsoft.camera.cameraApp.managers.AzureBlobStorageManager;
import com.microsoft.camera.cameraApp.ui.CamCanvas;
import com.microsoft.camera.cameraApp.ui.CamPanel;
import com.microsoft.camera.cameraApp.utils.ImageUtils;

import net.miginfocom.swt.MigLayout;

import static org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.cvFlip;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvSaveImage;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class CameraControlllerUI {
   
    private ICamCanvas canvas;
    private Camera camera;
    private JButton captureBtn;
    private JButton listBtn;
    private JButton deleteBtn;
    private ICamPanel panel;

    
    public CameraControlllerUI() {
    	setupUI();
    }

	/**
	 * Instantiate the camera with canvas.
	 * Start the camera as a separate thread.
	 */
	public void startCamera() {
        camera = new Camera(canvas);
        Thread th = new Thread(camera);
        th.start();
    }
    
    private void setupUI() {
    	captureBtn = new JButton("Capture And Upload");
    	listBtn = new JButton("List pictures");
    	deleteBtn = new JButton("Delete pictures");

        panel = new CamPanel(new GridLayout(2,4))
        		.add(captureBtn)
        		.add(listBtn)
        		.add(deleteBtn);

    	canvas = new CamCanvas("Camera Storage")
    			.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE)
    			.setLayout(new GridLayout(1,1))
    			.add(panel);
    	
        registerCaptureListener();
    }
    
    private void registerCaptureListener() {
    	captureBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  Frame curFrame = camera.getFrame();
        	  BufferedImage img = ImageUtils.toBufferedImage(curFrame);
        	  byte[] imageInByte = ImageUtils.imageToByes(img);
	          try {
				AzureBlobStorageManager.getInstance().uploadBlob2("vinaystorage", imageInByte, "image_"+System.currentTimeMillis()+".jpg");
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
				
          }
        });
    	
    	listBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	 try {
				ArrayList<String> images = AzureBlobStorageManager.getInstance().listBlobs("vinaystorage");
				for (String image : images) {
	                System.out.println("Image name: " + image);
				}
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
          }
        });
    	
    	deleteBtn.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	 
			try {
				ArrayList<String> images =  AzureBlobStorageManager.getInstance().listBlobs("vinaystorage");
				for(String image: images) {
					AzureBlobStorageManager.getInstance().deleteBlobIfExists("vinaystorage", image);
				}
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
          }
        });
    }

   


}