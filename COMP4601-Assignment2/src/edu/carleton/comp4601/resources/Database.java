package edu.carleton.comp4601.resources;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
		userCollection.replaceOne(new Document("docId", user.getDocId()), serialize(user), new UpdateOptions().upsert(true));
	}
	
	public synchronized void insert(WebPage webpage) {
		webpageCollection.replaceOne(new Document("docId", webpage.getDocId()), serialize(webpage), new UpdateOptions().upsert(true));
	}
	
	public Document serialize(User user) {
		Document doc = new Document();
		doc.put("docId", user.getDocId());
		doc.put("name", user.getName());
		doc.put("url", user.getUrl());
		doc.put("preferredGenre", user.getPreferredGenre());
		doc.put("webpages", user.getWebPages());
		HashMap<String, ArrayList<BigDecimal>> sentiments = user.getSentiments();
		for (String genre : sentiments.keySet()) {
			ArrayList<BigDecimal> genreSentiments = sentiments.get(genre);
			ArrayList<String> genreSentimentStrings = new ArrayList<String>();
			for (BigDecimal sentiment : genreSentiments) {
				genreSentimentStrings.add(NaiveBayes.format(sentiment));
			}
			doc.put(genre, genreSentimentStrings);
		}
		doc.put("reviews", user.getReviews()); //might break here
		return doc;
	}
	
	public Document serialize(WebPage webpage) {
		Document doc = new Document();
		doc.put("docId", webpage.getDocId());
		doc.put("name", webpage.getName());
		doc.put("url", webpage.getUrl());
		doc.put("users", webpage.getUsers());
		doc.put("genre", webpage.getGenre());
		doc.put("content", webpage.getContent());
		doc.put("html", webpage.getHTML());
		return doc;
	}
	
	@SuppressWarnings("unchecked")
	public User deserializeUser(Document doc) {
		int docId = doc.getInteger("docId", -1);
		String name = doc.getString("name");
		String url = doc.getString("url");
		String preferredGenre = doc.getString("preferredGenre");
		ArrayList<String> webpages = (ArrayList<String>) doc.get("webpages");
		HashMap<String, ArrayList<BigDecimal>> sentiments = new HashMap<String, ArrayList<BigDecimal>>();
		for (String genre : GenreAnalyzer.GENRES) {
			ArrayList<String> genreSentimentStrings = (ArrayList<String>) doc.get(genre);
			ArrayList<BigDecimal> genreSentiments = new ArrayList<BigDecimal>();
			for (String sentiment : genreSentimentStrings) {
				System.out.println(name);
				System.out.println(sentiment);
				genreSentiments.add(new BigDecimal(sentiment));
			}
			sentiments.put(genre, genreSentiments);
		}
		return new User(docId, name, url, preferredGenre, webpages, sentiments);
	}
	
	@SuppressWarnings("unchecked")
	public WebPage deserializeWebPage(Document doc) {
		int docId = doc.getInteger("docId", -1);
		String name = doc.getString("name");
		String url = doc.getString("url");
		HashSet<String> users = new HashSet<String>((ArrayList<String>) doc.get("users"));
		String genre = doc.getString("genre");
		String content = doc.getString("content");
		String html = doc.getString("html");
		return new WebPage(docId, name, url, users, genre, content, html);
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
		Document query = new Document();
		FindIterable<Document> docs = userCollection.find(query);
		ArrayList<User> users = new ArrayList<User>();
		for (Document doc : docs) {
			users.add(deserializeUser(doc));
		}
		return users;	 
	}
	
	public void setUsers(ArrayList<User> users) {
		userCollection.drop();
		userCollection = database.getCollection("userData");
		
		for (User user : users) {
			insert(user);
		}
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
