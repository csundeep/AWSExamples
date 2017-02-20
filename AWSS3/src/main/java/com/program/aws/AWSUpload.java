package com.program.aws;

import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;

public class AWSUpload {

	public static void main(String[] args) throws IOException {

		try {
			AmazonS3 s3client = S3Configuration.getS3Client();
			System.out.println("Uploading a new object to S3 from a file\n");
			File file = new File("C:/Users/Sundeep.Chilukur/Downloads/rootkey.csv");
			PutObjectResult result = S3Configuration.putS3Object(s3client, "testolmbucket", "test", file);
			System.out.println(result.getETag());
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
