package com.dexterind.gopigo.utils;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.*;

public class StatusesTest {

	@Test
	public void testStatuses() throws Exception {
		new Statuses();
		assertThat(Statuses.ERROR, is(-1));
		assertThat(Statuses.OK, is(1));
		assertThat(Statuses.INIT, is(2));
		assertThat(Statuses.HALT, is(3));
	}

}
