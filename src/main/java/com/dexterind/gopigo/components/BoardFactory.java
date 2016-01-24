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
		// int busId;
		//
		// String type = SystemInfo.getBoardType().name();
		//
		// if (type.indexOf("ModelA") > 0) {
		// busId = I2CBus.BUS_0;
		// } else {
		// busId = I2CBus.BUS_1;
		// }
		//
		// final I2CBus bus = I2CFactory.getInstance(busId);
		return new Board(bus.getDevice(ADDRESS), gopigo);
	}

}
