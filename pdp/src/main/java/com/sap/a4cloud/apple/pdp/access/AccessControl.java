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
package com.sap.a4cloud.apple.pdp.access;


import org.herasaf.xacml.core.api.PDP;
import org.herasaf.xacml.core.api.PolicyRepository;
import org.herasaf.xacml.core.context.RequestCtx;
import org.herasaf.xacml.core.context.ResponseCtx;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.simplePDP.SimplePDPFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.primelife.ppl.policy.impl.PolicySetType;
import eu.primelife.ppl.policy.impl.PolicyType;

/**
 * The Access Control class contains the methods which perform access control
 * evaluation using HERAS.
 *
 */
public class AccessControl {

	private static final Logger LOGGER =
			LoggerFactory.getLogger(AccessControl.class);

	private PDP simplePDP;
	private PolicyRepository repo;

	/**
	 * Creates a new Access Control object. Initializes HERAS.
	 */
	public AccessControl() {
		SimplePDPFactory.useDefaultInitializers();
		simplePDP = SimplePDPFactory.getSimplePDP();
		repo = simplePDP.getPolicyRepository();
	}

	/**
	 * Performs access control evaluation using HERAS PDP.
	 *
	 * @param policySet	the policy set against which the request is evaluated
	 * @param request	the XACML request
	 * @return the decision (PERMIT, DENY, INDETERMINATE, NOT_APPLICABLE)
	 */
	public DecisionType evaluate(RequestType request, PolicySetType policySet) {
		Evaluatable evaluatable;

		// convert to HERAS policy set
		try {
			evaluatable = ConverterFunctions.convertToHerasPolicySet(policySet);
		} catch (ConverterException e) {
			LOGGER.error("Exception while converting to the HERAS policy set", e);
			return DecisionType.INDETERMINATE;
		}

		return evaluate(request, evaluatable);
	}

	/**
	 * Performs access control evaluation using HERAS PDP. 
	 *
	 * @param policy	the policy against which the request is evaluated
	 * @param request	the XACML request
	 * @return the decision (PERMIT, DENY, INDETERMINATE, NOT_APPLICABLE)
	 */
	public DecisionType evaluate(RequestType request, PolicyType policy) {
		Evaluatable evaluatable;

		// convert to HERAS policy
		try {
			evaluatable = ConverterFunctions.convertToHerasPolicy(policy);
		} catch (ConverterException e) {
			LOGGER.error("Exception while converting to the HERAS policy", e);
			return DecisionType.INDETERMINATE;
		}

		return evaluate(request, evaluatable);
	}

	private DecisionType evaluate(RequestType request,
			Evaluatable evaluatable) {
		repo.deploy(evaluatable);

		ResponseCtx responseCtx = simplePDP.evaluate(new RequestCtx(request));
		DecisionType decision = responseCtx.getResponse().getResults().get(0).getDecision();
		return decision;
	}

}
