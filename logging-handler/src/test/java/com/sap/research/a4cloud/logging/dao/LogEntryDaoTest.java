package com.sap.research.a4cloud.logging.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.a4cloud.logging.entity.LogEntry;

public class LogEntryDaoTest {

	private static LogEntryDao dao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dao = new LogEntryDao();
	}

	@Test
	public void testFind() {
		LogEntry logEntry1 = new LogEntry();
		logEntry1.setPiiAttributeName("family name");
		logEntry1.setPiiAttributeValue("Doe");
		logEntry1.setMesssage("PII accessed");
		dao.persistObject(logEntry1);

		LogEntry logEntry2 = new LogEntry();
		logEntry2.setPiiAttributeName("given name");
		logEntry2.setPiiAttributeValue("John");
		logEntry2.setMesssage("PII send");
		dao.persistObject(logEntry2);

		List<LogEntry> logEntries = dao.find("family name", "Doe");
		assertFalse(logEntries.isEmpty());

		LogEntry logEntry = logEntries.get(0);
		assertEquals("family name", logEntry.getPiiAttributeName());
		assertEquals("Doe", logEntry.getPiiAttributeValue());
		assertEquals("PII accessed", logEntry.getMesssage());
	}

	@Test
	public void testFindByOwner() {
		LogEntry logEntry1 = new LogEntry();
		logEntry1.setPiiAttributeName("family name");
		logEntry1.setPiiAttributeValue("Doe");
		logEntry1.setMesssage("PII accessed");
		logEntry1.setOwner("John Doe");
		dao.persistObject(logEntry1);

		LogEntry logEntry2 = new LogEntry();
		logEntry2.setPiiAttributeName("given name");
		logEntry2.setPiiAttributeValue("John");
		logEntry2.setMesssage("PII send");
		logEntry2.setOwner("John Doe");
		dao.persistObject(logEntry2);

		LogEntry logEntry3 = new LogEntry();
		logEntry3.setPiiAttributeName("family name");
		logEntry3.setPiiAttributeValue("Doe");
		logEntry3.setMesssage("PII accessed");
		logEntry3.setOwner("Jane Doe");
		dao.persistObject(logEntry3);

		LogEntry logEntry4 = new LogEntry();
		logEntry4.setPiiAttributeName("given name");
		logEntry4.setPiiAttributeValue("Jane");
		logEntry4.setMesssage("PII send");
		logEntry4.setOwner("Jane Doe");
		dao.persistObject(logEntry4);

		List<LogEntry> logEntries = dao.findByOwner("Jane Doe");
		assertEquals(2, logEntries.size());

		for (LogEntry logEntry : logEntries) {
			assertEquals("Jane Doe", logEntry.getOwner());
		}
	}

}
