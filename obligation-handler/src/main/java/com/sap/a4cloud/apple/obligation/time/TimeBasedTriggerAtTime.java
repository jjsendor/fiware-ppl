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

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.a4cloud.apple.obligation.action.ActionFactory;
import com.sap.a4cloud.apple.obligation.action.ActionHandler;
import com.sap.a4cloud.apple.obligation.action.IActionHandler;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.obligation.impl.Action;
import eu.primelife.ppl.policy.obligation.impl.DateAndTime;
import eu.primelife.ppl.policy.obligation.impl.Duration;
import eu.primelife.ppl.policy.obligation.impl.TriggerAtTime;

public class TimeBasedTriggerAtTime extends TimeBasedTrigger {

	private final static Logger LOGGER =
			LoggerFactory.getLogger(TimeBasedTriggerAtTime.class);

	protected Duration maxDelay;
	protected DateAndTime start;
	protected long startMillisec = 0; // Epoch time representation of the Start Date


	protected TimeBasedTriggerAtTime(TriggerAtTime trigger, Action action,
			PIIType pii) {
		this(trigger, action, pii, new ActionHandler(), new ActionFactory());
	}

	protected TimeBasedTriggerAtTime(TriggerAtTime trigger, Action action,
			PIIType pii, IActionHandler actionHandler,
			ActionFactory actionFactory) {
		super(action, pii, actionHandler, actionFactory);
		maxDelay = trigger.getMaxDelay();
		start = trigger.getStart();
	}

	@Override
	public void start() {
		final TimeBasedTriggerAtTime timeBasedTrigger = this;
		timer = new Timer();
		timerTask = new TimerTask() {
			public void run() {
				long time = this.scheduledExecutionTime();
				LOGGER.info("TriggerAtTime for {} triggered at {}",
						pii.getHjid(), time);
				TimeBasedTriggerAtTime.tick(action, pii, time, actionHandler,
						actionFactory);
				timeBasedTrigger.cancel();
			}
		};

		Date now = new Date();
		if (start.getStartNow() != null) {
			start.setDateAndTimeItem(new Date());
			start.setStartNowObject(null);
			start.setStartNow(null);
		}

		startMillisec = toMilliseconds(start);

		long l = (startMillisec - now.getTime()) + maxDelay.getInSecond() * 1000;
		if (l >= 0) {
			timer.schedule(timerTask, (startMillisec - now.getTime()) + maxDelay.getInSecond() * 1000);
			LOGGER.info("Timer for trigger at time associated with PII {} started", pii.getHjid());
		}
		else {
			timer.schedule(timerTask, 1);
		}
	}

}
