package assignment3.model;

import java.util.HashSet;
import java.util.Set;

public class PriorityQueue {
	
	private Edge [] array;
	private int count;
	private EdgesCompare edgeComparer;
	private final int INITIAL_ARRAY_SIZE = 10;
	private int arraySize;
	
	public PriorityQueue() {
		array = new Edge [INITIAL_ARRAY_SIZE];
		arraySize = INITIAL_ARRAY_SIZE;
		count = 0;
		edgeComparer = new EdgesCompare();
	}
	
	public void addAll(Set<Edge> edges) {
		for(Edge e : edges)
			add(e);
	}
	
	public void add(Edge x) {
		
		if(count + 1 == arraySize) {
			Edge [] newArray = new Edge [arraySize * 2];
			
			for(int i = 0; i < arraySize; ++i)
				newArray[i] = array[i];
			
			array = newArray;
			arraySize *= 2;
		}
			
		int k = count++;
		array[k] = x;
		
		while(k != 0) {
			int p = parent(k);
			if(edgeComparer.compare(array[k], array[p]) < 0) {
				Edge temp = array[k];
				array[k] = array[p];
				array[p] = temp;
				k = p;
			}
			else
				break;
		}
	}
	
	public Edge take() { 
		if(count == 0) return null;
		Edge result = array[0];
		array[0] = array[--count];
		array[count] = null;
		
		int k = 0;
		while(k < count) {
			int left = left(k);
			int right = right(k);
			if(right > count)
				break;
			int c;
			
			if(right >= count || edgeComparer.compare(array[left], array[right]) < 0)
				c = left;
			else
				c = right;
			
			if(edgeComparer.compare(array[k], array[c]) < 0)
				k = c;
			else
				break;
		}
		
		return result;
	}
	
	public int getSize() {
		return count;
	}
	
	private int parent(int q) {
		if(q == 0) return 0;
		else return  (q - 1) >>> 1;
	}
	
	private int left(int q) {
		return (q << 1) + 1;
	}
	
	private int right(int q) {
		return left(q) + 1;
	}
}
