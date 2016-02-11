package com.oddsocks.gopig.messaging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class JacksonJsonMapperTest {

	private JacksonJsonMapper sut;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private ForwardCommand command;

	@Mock
	private Logger logger;

	@Before
	public void setUp() {
		sut = new JacksonJsonMapper();
		sut.setObjectMapper(objectMapper);
		sut.setLogger(logger);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReadValue() throws JsonParseException, JsonMappingException, IOException {
		when(objectMapper.readValue(Mockito.eq("json"), Mockito.any(Class.class))).thenReturn(command);
		ForwardCommand result = sut.fromJson("json", ForwardCommand.class);
		assertThat(result, equalTo(command));
		verify(objectMapper).readValue(Mockito.anyString(), Mockito.eq(ForwardCommand.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testReadValue_bytes() throws JsonParseException, JsonMappingException, IOException {
		when(objectMapper.readValue(Mockito.eq("json"), Mockito.any(Class.class))).thenReturn(command);
		ForwardCommand result = sut.fromJson("json".getBytes(), ForwardCommand.class);
		assertThat(result, equalTo(command));
		verify(objectMapper).readValue(Mockito.anyString(), Mockito.eq(ForwardCommand.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=JacksonSerializationException.class)
	public void exception_handling_test() throws JsonParseException, JsonMappingException, IOException{
		when(objectMapper.readValue(Mockito.anyString(), Mockito.any(Class.class))).thenThrow(new IOException("test"));
		sut.fromJson("", String.class);
		verify(logger).error(Mockito.anyString(), "test");
	}
}
