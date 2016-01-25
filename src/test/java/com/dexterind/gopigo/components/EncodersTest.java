package com.dexterind.gopigo.components;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.dexterind.gopigo.utils.Commands;
import com.dexterind.gopigo.utils.Statuses;

@RunWith(MockitoJUnitRunner.class)
public class EncodersTest {

	@Mock
	private Board board;

	private Encoders sut;

	@Before
	public void setUp() throws IOException, InterruptedException {
		sut = new Encoders(board);

	}

	@Test
	public void testTargeting() throws Exception {
		assertThat(sut.targeting(2, 0, 25), is(Statuses.ERROR));
		assertThat(sut.targeting(-1, 0, 25), is(Statuses.ERROR));
		assertThat(sut.targeting(0, 2, 25), is(Statuses.ERROR));
		assertThat(sut.targeting(0, -1, 25), is(Statuses.ERROR));

		sut.targeting(1, 1, 25);
		verify(board).writeI2c(Commands.ENC_TGT, (byte) 3, (byte) 0.25, 25);
	}

	@Test
	public void testRead() throws Exception {
		byte[] ret1 = { 2 };
		when(board.readI2c(1)).thenReturn(ret1);
		assertThat(sut.read(1), is(514));

		verify(board).writeI2c(Commands.ENC_READ, 1, Commands.UNUSED, Commands.UNUSED);
		verify(board).sleep(80);
		verify(board, times(2)).readI2c(1);

	}

	@Test
	public void testReadStatus() throws Exception {
		int[] value = { 1, 2 };
		when(board.readStatus()).thenReturn(value);
		assertThat(sut.readStatus(), is(1));
		verify(board).readStatus();
	}

	@Test
	public void testEnable() throws Exception {
		sut.enable();
		verify(board).writeI2c(Commands.EN_ENC, Commands.UNUSED, Commands.UNUSED, Commands.UNUSED);
	}

	@Test
	public void testDisable() throws Exception {
		sut.disable();
		verify(board).writeI2c(Commands.DIS_ENC, Commands.UNUSED, Commands.UNUSED, Commands.UNUSED);
	}
}
