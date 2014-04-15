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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.a4cloud.apple.obligation.dao.ObligationTriggerDao;
import com.sap.a4cloud.apple.obligation.entity.ObligationTrigger;
import com.sap.a4cloud.apple.obligation.time.ITimeBasedTriggerHandler;
import com.sap.a4cloud.apple.obligation.time.TimeBasedTriggerHandler;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.obligation.impl.Action;
import eu.primelife.ppl.policy.obligation.impl.Obligation;
import eu.primelife.ppl.policy.obligation.impl.ObligationsSet;
import eu.primelife.ppl.policy.obligation.impl.Trigger;
import eu.primelife.ppl.policy.obligation.impl.TriggerAtTime;
import eu.primelife.ppl.policy.obligation.impl.TriggerPeriodic;
import eu.primelife.ppl.policy.obligation.impl.TriggersSetTriggerItem;

public class ObligationHandler implements IObligationHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(
			ObligationHandler.class);

	private ObligationTriggerDao oeeStatusDao;
	private ITimeBasedTriggerHandler timeBasedTriggerHandler;
	private static ObligationHandler instance = null;

	public static ObligationHandler getInstance() {
		if (instance == null) {
			instance = new ObligationHandler();
		}

		return instance;
	}

	protected ObligationHandler() {
		oeeStatusDao = new ObligationTriggerDao();
		timeBasedTriggerHandler = TimeBasedTriggerHandler.getInstance();
	}

	protected ObligationHandler(ObligationTriggerDao oeeStatusDao, ITimeBasedTriggerHandler timeBasedTriggerHandler) {
		this.oeeStatusDao = oeeStatusDao;
		this.timeBasedTriggerHandler = timeBasedTriggerHandler;
	}

	public void addObligations(ObligationsSet obligationSet, PIIType pii) {
		for (Obligation obligation: obligationSet.getObligation()) {
			addObligation(obligation, pii);
		}
	}

	public void removeObligations(PIIType pii) {
		List<ObligationTrigger> oeeStatusList = oeeStatusDao.findByPiiId(pii.getHjid());

		// remove each obligation trigger one by one
		for (ObligationTrigger oeeStatus : oeeStatusList){
			deleteObligation(oeeStatus);
		}
	}

	private void deleteObligation(ObligationTrigger oeeStatus) {
		Trigger trigger = oeeStatus.getTrigger();
		Long piiId = oeeStatus.getPiiId();

		if (isTimeBased(trigger)) {
			timeBasedTriggerHandler.unhandle(trigger, piiId);
		}

		oeeStatusDao.deleteObject(oeeStatus);
	}

	private boolean isTimeBased(Trigger trigger) {
		return trigger instanceof TriggerAtTime
				|| trigger instanceof TriggerPeriodic;
	}

	private void addObligation(Obligation obligation, PIIType pii) {
		// extract the list of triggers
		List<TriggersSetTriggerItem> triggerItems =
				obligation.getTriggersSet().getTriggerItems();

		for (TriggersSetTriggerItem triggerItem : triggerItems){
			Trigger trigger = triggerItem.getItemValue();
			Action action = obligation.getActionValue();

			LOGGER.info("Adding obligation trigger {} for PII {}",
					trigger.getName(), pii.getHjid());

			// create and persist the obligation trigger entity
			ObligationTrigger oeeStatus = new ObligationTrigger();
			oeeStatus.setPiiId(pii.getHjid());
			oeeStatus.setTriggerName(trigger.getName());
			oeeStatus.setAction(action);
			oeeStatus.setTrigger(trigger);

			oeeStatusDao.persistObject(oeeStatus);

			// configure time-based trigger handler
			if (isTimeBased(trigger)) {
				timeBasedTriggerHandler.handle(trigger, action, pii);
			}
		}
	}

}
