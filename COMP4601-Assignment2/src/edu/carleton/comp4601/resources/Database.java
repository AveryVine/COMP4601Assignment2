package edu.carleton.comp4601.resources;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;

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
		userCollection.insertOne(serialize(user));
	}
	
	public synchronized void insert(WebPage webpage) {
		webpageCollection.insertOne(serialize(webpage));
	}
	
	public Document serialize(User user) {
		Document doc = new Document();
		doc.put("docId", user.getDocId());
		doc.put("name", user.getName());
		doc.put("url", user.getUrl());
		doc.put("webpages", user.getWebPages());
		return doc;
	}
	
	public Document serialize(WebPage webpage) {
		Document doc = new Document();
		doc.put("docId", webpage.getDocId());
		doc.put("name", webpage.getName());
		doc.put("url", webpage.getUrl());
		doc.put("users", webpage.getUsers());
		doc.put("content", webpage.getContent());
		doc.put("html", webpage.getHTML());
		return doc;
	}
	
	@SuppressWarnings("unchecked")
	public User deserializeUser(Document doc) {
		int docId = doc.getInteger("docId", -1);
		String name = doc.getString("name");
		String url = doc.getString("url");
		ArrayList<String> webpages = (ArrayList<String>) doc.get("webpages");
		return new User(docId, name, url, webpages);
	}
	
	@SuppressWarnings("unchecked")
	public WebPage deserializeWebPage(Document doc) {
		int docId = doc.getInteger("docId", -1);
		String name = doc.getString("name");
		String url = doc.getString("url");
		ArrayList<String> users = (ArrayList<String>) doc.get("users");
		String content = doc.getString("content");
		String html = doc.getString("html");
		return new WebPage(docId, name, url, users, content, html);
	}

	public void clear() {
		userCollection.drop();
		webpageCollection.drop();
	}
	
	public User getUser(String name) {
		Document query = new Document("name", name);
		FindIterable<Document> result = userCollection.find(query);
		Document doc = result.first();
		if (doc != null) {
			return deserializeUser(doc);
		}
		return null;
	}
	
	public ArrayList<User> getUsers() {
		ArrayList<Document> docs = (ArrayList<Document>) userCollection.find().into(new ArrayList<Document>());
		System.out.println("Number of user documents: " + docs.size());
		ArrayList<User> userList = new ArrayList<User>();
		for (Document doc : docs) {
			userList.add(deserializeUser(doc));
		}
		return userList;	 
	}
	
	public WebPage getWebPage(String name) {
		Document query = new Document("name", name);
		FindIterable<Document> result = webpageCollection.find(query);
		Document doc = result.first();
		if (doc != null) {
			return deserializeWebPage(doc);
		}
		return null;
	}
	
	public ArrayList<WebPage> getWebPages() {
		ArrayList<Document> docs = (ArrayList<Document>) webpageCollection.find().into(new ArrayList<Document>());
		System.out.println("Number of webpage documents: " + docs.size());
		ArrayList<WebPage> webpageList = new ArrayList<WebPage>();
		for (Document doc : docs) {
			webpageList.add(deserializeWebPage(doc));
		}
		return webpageList;	 
	}
	
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}
	
}
