package net.wldmr.srraide.cpack;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ParsingTest extends Parsing {

	private Node root;
	
	@Before
	public void setUp() throws IOException {
		String input =
			  "multivalue: 1\n"
			+ "multivalue: 2\n"
			+ "int: 2\n"
			+ "float: 2.3456\n"
			+ "word: something\n"
			+ "string: \"This is a String Value\"\n"

			+ "tree {\n"
			+ "  subtree {\n"
			+ "    item: whatever\n"
			+ "  }\n"
			+ "  subtree {\n"
			+ "    item: dontcare\n"
			+ "  }\n"
			+ "}\n"
			
			+ "sumpn_else {\n"
			+ "}\n";
		BufferedReader br = new BufferedReader(new StringReader(input));
		root = (new Builder()).build("ParsingTest", br);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testRootKey() throws IOException {
		assertEquals("ParsingTest", root.getKey());
	}

	@Test
	public void testStringValue() throws IOException {
		assertEquals("This is a String Value", root.getValue("string"));
	}

	@Test
	public void testWordValue() throws IOException {
		assertEquals("something", root.getValue("word"));
	}

	@Test
	public void testIntValue() throws IOException {
		assertEquals("2", root.getValue("int"));
	}

	@Test
	public void testFloatValue() throws IOException {
		assertEquals("2.3456", root.getValue("float"));
	}

	@Test
	public void testMultipleValues() throws IOException {
		String[] expected = {"1", "2"};
		String[] actuals = root.getValues("multivalue");
		assertArrayEquals(expected, actuals);
	}
	
	@Test
	public void testChildKeys() throws IOException {
		// Child keys are in order, based on first occurrence.
		String[] expected = {"multivalue", "int", "float", "word", "string", "tree", "sumpn_else"};
		String[] actuals = root.getChildKeys();
		assertArrayEquals(expected, actuals);
	}
		
	@Test
	public void testSubTrees() throws IOException {
		Node tree = root.getChild("tree");
		assertSame(tree, root.getChild("tree", 0));

		Node[] subs = tree.getChildren("subtree");
		assertEquals(2, subs.length);
		assertEquals("whatever", subs[0].getValue("item"));
		assertEquals("dontcare", subs[1].getValue("item"));
	}
	
	@Test
	public void testShortcuts() throws IOException {
		String value = root.getChild("word").getData();

		assertEquals(value, root.getValue("word"));
		assertEquals(value, root.getValue("word", 0));
	}
}
