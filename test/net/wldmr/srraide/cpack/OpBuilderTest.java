package net.wldmr.srraide.cpack;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import net.wldmr.srraide.cpack.Ops.Arg;
import net.wldmr.srraide.cpack.Parsing.Node;
import static net.wldmr.srraide.cpack.Ops.*;

import org.junit.Test;


public class OpBuilderTest extends OpBuilder {
	
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
		Node node = parse(input).getChild("ops");
		Op op = build(node);

		assertEquals("Kill all humans", op.getFunctionName());
		assertTrue(op.getArgs().length == 1);
		assertTrue(op.getArgs()[0] instanceof Function);
		Function func = (Function) op.getArgs()[0];
		assertEquals("Find humans in vicinity", func.getFunctionName());
	}

	@Test
	public void testCallValue() throws Exception {
		String input =
				  "call_value {\n"
				+ "  functionName: \"Find humans in vicinity\"\n"
				+ "  args {\n"
				+ "    int_value: 20\n"
				+ "  }\n"
				+ "}\n";
		Node node = parse(input).getChild("call_value");
		Arg arg = wrapInArg(node);

		assertTrue(arg instanceof Function);
		Function func = (Function) arg;
		assertEquals("Find humans in vicinity", func.getFunctionName());
		Arg[] args = func.getArgs();
		assertTrue(args.length == 1);
		assertTrue(args[0].equals(20));
	}

	@Test
	public void testFloatValue() throws Exception {
		Node node = parse("float_value: 2.25").getChild("float_value");
		Arg arg = wrapInArg(node);

		assertTrue(arg instanceof FloatValue);
		FloatValue farg = (FloatValue) arg;
		assertEquals(2.25f, farg.getValue(), 0.0000001f);
		// Short form:
		assertTrue(farg.equals(2.25f));
	}

	@Test
	public void testStringValue() throws Exception {
		Node node = parse("string_value: \"Hurr, durr, ima Strnigg!\"").getChild("string_value");
		Arg arg = wrapInArg(node);

		assertTrue(arg instanceof StringValue);
		StringValue sarg = (StringValue) arg;
		assertEquals("Hurr, durr, ima Strnigg!", (String) sarg.getValue());
		// Short form:
		assertTrue(sarg.equals("Hurr, durr, ima Strnigg!"));
	}

	@Test
	public void testIntValue() throws Exception {
		Node node = parse("int_value: 20").getChild("int_value");
		Arg arg = wrapInArg(node);

		assertTrue(arg instanceof IntValue);
		IntValue iarg = (IntValue) arg;
		assertEquals(20, (int) iarg.getValue());
		// Short form:
		assertTrue(iarg.equals(20));
	}

}
