package com.dexterind.gopigo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.EventObject;
import java.util.Timer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.dexterind.gopigo.behaviours.Motion;
import com.dexterind.gopigo.components.Board;
import com.dexterind.gopigo.components.Led;
import com.dexterind.gopigo.events.StatusEvent;
import com.dexterind.gopigo.events.VoltageEvent;
import com.dexterind.gopigo.utils.Statuses;
import com.dexterind.gopigo.utils.VoltageTaskTimer;

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

	@Mock
	private VoltageTaskTimer voltageTaskTimer;

	@Mock
	private Timer voltageTimer;

	@Mock
	private Board board;

	@Before
	public void setUp() {
		sut = new Gopigo();
		sut.setLedLeft(ledLeft);
		sut.setLedRight(ledRight);
		sut.setMotion(motion);
		sut.addListener(listener);
		sut.setVoltageTaskTimer(voltageTaskTimer);
		sut.setVoltageTimer(voltageTimer);
		sut.setBoard(board);
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

	@Test
	public void testInit() throws Exception {
		sut.init();
		verify(board).init();
		verify(voltageTimer).scheduleAtFixedRate(voltageTaskTimer, 0, 60000);
		verify(listener).onStatusEvent(new StatusEvent(sut, Statuses.INIT));
	}

}
