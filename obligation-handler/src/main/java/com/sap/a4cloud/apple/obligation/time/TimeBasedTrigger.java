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
package com.sap.a4cloud.apple.obligation.time;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.a4cloud.apple.obligation.action.ActionHandler;
import com.sap.a4cloud.apple.obligation.action.IActionHandler;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.obligation.impl.Action;
import eu.primelife.ppl.policy.obligation.impl.DateAndTime;

public abstract class TimeBasedTrigger implements ITimeBasedTrigger{

	private static final Logger LOGGER =
			LoggerFactory.getLogger(TimeBasedTrigger.class);

	protected Action action;
	protected PIIType pii;
	protected IActionHandler actionHandler;
	protected Timer timer;
	protected TimerTask timerTask;

	protected TimeBasedTrigger(Action action, PIIType pii) {
		this(action, pii, new ActionHandler());
	}

	protected TimeBasedTrigger(Action action, PIIType pii,
			IActionHandler actionHandler) {
		this.action = action;
		this.pii = pii;
		this.actionHandler = actionHandler;
		timer = new Timer();
	}

	public synchronized void cancel() {
		LOGGER.info("Time-based trigger for PII {} canceled", pii.getHjid());
		timer.cancel();

		if (timerTask != null) {
			timerTask.cancel();
		}
	} 
	
	public abstract void start();

	protected Long toMilliseconds(DateAndTime date) {
		XMLGregorianCalendar xmlGregorianDate = date.getDateAndTime();

		if (checkXmlGregorianDate(xmlGregorianDate)) {
			Date startDate = toDate(xmlGregorianDate);
			return startDate.getTime();
		}

		return null;
	}

	private Date toDate(XMLGregorianCalendar xmlGregorianDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(xmlGregorianDate.getYear(), 
				xmlGregorianDate.getMonth() - 1, // XMLGregorianCalendar month: 1-12, Calendar month: 0-11
				xmlGregorianDate.getDay(), 
				xmlGregorianDate.getHour(), 
				xmlGregorianDate.getMinute(), 
				xmlGregorianDate.getSecond());

		return calendar.getTime();
	}

	private boolean checkXmlGregorianDate(XMLGregorianCalendar xmlGregorianDate) {
		return xmlGregorianDate.getYear() != DatatypeConstants.FIELD_UNDEFINED
				&& xmlGregorianDate.getMonth() != DatatypeConstants.FIELD_UNDEFINED
				&& xmlGregorianDate.getDay() != DatatypeConstants.FIELD_UNDEFINED
				&& xmlGregorianDate.getHour() != DatatypeConstants.FIELD_UNDEFINED
				&& xmlGregorianDate.getMinute() != DatatypeConstants.FIELD_UNDEFINED
				&& xmlGregorianDate.getSecond() != DatatypeConstants.FIELD_UNDEFINED;
	}

}
