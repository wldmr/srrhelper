package net.wldmr.srraide.cpack;

import net.wldmr.srraide.cpack.Parsing.Node;

public interface CPackPartBuilder<T> {
	public T build(Node n);
}
