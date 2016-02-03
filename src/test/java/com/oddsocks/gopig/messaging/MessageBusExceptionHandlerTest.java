package com.oddsocks.gopig.messaging;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import com.google.common.eventbus.SubscriberExceptionContext;

@RunWith(MockitoJUnitRunner.class)
public class MessageBusExceptionHandlerTest {

	@Mock
	private Logger logger;

	@Mock
	private Throwable th;

	@Mock
	private SubscriberExceptionContext context;

	@Mock
	private MessageBusExceptionHandler sut;

	@Before
	public void setUp() {
		sut = new MessageBusExceptionHandler();
		sut.setLogger(logger);
	}

	@Test
	public void testHandleException() throws Exception {
		sut.handleException(th, context);
		verify(logger).error(Mockito.anyString(), any(Throwable.class), Mockito.anyObject(), any(Method.class), Mockito.anyObject());
	}

}
