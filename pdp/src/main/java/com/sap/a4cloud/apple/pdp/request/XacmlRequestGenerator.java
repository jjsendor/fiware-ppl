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

import java.util.HashMap;
import java.util.Map;

import org.herasaf.xacml.core.context.impl.ActionType;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.EnvironmentType;
import org.herasaf.xacml.core.context.impl.ObjectFactory;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResourceType;
import org.herasaf.xacml.core.context.impl.SubjectType;
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute;

/**
 * Generates XACML request for HERAS PDP.
 *
 * @author Jakub Sendor
 *
 */
public class XacmlRequestGenerator {

	private static final ObjectFactory ofHerasContext = new ObjectFactory();
	private Map<String, String> subjects = new HashMap<String, String>();
	private Map<String, String> resources = new HashMap<String, String>();
	private Map<String, String> actions = new HashMap<String, String>();
	private Map<String, String> environments = new HashMap<String, String>();
	private RequestType request = ofHerasContext.createRequestType();

	/**
	 * Adds subject to the XACML request.
	 *
	 * @param subjectAttributeId	the subject attribute id
	 * @param subjectAttributeValue	the subject attribute value
	 * @return the XACML request generator instance
	 */
	public XacmlRequestGenerator addSubject(String subjectAttributeId,
			String subjectAttributeValue) {
		subjects.put(subjectAttributeId, subjectAttributeValue);
		return this;
	}

	/**
	 * Adds resource to the XACML request.
	 *
	 * @param resourceAttributeId		the resource attribute id
	 * @param resourceAttributeValue	the resource attribute value
	 * @return the XACML request generator instance
	 */
	public XacmlRequestGenerator addResource(String resourceAttributeId,
			String resourceAttributeValue) {
		resources.put(resourceAttributeId, resourceAttributeValue);
		return this;
	}

	/**
	 * Adds action to the XACML request.
	 *
	 * @param actionAttributeId		the action attribute id
	 * @param actionAttributeValue	the action attribute value
	 * @return the XACML request generator instance
	 */
	public XacmlRequestGenerator addAction(String actionAttributeId,
			String actionAttributeValue) {
		actions.put(actionAttributeId, actionAttributeValue);
		return this;
	}

	/**
	 * Adds environment to the XACML request.
	 *
	 * @param environmentAttributeId	the environment attribute id
	 * @param environmentAttributeValue	the environment attribute value
	 * @return the XACML request generator instance
	 */
	public XacmlRequestGenerator addEnvironment(String environmentAttributeId,
			String environmentAttributeValue) {
		environments.put(environmentAttributeId, environmentAttributeValue);
		return this;
	}

	/**
	 * Generates the XACML request.
	 *
	 * @return	the XACML request compatible with HERAS
	 */
	public RequestType generate() {
		// add subject attributes
		for (String subjectAttributeId : subjects.keySet()) {
			String subjectAttributeValue = subjects.get(subjectAttributeId);
			addSubjectAttribute(subjectAttributeId, subjectAttributeValue);
		}

		// add resource attributes
		for (String resourceAttributeId : resources.keySet()) {
			String resourceAttributeValue = resources.get(resourceAttributeId);
			addResourceAttribute(resourceAttributeId, resourceAttributeValue);
		}

		// add action attribute
		// (due to the HERAS request taking only one action attribute only
		// the last one will be used)
		for (String actionAttributeId : actions.keySet()) {
			String actionAttributeValue = actions.get(actionAttributeId);
			addActionAttribute(actionAttributeId, actionAttributeValue);
		}

		// add environment
		// (only the last one - as for action) 
		for (String environmentAttributeId : environments.keySet()) {
			String environmentAttributeValue =
					environments.get(environmentAttributeId);
			addEnvironmentAttribute(environmentAttributeId,
					environmentAttributeValue);
		}

		return request;
	}

	/**
	 * Creates the subject attribute and adds in the request.
	 *
	 * @param subjectAttributeId	the subject attribute id
	 * @param subjectAttributeValue	the subject attribute value
	 */
	private void addSubjectAttribute(String subjectAttributeId,
			String subjectAttributeValue) {
		SubjectType subjectType = ofHerasContext.createSubjectType();
		AttributeType subjectAttribute = createAttribute(
				subjectAttributeId, subjectAttributeValue);
		subjectType.getAttributes().add(subjectAttribute);
		request.getSubjects().add(subjectType);
	}

	/**
	 * Creates the resource attribute and adds it to the request.
	 *
	 * @param resourceAttributeId		the resource attribute id
	 * @param resourceAttributeValue	the resource attribute value
	 */
	private void addResourceAttribute(String resourceAttributeId,
			String resourceAttributeValue) {
		ResourceType resourceType = ofHerasContext.createResourceType();
		AttributeType resourceAttribute = createAttribute(
				resourceAttributeId, resourceAttributeValue);
		resourceType.getAttributes().add(resourceAttribute);
		request.getResources().add(resourceType);
	}

	/**
	 * Creates the action attribute and adds it to the request.
	 *
	 * @param actionAttributeId		the action attribute id
	 * @param actionAttributeValue	the action attribute value
	 */
	private void addActionAttribute(String actionAttributeId,
			String actionAttributeValue) {
		ActionType actionType = ofHerasContext.createActionType();
		AttributeType actionAttribute = createAttribute(
				actionAttributeId, actionAttributeValue);
		actionType.getAttributes().add(actionAttribute);
		request.setAction(actionType);
	}

	/**
	 * Creates the environment attribute and adds it to the request.
	 *
	 * @param environmentAttributeId	the environment attribute id
	 * @param environmentAttributeValue	the environment attribute value
	 */
	private void addEnvironmentAttribute(String environmentAttributeId,
			String environmentAttributeValue) {
		EnvironmentType environmentType = ofHerasContext.createEnvironmentType();
		AttributeType environmentAttribute = createAttribute(
				environmentAttributeId, environmentAttributeValue);
		environmentType.getAttributes().add(environmentAttribute);
		request.setEnvironment(environmentType);
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
