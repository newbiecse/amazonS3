import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import common.utils.S3Utils;

public class AmazonS3Example {
	
	public static void main(String[] args) {

		AmazonS3 s3client = S3Utils.getAmazonS3();
		
		// create bucket - name must be unique for all S3 users
		String bucketName = "test.abcv.com";
		
		// list buckets
		for (Bucket bucket : s3client.listBuckets()) {
			System.out.println(" - " + bucket.getName());
		}
		
		// create folder into bucket
//		String folderName = "testfolder";
//		S3Utils.createFolder(bucketName, folderName, s3client);
		
		// upload file to folder and set it to public
//		String pathUpload = folderName + S3Utils.SUFFIX + "sample.mp4";
//		String pathLocalFile = "C:\\Users\\ABC-VN-17-PC\\Desktop\\AmazonS3\\test-upload\\sample.mp4";
//		
//		S3Utils.upload(bucketName, pathUpload, pathLocalFile, 
//				s3client, CannedAccessControlList.PublicRead);
		
		// deletes bucket
		S3Utils.deleteObject(s3client, "test.abcv.com", "");
		s3client.deleteBucket(bucketName);
	}
	

}