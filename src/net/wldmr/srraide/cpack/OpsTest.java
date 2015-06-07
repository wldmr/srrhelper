package net.wldmr.srraide.cpack;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class OpsTest extends Ops {
	
	//@BeforeClass
	public static void setUpClass() throws Exception {
		Ops.FunctionNameResolver.processNested(Ops.class);
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
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
