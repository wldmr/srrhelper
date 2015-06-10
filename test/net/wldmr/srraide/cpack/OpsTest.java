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
	public void testFunctionNesting() {
		Function f = call_value("Testfunction",
			string_value("Hey"),
			int_value(2),
			call_value("Inner function",
				float_value(2.4),
				ref("UndergroundClub")
			)
	);
		System.out.println(f.toString());
	}
}
