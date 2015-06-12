package net.wldmr.srraide.cpack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parsing {

	private static enum RE {
		/* Ordering matters here! PROPERTY_INT would be captured
		 * by PROPERTY_WORD if it came after.
		 */
		GROUP_START ("^\\s*(\\w+) \\{\\s*$"),
		GROUP_END ("^\\s*\\}\\s*$"),
		PROPERTY_INT ("^\\s*(\\w+): (\\d+)\\s*$"),
		PROPERTY_FLOAT ("^\\s*(\\w+): (\\d+\\.\\d+)\\s*$"),
		PROPERTY_STRING ("^\\s*(\\w+): \"([^\"]*)\"\\s*$"),
		PROPERTY_WORD ("^\\s*(\\w+): (\\w+)\\s*$");

		private final Pattern pattern;

		RE(String regex) {
			pattern = Pattern.compile(regex);
		}

		Matcher match(String input) {
			return pattern.matcher(input);
		}
	}

	public static class Node {

		private String key = null;
		// While this Node implementation can have both data and children at the same time,
		// the SRR file format does not (as of 2015-06-11). Just something to be aware of.
		private String data = null;
		private Children children = null;

		@SuppressWarnings("serial") // Compiler complained about unserializable somethingorother ...
		private class Children extends LinkedHashMap<String, List<Node>> {}

		public Node(String key, String value) {
			this.key = key;
			this.data = value;
		}

		public Node(String key) {
			this.key = key;
			this.children = new Children();
		}

		public String getData() {
			assert data != null : "Data should not be null";
			return data;
		}

		public String getKey() { return key; }

		/** Returns the node that was passed in, for convenience. */
		private Node add(Node node) {
			if (children.containsKey(node.key)) {
				List<Node> lst = children.get(node.key);
				lst.add(node);
			} else {
				List<Node> newList = new ArrayList<>();
				newList.add(node);
				children.put(node.key, newList);
			}
			return node;
		}

		public Node newChildren(String key) {
			return add(new Node(key));
		}

		// Is void to reinforce that it is a terminal
		// and nothing interesting can happen with this node.
		public void newValue(String key, String value) {
			add(new Node(key, value));
		}

		public Node[] getChildren(String key) {
			List<Node> ns = children.get(key);
			return ns.toArray(new Node[ns.size()]);
		}

		public Node getChild(String key, int index) {
			return children.get(key).get(index);
		}

		public Node getChild(String key) {
			return getChild(key, 0);
		}

		public String[] getValues(String key) {
			Node[] nodes = getChildren(key);
			String[] strings = new String[nodes.length];
			for (int i=0; i<nodes.length; i++)
				strings[i] = nodes[i].getData();
			return strings;
		}

		public String getValue(String key, int index) {
			return getChild(key, index).getData();
		}

		public String getValue(String key) {
			return getValue(key, 0);
		}

		public String[] getChildKeys() {
			Set<String> types = children.keySet();
			return types.toArray(new String[types.size()]);
		}
		
	}

	public static class Builder {
		private Stack<Node> state;

		public Builder() {
			super();
			this.state = null;
		}
	
		public Node build(String filetype, BufferedReader br) throws IOException {
			Node root = new Node(filetype);
			state = new Stack<Node>();
			state.push(root);
			String line;
			while( (line=br.readLine()) != null )
				handleLine(line); // (may mutate state)
			assert (state.size() == 1 && state.peek() == root) : "After reading the full file, the final state should be the root element.";
			return root;
		}

		public Node build(String filename) throws IOException {
			try (BufferedReader br = new BufferedReader(new FileReader(filename));) {
				return build(getFiletype(filename), br);
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
				/* Note: If we find a match, we return instead of break.
				 * This is to reduce unnecessary (and spurious) checks.
				 */
				if (m.matches()) {
					switch (re) {
					case GROUP_START:
						startGroup(m.group(1));
						return;
						
					case GROUP_END:
						endGroup();
						return;
						
					case PROPERTY_STRING:
					case PROPERTY_WORD:
					case PROPERTY_INT:
					case PROPERTY_FLOAT:
						addValue(m.group(1), m.group(2));
						return;
						
					default:
						assert false: "Line could not be handled: '"+strLine+"'";
					}
				}
			}
		}

		public void addValue(String key, String value) {
			state.peek().newValue(key, value);
		}
		
		public void startGroup(String key) {
			Node newTree = state.peek().newChildren(key);
			state.push(newTree);
		}
		
		public void endGroup() {
			state.pop();
		}
	}

}
