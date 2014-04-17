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
package com.sap.a4cloud.apple.obligation;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.a4cloud.apple.obligation.dao.ObligationTriggerDao;
import com.sap.a4cloud.apple.obligation.entity.ObligationTrigger;
import com.sap.a4cloud.apple.obligation.time.ITimeBasedTriggerHandler;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.obligation.impl.Action;
import eu.primelife.ppl.policy.obligation.impl.ActionDeletePersonalData;
import eu.primelife.ppl.policy.obligation.impl.DateAndTime;
import eu.primelife.ppl.policy.obligation.impl.Duration;
import eu.primelife.ppl.policy.obligation.impl.ObjectFactory;
import eu.primelife.ppl.policy.obligation.impl.Obligation;
import eu.primelife.ppl.policy.obligation.impl.ObligationsSet;
import eu.primelife.ppl.policy.obligation.impl.Trigger;
import eu.primelife.ppl.policy.obligation.impl.TriggerAtTime;
import eu.primelife.ppl.policy.obligation.impl.TriggerPeriodic;
import eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataSent;
import eu.primelife.ppl.policy.obligation.impl.TriggersSet;
import eu.primelife.ppl.policy.obligation.impl.TriggersSetTriggerItem;

public class ObligationHandlerTest {

	private IObligationHandler obligationHandler;
	@Mock private ObligationTriggerDao otDao;
	@Mock private ITimeBasedTriggerHandler timeBasedTriggerHandler;

	private PIIType pii;

	private ObjectFactory ofObligation = new ObjectFactory();

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		obligationHandler = new ObligationHandler(otDao,
				timeBasedTriggerHandler);

