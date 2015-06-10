package net.wldmr.srraide.cpack;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParsingTest extends Parsing {

	private Builder builder;
	
	private Node buildFromString(String rootname, String input) throws IOException {
		BufferedReader br = new BufferedReader(new StringReader(input));
		return builder.build(rootname, br);
	}

	@Before
	public void setUp() throws Exception {
		builder = new Builder();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testMultipleValues() throws IOException {
		Node root = buildFromString("MultiValueTest",
			( "keyword: literal_value\n"
			+ "keyword: another_value\n"
			+ "keyword: this_works")
		);

		String[] nodes = root.getValues("keyword");
		String[] expected = {"literal_value", "another_value", "this_works"};
		assertArrayEquals(nodes, expected);
	}
	
	@Test
	public void testSingleValue() throws IOException {
		Node root = buildFromString("ValueTest", "   keyword: literal_value   ");
		// (Spaces added for kicks)

		assertEquals(root.getKey(), "ValueTest");

		String[] nodeTypes = root.getChildKeys();
		assertEquals(nodeTypes.length, 1);
		assertEquals(nodeTypes[0], "keyword");
		
		Node[] nodes = root.getChildren("keyword");
		assertEquals(nodes.length, 1);

		Node node = nodes[0];
		String value = node.getData();
		assertEquals(value, "literal_value");
		
		assertEquals(value, node.getData());
		assertEquals(value, root.getValue("keyword"));
		assertEquals(value, root.getValue("keyword", 0));
		
	}
	
}
