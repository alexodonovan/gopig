package com.dexterind.gopigo.components;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import com.dexterind.gopigo.*;
import com.pi4j.io.i2c.*;

@RunWith(MockitoJUnitRunner.class)
public class BoardFactoryTest {

	@Mock
	private I2CBus bus;

	@Mock
	private Gopigo gopigo;

	@Mock
	private I2CDevice device;

	@Before
	public void setUp() throws IOException {
		when(bus.getDevice(anyInt())).thenReturn(device);
	}

	@Test
	public void testCreateBoard() throws Exception {
		Board board = BoardFactory.createBoard(bus, gopigo);
		assertThat(board, notNullValue());
	}

}
