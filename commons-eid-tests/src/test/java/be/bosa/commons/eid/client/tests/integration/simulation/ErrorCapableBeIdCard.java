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

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import java.util.Random;

public class ErrorCapableBeIdCard extends SimulatedBeIDCard {

	protected static final ResponseAPDU WAITWAITWAIT = new ResponseAPDU(new byte[]{0x6c, (byte) 0x00});
	protected static final ResponseAPDU WHO_AM_I = new ResponseAPDU(new byte[]{(byte) 0x6d, (byte) 0x00});

	private boolean nextTooFast;
	private boolean nextBitError;
	private boolean nextRandomResponse;
	private boolean nextCardException;
	private boolean nextConfused;
	private int delay;
	private final Random random;

	public ErrorCapableBeIdCard(String profile) {
		this(profile, System.currentTimeMillis());
	}

	public ErrorCapableBeIdCard(String profile, long seed) {
		super(profile);
		this.random = new Random(seed);
		this.delay = 0;
	}

	public ErrorCapableBeIdCard introduceTooFastError() {
		this.nextTooFast = true;
		return this;
	}

	public ErrorCapableBeIdCard introduceBitError() {
		this.nextBitError = true;
		return this;
	}

	public ErrorCapableBeIdCard introduceRandomResponse() {
		this.nextRandomResponse = true;
		return this;
	}

	public ErrorCapableBeIdCard introduceCardException() {
		this.nextCardException = true;
		return this;
	}

	public ErrorCapableBeIdCard introduceConfusion() {
		this.nextConfused = true;
		return this;
	}

	public int getDelay() {
		return this.delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	@Override
	protected ResponseAPDU transmit(CommandAPDU apdu)
			throws CardException {
		if (this.nextConfused) {
			this.nextConfused = false;
			try {
				Thread.sleep(40000);
			} catch (InterruptedException iex) {
				throw new RuntimeException(iex);
			}
			return WHO_AM_I;
		}

		if (this.nextCardException) {
			this.nextCardException = false;
			throw new CardException("Fake CardException Introduced By " + this.getClass().getName());
		}

		if (this.nextTooFast) {
			this.nextTooFast = false;
			return WAITWAITWAIT;
		}

		if (this.nextRandomResponse) {
			this.nextRandomResponse = false;
			byte[] randomAPDU = new byte[16];
			this.random.nextBytes(randomAPDU);
			return new ResponseAPDU(randomAPDU);
		}

		if (this.delay > 0) {
			try {
				Thread.sleep(this.delay);
			} catch (InterruptedException iex) {
				throw new RuntimeException(iex);
			}
		}

		if (this.nextBitError) {
			ResponseAPDU response = super.transmit(apdu);
			byte[] responseBytes = response.getBytes();

			// flip some bits
			responseBytes[this.random.nextInt(responseBytes.length)] ^= (byte) this.random.nextInt();

			return new ResponseAPDU(responseBytes);
		}

		return super.transmit(apdu);
	}
}
