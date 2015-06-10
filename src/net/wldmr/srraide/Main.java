package net.wldmr.srraide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import net.wldmr.srraide.cpack.Parsing;

public class Main {
	
	public static void main(String[] args) throws IOException {
		Parsing.Tree tree = (new Parsing.Builder()).build(args[0]);
		Layout.Layouter layouter = new Layout.Layouter();
		Layout.Graph graph = layouter.layout(tree);
		writeGraph(graph, "c:\\Users\\wldmr_2\\eclipse_workspace\\SRR_Cpack\\UnderGroundClub.srt.pdf");
	}
	
	public static void writeGraph(Layout.Graph graph, String path) throws IOException {
		String[] parts = path.split("\\.");
		String format = parts[parts.length-1];
		ProcessBuilder builder = new ProcessBuilder("dot", "-T"+format, "-o"+path);
		builder.directory(new File(path).getParentFile());
		
		Process process = builder.start();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		String dot = graph.toDot();
		//System.out.println(dot);
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

}
