package com.oddsocks.gopig.messaging;

public interface MessageBus {

	public void publish(Object obj);

	void register(Object handler);

}
