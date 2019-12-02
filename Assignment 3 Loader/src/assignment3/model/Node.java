package assignment3.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

import assignment1.model.HashTable;
import assignment3.utilities.IOUtilities;

public class Node implements Serializable{

	private static final long serialVersionUID = 1L;
	private Set <Edge> edges;
	private String name;
	private transient HashTable srcTable;
	private transient ScrapeResult srcScraped;
	private long mark;
	private Node parent;
	private double distance;
	
	public Node(String name, int n) {
		mark = 0;
		this.name = name;
		edges = new HashSet<Edge>();
		srcScraped = null;
		distance = Double.MAX_VALUE;
	}
	
	public Node(String name) {
		
		mark = 0;
		this.name = name;
		edges = new HashSet<Edge>();
		srcScraped = null;
		
		try {
			srcScraped = IOUtilities.scrapeWebsite(name);
			
			srcTable = Graph.constructHashtable(srcScraped.getWords(), name);
			for(String word : srcScraped.getWords()) 
				srcTable.addWord(word);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		distance = Double.MAX_VALUE;
	}
	
	public Node find(Node src, Node nodeToFind) {
		
		Graph.currentMarker++;
		ArrayDeque<Node> stack = new ArrayDeque<Node>();
		stack.addFirst(src);
		
		while(!stack.isEmpty()) {
			
			Node beingChecked = stack.removeFirst();
			
			if(beingChecked.getName().compareTo(nodeToFind.getName()) == 0) return beingChecked;
			
			if(beingChecked.mark == Graph.currentMarker) continue;
			else 
				for(Edge e : beingChecked.edges) {
					beingChecked.mark = Graph.currentMarker;
					stack.addFirst(e.getDst());
				}
		}
		return null;
	}

	public void add(Node dst, double cosine) {
		
		if(getName().compareTo(dst.getName()) == 0)
			return;
		
		Node possibleFind = find(Graph.getRoot(), dst);
		
		if(possibleFind != null) 
			edges.add(new Edge(this, possibleFind, cosine));
		else
			edges.add(new Edge(this, dst, cosine));
	}
	
	public void printName() {
		System.out.println(name);
	}
	
	public String getName() {
		return name;
	}
	
	public HashTable getTable() {
		return srcTable;
	}
	
	public void setTable(HashTable table) {
		srcTable = table;
	}
	
	public Set<Edge> getEdges() {
		return edges;
	}
	
	public ScrapeResult getScrapeResult() {
		return srcScraped;
	}
	
	public long getMark() {
		return mark;
	}
	
	public void setMark(long mark) {
		this.mark = mark;
	}

	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}	
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}	
	
}