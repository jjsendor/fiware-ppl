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
package com.sap.a4cloud.apple.pdp.request;

import org.herasaf.xacml.core.context.impl.RequestType;

/**
 * Authorization request used by decision point during the policy evaluation.
 *
 * @author Jakub Sendor
 *
 */
public class PdpRequest {

	private static final String SUBJECT_ATTR_ID = "subject:subject-id";

	private String subject;
	private String purpose;

	/**
	 * Create request for a given subject, resource and purpose.
	 *
	 * @param subject	the subject requesting authorization
	 * @param resource	the resource to which access is requested
	 * @param purpose	the purpose of requesting authorization to access
	 * 					the resource
	 */
	public PdpRequest(String subject, String purpose) {
		super();
		this.subject = subject;
		this.purpose = purpose;
	}

	/**
	 * Returns the subject which is requesting the authorization.
	 *
	 * @return	the request subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Returns the purpose for which requested resource is accessed.
	 *
	 * @return the request purpose
	 */
	public String getPurpose() {
		return purpose;
	}

	/**
	 * Converts the request to XACML request that can be used for access
	 * control.
	 *
	 * @return a request that can be used for access control evaluation
	 */
	public RequestType toXacmlRequest() {
		// create the request query using subject
		return new XacmlRequestGenerator()
			.addSubject(SUBJECT_ATTR_ID, subject)
			.generate();
	}

}
