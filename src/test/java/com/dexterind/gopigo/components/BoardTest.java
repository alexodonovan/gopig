package com.dexterind.gopigo.components;

import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import com.pi4j.io.i2c.*;


@RunWith(MockitoJUnitRunner.class)
public class BoardTest {

	@Mock
	private I2CDevice device;
	
	private Board sut;

	@Before
	public void setUp() throws Exception {
		sut =  new Board(device);
	}

	@Test
	public void testInit() throws Exception {
		sut.init();
		verify(device).write(0xfe, (byte) 0x04);
	}

}
