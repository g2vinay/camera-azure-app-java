package com.microsoft.camera.cameraApp.utils;

public class EnvironmentConfiguration {
	
	private static String camVaultUrl = null;
	private static String camClient = null;
	private static String camClientKey = null;
	private static String camTenant = null;
	
	public static String getCamVaultUrl() {
		if(camVaultUrl == null) {
			camVaultUrl = System.getenv("CAM_VAULT_URL");
		}
		return camVaultUrl;
	}
	
	public static String getCamClient() {
		if(camVaultUrl == null) {
			camVaultUrl = 	System.getenv("CAM_CLIENT");
		}
		return camVaultUrl;
	}
	
	public static String getCamClientKey() {
		if(camVaultUrl == null) {
			camVaultUrl = System.getenv("CAM_CLIENT_KEY");
		}
		return camVaultUrl;
	}
	
	public static String getCamTenant() {
		if(camVaultUrl == null) {
			camVaultUrl = 	System.getenv("CAM_TENANT");
		}
		return camVaultUrl;
	}
	
}
