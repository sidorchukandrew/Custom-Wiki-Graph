package assignment3.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import assignment1.model.HashTable;
import assignment3.utilities.IOUtilities;

public class Graph implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Node root;
	private static transient Hashtable <Integer, String> allWebsites;		// Makes sure a duplicate website isn't stored
	private static transient final int MAX_LINKS_FROM_SITE = 30;
	public static long currentMarker = 0;
	public ArrayList<Edge> MasterEdges;
	private int idGenerator = 0;
	
	public Graph() {
		
		MasterEdges = new ArrayList<Edge>();
		allWebsites = new Hashtable <Integer, String>();
	
		String rootURL = IOUtilities.readRootURLFromFile();
		System.out.println(rootURL);
		addToAllSites(rootURL);
		
		root = new Node(rootURL, this);
		root.setId(generateID());
		
		for(String URL : root.getScrapeResult().getLinksToOtherSites()) {
			Node dst = new Node(URL, this);
			Cosine c = new Cosine(root, dst);
			root.add(root, dst, c.compute());
			
			addToAllSites(URL);
		}
		
		for(Edge edge : root.getEdges()) {
			edge.setSrc(root);
			breadthFirstPopulate(edge.getDst());
		}
	}
	
	public void breadthFirstPopulate(Node src) {
		
		ScrapeResult srcScraped = src.getScrapeResult();
		int numLinksStored = 0;
		
		for(String URL : srcScraped.getLinksToOtherSites()) {
	
			// Helps limit the number of websites being scraped
			if(numLinksStored == MAX_LINKS_FROM_SITE)
				break;
			
			Node dst = new Node(URL, this);
			Cosine c = new Cosine(src, dst);
			src.add(src, dst, c.compute());
			
			addToAllSites(URL);
			++numLinksStored;
		}
	}
	
	public int numberOfDisjointSets() {
		
		// Create the universe of ids
		Map<Integer, Node> nodes = makeSet(MasterEdges);
		Integer [] ids = makeSet(nodes);
		
		for(Node node : nodes.values())
			ids[node.getID()] = find(node);
		
		Set<Integer> numberOfDisjointSets = new HashSet<Integer>();
		
		for(Integer id : ids)
			numberOfDisjointSets.add(id);
			
		return numberOfDisjointSets.size();
	}
	
	private Map<Integer, Node> makeSet(ArrayList<Edge> edges) {
		
		Map <Integer, Node> nodes = new HashMap<Integer, Node>();
		
		for(Edge edge : edges) {
			nodes.put(edge.getSrc().getID(), edge.getSrc());
			nodes.put(edge.getDst().getID(), edge.getDst());
		}
		
		return nodes;
	}
	
	private Integer [] makeSet(Map<Integer, Node> nodes) {
		Integer [] ids = new Integer[nodes.size()];
		ids = nodes.keySet().toArray(ids);
		
		return ids;
	}
	
	// Find the root's ID
	private int find(Node node) {
		
		if(node.getParent() == null)
			return node.getID();
		else
			return(find(node.getParent()));
	}
	
	public Node getRoot() {
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
	
	public int generateID() { 
		return idGenerator++;
	}
	
	public ArrayList<Edge> getMasterEdges() {
		return MasterEdges;
	}
}
