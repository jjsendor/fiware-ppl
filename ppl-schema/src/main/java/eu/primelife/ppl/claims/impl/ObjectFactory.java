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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v3.0-03/04/2009 09:20 AM(valikov)-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.03 at 09:58:45 AM CET 
//


package eu.primelife.ppl.claims.impl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.primelife.ppl.claims.impl package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DeneyPII_QNAME = new QName("http://www.primelife.eu/ppl/claims", "DeneyPII");
    private final static QName _AccessPII_QNAME = new QName("http://www.primelife.eu/ppl/claims", "AccessPII");
    private final static QName _MissingCredential_QNAME = new QName("http://www.primelife.eu/ppl/claims", "MissingCredential");
    private final static QName _Evidence_QNAME = new QName("http://www.primelife.eu/ppl/claims", "Evidence");
    private final static QName _ProvisionalAction_QNAME = new QName("http://www.primelife.eu/ppl/claims", "ProvisionalAction");
    private final static QName _Claims_QNAME = new QName("http://www.primelife.eu/ppl/claims", "Claims");
    private final static QName _ResourceQuery_QNAME = new QName("http://www.primelife.eu/ppl/claims", "ResourceQuery");
    private final static QName _MissingPII_QNAME = new QName("http://www.primelife.eu/ppl/claims", "MissingPII");
    private final static QName _Claim_QNAME = new QName("http://www.primelife.eu/ppl/claims", "Claim");
    private final static QName _Response_QNAME = new QName("http://www.primelife.eu/ppl/claims", "Response");
    private final static QName _PPLPolicyStatement_QNAME = new QName("http://www.primelife.eu/ppl/claims", "PPLPolicyStatement");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.primelife.ppl.claims.impl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResourceQueryType }
     * 
     */
    public ResourceQueryType createResourceQueryType() {
        return new ResourceQueryType();
    }

    /**
     * Create an instance of {@link PPLPolicyStatementType }
     * 
     */
    public PPLPolicyStatementType createPPLPolicyStatementType() {
        return new PPLPolicyStatementType();
    }

    /**
     * Create an instance of {@link ProvisionalActionType }
     * 
     */
    public ProvisionalActionType createProvisionalActionType() {
        return new ProvisionalActionType();
    }

    /**
     * Create an instance of {@link AttributeStatementType }
     * 
     */
    public AttributeStatementType createAttributeStatementType() {
        return new AttributeStatementType();
    }

    /**
     * Create an instance of {@link ListPIIType }
     * 
     */
    public ListPIIType createListPIIType() {
        return new ListPIIType();
    }

    /**
     * Create an instance of {@link ClaimType }
     * 
     */
    public ClaimType createClaimType() {
        return new ClaimType();
    }

    /**
     * Create an instance of {@link AttributeType }
     * 
     */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /**
     * Create an instance of {@link ConditionStatementType }
     * 
     */
    public ConditionStatementType createConditionStatementType() {
        return new ConditionStatementType();
    }

    /**
     * Create an instance of {@link StickyPolicyStatementType }
     * 
     */
    public StickyPolicyStatementType createStickyPolicyStatementType() {
        return new StickyPolicyStatementType();
    }

    /**
     * Create an instance of {@link ProvisionalActionStatementType }
     * 
     */
    public ProvisionalActionStatementType createProvisionalActionStatementType() {
        return new ProvisionalActionStatementType();
    }

    /**
     * Create an instance of {@link ClaimsType }
     * 
     */
    public ClaimsType createClaimsType() {
        return new ClaimsType();
    }

    /**
     * Create an instance of {@link EvidenceStatementType }
     * 
     */
    public EvidenceStatementType createEvidenceStatementType() {
        return new EvidenceStatementType();
    }

    /**
     * Create an instance of {@link EvidenceType }
     * 
     */
    public EvidenceType createEvidenceType() {
        return new EvidenceType();
    }

    /**
     * Create an instance of {@link ResponseType }
     * 
     */
    public ResponseType createResponseType() {
        return new ResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListPIIType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl/claims", name = "DeneyPII")
    public JAXBElement<ListPIIType> createDeneyPII(ListPIIType value) {
        return new JAXBElement<ListPIIType>(_DeneyPII_QNAME, ListPIIType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListPIIType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl/claims", name = "AccessPII")
    public JAXBElement<ListPIIType> createAccessPII(ListPIIType value) {
        return new JAXBElement<ListPIIType>(_AccessPII_QNAME, ListPIIType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListPIIType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl/claims", name = "MissingCredential")
    public JAXBElement<ListPIIType> createMissingCredential(ListPIIType value) {
        return new JAXBElement<ListPIIType>(_MissingCredential_QNAME, ListPIIType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EvidenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl/claims", name = "Evidence")
    public JAXBElement<EvidenceType> createEvidence(EvidenceType value) {
        return new JAXBElement<EvidenceType>(_Evidence_QNAME, EvidenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProvisionalActionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl/claims", name = "ProvisionalAction")
    public JAXBElement<ProvisionalActionType> createProvisionalAction(ProvisionalActionType value) {
        return new JAXBElement<ProvisionalActionType>(_ProvisionalAction_QNAME, ProvisionalActionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClaimsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl/claims", name = "Claims")
    public JAXBElement<ClaimsType> createClaims(ClaimsType value) {
        return new JAXBElement<ClaimsType>(_Claims_QNAME, ClaimsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResourceQueryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl/claims", name = "ResourceQuery")
    public JAXBElement<ResourceQueryType> createResourceQuery(ResourceQueryType value) {
        return new JAXBElement<ResourceQueryType>(_ResourceQuery_QNAME, ResourceQueryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListPIIType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl/claims", name = "MissingPII")
    public JAXBElement<ListPIIType> createMissingPII(ListPIIType value) {
        return new JAXBElement<ListPIIType>(_MissingPII_QNAME, ListPIIType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClaimType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl/claims", name = "Claim")
    public JAXBElement<ClaimType> createClaim(ClaimType value) {
        return new JAXBElement<ClaimType>(_Claim_QNAME, ClaimType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl/claims", name = "Response")
    public JAXBElement<ResponseType> createResponse(ResponseType value) {
        return new JAXBElement<ResponseType>(_Response_QNAME, ResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PPLPolicyStatementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.primelife.eu/ppl/claims", name = "PPLPolicyStatement")
    public JAXBElement<PPLPolicyStatementType> createPPLPolicyStatement(PPLPolicyStatementType value) {
        return new JAXBElement<PPLPolicyStatementType>(_PPLPolicyStatement_QNAME, PPLPolicyStatementType.class, null, value);
    }

}