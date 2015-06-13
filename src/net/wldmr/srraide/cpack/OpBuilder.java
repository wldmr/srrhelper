package net.wldmr.srraide.cpack;

import net.wldmr.srraide.cpack.Ops.Arg;
import net.wldmr.srraide.cpack.Ops.Op;
import net.wldmr.srraide.cpack.Parsing.Node;

public class OpBuilder implements CPackPartBuilder<Op> {

	public Op build(Node node) {
		String functionName = node.getValue("functionName");
		Arg[] args = makeArgs(node);
		Op op = new Op(functionName, args);
		return op;
	}

	protected Arg wrapInArg(Node node) {
		switch (node.getKey()) {
		case "call_value":
			String functionName = node.getValue("functionName");
			Arg[] args = makeArgs(node);
			return Ops.call_value(functionName, args);
			
		case "string_value":
			return Ops.string_value(node.getData());
	
		case "int_value":
			return Ops.int_value(Integer.valueOf(node.getData()));
	
		case "float_value":
			return Ops.float_value(Float.valueOf(node.getData()));
	
		default:
			throw new RuntimeException("Encountered data I don't know: " + node.getKey());
		}
	}

	protected Arg[] makeArgs(Node funcnode) {
		Node[] argnodes = funcnode.getChildren("args");
		Arg[] args = new Arg[argnodes.length];
		for (int i=0; i<argnodes.length; i++) {
			// At this point we don't know what kind of value this argnode holds.
			// So we look brute force.
			Node argnode = argnodes[i];
			String[] keys = argnode.getChildKeys();
			assert keys.length == 1: "An arg should only have one kind of child.";
			assert argnode.getChildren(keys[0]).length == 1: "An arg should only have one value";
			Node value = argnode.getChild(keys[0]);
			args[i] = wrapInArg(value);
		}
		return args;
	}

	public OpBuilder() {
		super();
	}

}