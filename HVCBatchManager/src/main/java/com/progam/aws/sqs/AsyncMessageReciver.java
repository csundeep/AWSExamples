package com.progam.aws.sqs;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

public class AsyncMessageReciver {
	public static void main(String args[]) throws JMSException, InterruptedException {
		
		SQSConfiguration config = SQSConfiguration.parseConfig();
		SQSConnectionFactory connectionFactory = SQSConnectionFactory.builder().withRegion(config.getRegion())
				.withAWSCredentialsProvider(config.getCredentialsProvider()).build();
		
		// Create the connection
		SQSConnection connection = connectionFactory.createConnection();
		// Create the session
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		MessageConsumer consumer = session.createConsumer(session.createQueue(config.getQueueName()));
		ReceiverCallback callback = new ReceiverCallback();
		consumer.setMessageListener(callback);
		connection.start();
		callback.waitForOneMinuteOfSilence();
		connection.close();
		System.out.println("Connection closed");
	}
	
	private static class ReceiverCallback implements MessageListener {
		public void waitForOneMinuteOfSilence() throws InterruptedException {
			for (;;) {
			}
		}
		
		public void onMessage(Message message) {
			try {
				String fileName = getFileName(message);
				AWSDownload.downloadFile(fileName);
				message.acknowledge();
				System.out.println("Acknowledged message " + message.getJMSMessageID());
			} catch (Exception e) {
				System.err.println("Error processing message: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		public static String getFileName(Message message) throws JSONException {
			
			TextMessage txtMessage = (TextMessage) message;
			// JSONObject records = new JSONObject(txtMessage);
			// JSONArray recordArray = records.getJSONArray("Records");
			// JSONObject record = new JSONObject(recordArray.get(0).toString());
			// JSONObject s3 = new JSONObject(record.get("s3").toString());
			// JSONObject object = new JSONObject(s3.get("object").toString());
			// fileName = object.get("key").toString();
			String fileName = new JSONObject(new JSONObject(new JSONObject(new JSONObject(txtMessage).getJSONArray("Records").get(0)
					.toString()).get("s3").toString()).get("object").toString()).get("key").toString();
			System.out.println(fileName);
			
			return fileName;
		}
	}
}
