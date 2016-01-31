package com.dexterind.gopigo.utils;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.dexterind.gopigo.Gopigo;
import com.dexterind.gopigo.events.VoltageEvent;

@RunWith(MockitoJUnitRunner.class)
public class VoltageTaskRunnerTest {

	private VoltageTaskRunner sut;

	@Mock
	private Gopigo gopigo;

	@Before
	public void setUp() {
		sut = new VoltageTaskRunner(gopigo);
	}

	@Test
	public void testRun_with_low_voltage() throws IOException {
		when(gopigo.isLowBoardVoltage()).thenReturn(true);
		sut.run();
		verify(gopigo).free();
		verify(gopigo).fireEvent(Mockito.any(VoltageEvent.class));
	}

	@Test
	public void testRun_with_critically_voltage() throws IOException {
		when(gopigo.isLowBoardVoltage()).thenReturn(false);
		when(gopigo.isCriticallyLowVoltage()).thenReturn(true);
		sut.run();
		verify(gopigo).halt();
		verify(gopigo).fireEvent(Mockito.any(VoltageEvent.class));
	}

	@Test
	public void testRun_with_good_voltage() throws IOException {
		when(gopigo.isLowBoardVoltage()).thenReturn(false);
		when(gopigo.isCriticallyLowVoltage()).thenReturn(false);
		sut.run();
		verify(gopigo).free();
		verify(gopigo, times(0)).fireEvent(Mockito.any(VoltageEvent.class));
	}

	@Test
	public void testRun_with_board_read_error() throws IOException {
		when(gopigo.isLowBoardVoltage()).thenThrow(new IOException());
		sut.run();
		verify(gopigo).isLowBoardVoltage();
		verifyNoMoreInteractions(gopigo);
	}

}
