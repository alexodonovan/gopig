package com.oddsocks.dexterind.gopigo.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;



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