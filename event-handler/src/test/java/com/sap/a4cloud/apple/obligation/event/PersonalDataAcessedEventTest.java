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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import eu.primelife.ppl.policy.obligation.impl.TriggerPersonalDataAccessedForPurpose;

/**
 * @author Jakub Sendor
 *
 */
public class PersonalDataAcessedEventTest {

	@Test
	public void testIsTriggering() {
		PersonalDataAccessedEvent event = new PersonalDataAccessedEvent();
		event.setPurpose("http://www.w3.org/2006/01/P3Pv11/marketing");
		TriggerPersonalDataAccessedForPurpose trigger =
				new TriggerPersonalDataAccessedForPurpose();
		List<String> purposes = new ArrayList<String>(3);
		trigger.setPurpose(purposes);

		// add two purposes different from the event
		purposes.add("http://www.w3.org/2002/01/P3Pv1/individual-analysis");
		purposes.add("http://www.w3.org/2002/01/P3Pv1/admin");
		assertFalse(event.isTriggering(trigger));

		// add the same purpose as in the event
		purposes.add("http://www.w3.org/2006/01/P3Pv11/marketing");
		assertTrue(event.isTriggering(trigger));
	}

}
