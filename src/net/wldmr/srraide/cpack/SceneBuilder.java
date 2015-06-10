package net.wldmr.srraide.cpack;

import net.wldmr.srraide.cpack.Ops.Arg;
import net.wldmr.srraide.cpack.Parsing.Node;
import net.wldmr.srraide.cpack.Scene.Trigger;
import net.wldmr.srraide.cpack.Scene.Trigger.*;
import net.wldmr.srraide.cpack.Ops.*;

public class SceneBuilder {
	public static Scene build(Node root) {
		Scene scene = new Scene(root.getKey());
		for (Node t : root.getChildren("triggers"))
			scene.accessTriggers().add(makeTrigger(t));
		return scene;
	}

	private static Trigger makeTrigger(Node root) {
		String id = root.getChild("idRef").getValue("id");
		String name = root.getValue("name");
		boolean isOneshot = Boolean.valueOf(root.getValue("is_oneshot"));
		boolean isActive = Boolean.valueOf(root.getValue("isActive"));
		Trigger trigger = new Trigger(id, name, isActive, isOneshot);
		
		for (Node n : root.getChildren("events"))
			trigger.accessEvents().add((Event) makeOp(n));
		return trigger;
	}

	private static Op makeOp(Node node) {
		String functionName = node.getValue("functionName");
		Arg[] args = makeArgs(node.getChildren("args"));
		Op op = new Op(functionName, args);
		return op;
	}

	private static Arg makeArg(Node node) {
		switch (node.getKey()) {
		case "call_value":
			String functionName = node.getValue("functionName");
			Arg[] args = makeArgs(node.getChildren("args"));
			return Ops.call_value(functionName, args);
			
		case "string_value":
			return Ops.string_value(node.getData());

		case "int_value":
			return Ops.int_value(Integer.valueOf(node.getData()));

		case "float_value":
			return Ops.float_value(Double.valueOf(node.getData()));

		default:
			throw new RuntimeException("Encountered a data I don't know: " + node.getKey());
		}
	}

	private static Arg[] makeArgs(Node[] nodes) {
		Arg[] args = new Arg[nodes.length];
		for (int i=0; i<nodes.length; i++)
			args[i] = makeArg(nodes[i]);
		return args;
	}
}