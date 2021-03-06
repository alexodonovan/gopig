/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  DexterIndustries
 * PROJECT       :  GoPiGo Java Library
 * FILENAME      :  Gopigo.java
 * AUTHOR        :  Marcello Barile <marcello.barile@gmail.com>
 *
 * This file is part of the GoPiGo Java Library project. More information about
 * this project can be found here:  https://github.com/DexterInd/GoPiGo
 * **********************************************************************
 * %%
 * GoPiGo for the Raspberry Pi: an open source robotics platform for the Raspberry Pi.
 * Copyright (C) 2015  Dexter Industries

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/gpl-3.0.txt>.
 *
 * #L%
 */
package com.oddsocks.dexterind.gopigo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.EventObject;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.oddsocks.dexterind.gopigo.behaviours.Motion;
import com.oddsocks.dexterind.gopigo.components.Board;
import com.oddsocks.dexterind.gopigo.components.BoardFactory;
import com.oddsocks.dexterind.gopigo.components.BusFactory;
import com.oddsocks.dexterind.gopigo.components.Encoders;
import com.oddsocks.dexterind.gopigo.components.Led;
import com.oddsocks.dexterind.gopigo.components.Motor;
import com.oddsocks.dexterind.gopigo.events.StatusEvent;
import com.oddsocks.dexterind.gopigo.events.VoltageEvent;
import com.oddsocks.dexterind.gopigo.utils.Statuses;
import com.oddsocks.dexterind.gopigo.utils.VoltageTaskTimer;

/**
 * The main class for the Gopigo. It instanciates all the components and
 * behaviours and is in charge to handle the events.
 * 
 * @author marcello
 * 
 */
@Service
public class Gopigo {

	private static final Logger logger = LoggerFactory.getLogger(Gopigo.class);

	/**
	 * The flag for the initialized status.
	 */
	private boolean isInit = false;
	/**
	 * The flag for the halted status.
	 */
	private boolean isHalt = false;
	/**
	 * The timer which will be in charge to check the voltage value.
	 */
	private Timer voltageTimer = new Timer();
	/**
	 * The time in milliseconds between successive task executions.
	 */
	private int voltageTimerTime = 60000;
	/**
	 * The minimum voltage value. If the voltage drops under this value a
	 * VoltageEvent will be fired.
	 */
	private double minVoltage = 5.5;
	/**
	 * The critical voltage value. If the voltage drops under this value a
	 * VoltageEvent will be fired and the GoPiGo will be flagged as "halted".
	 */
	private double criticalVoltage = 1.0;

	/**
	 * The main object which handles the methods to get access to the resources
	 * connected to the board.
	 */
	private Board board;
	/**
	 * The encoders.
	 */
	public Encoders encoders;
	/**
	 * The left led.
	 */
	public Led ledLeft;
	/**
	 * The right led.
	 */
	public Led ledRight;
	/**
	 * The left motor.
	 */
	public Motor motorLeft;
	/**
	 * The right motor.
	 */
	public Motor motorRight;
	/**
	 * The motion behaviour object.
	 */
	public Motion motion;
	/**
	 * The list of the listeners which are listening for some event.
	 */
	private CopyOnWriteArrayList<GopigoListener> listeners = new CopyOnWriteArrayList<GopigoListener>();

	private VoltageTaskTimer voltageTaskTimer = new VoltageTaskTimer(this);

	private BusFactory busFactory = new BusFactory();;
	private BoardFactory boardFactory = new BoardFactory(this);

	@PostConstruct
	public void postContruct() {
		logger.info("com.dexterind.gopigo.Gopigo");
		logger.trace("Instancing a new Gopigo!");

		try {
			board = boardFactory.createBoard(busFactory.createBus());
			encoders = new Encoders(board);
			ledLeft = new Led(Led.LEFT, board);
			ledRight = new Led(Led.RIGHT, board);
			motorLeft = new Motor(Motor.LEFT, board);
			motorRight = new Motor(Motor.RIGHT, board);
			motion = new Motion(board);
		} catch (IOException | InterruptedException e) {
			logger.error("Error instantiating Gopigo {}", e);
			// TODO is there a better exception here
			throw new StartupException("Error starting gopigo...", e);
		}
	}

	/**
	 * Initializes the Gopigo and fires an event once the init is done.
	 */
	public void init() {
		logger.trace("Init " + isInit);

		board.init();

		if (!isHalt) {
			isInit = true;
			initVoltageCheck();
			fireEvent(Statuses.INIT);
		}
	}

	/**
	 * Resets the Gopigo.
	 * 
	 * @throws IOException
	 */
	public void reset() throws IOException {
		logger.info("Reset");
		ledLeft.off();
		ledRight.off();
		motion.setSpeed(255);
		free();
	}

	/**
	 * Adds a <code>GopigoListener</code> to the listeners list.
	 * 
	 * @param listener
	 *            The <code>GopigoListener</code> to register.
	 */
	public void addListener(GopigoListener listener) {
		logger.info("Adding listener {}", listener);
		listeners.addIfAbsent(listener);
	}

