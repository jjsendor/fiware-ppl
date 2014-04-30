/*******************************************************************************
 * Copyright (c) 2013, SAP AG
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
package com.sap.a4cloud.apple.obligation.dao;

import java.util.List;

import javax.persistence.Query;

import com.sap.research.primelife.dao.DaoImpl;
import com.sap.a4cloud.apple.obligation.entity.ObligationTrigger;

/**
 * Extends generic DAO with {@link ObligationTrigger} entity related methods.
 *
 *
 */

public class ObligationTriggerDao extends DaoImpl<ObligationTrigger> {

	/**
	 * Looks for the list of {@link ObligationTrigger} objects associated with
	 * a PII and the trigger type.
	 *
	 * @param piiId			the PII id
	 * @param triggerName	the trigger name
	 *
	 * @return	list of {@link ObligationTrigger} associated with the given
	 * 			PII id and the trigger type
	 */
	@SuppressWarnings("unchecked")
	public List<ObligationTrigger> findByPiiIdAndTriggerName(Long piiId,
			String triggerName) {
		Query query = em.createQuery("SELECT ot FROM "
					+ ObligationTrigger.class.getName()
					+ " ot WHERE ot.piiId = :piiId"
					+ " and ot.triggerName= :triggerName");
		query.setParameter("piiId", piiId);
		query.setParameter("triggerName", triggerName);

		if (query.getResultList().isEmpty()) {
			return null;
		}

		return (List<ObligationTrigger>) query.getResultList();
	}

	/**
	 * Looks for {@link ObligationTrigger} associated with a PII identified by its id.
	 *
	 * @param piiId	the PII id
	 *
	 * @return	the list of the {@link ObligationTrigger} associated with the given
	 * 			PII id
	 */
	@SuppressWarnings("unchecked")
	public List<ObligationTrigger> findByPiiId(Long piiId) {
		Query query = em.createQuery("SELECT ot FROM "
					+ ObligationTrigger.class.getName()
					+ " ot WHERE ot.piiId = :piiId");
		query.setParameter("piiId", piiId);

		return (List<ObligationTrigger>) query.getResultList();
	}

}
