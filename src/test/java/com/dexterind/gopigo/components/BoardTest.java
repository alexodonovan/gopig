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
import com.dexterind.gopigo.utils.Commands;
import com.dexterind.gopigo.utils.Statuses;
import com.pi4j.io.i2c.I2CDevice;

@RunWith(MockitoJUnitRunner.class)
public class BoardTest {

	@Mock
	private I2CDevice device;

	private Board sut;

	@Mock
	private Gopigo gopigo;

	@Before
	public void setUp() throws Exception {
		sut = new Board(device, gopigo);
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
		byte[] bytes2 = { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 };
		verifyRead1(bytes2, 1);
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
		verifyWriteBytes(bytes);

		// always returns -2 unless pin 10 sent
		assertThat(sut.analogWrite(9, 2), is(-2));

	}

	@Test
	public void testDigitalWrite() throws Exception {
		assertThat(sut.digitalWrite(5, 10), is(Statuses.ERROR));

		assertThat(sut.digitalWrite(5, 0), is(1));
		assertThat(sut.digitalWrite(5, 1), is(1));
		byte[] bytes = { (byte) 12, (byte) 5, (byte) 0, (byte) 0 };
		verifyWriteBytes(bytes);
	}

	private void verifyWriteBytes(byte[] bytes) throws IOException {
		verify(device).write(0xfe, bytes, 0, 4);
	}

	@Test
	public void testVersion() throws Exception {
		sut.version();
		byte[] bytes = { Commands.FW_VER, Commands.UNUSED, Commands.UNUSED, Commands.UNUSED };
		verifyWriteBytes(bytes);
		byte[] bytes2 = { (byte) 0 };
		verifyRead1(bytes2, 2);
	}

	private void verifyRead1(byte[] bytes, int times) throws IOException {
		verify(device, Mockito.times(times)).read(eq(1), eq(bytes), eq(0), eq(bytes.length));
	}

	@Test
	public void testRevision() throws Exception {
		int revision = sut.revision();
		byte[] bytes = { Commands.ANALOG_READ, 7, Commands.UNUSED, Commands.UNUSED };
		verifyWriteBytes(bytes);
		byte[] bytes2 = { 0 };
		verifyRead1(bytes2, 2);
		assertThat(0, equalTo(revision));
	}

	@Test
	public void testSetPinMode() throws Exception {
		sut.setPinMode(2, 1);
		byte[] bytes = { Commands.PIN_MODE, 2, 1, Commands.UNUSED };
		verifyWriteBytes(bytes);
	}

	@Test
	public void testAnalogRead() throws Exception {
		whenRead(5);
		int analogRead = sut.analogRead(2);
		byte[] bytes = { Commands.ANALOG_READ, 2, Commands.UNUSED, Commands.UNUSED };
		verifyWriteBytes(bytes);
		assertThat(analogRead, is(0));
	}

	private void whenRead(Integer retVal) throws IOException {
		when(device.read(eq(1), Mockito.any(byte[].class), eq(0), Mockito.anyInt())).thenReturn(retVal);
	}

	@Test
	public void testDigitalRead() throws Exception {
		when(device.read()).thenReturn(4);
		int digitalRead = sut.digitalRead(2);
		byte[] bytes = { Commands.DIGITAL_READ, 2, Commands.UNUSED, Commands.UNUSED };
		verifyWriteBytes(bytes);
		verify(device).read();
		assertThat(digitalRead, is(4));
	}

}
