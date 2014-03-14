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
package com.sap.research.a4cloud.logging;

import java.util.List;

import com.sap.research.a4cloud.logging.entity.LogEntry;

import eu.primelife.ppl.pii.impl.PIIType;

/**
 * Handles logging operations for PPL Engine.
 *
 */
public interface ILoggerHandler {

	/**
	 * Creates a log entry for particular event described by
	 * <code>message</code>.
	 *
	 * @param message	the log entry message
	 */
	public void log(String message);

	/**
	 * Creates a log entry for particular event described by
	 * <code>message</code>. Log entry can be associated with a particular
	 * PII by passing PII object in <code>pii</code> parameter.
	 *
	 * @param pii		the personal data to associate with the log entry
	 * @param message	the log entry message
	 */
	public void log(PIIType pii, String message);

	/**
	 * Fetches the logs associated with the given PII.
	 *
	 * @param pii	the PII with which the logs are associated with
	 * @return	the log entry list
	 */
	public List<LogEntry> getHistoryByPii(PIIType pii);

	/**
	 * Fetches the logs associated with the given PII owner.
	 *
	 * @param owner	the PII owner
	 * @return	the log list entry
	 */
	public List<LogEntry> getHistoryByOwner(String owner);

}
