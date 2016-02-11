package com.oddsocks.gopig.messaging;

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

@Service
public class GopigMqttClient implements MqttCallback {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String COMMANDS_TOPIC = "gopig/commands";

	private static final String CLIENT_ID = MqttClient.generateClientId();

	private static final String SERVER_URL = "tcp://192.168.1.10:1883";

	@Autowired
	private JsonMapper jsonMapper;

	@Autowired
	private CommandBus commandBus;

	@PostConstruct
	public void init() throws MqttException {
		logger.info("Connecting to MqttClient on {}: {}", SERVER_URL, CLIENT_ID);
		MqttClient mqttClient = new MqttClient(SERVER_URL, CLIENT_ID);
		mqttClient.setCallback(this);
		mqttClient.connect();
		logger.info("Subscribing to MQTT broker topic {}", COMMANDS_TOPIC);
		mqttClient.subscribe(COMMANDS_TOPIC);
	}

	@Override
	public void connectionLost(Throwable throwable) {
		logger.info("ERROR: Lost Connection {}", throwable);
	}

	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		logger.info("Received {}: {}", topic, mqttMessage);
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

	public void setJsonMapper(JsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}

	public void setCommandBus(CommandBus commandBus) {
		this.commandBus = commandBus;
	}

}
