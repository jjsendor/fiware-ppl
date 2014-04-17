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
package com.sap.a4cloud.apple.obligation.action;

import com.sap.a4cloud.apple.logging.LoggingHandler;
import com.sap.a4cloud.apple.pap.PAP;

import eu.primelife.ppl.pii.impl.PIIType;
import eu.primelife.ppl.policy.obligation.impl.ActionAnonymizePersonalData;
import eu.primelife.ppl.policy.obligation.impl.ActionDeletePersonalData;
import eu.primelife.ppl.policy.obligation.impl.ActionLog;
import eu.primelife.ppl.policy.obligation.impl.ActionNotifyDataSubject;
import eu.primelife.ppl.policy.obligation.impl.ActionSecureLog;

/**
 * Produces the instance of the {@link Action} based on the given description
 * from the <code>&lt;Action&gt;</code> in the obligation that is a part of
 * the PPL policy .
 *
 * @author Jakub Sendor
 *
 */
public class ActionFactory {

	/**
	 * Creates {@link Action} based on a given
	 * {@link eu.primelife.ppl.policy.obligation.impl.Action}.
	 * @param	pii		the PII associated with the obligation
	 * @param	cause	the event that caused the obligation trigger
	 * @param	action	the action element of the obligation
	 * @return
	 */
	public Action createAction(PIIType pii, String cause,
			eu.primelife.ppl.policy.obligation.impl.Action action) {
		if (action instanceof ActionDeletePersonalData) {
			return new DeleteAction(new PAP(), pii.getHjid());
		}

		if (action instanceof ActionAnonymizePersonalData) {
			// TODO not yet implemented
			return null;
		}

		if (action instanceof ActionNotifyDataSubject) {
			ActionNotifyDataSubject actionNotify =
					(ActionNotifyDataSubject) action;
			String media = actionNotify.getMedia();
			String address = actionNotify.getAddress();
			String recipient = null;// FIXME update with the new schema

			return new NotifyAction(media, address, recipient, cause);
		}

		if (action instanceof ActionLog) {
			if (pii != null) {
				return new LogAction(new LoggingHandler(), cause,
						pii.getAttributeName(), pii.getOwner());
			}

			return new LogAction(new LoggingHandler(), cause, null, null);
		}

		if (action instanceof ActionSecureLog) {
			// TODO not yet implemented
			return null;
		}

		return null;
	}

}
