package com.dexterind.gopigo.components;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.system.SystemInfo;

public class BusFactory {

	public static I2CBus createBus() throws IOException, InterruptedException {
		int busId;
		String type = SystemInfo.getBoardType()
								.name();

		if (type.indexOf("ModelA") > 0) {
			busId = I2CBus.BUS_0;
		} else {
			busId = I2CBus.BUS_1;
		}

		return I2CFactory.getInstance(busId);
	}

}
