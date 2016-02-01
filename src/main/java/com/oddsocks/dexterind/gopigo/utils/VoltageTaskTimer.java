package com.oddsocks.dexterind.gopigo.utils;

import java.util.TimerTask;

import com.oddsocks.dexterind.gopigo.Gopigo;

//TODO use spring or other to avoid spawning new thread like this.
public class VoltageTaskTimer extends TimerTask {

	private final Gopigo gopigo;

	public VoltageTaskTimer(Gopigo gopigo) {
		this.gopigo = gopigo;
	}

	@Override
	public void run() {
		new Thread(newRunnable()).start();
	}

	private Runnable newRunnable() {
		return new VoltageTaskRunner(gopigo);
	}
}
