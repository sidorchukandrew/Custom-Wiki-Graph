package assignment3.control;

import assignment3.model.Graph;
import assignment3.utilities.IOUtilities;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {
		
//		System.out.println("Generating graph.");
//		Graph graph = new Graph();
//		
//		System.out.println(graph.getMasterEdges().size());
//		System.out.println("Graph created! Attempting to write to a serialized file...");
//		
//		try {
//			IOUtilities.serializeGraph(graph);
//			System.out.println("Serialization complete.");
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
		
		Graph graph = null;
		System.out.println("Reading from file.");
		try {
			graph = IOUtilities.readActualGraph("serializedTEMP.txt");
			System.out.println("Reading complete.");
			
			System.out.println("Number of disjoint sets : " + graph.numberOfDisjointSets());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
