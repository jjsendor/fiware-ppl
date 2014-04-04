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

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.herasaf.xacml.core.policy.PolicyConverter;
import org.herasaf.xacml.core.policy.impl.PolicySetType;
import org.herasaf.xacml.core.policy.impl.PolicyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.MarshallFactory;
import com.sap.research.primelife.marshalling.MarshallImpl;
import com.sap.research.primelife.marshalling.UnmarshallFactory;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.policy.impl.ObjectFactory;
import eu.primelife.ppl.policy.impl.RuleType;
import eu.primelife.ppl.policy.xacml.impl.PolicySetTypePolicySetOrPolicyOrPolicySetIdReferenceItem;
import eu.primelife.ppl.policy.xacml.impl.PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem;

/**
 * Class contain different converter functions
 * 
 * 
 * @Version 0.1
 * @Date Jun 29, 2010
 * 
 */
public class ConverterFunctions {

	private static final Logger LOGGER =
			LoggerFactory.getLogger(ConverterFunctions.class);
	private static ObjectFactory objectFactory = new ObjectFactory();
	private static MarshallImpl marshaller;
	private static UnmarshallImpl unmarshaller;

	static {
		try {
			marshaller = MarshallFactory.createMarshallImpl(
					eu.primelife.ppl.policy.impl.PolicySetType.class
							.getPackage(), false);
		} catch (JAXBException e) {
			LOGGER.error("Exception while creating marshaller for policy converter", e);
		}

		try {
			unmarshaller = UnmarshallFactory.createUnmarshallImpl(
					eu.primelife.ppl.policy.impl.PolicySetType.class
							.getPackage());
		} catch (JAXBException e) {
			LOGGER.error("Exception while creating unmarshaller for policy converter", e);
		}
	}

	/**
	 * Transforms policy set element from PPL schema to conform with pure XACML
	 * schema (e.g. removes Data Handling Policy and Data Handling Preferences).
	 *
	 * @param pplPolicySet
	 *            the policy set element conforming with PPL schema
	 * @return the policy set element conforming with the HERAS XACML schema
	 * @throws ConverterException
	 *             if there was an error while converting the policy set
	 */
	public static PolicySetType convertToHerasPolicySet(
			eu.primelife.ppl.policy.impl.PolicySetType pplPolicySet)
			throws ConverterException {
		// clone PPL policy set
		// (because transformation will be removing PPL-specific parts of PolicySet,
		// and that changes shouldn't affect original Policy)
		eu.primelife.ppl.policy.impl.PolicySetType clonedPplPolicySet =
				clonePplPolicySet(pplPolicySet);

		// remove PPL-specific elements
		removePplSpecificElements(clonedPplPolicySet);

		// marshal to string again
		StringWriter writer = marshalPplPolicySet(clonedPplPolicySet);

		// change namespace prefix of PPL elements
		String modifiedPolicySet = swapPolicySetPrefix(writer.toString());
		Reader reader = new StringReader(modifiedPolicySet);

		PolicySetType herasPolicySet = unmarshalHerasPolicySet(reader);
		return herasPolicySet;
	}

	/**
	 * PPL policy set is marshalled and unmarshalled to create ideal copy.
	 *
	 * @param pplPolicySet
	 *            the PPL policy set to be cloned
	 * @return ideal copy of the policy set
	 * @throws ConverterException
	 *             thrown if there is an issue marshalling or unmarshalling the
	 *             policy set
	 */
	private static eu.primelife.ppl.policy.impl.PolicySetType clonePplPolicySet(
			eu.primelife.ppl.policy.impl.PolicySetType pplPolicySet)
			throws ConverterException {
		StringWriter writer = marshalPplPolicySet(pplPolicySet);
		Reader reader = new StringReader(writer.toString());
		eu.primelife.ppl.policy.impl.PolicySetType clonedPplPolicySet =
				unmarshalPplPolicySet(reader);
		return clonedPplPolicySet;
	}

