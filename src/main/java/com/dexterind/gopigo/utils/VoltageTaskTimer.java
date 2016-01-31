package com.dexterind.gopigo.utils;

import java.io.IOException;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dexterind.gopigo.Gopigo;
import com.dexterind.gopigo.events.VoltageEvent;

public class VoltageTaskTimer extends TimerTask {

	private static final Logger logger = LoggerFactory.getLogger(VoltageTaskTimer.class);

	private final Gopigo gopigo;

	public VoltageTaskTimer(Gopigo gopigo) {
		this.gopigo = gopigo;
	}

	@Override
	public void run() {
		new Thread(new Runnable() {
			public void run() {
				double voltage;
				Boolean dispatchEvent = false;
				try {
					logger.info("Voltage check");

					voltage = gopigo.board.volt();
					if (voltage < gopigo.getMinVoltage()) {
						logger.debug("Low voltage detected");
						gopigo.free();
						dispatchEvent = true;
					} else if (voltage <= gopigo.getCriticalVoltage()) {
						logger.debug("Critical voltage detected. GoPiGo is now halted.");
						gopigo.halt();
						dispatchEvent = true;
					} else {
						gopigo.free();
					}

					if (dispatchEvent) {
						VoltageEvent voltageEvent = new VoltageEvent(gopigo, voltage);
						gopigo.fireEvent(voltageEvent);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
