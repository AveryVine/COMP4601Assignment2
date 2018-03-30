package edu.carleton.comp4601.resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

import org.apache.spark.ml.clustering.LDA;
import org.apache.spark.ml.clustering.LDAModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class TopicAnalyzer {
	
	public static ArrayList<WebPage> FILE;
	public static String STOP_FILE = "stop.txt";
	public static String LIBSVM_FILE = "/Users/AveryVine/Documents/COMP4601_recommender_topic_data.txt";
	public static ArrayList<String> STOP_WORDS;
	public static ArrayList<HashMap<String, Integer>> MAPS;
	public static HashMap<String, Integer> MASTER;
	public static ArrayList<Word> TERMS;
	public static int MAX_TERMS = 5;

	public TopicAnalyzer() {
		FILE = Database.getInstance().getWebPages();
		MAPS = new ArrayList<HashMap<String, Integer>>();
		MASTER = new HashMap<String, Integer>();
		TERMS = new ArrayList<Word>();
	}
	
	public void analyze() {
		System.out.println("Reading stop words...");
		readStopWords();
		System.out.println("Reading words...");
		readWords();
		System.out.println("Sorting terms...");
		sortTerms();
		System.out.println("Writing to file...");
		writeToFile();
		System.out.println("Calculating topics...");
		determineTopics();
	}
	
	private void readStopWords() {
		try {
			Scanner s = new Scanner("/Users/AveryVine/Documents/School/Third Year/COMP4601/eclipse-workspace/COMP4601Assignment2/COMP4601-Assignment2/stop.txt");
			STOP_WORDS = new ArrayList<String>();
			while (s.hasNext()) {
				STOP_WORDS.add(s.next());
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void readWords() {
		try {
			for (WebPage doc : FILE) {
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				MAPS.add(map);
				ContentClass content = new ContentClass(doc.getContent());
				while (content.hasNext()) {
					String word = content.next().toLowerCase();
					if (map.containsKey(word)) {
						map.put(word, map.get(word) + 1);
					} else {
						map.put(word, 1);
					}
					if (MASTER.containsKey(word)) {
						MASTER.put(word, MASTER.get(word) + 1);
					} else {
						MASTER.put(word, 1);
					}
				}
				for (String stopWord : STOP_WORDS) {
					map.remove(stopWord);
					MASTER.remove(stopWord);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sortTerms() {
		for (Entry<String, Integer> e : MASTER.entrySet()) {
			int df = 0;
			for (int i = 0; i < FILE.size(); i++)
				if (MAPS.get(i).containsKey(e.getKey()))
					df++;
			TERMS.add(new Word(e.getKey(), e.getValue(), df));
		}
		Collections.sort(TERMS);
	}
	
	private void writeToFile() {
		try {
			BufferedWriter file = new BufferedWriter(new FileWriter(new File(LIBSVM_FILE)), 32768);
			for (int i = 0; i < FILE.size(); i++) {
				file.write(Integer.toString(i));
				for (int j = 0; j < TERMS.size(); j++) {
					file.write(" " + (j + 1) + ":" + getDocWordFreq(i, j));
				}
				file.write("\n");
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static int getDocWordFreq(int docID, int wordID) {
		if (wordID < 0 || wordID > TERMS.size())
			return 0;
		if (docID < 0 || docID > FILE.size())
			return 0;
		Word word = TERMS.get(wordID);
		HashMap<String,Integer> doc = getDocument(docID);
		Integer freq = doc.get(word.w);
		if (freq == null)
			return 0;
		else
			return freq;
	}
	
	private static HashMap<String, Integer> getDocument(int index) {
		if (index < 0 || index > MAPS.size())
			return null;
		return MAPS.get(index);
	}
	
	private static void determineTopics() {
		SparkSession spark = SparkSession
			      .builder()
			      .appName("JavaLDAExample")
			      .master("local")
			      .getOrCreate();

		// Loads data.
		Dataset<Row> dataset = spark.read().format("libsvm")
		  .load(LIBSVM_FILE);

		// Trains a LDA model.
		// Uses 10 topics 
		// Runs the algorithm for 10 iterations
		LDA lda = new LDA().setK(10).setMaxIter(10);
		LDAModel model = lda.fit(dataset);

		double ll = model.logLikelihood(dataset);
		double lp = model.logPerplexity(dataset);
		System.out.println("The lower bound on the log likelihood of the entire corpus: " + ll);
		System.out.println("The upper bound on perplexity: " + lp);

		// Describe topics.
		// Only want to output the 3 top-weighted terms
		Dataset<Row> topics = model.describeTopics(3);
		System.out.println("The topics described by their top-weighted terms:");
		topics.show(false);

		// Shows the result.
		Dataset<Row> transformed = model.transform(dataset);
		transformed.show(false);
		spark.stop();
	}
	
}
