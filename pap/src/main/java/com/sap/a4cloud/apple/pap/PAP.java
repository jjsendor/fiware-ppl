/*******************************************************************************
 * Copyright (c) 2014, SAP AG
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     - Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *     - Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *     - Neither the name of the SAP AG nor the names of its contributors may
 *      be used to endorse or promote products derived from this software
 *      without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.sap.a4cloud.apple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.dao.PiiDao;

import eu.primelife.ppl.pii.impl.PIIType;

/**
 * Policy Administration Point.
 * Provides methods for accessing PII store.
 *
 */
public class PAP {

	private static final Logger LOGGER = LoggerFactory.getLogger(PAP.class);

	private PiiDao piiDao = new PiiDao();

	/**
	 * Stores PII and attached policy. Configures Obligation Handler.
	 *
	 * @param pii	PII containing attribute name, value, owner and policy
	 */
	public void storePii(PIIType pii) {
		LOGGER.info("Storing PII " + pii.getAttributeName()
				+ " with value " + pii.getAttributeValue()
				+ " owned by " + pii.getOwner());
		// persist PII
		piiDao.persistObject(pii);

		// TODO add obligations

		// TODO log information about storing PII
	}

	/**
	 * Deletes PII from the repository.
	 *
	 * @param piiId	the PII id
	 */
	public void deletePii(Long piiId) {
		LOGGER.info("Deleting with id {}", piiId);
		PIIType pii = piiDao.findObject(PIIType.class, piiId);

		if (pii != null) {
			LOGGER.info("Deleting PII " + pii.getAttributeName()
					+ " with value " + pii.getAttributeValue()
					+ " owned by " + pii.getOwner());
			// TODO create and trigger event

			// TODO log information about deleting PII

			piiDao.deleteObject(pii);
		}
	}

}
