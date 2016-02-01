package com.oddsocks.dexterind.gopigo.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oddsocks.dexterind.gopigo.Gopigo;
import com.oddsocks.dexterind.gopigo.events.VoltageEvent;

public class VoltageTaskRunner implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(VoltageTaskRunner.class);

	private final Gopigo gopigo;

	public VoltageTaskRunner(Gopigo gopigo) {
		this.gopigo = gopigo;
	}

	// TODO refactor
	@Override
	public void run() {
		Boolean dispatchEvent = false;
		try {
			logger.info("Voltage check");

			if (gopigo.isLowBoardVoltage()) {
				logger.debug("Low voltage detected");
				gopigo.free();
				dispatchEvent = true;
			} else if (gopigo.isCriticallyLowVoltage()) {
				logger.debug("Critical voltage detected. GoPiGo is now halted.");
				gopigo.halt();
				dispatchEvent = true;
			} else {
				gopigo.free();
			}

			if (dispatchEvent) {
				VoltageEvent voltageEvent = new VoltageEvent(gopigo, gopigo.boardVoltage());
				gopigo.fireEvent(voltageEvent);
			}
		} catch (IOException e) {
			logger.error("Error {}", e);
		}
	}

}
