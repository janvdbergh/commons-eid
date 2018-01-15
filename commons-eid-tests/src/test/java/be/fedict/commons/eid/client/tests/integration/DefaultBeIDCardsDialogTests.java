/*
 * Commons eID Project.
 * Copyright (C) 2008-2013 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */

package be.fedict.commons.eid.client.tests.integration;

import be.fedict.commons.eid.client.BeIDCard;
import be.fedict.commons.eid.client.BeIDCards;
import be.fedict.commons.eid.client.FileType;
import be.fedict.commons.eid.client.spi.BeIDCardsUI;
import be.fedict.commons.eid.client.tests.integration.simulation.SimulatedCard;
import be.fedict.commons.eid.consumer.Identity;
import be.fedict.commons.eid.consumer.tlv.TlvParser;
import be.fedict.commons.eid.dialogs.DefaultBeIDCardsUI;
import org.junit.Before;
import org.junit.Test;

import javax.smartcardio.ATR;
import java.util.ArrayList;
import java.util.List;

public class DefaultBeIDCardsDialogTests {
	private static final int NUMBER_OF_CARDS = 3;

	private List<BeIDCard> simulatedBeIDCards;

	@Before
	public void setUp() {
		this.simulatedBeIDCards = new ArrayList<>(NUMBER_OF_CARDS);
		for (int i = 0; i < NUMBER_OF_CARDS; i++) {
			this.simulatedBeIDCards.add(new BeIDCard(new SimulatedCard(new ATR(new byte[]{
					0x3b, (byte) 0x98, (byte) i, 0x40, (byte) i, (byte) i, (byte) i, (byte) i, 0x01, 0x01, (byte) 0xad, 0x13, 0x10
			}))));
		}
	}

	@Test
	public void testCardSelection() throws Exception {
		BeIDCardsUI ui = new DefaultBeIDCardsUI();
		ui.selectBeIDCard(this.simulatedBeIDCards);
	}

	@Test
	public void testCardSelection2() throws Exception {
		BeIDCard card = new BeIDCards().getOneBeIDCard();
		byte[] identityData = card.readFile(FileType.Identity);
		Identity identity = TlvParser.parse(identityData, Identity.class);
		System.out.println("chose " + identity.getName() + ", " + identity.getFirstName() + " " + identity.getMiddleName());
	}
}