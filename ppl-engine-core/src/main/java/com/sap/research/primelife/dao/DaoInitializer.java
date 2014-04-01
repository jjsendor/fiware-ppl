/*******************************************************************************
 * Copyright (c) 2013, SAP AG
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
package com.sap.research.primelife.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.ejb.Ejb3Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The initializer of the DAO (persistence manager).
 * Facilitates obtaining the Entity Manager.
 *
 * @Version 0.2
 * @Date Jun 18, 2010
 * 
 */
public class DaoInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoInitializer.class);
	private static EntityManager em;
	private static final String PERSISTANCE_UNIT_NAME = "ppl";

	/**
	 * Create a singleton EntityManager
	 * @return
	 */
	public static EntityManager getEntityManager() {
		if (em == null){
			LOGGER.debug("EntityManager is null, creating new EntityManager.");
			Ejb3Configuration cfg = new Ejb3Configuration();
			EntityManagerFactory emf = cfg
					.configure(PERSISTANCE_UNIT_NAME, null)
					.buildEntityManagerFactory();
			em = emf.createEntityManager();
		}

		return em;
	}

	/**
	 * Close the EntityManager
	 */
	public static void closeEntityManager(){
		if (em != null) {
			em.close();
			em = null;
		}
	}

}
