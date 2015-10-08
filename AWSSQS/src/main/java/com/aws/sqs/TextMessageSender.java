package com.aws.sqs;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;

public class TextMessageSender {
	public static void main(String args[]) throws JMSException {
		ExampleConfiguration config = ExampleConfiguration.parseConfig();
		
		// Create the connection factory based on the config
		SQSConnectionFactory connectionFactory = SQSConnectionFactory.builder().withRegion(config.getRegion())
				.withAWSCredentialsProvider(config.getCredentialsProvider()).build();
		
		// Create the connection
		SQSConnection connection = connectionFactory.createConnection();
		// Create the session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		MessageProducer producer = session.createProducer(session.createQueue(config.getQueueName()));
		
		TextMessage message = session.createTextMessage("HelloWorld");
		producer.send(message);
		System.out.println("Send message " + message.getJMSMessageID());
		// sendMessages(session, producer);
		
		// Close the connection. This will close the session automatically
		connection.close();
		System.out.println("Connection closed");
	}
	
	/*private static void sendMessages(Session session, MessageProducer producer) {
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset()));
		
		try {
			String input;
			while (true) {
				System.out.print("Enter message to send (leave empty to exit): ");
				input = inputReader.readLine();
				if (input == null || input.equals(""))
					break;
				
				TextMessage message = session.createTextMessage(input);
				producer.send(message);
				System.out.println("Send message " + message.getJMSMessageID());
			}
		} catch (EOFException e) {
			// Just return on EOF
		} catch (IOException e) {
			System.err.println("Failed reading input: " + e.getMessage());
		} catch (JMSException e) {
			System.err.println("Failed sending message: " + e.getMessage());
			e.printStackTrace();
		}
	}*/
}
