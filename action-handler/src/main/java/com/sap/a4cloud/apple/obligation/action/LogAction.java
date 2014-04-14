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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.a4cloud.apple.logging.dao.LogEntryDao;
import com.sap.a4cloud.apple.logging.entity.LogEntry;

/**
 * Logs the event associated with the obligation that triggered this action.
 *
 * @author Jakub Sendor
 *
 */
public class LogAction implements Action {

	private static Logger LOGGER = LoggerFactory.getLogger(LogAction.class);

	private String message;
	private String piiAttributeName;
	private String piiOwner;

	/**
	 * Creates LogAction with a given log message, the PII attribute name and
	 * the PII owner.
	 *
	 * @param message				the log message
	 * @param piiAttributeName		the PII attribute name
	 * @param piiOwner				the PII owner
	 */
	public LogAction(String message, String piiAttributeName,
			String piiOwner) {
		this.message = message;
		this.piiAttributeName = piiAttributeName;
		this.piiOwner = piiOwner;
	}

	/* (non-Javadoc)
	 * @see com.sap.research.a4cloud.action.Action#execute()
	 */
	@Override
	public void execute() {
		LogEntryDao dao = new LogEntryDao();
		LogEntry logEntry = new LogEntry();

		logEntry.setMesssage(message);
		logEntry.setPiiAttributeName(piiAttributeName);
		logEntry.setPiiOwner(piiOwner);

		dao.persistObject(logEntry);
		LOGGER.info("Executed log action for PII {} owned by {}",
				piiAttributeName, piiOwner);
	}

}
