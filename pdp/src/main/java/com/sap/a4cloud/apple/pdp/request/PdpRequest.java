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

import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.EnvironmentType;
import org.herasaf.xacml.core.context.impl.ObjectFactory;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResourceType;
import org.herasaf.xacml.core.context.impl.SubjectType;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;

/**
 * Authorization request used by decision point during the policy evaluation.
 *
 * @author Jakub Sendor
 *
 */
public class PdpRequest {

	private static final ObjectFactory ofHerasContext = new ObjectFactory();

	private String subject;
	private String resource;
	private String purpose;

	/**
	 * Create request for a given subject, resource and purpose.
	 *
	 * @param subject	the subject requesting authorization
	 * @param resource	the resource to which access is requested
	 * @param purpose	the purpose of requesting authorization to access
	 * 					the resource
	 */
	public PdpRequest(String subject, String resource, String purpose) {
		super();
		this.subject = subject;
		this.resource = resource;
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
	 * Returns the resource to which access is requested.
	 *
	 * @return	the request resource
	 */
	public String getResource() {
		return resource;
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
		//create the request query
		RequestType request = ofHerasContext.createRequestType();

		//create the subject attribute
		SubjectType subjectType = ofHerasContext.createSubjectType();
		AttributeType subjectAttribute = createAttribute(
				"http://www.a4cloud.eu/appl/subject", subject);
		subjectType.getAttributes().add(subjectAttribute);
		request.getSubjects().add(subjectType);

		// create the resource attribute
		ResourceType resourceType = ofHerasContext.createResourceType();
		AttributeType resourceAttribute = createAttribute(
				"http://www.a4cloud.eu/appl/resource", resource);
		resourceType.getAttributes().add(resourceAttribute);
		request.getResources().add(resourceType);

		// create empty environment
		request.setEnvironment(new EnvironmentType());

		return request;
	}

	/**
	 * Helper method that creates attribute element with a given id and value.
	 *
	 * @param attributeId		the attribute id
	 * @param attributeValue	the attribute value
	 * @return	the attribute element containing given id and value
	 */
	private static AttributeType createAttribute(String attributeId,
			String attributeValue) {
		AttributeType attribute = ofHerasContext.createAttributeType();
		AttributeValueType value = ofHerasContext.createAttributeValueType();

		attribute.setAttributeId(attributeId);
		attribute.setDataType(new StringDataTypeAttribute());

		value.getContent().add(attributeValue);
		attribute.getAttributeValues().add(value);
		return attribute;
	}

}
