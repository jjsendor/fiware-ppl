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

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.a4cloud.apple.pdp.request.PdpRequest;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.policy.impl.PolicySetType;
import eu.primelife.ppl.policy.impl.PolicyType;

/**
 * Tests for Policy Decision Point.
 *
 * @author Jakub Sendor
 *
 */
public class PdpTest {

	private static final String POLICY_ROOT = "/policies/access/";
	private static UnmarshallImpl unmarshaller;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		unmarshaller = new UnmarshallImpl(PolicyType.class.getPackage());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testDecideOnPolicySet() throws SyntaxException {
		// unmarshal policy set
		PolicySetType policySet = (PolicySetType) unmarshaller
				.unmarshal(getClass()
				.getResourceAsStream(POLICY_ROOT + "PolicySet.xml"));
		PDP pdp = new PDP();

		// test whether the request is permitted
		PdpRequest requestPermit = new PdpRequest("MarcheAzur",
				"shopping-history",
				"http://www.w3.org/2006/01/P3Pv11/marketing");
		boolean resultPermit = pdp.decide(requestPermit, policySet);
		assertTrue(resultPermit);

		// test whether the request is denied
		PdpRequest requestDeny = new PdpRequest("SkyCapital",
				"shopping-history",
				"http://www.w3.org/2006/01/P3Pv11/marketing");
		boolean resultDeny = pdp.decide(requestDeny, policySet);
		assertFalse(resultDeny);
	}

	@Test
	public void testDecideOnPolicy() throws SyntaxException {
		// unmarshall policy
		PolicyType policy = (PolicyType) unmarshaller.unmarshal(getClass()
				.getResourceAsStream(POLICY_ROOT + "Policy.xml"));
		PDP pdp = new PDP();

		// test whether the request is permitted
		PdpRequest requestPermit = new PdpRequest("MarcheAzur",
				"shopping-history",
				"http://www.w3.org/2006/01/P3Pv11/marketing");
		boolean resultPermit = pdp.decide(requestPermit, policy);
		assertTrue(resultPermit);

		// test whether the request is denied
		PdpRequest requestDeny = new PdpRequest("SkyCapital",
				"shopping-history",
				"http://www.w3.org/2006/01/P3Pv11/marketing");
		boolean resultDeny = pdp.decide(requestDeny, policy);
		assertFalse(resultDeny);
	}

}
