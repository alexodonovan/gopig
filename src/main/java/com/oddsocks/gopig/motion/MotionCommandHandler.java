package com.oddsocks.gopig.motion;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oddsocks.dexterind.gopigo.Gopigo;
import com.oddsocks.gopig.messaging.ForwardCommand;
import com.oddsocks.gopig.messaging.StopCommand;

@Service
public class MotionCommandHandler {

	@Autowired
	private Gopigo gopigo;

	public void onFowrard(ForwardCommand command) throws IOException {
		gopigo.motion.forward(false);
	}

	public void onStop(StopCommand command) throws IOException {
		gopigo.motion.stop();
	}

}
