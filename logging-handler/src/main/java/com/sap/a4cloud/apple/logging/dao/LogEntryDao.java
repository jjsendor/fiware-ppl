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
package com.sap.a4cloud.apple.logging.dao;

import java.util.List;

import javax.persistence.Query;

import com.sap.a4cloud.apple.logging.entity.LogEntry;
import com.sap.research.primelife.dao.DaoImpl;

public class LogEntryDao extends DaoImpl<LogEntry> {

	/**
	 * Returns the log entries associated with the PII identified by the given
	 * PII attribute name and PII attribute value.
	 *
	 * @param attributeName		the PII attribute name
	 * @param attributeValue	the PII attribute value
	 *
	 * @return	the list of associated log entries
	 */
	@SuppressWarnings("unchecked")
	public List<LogEntry> find(String attributeName, String attributeValue) {
		Query query = em.createQuery("SELECT logEntry FROM "
				+ LogEntry.class.getName()
				+ " logEntry WHERE logEntry.piiAttributeName = :attributeName"
				+ " AND logEntry.piiAttributeValue = :attributeValue"
				+ " ORDER BY logEntry.date ASC");

		query.setParameter("attributeName", attributeName);
		query.setParameter("attributeValue", attributeValue);

		List<LogEntry> results = query.getResultList();

		return results;
	}

	/**
	 * Returns the log entries associated with the given PII.
	 *
	 * @param owner	the PII owner
	 *
	 * @return	the list of associated log entries
	 */
	@SuppressWarnings("unchecked")
	public List<LogEntry> findByOwner(String owner) {
		Query query = em.createQuery("SELECT logEntry FROM "
				+ LogEntry.class.getSimpleName()
				+ " logEntry WHERE logEntry.piiOwner = :owner"
				+ " ORDER BY logEntry.date ASC");

		query.setParameter("owner", owner);

		List<LogEntry> results = query.getResultList();

		return results;
	}

	public void refresh(LogEntry piih) {
		em.refresh(piih);
	}

}
