package net.wldmr.srraide.cpack;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import net.wldmr.srraide.cpack.Parsing.Node;
import static net.wldmr.srraide.cpack.Ops.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SceneBuilderTest extends SceneBuilder {
	
	//@Before
	public void setUp() throws Exception {
		//root = new Parsing.Builder().build("test-data/UnderGroundClub.srt.txt");
	}
	
	private Node parse(String input) throws IOException {
		String fakeRoot = "FakeRoot";
		BufferedReader br = new BufferedReader(new StringReader(input));
		return (new Parsing.Builder()).build(fakeRoot, br);
	}

	@Test
	public void testOp() throws Exception {
		String input =
				  "ops {\n"
				+ "  functionName: \"Kill all humans\"\n"
				+ "  args {\n"
				+ "    call_value {\n"
				+ "      functionName: \"Find humans in vicinity\"\n"
				+ "      args {\n"
				+ "        int_value: 20\n"
				+ "      }\n"
				+ "    }\n"
				+ "  }\n"
				+ "}\n";
	}

	@Test
	public void testIntArg() throws Exception {
		Node node = parse("int_value: 20").getChild("int_value");
		Arg arg = makeArg(node);

		assertTrue(arg instanceof IntValue);
		IntValue iarg = (IntValue) arg;
		assertEquals(20, (int) iarg.getValue());
		// Short form:
		assertTrue(iarg.equals(20));
	}

}
