package com.dexterind.gopigo.events;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.*;

public class VoltageEventTest {

	@Test
	public void testVoltageEventObjectDouble() throws Exception {
		Object source = "test";
		VoltageEvent event = new VoltageEvent(source, 24.0);
		assertThat(event.getValue(), equalTo(24.0));
		assertThat(event.getSource(), equalTo(source));
	}

}
