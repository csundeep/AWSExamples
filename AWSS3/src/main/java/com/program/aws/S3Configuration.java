package com.program.aws;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

public class S3Configuration {
	
	public static AmazonS3 getS3Client() {
		AWSCredentials credentials = new BasicAWSCredentials("AKIAJQDRCDMGPKLM5UMQ", "kQMvfOg3WFFStc92zp7kY6UTEB3B6c2W2otGuax4");
		AmazonS3 s3client = new AmazonS3Client(credentials);
		return s3client;
	}
	
	public static PutObjectResult putS3Object(AmazonS3 s3client, String bucketName, String fileName, File file)
			throws AmazonServiceException, AmazonClientException {
		return s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
	}
}
