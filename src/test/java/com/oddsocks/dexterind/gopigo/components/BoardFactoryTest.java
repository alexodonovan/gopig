package com.oddsocks.dexterind.gopigo.components;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.oddsocks.dexterind.gopigo.Gopigo;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;

@RunWith(MockitoJUnitRunner.class)
public class BoardFactoryTest {

	@Mock
	private I2CBus bus;

	@Mock
	private Gopigo gopigo;

	@Mock
	private I2CDevice device;

	private BoardFactory sut;

	@Before
	public void setUp() throws IOException {
		sut = new BoardFactory(gopigo);
		when(bus.getDevice(Mockito.anyInt())).thenReturn(device);
	}

	@Test
	public void testCreateBoard() throws Exception {
		Board board = sut.createBoard(bus);
		assertThat(board, notNullValue());
	}

}
