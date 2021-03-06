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

import eu.primelife.ppl.policy.obligation.impl.Trigger;
import eu.primelife.ppl.policy.obligation.impl.TriggerOnViolation;

/**
 * Event that should be triggered when policy is violated.
 *
 * @author Jakub Sendor
 *
 */
public class OnViolationEvent extends AbstractEvent implements Event {

	private static final String NAME = "{http://www.primelife.eu/ppl/obligation}TriggerOnViolation";

	private String violation;

	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * Returns description of violation.
	 *
	 * @return the violation description
	 */
	public String getViolation() {
		return violation;
	}

	/**
	 * Sets the representation of the policy violation.
	 *
	 * @param violation	the textual description of the violation
	 */
	public void setViolation(String violation) {
		this.violation = violation;
	}

	@Override
	public String toString() {
		return super.toString() + " policy violated: " + violation;
	}

	@Override
	public boolean isTriggering(Trigger trigger) {
		return trigger instanceof TriggerOnViolation;
	}

}
