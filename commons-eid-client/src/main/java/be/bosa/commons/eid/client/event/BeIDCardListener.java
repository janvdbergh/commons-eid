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

package be.bosa.commons.eid.client.event;

import be.bosa.commons.eid.client.FileType;

/**
 * Listener interface for various eID related events like read progress. To be
 * used in user interfaces.
 *
 * @author Frank Cornelis
 */
public interface BeIDCardListener {

	void notifyReadProgress(FileType fileType, int offset, int estimatedMaxSize);

	void notifySigningBegin(FileType fileType);

	void notifySigningEnd(FileType fileType);
}