	/**
	 * Removes a <code>GopigoListener</code> from the listeners list.
	 * 
	 * @param listener
	 *            The <code>GopigoListener</code> to remove.
	 */
	public void removeListener(GopigoListener listener) {
		if (!hasListener(listener)) return;
		logger.info("Removing listener {}", listener);
		listeners.remove(listener);
	}

	/**
	 * Fires an event to the listeners.
	 * 
	 * @param event
	 *            The event to fire.
	 */
	public void fireEvent(EventObject event) {
		int i = 0;
		logger.info("Firing event [" + listeners.toArray().length + " listeners]");

		for (GopigoListener listener : listeners) {
			logger.info("listener[{}] {}", i, event.getClass()
												   .toString());

			if (event instanceof StatusEvent) {
				listener.onStatusEvent((StatusEvent) event);
			} else if (event instanceof VoltageEvent) {
				listener.onVoltageEvent((VoltageEvent) event);
			}
			i++;
		}
	}

	private void fireEvent(int status) {
		fireEvent(new StatusEvent(this, status));
	}

	/**
	 * Initializes a timer which will checks the voltage and will fires and
	 * event in case of low voltage.
	 */
	private void initVoltageCheck() {
		voltageTimer.scheduleAtFixedRate(voltageTaskTimer, 0, voltageTimerTime);
	}

	/**
	 * Sets the GoPiGo free. It removes the "halted" flag if necessary.
	 */
	public void free() {
		if (isHalt) {
			logger.info("The GoPigo was halted. I'm setting it free.");
			isHalt = false;
		}
	}

	/**
	 * It returns the operativity status.
	 * 
	 * @return True if operative, False if not.
	 */
	public Boolean isOperative() {
		return !isHalt;
	}

	/**
	 * Halts the GoPiGo if necessary.
	 */
	public void halt() {
		if (!isHalt) {
			logger.warn("The GoPigo was free, now it's halted.");
			isHalt = true;
		}
	}

	/**
	 * It returns the halt status.
	 * 
	 * @return True if halted, False if not.
	 */
	public Boolean isHalt() {
		return isHalt;
	}

	/**
	 * Fires a "HALT" <code>StatusEvent</code>
	 */
	public void onHalt() {
		logger.warn("The GoPigo seems to be halted, it will be probably difficult to execute the commands.");
		fireEvent(Statuses.HALT);
	}

	/**
	 * Sets the min. voltage value.
	 * 
	 * @param value
	 *            The minimum voltage value to set.
	 */
	public void setMinVoltage(double value) {
		logger.info("Setting minVoltage to " + Double.toString(value));
		minVoltage = value;
	}

	/**
	 * Returns the min. voltage value.
	 * 
	 * @return The min. voltage value.
	 */
	public Double getMinVoltage() {
		logger.info("Getting minVoltage");
		return minVoltage;
	}

	/**
	 * Sets the critical voltage value. Under this value the GoPiGo will be
	 * flagged as "halted".
	 * 
	 * @param value
	 *            The critical voltage value to set.
	 */
	public void setCriticalVoltage(double value) {
		logger.info("Setting criticalVoltage to " + Double.toString(value));
		criticalVoltage = value;
	}

	/**
	 * Returns the critical voltage value.
	 * 
	 * @return The Critical voltage value.
	 */
	public Double getCriticalVoltage() {
		logger.info("Getting criticalVoltage");
		return criticalVoltage;
	}

	public void right90() throws IOException {
		encoders.targeting(1, 1, 18);
		motion.rightWithRotation();
	}

	public void setLedLeft(Led ledLeft) {
		this.ledLeft = ledLeft;
	}

	public void setLedRight(Led ledRight) {
		this.ledRight = ledRight;
	}

	public void setMotion(Motion motion) {
		this.motion = motion;
	}

	public boolean hasListener(GopigoListener candidate) {
		if (listeners.isEmpty()) return false;
		checkNotNull(candidate);
		return listeners.contains(candidate);
	}

	public void setVoltageTaskTimer(VoltageTaskTimer voltageTaskTimer) {
		this.voltageTaskTimer = voltageTaskTimer;
	}

	public void setVoltageTimer(Timer voltageTimer) {
		this.voltageTimer = voltageTimer;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setEncoders(Encoders encoders) {
		this.encoders = encoders;
	}

	public double boardVoltage() throws IOException {
		return board.volt();
	}

	public float boardVersion() throws IOException {
		return board.version();
	}

	public int boardRevision() throws IOException {
		return board.revision();
	}

	public boolean isLowBoardVoltage() throws IOException {
		return boardVoltage() < getMinVoltage();
	}

	public boolean isCriticallyLowVoltage() throws IOException {
		return boardVoltage() <= getCriticalVoltage();
	}

	public void setBusFactory(BusFactory busFactory) {
		this.busFactory = busFactory;
	}

	public void setBoardFactory(BoardFactory boardFactory) {
		this.boardFactory = boardFactory;
	}

	public Board getBoard() {
		return board;
	}

	public Encoders getEncoders() {
		return encoders;
	}

	public Led getLedLeft() {
		return ledLeft;
	}

	public Led getLedRight() {
		return ledRight;
	}

	public Motor getMotorLeft() {
		return motorLeft;
	}

	public Motor getMotorRight() {
		return motorRight;
	}

	public Motion getMotion() {
		return motion;
	}

}