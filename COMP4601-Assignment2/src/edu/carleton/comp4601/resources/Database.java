package edu.carleton.comp4601.resources;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.uci.ics.crawler4j.url.WebURL;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bson.Document;

public class Database {

	static Database instance;
	MongoCollection<Document> userCollection, webpageCollection;
	MongoClient mongoClient;
	MongoDatabase database;
	
	public Database() {
		mongoClient = new MongoClient("localhost", 27017);
		database = mongoClient.getDatabase("assignment2");
		
		userCollection = database.getCollection("userData");
		webpageCollection = database.getCollection("webpageData");
	}
	
	public synchronized void insert(User user) {
		userCollection.insertOne(user);
	}
	
	public synchronized void insert(WebPage webpage) {
		webpageCollection.insertOne(webpage);
	}

	public void clear() {
		userCollection.drop();
		webpageCollection.drop();
	}
	
	@SuppressWarnings("unchecked")
	public User getUser(String name) {
		org.bson.Document query = new org.bson.Document("name", name);
		FindIterable<Document> result = userCollection.find(query);
	
		Document user = result.first();
		return user != null? (User) user : null;
	}
	
	public ArrayList<User> getUsers() {
		ArrayList<Document> documents = (ArrayList<Document>) userCollection.find().into(new ArrayList<Document>());
		System.out.println("documentsNum is " + documents.size());
		ArrayList<User> userList = new ArrayList<User>();
		int count = 0;
		for (Document doc : documents) {
			System.out.println("has next " + count++ + ", name is " + doc.getString("name"));
			userList.add(new User(doc));
		}
//		userList.add(new User(11, "bob", new HashSet<WebURL>()));
//		userList.add(new User(14, "hones", new HashSet<WebURL>()));

		return userList;	 
	}
	
	@SuppressWarnings("unchecked")
	public WebPage getWebPage(String name) {
		org.bson.Document query = new org.bson.Document("name", name);
		FindIterable<Document> result = webpageCollection.find(query);
	
		Document page = result.first();
		return page != null? (WebPage) page : null;
	}
	
	public boolean userExists(String name) {
		org.bson.Document query = new org.bson.Document("name", name);
		FindIterable<Document> result = userCollection.find(query);
		return result.first() != null? true : false;
	}
	
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}
	
}
