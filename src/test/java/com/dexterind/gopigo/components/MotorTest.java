package com.dexterind.gopigo.components;

import static org.mockito.Mockito.*;

import java.io.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import com.dexterind.gopigo.utils.*;

@RunWith(MockitoJUnitRunner.class)
public class MotorTest {

	private int id;

	@Mock
	private Board board;

	private Motor sut;

	@Before
	public void setUp() throws IOException, InterruptedException {
		id = Motor.LEFT;
		sut = new Motor(id, board);
	}

	@Test
	public void testMoveLeftMotor() throws Exception {
		sut.move(Motor.FORWARD, 100);
		verify(board).writeI2c(Commands.M2, Motor.FORWARD, 100, Commands.UNUSED);
	}

	@Test
	public void testMoveRightMotor() throws Exception {
		id = Motor.RIGHT;
		sut = new Motor(id, board);
		sut.move(Motor.FORWARD, 100);
		verify(board).writeI2c(Commands.M1, Motor.FORWARD, 100, Commands.UNUSED);
	}

}
