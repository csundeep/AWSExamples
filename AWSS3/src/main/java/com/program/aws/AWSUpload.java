package com.program.aws;

import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AWSUpload {

	public static void main(String[] args) throws IOException {
		AWSCredentials credentials = new BasicAWSCredentials(
				"AKIAJB2IT6DSGPF4QIUQ",
				"DGkvxF4skIq2BeLIzbIDwyqPaejYm+6l+Joenn+w");
		AmazonS3 s3client = new AmazonS3Client(credentials);
		try {
			System.out.println("Uploading a new object to S3 from a file\n");
			File file = new File("E:/WordList.xlsx");
			s3client.putObject(new PutObjectRequest("testolm", "WordList", file));

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which "
					+ "means your request made it "
					+ "to Amazon S3, but was rejected with an error response"
					+ " for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which "
					+ "means the client encountered "
					+ "an internal error while trying to "
					+ "communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}
}
