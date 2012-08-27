package test.integ.be.fedict.commons.eid.client;

import static org.junit.Assert.assertNotNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import be.fedict.commons.eid.client.BeIDCard;
import be.fedict.commons.eid.client.BeIDCards;
import be.fedict.commons.eid.client.FileType;
import be.fedict.commons.eid.consumer.Identity;
import be.fedict.commons.eid.consumer.tlv.TlvParser;

public class BeIDCardsExercise {
	private static final Log LOG = LogFactory.getLog(BeIDCardsExercise.class);

	@Test
	public void waitInsertAndRemove() throws Exception {
		LOG.debug("creating beIDCards Instance");
		BeIDCards beIDCards = new BeIDCards(new TestLogger());
		assertNotNull(beIDCards);

		LOG.debug("asking beIDCards Instance for One BeIDCard");
		BeIDCard beIDCard = beIDCards.getOneBeIDCard();
		assertNotNull(beIDCard);

		LOG.debug("reading identity file");
		byte[] identityFile = beIDCard.readFile(FileType.Identity);
		Identity identity = TlvParser.parse(identityFile, Identity.class);
		LOG.debug("card holder is " + identity.getFirstName() + " "
				+ identity.getName());

		LOG.debug("waiting for card removal");
		beIDCards.waitUntilCardRemoved(beIDCard);
		LOG.debug("card removed");

	}
}