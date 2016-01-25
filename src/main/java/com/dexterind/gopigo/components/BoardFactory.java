package com.dexterind.gopigo.components;

import java.io.*;

import com.dexterind.gopigo.*;
import com.pi4j.io.i2c.*;

public class BoardFactory {

	/**
	 * The device's address.
	 */
	private static final byte ADDRESS = 0x08;

	public static Board createBoard(I2CBus bus, Gopigo gopigo) throws IOException, InterruptedException {
		return new Board(bus.getDevice(ADDRESS), gopigo);
	}

}
