package gatetest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NaiveBayesClassifier {
	private ArrayList<String[]> trainingSet;
	
	private double[] prior;
	
	public NaiveBayesClassifier() {
		trainingSet = new ArrayList<String[]>();
		
		prior = new double[2];
	}
	
	public String classify(String[] testInstance, String fileName) {
		loadTrainingSet(fileName);
		calculatePriorProbabilities();		
		double[] posterior = calculatePosteriorNumerators(testInstance);
		double bestProbability = -1;
		String bestClass = "";
		for (int i = 0; i < posterior.length; i++) {
			if (posterior[i] > bestProbability) {
				bestProbability = posterior[i];
				if (i == 0) {
					bestClass = "No";
				} else {
					bestClass = "Yes";
				}
			}
		}
		
		return bestClass;
	}
	
	private double[] calculatePosteriorNumerators(String[] testInstance) {
		double[] posterior = new double[2];
		
		for (int i = 0; i < prior.length; i++) {
			double posteriorNum = prior[i];
			
			String classValue;
			
			if (i == 0) {
				classValue = "No";
			}
			else {
				classValue = "Yes";
			}
			
			for (int j = 0; j < testInstance.length; j++) {
				if (j == testInstance.length - 1) {
					continue;
				}
				
				posteriorNum *= calculateConditionalProbability(j, testInstance[j], classValue);
			}
			
			posterior[i] = posteriorNum;
		}
		
		return posterior;
	}
	
	private double calculateConditionalProbability(int attribute, String attributeValue, String classValue) {
		int total = 0;
		int classValueCount = 0;
		for (String[] trainingInstance : trainingSet) {
			if (!(trainingInstance[trainingInstance.length - 1].equals(classValue))) {
				continue;
			}
			
			classValueCount++;
			for (int i = 0; i < trainingInstance.length; i++) {
				if (i != attribute || i == trainingInstance.length - 1) {
					continue;
				}
				
				if (trainingInstance[i].equals(attributeValue)) {
					total++;
				}
			}
		}
		
		double conditionalProbability = total / (double)classValueCount;
		return conditionalProbability;
	}
	
	private void calculatePriorProbabilities() {
		prior = new double[2];
		String[] classes = new String[trainingSet.size()];
		for (int i = 0; i < trainingSet.size(); i++) {
			classes[i] = trainingSet.get(i)[trainingSet.get(i).length - 1];
		}
		
		int[] classesCounter = new int[2];
		for (int i = 0; i < classes.length; i++) {
			if (classes[i].equals("No")) {
				classesCounter[0]++;
			} else {
				classesCounter[1]++;
			}
		}
		
		for (int i = 0; i < classesCounter.length; i++) {
			prior[i] = (double)classesCounter[i] / classes.length;
		}
	}
	
	private void loadTrainingSet(String filename) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "Cp1252"));
			
			String line;
			while ((line = br.readLine()) != null) {
                String[] splittedLine = line.split(" ");
                String[] instance = new String[]{splittedLine[0], splittedLine[1], splittedLine[2], 
                		splittedLine[3], splittedLine[4]};
                trainingSet.add(instance);
            }
			
            br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
