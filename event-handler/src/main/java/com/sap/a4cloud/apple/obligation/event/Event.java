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
package com.sap.a4cloud.apple.obligation.event;

import java.util.Date;

/**
 * Interface represents event that triggers obligation in PPL.
 *
 * @author Jakub Sendor
 *
 */
public interface Event {

	/**
	 * Returns trigger name based on PPL schema.
	 *
	 * @return	the trigger name
	 */
	public String getName();

	/**
	 * Returns the id of the PII associated with this event.
	 *
	 * @return	the id of the PII associated with this event
	 */
	public Long getPiiId();

	/**
	 * Sets the id of the PII associated with this event.
	 *
	 * @param piiId	the id of the associated PII
	 */
	public void setPiiId(Long piiId);

	/**
	 * Returns the attribute name of the PII associated with this event.
	 *
	 * @return	the attribute name of the PII associated with this event
	 */
	public String getPiiAttributeName();

	/**
	 * Sets the attribute name of the PII associated with this event.
	 *
	 * @param piiAttributeName	the attribute name of the associated PII
	 */
	public void setPiiAttributeName(String piiAttributeName);

	/**
	 * Returns the owner of the PII associated with this event.
	 *
	 * @return	the owner of the PII associated with this event
	 */
	public String getPiiOwner();

	/**
	 * Sets the id of the PII associated with this event.
	 *
	 * @param piiOwner	the owner of the associated PII
	 */
	public void setPiiOwner(String piiOwner);

	/**
	 * Returns the date when the event occurred.
	 *
	 * @return	the date when the event occurred.
	 */
	public Date getDate();

	/**
	 * Sets the date when the event occurred.
	 *
	 * @param date	indicates when the event occurred
	 */
	public void setDate(Date date);

}
