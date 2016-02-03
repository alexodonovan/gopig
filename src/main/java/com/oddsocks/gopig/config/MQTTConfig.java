package com.oddsocks.gopig.config;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQTTConfig {

	private static final String CLIENT_ID = "gopig";

	private static final String SERVER_URL = "tcp://192.168.1.10:1883";

	@Autowired
	private MqttCallback mqttCallback;

	@Bean
	public MqttClient mqttClient() throws MqttException {
		MqttClient mqttClient = new MqttClient(SERVER_URL, CLIENT_ID);
		mqttClient.setCallback(mqttCallback);
		mqttClient.connect();
		return mqttClient;
	}

}
