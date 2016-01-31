package com.dexterind.gopigo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.EventObject;
import java.util.Timer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.dexterind.gopigo.behaviours.Motion;
import com.dexterind.gopigo.components.Board;
import com.dexterind.gopigo.components.Encoders;
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

	@Mock
	private Encoders encoders;

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
		sut.setEncoders(encoders);
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
	
	@Test
	public void testInit_when_halted() throws Exception {
		sut.halt();
		sut.init();
		verify(board).init();
		verify(voltageTimer, times(0)).scheduleAtFixedRate(voltageTaskTimer, 0, 60000);
		verify(listener, times(0)).onStatusEvent(new StatusEvent(sut, Statuses.INIT));
	}

	@Test
	public void testFree() throws Exception {
		sut.halt();
		assertThat(sut.isHalt(), is(true));
		sut.free();
		assertThat(sut.isHalt(), is(false));

		assertThat(sut.isOperative(), is(true));
	}

	@Test
	public void testHalt() throws Exception {
		assertThat(sut.isHalt(), is(false));
		sut.halt();
		assertThat(sut.isHalt(), is(true));

		// second call has not effect
		sut.halt();
		assertThat(sut.isHalt(), is(true));

		assertThat(sut.isOperative(), is(false));
	}

	@Test
	public void testMinVoltage() throws Exception {
		assertThat(sut.getMinVoltage(), is(5.5));
		sut.setMinVoltage(12.0);
		assertThat(sut.getMinVoltage(), is(12.0));
	}

	@Test
	public void testCriticalVoltage() throws Exception {
		assertThat(sut.getCriticalVoltage(), is(1.0));
		sut.setCriticalVoltage(12.0);
		assertThat(sut.getCriticalVoltage(), is(12.0));
	}

	@Test
	public void testRight90() throws Exception {
		sut.right90();
		verify(encoders).targeting(1, 1, 18);
		verify(motion).rightWithRotation();
	}

	@Test
	public void testBoardVoltage() throws Exception {
		when(board.volt()).thenReturn(5.5);
		assertThat(sut.boardVoltage(), is(5.5));
	}

	@Test
	public void testBoardVersion() throws Exception {
		when(board.version()).thenReturn(2.1f);
		assertThat(sut.boardVersion(), is(2.1f));
	}

	@Test
	public void testBoardRevision() throws Exception {
		when(board.revision()).thenReturn(2);
		assertThat(sut.boardRevision(), is(2));
	}

	@Test
	public void testIsLowBoardVoltage() throws Exception {
		when(board.volt()).thenReturn(0.5);
		assertThat(sut.isLowBoardVoltage(), is(true));

		when(board.volt()).thenReturn(7.0);
		assertThat(sut.isLowBoardVoltage(), is(false));
	}

	@Test
	public void testIsCriticallyLowVoltage() throws Exception {
		when(board.volt()).thenReturn(0.5);
		assertThat(sut.isCriticallyLowVoltage(), is(true));

		when(board.volt()).thenReturn(7.0);
		assertThat(sut.isCriticallyLowVoltage(), is(false));
	}

}
