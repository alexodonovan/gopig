package com.oddsocks.gopig.messaging;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionHandler;

@Service
public class CommandBus implements MessageBus {

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

	@Override
	public void register(Object handler) {
		bus.register(handler);
	}

	protected EventBus getBus() {
		return bus;
	}

	protected void setBus(EventBus bus) {
		this.bus = bus;
	}

	protected void setExceptionHandler(SubscriberExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

}
