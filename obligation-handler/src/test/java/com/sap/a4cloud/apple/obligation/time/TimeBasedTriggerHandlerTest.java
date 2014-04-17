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

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.a4cloud.apple.obligation.dao.ObligationTriggerDao;
import com.sap.a4cloud.apple.obligation.entity.ObligationTrigger;
import com.sap.research.primelife.dao.PiiDao;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.obligation.impl.Action;
import eu.primelife.ppl.policy.obligation.impl.ActionDeletePersonalData;
import eu.primelife.ppl.policy.obligation.impl.Trigger;
import eu.primelife.ppl.policy.obligation.impl.TriggerAtTime;
import eu.primelife.ppl.policy.obligation.impl.TriggerPeriodic;

public class TimeBasedTriggerHandlerTest {

	private ITimeBasedTriggerHandler timeBasedTriggerHandler;
	@Mock private ITimeBasedTriggerFactory timeBasedTriggerFactory;
	@Mock private ObligationTriggerDao otDao;
	@Mock private PiiDao piiDao;
	private PIIType pii;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		timeBasedTriggerHandler = new TimeBasedTriggerHandler(
				otDao, piiDao, timeBasedTriggerFactory);

		pii = new PIIType();
		pii.setAttributeName("attrName");
		pii.setAttributeValue("attrValue");
		pii.setHjid((long) 0);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests {@link TimeBasedTriggerHandler#handle(Trigger, Action, PIIType)}
	 * for an obligation with {@link TriggerAtTime}.
	 * {@link TimeBasedTriggerHandler} should call
	 * {@link TimeBasedTriggerFactory#makeTimeBasedTrigger(Trigger, Action, PIIType)}.
	 * {@link TimeBasedTriggerHandler} should call
	 * {@link TimeBasedTriggerAtTime#start()}.
	 */
	@Test
	public void testHandleObligationWithTriggerAtTime() {
		ObligationTrigger ot = new ObligationTrigger();
		ot.setId(0);
		ot.setPiiId((long) 0);
		ActionDeletePersonalData action = new ActionDeletePersonalData();
		ot.setAction(action);
		TriggerAtTime trigger = new TriggerAtTime();
		trigger.setHjid((long) 0);
		trigger.setName("trigger-0");
		ot.setTrigger(trigger);
		ot.setTriggerName(
				"{http://www.primelife.eu/ppl/obligation}TriggerAtTime");

		TimeBasedTriggerAtTime triggerAtTime =
				mock(TimeBasedTriggerAtTime.class);
		when(timeBasedTriggerFactory.makeTimeBasedTrigger(trigger, action, pii))
		.thenReturn(triggerAtTime);

		timeBasedTriggerHandler.handle(trigger, action, pii);

		verify(timeBasedTriggerFactory).makeTimeBasedTrigger(
				trigger, action, pii);
		verify(triggerAtTime).start();
		verify(triggerAtTime, never()).cancel();
		verifyZeroInteractions(otDao);
	}

	/**
	 * Tests {@link TimeBasedTriggerHandler#unhandle(Trigger, Long)} for
	 * an obligation with {@link TriggerPeriodic}.
	 * {@link TimeBasedTriggerHandler} should call
	 * {@link TimeBasedTriggerFactory#makeTimeBasedTrigger(Trigger, Action, PIIType)}.
	 * {@link TimeBasedTriggerHandler} should call
	 * {@link TimeBasedTriggerPeriodic#start()}.
	 */
	@Test
	public void testHandleObligationWithTriggerPeriodic() {
		ObligationTrigger ot = new ObligationTrigger();
		ot.setId(0);
		ot.setPiiId((long) 0);
		ActionDeletePersonalData action = new ActionDeletePersonalData();
		ot.setAction(action);
		TriggerPeriodic trigger = new TriggerPeriodic();
		trigger.setHjid((long) 1);
		trigger.setName("trigger-1");
		ot.setTrigger(trigger);
		ot.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerPeriodic");

		TimeBasedTriggerPeriodic triggerPeriodic = mock(TimeBasedTriggerPeriodic.class);
		when(timeBasedTriggerFactory.makeTimeBasedTrigger(trigger, action, pii))
		.thenReturn(triggerPeriodic);

		timeBasedTriggerHandler.handle(trigger, action, pii);

		verify(timeBasedTriggerFactory).makeTimeBasedTrigger(
				trigger, action, pii);
		verify(triggerPeriodic).start();
		verify(triggerPeriodic, never()).cancel();
		verifyZeroInteractions(otDao);
	}

	/**
	 * Tests start when n obligations are already in the obligations handler.
	 * {@link TimeBasedTriggerHandler} should call
	 * {@link ObligationTriggerDao#findObjects(Class)}.
	 * {@link TimeBasedTriggerHandler} should call
	 * {@link TimeBasedTriggerFactory#makeTimeBasedTrigger(Trigger, Action, PIIType)}.
	 * {@link TimeBasedTriggerHandler} should call
	 * {@link TimeBasedTriggerPeriodic#start()}.
	 */
	@Test
	public void testStartWithObligations() {
		ObligationTrigger ot0 = new ObligationTrigger();
		ot0.setId(0);
		ot0.setPiiId((long) 0);
		ActionDeletePersonalData action0 = new ActionDeletePersonalData();
		ot0.setAction(action0);
		TriggerPeriodic trigger0 = new TriggerPeriodic();
		trigger0.setHjid((long) 2);
		trigger0.setName("trigger-2");
		ot0.setTrigger(trigger0);
		ot0.setTriggerName(
				"{http://www.primelife.eu/ppl/obligation}TriggerPeriodic");

		ObligationTrigger ot1 = new ObligationTrigger();
		ot1.setId(0);
		ot1.setPiiId((long) 0);
		ActionDeletePersonalData action1 = new ActionDeletePersonalData();
		ot1.setAction(action1);
		TriggerAtTime trigger1 = new TriggerAtTime();
		ot1.setTrigger(trigger1);
		trigger1.setHjid((long) 3);
		trigger1.setName("trigger-3");
		ot1.setTriggerName(
				"{http://www.primelife.eu/ppl/obligation}TriggerAtTime");

		List<ObligationTrigger> otList = new ArrayList<ObligationTrigger>();
		otList.add(ot0);
		otList.add(ot1);

		TimeBasedTriggerPeriodic triggerPeriodic =
				mock(TimeBasedTriggerPeriodic.class);
		TimeBasedTriggerAtTime triggerAtTime =
				mock(TimeBasedTriggerAtTime.class);

		when(timeBasedTriggerFactory
				.makeTimeBasedTrigger(trigger0, action0, pii))
		.thenReturn(triggerPeriodic);
		when(timeBasedTriggerFactory
				.makeTimeBasedTrigger(trigger1, action1, pii))
		.thenReturn(triggerAtTime);
		when(otDao.findObjects(ObligationTrigger.class))
		.thenReturn(otList);
		when(piiDao.findObject(PIIType.class, (long) 0)).thenReturn(pii);

		timeBasedTriggerHandler.start();

		verify(otDao).findObjects(ObligationTrigger.class);
		verify(piiDao, times(2)).findObject(PIIType.class, (long) 0);
		verify(triggerPeriodic).start();
		verify(triggerAtTime).start();
	}

	/**
	 * Tests {@link TimeBasedTriggerHandler#start when no obligations that were
	 * already stored in the obligation handler.
	 * {@link TimeBasedTriggerHandler} should call
	 * {@link ObligationTriggerDao#findObjects(Class)}.
	 * {@link TimeBasedTriggerHandler} should have no interaction with
	 * {@link TimeBasedTriggerFactory}.
	 */
	@Test
	public void testStartWithNoObligations() {
		List<ObligationTrigger> listOeeStatus = new ArrayList<ObligationTrigger>();

		when(otDao.findObjects(ObligationTrigger.class)).thenReturn(listOeeStatus);

		timeBasedTriggerHandler.start();

		verify(otDao).findObjects(ObligationTrigger.class);
		verifyZeroInteractions(timeBasedTriggerFactory);
	}

	/**
	 * Tests {@link TimeBasedTriggerHandler#stop()} when n obligations are
	 * already managed.
	 * {@link TimeBasedTriggerHandler} should call cancel on every
	 * {@link TimeBasedTrigger} object it manages.
	 */
	@Test
	public void testStopWithObligations() {
		ObligationTrigger ot0 = new ObligationTrigger();
		ot0.setId(0);
		ot0.setPiiId((long) 0);
		ActionDeletePersonalData action0 = new ActionDeletePersonalData();
		ot0.setAction(action0);
		TriggerPeriodic trigger0 = new TriggerPeriodic();
		trigger0.setHjid((long) 4);
		trigger0.setName("trigger-4");
		ot0.setTrigger(trigger0);
		ot0.setTriggerName(
				"{http://www.primelife.eu/ppl/obligation}TriggerPeriodic");

		ObligationTrigger ot1 = new ObligationTrigger();
		ot1.setId(0);
		ot1.setPiiId((long) 0);
		ActionDeletePersonalData action1 = new ActionDeletePersonalData();
		ot1.setAction(action1);
		TriggerAtTime trigger1 = new TriggerAtTime();
		trigger1.setHjid((long) 5);
		trigger1.setName("trigger-5");
		ot1.setTrigger(trigger1);
		ot1.setTriggerName(
				"{http://www.primelife.eu/ppl/obligation}TriggerAtTime");

		TimeBasedTriggerPeriodic triggerPeriodic =
				mock(TimeBasedTriggerPeriodic.class);
		TimeBasedTriggerAtTime triggerAtTime =
				mock(TimeBasedTriggerAtTime.class);

		when(timeBasedTriggerFactory
				.makeTimeBasedTrigger(trigger0, action0, pii))
		.thenReturn(triggerPeriodic);
		when(timeBasedTriggerFactory
				.makeTimeBasedTrigger(trigger1, action1, pii))
		.thenReturn(triggerAtTime);

		timeBasedTriggerHandler.handle(trigger0, action0, pii);
		timeBasedTriggerHandler.handle(trigger1, action1, pii);
		timeBasedTriggerHandler.stop();

		verify(triggerPeriodic).cancel();
		verify(triggerAtTime).cancel();
	}

	/**
	 * Tests {@link TimeBasedTriggerHandler#stop()} when no obligations are
	 * managed.
	 * Expects no interaction with any other component.
	 */
	@Test
	public void testStopWithNoObligation() {
		TimeBasedTriggerPeriodic triggerPeriodic =
				mock(TimeBasedTriggerPeriodic.class);
		TimeBasedTriggerAtTime triggerAtTime =
				mock(TimeBasedTriggerAtTime.class);

		timeBasedTriggerHandler.stop();

		verifyZeroInteractions(triggerPeriodic);
		verifyZeroInteractions(triggerAtTime);
		verifyZeroInteractions(timeBasedTriggerFactory);
		verifyZeroInteractions(otDao);
	}

}
