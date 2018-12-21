# camera-azure-app-java
The app works like a stand alone camera app on your machine.  
It lets you click pictures and uploads it to pre-configured storage account on Azure. It also lets you list and delete the pictures from the storage account. 
It uses keyvault to hide storage account credentials, and your service principal needs to be given access to keyvault for you to be able to use the app. 

App Design:
![](https://github.com/g2vinay/camera-azure-app-java/blob/master/design.png)

