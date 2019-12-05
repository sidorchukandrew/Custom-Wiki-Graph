package assignment3.model;

import java.io.IOException;
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
	private int idGenerator = 0;
	private int websiteNumber = 0;
	
	public ArrayList<Edge> MasterEdges;
	private Set<Node> MasterNodes;

	public Graph() {
		
		// Variables and Constants
		MasterEdges = new ArrayList<Edge>();
		MasterNodes = new HashSet<Node>();
		allWebsites = new Hashtable <Integer, String> ();
		String rootURL = IOUtilities.readRootURLFromFile();
		root = new Node(rootURL, this);
		updateUser();
		
		// Method
		addToAllSites(rootURL);
		root.setId(generateID());
		root.setParent(null);
		MasterNodes.add(root);
		
		// Add all the root's edges
		for(String URL : root.getScrapeResult().getLinksToOtherSites()) {
			Node dst = new Node(URL, this);
			
			if(!graphContains(dst)) {
				dst.setParent(root);
				dst.setId(generateID());
				root.addEdge(dst);
				MasterNodes.add(dst);
			}
			else 
				root.addEdge(getByName(dst.getName()));
			
			updateUser();
		}
		
		//- - - - - - - - - - - - - - -  Add edges to the root's edges
		//
		//       N4 -> *
		//	   / 
		//   R ->  N2 -> *
		//     \
		//       N3 -> *
		//
		// 								* Connecting to these
		//- - - - - - - - - - - - - - - - - - - 
		
		for(Edge edge : root.getEdges()) 
			populate(edge.getDst());
		
		// Add medoids
		for(String URL : IOUtilities.readMedoids("C:\\Users\\Andrew Sidorchuk\\CSC365 Workspace\\Assignment 3 Application\\medoids.txt")) {
			Node medoid = new Node(URL, this);
			updateUser();
			addToAllSites(URL);
			
			// Check if the medoid is already in the graph
			if(!graphContains(medoid)) {
				medoid.setId(generateID());
				medoid.setParent(null);
				MasterNodes.add(medoid);
				
				// Check if the graph contains any of the links from the medoid
				for(String site : medoid.getScrapeResult().getLinksToOtherSites()) 
					if(graphContains(site)) {
						System.out.println("One of the medoid's links was : " + site + " and that site already exists in the graph.");
						medoid.addEdge(getByName(site));
					}
			}
			else
				System.out.println("The graph already contains the medoid : " + medoid.getName());
		}
	}
	
	
	public void populate(Node src) {
		
		ScrapeResult srcScraped = src.getScrapeResult();
		int numLinksStored = 0;
		
		for(String URL : srcScraped.getLinksToOtherSites()) {
	
			// Helps limit the number of websites being scraped
			if(numLinksStored == MAX_LINKS_FROM_SITE)
				break;
			
			Node dst = new Node(URL, this);
			
			if(!graphContains(dst)) {
				dst.setParent(src);
				dst.setId(generateID());
				src.addEdge(dst);
				MasterNodes.add(dst);
			}
			else
				src.addEdge(getByName(dst.getName()));
			
			updateUser();
			addToAllSites(URL);
			++numLinksStored;
		}
	}
	
	// VIEW IT AS AN UNDIRECTED GRAPH
	// How can we tell if nodes are connected? By the MasterEdges list
	// If there exists an edge in that list, union it
	
	public int numberOfDisjointSets() {
		Integer [] ids = makeSet(MasterNodes);
		
		for(Edge e : MasterEdges)
			ids = union(ids, e.getSrc().getID(), e.getDst().getID());
		
		Set<Integer> numberOfDisjointSets = new HashSet<Integer>();
		
		for(Integer id : ids)
			numberOfDisjointSets.add(id);
			
		return numberOfDisjointSets.size();
	}
	
	private Integer [] makeSet(Set<Node> MasterNodes) {
		Integer [] ids = new Integer[MasterNodes.size()];
		for(Node n : MasterNodes)
			ids[n.getID()] = n.getID();
		
		return ids;
	}
	
	private Integer [] union(Integer [] ids, int a, int b) {
		 int x = find(ids, a);
		 int y = find(ids, b);
		 
		 int temp = ids[x];
		 ids[x] = y;
		 
		 for(int pos = 0; pos < ids.length; ++pos)
			 if(ids[pos] == temp)
				 ids[pos] = y;
		 
		 return ids;
	}
	
	private int find(Integer [] ids, int k) {
		
		if(ids[k] == k) return k;
		else 
			return find(ids, ids[k]);
	}
	
	public void addToMasterEdges(Edge e) {
		MasterEdges.add(e);
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
	
	public boolean graphContains(Node nodeToFind) {
		for(Node node : MasterNodes)
			if(node.getName().compareTo(nodeToFind.getName()) == 0)
				return true;
		
		return false;
	}
	
	public boolean graphContains(String nodeToFind) {
		for(Node node : MasterNodes)
			if(node.getName().compareTo(nodeToFind) == 0)
				return true;
		
		return false;
	}
	
	public Node getByName(String name) {
		for(Node n : MasterNodes)
			if(n.getName().compareTo(name) == 0)
				return n;
		
		return null;
	}
	
	public int generateID() { 
		return idGenerator++;
	}
	
	public ArrayList<Edge> getMasterEdges() {
		return MasterEdges;
	}
	
	public Set<Node> getMasterNodes() {
		return MasterNodes;
	}

	public void updateUser() {
		System.out.println(websiteNumber++);
	}
}