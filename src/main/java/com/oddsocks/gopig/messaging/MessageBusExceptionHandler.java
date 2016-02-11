package com.oddsocks.gopig.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

@Service
public class MessageBusExceptionHandler implements SubscriberExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void handleException(Throwable th, SubscriberExceptionContext context) {
		logger.error("Error in subscriber {}: {}: {}: {}", th, context.getSubscriber(), context.getSubscriberMethod(), context.getEvent());
	}

	protected void setLogger(Logger logger) {
		this.logger = logger;
	}

}