	private static eu.primelife.ppl.policy.impl.PolicySetType unmarshalPplPolicySet(
			Reader reader) throws ConverterException {
		eu.primelife.ppl.policy.impl.PolicySetType policySet;

		try {
			policySet = (eu.primelife.ppl.policy.impl.PolicySetType)
					unmarshaller.unmarshal(reader);
		} catch (SyntaxException e) {
			LOGGER.error("Exception while unmarshalling the policy set", e);
			throw new ConverterException("Unable to unmarshal the policy set", e);
		}

		return policySet;
	}

	private static StringWriter marshalPplPolicySet(
			eu.primelife.ppl.policy.impl.PolicySetType pplPolicySet)
			throws ConverterException {
		StringWriter writer = new StringWriter();

		try {
			JAXBElement<eu.primelife.ppl.policy.impl.PolicySetType> jaxbPolicySet =
					objectFactory.createPolicySet(pplPolicySet);
			marshaller.marshal(jaxbPolicySet, writer);
		} catch (WritingException e) {
			LOGGER.error("Exception while marshalling the policy set", e);
			throw new ConverterException("Unable to marshall the policy set", e);
		}

		return writer;
	}

	private static PolicySetType unmarshalHerasPolicySet(Reader reader)
			throws ConverterException {
		PolicySetType herasPolicySetType = null;

		try {
			herasPolicySetType = (PolicySetType) PolicyConverter
					.unmarshal(reader);
		} catch (org.herasaf.xacml.core.SyntaxException e) {
			LOGGER.error("Exception while umarshalling the HERAS policy set", e);
			throw new ConverterException("Unable to umarshal the HERAS policy set", e);
		}

		return herasPolicySetType;
	}

	/**
	 * Recursively removes all PPL-specific elements (Data Handling Preferences,
	 * Data Handling Policy or Sticky Policy elements) from a given policy set
	 * and all nested policy sets and policy elements.
	 *
	 * @param policySet	the policy set from which all PPL-specific elements
	 * 					will be removed
	 */
	private static void removePplSpecificElements(eu.primelife.ppl.policy.impl.PolicySetType policySet) {
		policySet.setDataHandlingPolicy(null);
		policySet.setDataHandlingPreferences(null);
		policySet.setStickyPolicy(null);

		for (PolicySetTypePolicySetOrPolicyOrPolicySetIdReferenceItem item :
				policySet.getPolicySetOrPolicyOrPolicySetIdReferenceItems()) {
			eu.primelife.ppl.policy.impl.PolicyType policy =
					(eu.primelife.ppl.policy.impl.PolicyType) item.getItemPolicy();

			if (policy != null) {
				removePplSpecificElements(policy);
			}

			eu.primelife.ppl.policy.impl.PolicySetType nestedPolicySet =
				(eu.primelife.ppl.policy.impl.PolicySetType) item.getItemPolicySet();

			if (nestedPolicySet != null) {
				removePplSpecificElements(nestedPolicySet);
			}
		}
	}

	/**
	 * Transforms policy element from PPL schema to conform with pure XACML
	 * schema (e.g. removes Data Handling Policy and Data Handling Preferences).
	 *
	 * @param pplPolicy
	 *            the policy element conforming with the PPL schema
	 * @return the policy element conforming with the HERAS XACML schema
	 * @throws ConverterException
	 *             if there was an error while converting the policy
	 */
	public static PolicyType convertToHerasPolicy(
			eu.primelife.ppl.policy.impl.PolicyType pplPolicy)
			throws ConverterException {
		// clone PPL policy
		// (because transformation will be removing PPL-specific parts of Policy,
		// and that changes shouldn't affect original Policy)
		eu.primelife.ppl.policy.impl.PolicyType clonedPplPolicy =
				clonePplPolicy(pplPolicy);

		// remove PPL-specific elements
		removePplSpecificElements(clonedPplPolicy);

		// marshal to string again
		StringWriter writer = marshalPplPolicy(clonedPplPolicy);

		// change namespace prefix of PPL elements
		String modifiedPolicy = swapPolicyPrefix(writer.toString());
		Reader reader = new StringReader(modifiedPolicy);

		PolicyType herasPolicy = unmarshalHerasPolicy(reader);
		return herasPolicy;
	}

