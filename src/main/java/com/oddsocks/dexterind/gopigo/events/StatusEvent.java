/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  DexterIndustries
 * PROJECT       :  GoPiGo Java Library
 * FILENAME      :  StatusEvent.java
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
package com.oddsocks.dexterind.gopigo.events;

import java.util.EventObject;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

//TODO add a timestamp to the event.
public class StatusEvent extends EventObject {

	private static final long serialVersionUID = -2236533038040111378L;
	/**
	 * The status of the event. All the status are referenced into the Statuses
	 * static class.
	 */
	private int status;

	public StatusEvent(Object source) {
		super(source);
	}

	public StatusEvent(Object source, int status) {
		this(source);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof StatusEvent)) return false;

		StatusEvent rhs = (StatusEvent) obj;

		return new EqualsBuilder().append(status, rhs.status)
								  .isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(status)
									.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append(status)
																		  .toString();
	}

}