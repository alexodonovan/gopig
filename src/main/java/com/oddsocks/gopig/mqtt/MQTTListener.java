package com.oddsocks.gopig.mqtt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oddsocks.dexterind.gopigo.Gopigo;

@Service
public class MQTTListener implements MqttCallback {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private Gopigo gopigo;

	@Override
	public void connectionLost(Throwable throwable) {
		logger.info("ERROR: Lost Connection.");
	}

	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		logger.info("Topic: " + topic);
		String payload = new String(mqttMessage.getPayload());
		logger.info(payload);
		logger.info("QoS: " + mqttMessage.getQos());
		logger.info("Retained: " + mqttMessage.isRetained());

		if (payload.equals("forward")) {
			gopigo.motion.forward(false);
		}
		if (payload.equals("stop")) {
			gopigo.motion.stop();
		}

	}

	@Override
	public void deliveryComplete(final IMqttDeliveryToken iMqttDeliveryToken) {
		// When message delivery was complete
	}

}
