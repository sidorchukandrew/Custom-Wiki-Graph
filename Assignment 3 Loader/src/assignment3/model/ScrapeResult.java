package assignment3.model;

import java.util.ArrayList;

public class ScrapeResult {
	
	private String [] words;
	private ArrayList<String> linksToOtherSites;
	private String title;
	
	public ScrapeResult() {
		linksToOtherSites = new ArrayList<String>();
	}

	public String[] getWords() {
		return words;
	}

	public void setWords(String[] words) {
		this.words = words;
	}

	public ArrayList<String> getLinksToOtherSites() {
		return linksToOtherSites;
	}

	public void setLinksToOtherSites(ArrayList<String> linksToOtherSites) {
		this.linksToOtherSites = linksToOtherSites;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
