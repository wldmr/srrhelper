package net.wldmr.srraide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import net.wldmr.srraide.cpack.Parsing.Node;
import net.wldmr.srraide.cpack.Parsing.Tree;

public class Writer {

	private Tree tree;
	
	public Tree getTree() {
		return tree;
	}

	public Writer(Tree tree) {
		super();
		this.tree = tree;
	}
	
	public void writeGraph(String path) throws IOException {
		String[] parts = path.split("\\.");
		String format = parts[parts.length-1];
		ProcessBuilder builder = new ProcessBuilder("dot", "-T"+format, "-o"+path);
		builder.directory(new File(path).getParentFile());
		
		Process process = builder.start();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		String dot = this.toString();
		bw.write(dot);
		bw.close();
		
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		br.close();
	}
	
	public String toString() {
		
		Layout.Graph graph = new Layout.Graph();

		Layout.Node node = graph.addNode("source");
		node.setAttribute("label", "The Sauce");
		node.setAttribute("fontsize", 18);
		node.setAttribute("shape", "rectangle");
		node.setAttribute("style", "rounded");

		Layout.Edge edge = graph.addEdge("source", "target");

		return graph.toDot();
	}
}