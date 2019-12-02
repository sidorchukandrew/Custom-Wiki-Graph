package assignment3.control;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Stack;

import assignment3.model.Edge;
import assignment3.model.Graph;
import assignment3.model.Node;

public class GraphUtilities {

	public static ArrayList<String> getAllWebsites(Node root) {
		
		ArrayList<String> websites = new ArrayList<String>();
		Hashtable<Integer, String> allWebsites = new Hashtable<Integer, String>();
		
		websites = visitEach(root, websites);
		websites.add(root.getName());
		
		for(String website : websites)
			allWebsites.put(website.hashCode(), website);
		
		websites = new ArrayList<String>();
		
		for(String website : allWebsites.values())
			websites.add(website);
		
		return websites;
	}
	
	private static ArrayList<String> visitEach(Node root, ArrayList<String> websites) {
		
		long currentMarker = root.getMark() + 1;
		ArrayDeque<Node> stack = new ArrayDeque<Node>();
		stack.addFirst(root);
		
		while(!stack.isEmpty()) {
			
			Node beingChecked = stack.removeFirst();
			
			if(beingChecked.getMark() == currentMarker) continue;
			else {
				websites.add(beingChecked.getName());
				for(Edge e : beingChecked.getEdges()) {
					beingChecked.setMark(currentMarker);
					stack.addFirst(e.getDst());
				}
			}
		}
		
		return websites;
	}
	
	public static Stack dijkstra(Node src, String dst) {
		
		// Mark all nodes as unvisited
		reinitialize(Main.getGraphRoot());
		
		// Keeps track of unvisited nodes
		PriorityQueue<Edge> unvisited = new PriorityQueue<Edge>();
		
		// Initial node gets distance of 0
		src.setDistance(0);
		Node current = src;
		
		unvisited.add(new Edge(null, current, 0));
		
		while(true) {
			// Consider all of current node's neighbors
			for(Edge e : current.getEdges()) {
				
				// Calculate the tentative distance based on the edge weight
				double tentativeDistance = current.getDistance() + e.getWeight();
				
				// If the distance is smaller, assign it the tentative one
				if(e.getDst().getDistance() > tentativeDistance) {
					e.getDst().setDistance(tentativeDistance);
					e.getDst().setParent(current);
					
					// Decrease priority
					unvisited.remove(e);
					unvisited.add(e);
				}
			}
			
			current = unvisited.poll().getDst();
			if(current.getName().compareTo(dst) == 0) {
				System.out.println("Found!");
				break;
			}
			
			if(unvisited.isEmpty() || unvisited.peek().getDst().getDistance() == Double.MAX_VALUE) {
				System.out.println("Not found");
				current = null;
				break;
			}
			
		}
		
		Stack<String> path = new Stack<String>();
		while(current != null) {
			path.push(current.getName());
			current = current.getParent();
		}
		
		return path;
	}
	
	public static void reinitialize(Node root) {
		
		long currentMarker = root.getMark() + 1;
		ArrayDeque<Node> stack = new ArrayDeque<Node>();
		stack.addFirst(root);
		
		while(!stack.isEmpty()) {
			
			Node beingChecked = stack.removeFirst();
			
			if(beingChecked.getMark() == currentMarker) continue;
			else {
				beingChecked.setMark(currentMarker);
				beingChecked.setDistance(Double.MAX_VALUE);
				beingChecked.setParent(null);
				
				for(Edge e : beingChecked.getEdges()) 
					stack.addFirst(e.getDst());
			}
		}
	}
	
	public static Node find(Node root, String nameOfNodeToFind) {
		
		long currentMarker = root.getMark() + 1;
		ArrayDeque<Node> stack = new ArrayDeque<Node>();
		stack.addFirst(root);
		
		while(!stack.isEmpty()) {
			
			Node beingChecked = stack.removeFirst();
			
			if(beingChecked.getName().compareTo(nameOfNodeToFind) == 0) return beingChecked;
			
			if(beingChecked.getMark() == currentMarker) continue;
			else 
				for(Edge e : beingChecked.getEdges()) {
					beingChecked.setMark(currentMarker);
					stack.addFirst(e.getDst());
				}
		}
		return null;
	}
}
