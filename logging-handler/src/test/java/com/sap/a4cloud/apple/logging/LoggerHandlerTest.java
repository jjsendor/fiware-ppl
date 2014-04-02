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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.a4cloud.apple.logging.LoggerHandler;
import com.sap.a4cloud.apple.logging.dao.LogEntryDao;
import com.sap.a4cloud.apple.logging.entity.LogEntry;

import eu.primelife.ppl.pii.impl.PIIType;

public class LoggerHandlerTest {

	private LoggerHandler loggerHandler;
	@Mock private LogEntryDao logEntryDao;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		loggerHandler = new LoggerHandler(logEntryDao);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLogMessage() {
		loggerHandler.log("test message");

		class checkLogEntry extends ArgumentMatcher<LogEntry> {
			public boolean matches(Object obj) {
				LogEntry logEntry = (LogEntry) obj;
				return logEntry.getId() == null
						&& logEntry.getMesssage() == "test message"
						&& logEntry.getDate() == null
						&& logEntry.getPiiOwner() == null
						&& logEntry.getPiiAttributeName() == null
						&& logEntry.getPiiAttributeValue() == null;
			}
		}

		verify(logEntryDao).persistObject(argThat(new checkLogEntry()));
	}

	@Test
	public void testLogPii() {
		PIIType pii = new PIIType();
		pii.setAttributeName("name");
		pii.setAttributeValue("Robert");
		pii.setHjid((long) 1111);
		pii.setOwner("Bob");

		loggerHandler.log(pii, "test message 2");

		class checkLogEntry extends ArgumentMatcher<LogEntry> {
			public boolean matches(Object obj) {
				LogEntry logEntry = (LogEntry) obj;
				return logEntry.getId() == null
						&& logEntry.getMesssage() == "test message 2"
						&& logEntry.getDate() == null
						&& logEntry.getPiiOwner() == "Bob"
						&& logEntry.getPiiAttributeName() == "name"
						&& logEntry.getPiiAttributeValue() == "Robert";
			}
		}

		verify(logEntryDao).persistObject(argThat(new checkLogEntry()));
	}

	@Test
	public void testGetHistoryByPii() {
		LogEntry logEntry = new LogEntry();
		logEntry.setPiiAttributeName("name");
		logEntry.setPiiAttributeValue("Robert");
		List<LogEntry> logEntries = new ArrayList<LogEntry>(1);
		logEntries.add(logEntry);

		PIIType pii = new PIIType();
		pii.setAttributeName("name");
		pii.setAttributeValue("Robert");

		when(logEntryDao.find("name", "Robert")).thenReturn(logEntries);

		List<LogEntry> logEntries2 = loggerHandler.getHistoryByPii(pii);
		assertEquals(1, logEntries2.size());
		LogEntry logEntry2 = logEntries2.get(0);
		assertEquals("name", logEntry2.getPiiAttributeName());
		assertEquals("Robert", logEntry2.getPiiAttributeValue());
	}

	@Test
	public void testGetHistoryByOwner() {
		LogEntry logEntry = new LogEntry();
		logEntry.setPiiAttributeName("name");
		logEntry.setPiiAttributeValue("Robert");
		List<LogEntry> logEntries = new ArrayList<LogEntry>(1);
		logEntries.add(logEntry);

		when(logEntryDao.findByOwner("Bob")).thenReturn(logEntries);

		List<LogEntry> logEntries2 = loggerHandler.getHistoryByOwner("Bob");
		assertEquals(1, logEntries2.size());
		LogEntry logEntry2 = logEntries2.get(0);
		assertEquals("name", logEntry2.getPiiAttributeName());
		assertEquals("Robert", logEntry2.getPiiAttributeValue());
	}

}