		pii = new PIIType();
		pii.setAttributeName("attrName");
		pii.setAttributeValue("attrValue");
		pii.setHjid((long) 0);
	}

	@After
	public void tearDown() throws Exception {
	}

	private class IsSameObligationTrigger
			extends ArgumentMatcher<ObligationTrigger> {
		private ObligationsSet obligationSet;

		public IsSameObligationTrigger(ObligationsSet obligationSet) {
			this.obligationSet = obligationSet;
		}

		public boolean matches(Object obj) {
			ObligationTrigger ot = (ObligationTrigger) obj;

			for (Obligation ob : obligationSet.getObligation()) {
				String actualTriggerName = ot.getTriggerName();
				String expectedTriggerName = ob.getTriggersSet().getTrigger().get(0).getName().toString();

				Trigger actualTrigger = ot.getTrigger();
				Trigger expectedTrigger = ob.getTriggersSet().getTrigger().get(0).getValue();

				if (ot.getAction().equals(ob.getActionValue())
						&& actualTriggerName.equals(expectedTriggerName)
						&& actualTrigger.equals(expectedTrigger)) {
					return true;
				}
			}

			return false;
		}
	}

	/**
	 * Test add n obligations with no time based trigger on an existing Pii
	 * The Obligation Handler should call n times persistObject on ObligationTriggerDao, no interaction on Time Based Triggers Handler
	 */
	@Test
	public void testAddObligationsNoTimeBasedTriggers() {
		final ObligationsSet obligationSet = new ObligationsSet();
		obligationSet.setObligation(new ArrayList<Obligation>());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());

		obligationHandler.addObligations(obligationSet, pii);

		verify(otDao, times(3)).persistObject(
				argThat(new IsSameObligationTrigger(obligationSet)));
		verifyZeroInteractions(timeBasedTriggerHandler);
	}

	/**
	 * Test add n obligations with m time based trigger on an existing Pii
	 * The Obligation Handler should call n times persistObject on ObligationTriggerDao, no interaction on Time Based Triggers Handler
	 */
	@Test
	public void testAddObligationsTimeBasedTriggers() {
		final ObligationsSet obligationSet = new ObligationsSet();
		obligationSet.setObligation(new ArrayList<Obligation>());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());
		obligationSet.getObligation().add(createSimpleObligationWithSimpleTrigger());

		final Obligation obligationWithTriggerAtTime = createSimpleObligationWithTriggerAtTime();
		final Obligation obligationWithTriggerPeriodic = createSimpleObligationWithTriggerPeriodic();

		obligationSet.getObligation().add(obligationWithTriggerAtTime);
		obligationSet.getObligation().add(obligationWithTriggerPeriodic);

		class TimeBasedTriggerMatcher extends ArgumentMatcher<Trigger> {
			public boolean matches(Object obj) {
				Trigger trigger = (Trigger) obj;

				return trigger.equals(obligationWithTriggerAtTime.getTriggersSet().getTrigger().get(0).getValue())
						|| trigger.equals(obligationWithTriggerPeriodic.getTriggersSet().getTrigger().get(0).getValue());
			}
		}

		class ActionTimeBasedTriggerMatcher extends ArgumentMatcher<Action> {
			public boolean matches(Object obj) {
				Action action = (Action) obj;

				return action.equals(obligationWithTriggerAtTime.getActionValue())
						|| action.equals(obligationWithTriggerPeriodic.getActionValue());
			}
		}

		obligationHandler.addObligations(obligationSet, pii);

		verify(otDao, times(5)).persistObject(
				argThat(new IsSameObligationTrigger(obligationSet)));
		verify(timeBasedTriggerHandler, times(2)).handle(argThat(new TimeBasedTriggerMatcher()), argThat(new ActionTimeBasedTriggerMatcher()), eq(pii));
	}

	/**
	 * Test add 0 obligations with no time based trigger on an existing Pii
	 * The Obligation Handler should call 0 times persistObject on ObligationTriggerDao, no interaction on Time Based Triggers Handler
	 */
	@Test
	public void testAddObligationsEmptyObligationSet() {
		final ObligationsSet obligationSet = new ObligationsSet();
		obligationSet.setObligation(new ArrayList<Obligation>());

		obligationHandler.addObligations(obligationSet, pii);

		verifyZeroInteractions(otDao);
		verifyZeroInteractions(timeBasedTriggerHandler);
	}

	/**
	 * Test deletion With No Obligation
	 * The Obligation Handler should not call deleteObject on ObligationTriggerDao 
	 * and have no interaction with the Time Based Trigger Handler
	 */
	@Test
	public void testDeleteObligationsWithNoObligation() {
		List<ObligationTrigger> otList = new ArrayList<ObligationTrigger>();

		when(otDao.findByPiiId((long) 0)).thenReturn(otList);

		obligationHandler.removeObligations(pii);

		verify(otDao).findByPiiId((long) 0);
		verifyZeroInteractions(timeBasedTriggerHandler);
		verify(otDao, never()).deleteObject(
				(ObligationTrigger) anyObject());
	}

	/**
	 * Test deletion with n obligations (no time-based trigger)
	 * The Obligation Handler should call n times deleteObject on ObligationTriggerDaom with the correct ObligationTrigger instances
	 * should have no interaction with the Time Based Trigger Handler
	 */
	@Test
	public void testDeleteObligationsWithObligations() {
		List<ObligationTrigger> otList = new ArrayList<ObligationTrigger>();
		ObligationTrigger ot0 = new ObligationTrigger();
		ot0.setId(0);
		ot0.setPiiId((long) 0);
		ot0.setAction(new Action());
		otList.add(ot0);

		ObligationTrigger ot1 = new ObligationTrigger();
		ot1.setId(1);
		ot1.setPiiId((long) 0);
		ot1.setAction(new Action());
		otList.add(ot1);

		ObligationTrigger ot2 = new ObligationTrigger();
		ot2.setId(2);
		ot2.setPiiId((long) 0);
		ot2.setAction(new Action());
		otList.add(ot2);

		when(otDao.findByPiiId((long) 0)).thenReturn(otList);

		obligationHandler.removeObligations(pii);

		verify(otDao).findByPiiId((long) 0);
		verify(otDao).deleteObject(ot0);
		verify(otDao).deleteObject(ot1);
		verify(otDao).deleteObject(ot2);
		verifyZeroInteractions(timeBasedTriggerHandler);
	}

	/**
	 * Test deletion with n obligations and m containing time-based triggers
	 * The Obligation Handler should call n times deleteObject on ObligationTriggerDao with the correct ObligationTrigger instances
	 * should call m times unHandle on the Time Based Trigger Handler with the correct ObligationTrigger instances
	 */
	@Test
	public void testDeleteObligationsWithTimeBasedObligations() {
		// ----- Test n obligations (with k time based triggers) -----
		List<ObligationTrigger> otList = new ArrayList<ObligationTrigger>();

		ObligationTrigger ot0 = new ObligationTrigger();
		ot0.setId(0);
		ot0.setPiiId((long) 0);
		ot0.setAction(new Action());
		otList.add(ot0);

		ObligationTrigger ot1 = new ObligationTrigger();
		ot1.setId(1);
		ot1.setPiiId((long) 0);
		ot1.setAction(new Action());
		otList.add(ot1);

		ObligationTrigger ot2 = new ObligationTrigger();
		ot2.setId(2);
		ot2.setPiiId((long) 0);
		ot2.setAction(new Action());
		otList.add(ot2);

		ObligationTrigger ot3 = new ObligationTrigger();
		ot3.setId(3);
		ot3.setPiiId((long) 0);
		ot3.setAction(new Action());
		ot3.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerAtTime");
		Trigger triggerAtTime = new TriggerAtTime();
		ot3.setTrigger(triggerAtTime);
		otList.add(ot3);

		ObligationTrigger ot4 = new ObligationTrigger();
		ot4.setId(3);
		ot4.setPiiId((long) 0);
		ot4.setAction(new Action());
		ot4.setTriggerName("{http://www.primelife.eu/ppl/obligation}TriggerPeriodic");
		Trigger triggerPeriodic = new TriggerPeriodic();
		ot4.setTrigger(triggerPeriodic);
		otList.add(ot4);

		when(otDao.findByPiiId((long) 0)).thenReturn(otList);

		obligationHandler.removeObligations(pii);
		verify(otDao).findByPiiId((long) 0);
		verify(otDao).deleteObject(ot0);
		verify(otDao).deleteObject(ot1);
		verify(otDao).deleteObject(ot2);
		verify(otDao).deleteObject(ot3);
		verify(otDao).deleteObject(ot4);
		verify(timeBasedTriggerHandler).unhandle(triggerAtTime, (long) 0);
		verify(timeBasedTriggerHandler).unhandle(triggerPeriodic, (long) 0);
	}

	private Obligation createSimpleObligationWithSimpleTrigger() {
		Obligation obligation = new Obligation();
		ActionDeletePersonalData actionDelete = new ActionDeletePersonalData();
		obligation.setAction(
				ofObligation.createActionDeletePersonalData(actionDelete));

		// TriggerSet element
		TriggersSet triggerSet = new TriggersSet();
		obligation.setTriggersSet(triggerSet);
		List<TriggersSetTriggerItem> triggerItemList =
				new ArrayList<TriggersSetTriggerItem>();
		triggerSet.setTriggerItems(triggerItemList);

		// TriggerAtTime element
		TriggersSetTriggerItem triggerItem = new TriggersSetTriggerItem();
		triggerItemList.add(triggerItem);

		TriggerPersonalDataSent triggerPersonalDataSent =
				new TriggerPersonalDataSent();
		triggerPersonalDataSent.setName(
				"{http://www.primelife.eu/ppl/obligation}TriggerPersonalDataSent");
		Duration duration = new Duration();
		duration.setDuration("P0Y0M0DT0H5M0S");
		triggerPersonalDataSent.setMaxDelay(duration);
		triggerPersonalDataSent.setId("http://www.primelife.eu/ppl/email");
		triggerItem.setItem(ofObligation.createTriggerPersonalDataSent(
				triggerPersonalDataSent));

		return obligation;
	}

	private Obligation createSimpleObligationWithTriggerAtTime() {
		Obligation obligation = new Obligation();
		ActionDeletePersonalData actionDelete = new ActionDeletePersonalData();
		obligation.setAction(
				ofObligation.createActionDeletePersonalData(actionDelete));

		// TriggerSet element
		TriggersSet triggerSet = new TriggersSet();
		obligation.setTriggersSet(triggerSet);
		List<TriggersSetTriggerItem> triggerItemList =
				new ArrayList<TriggersSetTriggerItem>();
		triggerSet.setTriggerItems(triggerItemList);

		// TriggerAtTime element
		TriggersSetTriggerItem triggerItem = new TriggersSetTriggerItem();
		triggerItemList.add(triggerItem);
		TriggerAtTime triggerAtTime = new TriggerAtTime();
		triggerAtTime.setName(
				"{http://www.primelife.eu/ppl/obligation}TriggerAtTime");
		DateAndTime dateAndTime = new DateAndTime();
		triggerAtTime.setStart(dateAndTime);
		dateAndTime.setStartNowObject("<StartNow/>");
		Duration duration = new Duration();
		triggerAtTime.setMaxDelay(duration);
		duration.setDuration("P0Y0M0DT1H0M0S");
		triggerItem.setItem(ofObligation.createTriggerAtTime(triggerAtTime));

		return obligation;
	}

	private Obligation createSimpleObligationWithTriggerPeriodic() {
		Obligation obligation = new Obligation();
		ActionDeletePersonalData actionDelete = new ActionDeletePersonalData();
		obligation.setAction(
				ofObligation.createActionDeletePersonalData(actionDelete));

		// TriggerSet element
		TriggersSet triggerSet = new TriggersSet();
		obligation.setTriggersSet(triggerSet);
		List<TriggersSetTriggerItem> triggerItemList =
				new ArrayList<TriggersSetTriggerItem>();
		triggerSet.setTriggerItems(triggerItemList);

		// TriggerAtTime element
		TriggersSetTriggerItem triggerItem = new TriggersSetTriggerItem();
		triggerItemList.add(triggerItem);
		TriggerPeriodic triggerPeriodic = new TriggerPeriodic();
		triggerPeriodic.setName(
				"{http://www.primelife.eu/ppl/obligation}TriggerPeriodic");
		DateAndTime startDate = new DateAndTime();
		startDate.setStartNowObject("<StartNow/>");
		triggerPeriodic.setStart(startDate);
		Duration duration = new Duration();
		duration.setDuration("P0Y0M0DT1H0M0S");
		triggerPeriodic.setMaxDelay(duration);
		DateAndTime endDate = new DateAndTime();
		endDate.setDateAndTimeItem(new Date());
		triggerPeriodic.setEnd(endDate);

		triggerItem.setItem(
				ofObligation.createTriggerPeriodic(triggerPeriodic));

		return obligation;
	}

}
