package com.microsoft.camera.cameraApp.managers;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;
import com.microsoft.azure.AzureResponseBuilder;
import com.microsoft.azure.keyvault.KeyVaultClient;
import com.microsoft.azure.serializer.AzureJacksonAdapter;
import com.microsoft.camera.cameraApp.interfaces.IAzureKeyVaultManager;
import com.microsoft.camera.cameraApp.utils.EnvironmentConfiguration;
import com.microsoft.rest.LogLevel;
import com.microsoft.rest.RestClient;
import com.microsoft.rest.credentials.TokenCredentials;
import com.microsoft.rest.interceptors.LoggingInterceptor;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AzureKeyVaultManager implements IAzureKeyVaultManager{
    private  String AUTHORITY = "https://login.microsoftonline.com/{tenantId}";

    private KeyVaultClient keyVaultClient;


    private static AzureKeyVaultManager instance;


    private AzureKeyVaultManager() throws InterruptedException, ExecutionException, MalformedURLException {
        String accessToken = getAccessToken();
        RestClient restClient = new RestClient.Builder().withBaseUrl(EnvironmentConfiguration.getCamVaultUrl())
                .withSerializerAdapter(new AzureJacksonAdapter())
                .withResponseBuilderFactory(new AzureResponseBuilder.Factory())
                .withCredentials(new TokenCredentials(null,accessToken))
                .withLogLevel(LogLevel.NONE)
                .withNetworkInterceptor(new LoggingInterceptor(LogLevel.BODY_AND_HEADERS)).build();
         keyVaultClient = new KeyVaultClient(restClient);
    }

    public static AzureKeyVaultManager getInstance() throws InterruptedException, ExecutionException, MalformedURLException {
        if (instance == null) {
            instance = new AzureKeyVaultManager();
        }
        return instance;
    }



    private String getAccessToken() throws MalformedURLException, ExecutionException, InterruptedException {
    	String auth = AUTHORITY.replace("{tenantId}", EnvironmentConfiguration.getCamTenant());
        String resourceUrl = "https://vault.azure.net";
        ExecutorService service = Executors.newFixedThreadPool(1);
        AuthenticationContext context = new AuthenticationContext(auth, true, service);
        // Acquire Token
        Future<AuthenticationResult> result = context.acquireToken(
                resourceUrl,
                new ClientCredential(EnvironmentConfiguration.getCamClient(), EnvironmentConfiguration.getCamClientKey()),
                null
        );
        String token = result.get().getAccessToken();
        return token;
    }

    /**
     * Gets the storage account name and secret key from key vault.
     * @return a hashmap holding account name and secret key
     */
    public Map<String, String> getStorageAccountInfo(){
        Map<String, String> storageAccountMap = new HashMap<String, String>();
        storageAccountMap.put("secretKey", keyVaultClient.getSecret(EnvironmentConfiguration.getCamVaultUrl(),"storageKey").value());
        storageAccountMap.put("accountName", keyVaultClient.getSecret(EnvironmentConfiguration.getCamVaultUrl(),"storageName").value());

        return storageAccountMap;

    }





}
