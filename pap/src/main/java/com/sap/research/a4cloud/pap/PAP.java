package com.sap.research.a4cloud.pap;

import com.sap.research.primelife.dao.PiiDao;

import eu.primelife.ppl.pii.impl.PIIType;

/**
 * Policy Administration Point.
 * Provides method for storing PII.
 *
 */
public class PAP {

	private PiiDao piiDao = new PiiDao();

	/**
	 * Stores PII and attached policy. Configures Obligation Handler.
	 *
	 * @param pii	PII containing attribute name, value, owner and policy
	 */
	public void storePii(PIIType pii) {

		piiDao.persistObject(pii);

		// TODO set obligations

		// TODO log information about storing PII
	}

}
