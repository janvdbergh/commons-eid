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

package be.fedict.commons.eid.consumer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Special Organisation enumeration.
 * 
 * @author Frank Cornelis
 * 
 */
public enum SpecialOrganisation implements Serializable {

	UNSPECIFIED(null),
	SHAPE("1"),
	NATO("2"),
	FORMER_BLUE_CARD_HOLDER("4"),
	RESEARCHER("5"),
	UNKNOWN(null);

	private final String key;

	SpecialOrganisation(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	private static Map<String, SpecialOrganisation> specialOrganisations = getSpecialOrganisations();

	private static Map<String, SpecialOrganisation> getSpecialOrganisations() {
		Map<String, SpecialOrganisation> specialOrganisations = new HashMap<>();
		for (SpecialOrganisation specialOrganisation : SpecialOrganisation.values()) {
			String key = specialOrganisation.getKey();
			if (key != null) {
				if (specialOrganisations.containsKey(key)) {
					throw new RuntimeException(
							"duplicate key for special organisation type: "
									+ key);
				}
				specialOrganisations.put(key, specialOrganisation);
			}
		}

		return specialOrganisations;
	}

	public static SpecialOrganisation toSpecialOrganisation(String key) {
		if (null == key || key.isEmpty()) {
			return UNSPECIFIED;
		}

		SpecialOrganisation specialOrganisation = SpecialOrganisation.specialOrganisations.get(key);
		if (null == specialOrganisation) {
			return UNKNOWN;
		}

		return specialOrganisation;
	}
}