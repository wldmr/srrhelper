package net.wldmr.srraide;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.wldmr.srraide.cpack.Parsing;
import net.wldmr.srraide.cpack.Parsing.Tree;

public class Layout {
	
	static final Map<String, BaseLayoutStrategy> strategies = new HashMap<>();
	static {
		strategies.put("srt", new SceneLayoutStrategy());
		strategies.put("triggers", new TriggerLayoutStrategy());
		strategies.put("events", new BaseLayoutStrategy());
		strategies.put("ops", new FunctionLayoutStrategy());
	}
	
	private static class BaseLayoutStrategy {
		void layout(Graph graph, Parsing.Node rootNode) {
			Tree root = (Tree) rootNode;
			for (String nodeType : root.getNodeTypes()) {
				if (strategies.containsKey(nodeType)) {
					BaseLayoutStrategy strategy = strategies.get(nodeType);
					for (Parsing.Node node : root.getNodes(nodeType)) {
						strategy.layout(graph, node);
					}
				}
			}
		}
	}
	
	private static class SceneLayoutStrategy extends BaseLayoutStrategy {
	}
	
	private static class TriggerLayoutStrategy extends BaseLayoutStrategy {
		
		private LabelMaker lm;
		
		@Override
		void layout(Graph graph, Parsing.Node rootNode) {
			Tree root = (Tree) rootNode;
			String id = root.getTree("idRef").getAttribute("id");
			Layout.Node n = graph.addNode(id);
			n.setAttribute("shape", "plaintext");
			n.setAttribute("label", makeLabel(root));
			
			makeEdges(graph, root, "events", id);
			
			//super.layout(graph, rootNode);
		}
		
		private void makeEdges(Graph graph, Parsing.Tree root, String subtreeName, String sourceID) {
			List<Parsing.Node> ops = root.getTree(subtreeName).getNodes("ops");
			if (ops != null) {
				for (Parsing.Node n : ops) {
					Parsing.Tree op = (Parsing.Tree) n;
				}
			}
		}

		private String makeLabel(Tree root) {
			lm = new LabelMaker();
			lm.table().border(0).align("left");

			makeRow(root.getAttribute("name"));
			makeRow("<b>When</b>");
			makeFunctionList(root, "events");
			makeRow("<b>If</b>");
			makeFunctionList(root, "conditions");
			makeRow("<b>Then</b>");
			makeFunctionList(root, "actions");
			makeRow("<b>Else</b>");
			makeFunctionList(root, "elseActions");

			lm._table();
			return lm.toString();
		}

		public void makeFunctionList(Tree root, String tree) {
			List<Parsing.Node> ops = root.getTree(tree).getNodes("ops");
			if (ops != null) {
				for (Parsing.Node n : ops) {
					Parsing.Tree op = (Parsing.Tree) n;
					makeRow(op.getAttribute("functionName"));
				}
			}
		}
		
		private void makeRow(String text) {
			lm.tr().td().text(text)._td()._tr();
		}
		
	}

	private static class FunctionLayoutStrategy extends BaseLayoutStrategy {

		@Override
		void layout(Graph graph, Parsing.Node rootNode) {
			Parsing.Tree root = (Parsing.Tree) rootNode; 
			makeEdges(graph, root);
			//super.layout(graph, rootNode);
		}
		
		private void makeEdges(Graph graph, Tree root) {
			List<Parsing.Node> ops = root.getTree("events").getNodes("ops");
			if (ops != null) {
				for (Parsing.Node n : ops) {
					Parsing.Tree op = (Parsing.Tree) n;
				}
			}
		}
	}
	public static class Layouter {
		public Graph layout(Parsing.Tree root) {
			Graph graph = new Graph();
			BaseLayoutStrategy strategy = strategies.get(root.key);
			if (strategy != null)
				strategy.layout(graph, root);  // may call other strategies
			return graph;
		}
		
	}
	
	private static class LabelMaker {
		private StringBuilder sb = new StringBuilder();
		
		public LabelMaker start(String tag) {
			sb.append("<"+tag+">");
			return this;
		}
		
		public LabelMaker end(String tag) {
			sb.append("</"+tag+">");
			return this;
		}
		
		public LabelMaker startend(String tag) {
			sb.append("<"+tag+"/>");
			return this;
		}
		
		public LabelMaker text(String text) {
			sb.append(text);
			return this;
		}
		
		public LabelMaker attribute(String attr, Object value) {
			String tail = reopenTag();
			sb.append(" "+attr+"=\""+value+"\""+tail);
			return this;
		}
		
		
		@Override
		public String toString() {
			return "<"+sb.toString()+">";
		}

		private String reopenTag() {
			int last = sb.length();
			String lastTwo = sb.substring(last-2, last);
			assert lastTwo.endsWith(">") : "Previous character not a '>'";
			if (lastTwo.equals("/>")) {
				sb.setLength(sb.length() - 2);
				return "/>";
			} else {
				sb.setLength(sb.length() - 1);
				return ">";
			}
		}
		
