package net.wldmr.srrcpack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CPack {

	private static enum RE {
		group_start("^\\s*(\\w+) \\{$"), group_end("^\\s*\\}$"), property_string(
				"^\\s*(\\w+): \"([^\"]*)\"$"), property_value(
				"^\\s*(\\w+): (\\w+)$");

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
	}

	public static void build(String filename) {
		// Open the file

		String strLine;

		Tree root = new Tree(null, filename);

		// Read File Line By Line
		try (	FileReader fr = new FileReader(filename);
				BufferedReader br = new BufferedReader(fr);) {
			Tree state = root;
			while ((strLine = br.readLine()) != null) {
				state = handleLine(state, strLine); // (may mutate state)
			}
			assert (root == state) : "After reading the full file, the final state should be the root element.";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	private static Tree handleLine(Tree state, String strLine) {
		for (RE re : RE.values()) {
			Matcher m = re.match(strLine);
			if (m.matches()) {
				switch (re.name()) {
				case "group_start":
					state = state.add(m.group(1));
					break;
				case "group_end":
					state = state.parent;
					break;
				case "property_string":
				case "property_value":
					state.add(m.group(1), m.group(2));
					break;
				default:
					System.out.println("WUT?!");
				}
			}
		}
		return state;
	}
}
