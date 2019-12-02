package assignment3.model;

import java.io.Serializable;

public class Edge implements Serializable, Comparable<Edge> {

	private double weight; 							// This is a cosine weight
	private Node dst, src;
	
	public Edge(Node src, Node dst) {
		this.dst = dst;
		weight = Double.MAX_VALUE;
	}
	
	public Edge(Node src, Node dst, double weight) {
		this.dst = dst;
		this.weight = weight;
	}
	
	public Edge() {
		dst = null;
		weight = Double.MAX_VALUE;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Node getDst() {
		return dst;
	}

	public void setDst(Node dst) {
		this.dst = dst;
	}
	
	public Node getSrc() {
		return src;
	}

	public void setSrc(Node src) {
		this.src = src;
	}

	@Override
	public int compareTo(Edge e2) {
		
		if(getWeight() > e2.getWeight()) return 1;
		if(getWeight() < e2.getWeight()) return -1;
		else
			return 0;
	}
}
