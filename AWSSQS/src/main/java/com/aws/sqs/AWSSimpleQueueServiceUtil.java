package com.aws.sqs;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

/**
 * Created by IntelliJ IDEA.
 * User: Niraj Singh
 * Date: 3/19/13
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class AWSSimpleQueueServiceUtil {
	private BasicAWSCredentials credentials;
	private AmazonSQS sqs;
	private String simpleQueue;
	private static volatile AWSSimpleQueueServiceUtil awssqsUtil = new AWSSimpleQueueServiceUtil();
	
	/**
	 * instantiates a AmazonSQSClient http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/sqs/AmazonSQSClient.html
	 * Currently using  BasicAWSCredentials to pass on the credentials.
	 * For SQS you need to set your regions endpoint for sqs.
	 */
	private AWSSimpleQueueServiceUtil() {
		try {
			this.credentials = new BasicAWSCredentials("AKIAIRT54AYQCXMK4U4A", "lGvsmuNRFGPIZCmXFJF8tOxeC5Z9xnCDVcsrQ5Xy");
			this.simpleQueue = "hvc-sqs";
			
			this.sqs = new AmazonSQSClient(this.credentials);
			/**
			 * My queue is in singapore region which has following endpoint for sqs
			 * https://sqs.ap-southeast-1.amazonaws.com
			 * you can find your endpoints here
			 * http://docs.aws.amazon.com/general/latest/gr/rande.html
			 *
			 * Overrides the default endpoint for this client ("sqs.us-east-1.amazonaws.com")
			 */
			this.sqs.setEndpoint("https://sqs.us-east-1.amazonaws.com/640562172725");
			
			/**
			   You can use this in your web app where    AwsCredentials.properties is stored in web-inf/classes
			 */
			// AmazonSQS sqs = new AmazonSQSClient(new ClasspathPropertiesFileCredentialsProvider());
			
		} catch (Exception e) {
			System.out.println("exception while creating awss3client : " + e);
		}
	}
	
	public static AWSSimpleQueueServiceUtil getInstance() {
		return awssqsUtil;
	}
	
	public AmazonSQS getAWSSQSClient() {
		return awssqsUtil.sqs;
	}
	
	public String getQueueName() {
		return awssqsUtil.simpleQueue;
	}
	
	/**
	 * Creates a queue in your region and returns the url of the queue
	 * @param queueName
	 * @return
	 */
	public String createQueue(String queueName) {
		CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
		String queueUrl = this.sqs.createQueue(createQueueRequest).getQueueUrl();
		return queueUrl;
	}
	
	/**
	 * returns the queueurl for for sqs queue if you pass in a name
	 * @param queueName
	 * @return
	 */
	public String getQueueUrl(String queueName) {
		GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest(queueName);
		return this.sqs.getQueueUrl(getQueueUrlRequest).getQueueUrl();
	}
	
	/**
	 * lists all your queue.
	 * @return
	 */
	public ListQueuesResult listQueues() {
		return this.sqs.listQueues();
	}
	
	/**
	 * send a single message to your sqs queue
	 * @param queueUrl
	 * @param message
	 */
	public void sendMessageToQueue(String queueUrl, String message) {
		SendMessageResult messageResult = this.sqs.sendMessage(new SendMessageRequest(queueUrl, message));
		System.out.println(messageResult.toString());
	}
	
	/**
	 * gets messages from your queue
	 * @param queueUrl
	 * @return
	 */
	public List<Message> getMessagesFromQueue(String queueUrl) {
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
		receiveMessageRequest.withMaxNumberOfMessages(5);
		receiveMessageRequest.withVisibilityTimeout(20);
		receiveMessageRequest.withWaitTimeSeconds(20);
		receiveMessageRequest.setMaxNumberOfMessages(5);
		receiveMessageRequest.setVisibilityTimeout(20);
		receiveMessageRequest.setWaitTimeSeconds(20);
		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		return messages;
	}
	
	/**
	 * deletes a single message from your queue.
	 * @param queueUrl
	 * @param message
	 */
	public void deleteMessageFromQueue(String queueUrl, Message message) {
		String messageRecieptHandle = message.getReceiptHandle();
		System.out.println("message deleted : " + message.getBody() + "." + message.getReceiptHandle());
		sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageRecieptHandle));
	}
	
	public static void main(String[] args) {
		AWSSimpleQueueServiceUtil awssqsUtil = AWSSimpleQueueServiceUtil.getInstance();
		String queueUrl = awssqsUtil.getQueueUrl(awssqsUtil.getQueueName());
		System.out.println("queueUrl : " + queueUrl);
		List<Message> messages = awssqsUtil.getMessagesFromQueue(queueUrl);
		for (Message message : messages) {
			System.out.println(message.getBody());
		}
		
	}
	
}
