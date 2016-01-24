package com.dexterind.gopigo.utils;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.*;



public class CommandsTest {
	
	@Before
	public void setUp(){
		new Commands();
	}
	
	@Test
	public void testValues(){
		assertThat(Commands.FWD, is(119));
		assertThat(Commands.MOTOR_FWD, is(105));
	}

}