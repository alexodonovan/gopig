package com.dexterind.gopigo.events;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.*;

public class StatusEventTest {

	@Test
	public void testStatusEventObjectInt() throws Exception {
		String source = "";
		StatusEvent event = new StatusEvent(source, 15);
		assertThat(event.getStatus(), equalTo(15));
		assertThat(event.getSource(), equalTo((Object) source));
	}
}
