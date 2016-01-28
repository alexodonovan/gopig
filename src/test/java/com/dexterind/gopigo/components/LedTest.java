package com.dexterind.gopigo.components;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.dexterind.gopigo.utils.Statuses;

@RunWith(MockitoJUnitRunner.class)
public class LedTest {

	@Mock
	private Board board;

	@Mock
	private IOException ex;

	private Led sut;

	@Before
	public void setUp() {
		sut = new Led(0, board);
	}

	@Test
	public void testSetPin_right() throws Exception {
		when(board.analogRead(7)).thenReturn(500);
		sut.setPin();
		assertThat(sut.getPin(), is(5));

		when(board.analogRead(7)).thenReturn(800);
		sut.setPin();
		assertThat(sut.getPin(), is(16));
	}

	@Test
	public void testSetPin_left() throws Exception {
		sut = new Led(1, board);
		when(board.analogRead(7)).thenReturn(500);
		sut.setPin();
		assertThat(sut.getPin(), is(10));

		when(board.analogRead(7)).thenReturn(800);
		sut.setPin();
		assertThat(sut.getPin(), is(17));
	}

	@Test
	public void testError_digital_read() throws IOException {
		when(board.analogRead(7)).thenThrow(ex);
		sut.setPin();
		verify(ex).printStackTrace();
	}

	@Test
	public void testOn_right() throws Exception {
		sut.on();
		assertThat(sut.getPin(), is(5));
	}

	@Test
	public void testOff_right() throws Exception {
		sut.off();
		// voltage > 700 seems to indicate currently turned on state
		when(board.analogRead(7)).thenReturn(800);
		assertThat(sut.getPin(), is(5));
	}

	@Test
	public void testOn_left() throws Exception {
		sut = new Led(1, board);
		when(board.analogRead(7)).thenReturn(800);
		sut.setPin();
		
		sut.on();
		assertThat(sut.getPin(), is(17));

		verify(board).setPinMode(sut.getPin(), 1);
		verify(board).digitalWrite(sut.getPin(), 1);
	}

	@Test
	public void testOn_right_error_if_board_error() throws IOException {
		when(board.setPinMode(Mockito.anyInt(), Mockito.anyInt())).thenThrow(ex);
		assertThat(sut.on(), is(Statuses.ERROR));
	}

	@Test
	public void testOff_left_error_if_board_error() throws IOException {
		sut = new Led(1, board);
		when(board.setPinMode(Mockito.anyInt(), Mockito.anyInt())).thenThrow(ex);
		assertThat(sut.off(), is(Statuses.ERROR));
	}

	@Test
	public void testOff_left() throws Exception {
		sut = new Led(1, board);

		// voltage > 700 seems to indicate currently turned on state
		when(board.analogRead(7)).thenReturn(800);
		sut.setPin();

		sut.off();
		assertThat(sut.getPin(), is(17));

		verify(board).setPinMode(sut.getPin(), 1);
		verify(board).digitalWrite(sut.getPin(), 0);
	}

}
