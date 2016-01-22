package com.dexterind.gopigo.behaviours;

import static org.mockito.Mockito.*;

import java.io.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import com.dexterind.gopigo.components.*;
import com.dexterind.gopigo.utils.*;

@RunWith(MockitoJUnitRunner.class)
public class MotionTest {

	private Motion sut;

	@Mock
	private Board board;

	@Before
	public void setUp() throws IOException, InterruptedException {
		sut = new Motion(board);
	}

	@Test
	public void testForward() throws Exception {
		sut.forward(false);
		verify(board).writeI2c(eq(Commands.MOTOR_FWD), eq(Commands.UNUSED), eq(Commands.UNUSED), eq(Commands.UNUSED));
	}

}
