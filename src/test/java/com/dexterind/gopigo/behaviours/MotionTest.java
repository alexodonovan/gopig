package com.dexterind.gopigo.behaviours;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
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
	public void testForward_without_pid() throws Exception {
		sut.forward(false);
		verify(board).writeI2c(eq(Commands.MOTOR_FWD), eq(Commands.UNUSED), eq(Commands.UNUSED), eq(Commands.UNUSED));
	}

	@Test
	public void testForward_wit_pid() throws Exception {
		sut.forward(true);
		verify(board).writeI2c(eq(Commands.FWD), eq(Commands.UNUSED), eq(Commands.UNUSED), eq(Commands.UNUSED));
	}

	@Test
	public void testBackward_without_pid() throws Exception {
		sut.backward(false);
		verify(board).writeI2c(eq(Commands.MOTOR_BWD), eq(Commands.UNUSED), eq(Commands.UNUSED), eq(Commands.UNUSED));
	}

	@Test
	public void testBackward_wit_pid() throws Exception {
		sut.backward(true);
		verify(board).writeI2c(eq(Commands.BWD), eq(Commands.UNUSED), eq(Commands.UNUSED), eq(Commands.UNUSED));
	}

	@Test
	public void testLeft() throws Exception {
		sut.left();
		verify(board).writeI2c(eq(Commands.LEFT), eq(Commands.UNUSED), eq(Commands.UNUSED), eq(Commands.UNUSED));
	}

	@Test
	public void testLeftWithRotation() throws Exception {
		sut.leftWithRotation();
		verify(board).writeI2c(eq(Commands.LEFT_ROT), eq(Commands.UNUSED), eq(Commands.UNUSED), eq(Commands.UNUSED));
	}

	@Test
	public void testRight() throws Exception {
		sut.right();
		verify(board).writeI2c(eq(Commands.RIGHT), eq(Commands.UNUSED), eq(Commands.UNUSED), eq(Commands.UNUSED));
	}

	@Test
	public void testRightWithRotation() throws Exception {
		sut.rightWithRotation();
		verify(board).writeI2c(eq(Commands.RIGHT_ROT), eq(Commands.UNUSED), eq(Commands.UNUSED), eq(Commands.UNUSED));
	}

	@Test
	public void testStop() throws Exception {
		sut.stop();
		verify(board).writeI2c(eq(Commands.STOP), eq(Commands.UNUSED), eq(Commands.UNUSED), eq(Commands.UNUSED));
	}

	@Test
	public void testIncreaseSpeed() throws Exception {
		sut.increaseSpeed();
		verify(board).writeI2c(eq(Commands.ISPD), eq(Commands.UNUSED), eq(Commands.UNUSED), eq(Commands.UNUSED));
	}

	@Test
	public void testDecreaseSpeed() throws Exception {
		sut.decreaseSpeed();
		verify(board).writeI2c(eq(Commands.DSPD), eq(Commands.UNUSED), eq(Commands.UNUSED), eq(Commands.UNUSED));
	}

	@Test
	public void testReadTimeoutStatus() throws Exception {
		int[] values = { 1, 2 };
		when(board.readStatus()).thenReturn(values);
		int result = sut.readTimeoutStatus();
		assertThat(result, equalTo(2));
	}

	@Test
	public void testEnableCommunicationTimeout() throws Exception {
		sut.enableCommunicationTimeout(512);
		verify(board).writeI2c(eq(Commands.EN_COM_TIMEOUT), eq(2), eq(0), eq(Commands.UNUSED));
	}

	@Test
	public void testDisableCommunicationTimeout() throws Exception {
		sut.disableCommunicationTimeout();
		verify(board).writeI2c(eq(Commands.DIS_COM_TIMEOUT), eq(Commands.UNUSED), eq(Commands.UNUSED), eq(Commands.UNUSED));
	}

}
