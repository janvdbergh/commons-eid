/*
 * Commons eID Project.
 * Copyright (C) 2013 Frank Cornelis.
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

package be.bosa.commons.eid.client.tests.integration;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.junit.Test;

import be.bosa.commons.eid.jca.BeIDProvider;

public class CMSTest {

	@Test
	public void testCMSSignature() throws Exception {
		Security.addProvider(new BeIDProvider());
		Security.addProvider(new BouncyCastleProvider());

		KeyStore keyStore = KeyStore.getInstance("BeID");
		keyStore.load(null);

		PrivateKey privateKey = (PrivateKey) keyStore.getKey("Authentication", null);
		X509Certificate certificate = (X509Certificate) keyStore.getCertificate("Authentication");

		CMSTypedData msg = new CMSProcessableByteArray("Hello world!".getBytes());
		CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
		ContentSigner sha256Signer = new JcaContentSignerBuilder("SHA256withRSA").build(privateKey);

		gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider(BouncyCastleProvider.PROVIDER_NAME).build())
				.build(sha256Signer, certificate));

		CMSSignedData sigData = gen.generate(msg, false);
		System.out.println(sigData);
	}
}
