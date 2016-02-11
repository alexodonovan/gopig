package com.oddsocks.gopig.messaging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;



public class JacksonSerializatoinExceptionTest {
	
	private IOException ioException;
	private JacksonSerializationException sut;

	@Before
	public void setUp(){
		ioException = new IOException("some io exception");
		sut = new JacksonSerializationException("test", ioException);
	}
	
	@Test
	public void test(){
		assertThat(sut.getMessage(), is("test"));
	}

}
