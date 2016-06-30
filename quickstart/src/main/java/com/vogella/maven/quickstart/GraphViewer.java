package com.vogella.maven.quickstart;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import javax.swing.JFrame;
import org.apache.commons.collections15.Transformer;

public class GraphViewer {
	
	//Create Graph
	private static Graph<String, String> g; 
	private ArrayList<String> tmpNames = new ArrayList<String>();
	private ArrayList<Float> prb = new ArrayList<Float>();
	private static Scanner scanner = new Scanner(System.in);
	
	/** Helper Functions **/ 
	
	//Sort String Array by alphabetical order while respectively reordering the probability array

	//Uses binary search in order to find the starting location of a company in an arraylist
	public int isolateField(String company, int min, int max) {
		int mid = (min + max) / 2;
		if (tmpNames.get(mid).equals(company)) return mid;
		else if (tmpNames.get(mid).compareTo(company) >0) {
			return isolateField(company, min, mid-1);
		} else {
			return isolateField(company, mid+1, max);
		}
	}
	
	//Finds exactly how many instances of 
	public int endingField(String company) {
		int startPos = isolateField(company, 0, tmpNames.size());
		int count = 0;
		for (int i = startPos; i < tmpNames.size(); i++) {
			if (tmpNames.get(i).equals(company)) count++;
		}
		return count;
	}
	
	/** Constructor **/ 
	public GraphViewer() {
		
		//Create reader object
		ReadCVS reader = new ReadCVS();
		
		//Run main method so that we have all our data in the two Queues
		//Sizes are equal
		reader.run();
		Queue<Float> probabilityQueue =  reader.probabilityQ();
		Queue<String> nameQueue = reader.probablityN();
		
		Map<String, Float> toolMap = new HashMap<String, Float>();
		
//		for (int i = 0; i < probabilityQueue.size(); i++) {
//			toolMap.put(nameQueue.remove()+":"+nameQueue.remove(), probabilityQueue.remove());
//		}
		
		//Queue<String> tmpQueue = nameQueue;
		
		//Create new graph
		g = new DirectedSparseMultigraph<String, String>();
				
		
		
		while (nameQueue.size() != 0) {
			tmpNames.add(nameQueue.remove());
		}
		
		while (probabilityQueue.size() != 0) {
			prb.add(probabilityQueue.remove());
		}
		
		
		//Grab input from user
		System.out.println("Enter a tech tool to graph: ");
		String in = scanner.nextLine();
		
		int startPos = isolateField(in, 0, tmpNames.size());
		int nSize = endingField(in);
		int endPos = startPos + nSize;
		
		//Add edges and vertices to the Graph object
		for (int i = startPos; i < endPos; i++) {
			int j = 0;
			Random rnd1 = new Random();
			Random rnd2 = new Random();
			//if (tmpNames.get(i+1) == null) break;
			String tmpVal = tmpNames.get(i) + ":" + rnd1.nextInt(100000) + tmpNames.get(i+1)
			+ rnd2.nextInt(100000) + rnd1.nextInt(100000);
			
			g.addVertex(tmpNames.get(i));
			
			if (g.findEdge(tmpNames.get(i), tmpNames.get(i+1)) != null) {
				g.removeEdge(g.findEdge(tmpNames.get(i), tmpNames.get(i+1)));
			} else {
				g.addEdge(tmpVal + "P: " + prb.get(i).toString(), tmpNames.get(i), tmpNames.get(i+1));
			}
		}
	}

	public static void main(String[] args) {
		 	GraphViewer sgv = new GraphViewer(); // Creates the graph...
	        // Layout<V, E>, VisualizationComponent<V,E>
	        Layout<String, String> layout = new CircleLayout(sgv.g);
	        layout.setSize(new Dimension(1000,1000));
	        VisualizationViewer<String,String> vv = new VisualizationViewer<String,String>(layout);
	        vv.setPreferredSize(new Dimension(1050,1050));
	        // Show vertex and edge labels
	        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

	        // Create a graph mouse and add it to the visualization component
	        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
	        gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
	        vv.setGraphMouse(gm); 
	        JFrame frame = new JFrame("Interactive Graph View 1");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.getContentPane().add(vv);
	        frame.pack();
	        frame.setVisible(true);
		
		//Setup a new vertex to paint transformer..
		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String i) {
				return Color.BLUE;
			}
		};
		
        vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        
		
		//Make edges more presentable
        vv.getRenderContext().setEdgeLabelTransformer(new Transformer<String, String>() {
        	public String transform(String c) {
        		return StringUtils.substringAfterLast(c, ":");
        	}
        });
        
		//Setup a new stroke Transformer for edges
		float dash[] = {10.0f};
		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		
		Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
			public Stroke transform(String f) {
				return edgeStroke;
			}
		};
		
		//vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR); 
        
        //Distance Statistics
        DijkstraShortestPath<String, String> dsp = new DijkstraShortestPath<String, String>(g);
        Transformer<String, Double> distances = DistanceStatistics.averageDistances(g, dsp);
        
        
        
	}
	
	
}
