package com.oddsocks.gopig.messaging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;


@RunWith(MockitoJUnitRunner.class)
public class DefaultMessageBusTest {
	
	private DefaultMessageBus sut;
	
	@Mock
	private EventBus bus;

	@Mock
	private SubscriberExceptionHandler exceptionHandler;

	@Before
	public void init(){
		sut = new DefaultMessageBus(){};
		sut.setBus(bus);
		sut.setExceptionHandler(exceptionHandler);
	}

	@Test
	public void testPublish() throws Exception {
		sut.publish("test");
		verify(bus).post("test");
	}
	
	@Test(expected=NullPointerException.class)
	public void testPublish_throws_if_no_arg() throws Exception {
		sut.publish(null);
		verifyZeroInteractions(bus);
	}

	@Test
	public void testInit() throws Exception {
		sut.init();
		EventBus bus = sut.getBus();
		assertThat(bus, notNullValue());
	}

}
