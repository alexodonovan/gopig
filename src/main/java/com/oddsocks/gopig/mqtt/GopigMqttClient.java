package com.oddsocks.gopig.mqtt;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oddsocks.gopig.messaging.Command;
import com.oddsocks.gopig.messaging.CommandBus;
import com.oddsocks.gopig.messaging.JsonMapper;

@Service
public class GopigMqttClient implements MqttCallback {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String COMMANDS_TOPIC = "gopig/commands";

	@Autowired
	private MqttClient mqttClient;

	@Autowired
	private JsonMapper jsonMapper;

	@Autowired
	private CommandBus commandBus;

	@PostConstruct
	public void init() throws MqttException {
		logger.info("Connecting to MQTT broker...");
		mqttClient.subscribe(COMMANDS_TOPIC);
	}

	@Override
	public void connectionLost(Throwable throwable) {
		logger.info("ERROR: Lost Connection {}", throwable);
	}

	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		Command command = jsonMapper.fromJson(mqttMessage.getPayload(), Command.class);
		logger.info("Topic {}: Payload {}: QoS {}, Retrained {}", topic, command, mqttMessage.getQos(),
				mqttMessage.isRetained());
		logger.info("Publishing command {}", command);
		commandBus.publish(command);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		logger.info("Delivery complete: {}", token);
	}

}
