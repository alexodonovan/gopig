package com.oddsocks.gopig;

import java.io.IOException;

import com.dexterind.gopigo.Gopigo;
import com.dexterind.gopigo.GopigoListener;
import com.dexterind.gopigo.events.StatusEvent;
import com.dexterind.gopigo.events.VoltageEvent;

public class GopigoCommandListener implements GopigoListener {

	private Gopigo gopigo;

	public GopigoCommandListener() throws IOException, InterruptedException {
		gopigo = new Gopigo();
		gopigo.postContruct();
		gopigo.addListener(this);
		gopigo.setMinVoltage(5.5);
		gopigo.init();
	}

	@Override
	public void onStatusEvent(StatusEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVoltageEvent(VoltageEvent event) {
		// TODO Auto-generated method stub

	}

}
