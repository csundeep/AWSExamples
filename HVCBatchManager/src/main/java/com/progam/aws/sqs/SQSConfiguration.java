package com.progam.aws.sqs;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

public class SQSConfiguration {
	
	private String queueName;
	private Region region;
	private AWSCredentialsProvider credentialsProvider;
	
	public String getQueueName() {
		return queueName;
	}
	
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	public Region getRegion() {
		return region;
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	
	public AWSCredentialsProvider getCredentialsProvider() {
		return credentialsProvider;
	}
	
	public void setCredentialsProvider(AWSCredentialsProvider credentialsProvider) {
		this.credentialsProvider = credentialsProvider;
	}
	
	public static SQSConfiguration parseConfig() {
		return new SQSConfiguration();
	}
	
	private SQSConfiguration() {
		setQueueName("HVCQueue");
		setRegion(Region.getRegion(Regions.fromName(Regions.US_EAST_1.getName())));
		setCredentialsProvider(new StaticCredentialsProvider(new BasicAWSCredentials("AKIAJB2IT6DSGPF4QIUQ",
				"DGkvxF4skIq2BeLIzbIDwyqPaejYm+6l+Joenn+w")));
	}
	
}
