package com.dexterind.gopigo.components;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.dexterind.gopigo.Gopigo;
import com.dexterind.gopigo.utils.Debug;
import com.dexterind.gopigo.utils.Statuses;
import com.pi4j.io.i2c.I2CDevice;

@RunWith(MockitoJUnitRunner.class)
public class BoardTest {

	@Mock
	private I2CDevice device;

	private Board sut;

	@Mock
	private Gopigo gopigo;

	@Mock
	private Debug debug;

	@Before
	public void setUp() throws Exception {
		sut = new Board(device, gopigo);
		sut.setDebug(debug);
		when(gopigo.isHalt()).thenReturn(false);

	}

	@Test
	public void testInit() throws Exception {
		sut.init();
		verify(device).write(0xfe, (byte) 0x04);
	}

	@Test
	public void init_with_device_write_error() throws IOException {
		doThrow(IOException.class).when(device).write(Mockito.anyInt(), Mockito.anyByte());
		sut.init();
		// halts the gopigo
		verify(gopigo).halt();
		// logs the error
		verify(debug).log(eq(Debug.SEVERE), Mockito.anyString());
	}

	@Test
	public void testWriteI2c() throws Exception {
		int result = sut.writeI2c(2);
		assertThat(result, is(Statuses.OK));
		byte[] bytes = { (byte) 2 };
		verify(device).write(0xfe, bytes, 0, 1);
	}

	@Test
	public void writeI2c_calls_halt_on_gopigo_if_halt() throws Exception {
		when(gopigo.isHalt()).thenReturn(true);
		sut.writeI2c(2);
		verify(gopigo).onHalt();
	}

	@Test
	public void testReadI2c() throws Exception {
		byte[] readI2c = sut.readI2c(5);
		assertThat(readI2c.length, is(5));
		verify(device).read(eq(1), Mockito.any(byte[].class), eq(0), eq(5));
	}

	// TODO find a way to mock the response from read on device?
	@Test
	public void testReadStatus() throws Exception {
		int[] readStatus = sut.readStatus();
		assertThat(readStatus.length, is(2));
	}

	@Test
	public void testVolt() throws Exception {
		double volt = sut.volt();
		assertThat(volt, equalTo(0.0));
	}

	@Test
	public void testAnalogWrite() throws Exception {
		assertThat(sut.analogWrite(10, 2), is(Statuses.OK));
		byte[] bytes = { (byte) 15, (byte) 10, (byte) 2, (byte) 0 };
		verify(device).write(0xfe, bytes, 0, 4);

		// always returns -2 unless pin 10 sent
		assertThat(sut.analogWrite(9, 2), is(-2));

	}

	@Test
	public void testDigitalWrite() throws Exception {
		assertThat(sut.digitalWrite(5, 10), is(Statuses.ERROR));

		assertThat(sut.digitalWrite(5, 0), is(1));
		assertThat(sut.digitalWrite(5, 1), is(1));
		byte[] bytes = { (byte) 12, (byte) 5, (byte) 0, (byte) 0 };
		verify(device).write(0xfe, bytes, 0, 4);
	}

}
