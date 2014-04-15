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

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.a4cloud.apple.obligation.dao.ObligationTriggerDao;
import com.sap.a4cloud.apple.obligation.entity.ObligationTrigger;
import com.sap.research.primelife.dao.PiiDao;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.obligation.impl.Action;
import eu.primelife.ppl.policy.obligation.impl.Trigger;

public class TimeBasedTriggerHandler implements ITimeBasedTriggerHandler {

	private final static Logger LOGGER =
			LoggerFactory.getLogger(TimeBasedTriggerHandler.class);

	private static ITimeBasedTriggerHandler instance = null;
	private ObligationTriggerDao obligationTriggerDao;
	private PiiDao piiDao;
	private ITimeBasedTriggerFactory factory = new TimeBasedTriggerFactory();
	private HashMap<Integer, ITimeBasedTrigger> timeBasedTriggers;

	public static ITimeBasedTriggerHandler getInstance(){
		if(instance == null){
			instance = new TimeBasedTriggerHandler();
		}
		return instance;
	}

	protected TimeBasedTriggerHandler() {
		this(new ObligationTriggerDao(), new PiiDao(),
				new TimeBasedTriggerFactory());
		timeBasedTriggers = new HashMap<Integer, ITimeBasedTrigger>();
	}

	protected TimeBasedTriggerHandler(ObligationTriggerDao obligationTriggerDao,
			PiiDao piiDao, ITimeBasedTriggerFactory factory) {
		this.obligationTriggerDao = obligationTriggerDao;
		this.piiDao = piiDao;
		this.factory = factory;
		timeBasedTriggers = new HashMap<Integer, ITimeBasedTrigger>();
	}

	@Override
	public void start() {
		LOGGER.info("Starting time-based triggers");
		// re-handle the obligations
		List<ObligationTrigger> obligationTriggerList =
				obligationTriggerDao.findObjects(ObligationTrigger.class);

		for (ObligationTrigger obligationTrigger: obligationTriggerList) {
			long piiId = obligationTrigger.getPiiId();
			PIIType pii = piiDao.findObject(PIIType.class, piiId);

			if (pii != null) {
				Trigger trigger = obligationTrigger.getTrigger();
				Action action = obligationTrigger.getAction();
				handle(trigger, action, pii);
			}
			else {
				LOGGER.warn("PII {} associated with a time-based trigger could not be found", piiId);
				obligationTriggerDao.deleteObject(obligationTrigger);
			}
		}
	}

	@Override
	public void stop() {
		LOGGER.info("Stopping time-based triggers");

		for (Entry<Integer, ITimeBasedTrigger> entry : timeBasedTriggers
				.entrySet()) {
			entry.getValue().cancel();
		}

		timeBasedTriggers.clear();
	}

	@Override
	public void handle(Trigger trigger, Action action, PIIType pii) {
		LOGGER.info("Handling time-based trigger {} for PII {}",
				trigger.getName(), pii.getHjid());
		ITimeBasedTrigger timeBasedTrigger =
				factory.makeTimeBasedTrigger(trigger, action, pii);
		int hashCode = triggerHashCode(trigger, pii.getHjid());
		timeBasedTriggers.put(hashCode, timeBasedTrigger);
		timeBasedTrigger.start();
	}

	@Override
	public void unhandle(Trigger trigger, Long piiId) {
		LOGGER.info("Removing time-based trigger {} for PII {}",
				trigger.getName(), piiId);
		int hashCode = triggerHashCode(trigger, piiId);
		ITimeBasedTrigger timeBasedTrigger = timeBasedTriggers.get(hashCode);

		if (timeBasedTrigger != null) {
			timeBasedTrigger.cancel();
			timeBasedTriggers.remove(hashCode);
		}
	}

	/**
	 * Helper method to calculate hash set key for a given trigger.
	 *
	 * @param trigger
	 * @param piiId
	 * @return	the hash code
	 */
	private int triggerHashCode(Trigger trigger, Long piiId) {
		return piiId.hashCode() ^ (trigger.getHjid().hashCode() >>> 32);
	}

}
