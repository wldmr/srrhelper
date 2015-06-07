package net.wldmr.srraide;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parsing {

	private static enum RE {
		group_start ("^\\s*(\\w+) \\{$"),
		group_end ("^\\s*\\}$"),
		property_string ("^\\s*(\\w+): \"([^\"]*)\"$"),
		property_value ("^\\s*(\\w+): (\\w+)$");

		private final Pattern pattern;

		RE(String regex) {
			pattern = Pattern.compile(regex);
		}

		Matcher match(String input) {
			return pattern.matcher(input);
		}
	}

	public static abstract class Node {
		Tree parent;
		String key;

		public Node(Tree parent, String key) {
			super();
			this.parent = parent;
			this.key = key;
		}
		
	}

	public static class Leaf extends Node {
		String value;

		public Leaf(Tree parent, String key, String value) {
			super(parent, key);
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}

	}

	public static class Tree extends Node {
		Map<String, List<Node>> nodes;

		public Tree(Tree parent, String key) {
			super(parent, key);
			this.nodes = new HashMap<String, List<Node>>();
		}

		private void add(Node node) {
			if (nodes.containsKey(node.key)) {
				nodes.get(node.key).add(node);
			} else {
				List<Node> newList = new ArrayList<>();
				newList.add(node);
				nodes.put(node.key, newList);
			}
		}

		public Leaf add(String key, String value) {
			Leaf leaf = new Leaf(this, key, value);
			add(leaf);
			return leaf;
		}

		public Tree add(String key) {
			Tree newTree = new Tree(this, key);
			add(newTree);
			return newTree;
		}
		
		public List<Node> getNodes(String key) {
			return nodes.get(key);
		}
		
		public Node getNode(String key, int index) {
			return getNodes(key).get(index);
		}
		
		public Tree getTree(String key, int index) {
			return (Tree) getNode(key, index);
		}
		
		public Tree getTree(String key) {
			return getTree(key, 0);
		}
		
		public String getAttribute(String key, int index) {
			Leaf l = (Leaf) getNode(key, index);
			return l.value;
		}
		
		public String getAttribute(String key) {
			return getAttribute(key, 0);
		}

		public String[] getNodeTypes() {
			Set<String> types = nodes.keySet();
			return types.toArray(new String[types.size()]);
		}
		
	}

	public static class Builder {
		private Tree state;

		public Builder() {
			super();
			this.state = null;
		}
		
		public Tree build(String filename) throws IOException {
			// Open the file

			String strLine;

			Tree root = new Tree(null, getFiletype(filename));

			// Read File Line By Line
			try (	FileReader fr = new FileReader(filename);
					BufferedReader br = new BufferedReader(fr);) {
				this.state = root;
				while ((strLine = br.readLine()) != null) {
					handleLine(strLine); // (may mutate state)
				}
				assert (root == this.state) : "After reading the full file, the final state should be the root element.";
				return root;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw (e);
			}
		}
		
		private static String getFiletype(String path) {
			String[] parts = path.split("\\.");
			// we expect filenames like yadda.TYPE.txt
			// so we extract TYPE
			return parts[parts.length-2];
		}

		private void handleLine(String strLine) {
			for (RE re : RE.values()) {
				Matcher m = re.match(strLine);
				if (m.matches()) {
					switch (re.name()) {
					case "group_start":
						startGroup(m.group(1));
						break;
						
					case "group_end":
						endGroup();
						break;
						
					case "property_string":
					case "property_value":
						addValue(m.group(1), m.group(2));
						break;
						
					default:
						System.out.println("WUT?!");
					}
				}
			}
		}

		public void addValue(String key, String value) {
			state.add(key, value);
		}
		
		public void startGroup(String key) {
			state = state.add(key);
		}
		
		public void endGroup() {
			state = state.parent;
		}
	}

}
