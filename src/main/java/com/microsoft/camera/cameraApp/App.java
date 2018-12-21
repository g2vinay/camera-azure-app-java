package com.microsoft.camera.cameraApp;

import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.microsoft.camera.cameraApp.managers.AzureBlobStorageManager;
import com.microsoft.camera.cameraApp.managers.AzureKeyVaultManager;

public class App
{
	
	
    public static void main(String[] args) throws InvalidKeyException, MalformedURLException, InterruptedException, ExecutionException {
    	Map<String, String> storageInfo = AzureKeyVaultManager.getInstance().getStorageAccountInfo();
        AzureBlobStorageManager.getInstance().setCredentials(storageInfo.get("accountName"), storageInfo.get("secretKey"));
        CameraControlllerUI cameraUI = new CameraControlllerUI();
        cameraUI.startCamera();
    }

	
}
