package assignment3.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import assignment3.model.Graph;
import assignment3.model.Node;
import assignment3.model.ScrapeResult;

public class IOUtilities {
	
	/**
	 * The URL is the URL of the Wiki site that is being added to the graph. 
	 * It should match this expression: www.wikipedia.com/wiki/
	 * @param URL
	 * @return ScrapeResult
	 * @throws IOException
	 */
	
	public static ScrapeResult scrapeWebsite(String URL) throws IOException {
		
		Hashtable <Integer, String> linksOnThisSite = new Hashtable<Integer, String>();
		Document webDocument = Jsoup.connect(URL).get();
		ScrapeResult scrapeResult = new ScrapeResult();
		
		scrapeResult.setTitle(URL);
		
		Elements elements = webDocument.getElementsByTag("p");
		scrapeResult.setWords(elements.text().split(" "));
		
		elements = webDocument.select("a[href]");
		for(Element e : elements) {
			if(e.attr("abs:href").matches("https://en\\.wikipedia\\.org/wiki/\\w*") 
					&& !(linksOnThisSite.containsKey(e.attr("abs:href").hashCode()))
					&& !(e.attr("abs:href").matches("https://en\\.wikipedia\\.org/wiki/Main_Page"))
					&& (URL.compareTo(e.attr("abs:href")) != 0)) {
				
				linksOnThisSite.put(e.attr("abs:href").hashCode(), (e.attr("abs:href")));
				scrapeResult.getLinksToOtherSites().add(e.attr("abs:href"));
			}
		}
		
		return scrapeResult;
	}
	
	/**
	 * Reads the starting URL from a file called "websites.txt"
	 * @return rootURL
	 */
	public static String readRootURLFromFile() {
		
		File fileContainingWebsiteURL = new File("websites.txt");

		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(fileContainingWebsiteURL));
			String URL =  fileReader.readLine();
			fileReader.close();
			return URL;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void serializeGraph(Node root) throws IOException {
    	
		FileOutputStream fileOutputStream 		= new FileOutputStream(new File("serializedTEMP.txt"));
    	ObjectOutputStream objectOutputStream 	= new ObjectOutputStream(fileOutputStream);
    	
		objectOutputStream.writeObject(root);
		objectOutputStream.flush();
		objectOutputStream.close();
	}
	
	public static void serializeGraph(Graph g) throws IOException {
    	
		FileOutputStream fileOutputStream 		= new FileOutputStream(new File("serializedTEMP.txt"));
    	ObjectOutputStream objectOutputStream 	= new ObjectOutputStream(fileOutputStream);
    	
		objectOutputStream.writeObject(g);
		objectOutputStream.flush();
		objectOutputStream.close();
	}
	
	public static Node readGraphFromFile(String address) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream 		= new FileInputStream(address);
	    ObjectInputStream objectInputStream 	= new ObjectInputStream(fileInputStream);
	    Node root = (Node) objectInputStream.readObject();
	    objectInputStream.close(); 
	    
	    return root;
	}
	
	public static Graph readActualGraph(String address) throws IOException, ClassNotFoundException {
		FileInputStream fileInputStream 		= new FileInputStream(address);
	    ObjectInputStream objectInputStream 	= new ObjectInputStream(fileInputStream);
	    Graph graph = (Graph) objectInputStream.readObject();
	    objectInputStream.close(); 
	    
	    return graph;
	}
	
	public static ArrayList<String> readMedoids(String address) {
		
		File file = new File(address);
		ArrayList<String> medoids = new ArrayList<String>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = in.readLine()) != null)
				medoids.add(line);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return medoids;
	}
}
