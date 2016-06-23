package com.vogella.maven.quickstart;

import edu.uci.ics.jung.*;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class GraphTest {
	
	public static void main(String[] args) {
		//Initialize graph
		Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
		
		//Add vertices to graph
		g.addVertex((Integer)1);
		g.addVertex((Integer)2);
		g.addVertex((Integer)3);
		
		//Add some edges to our graph
		g.addEdge("Edge-A", 1, 2);
		g.addEdge("Edge-B", 2, 3);
		
		//See output so far..
		System.out.println("The graph g = " + g.toString());
		
		//We can use the same edges in two different graphs
		Graph<Integer, String> g2 = new SparseMultigraph<Integer, String>();
		g2.addVertex((Integer)1);
		g2.addVertex((Integer)2);
		g2.addVertex((Integer)3);
		g2.addEdge("Edge-A", 1, 3);
		g2.addEdge("Edge-B", 2, 3, EdgeType.DIRECTED);
		g2.addEdge("Edge-C", 3, 2, EdgeType.DIRECTED);
		g2.addEdge("Edge-P", 2,3);	//A parallel edge
		
		System.out.println("The graph of g2 is "+ g2.toString());
		
		
	}
}
