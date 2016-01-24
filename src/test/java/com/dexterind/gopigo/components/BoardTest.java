package com.dexterind.gopigo.components;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import com.dexterind.gopigo.*;
import com.dexterind.gopigo.utils.*;
import com.pi4j.io.i2c.*;

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

}
