package assignment3.model;

import assignment1.model.HashTable;

public class Cosine {

	private double cumulativeNumerator, cumulativeFirstFactorDenominator, cumulativeSecondFactorDenominator;
	private double cosine = 0;
	private HashTable a, b;
	
	public Cosine(Node a, Node b) {
		this.a = a.getTable();
		this.b = b.getTable();
		cumulativeNumerator = 0.0;
		cumulativeFirstFactorDenominator = 0.0;
		cumulativeSecondFactorDenominator = 0.0;
	}
	
	public double compute() {
		
		// For each word in table A
		for(int column = 0; column < a.getSize(); ++column) {
			for(assignment1.model.Node row = a.getNode(column); row != null; row = row.getNext()) {
				
				String currentWord = row.getKey();
				int frequencyA = row.getFrequency();
				int frequencyB = 0;
				
				int potentialPositionInB = b.performHashFunction(currentWord);
				
				for(assignment1.model.Node rowInB = b.getNode(potentialPositionInB); rowInB != null; rowInB = rowInB.getNext())
					if(rowInB.getKey().compareTo(currentWord) == 0)
						frequencyB = rowInB.getFrequency();
				
				computeNumerator(frequencyA, frequencyB);
				computeDenominator(frequencyA, frequencyB);
			}
		}
		
		// For all the other words in table B, not in A
		for(int column = 0; column < b.getSize(); ++column) {
			for(assignment1.model.Node row = b.getNode(column); row != null; row = row.getNext()) {
				
				String currentWord = row.getKey();
				int frequencyB = row.getFrequency();
				int frequencyA = 0;
				
				int potentialPositionInA = a.performHashFunction(currentWord);
				
				for(assignment1.model.Node rowInA = a.getNode(potentialPositionInA); rowInA != null; rowInA = rowInA.getNext())
					if(rowInA.getKey().compareTo(currentWord) == 0)
						frequencyA = rowInA.getFrequency();
				
				if(frequencyA == 0) {
//					System.out.print(currentWord + " ");
					computeNumerator(frequencyA, frequencyB);
					computeDenominator(frequencyA, frequencyB);
				}
			}
		}
		
		cosine = cumulativeNumerator / (cumulativeFirstFactorDenominator + cumulativeSecondFactorDenominator);
		return cosine;
	}
	
	private void computeNumerator(double frequencyA, double frequencyB) {
		
		//sum of ((x-y)^2)
		cumulativeNumerator += ((frequencyA - frequencyB) * (frequencyA - frequencyB));
	}
	
	private void computeDenominator(double frequencyA, double frequencyB) {
		
		//sum of all x's
		cumulativeFirstFactorDenominator += frequencyA;
		
		//sum of all y's
		cumulativeSecondFactorDenominator += frequencyB;
	}
	
	public double getCosine() {
		return cosine;
	}
}
