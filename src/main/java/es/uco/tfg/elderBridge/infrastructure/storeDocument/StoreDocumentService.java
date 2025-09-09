package es.uco.tfg.elderBridge.infrastructure.storeDocument;


import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@Log4j2
public class StoreDocumentService implements StoreFile{
	
	
	private S3Client s3;
	@Value("${spring.destination.folder}")
	private String destinationFolder;
	
	private static String bucketNameSuffix ="bucketelderbridge";

    public StoreDocumentService(S3Client s3Client) {
    	this.s3 = s3Client;
    }

	@Override
	public Boolean upload(String fileName, Path path, String bucketName) throws Exception {
		String officialBucketName= bucketName +bucketNameSuffix;
		if (!checkIfBucketExist(officialBucketName)) {
			createBucket(officialBucketName);
		}
		
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(officialBucketName)
				.key(fileName)
				.build();
		PutObjectResponse response= this.s3.putObject(putObjectRequest, path);
		return  response.sdkHttpResponse().isSuccessful();
	}


	@Override
	public byte[] download(String bucket,String fileName) throws Exception {
		String officialBucketName= bucket +bucketNameSuffix;

		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(officialBucketName)
				.key(fileName)
				.build();
		
		ResponseBytes<GetObjectResponse> getResponseBytes = this.s3.getObjectAsBytes(getObjectRequest);
		
	    byte[] fileContent = getResponseBytes.asByteArray();
	    
	    return fileContent;
		
	}
	
	@Override
	public void createBucket(String bucketName) {
		CreateBucketResponse response = this.s3.createBucket(bucketBuilder -> bucketBuilder.bucket(bucketName.toLowerCase()));
        log.info("Bucket creado en la ubicación: " + response.location());
    }

    @Override
    public Boolean checkIfBucketExist(String bucketName) {
        try {
            this.s3.headBucket( headBucket -> headBucket.bucket(bucketName) );
            log.info("El bucket " + bucketName + " si existe.");
            return true;
        } catch(S3Exception exception){
            log.info("El bucket " + bucketName + " no existe.");
            return false;
        }
    }

}
