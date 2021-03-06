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

package be.bosa.commons.eid.jca;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * eID specific {@link X509ExtendedKeyManager}.
 * 
 * @see BeIDKeyManagerFactory
 * @author Frank Cornelis
 * 
 */
public class BeIDX509KeyManager extends X509ExtendedKeyManager {

	private static final Log LOG = LogFactory.getLog(BeIDX509KeyManager.class);

	private KeyStore keyStore;

	public BeIDX509KeyManager() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		this(null);
	}

	public BeIDX509KeyManager(BeIDManagerFactoryParameters beIDSpec) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		LOG.debug("constructor");
		this.keyStore = KeyStore.getInstance("BeID");
		BeIDKeyStoreParameter beIDKeyStoreParameter;
		if (null == beIDSpec) {
			beIDKeyStoreParameter = null;
		} else {
			beIDKeyStoreParameter = new BeIDKeyStoreParameter();
			beIDKeyStoreParameter.setLocale(beIDSpec.getLocale());
			beIDKeyStoreParameter.setParentComponent(beIDSpec.getParentComponent());
			beIDKeyStoreParameter.setAutoRecovery(beIDSpec.getAutoRecovery());
			beIDKeyStoreParameter.setCardReaderStickiness(beIDSpec.getCardReaderStickiness());
		}
		this.keyStore.load(beIDKeyStoreParameter);
	}

	@Override
	public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
		LOG.debug("chooseClientAlias");
		for (String keyType : keyTypes) {
			LOG.debug("key type: " + keyType);
			if ("RSA".equals(keyType)) {
				return "beid";
			}
		}
		return null;
	}

	@Override
	public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
		LOG.debug("chooseServerAlias");
		return null;
	}

	@Override
	public X509Certificate[] getCertificateChain(String alias) {
		LOG.debug("getCertificateChain: " + alias);
		if ("beid".equals(alias)) {
			Certificate[] certificateChain;
			try {
				certificateChain = keyStore.getCertificateChain("Authentication");
			} catch (KeyStoreException e) {
				LOG.error("BeID keystore error: " + e.getMessage(), e);
				return null;
			}
			X509Certificate[] x509CertificateChain = new X509Certificate[certificateChain.length];
			for (int idx = 0; idx < certificateChain.length; idx++) {
				x509CertificateChain[idx] = (X509Certificate) certificateChain[idx];
			}
			return x509CertificateChain;
		}
		return null;
	}

	@Override
	public String[] getClientAliases(String keyType, Principal[] issuers) {
		LOG.debug("getClientAliases");
		return null;
	}

	@Override
	public PrivateKey getPrivateKey(String alias) {
		LOG.debug("getPrivateKey: " + alias);
		if ("beid".equals(alias)) {
			PrivateKey privateKey;
			try {
				privateKey = (PrivateKey) keyStore.getKey("Authentication", null);
			} catch (Exception e) {
				LOG.error("getKey error: " + e.getMessage(), e);
				return null;
			}
			return privateKey;
		}
		return null;
	}

	@Override
	public String[] getServerAliases(String keyType, Principal[] issuers) {
		LOG.debug("getServerAliases");
		return null;
	}

	@Override
	public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine) {
		LOG.debug("chooseEngineClientAlias");
		return super.chooseEngineClientAlias(keyType, issuers, engine);
	}

	@Override
	public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine) {
		LOG.debug("chooseEngineServerAlias");
		return super.chooseEngineServerAlias(keyType, issuers, engine);
	}
}
