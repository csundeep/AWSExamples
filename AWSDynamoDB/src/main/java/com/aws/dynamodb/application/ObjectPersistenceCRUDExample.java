package com.aws.dynamodb.application;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.aws.dynamodb.model.CatalogItem;

public class ObjectPersistenceCRUDExample {

	private final static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();

	public static void main(String[] args) throws IOException {
		testCRUDOperations();
		System.out.println("Example complete!");
	}

	private static void testCRUDOperations() {

		CatalogItem item = new CatalogItem();
		item.setId(602);
		item.setTitle("Book 601");
		item.setISBN("511-1111111111");
		item.setBookAuthors(new HashSet<String>(Arrays.asList("Author1", "Author2")));

		// Save the item (book).
		DynamoDBMapper mapper = new DynamoDBMapper(client);
		mapper.save(item);

		// Retrieve the item.
		CatalogItem itemRetrieved = mapper.load(CatalogItem.class, 602);
		System.out.println("Item retrieved:");
		System.out.println(itemRetrieved);

		// Update the item.
		itemRetrieved.setISBN("522-2222222222");
		itemRetrieved.setBookAuthors(new HashSet<String>(Arrays.asList("Author1", "Author3")));
		mapper.save(itemRetrieved);
		System.out.println("Item updated:");
		System.out.println(itemRetrieved);

		// Retrieve the updated item.
		DynamoDBMapperConfig config = new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.EVENTUAL);
		CatalogItem updatedItem = mapper.load(CatalogItem.class, 602, config);
		System.out.println("Retrieved the previously updated item:");
		System.out.println(updatedItem);

		// Delete the item.
		mapper.delete(updatedItem);

		// Try to retrieve deleted item.
		CatalogItem deletedItem = mapper.load(CatalogItem.class, updatedItem.getId(), config);
		if (deletedItem == null) {
			System.out.println("Done - Sample item is deleted.");
		}
	}
}