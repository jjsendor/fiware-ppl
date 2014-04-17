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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.a4cloud.apple.obligation.action.ActionFactory;
import com.sap.a4cloud.apple.obligation.action.DeleteAction;
import com.sap.a4cloud.apple.obligation.action.IActionHandler;
import com.sap.a4cloud.apple.obligation.action.LogAction;
import com.sap.a4cloud.apple.obligation.entity.ObligationTrigger;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.obligation.impl.ActionDeletePersonalData;
import eu.primelife.ppl.policy.obligation.impl.DateAndTime;
import eu.primelife.ppl.policy.obligation.impl.Duration;
import eu.primelife.ppl.policy.obligation.impl.TriggerPeriodic;

public class TimeBasedTriggerPeriodicTest {

	@Mock private IActionHandler actionHandler;
	@Mock private ActionFactory actionFactory;
	private PIIType pii;
	private ObligationTrigger ot;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		pii = new PIIType();
		pii.setAttributeName("attrName");
		pii.setAttributeValue("attrValue");
		pii.setHjid((long) 0);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests {@link TimeBasedTriggerPeriodic} with a start time set to now.
	 * {@link TimeBasedTriggerPeriodic} should tick every n seconds between
	 * now and now + t seconds and should tick t/n times.
	 */
	@Test
	public void testSingleTriggerPeriodicStartNow() {
		TriggerPeriodic triggerPeriodic = new TriggerPeriodic();

		Duration maxDelay = new Duration();
		maxDelay.setDuration("P0Y0M0DT0H0M1S");
		Duration period = new Duration();
		period.setDuration("P0Y0M0DT0H0M1S");
		DateAndTime start = new DateAndTime();
		start.setStartNowObject("<StartNow/>");
		DateAndTime end = new DateAndTime();
		XMLGregorianCalendar endDate = null;

		try {
			GregorianCalendar c = new GregorianCalendar();
			Long l = System.currentTimeMillis() + ((long) 5000);
			c.setTime(new Date(l));
			endDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			fail("Datatype configuration exception");
		}

		end.setDateAndTime(endDate);

		triggerPeriodic.setMaxDelay(maxDelay);
		triggerPeriodic.setPeriod(period);
		triggerPeriodic.setStart(start);
		triggerPeriodic.setEnd(end);

		ot = new ObligationTrigger();
		ot.setId(0);
		ot.setPiiId((long) 0);
		ActionDeletePersonalData action = new ActionDeletePersonalData();
		ot.setAction(action);
		ot.setTrigger(triggerPeriodic);
		ot.setTriggerName(
				"{http://www.primelife.eu/ppl/obligation}TriggerPeriodic");

		LogAction logAction = mock(LogAction.class);
		when(actionFactory.createAction(eq(pii), any(String.class), eq(action)))
			.thenReturn(logAction);

		TimeBasedTriggerPeriodic timeBasedTriggerPeriodic =
				new TimeBasedTriggerPeriodic(triggerPeriodic, action, pii,
						actionHandler, actionFactory);
		timeBasedTriggerPeriodic.start();

		try {
			Thread.sleep(30000);
			timeBasedTriggerPeriodic.cancel();
			verify(actionFactory, org.mockito.Mockito.atLeast(4)).createAction(
					eq(pii), any(String.class), eq(action));
			verify(actionHandler, org.mockito.Mockito.atLeast(4)).handle(
					logAction);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Error Interrupted Thread");
		}

		timeBasedTriggerPeriodic.cancel();
	}

	/**
	 * Tests {@link TimeBasedTriggerPeriodic} with a start time set to now.
	 * {@link TimeBasedTriggerPeriodic} should tick every n seconds between
	 * now and now + t seconds and should tick t/n times.
	 */
	@Test
	public void testSingleTriggerPeriodicStartLater() {
		TriggerPeriodic triggerPeriodic = new TriggerPeriodic();

		Duration maxDelay = new Duration();
		maxDelay.setDuration("P0Y0M0DT0H0M1S");
		Duration period = new Duration();
		period.setDuration("P0Y0M0DT0H0M1S");
		DateAndTime start = new DateAndTime();
		XMLGregorianCalendar startDate = null;

		try {
			GregorianCalendar c = new GregorianCalendar();
			Long l = System.currentTimeMillis() + ((long) 2000);
			c.setTime(new Date(l));
			startDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			fail("Datatype configuration exception");
		}

		start.setDateAndTime(startDate);

		DateAndTime end = new DateAndTime();
		XMLGregorianCalendar endDate = null;

		try {
			GregorianCalendar c = new GregorianCalendar();
			Long l = System.currentTimeMillis() + ((long) 6000);
			c.setTime(new Date(l));
			endDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			fail("Datatype configuration exception");
		}

		end.setDateAndTime(endDate);

		triggerPeriodic.setMaxDelay(maxDelay);
		triggerPeriodic.setPeriod(period);
		triggerPeriodic.setStart(start);
		triggerPeriodic.setEnd(end);

		ot = new ObligationTrigger();
		ot.setId(0);
		ot.setPiiId((long) 0);
		ActionDeletePersonalData action = new ActionDeletePersonalData();
		ot.setAction(action);
		ot.setTrigger(triggerPeriodic);
		ot.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerPeriodic");

		DeleteAction deleteAction = mock(DeleteAction.class);
		when(actionFactory.createAction(eq(pii), any(String.class), eq(action)))
			.thenReturn(deleteAction);
		TimeBasedTriggerPeriodic timeBasedTriggerPeriodic =
				new TimeBasedTriggerPeriodic(triggerPeriodic, action, pii,
						actionHandler, actionFactory);
		timeBasedTriggerPeriodic.start();

		try {
			Thread.sleep(30000 + 10000);
			timeBasedTriggerPeriodic.cancel();
			verify(actionFactory, org.mockito.Mockito.atLeast(3)).createAction(
					eq(pii), any(String.class), eq(action));
			verify(actionHandler, org.mockito.Mockito.atLeast(3)).handle(
					deleteAction);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Error Interrupted Thread");
		}

		timeBasedTriggerPeriodic.cancel();
	}

}
