package com.sap.a4cloud.apple.pap;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.research.primelife.dao.PiiDao;
import com.sap.research.primelife.exceptions.SyntaxException;
import com.sap.research.primelife.marshalling.UnmarshallImpl;

import eu.primelife.ppl.pii.impl.PIIType;

/**
 * Tests for the Policy Administration Point.
 */
public class PapTest {

	private static final String PII_ROOT = "/pii/";
	private static UnmarshallImpl unmarshaller;
	private static PiiDao dao = new PiiDao();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		unmarshaller = new UnmarshallImpl(PIIType.class.getPackage());
	}

	@Test
	public void testStorePii() throws SyntaxException {
		// unmarshal PII
		String piiPath = PII_ROOT + "PII.xml";
		PIIType pii = (PIIType) unmarshaller.unmarshal(getClass()
				.getResourceAsStream(piiPath));

		// store PII in PAP
		PAP pap = new PAP();
		pap.storePii(pii);

		// test creation of id
		Long hjid = pii.getHjid();
		assertNotNull(hjid);

		// retrieve PII from the database
		PIIType actualPii = dao.findObject(PIIType.class, hjid);
		assertNotNull(actualPii);
	}

	@Test
	public void testDeletePii() throws SyntaxException {
		// unmarshal PII
		String piiPath = PII_ROOT + "PII.xml";
		PIIType pii = (PIIType) unmarshaller.unmarshal(getClass()
				.getResourceAsStream(piiPath));

		// store PII in PAP
		PAP pap = new PAP();
		pap.storePii(pii);

		// delete PII
		Long hjid = pii.getHjid();
		pap.deletePii(hjid);

		// test that the PII is no more in the database
		PIIType actualPii = dao.findObject(PIIType.class, hjid);
		assertNull(actualPii);
	}

}
