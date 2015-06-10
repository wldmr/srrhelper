package net.wldmr.srraide;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GraphTest {
	
	private Layout.Graph graph;
	private Layout.Node node;
	private Layout.Edge edge;
	
	@Before
	public void setUp() throws Exception {
		graph = new Layout.Graph();
		
		node = graph.addNode("source");
		node.setAttribute("label", "The Source");
		node.setAttribute("fontsize", 18);
		
		edge = graph.addEdge("source", "target");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void nodeToDot() {
		String dot = node.toDot();
		assertNotNull(dot);
		assertEquals(dot, "\"source\" [fontsize=\"18\", label=\"The Source\", ];");
	}
	
	@Test
	public void edgeToDot() {
		String dot = edge.toDot();
		assertNotNull(dot);
		assertEquals(dot, "\"source\" -> \"target\" [];");
	}

}
