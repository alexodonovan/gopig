package com.dexterind.gopigo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.EventObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.dexterind.gopigo.behaviours.Motion;
import com.dexterind.gopigo.components.Led;
import com.dexterind.gopigo.events.StatusEvent;
import com.dexterind.gopigo.events.VoltageEvent;
import com.dexterind.gopigo.utils.Statuses;

@RunWith(MockitoJUnitRunner.class)
public class GopigoTest {

	private Gopigo sut;

	@Mock
	private Motion motion;

	@Mock
	private Led ledRight;

	@Mock
	private Led ledLeft;

	@Mock
	private GopigoListener listener;

	@Mock
	private StatusEvent statusEvent;

	@Mock
	private VoltageEvent voltageEvent;

	@Mock
	private EventObject genericEvent;

	@Before
	public void setUp() {
		sut = new Gopigo();
		sut.setLedLeft(ledLeft);
		sut.setLedRight(ledRight);
		sut.setMotion(motion);
		sut.addListener(listener);
	}

	@Test
	public void testGopigo() throws Exception {
		sut.reset();
		verify(ledLeft).off();
		verify(ledRight).off();
		verify(motion).setSpeed(255);
	}

	@Test
	public void testAddListener() throws Exception {
		// listener added during setup
		assertThat(sut.hasListener(listener), is(true));
	}

	@Test
	public void testFireEvent() throws Exception {
		sut.fireEvent(statusEvent);
		verify(listener).onStatusEvent(statusEvent);

		sut.fireEvent(voltageEvent);
		verify(listener).onVoltageEvent(voltageEvent);

		sut.fireEvent(genericEvent);
		verifyZeroInteractions(listener);
	}

	@Test
	public void testRemoveListener() throws Exception {
		assertThat(sut.hasListener(listener), is(true));
		sut.removeListener(listener);
		assertThat(sut.hasListener(listener), is(false));

		sut = new Gopigo();
		// remove listener that wasn't added has not effect
		sut.removeListener(listener);
		assertThat(sut.hasListener(listener), is(false));
	}

	@Test
	public void testOnHalt() throws Exception {
		sut.onHalt();
		verify(listener).onStatusEvent(new StatusEvent(this, Statuses.HALT));

	}

}
