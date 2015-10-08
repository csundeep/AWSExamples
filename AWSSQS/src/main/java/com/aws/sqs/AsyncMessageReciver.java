package com.aws.sqs;

import java.util.concurrent.TimeUnit;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.util.Base64;

public class AsyncMessageReciver {
	public static void main(String args[]) throws JMSException, InterruptedException {
		
		ExampleConfiguration config = ExampleConfiguration.parseConfig();
		SQSConnectionFactory connectionFactory = SQSConnectionFactory.builder().withRegion(config.getRegion())
				.withAWSCredentialsProvider(config.getCredentialsProvider()).build();
		
		// Create the connection
		SQSConnection connection = connectionFactory.createConnection();
		// ExampleCommon.setupLogging();
		
		// Create the session
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		MessageConsumer consumer = session.createConsumer(session.createQueue(config.getQueueName()));
		
		ReceiverCallback callback = new ReceiverCallback();
		consumer.setMessageListener(callback);
		
		// No messages will be processed until this is called
		connection.start();
		
		callback.waitForOneMinuteOfSilence();
		System.out.println("Returning after one minute of silence");
		
		// Close the connection. This will close the session automatically
		connection.close();
		System.out.println("Connection closed");
	}
	
	private static class ReceiverCallback implements MessageListener {
		// Used to listen for message silence
		private volatile long timeOfLastMessage = System.nanoTime();
		
		public void waitForOneMinuteOfSilence() throws InterruptedException {
			for (;;) {
				long timeSinceLastMessage = System.nanoTime() - timeOfLastMessage;
				long remainingTillOneMinuteOfSilence = TimeUnit.MINUTES.toNanos(1) - timeSinceLastMessage;
				if (remainingTillOneMinuteOfSilence < 0) {
					break;
				}
				TimeUnit.NANOSECONDS.sleep(remainingTillOneMinuteOfSilence);
			}
		}
		
		public void onMessage(Message message) {
			try {
				handleMessage(message);
				message.acknowledge();
				System.out.println("Acknowledged message " + message.getJMSMessageID());
				timeOfLastMessage = System.nanoTime();
			} catch (JMSException e) {
				System.err.println("Error processing message: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		public static void handleMessage(Message message) throws JMSException {
			System.out.println("Got message " + message.getJMSMessageID());
			System.out.println("Content: ");
			if (message instanceof TextMessage) {
				TextMessage txtMessage = (TextMessage) message;
				System.out.println("\t" + txtMessage.getText());
			} else if (message instanceof BytesMessage) {
				BytesMessage byteMessage = (BytesMessage) message;
				// Assume the length fits in an int - SQS only supports sizes up to 256k so that
				// should be true
				byte[] bytes = new byte[(int) byteMessage.getBodyLength()];
				byteMessage.readBytes(bytes);
				System.out.println("\t" + Base64.encodeAsString(bytes));
			} else if (message instanceof ObjectMessage) {
				ObjectMessage objMessage = (ObjectMessage) message;
				System.out.println("\t" + objMessage.getObject());
			}
		}
		
	}
}
