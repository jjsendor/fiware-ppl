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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.a4cloud.apple.obligation.IObligationHandler;
import com.sap.a4cloud.apple.obligation.ObligationHandler;
import com.sap.a4cloud.apple.obligation.action.ActionFactory;
import com.sap.a4cloud.apple.obligation.action.ActionHandler;
import com.sap.a4cloud.apple.obligation.action.IActionHandler;
import com.sap.a4cloud.apple.obligation.entity.ObligationTrigger;
import com.sap.research.primelife.dao.PiiDao;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.obligation.impl.Action;
import eu.primelife.ppl.policy.obligation.impl.Trigger;

/**
 * @author Jakub Sendor
 *
 */
public class EventHandler implements IEventHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(
			EventHandler.class);

	private IObligationHandler obligationHandler;
	private PiiDao piiDao;
	private IActionHandler actionHandler;
	private ActionFactory actionFactory;

	public EventHandler() {
		obligationHandler = ObligationHandler.getInstance();
		piiDao = new PiiDao();
		actionHandler = new ActionHandler();
		actionFactory = new ActionFactory();
	}


	public EventHandler(IObligationHandler obligationHandler, PiiDao piiDao,
			IActionHandler actionHandler, ActionFactory actionFactory) {
		super();
		this.obligationHandler = obligationHandler;
		this.piiDao = piiDao;
		this.actionHandler = actionHandler;
		this.actionFactory = actionFactory;
	}


	/* (non-Javadoc)
	 * @see com.sap.a4cloud.apple.obligation.event.IEventHandler#trigger(com.sap.a4cloud.apple.obligation.event.Event)
	 */
	@Override
	public void trigger(Event event) {
		Long piiId = event.getPiiId();
		PIIType pii = piiDao.findObject(PIIType.class, piiId);

		if (pii != null) {
			// retrieve obligation-trigger list
			List<ObligationTrigger> otList =
					obligationHandler.getObligations(piiId, event.getName());
			String cause = event.toString();
			for (ObligationTrigger ot : otList) {
				Trigger trigger = ot.getTrigger();

				// check whether the trigger corresponds with the event
				if (event.isTriggering(trigger)) {
					Action action = ot.getAction();
					executeAction(pii, action, cause);
				}
			}
		}
		else {
			LOGGER.warn("No PII with id {} found for the event {}",
					piiId, event);
		}
	}

	/**
	 * Executes the action by calling the
	 * {@link ActionHandler#handle(com.sap.a4cloud.apple.obligation.action.Action)}
	 * method.
	 *
	 * @param pii		the PII associated with the obligation
	 * @param action	the action element from the PPL policy
	 * @param cause		the cause of the action to be triggered
	 */
	private void executeAction(PIIType pii, Action action, String cause) {
		// create and trigger the action
		com.sap.a4cloud.apple.obligation.action.Action actionToHandle =
				actionFactory.createAction(pii, cause, action);
		LOGGER.info("Triggering action execution {} for PII {}",
				action, pii.getHjid());
		actionHandler.handle(actionToHandle);
	}

}
