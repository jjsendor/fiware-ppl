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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import eu.primelife.ppl.policy.impl.DataHandlingPolicyType;
import oasis.names.tc.saml._2_0.assertion.AssertionType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBHashCodeBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;


/**
 * <p>Java class for ResourceQueryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceQueryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:context:schema:os}Request"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Assertion" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.primelife.eu/ppl}DataHandlingPolicy" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceQueryType", propOrder = {
    "request",
    "assertion",
    "dataHandlingPolicy"
})
@Entity(name = "eu.primelife.ppl.claims.impl.ResourceQueryType")
@Table(name = "RESOURCEQUERYTYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class ResourceQueryType
    implements Serializable, Equals, HashCode, ToString
{

    @XmlElement(name = "Request", namespace = "urn:oasis:names:tc:xacml:2.0:context:schema:os", required = true)
    protected RequestType request;
    @XmlElement(name = "Assertion", namespace = "urn:oasis:names:tc:SAML:2.0:assertion")
    protected List<AssertionType> assertion;
    @XmlElement(name = "DataHandlingPolicy", namespace = "http://www.primelife.eu/ppl")
    protected DataHandlingPolicyType dataHandlingPolicy;
    @XmlAttribute(name = "Hjid")
    protected Long hjid;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link RequestType }
     *     
     */
    @ManyToOne(targetEntity = RequestType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "REQUEST_RESOURCEQUERYTYPE_HJ_0")
    public RequestType getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestType }
     *     
     */
    public void setRequest(RequestType value) {
        this.request = value;
    }

    /**
     * Gets the value of the assertion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assertion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssertion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssertionType }
     * 
     * 
     */
    @OneToMany(targetEntity = AssertionType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "ASSERTION__RESOURCEQUERYTYPE_0")
    public List<AssertionType> getAssertion() {
        if (assertion == null) {
            assertion = new ArrayList<AssertionType>();
        }
        return this.assertion;
    }

    /**
     * 
     * 
     */
    public void setAssertion(List<AssertionType> assertion) {
        this.assertion = assertion;
    }

    /**
     * Gets the value of the dataHandlingPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link DataHandlingPolicyType }
     *     
     */
    @ManyToOne(targetEntity = DataHandlingPolicyType.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "DATAHANDLINGPOLICY_RESOURCEQ_0")
    public DataHandlingPolicyType getDataHandlingPolicy() {
        return dataHandlingPolicy;
    }

    /**
     * Sets the value of the dataHandlingPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandlingPolicyType }
     *     
     */
    public void setDataHandlingPolicy(DataHandlingPolicyType value) {
        this.dataHandlingPolicy = value;
    }

    /**
     * Gets the value of the hjid property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    @Id
    @Column(name = "HJID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getHjid() {
        return hjid;
    }

    /**
     * Sets the value of the hjid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHjid(Long value) {
        this.hjid = value;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof ResourceQueryType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final ResourceQueryType that = ((ResourceQueryType) object);
        equalsBuilder.append(this.getRequest(), that.getRequest());
        equalsBuilder.append(this.getAssertion(), that.getAssertion());
        equalsBuilder.append(this.getDataHandlingPolicy(), that.getDataHandlingPolicy());
    }

    public boolean equals(Object object) {
        if (!(object instanceof ResourceQueryType)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getRequest());
        hashCodeBuilder.append(this.getAssertion());
        hashCodeBuilder.append(this.getDataHandlingPolicy());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            RequestType theRequest;
            theRequest = this.getRequest();
            toStringBuilder.append("request", theRequest);
        }
        {
            List<AssertionType> theAssertion;
            theAssertion = this.getAssertion();
            toStringBuilder.append("assertion", theAssertion);
        }
        {
            DataHandlingPolicyType theDataHandlingPolicy;
            theDataHandlingPolicy = this.getDataHandlingPolicy();
            toStringBuilder.append("dataHandlingPolicy", theDataHandlingPolicy);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

}