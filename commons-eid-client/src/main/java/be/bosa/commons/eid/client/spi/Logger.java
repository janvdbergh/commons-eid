/*
 * Commons eID Project.
 * Copyright (C) 2014 - 2018 BOSA.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License version 3.0 as published by
 * the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, see https://www.gnu.org/licenses/.
 */

package be.bosa.commons.eid.client.spi;

/**
 * Logger-interface.
 *
 * @author Frank Cornelis
 */
public interface Logger {

	/**
	 * Error messages receiver.
	 *
	 * @param message the error message.
	 */
	void error(String message);

	/**
	 * Debug messages receiver.
	 *
	 * @param message the debug message.
	 */
	void debug(String message);
}
