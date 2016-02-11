package com.oddsocks.gopig;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

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
	
	@Mock 
	private Logger logger;

	@Before
	public void setUp() {
		sut = new GopigoCommandListener();
		sut.setGopigo(gopigo);
		sut.setLogger(logger);
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
		verify(logger).info(Mockito.anyString(), Mockito.eq(statusEvent));
	}

	@Test
	public void testOnVoltageEvent() throws Exception {
		sut.onVoltageEvent(volatgeEvent);
		verifyZeroInteractions(gopigo);
		verify(logger).info(Mockito.anyString(), Mockito.eq(volatgeEvent));
	}

}
