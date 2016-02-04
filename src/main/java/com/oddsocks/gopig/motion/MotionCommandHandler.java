package com.oddsocks.gopig.motion;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.eventbus.Subscribe;
import com.oddsocks.dexterind.gopigo.Gopigo;
import com.oddsocks.gopig.messaging.CommandBus;
import com.oddsocks.gopig.messaging.ForwardCommand;
import com.oddsocks.gopig.messaging.StopCommand;

@Service
public class MotionCommandHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Gopigo gopigo;

	@Autowired
	public CommandBus commandBus;

	@PostConstruct
	public void init() {
		logger.info("Regstering command handler {}", this);
		commandBus.register(this);
	}

	@Subscribe
	public void onFowrard(ForwardCommand command) throws IOException {
		logger.info("Handling forward command {}", command);
		gopigo.motion.forward(false);
	}

	@Subscribe
	public void onStop(StopCommand command) throws IOException {
		logger.info("Handling stop command {}", command);
		gopigo.motion.stop();
	}

}
