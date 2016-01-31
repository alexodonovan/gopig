package com.dexterind.gopigo.events;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;

public class StatusEventTest {

	private int status = 15;
	private String source = "";
	private StatusEvent sut;

	@Before
	public void setUp() {
		sut = new StatusEvent(source, status);
	}

	@Test
	public void testStatusEventObjectInt() throws Exception {
		assertThat(sut.getStatus(), equalTo(15));
		assertThat(sut.getSource(), equalTo((Object) source));
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(sut.equals(null), is(false));
		assertThat(sut.equals(""), is(false));
		assertThat(sut.equals(sut), is(true));
		assertThat(sut.equals(new StatusEvent(source, status)), is(true));
	}

	@Test
	public void testHashCode() throws Exception {
		assertThat(sut.hashCode(), equalTo(new StatusEvent(source, status).hashCode()));
	}

	@Test
	public void testToString() throws Exception {
		StatusEvent other = new StatusEvent(source, status);
		assertThat(other.toString(), equalTo(sut.toString()));
	}
}
