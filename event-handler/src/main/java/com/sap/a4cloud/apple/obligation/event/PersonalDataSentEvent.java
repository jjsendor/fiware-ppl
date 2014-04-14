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

/**
 * Event that should be triggered when Data Controller shares the personal
 * data with a third party.
 *
 * @author Jakub Sendor
 *
 */
public class PersonalDataSentEvent extends AbstractEvent implements Event {

	private String sharedWith;

	/**
	 * Returns the identifier of the third party with whom the personal data
	 * is shared.
	 *
	 * @return the third party identifier
	 */
	public String getSharedWith() {
		return sharedWith;
	}

	/**
	 * Sets the third party identifier with whom the personal data is shared.
	 *
	 * @param sharedWith the third party identifier
	 */
	public void setSharedWith(String sharedWith) {
		this.sharedWith = sharedWith;
	}

	@Override
	public String toString() {
		return super.toString() + " sent to " + sharedWith;
	}

}
