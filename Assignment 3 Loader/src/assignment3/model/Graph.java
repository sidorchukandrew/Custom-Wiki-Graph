package assignment3.model;

import java.io.Serializable;
import java.util.Hashtable;

import assignment1.model.HashTable;
import assignment3.utilities.IOUtilities;

public class Graph implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static Node root;
	private static transient Hashtable <Integer, String> allWebsites;
	private static transient final int MAX_LINKS_FROM_SITE = 30;
	public static long currentMarker = 0;
	
	public Graph(int a) {
		
		root = new Node("A");
		Node b = new Node("B");
		
		Node c = new Node("C");
		Node d = new Node("D");
		Node e = new Node("E");
		Node f = new Node("F");

		root.add(b, 0.0);
		b.add(d, 0.0);
		b.add(root, 0.0);
		d.add(e, 0.0);
		root.add(c, 0.0);
		c.add(e, 0.0);
		c.add(f, 0.0);
		f.add(c, 0.0);
	}
	
	public Graph() {
		
		allWebsites = new Hashtable <Integer, String>();
		String rootURL = IOUtilities.readRootURLFromFile();
		
		System.out.println(rootURL);
		addToAllSites(rootURL);
		
		root = new Node(rootURL);

		for(String URL : root.getScrapeResult().getLinksToOtherSites()) {
			Node dst = new Node(URL);
			Cosine c = new Cosine(root, dst);
			root.add(dst, c.compute());
			addToAllSites(URL);
		}
		
		for(Edge edge : root.getEdges()) 
			breadthFirstPopulate(edge.getDst());
	}
	
	public void breadthFirstPopulate(Node src) {
		
		ScrapeResult srcScraped = src.getScrapeResult();
		int numLinksStored = 0;
		
		for(String URL : srcScraped.getLinksToOtherSites()) {
	
			if(numLinksStored == MAX_LINKS_FROM_SITE)
				break;
			
			Node dst = new Node(URL);
			Cosine c = new Cosine(root, dst);
			src.add(dst, c.compute());
			addToAllSites(URL);
			++numLinksStored;
		}
	}
	
	public static Node getRoot() {
		return root;
	}
	
	public static HashTable constructHashtable(String [] words, String websiteURL) {
		return new HashTable(words.length / 2, websiteURL, words.length);
	}
	
	public static void addToAllSites(String URL) {
		allWebsites.put(URL.hashCode(), URL);
	}
	
	public static boolean graphContains(String URL) {
		return allWebsites.containsKey(URL.hashCode());
	}
}
