package net.wldmr.srraide.cpack;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OpsTest extends Ops {
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testNesting() {
		MakeString m = new MakeString();
		m.value(new Values.Value<>("Some String, OK?"));
		
		TakeString t = new TakeString();
		t.arg1(m);
		System.out.println(t);
	}
	
	@Test
	public void testAllOps() {
		Ops.FunctionNameResolver.processNested(Ops.class);
	}

	@Test
	public void testFunctionNameAnnotation() {
		@Ops.FunctionName("shit")
		class ShitClass {}
		Ops.FunctionNameResolver.process(ShitClass.class);

		assertEquals(Ops.FunctionNameResolver.get(ShitClass.class), "shit");
		assertEquals(Ops.FunctionNameResolver.get("shit"), ShitClass.class);
	}
}
