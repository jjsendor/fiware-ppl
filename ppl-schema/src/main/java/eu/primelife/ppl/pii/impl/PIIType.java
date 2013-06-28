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


package eu.primelife.ppl.pii.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.hyperjaxb3.item.ItemUtils;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XMLGregorianCalendarAsDateTime;
import org.jvnet.hyperjaxb3.xml.bind.annotation.adapters.XmlAdapterUtils;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBHashCodeBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;

import eu.primelife.ppl.policy.impl.PolicySetType;
import eu.primelife.ppl.policy.impl.PolicyType;


/**
 * <p>Java class for PIIType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PIIType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AttributeName" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="AttributeValue" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://www.primelife.eu/ppl}PolicySet"/>
 *           &lt;element ref="{http://www.primelife.eu/ppl}Policy"/>
 *         &lt;/choice>
 *         &lt;element name="CreationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="ModificationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PIIType", propOrder = {
    "attributeName",
    "attributeValue",
    "policySetOrPolicy",
    "creationDate",
    "modificationDate"
}) 
@Entity(name = "eu.primelife.ppl.pii.impl.PIIType")
@Table(name = "PIITYPE")
@Inheritance(strategy = InheritanceType.JOINED)
public class PIIType
    implements Serializable, Equals, HashCode, ToString
{
	@XmlElement(name = "AttributeName", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String attributeName;
    @XmlElement(name = "AttributeValue", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String attributeValue;
    @XmlElements({
        @XmlElement(name = "PolicySet", namespace = "http://www.primelife.eu/ppl", type = PolicySetType.class),
        @XmlElement(name = "Policy", namespace = "http://www.primelife.eu/ppl", type = PolicyType.class)
    })
    protected List<Object> policySetOrPolicy;
    @XmlElementRef(name = "CreationDate", namespace = "http://www.primelife.eu/ppl/pii", type = JAXBElement.class)
    protected JAXBElement<XMLGregorianCalendar> creationDate;
    @XmlElementRef(name = "ModificationDate", namespace = "http://www.primelife.eu/ppl/pii", type = JAXBElement.class)
    protected JAXBElement<XMLGregorianCalendar> modificationDate;
    @XmlAttribute(name = "Hjid")
    protected Long hjid;
    protected transient List<PIITypePolicySetOrPolicyItem> policySetOrPolicyItems;
    protected transient String owner;

    /**
     * Gets the value of the attributeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "ATTRIBUTENAME")
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Sets the value of the attributeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttributeName(String value) {
        this.attributeName = value;
    }

    /**
     * Gets the value of the attributeValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Basic
    @Column(name = "ATTRIBUTEVALUE")
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     * Sets the value of the attributeValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttributeValue(String value) {
        this.attributeValue = value;
    }

    /**
     * Gets the value of the policySetOrPolicy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the policySetOrPolicy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolicySetOrPolicy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PolicySetType }
     * {@link PolicyType }
     * 
     * 
     */
    @Transient
    public List<Object> getPolicySetOrPolicy() {
        if (policySetOrPolicy == null) {
            policySetOrPolicy = new ArrayList<Object>();
        }
        return this.policySetOrPolicy;
    }

    /**
     * 
     * 
     */
    public void setPolicySetOrPolicy(List<Object> policySetOrPolicy) {
        this.policySetOrPolicy = policySetOrPolicy;
    }

    /**
     * Gets the value of the creationDate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    @Transient
    public JAXBElement<XMLGregorianCalendar> getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the value of the creationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setCreationDate(JAXBElement<XMLGregorianCalendar> value) {
        this.creationDate = ((JAXBElement<XMLGregorianCalendar> ) value);
    }

    /**
     * Gets the value of the modificationDate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    @Transient
    public JAXBElement<XMLGregorianCalendar> getModificationDate() {
        return modificationDate;
    }

    /**
     * Sets the value of the modificationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setModificationDate(JAXBElement<XMLGregorianCalendar> value) {
        this.modificationDate = ((JAXBElement<XMLGregorianCalendar> ) value);
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

    @OneToMany(targetEntity = PIITypePolicySetOrPolicyItem.class, cascade = {
        CascadeType.ALL
    })
    @JoinColumn(name = "POLICYSETORPOLICYITEMS_PIITY_0")
    public List<PIITypePolicySetOrPolicyItem> getPolicySetOrPolicyItems() {
        if (this.policySetOrPolicyItems == null) {
            this.policySetOrPolicyItems = new ArrayList<PIITypePolicySetOrPolicyItem>();
        }
        if (ItemUtils.shouldBeWrapped(this.policySetOrPolicy)) {
            this.policySetOrPolicy = ItemUtils.wrap(this.policySetOrPolicy, this.policySetOrPolicyItems, PIITypePolicySetOrPolicyItem.class);
        }
        return this.policySetOrPolicyItems;
    }

    public void setPolicySetOrPolicyItems(List<PIITypePolicySetOrPolicyItem> value) {
        this.policySetOrPolicy = null;
        this.policySetOrPolicyItems = null;
        this.policySetOrPolicyItems = value;
        if (this.policySetOrPolicyItems == null) {
            this.policySetOrPolicyItems = new ArrayList<PIITypePolicySetOrPolicyItem>();
        }
        if (ItemUtils.shouldBeWrapped(this.policySetOrPolicy)) {
            this.policySetOrPolicy = ItemUtils.wrap(this.policySetOrPolicy, this.policySetOrPolicyItems, PIITypePolicySetOrPolicyItem.class);
        }
    }

    @Basic
    @Column(name = "CREATIONDATEITEM")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreationDateItem() {
        return XmlAdapterUtils.unmarshallJAXBElement(XMLGregorianCalendarAsDateTime.class, ((JAXBElement<? extends XMLGregorianCalendar> ) this.getCreationDate()));
    }

    public void setCreationDateItem(Date target) {
        setCreationDate(XmlAdapterUtils.marshallJAXBElement(XMLGregorianCalendarAsDateTime.class, XMLGregorianCalendar.class, new QName("http://www.primelife.eu/ppl/pii", "CreationDate"), PIIType.class, target));
    }

    @Basic
    @Column(name = "MODIFICATIONDATEITEM")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModificationDateItem() {
        return XmlAdapterUtils.unmarshallJAXBElement(XMLGregorianCalendarAsDateTime.class, ((JAXBElement<? extends XMLGregorianCalendar> ) this.getModificationDate()));
    }

    public void setModificationDateItem(Date target) {
        setModificationDate(XmlAdapterUtils.marshallJAXBElement(XMLGregorianCalendarAsDateTime.class, XMLGregorianCalendar.class, new QName("http://www.primelife.eu/ppl/pii", "ModificationDate"), PIIType.class, target));
    }
    
    @Basic
    @Column(name = "OWNER")
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof PIIType)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final PIIType that = ((PIIType) object);
        equalsBuilder.append(this.getAttributeName(), that.getAttributeName());
        equalsBuilder.append(this.getAttributeValue(), that.getAttributeValue());
        equalsBuilder.append(this.getPolicySetOrPolicy(), that.getPolicySetOrPolicy());
        equalsBuilder.append(this.getCreationDate(), that.getCreationDate());
        equalsBuilder.append(this.getModificationDate(), that.getModificationDate());
    }

    public boolean equals(Object object) {
        if (!(object instanceof PIIType)) {
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
        hashCodeBuilder.append(this.getAttributeName());
        hashCodeBuilder.append(this.getAttributeValue());
        hashCodeBuilder.append(this.getPolicySetOrPolicy());
        hashCodeBuilder.append(this.getCreationDate());
        hashCodeBuilder.append(this.getModificationDate());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theAttributeName;
            theAttributeName = this.getAttributeName();
            toStringBuilder.append("attributeName", theAttributeName);
        }
        {
            String theAttributeValue;
            theAttributeValue = this.getAttributeValue();
            toStringBuilder.append("attributeValue", theAttributeValue);
        }
        {
            List<Object> thePolicySetOrPolicy;
            thePolicySetOrPolicy = this.getPolicySetOrPolicy();
            toStringBuilder.append("policySetOrPolicy", thePolicySetOrPolicy);
        }
        {
            JAXBElement<XMLGregorianCalendar> theCreationDate;
            theCreationDate = this.getCreationDate();
            toStringBuilder.append("creationDate", theCreationDate);
        }
        {
            JAXBElement<XMLGregorianCalendar> theModificationDate;
            theModificationDate = this.getModificationDate();
            toStringBuilder.append("modificationDate", theModificationDate);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }
}