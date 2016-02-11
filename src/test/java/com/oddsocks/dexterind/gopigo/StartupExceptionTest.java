package com.oddsocks.dexterind.gopigo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;



public class StartupExceptionTest {
	
	private IOException ioException;
	private StartupException sut;

	@Before
	public void setUp(){
		ioException = new IOException("some io exception");
		sut = new StartupException("test", ioException);
	}
	
	@Test
	public void test(){
		assertThat(sut.getMessage(), is("test"));
	}

}
