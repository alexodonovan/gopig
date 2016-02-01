package com.oddsocks.dexterind.gopigo.components;

import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oddsocks.dexterind.gopigo.utils.Commands;

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