		/** <h1>Convenience Methods</h1> */
		/** <h2>Tags</h2> */
		public LabelMaker table() { return start("table"); }
		public LabelMaker _table() { return end("table"); }
		public LabelMaker tr() { return start("tr"); }
		public LabelMaker _tr() { return end("tr"); }
		public LabelMaker td() { return start("td"); }
		public LabelMaker _td() { return end("td"); }
		public LabelMaker font() { return start("font"); }
		public LabelMaker _font() { return end("font"); }
		public LabelMaker i() { return start("i"); }
		public LabelMaker _i() { return end("i"); }
		public LabelMaker b() { return start("b"); }
		public LabelMaker _b() { return end("b"); }
		public LabelMaker u() { return start("u"); }
		public LabelMaker _u() { return end("u"); }
		public LabelMaker o() { return start("o"); }
		public LabelMaker _o() { return end("o"); }
		public LabelMaker sub() { return start("sub"); }
		public LabelMaker _sub() { return end("sub"); }
		public LabelMaker sup() { return start("sup"); }
		public LabelMaker _sup() { return end("sup"); }
		public LabelMaker s() { return start("s"); }
		public LabelMaker _s() { return end("s"); }
		
		/** <h2>Standalone Tags<h2> */
		public LabelMaker br() { return startend("br"); }
		public LabelMaker img() { return startend("img"); }
		public LabelMaker hr() { return startend("hr"); }
		public LabelMaker vr() { return startend("vr"); }

		/** <h2>Attributes</h2> */
		public LabelMaker align(Object value) { return attribute("align", value); }
		public LabelMaker balign(Object value) { return attribute("balign", value); }
		public LabelMaker bgcolor(Object value) { return attribute("bgcolor", value); }
		public LabelMaker border(Object value) { return attribute("border", value); }
		public LabelMaker cellborder(Object value) { return attribute("cellborder", value); }
		public LabelMaker cellpadding(Object value) { return attribute("cellpadding", value); }
		public LabelMaker cellspacing(Object value) { return attribute("cellspacing", value); }
		public LabelMaker color(Object value) { return attribute("color", value); }
		public LabelMaker colspan(Object value) { return attribute("colspan", value); }
		public LabelMaker columns(Object value) { return attribute("columns", value); }
		public LabelMaker face(Object value) { return attribute("face", value); }
		public LabelMaker fixedsize(Object value) { return attribute("fixedsize", value); }
		public LabelMaker gradientangle(Object value) { return attribute("gradientangle", value); }
		public LabelMaker height(Object value) { return attribute("height", value); }
		public LabelMaker href(Object value) { return attribute("href", value); }
		public LabelMaker id(Object value) { return attribute("id", value); }
		public LabelMaker pointSize(Object value) { return attribute("point-size", value); }
		public LabelMaker port(Object value) { return attribute("port", value); }
		public LabelMaker rows(Object value) { return attribute("rows", value); }
		public LabelMaker rowspan(Object value) { return attribute("rowspan", value); }
		public LabelMaker scale(Object value) { return attribute("scale", value); }
		public LabelMaker sides(Object value) { return attribute("sides", value); }
		public LabelMaker src(Object value) { return attribute("src", value); }
		public LabelMaker style(Object value) { return attribute("style", value); }
		public LabelMaker target(Object value) { return attribute("target", value); }
		public LabelMaker title(Object value) { return attribute("title", value); }
		public LabelMaker tooltip(Object value) { return attribute("tooltip", value); }
		public LabelMaker valign(Object value) { return attribute("valign", value); }
		public LabelMaker width(Object value) { return attribute("width", value); }
	}

	abstract private static class Part {
		protected Map<String, String> attributes = new HashMap<>();

		public String getAttribute(String key) {
			return attributes.get(key);
		}

		public void setAttribute(String key, Object value) {
			this.attributes.put(key, value.toString());
		}

		abstract String toDot();

		protected String formatAttributes() {
			StringBuilder sb = new StringBuilder();
			for (String key : attributes.keySet()) {
				sb.append(key+"="+getAttribute(key)+", ");
			}
			return sb.toString();
		}
	}

	static class Node extends Part {
		private String id;

		public Node(String id) {
			super();
			this.id = id;
		}

		@Override
		String toDot() {
			return "\""+id+"\" ["+formatAttributes()+"];";
		}
	}

	static class Edge extends Part {
		private String from;
		private String to;

		public Edge(String from, String to) {
			super();
			this.from = from;
			this.to = to;
		}

		@Override
		String toDot() {
			return "\""+from+"\" -> \""+to+"\" ["+formatAttributes()+"];";
		}
	}

	static class Graph extends Part {
		Set<Edge> edges = new HashSet<>();
		Set<Node> nodes = new HashSet<>();

		public String toDot() {
			StringBuilder sb = new StringBuilder();

			sb.append("digraph {\n");
			sb.append("layout=neato;\n");
			for (Node n : nodes)
				sb.append(n.toDot() + "\n");
			sb.append("\n");
			for (Edge e : edges)
				sb.append(e.toDot() + "\n");
			sb.append("}");

			return sb.toString();
		}

		public Edge addEdge(String from, String to) {
			Edge e = new Edge(from, to);
			edges.add(e);
			return e;
		}

		public Node addNode(String id) {
			Node n = new Node(id);
			nodes.add(n);
			return n;
		}
	}
}
