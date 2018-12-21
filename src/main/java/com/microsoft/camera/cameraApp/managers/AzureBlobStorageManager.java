package com.microsoft.camera.cameraApp.managers;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Locale;

import com.microsoft.azure.storage.blob.BlobURL;
import com.microsoft.azure.storage.blob.BlockBlobURL;
import com.microsoft.azure.storage.blob.ContainerURL;
import com.microsoft.azure.storage.blob.ListBlobsOptions;
import com.microsoft.azure.storage.blob.PipelineOptions;
import com.microsoft.azure.storage.blob.ServiceURL;
import com.microsoft.azure.storage.blob.SharedKeyCredentials;
import com.microsoft.azure.storage.blob.StorageURL;
import com.microsoft.azure.storage.blob.models.BlobItem;
import com.microsoft.azure.storage.blob.models.ContainerListBlobFlatSegmentResponse;
import com.microsoft.camera.cameraApp.interfaces.IAzureBlobStorageManager;
import com.microsoft.rest.v2.RestException;
import com.microsoft.rest.v2.RestResponse;
import com.microsoft.rest.v2.http.HttpPipeline;
import com.microsoft.rest.v2.util.FlowableUtil;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * 
 * @author vigera
 *
 */
public class AzureBlobStorageManager implements IAzureBlobStorageManager {
	
	private String accountName;
	private String accountKey;
    private SharedKeyCredentials credential;
	private  HttpPipeline pipeline;
	private  URL u;
	 
    // Create a ServiceURL object that wraps the service URL and a request pipeline.
    private ServiceURL serviceURL;

    
    private static AzureBlobStorageManager instance;
    
    
    private AzureBlobStorageManager() {
    	
    	
    }
    
    public static AzureBlobStorageManager getInstance() {
    	if (instance == null) {
    		instance = new AzureBlobStorageManager();
    	}
    	return instance;
    }
	
    /**
     * Sets up the credentials used to connect to Azure storage service.
     * 
     * @param accountName - name of the storage account
     * @param accountKey - access key for the storage account
     * @throws InvalidKeyException - when storage account key is not valid.
     */
	public void setCredentials(String accountName, String accountKey) throws InvalidKeyException {
		this.accountName = accountName;
		this.accountKey  = accountKey;
		this.credential = new SharedKeyCredentials(accountName, accountKey);
		pipeline = getDefaultPipeline();
    	u = getURLwithDefaultOptions();
    	serviceURL = new ServiceURL(u, pipeline);
	}
	

	/**
	 * Creates container in the storage account if it doesn't already exists.
	 * @param containerName - Name of the container to be created.
	 * @return Boolean wrapped in Single Instance, - true if the container was created, false if it already exists.
	 */
	public Single<Boolean> createContainerIfNotExists(String containerName) {
		
		 ContainerURL containerURL = serviceURL.createContainerURL(containerName);

        return Single.just(containerURL.create(null, null, null).map((r) -> true).onErrorResumeNext((e) -> {

            if (e instanceof RestException) {

                RestException re = (RestException) e;

                if (re.getMessage().contains("ContainerAlreadyExists")) {

                    return Single.just(false);

                }
            }
            return Single.error(e);
        }).blockingGet());
    }
	
	/**
	 * Deletes the container if it exists in the storage account.
	 * @param containerName - Name of the container to be deleted.
	 * @return Boolean wrapped in Single Instance, - true if the container was deleted, false if it doesn't exists.
	 */
	 public Single<Boolean> deleteContainerIfExists(String containerName) {
		 
		 	ContainerURL containerURL = serviceURL.createContainerURL(containerName);

	        return Single.just(containerURL.delete(null, null).map((r) -> true).onErrorResumeNext((e) -> {

	            if (e instanceof RestException) {

	                RestException re = (RestException) e;

	                if (re.getMessage().contains("ContainerNotFound")) {
	                    return Single.just(false);
	                }
	            }
	            return Single.error(e);
	        }).blockingGet());

	    }


	/**
	 * Deletes the blob if it exists in the storage account.
	 * @param parentContainer - Name of the container in which blob exists.
	 * @param blob - Name of the blob to be deleted in parentContainer.
	 * @return Boolean wrapped in Single Instance, - true if the blob was deleted, false if it doesn't exists.
	 */
	 public Single<Boolean> deleteBlobIfExists(String parentContainer, String blob){
		 ContainerURL containerURL = serviceURL.createContainerURL(parentContainer);
		 BlobURL blobURL = containerURL.createBlobURL(blob);
	        return Single.just(blobURL.delete().map((r) -> true).onErrorResumeNext((e) -> {

	            if (e instanceof RestException) {

	                RestException re = (RestException) e;

	                if (re.getMessage().contains("BlobNotFound")) {
	                    return Single.just(false);
	                }
	            }
	            return Single.error(e);
	        }).blockingGet());

	 }
	

