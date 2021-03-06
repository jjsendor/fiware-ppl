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
package com.sap.a4cloud.apple.pep;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.a4cloud.apple.pdp.PDP;
import com.sap.a4cloud.apple.pdp.request.PdpRequest;
import com.sap.research.primelife.dao.PiiDao;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.pii.impl.PIITypePolicySetOrPolicyItem;
import eu.primelife.ppl.policy.impl.PolicySetType;
import eu.primelife.ppl.policy.impl.PolicyType;

/**
 * Policy Enforcement Point.
 * Provides methods for enforcing access to the PII.
 *
 * @author Jakub Sendor
 *
 */
public class PEP {

	private static final Logger LOGGER = LoggerFactory.getLogger(PEP.class);

	private PiiDao piiDao = new PiiDao();
	private PDP pdp = new PDP();

	/**
	 * Retrieves list of PII with a given attribute name owned by a given user.
	 * If the owner is <code>null</code> than all PII with this attribute name
	 * are retrieved regardless of the owner.
	 *
	 * @param subject			the subject requesting PII
	 * @param purpose			the purpose of using requested PII
	 * @param attributeName		the PII attribute name
	 * @param owner				the PII owner
	 * @return list of PII
	 */
	public List<PIIType> getPii(String subject, String purpose,
			String attributeName, String owner) {
		LOGGER.info("Requesting PII with attribute name {} owned by {}",
				attributeName, owner);
		List<PIIType> piiList = retrievePiiList(attributeName, owner);
		List<PIIType> result = new ArrayList<PIIType>(piiList.size());

		PdpRequest request = new PdpRequest(subject, attributeName, purpose);
		// filter the list according to the decision and add to the result list
		for (PIIType pii : piiList) {
			List<PIITypePolicySetOrPolicyItem> policies =
					pii.getPolicySetOrPolicyItems();
			boolean authorized = isAuthorized(request, policies);

			if (authorized) {
				result.add(pii);
			}
		}

		// TODO create and trigger event data used for purpose
		return result;
	}

	/**
	 * Retrieves the PII list based on the PII attribute name. If the owner is
	 * <code>null</code> only the attribute name is taken into account.
	 *
	 * @param attributeName	the PII attribute name
	 * @param owner			the PII owner
	 * @return	the PII list
	 */
	private List<PIIType> retrievePiiList(String attributeName, String owner) {
		if (owner == null) {
			return piiDao.findAllByAttributeNameAndOwner(attributeName, owner);
		}

		return piiDao.findAllByAttributeName(attributeName);
	}

	/**
	 * Checks the authorization based on the given request and the list of
	 * policy sets or policies.
	 * Only the first element of the list is taken into account when taking the
	 * access control decision. It can be either policy set or policy
	 *
	 * @param request	the access control request 
	 * @param policies	list of policy sets or policies
	 * @return	<code>true</code> if the access is permitted,
	 * 			<code>false</code> otherwise
	 */
	private boolean isAuthorized(PdpRequest request,
			List<PIITypePolicySetOrPolicyItem> policies) {
		// empty policy list means there no access right
		if (policies.isEmpty()) {
			return false;
		}

		PIITypePolicySetOrPolicyItem policySetOrPolicy =
				policies.get(0);
		// extract policy set and policy
		PolicySetType policySet = policySetOrPolicy.getItemPolicySet();
		PolicyType policy = policySetOrPolicy.getItemPolicy();

		// the policy set has the precedence over the policy:
		// if the policy set exists, check the authorization against it
		// otherwise check the authorization against the policy
		return (policySet != null && pdp.decide(request, policySet))
				|| (policy != null && pdp.decide(request, policy));
	}

}
