package com.progam.aws.sqs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class AWSDownload {
	private static String bucketName = "hvcsqstesting";
	
	public static void downloadFile(String fileName) throws IOException {
		AmazonS3 s3Client = S3Configuration.getS3Client();
		try {
			System.out.println("Downloading an object");
			// for (S3ObjectSummary iterable_element : s3Client.listObjects(bucketName).getObjectSummaries()) {
			// String value = iterable_element.getKey();
			// if (value.startsWith("Submited/") && value.length() > 9) {
			// key = value;
			// }
			//
			// }
			S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, fileName));
			WritingObject(s3object.getObjectContent());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void WritingObject(InputStream input) throws IOException {
		OutputStream outputStream = new FileOutputStream(new File("D:/WordList.xlsx"));
		byte[] buffer = new byte[1024];
		flow(input, outputStream, buffer);
		input.close();
		outputStream.close();
	}
	
	private static void flow(InputStream is, OutputStream os, byte[] buf) throws IOException {
		int numRead;
		while ((numRead = is.read(buf)) >= 0) {
			os.write(buf, 0, numRead);
		}
	}
}
