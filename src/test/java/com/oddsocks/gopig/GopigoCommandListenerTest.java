package com.oddsocks.gopig;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oddsocks.dexterind.gopigo.Gopigo;
import com.oddsocks.dexterind.gopigo.events.StatusEvent;
import com.oddsocks.dexterind.gopigo.events.VoltageEvent;

@RunWith(MockitoJUnitRunner.class)
public class GopigoCommandListenerTest {

	private GopigoCommandListener sut;

	@Mock
	private Gopigo gopigo;

	@Mock
	private VoltageEvent volatgeEvent;

	@Mock
	private StatusEvent statusEvent;

	@Before
	public void setUp() {
		sut = new GopigoCommandListener();
		sut.setGopigo(gopigo);
	}

	@Test
	public void testInit() throws Exception {
		sut.init();
		verify(gopigo).addListener(sut);
		verify(gopigo).setMinVoltage(5.5);
		verify(gopigo).init();
	}

	@Test
	public void testOnStatusEvent() throws Exception {
		sut.onStatusEvent(statusEvent);
		verifyZeroInteractions(gopigo);
	}

	@Test
	public void testOnVoltageEvent() throws Exception {
		sut.onVoltageEvent(volatgeEvent);
		verifyZeroInteractions(gopigo);
	}

}
