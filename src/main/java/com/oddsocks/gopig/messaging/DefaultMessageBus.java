package com.oddsocks.gopig.messaging;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;

@Service
public abstract class DefaultMessageBus implements MessageBus {

	private EventBus bus;

	@Autowired
	private SubscriberExceptionHandler exceptionHandler;

	@PostConstruct
	public void init() {
		bus = new EventBus(exceptionHandler);
	}

	@Override
	public void publish(Object obj) {
		checkNotNull(obj);
		bus.post(obj);
	}

	EventBus getBus() {
		return bus;
	}

	void setBus(EventBus bus) {
		this.bus = bus;
	}

	void setExceptionHandler(SubscriberExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

}
