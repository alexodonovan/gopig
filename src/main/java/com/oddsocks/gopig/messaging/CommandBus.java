package com.oddsocks.gopig.messaging;

import static com.google.common.base.Preconditions.checkArgument;

public class CommandBus extends DefaultMessageBus {

	@Override
	public void publish(Object obj) {
		checkArgument(Command.class.isAssignableFrom(obj.getClass()));
		super.publish(obj);
	}

}
