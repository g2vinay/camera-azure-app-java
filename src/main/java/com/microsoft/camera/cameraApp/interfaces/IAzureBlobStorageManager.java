package com.microsoft.camera.cameraApp.interfaces;

import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

public interface IAzureBlobStorageManager {
    public void setCredentials(String accountName, String accountKey) throws InvalidKeyException;

    public Single<Boolean> deleteContainerIfExists(String containerName);

    public Single<Boolean> deleteBlobIfExists(String parentContainer, String blob);

    public void uploadBlob2(String parentContainer, byte[] data, String name) throws MalformedURLException;

    public ArrayList<String> listBlobs(String parentContainer) throws MalformedURLException;

    public Single<Boolean> createContainerIfNotExists(String containerName);

}
