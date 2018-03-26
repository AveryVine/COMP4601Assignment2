package edu.carleton.comp4601.resources;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class Database {

	static Database instance;
	MongoCollection<Document> userCollection;
	MongoClient mongoClient;
	MongoDatabase database;
	
	public Database() {
		mongoClient = new MongoClient("localhost", 27017);
		database = mongoClient.getDatabase("assignment2");
		
		userCollection = database.getCollection("userData");
	}
	
	public void insert(User user) {
		userCollection.insertOne(serialize(user));
	}
	
	private Document serialize(User user) {
		Document doc = new Document();
		return doc;
	}
	
	public void clear() {
		userCollection.drop();
	}
	
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}
	
}
