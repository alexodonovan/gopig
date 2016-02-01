package com.oddsocks.gopig;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oddsocks.dexterind.gopigo.Gopigo;
import com.oddsocks.dexterind.gopigo.GopigoListener;
import com.oddsocks.dexterind.gopigo.events.StatusEvent;
import com.oddsocks.dexterind.gopigo.events.VoltageEvent;

@Service
public class GopigoCommandListener implements GopigoListener {

	@Autowired
	private Gopigo gopigo;

	@PostConstruct
	public void init() {
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
