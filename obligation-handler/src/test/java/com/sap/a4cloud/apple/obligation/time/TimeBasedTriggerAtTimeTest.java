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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.a4cloud.apple.obligation.action.Action;
import com.sap.a4cloud.apple.obligation.action.ActionFactory;
import com.sap.a4cloud.apple.obligation.action.IActionHandler;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.obligation.impl.ActionDeletePersonalData;
import eu.primelife.ppl.policy.obligation.impl.DateAndTime;
import eu.primelife.ppl.policy.obligation.impl.Duration;
import eu.primelife.ppl.policy.obligation.impl.TriggerAtTime;

public class TimeBasedTriggerAtTimeTest {

	@Mock private IActionHandler actionHandler;
	@Mock private ActionFactory actionFactory;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	private PIIType createPii(String attrName, String attrValue, Long id) {
		PIIType pii = new PIIType();
		pii.setAttributeName(attrName);
		pii.setAttributeValue(attrValue);
		pii.setHjid(id);
		return pii;
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests a single execution of a {@link TimeBasedTriggerAtTime} trigger.
	 * {@link TimeBasedTriggerAtTime} should not interact with
	 * {@link IActionHandler} within n seconds after the start.
	 * After n seconds {@link TimeBasedTriggerAtTime} should call
	 * {@link IActionHandler#handle(Action)}.
	 */
	@Test
	public void testSingleTriggerAtTime() {
		PIIType pii = createPii("name", "Jimmy", (long) 1);

		TriggerAtTime triggerAtTime = new TriggerAtTime();
		DateAndTime dateAndTime = new DateAndTime();
		triggerAtTime.setStart(dateAndTime);
		dateAndTime.setStartNowObject("<StartNow/>");
		Duration duration = new Duration();
		triggerAtTime.setMaxDelay(duration);
		duration.setDuration("P0Y0M0DT0H0M10S");	// 10 sec

		ActionDeletePersonalData action = new ActionDeletePersonalData();

		TimeBasedTriggerAtTime timeBasedTriggerAtTime0 =
				new TimeBasedTriggerAtTime(
						triggerAtTime, action, pii, actionHandler,
						actionFactory);
		timeBasedTriggerAtTime0.start();

		try {
			for (int i = 0; i < 10; i++) {
				verify(actionHandler, never()).handle(any(Action.class));
				Thread.sleep(1000);
			}

			Thread.sleep(1000);
			verify(actionFactory).createAction(
					eq(pii), any(String.class), eq(action));
			verify(actionHandler).handle(any(Action.class));
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Time-based trigger interrupted");
		}
	}

	/**
	 * Tests the execution of multiple {@link TimeBasedTriggerAtTime} triggers.
	 * {@link TimeBasedTriggerAtTime} should not interact with
	 * {@link IActionHandler} within s seconds after the start.
	 * After s seconds {@link TimeBasedTriggerAtTime} should call
	 * {@link IActionHandler#handle(Action)} n times.
	 */
	@Test
	public void testMultipleTriggersAtTime() {
		PIIType pii = createPii("date-of-birth", "01/01/1964", (long) 2);

		TriggerAtTime triggerAtTime = new TriggerAtTime();
		DateAndTime dateAndTime = new DateAndTime();
		triggerAtTime.setStart(dateAndTime);
		dateAndTime.setStartNowObject("<StartNow/>");
		Duration duration = new Duration();
		triggerAtTime.setMaxDelay(duration);
		duration.setDuration("P0Y0M0DT0H0M10S");

		ActionDeletePersonalData action = new ActionDeletePersonalData();

		TimeBasedTriggerAtTime timeBasedTriggerAtTime0 =
				new TimeBasedTriggerAtTime(
						triggerAtTime, action, pii, actionHandler,
						actionFactory);
		TimeBasedTriggerAtTime timeBasedTriggerAtTime1 =
				new TimeBasedTriggerAtTime(
						triggerAtTime, action, pii, actionHandler,
						actionFactory);
		TimeBasedTriggerAtTime timeBasedTriggerAtTime2 =
				new TimeBasedTriggerAtTime(
						triggerAtTime, action, pii, actionHandler,
						actionFactory);
		TimeBasedTriggerAtTime timeBasedTriggerAtTime3 =
				new TimeBasedTriggerAtTime(
						triggerAtTime, action, pii, actionHandler,
						actionFactory);
		TimeBasedTriggerAtTime timeBasedTriggerAtTime4 =
				new TimeBasedTriggerAtTime(
						triggerAtTime, action, pii, actionHandler,
						actionFactory);

		timeBasedTriggerAtTime0.start();
		timeBasedTriggerAtTime1.start();
		timeBasedTriggerAtTime2.start();
		timeBasedTriggerAtTime3.start();
		timeBasedTriggerAtTime4.start();

		try {
			for(int i = 0; i < 10; i++) {
				verify(actionHandler, never()).handle(any(Action.class));
				Thread.sleep(1000);
			}

			Thread.sleep(1000);
			verify(actionHandler, times(5)).handle(any(Action.class));
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Time-based trigger interrupted");
		}
	}

	/**
	 * Tests a cancellation execution of a {@link TimeBasedTriggerAtTime} trigger.
	 * {@link TimeBasedTriggerAtTime} should not interact with
	 * {@link IActionHandler} within n seconds after the start.
	 * After n seconds {@link TimeBasedTriggerAtTime} should call
	 * {@link IActionHandler#handle(Action)}.
	 */
	@Test
	public void testCancelTriggerAtTime() {
		PIIType pii = createPii("SSN", "007", (long) 3);

		TriggerAtTime triggerAtTime = new TriggerAtTime();
		DateAndTime dateAndTime = new DateAndTime();
		triggerAtTime.setStart(dateAndTime);
		dateAndTime.setStartNowObject("<StartNow/>");
		Duration duration = new Duration();
		triggerAtTime.setMaxDelay(duration);
		duration.setDuration("P0Y0M0DT0H0M10S");

		ActionDeletePersonalData action = new ActionDeletePersonalData();

		TimeBasedTriggerAtTime timeBasedTriggerAtTime0 =
				new TimeBasedTriggerAtTime(
						triggerAtTime, action, pii, actionHandler,
						actionFactory);
		timeBasedTriggerAtTime0.start();

		try {
			Thread.sleep(5000);
			timeBasedTriggerAtTime0.cancel();
			Thread.sleep(5000);
			verifyZeroInteractions(actionHandler);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail("Time-based trigger interrupted");
		}
	}

}
