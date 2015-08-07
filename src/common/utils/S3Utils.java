package common.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3Utils {

	public static final String SUFFIX = "/";
	public static final String DOMAIN = "abcv.com";
	
	private static final String ACCESS_KEY_ID = "AKIAJGXAXJAZMW3PL7FQ";
	private static final String SECRET_ACCESS_KEY = "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";

	
	public static AmazonS3 getAmazonS3() {
		// credentials object identifying user for authentication
		// user must have AWSConnector and AmazonS3FullAccess for 
		// this example to work
		AWSCredentials credentials = new BasicAWSCredentials(
				ACCESS_KEY_ID, 
				SECRET_ACCESS_KEY);
		
		// create a client connection based on credentials
		AmazonS3 s3client = new AmazonS3Client(credentials);

		return s3client;
	}
	
	
	public static Bucket createBucket(String bucketName, AmazonS3 s3client) {

		Bucket bucket = null;
		
		try {
			String uniqueBucketName = DOMAIN + "." + bucketName;
			bucket = s3client.createBucket(uniqueBucketName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bucket;
	}
	
	
	/**
	 * 
	 * @param bucketName
	 * @param folderName
	 * @param client
	 */
	public static void createFolder(String bucketName, String folderName, AmazonS3 client) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);

		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);

		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
				folderName + SUFFIX, emptyContent, metadata);

		// send request to S3 to create folder
		client.putObject(putObjectRequest);
	}
	
	
	/**
	 * This method first deletes all the files in given folder and than the
	 * folder itself
	 */
	public static void deleteFolder(String bucketName, String folderName, AmazonS3 client) {
		
//		client.
		
		try {
			for (S3ObjectSummary file : client.listObjects(bucketName, folderName).getObjectSummaries()) {
				client.deleteObject(bucketName, file.getKey());
			}
			client.deleteObject(bucketName, folderName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void deleteObject(AmazonS3 s3client, String bucketName, String keyName) {
		try {
			s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));	
		} catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException.");
            System.out.println("Error Message: " + ace.getMessage());
        }
	}
	
	
	public static void upload( String bucketName, String pathUpload, 
			String pathLocalFile, AmazonS3 client,
			CannedAccessControlList cannedAcl ) {
		
			try {
				client.putObject(new PutObjectRequest(bucketName, pathUpload,
						new File(pathLocalFile))
						.withCannedAcl(cannedAcl));
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
