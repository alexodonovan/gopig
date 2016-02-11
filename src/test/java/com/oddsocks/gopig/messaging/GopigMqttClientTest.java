package com.oddsocks.gopig.messaging;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GopigMqttClientTest {

	private GopigMqttClient sut;

	@Mock
	private MqttMessage mqttMessage;

	@Mock
	private JsonMapper jsonMapper;

	@Mock
	private Command command;

	@Mock
	private CommandBus commandBus;

	private String topic;

	@Before
	public void setUp() {
		sut = new GopigMqttClient();
		sut.setJsonMapper(jsonMapper);
		sut.setCommandBus(commandBus);
		topic = "testTopic";
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testConnectionLost() throws Exception {
		when(jsonMapper.fromJson(Mockito.any(byte[].class), Mockito.any(Class.class))).thenReturn(command);
		sut.messageArrived(topic, mqttMessage);
		
		verify(commandBus).publish(command);
	}

}
