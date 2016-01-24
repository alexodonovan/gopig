package com.dexterind.gopigo.components;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import com.pi4j.io.i2c.*;


@RunWith(MockitoJUnitRunner.class)
public class BoardFactoryTest {
	
	@Mock
	private I2CBus bus;

	@Test
	public void testCreateBoard() throws Exception {
		Board board = BoardFactory.createBoard(bus);
		assertThat(board, notNullValue());
	}

}
