# camera-azure-app-java
The app works like a stand alone camera app on your machine.  
It lets you click pictures and uploads it to pre-configured storage account on Azure. It also lets you list and delete the pictures from the storage account. 
It uses keyvault to hide storage account credentials, and your service principal needs to be given access to keyvault for you to be able to use the app. 

App Design:

![](https://github.com/g2vinay/camera-azure-app-java/blob/master/design.png)

1. App bootstraps and uses your service principal to get storage account details from the key vault. 
2. Camera Starts displaying the Camera UI (Swing UI) 
3. Using the UI pictures are clicked, uploaded, listed or deleted. 
4. The storage credentials fetched in Step 1 are used to perform storage operations in Step 3. 

 
Libraries/Tools used: 
Jackson 
RxJava (will migrate to Reactor) 
Spotbugs 
azure-mgmt-vault 
azure-storage-blob 


Setup Instructions:
Configure Following Environment Variables:
"CAM_VAULT_URL" 
"CAM_CLIENT"
"CAM_CLIENT_KEY"
"CAM_TENANT"

Get your Service Principal added to camera vault's access policies.


ScreenShots: 
