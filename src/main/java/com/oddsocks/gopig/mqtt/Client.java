package com.oddsocks.gopig.mqtt;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Client {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	private MqttClient mqttClient;
	
	@Autowired
	private MqttCallback mqttCallback;

	@PostConstruct
	public void init() throws MqttException{
		logger.info("Connecting to MQTT broker...");
		mqttClient = new MqttClient("tcp://192.168.1.10:1883", "gopig");
		mqttClient.setCallback(mqttCallback);
		mqttClient.connect();
		mqttClient.subscribe("my/topic");
	}

}
