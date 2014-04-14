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
package com.sap.a4cloud.apple.logging;

import java.util.List;

import com.sap.a4cloud.apple.logging.dao.LogEntryDao;
import com.sap.a4cloud.apple.logging.entity.LogEntry;

import eu.primelife.ppl.pii.impl.PIIType;

public class LoggerHandler implements ILoggerHandler {

	private LogEntryDao logEntryDao;

	/**
	 * Default constructor for Logging Handler.
	 */
	public LoggerHandler() {
		logEntryDao = new LogEntryDao();
	}

	/**
	 * Creates Logging Handler instance initialized with a given Log Entry
	 * Data Access Object.
	 *
	 * @param logEntryDao	Log Entry Data Access Object
	 */
	public LoggerHandler(LogEntryDao logEntryDao) {
		this.logEntryDao = logEntryDao;
	}

	@Override
	public void log(String message) {
		LogEntry piih = new LogEntry();
		piih.setMesssage(message);
		logEntryDao.persistObject(piih);
		logEntryDao.refresh(piih);
	}

	@Override
	public void log(PIIType pii, String message) {
		LogEntry piih = createFromPii(pii);
		piih.setMesssage(message);
		logEntryDao.persistObject(piih);
		logEntryDao.refresh(piih);
	}

	@Override
	public List<LogEntry> getHistoryByPii(PIIType pii){
		return logEntryDao.find(pii.getAttributeName(), pii.getAttributeValue());
	}

	@Override
	public List<LogEntry> getHistoryByOwner(String owner) {
		return logEntryDao.findByOwner(owner);
	}

	private LogEntry createFromPii(PIIType pii){
		LogEntry piih = new LogEntry();
		piih.setPiiAttributeName(pii.getAttributeName());
		piih.setPiiAttributeValue(pii.getAttributeValue());
		piih.setPiiOwner(pii.getOwner());
		return piih;
	}

}