	/**
	 * Uploads the data as blob with the specified name to the specified container in the storage account
	 * @param parentContainer - the container to which data should be uploaded.
	 * @param data - data in bytes to be uploaded as blob.
	 * @param name - name of the blob to be uploaded and created.
	 * @throws MalformedURLException - thrown when container/blob URL is not properly formed/instantiated.
	 */
	public void uploadBlob2(String parentContainer, byte[] data, String name) throws MalformedURLException{
				
	     /*
	     Create a URL that references a to-be-created container in your Azure Storage account. This returns a
	     ContainerURL object that wraps the container's URL and a request pipeline (inherited from serviceURL).
	     Note that container names require lowercase.
	      */
	     ContainerURL containerURL = serviceURL.createContainerURL(parentContainer);
	
	     Single<Boolean> status = createContainerIfNotExists(parentContainer);
	     
	     /*
	     Create a URL that references a to-be-created blob in your Azure Storage account's container.
	     This returns a BlockBlobURL object that wraps the blob's URl and a request pipeline
	     (inherited from containerURL). Note that blob names can be mixed case.
	      */
	  	 BlockBlobURL blobURL = containerURL.createBlockBlobURL(name);
	 	 blobURL.upload(Flowable.just(ByteBuffer.wrap(data)), data.length,
	
	             null, null, null, null).blockingGet();
		}
	
	private URL getURLwithDefaultOptions() {
		
	     /*
	     From the Azure portal, get your Storage account blob service URL endpoint.
	     The URL typically looks like this:
	      */
	     URL u;
		try {
			u = new URL(String.format(Locale.ROOT, "https://%s.blob.core.windows.net", accountName));
			return u;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	    
	}
	
	private HttpPipeline getDefaultPipeline() {
		HttpPipeline pipeline = StorageURL.createPipeline(credential, new PipelineOptions());
		return pipeline;
	}
	
	
	/**
	 * Pulls all the items inside the container and returns their names in a list form.
	 * @param parentContainer - The container to list the items from.
	 * @return an array list of names of items inside the container.
	 * @throws MalformedURLException - thrown when service/container URL is not properly formed/instantiated.
	 */
	public ArrayList<String> listBlobs(String parentContainer) throws MalformedURLException {
		
		 HttpPipeline pipeline = getDefaultPipeline();
		 URL u = getURLwithDefaultOptions();
		 
	     // Create a ServiceURL object that wraps the service URL and a request pipeline.
	     ServiceURL serviceURL = new ServiceURL(u, pipeline);
		
		ContainerURL containerURL = serviceURL.createContainerURL(parentContainer);
		
	    // Each ContainerURL.listBlobsFlatSegment call return up to maxResults (maxResults=10 passed into ListBlobOptions below).
	    // To list all Blobs, we are creating a helper static method called listAllBlobs, 
	    // and calling it after the initial listBlobsFlatSegment call
	    ListBlobsOptions options = new ListBlobsOptions().withMaxResults(10);
	    ArrayList<String> blobs = new ArrayList<String>();
	    containerURL.listBlobsFlatSegment(null, options, null)
	        .flatMap(containersListBlobFlatSegmentResponse -> 
	        listBlobsFlatHelper(containerURL, containersListBlobFlatSegmentResponse, blobs)).onErrorResumeNext((e) -> {

	            if (e instanceof RestException) {

	                RestException re = (RestException) e;

	                if (re.getMessage().contains("ContainerNotFound")) {
	                    System.out.println("No Images to Delete");
	                }
	            }
	            return Single.error(e);
	        }).blockingGet();
	    return blobs;
	}
	
	

    // <list_blobs_flat_helper>

    private Single<ContainerListBlobFlatSegmentResponse> listBlobsFlatHelper(

            ContainerURL containerURL, ContainerListBlobFlatSegmentResponse response, ArrayList<String> blobs) {

        // Process the blobs returned in this result segment (if the segment is empty, blob() will be null.
        if (response.body().segment() != null) {
            for (BlobItem b : response.body().segment().blobItems()) {
                blobs.add(b.name());
            }
        }
        
     // If there is not another segment, return this response as the final response.
        if (response.body().nextMarker() == null) {
            return Single.just(response);
        } else {
        	
            /*
             IMPORTANT: ListBlobsFlatSegment returns the start of the next segment; you MUST use this to get the next
             segment (after processing the current result segment
             */
            String nextMarker = response.body().nextMarker();

            /*
            The presence of the marker indicates that there are more blobs to list, so we make another call to
            listBlobsFlatSegment and pass the result through this helper function.
             */

            return containerURL.listBlobsFlatSegment(nextMarker, new ListBlobsOptions().withMaxResults(10), null)
                    .flatMap(containersListBlobFlatSegmentResponse ->
                            listBlobsFlatHelper(containerURL, containersListBlobFlatSegmentResponse, blobs));
        }
    }
}
