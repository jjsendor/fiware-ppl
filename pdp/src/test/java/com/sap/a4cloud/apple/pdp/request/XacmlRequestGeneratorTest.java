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

import static org.junit.Assert.*;

import java.util.List;

import org.herasaf.xacml.core.context.impl.ActionType;
import org.herasaf.xacml.core.context.impl.AttributeType;
import org.herasaf.xacml.core.context.impl.AttributeValueType;
import org.herasaf.xacml.core.context.impl.EnvironmentType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResourceType;
import org.herasaf.xacml.core.context.impl.SubjectType;
import org.junit.Test;

/**
 * Tests for XACML request generator.
 *
 * @author Jakub Sendor
 *
 */
public class XacmlRequestGeneratorTest {

	/**
	 * Tests adding the subject to the request.
	 */
	@Test
	public void testAddSubject() {
		String subjectAttributeId = "subject-id";
		String subjectAttributeValue = "Ernst Stavro Blofeld";

		RequestType request = new XacmlRequestGenerator()
			.addSubject(subjectAttributeId, subjectAttributeValue)
			.generate();

		List<SubjectType> subjects = request.getSubjects();
		assertEquals(1, subjects.size());

		SubjectType subject = subjects.get(0);
		List<AttributeType> attrs = subject.getAttributes();
		assertEquals(1, attrs.size());

		AttributeType attr = attrs.get(0);
		assertEquals(subjectAttributeId, attr.getAttributeId());

		String value = extractAttributeValue(attr);
		assertEquals(subjectAttributeValue, value);
	}

	/**
	 * Tests adding the resource to the request.
	 */
	@Test
	public void testAddResource() {
		String resourceAttributeId = "resource-id";
		String resourceAttributeValue = "The World";

		RequestType request = new XacmlRequestGenerator()
			.addResource(resourceAttributeId, resourceAttributeValue)
			.generate();

		List<ResourceType> resources = request.getResources();
		assertEquals(1, resources.size());

		ResourceType resource = resources.get(0);
		List<AttributeType> attrs = resource.getAttributes();
		assertEquals(1, attrs.size());

		AttributeType attr = attrs.get(0);
		assertEquals(resourceAttributeId, attr.getAttributeId());

		String value = extractAttributeValue(attr);
		assertEquals(resourceAttributeValue, value);
	}

	/**
	 * Tests adding the action to the request.
	 */
	@Test
	public void testAddAction() {
		String actionAttributeId = "action-id";
		String actionAttributeValue = "take over";

		RequestType request = new XacmlRequestGenerator()
			.addAction(actionAttributeId, actionAttributeValue)
			.generate();

		ActionType action = request.getAction();
		assertNotNull(action);

		List<AttributeType> attrs = action.getAttributes();
		assertEquals(1, attrs.size());

		AttributeType attr = attrs.get(0);
		assertEquals(actionAttributeId, attr.getAttributeId());

		String value = extractAttributeValue(attr);
		assertEquals(actionAttributeValue, value);
	}

	/**
	 * Tests adding the subject to the request.
	 */
	@Test
	public void testAddEnvironment() {
		String environmentAttributeId = "time";
		String environmentAttributeValue = "now";

		RequestType request = new XacmlRequestGenerator()
			.addEnvironment(environmentAttributeId, environmentAttributeValue)
			.generate();

		EnvironmentType environment = request.getEnvironment();
		assertNotNull(environment);

		List<AttributeType> attrs = environment.getAttributes();
		assertEquals(1, attrs.size());

		AttributeType attr = attrs.get(0);
		assertEquals(environmentAttributeId, attr.getAttributeId());

		String value = extractAttributeValue(attr);
		assertEquals(environmentAttributeValue, value);
	}

	private String extractAttributeValue(AttributeType attr) {
		List<AttributeValueType> values = attr.getAttributeValues();
		assertEquals(1, values.size());

		// extract attribute value
		AttributeValueType value = values.get(0);
		List<Object> content = value.getContent();
		assertNotNull(content);
		assertEquals(1, content.size());

		Object object = content.get(0);
		assertTrue (object instanceof String);

		return (String) object;
	}
}
