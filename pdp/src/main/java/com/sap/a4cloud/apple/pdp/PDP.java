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
package com.sap.a4cloud.apple.pdp;

import org.herasaf.xacml.core.context.impl.DecisionType;

import com.sap.a4cloud.apple.pdp.access.AccessControl;
import com.sap.a4cloud.apple.pdp.request.PdpRequest;

import eu.primelife.ppl.policy.impl.PolicySetType;
import eu.primelife.ppl.policy.impl.PolicyType;

/**
 * Policy Decision Point.
 * Evaluates policy and issues authorization decision.
 *
 * @author Jakub Sendor
 *
 */
public class PDP {

	/**
	 * Evaluates authorization request against a given policy.
	 *
	 * @param request	the authorization request
	 * @param policy	the policy against which the request is evaluated
	 *
	 * @return	<code>true</code> if the authorization is granted,
	 * 			<code>false</code> if it is denied
	 */
	public boolean decide(PdpRequest request, PolicyType policy) {
		AccessControl ac = new AccessControl();
		DecisionType decision = ac.evaluate(request.toXacmlRequest(), policy);
		// TODO add usage control (check purpose)
		return DecisionType.PERMIT.equals(decision);
	}

	/**
	 * Evaluates authorization request against a given policy set.
	 *
	 * @param request	the authorization request
	 * @param policySet	the policy against which the request is evaluated
	 *
	 * @return	<code>true</code> if the authorization is granted,
	 * 			<code>false</code> if it is denied
	 */
	public boolean decide(PdpRequest request, PolicySetType policySet) {
		AccessControl ac = new AccessControl();
		DecisionType decision = ac.evaluate(request.toXacmlRequest(), policySet);
		// TODO add usage control (check purpose)
		return DecisionType.PERMIT.equals(decision);
	}

}
