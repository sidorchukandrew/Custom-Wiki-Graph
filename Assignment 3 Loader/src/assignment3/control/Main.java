package assignment3.control;

import assignment3.model.Graph;
import assignment3.utilities.IOUtilities;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) {
		
		System.out.println("Generating graph.");
		Graph graph = new Graph();
		
		System.out.println("Graph created! Attempting to write to a serialized file...");
		
		try {
			IOUtilities.serializeGraph(graph.getRoot());
			System.out.println("Serialization complete.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
