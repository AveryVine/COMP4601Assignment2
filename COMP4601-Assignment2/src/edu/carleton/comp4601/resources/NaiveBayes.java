package edu.carleton.comp4601.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
		
		trainingPath = "/Users/maximkuzmenko/Desktop/School/Third Year/First Semester/COMP 4601/COMP4601Assignment2/COMP4601-Assignment2/training/";
//		trainingPath = "/Users/AveryVine/Documents/School/Third Year/COMP4601/eclipse-workspace/COMP4601Assignment2/COMP4601-Assignment2/training/";
		
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
	
	protected ArrayList<BigDecimal> processText(String text) {
		text = cleanText(text);
		ArrayList<String> words = new ArrayList<String>(Arrays.asList(text.split(" ")));
		
		ArrayList<BigDecimal> scores = calculateClassScores(words);
		return scores;
	}
	
	private void readStopWords() {
		System.out.println("Reading stop words...");
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
		System.out.println("Reading class text...");
		totalClassDocs = 0;
		for (int i = 0; i < classes.size(); i++) {
			File classDirectory = new File(trainingPath + "/" + classes.get(i));
			ArrayList<String> classText = new ArrayList<String>();
			for (File file : classDirectory.listFiles()) {
				if (file.getAbsolutePath().contains(".html") || file.getAbsolutePath().contains(".txt")) {
					classText.add(readClassFile(file));
					totalClassDocs++;
				}
			}
			classTexts.add(classText);
		}
	}
	
	private void calculateClassPriors() {
		System.out.println("Calculating class priors...");
		for (int i = 0; i < classes.size(); i++) {
			classPriors.add((double) (((float) classTexts.get(i).size()) / ((float) totalClassDocs)));
		}
		
	}
	
	private void cleanClassTexts() {
		System.out.println("Cleaning class texts...");
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
		text = text.toLowerCase().replaceAll("[^A-Za-z0-9 ]", " ").trim().replaceAll(" +", " ");
		
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
		System.out.println("Determining top words...");
		ArrayList<LinkedHashMap<String, Integer>> tempClassWordMaps = new ArrayList<LinkedHashMap<String, Integer>>();
		for (int i = 0; i < classes.size(); i++) {
			ArrayList<String> classText = classTexts.get(i);
			
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
			
			tempClassWordMaps.add(sortedWordMap);
		}
		
		classWordMaps = onlyKeepMutualWords(tempClassWordMaps);
		if (classWordMaps.size() > 0) {
			totalVocabulary = classWordMaps.get(0).size();
		}
	}
	
	public ArrayList<LinkedHashMap<String, Integer>> onlyKeepMutualWords(ArrayList<LinkedHashMap<String, Integer>> mapList) {
		HashSet<String> mutualWords = new HashSet<String>();
		if (mapList.size() == 0) {
			return mapList;
		}
		for (String key : mapList.get(0).keySet()) {
			boolean canAdd = true;
			for (int i = 1; i < mapList.size(); i++) {
				LinkedHashMap<String, Integer> map = mapList.get(i);
				if (!map.containsKey(key)) {
					canAdd = false;
					break;
				}
			}
			if (canAdd) {
				mutualWords.add(key);
			}
		}
		
		ArrayList<LinkedHashMap<String, Integer>> tempMapList = new ArrayList<LinkedHashMap<String, Integer>>();
		
		for (int i = 0; i < mapList.size(); i++) {
			LinkedHashMap<String, Integer> tempMap = new LinkedHashMap<String, Integer>(mapList.get(i));
			for (String word : mapList.get(i).keySet()) {
				if (!mutualWords.contains(word)) {
					tempMap.remove(word);
				}
			}
			tempMapList.add(tempMap);
		}
		
		return tempMapList;
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
		System.out.println("Counting class values...");
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
		System.out.println("Calculating conditional word probabilities...");
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
	
	protected ArrayList<BigDecimal> calculateClassScores(ArrayList<String> words) {
		ArrayList<BigDecimal> classScores = new ArrayList<BigDecimal>();
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
			classScores.add(probability);
		}
		return classScores;
	}
	
	public ArrayList<String> getClasses() {
		return classes;
	}

}
