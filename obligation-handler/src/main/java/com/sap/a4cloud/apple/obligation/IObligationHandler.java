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
package com.sap.a4cloud.apple.obligation;

import java.util.List;

import com.sap.a4cloud.apple.obligation.entity.ObligationTrigger;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.obligation.impl.ObligationsSet;

/**
 * Obligation Handler interface for the PPL obligations.
 * It provides method to manage the Obligation Handler instance, e.g.
 * adding obligations or removing obligations associate with a specific PII.
 *
 * @author Jakub Sendor
 *
 */
public interface IObligationHandler {

	/**
	 * Configures the Obligation Handler with the new obligations for a given
	 * PII.
	 *
	 * @param obligationSet	the obligation set
	 * @param pii			the PII with which the obligation set is associated
	 */
	public void addObligations(ObligationsSet obligationSet, PIIType pii);

	/**
	 * Retrieves all of the {@link ObligationTrigger} objects associated with
	 * a given PII and trigger.
	 *
	 * @param piiId			the PII id
	 * @param triggerName	the trigger name, as in PPL schema
	 * @return	list of the {@link ObligationTrigger} objects
	 */
	public List<ObligationTrigger> getObligations(Long piiId,
			String triggerName);

	/**
	 * Removes obligations associated with a given PII.
	 * That method should be used when PII is deleted so that Obligation
	 * Handler also removes associate obligations.
	 *
	 * @param pii	the PII which associated obligations will be removed
	 */
	public void removeObligations(PIIType pii);

}
