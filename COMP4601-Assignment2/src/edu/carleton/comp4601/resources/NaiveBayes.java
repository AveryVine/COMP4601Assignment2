package edu.carleton.comp4601.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

public abstract class NaiveBayes {
	
	protected String trainingPath;
	protected ArrayList<String> classes;
	protected ArrayList<Double> classPriors;
	protected ArrayList<ArrayList<String>> classTexts;
	protected ArrayList<Integer> classValues;
	protected LinkedHashMap<String, Integer> topWords;
	protected ArrayList<LinkedHashMap<String, Integer>> classWordMaps;
	protected ArrayList<HashMap<String, Double>> classConditionalProbabilities;
	protected HashSet<String> stopWords;
	protected int totalClassDocs, totalVocabulary;
	
	protected NaiveBayes(ArrayList<String> classes) {
		this.classes = classes;
		
		trainingPath = "/Users/AveryVine/Documents/School/Third Year/COMP4601/eclipse-workspace/COMP4601Assignment2/COMP4601-Assignment2/training/";
		
		classPriors = new ArrayList<Double>();
		classTexts = new ArrayList<ArrayList<String>>();
		classValues = new ArrayList<Integer>();
		topWords = new LinkedHashMap<String, Integer>();
		classWordMaps = new ArrayList<LinkedHashMap<String, Integer>>();
		classConditionalProbabilities = new ArrayList<HashMap<String, Double>>();
		stopWords = new HashSet<String>();
		
		readStopWords();
		readClassText();
		calculateClassPriors();
		cleanClassTexts();
		determineTopWords();
		countClassValues();
		calculateConditionalWordProbabilities();
	}
	
	public abstract void analyze();
	
	protected ArrayList<Double> processText(String text) {
		text = cleanText(text);
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(text.split(" ")));
		
		ArrayList<Double> scores = calculateClassScores(words);
		return scores;
	}
	
	private void readStopWords() {
		File file = new File(trainingPath + "stopwords.txt");
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				stopWords.add(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if (scanner != null) {
				scanner.close();
			}
		} 
	}
	
	@SuppressWarnings("resource")
	private String readClassFile(File file) {
		String content = "";
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				content += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	private void readClassText() {
		totalClassDocs = 0;
		for (int i = 0; i < classes.size(); i++) {
			File classDirectory = new File(trainingPath + "/" + classes.get(i));
			ArrayList<String> classText = new ArrayList<String>();
			for (File file : classDirectory.listFiles()) {
				if (file.getAbsolutePath().contains(".html")) {
					classText.add(readClassFile(file));
					totalClassDocs++;
				}
			}
			classTexts.add(classText);
		}
	}
	
	private void calculateClassPriors() {
		for (int i = 0; i < classes.size(); i++) {
			classPriors.add((double) (((float) classTexts.get(i).size()) / ((float) totalClassDocs)));
		}
		
	}
	
	private void cleanClassTexts() {
		ArrayList<ArrayList<String>> cleanedClassTexts = new ArrayList<ArrayList<String>>();
		for (ArrayList<String> classText : classTexts) {
			ArrayList<String> cleanedClassText = new ArrayList<String>();
			for (String text: classText) {
				cleanedClassText.add(cleanText(text));
			}
			cleanedClassTexts.add(cleanedClassText);
		}
		classTexts = cleanedClassTexts;
	}
	
	protected String cleanText(String text) {
		List<String> textList = Arrays.asList(text.split(" "));
		List<String> removedTextList = new ArrayList<String>();
		for (int i = 0; i < textList.size(); i++) {
			String currTextWord = textList.get(i);
			if (!stopWords.contains(currTextWord) && !currTextWord.matches("^.*[^a-zA-Z0-9 ].*$") && !currTextWord.equals("")) {
				removedTextList.add(textList.get(i));
			}
		}
		return String.join(" ", removedTextList);
	}
	
	private void determineTopWords() {
		LinkedHashMap<String, Integer> tempTopWords = new LinkedHashMap<String, Integer>();
		
		for (int i = 0; i < classes.size(); i++) {
			ArrayList<String> classText = classTexts.get(i);
			LinkedHashMap<String, Integer> classWordMap = new LinkedHashMap<String, Integer>();
			
			ArrayList<String> wordsToProcess = new ArrayList<String>();
			for (String text : classText) {
				wordsToProcess.addAll(Arrays.asList(text.split(" ")));
			}
			
			HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
			
			for (String word : wordsToProcess) {
				int count = 0;
				if (wordMap.containsKey(word)) {
					count = wordMap.get(word);
				}
				wordMap.put(word,  count + 1);
			}
			
			LinkedHashMap<String, Integer> sortedWordMap = new LinkedHashMap<String, Integer>(sortByValue(wordMap));
			
			classWordMap.putAll(sortedWordMap);
			classWordMaps.add(classWordMap);
			
			tempTopWords.putAll(sortedWordMap);
		}
		
		for (Entry<String, Integer> entry : tempTopWords.entrySet()) {
			topWords.put(entry.getKey(), entry.getValue());
			if (topWords.size() > totalClassDocs * 10) {
				totalVocabulary = topWords.size();
				break;
			}
		}
	}
	
	private static <K, V extends Comparable<? super Integer>> LinkedHashMap<String, Integer> sortByValue(Map<String, Integer> map) {
        List<Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((o2, o1) -> o1.getValue().compareTo(o2.getValue()));
        
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list) {
        		result.put(entry.getKey(), entry.getValue());
        }
        
        return result;
    }
	
	private void countClassValues() {
		for (int i = 0; i < classes.size(); i++) {
			int count = 0;
			for (Integer value : classWordMaps.get(i).values()) {
				count += value;
			}
			classValues.add(count);
		}
	}
	
	private int getCountOfWordInClass(String word, LinkedHashMap<String, Integer> classWords) {
		return classWords.containsKey(word) ? classWords.get(word) : 0;
	}
	
	private void calculateConditionalWordProbabilities() {
		for (int i = 0; i < classes.size(); i++) {
			HashMap<String, Double> classProbabilities = new HashMap<String, Double>();
			LinkedHashMap<String, Integer> classWords = classWordMaps.get(i);
			double denominator = classValues.get(i) + totalVocabulary; 
			for (String word: classWords.keySet()) {
				double numerator = getCountOfWordInClass(word, classWords) + 1;
				double result = numerator / denominator;
				classProbabilities.put(word, result);
			}
			classConditionalProbabilities.add(classProbabilities);
		}
	}
	
	protected ArrayList<Double> calculateClassScores(ArrayList<String> words) {
		ArrayList<Double> classScores = new ArrayList<Double>();
		for (int i = 0; i < classes.size(); i++) {
			HashMap<String, Double> conditionalProbabilities = classConditionalProbabilities.get(i);
			Double classPrior = classPriors.get(i);
			BigDecimal probability = BigDecimal.valueOf(1);
			
			for (String word : words) {
				if (conditionalProbabilities.containsKey(word) && conditionalProbabilities.get(word) > 0) {
					probability = probability.multiply(BigDecimal.valueOf(conditionalProbabilities.get(word)));
				}
			}
			probability = probability.multiply(BigDecimal.valueOf(classPrior));
			//TODO: probabilities are getting so small that when they are converted to a double they become 0
			classScores.add(probability.doubleValue());
		}
		return classScores;
	}
	
	public ArrayList<String> getClasses() {
		return classes;
	}

}
