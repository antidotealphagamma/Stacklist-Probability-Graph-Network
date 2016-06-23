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
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Queue;

import javax.swing.JFrame;
import org.apache.commons.collections15.Transformer;

public class GraphViewer {
	
	//Create Graph
	Graph<String, Float> g; 
	
	//Constructor
	@SuppressWarnings("null")
	public GraphViewer() {
		
		//Create reader object
		ReadCVS reader = new ReadCVS();
		
		//Run main method so that we have all our data in the two Queues
		reader.run();
		Queue<Float> probabilityQueue =  reader.probabilityQ();
		Queue<String> nameQueue = reader.probablityN();
		Queue<String> tmpQueue = nameQueue;
		
		//Create new graph
		g = new DirectedSparseMultigraph<String, Float>();
				
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
		
		
		System.out.println("First test");
		
		//[][][][][][]Debugging[][][][][][][][]
		//System.out.println(tmpNames.toString());
		
		
		//Create Vertices for each tool name
		for (int i = 0; i < tmpNames.size(); i++) {
			g.addVertex(tmpNames.get(i));
			
			if (g.findEdge(tmpNames.get(i), tmpNames.get(i+1)) != null) {
				g.removeEdge(g.findEdge(tmpNames.get(i), tmpNames.get(i+1)));
			}
			
			System.out.println("======");
			System.out.println("Fine");
			System.out.println(g.toString());
			g.addEdge(prb.get(i), tmpNames.get(i), tmpNames.get(i+1));
			
		}
		
		
		//Add directed edges
//		for (int i = 0; i < probabilityQueue.size(); i++) {
//			g.addEdge(probabilityQueue.remove(), tmpNames.get(i), tmpNames.get(i+1), EdgeType.DIRECTED);
//		}
		
		
	}
	
	public static void main(String[] args) {
		GraphViewer gv = new GraphViewer();
		//Layout<V, E>, VisualizationComponent<V,E>
		Layout<String, Float> layout = new CircleLayout<String, Float>(gv.g);
		layout.setSize(new Dimension(300, 300));
		BasicVisualizationServer<String, Float> vv = new BasicVisualizationServer<String, Float>(layout);
		vv.setPreferredSize(new Dimension(350, 350));
		
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
		
		Transformer<Float, Stroke> edgeStrokeTransformer = new Transformer<Float, Stroke>() {
			public Stroke transform(Float f) {
				return edgeStroke;
			}
		};
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);        
        
        JFrame frame = new JFrame("Simple Graph View 2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
	}
	
	
}
