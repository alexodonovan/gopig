package com.oddsocks.gopig.messaging;

public class JacksonSerializationException extends RuntimeException {

	private static final long serialVersionUID = -4296987955725064234L;

	public JacksonSerializationException(String msg, Throwable th) {
		super(msg, th);
	}

}
