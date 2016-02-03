package com.oddsocks.gopig.messaging;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.eventbus.EventBus;

@RunWith(MockitoJUnitRunner.class)
public class CommandBusTest {

	@Mock
	private EventBus bus;

	@Mock
	private ForwardCommand command;

	private CommandBus sut;

	@Before
	public void setUp() {
		sut = new CommandBus();
		sut.setBus(bus);
	}

	@Test
	public void testPublish() throws Exception {
		sut.publish(command);
		verify(bus).post(command);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPublish_only_accepts_commands() throws Exception {
		sut.publish("");
		verifyZeroInteractions(bus);
	}
}
