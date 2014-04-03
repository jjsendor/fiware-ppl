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

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.herasaf.xacml.core.ProcessingException;
import org.herasaf.xacml.core.context.impl.DecisionType;
import org.herasaf.xacml.core.policy.MissingAttributeException;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.a4cloud.apple.pdp.request.PdpRequest;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.policy.impl.PolicySetType;
import eu.primelife.ppl.policy.impl.PolicyType;

/**
 * 
 * 
 * @Version 0.1
 * @Date Jun 11, 2010
 * 
 */
public class AccessControlTest {

	private static final String POLICY_ROOT = "/policies/access/";
	private static UnmarshallImpl unmarshaller;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		unmarshaller = new UnmarshallImpl(PolicyType.class.getPackage());
	}

	/**
	 * Tests whether request against the policy set is evaluated as permit.
	 */
	@Test
	public void testEvaluatePolicySet() throws SyntaxException,
			FileNotFoundException, org.herasaf.xacml.core.SyntaxException,
			ProcessingException, MissingAttributeException, WritingException,
			JAXBException {
		String policySetPath = POLICY_ROOT + "PolicySet.xml";
		PolicySetType policySet = (PolicySetType) unmarshaller.unmarshal(
				getClass().getResourceAsStream(policySetPath));
		PdpRequest request = new PdpRequest("MarcheAzur", "shopping-history",
				null);

		AccessControl ac = new AccessControl();
		DecisionType decision =
				ac.evaluate(request.toXacmlRequest(), policySet);
		assertEquals(DecisionType.PERMIT, decision);
	}

	/**
	 * Tests whether request against the policy is evaluated as permit.
	 */
	@Test
	public void testEvaluatePolicy() throws SyntaxException,
			FileNotFoundException, org.herasaf.xacml.core.SyntaxException,
			ProcessingException, MissingAttributeException, WritingException,
			JAXBException {
		String policyPath = POLICY_ROOT + "Policy.xml";
		PolicyType policy = (PolicyType) unmarshaller.unmarshal(
				getClass().getResourceAsStream(policyPath));
		PdpRequest request = new PdpRequest("MarcheAzur", "shopping-history",
				null);

		AccessControl ac = new AccessControl();
		DecisionType decision =
				ac.evaluate(request.toXacmlRequest(), policy);
		assertEquals(DecisionType.PERMIT, decision);
	}

}
