package com.oddsocks.gopig;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oddsocks.dexterind.gopigo.Gopigo;
import com.oddsocks.dexterind.gopigo.GopigoListener;
import com.oddsocks.dexterind.gopigo.events.StatusEvent;
import com.oddsocks.dexterind.gopigo.events.VoltageEvent;

@Service
public class GopigoCommandListener implements GopigoListener {

	private Logger logger = LoggerFactory.getLogger(getClass());

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
		logger.info("Status Event: {}", event);
	}

	@Override
	public void onVoltageEvent(VoltageEvent event) {
		logger.info("Voltage Event: {}", event);
	}

	public void setGopigo(Gopigo gopigo) {
		this.gopigo = gopigo;
	}

	protected void setLogger(Logger logger) {
		this.logger = logger;
	}

}
