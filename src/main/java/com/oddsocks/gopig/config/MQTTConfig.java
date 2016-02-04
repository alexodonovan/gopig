package com.oddsocks.gopig.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQTTConfig {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String CLIENT_ID = "gopig";

	private static final String SERVER_URL = "tcp://192.168.1.10:1883";

	@Bean
	public MqttClient mqttClient() throws MqttException {
		logger.info("Creating MqttClient to {}: {}", SERVER_URL, CLIENT_ID);
		return new MqttClient(SERVER_URL, CLIENT_ID);
	}

}
