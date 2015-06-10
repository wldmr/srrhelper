package net.wldmr.srraide.cpack;

import static org.junit.Assert.*;
import net.wldmr.srraide.cpack.Parsing.Node;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SceneBuilderTest extends SceneBuilder {

	Node root;
	
	@Before
	public void setUp() throws Exception {
		root = new Parsing.Builder().build("test-data/UnderGroundClub.srt.txt");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNesting() {
		Scene scene = build(root);
	}

}
