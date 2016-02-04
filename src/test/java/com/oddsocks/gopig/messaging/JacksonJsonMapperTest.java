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

	@Before
	public void setUp() {
		sut = new JacksonJsonMapper();
		sut.setObjectMapper(objectMapper);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReadValue() throws JsonParseException, JsonMappingException, IOException {
		when(objectMapper.readValue(Mockito.eq("json"), Mockito.any(Class.class))).thenReturn(command);
		ForwardCommand result = sut.fromJson("json", ForwardCommand.class);
		assertThat(result, equalTo(command));
		verify(objectMapper).readValue(Mockito.anyString(), Mockito.eq(ForwardCommand.class));
	}
}
