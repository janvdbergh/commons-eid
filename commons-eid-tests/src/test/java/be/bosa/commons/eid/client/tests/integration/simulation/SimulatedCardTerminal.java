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

package be.bosa.commons.eid.client.tests.integration.simulation;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

public class SimulatedCardTerminal extends CardTerminal {

	private final String name;
	private SimulatedCard card;
	private SimulatedCardTerminals terminals;

	public SimulatedCardTerminal(String name) {
		super();
		this.name = name;
	}

	public synchronized void insertCard(SimulatedCard card) {
		if (this.card != null) {
			throw new RuntimeException("Can't Insert 2 Cards in one Card Reader");
		}
		this.card = card;
		notifyAll();
		if (this.terminals != null) {
			this.terminals.propagateCardEvent();
		}
	}

	public synchronized void removeCard() {
		if (this.card == null) {
			throw new RuntimeException("Can't Remove Card From Empty Reader");
		}
		this.card = null;
		notifyAll();
		if (this.terminals != null) {
			this.terminals.propagateCardEvent();
		}
	}

	@Override
	public Card connect(String protocol) throws CardException {
		if (!isCardPresent()) {
			throw new CardException("No Card Present");
		}
		return this.card;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isCardPresent() {
		return this.card != null;
	}

	@Override
	public synchronized boolean waitForCardAbsent(long timeout) {
		return waitForCardState(false, timeout);
	}

	@Override
	public synchronized boolean waitForCardPresent(long timeout) {
		return waitForCardState(true, timeout);
	}

	private synchronized boolean waitForCardState(boolean state, long timeout) {
		if (this.isCardPresent() == state) {
			return true;
		}

		try {
			wait(timeout);
		} catch (InterruptedException iex) {
			return false;
		}
		return true;
	}

	public void setTerminals(SimulatedCardTerminals terminals) {
		this.terminals = terminals;
	}
}
