package com.program.aws;

import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.codedeploy.AmazonCodeDeployClient;
import com.amazonaws.services.codedeploy.model.BundleType;
import com.amazonaws.services.codedeploy.model.RegisterApplicationRevisionRequest;
import com.amazonaws.services.codedeploy.model.RevisionLocation;
import com.amazonaws.services.codedeploy.model.RevisionLocationType;
import com.amazonaws.services.codedeploy.model.S3Location;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;

public class AWSUpload {

	public static void main(String[] args) throws IOException {

		AmazonS3 s3client = S3Configuration.getS3Client();
		try {
			AmazonCodeDeployClient amazonCodeDeployClient = new AmazonCodeDeployClient(new StaticCredentialsProvider(
					new BasicAWSCredentials("AKIAJB2IT6DSGPF4QIUQ", "DGkvxF4skIq2BeLIzbIDwyqPaejYm+6l+Joenn+w")));
			System.out.println("Uploading a new object to S3 from a file\n");
			File file = new File("E:/test.txt");
			PutObjectResult result = S3Configuration.putS3Object(s3client, "testolmbucket", "test", file);
			System.out.println(result.getETag());
			S3Location s3Location = new S3Location();
			s3Location.setBucket("testolmbucket");
			s3Location.setKey("test");
			RevisionLocation revision = new RevisionLocation();
			revision.setS3Location(s3Location);
			revision.setRevisionType(RevisionLocationType.S3);
			s3Location.setETag(result.getETag());
			System.out.println(s3Location.getBucket());
//			amazonCodeDeployClient.registerApplicationRevision(
//					new RegisterApplicationRevisionRequest().withApplicationName("sa").withRevision(revision));

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which " + "means your request made it "
					+ "to Amazon S3, but was rejected with an error response" + " for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which " + "means the client encountered "
					+ "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}
}
