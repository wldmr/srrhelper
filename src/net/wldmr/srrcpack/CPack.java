package net.wldmr.srrcpack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CPack {
	public static abstract class Node {
		Branches parent;
		String key;
		
		public Node(Branches parent, String key) {
			super();
			this.parent = parent;
			this.key = key;
		}
	}
	
	public static class Leaf extends Node {
		String value;

		public Leaf(Branches parent, String key, String value) {
			super(parent, key);
			this.value = value;
		}

	}
	
	public static class Branches extends Node {
		Map<String, List<Node>> nodes;

		public Branches(Branches parent, String key) {
			super(parent, key);
			this.nodes = new HashMap<String, List<Node>>();
		}
		
		public void add(Node node) {
			if (nodes.containsKey(node.key)) {
				nodes.get(node.key).add(node);
			} else {
				List<Node> newList = new ArrayList<>();
				newList.add(node);
				nodes.put(node.key, newList);
			}
		}
	}
}