	/**
	 * PPL policy is marshalled and unmarshalled to create ideal copy.
	 *
	 * @param pplPolicy
	 *            the PPL policy to be cloned
	 * @return ideal copy of the policy
	 * @throws ConverterException
	 *             thrown if there is an issue marshalling or unmarshalling the
	 *             policy
	 */
	private static eu.primelife.ppl.policy.impl.PolicyType clonePplPolicy(
			eu.primelife.ppl.policy.impl.PolicyType pplPolicy)
			throws ConverterException {
		StringWriter writer = marshalPplPolicy(pplPolicy);
		Reader reader = new StringReader(writer.toString());
		eu.primelife.ppl.policy.impl.PolicyType clonedPplPolicy =
				unmarshalPplPolicy(reader);
		return clonedPplPolicy;
	}

	private static eu.primelife.ppl.policy.impl.PolicyType unmarshalPplPolicy(
			Reader reader) throws ConverterException {
		eu.primelife.ppl.policy.impl.PolicyType policy = null;

		try {
			policy = (eu.primelife.ppl.policy.impl.PolicyType)
					unmarshaller.unmarshal(reader);
		} catch (SyntaxException e) {
			LOGGER.error("Excpetion while unmarshalling the policy", e);
			throw new ConverterException("Unable to unmarshal the policy", e);
		}

		return policy;
	}

	private static StringWriter marshalPplPolicy(
			eu.primelife.ppl.policy.impl.PolicyType pplPolicy)
			throws ConverterException {
		StringWriter writer = new StringWriter();

		try {
			JAXBElement<eu.primelife.ppl.policy.impl.PolicyType> jaxbPolicy =
					objectFactory.createPolicy(pplPolicy);
			marshaller.marshal(jaxbPolicy, writer);
		} catch (WritingException e) {
			LOGGER.error("Exception while marshalling the policy", e);
			throw new ConverterException("Unable to marshall the policy", e);
		}

		return writer;
	}

	private static PolicyType unmarshalHerasPolicy(Reader reader)
			throws ConverterException {
		PolicyType herasPolicy;

		try {
			herasPolicy = (PolicyType) PolicyConverter.unmarshal(reader);
		} catch (org.herasaf.xacml.core.SyntaxException e) {
			LOGGER.error("Exception while umarshalling the cloned policy", e);
			throw new ConverterException("Unable to umarshal the cloned policy", e);
		}

		return herasPolicy;
	}

	/**
	 * Removes all PPL-specific elements (Data Handling Preferences,
	 * Data Handling Policy or Sticky Policy elements) from a given policy
	 * and rules that this policy contains.
	 *
	 * @param policy	the policy from which PPL-specific elements will be
	 * 					removed
	 */
	private static void removePplSpecificElements(eu.primelife.ppl.policy.impl.PolicyType policy) {
		policy.setDataHandlingPolicy(null);
		policy.setDataHandlingPreferences(null);
		policy.setStickyPolicy(null);

		for (PolicyTypeCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItem item :
				policy.getCombinerParametersOrRuleCombinerParametersOrVariableDefinitionItems()) {
			RuleType rule = (RuleType) item.getItemRule();

			if (rule != null) {
				rule.setDataHandlingPolicy(null);
				rule.setDataHandlingPreferences(null);
				rule.setStickyPolicy(null);
			}
		}
	}

	private static String swapPolicySetPrefix(String s) {
		return swapPolicyPrefix(s.replaceAll("ppl:PolicySet", "xacml:PolicySet"));
	}

	private static String swapPolicyPrefix(String s) {
		return swapRulePrefix(s.replaceAll("ppl:Policy", "xacml:Policy"));
	}

	private static String swapRulePrefix(String s) {
		return s.replaceAll("ppl:Rule", "xacml:Rule");
	}

}
