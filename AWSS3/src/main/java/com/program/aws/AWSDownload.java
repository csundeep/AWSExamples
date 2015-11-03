package com.program.aws;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AWSDownload {
	private static String bucketName = "hvc-batch";
	
	public static void main(String[] args) throws IOException {
		AmazonS3 s3Client = S3Configuration.getS3Client();
		try {
			System.out.println("Downloading an object");
			List<String> exsitKeys = new ArrayList<String>();
			for (S3ObjectSummary s3ObjectSummary : s3Client.listObjects(bucketName).getObjectSummaries()) {
				System.out.println(s3ObjectSummary.getKey());
				exsitKeys.add(s3ObjectSummary.getKey());
			}
			
			Collection<String> allArtifacts = new ArrayList<String>(Arrays.asList("101_INVALID.csv", "101_PROVISION.csv",
					"101_EXISTING.csv"));
			System.out.println(exsitKeys.containsAll(allArtifacts));
			
			 S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, ""));
			 WritingObject(s3object.getObjectContent());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void WritingObject(InputStream input) throws IOException {
		
	}
}
