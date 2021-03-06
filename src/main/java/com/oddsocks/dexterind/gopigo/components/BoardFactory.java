package com.oddsocks.dexterind.gopigo.components;

import java.io.IOException;

import com.oddsocks.dexterind.gopigo.Gopigo;
import com.pi4j.io.i2c.I2CBus;

public class BoardFactory {
	
	/**
	 * The device's address.
	 */
	private static final byte ADDRESS = 0x08;

	private final Gopigo gopigo;

	public BoardFactory(Gopigo gopigo) {
		this.gopigo = gopigo;
	}

	public Board createBoard(I2CBus bus) throws IOException, InterruptedException {
		return new Board(bus.getDevice(ADDRESS), gopigo);
	}

}
