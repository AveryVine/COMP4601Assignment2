package edu.carleton.comp4601.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class SentimentAnalyzer {
		
	public static final String txtPath = "/Users/maximkuzmenko/Desktop/School/Third Year/First Semester/COMP 4601/COMP4601-Assignment2/training/";
	ArrayList<String> negativeText, positiveText, processedPositiveText, processedNegativeText;
	LinkedHashMap<String, Integer> positiveWords, negativeWords;
	HashMap<String, Double> positiveConditionalProbabilities, negativeConditionalProbabilities;
	Set<String> stopwords;
	double probPositive, probNegative;
	int numPositiveReviews, numNegativeReviews, totalVocabulary;
	
	public SentimentAnalyzer() {
		stopwords = new HashSet<String>(readFileToArray(new File(txtPath + "stopwords.txt")));
		negativeText = new ArrayList<String>();
		positiveText = new ArrayList<String>();
		
		System.out.println("stopwords are " + stopwords);
		
		File negDir = new File(txtPath + "neg");
		File posDir = new File(txtPath + "pos");

		numPositiveReviews = 0;
		numNegativeReviews = 0;
		
		for (final File fileEntry : posDir.listFiles()) {
			positiveText.add(readFileToString(fileEntry));
			numPositiveReviews++;
		}
		
		for (final File fileEntry : negDir.listFiles()) {
			negativeText.add(readFileToString(fileEntry));
			numNegativeReviews++;
		}
		
		probPositive = ((float) numPositiveReviews)/((float) (numPositiveReviews + numNegativeReviews));
		probNegative = ((float) numNegativeReviews)/((float) (numPositiveReviews + numNegativeReviews));;

		System.out.println("probPositive is " + probPositive);
		System.out.println("probNegative is " + probNegative);
		
		System.out.println("Processing positive text...");
		processedPositiveText = stripOutStopWordsAndSymbols(positiveText);
		System.out.println("Processing negative text...");
		processedNegativeText = stripOutStopWordsAndSymbols(negativeText);

		System.out.println("Preparing positive vocabulary...");
		positiveWords = prepareVocabulary(processedPositiveText);
		System.out.println("Preparing negative vocabulary...");
		negativeWords = prepareVocabulary(processedNegativeText);
		
		HashSet<String> mutualWords = new HashSet<String>();
		for (String positiveKey : positiveWords.keySet()) {
			if (negativeWords.containsKey(positiveKey)) {
				mutualWords.add(positiveKey);
			}
		}
		LinkedHashMap<String, Integer> tempMap = new LinkedHashMap<>(positiveWords);
		for (String word : tempMap.keySet()) {
			if (!mutualWords.contains(word)) {
				positiveWords.remove(word);
			}
		}
		
		tempMap = new LinkedHashMap<>(negativeWords);
		for (String word : tempMap.keySet()) {
			if (!mutualWords.contains(word)) {
				negativeWords.remove(word);
			}
		}
		
		System.out.println("Calculating count(Positive) value...");
		int positiveCounts = getCountClassValue(positiveWords);
		System.out.println("Calculating count(Negative) value...");
		int negativeCounts = getCountClassValue(negativeWords);
	
		totalVocabulary = mutualWords.size();

		System.out.println("Calculating positive conditional probabilities...");
		positiveConditionalProbabilities = calculateConditionalProbabilities(positiveWords, processedPositiveText, positiveCounts);
		System.out.println("Calculating negative conditional probabilities...");
		negativeConditionalProbabilities = calculateConditionalProbabilities(negativeWords, processedNegativeText, negativeCounts);
	}
	
	public double sentiment(String text) {
		ArrayList<String> sample = new ArrayList<String>();
		sample.add(text);
		sample = stripOutStopWordsAndSymbols(sample);		
		
		ArrayList<String> sample2 = new ArrayList<String>();
		sample2.addAll(Arrays.asList(sample.get(0).split(" ")));
		
		BigDecimal positiveEval = calculatePosteriorProbability(sample2, positiveConditionalProbabilities, probPositive);
		BigDecimal negativeEval = calculatePosteriorProbability(sample2, negativeConditionalProbabilities, probNegative);

		double sentimentScore;
		
		if (positiveEval.compareTo(negativeEval) == 1) {
			sentimentScore = positiveEval.divide(negativeEval).doubleValue();
		}
		else {
			sentimentScore = - (negativeEval.divide(positiveEval).doubleValue());
		}
			
		return sentimentScore;
	}
	
	public BigDecimal calculatePosteriorProbability(ArrayList<String> text, HashMap<String, Double> condProbabilities, double classProbability) {
		BigDecimal probability = BigDecimal.valueOf(1);	
		
		for (String word : text) {
			if (condProbabilities.containsKey(word) && condProbabilities.get(word) > 0) {
				probability = probability.multiply(BigDecimal.valueOf(condProbabilities.get(word)));
			}
		}
		probability = probability.multiply(BigDecimal.valueOf(classProbability));
		return probability;
	}
	
	public HashMap<String, Double> calculateConditionalProbabilities(LinkedHashMap<String, Integer> classWords, ArrayList<String> classDocs, int totalClassWords) {
		HashMap<String, Double> probabilities = new HashMap<String, Double>();
		
		double denominator = totalClassWords + totalVocabulary;
		
		int count = 0;
		for (String word : classWords.keySet()) {
			double numerator = getUniqueWordCountInClass(word, classWords) + 1;
			double result = numerator/denominator;
			probabilities.put(word, result);
		}
		return probabilities;
	}
	
	public static void main(String[] args) {
		SentimentAnalyzer analyzer = new SentimentAnalyzer();
		System.out.println("Done.");	
	}
	
	//count (w | c )
	public int getUniqueWordCountInClass(String word, LinkedHashMap<String, Integer> classWords) {
		return classWords.containsKey(word)? classWords.get(word) : 0;
	}
	
	//count (c) 
	public int getCountClassValue(LinkedHashMap<String, Integer> classWords) {
		int count = 0;
		for (Integer value : classWords.values()) {
			count += value;
		}
		return count;
	}
	
	public LinkedHashMap<String, Integer> prepareVocabulary(ArrayList<String> documents) {
		int count = 0;
		
		ArrayList<String> tempTextProcessing = new ArrayList<String>();
		
		for (String str : documents) {
			tempTextProcessing.addAll(Arrays.asList(str.split(" ")));
		}
		
		HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
		
		for (String word : tempTextProcessing) {
			if (!wordCount.containsKey(word))  {
				wordCount.put(word, 1);
			}
			else {
				wordCount.put(word, wordCount.get(word) + 1);
			}
		}
		
		LinkedHashMap<String, Integer> tempMap = new LinkedHashMap<String, Integer>(sortByValue(wordCount));
		
		LinkedHashMap<String, Integer> topWords = new LinkedHashMap<String, Integer>();
		
		int numValues = 0;
		for (Entry<String, Integer> entry : tempMap.entrySet()) {
			topWords.put(entry.getKey(), entry.getValue());
			numValues++;
			if (numValues > 1000) {
				break;
			}
		}
		
		return topWords;	
	}

	@SuppressWarnings("hiding")
	public static <String, Integer extends Comparable<? super Integer>> LinkedHashMap<String, Integer> sortByValue(Map<String, Integer> map) {
        List<Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((o2, o1) -> o1.getValue().compareTo(o2.getValue()));
        
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list) {
        		result.put(entry.getKey(), entry.getValue());
        }
        
        return result;
    }
	
	public ArrayList<String> stripOutStopWordsAndSymbols(ArrayList<String> strList) {
		ArrayList<String> newText = new ArrayList<String>();
		String currTextWord;
		
		for (String text : strList) {
			List<String> textList = Arrays.asList(text.replaceAll("\n", "").split(" "));
			List<String> removedTextList = new ArrayList<String>();
			for (int i = 0; i < textList.size(); i++) {
				currTextWord = textList.get(i);
				if (!stopwords.contains(currTextWord) && !currTextWord.matches("^.*[^a-zA-Z0-9 ].*$")) {
					removedTextList.add(textList.get(i));
				}
			}
			
			newText.add(String.join(" ", removedTextList));
		}
		return newText;
	}
	
	@SuppressWarnings("resource")
	public static String readFileToString(File file) {
		String content = "";
		try {
			content = new Scanner(file).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	public ArrayList<String> readFileToArray(File file) {
		ArrayList<String> strArray = new ArrayList<String>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				strArray.add(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if (scanner != null) {
				scanner.close();
			}
		} 
		return strArray;
	}
	
}
