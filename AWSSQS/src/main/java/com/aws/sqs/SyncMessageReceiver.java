package com.aws.sqs;

import java.util.concurrent.TimeUnit;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.util.Base64;

public class SyncMessageReceiver {
	public static void main(String args[]) throws JMSException {
		ExampleConfiguration config = ExampleConfiguration.parseConfig();
		
		// Create the connection factory based on the config
		SQSConnectionFactory connectionFactory = SQSConnectionFactory.builder().withRegion(config.getRegion())
				.withAWSCredentialsProvider(config.getCredentialsProvider()).build();
		
		// Create the connection
		SQSConnection connection = connectionFactory.createConnection();
		
		// Create the session
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		MessageConsumer consumer = session.createConsumer(session.createQueue(config.getQueueName()));
		
		connection.start();
		
		receiveMessages(session, consumer);
		
		// Close the connection. This will close the session automatically
		connection.close();
		System.out.println("Connection closed");
	}
	
	private static void receiveMessages(Session session, MessageConsumer consumer) {
		try {
			while (true) {
				System.out.println("Waiting for messages");
				// Wait 1 minute for a message
				Message message = consumer.receive(TimeUnit.MINUTES.toMillis(1));
				if (message == null) {
					System.out.println("Shutting down after 1 minute of silence");
					break;
				}
				handleMessage(message);
				message.acknowledge();
				System.out.println("Acknowledged message " + message.getJMSMessageID());
			}
		} catch (JMSException e) {
			System.err.println("Error receiving from SQS: " + e.getMessage());
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
