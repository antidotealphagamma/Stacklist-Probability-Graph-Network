package com.vogella.maven.quickstart;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
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
import java.util.Queue;
import java.util.Random;

import javax.swing.JFrame;
import org.apache.commons.collections15.Transformer;

public class GraphViewer {
	
	//Create Graph
	Graph<String, String> g; 
	
	//Constructor
	
	public GraphViewer() {
		
		//Create reader object
		ReadCVS reader = new ReadCVS();
		
		//Run main method so that we have all our data in the two Queues
		reader.run();
		Queue<Float> probabilityQueue =  reader.probabilityQ();
		Queue<String> nameQueue = reader.probablityN();
		//Queue<String> tmpQueue = nameQueue;
		
		//Create new graph
		g = new DirectedSparseMultigraph<String, String>();
				
		//Data structures to be utilized
		ArrayList<String> tmpNames = new ArrayList<String>();
		ArrayList<Float> prb = new ArrayList<Float>();
		
		//boolean done = false;
		while (nameQueue.size() != 0) {
			//if (nameQueue.peek() == null) 
			tmpNames.add(nameQueue.remove());
			//System.out.println("here");
		}
		
		while (probabilityQueue.size() != 0) {
			prb.add(probabilityQueue.remove());
		}
		
		
		//System.out.println("First test");
		
		//[][][][][][]Debugging[][][][][][][][]
		//System.out.println(tmpNames.toString());

		//System.out.println(prb.toString());
		
//		for (int i = 0; i < tmpNames.size(); i++) {
//				//	If two tool names are matching strings, then delete the second data point
//				// - and delete the probability value between the association.
//			if (tmpNames.get(i).equals(i+1)) {
//				tmpNames.remove(i);
//				prb.remove(i);
//			}
//		}
		
//		tmpNames.remove(0);
//		tmpNames.remove(0);
//		prb.remove(0);
//		System.out.println(tmpNames.toString());
		
		
		
		
		//Create Vertices for each tool name
		//[][][][] Issues present here [][][][][][]
		//tmpNames.size()-1
		//Debugging Edge display
		for (int i = 0; i < 25; i++) {
			int j = 0;
			Random rnd = new Random();
			//if (tmpNames.get(i+1) == null) break;
			String tmpVal = "" + tmpNames.get(i).charAt(0) + tmpNames.get(i).charAt(tmpNames.get(i).length()-1) 
					+ "," + tmpNames.get(i+1).charAt(0) + tmpNames.get(i+1).charAt(tmpNames.get(i+1).length()-1) + 
					rnd.nextInt(1000000) + tmpNames.get(i+1).charAt(tmpNames.get(i+1).length()-2);
			g.addVertex(tmpNames.get(i));
			
			if (g.findEdge(tmpNames.get(i), tmpNames.get(i+1)) != null) {
				g.removeEdge(g.findEdge(tmpNames.get(i), tmpNames.get(i+1)));
			} else {
				g.addEdge("P: " + tmpVal + " -> "+ prb.get(i).toString(), tmpNames.get(i), tmpNames.get(i+1));
				//g.addEdge(new MyEdge(prb.get(i)).toString(), tmpNames.get(i), tmpNames.get(i+1));
			}
		}
	}
	
	
	public static void main(String[] args) {
		 	GraphViewer sgv = new GraphViewer(); // Creates the graph...
	        // Layout<V, E>, VisualizationComponent<V,E>
	        Layout<String, String> layout = new CircleLayout(sgv.g);
	        layout.setSize(new Dimension(300,300));
	        VisualizationViewer<String,String> vv = new VisualizationViewer<String,String>(layout);
	        vv.setPreferredSize(new Dimension(350,350));
	        // Show vertex and edge labels
	        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
	        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
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
				return Color.GREEN;
			}
		};
		
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
        
	}
	
	
}
