package es.uco.tfg.elderBridge.infrastructure.storeDocument;

import java.nio.file.Path;


public interface StoreFile {
	public Boolean upload(String fileName, Path path, String bucketName) throws Exception;
	public byte[] download(String id,String fileName ) throws Exception; 
	Boolean checkIfBucketExist(String bucketName);
	void createBucket(String bucketName);
	
}
