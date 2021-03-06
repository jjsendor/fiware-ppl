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
package com.sap.research.primelife.restful.client.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.json.JSONException;
import org.json.JSONObject;

import com.sap.research.primelife.exceptions.WritingException;
import com.sap.research.primelife.marshalling.MarshallImpl;
import com.sap.research.primelife.message.request.PiiCreateRequest;
import com.sap.research.primelife.restful.client.file.PolicyGenerator;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

import eu.primelife.ppl.stickypolicy.impl.StickyPolicy;

/**
 * 
 * 
 *
 */
public class CreatePiiTest {


	private static final String SERVER_URL = "http://localhost:8080/ppl-rest/pii";
	PolicyGenerator pg = new PolicyGenerator();
	
	public void run() throws IOException {
		System.out.println("Running CreatePii test ...");
		
		String fileName = "test.txt";
		List<String> delegates = new ArrayList<String>();
		delegates.add("bob@example.com");
		List<String> fileNames = new ArrayList<String>();
		fileNames.add(fileName);
		String notify = "alice@example.com";
		
		createPii(fileName, delegates, fileNames, notify);
	}
	
	private Long createPii(String fileName, List<String> delegates, List<String> fileNames, String notify){
		File file = null;

		try {
			URI fileURI = getClass().getResource("/" + fileName).toURI();
			file = new File(fileURI);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		StickyPolicy stickyPolicy = pg.buildStickyPolicy(delegates, notify, "", fileNames);
		
		String ret = sendPii(file, stickyPolicy, null);
		
		JSONObject jsob;
		try {
			jsob = new JSONObject(ret);
			String uniqueIdStr = jsob.get("uniqueId").toString();
			return Long.parseLong(uniqueIdStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String sendPii(File file, StickyPolicy stickyPolicy, Long uid){
		try {
			
			Client client = Client.create();
			WebResource webResource = client.resource(SERVER_URL);
			ClientResponse response = null;
			
			if(uid != null){
				/*FormDataMultiPart form = new FormDataMultiPart();
				form.bodyPart(new FileDataBodyPart("file", file));
				form.field("stickyPolicy", stickyPolicy);
				form.field("uniqueId", uid.toString());
				 response = webResource
							.type(MediaType.MULTIPART_FORM_DATA)
							.accept(MediaType.APPLICATION_JSON)
							.post(ClientResponse.class, form);*/
			}else{
				PiiCreateRequest request = new PiiCreateRequest();
				request.setOwner((long) 1);
				request.setStickyPolicy(stickyPolicy);
				
//				JsonService js = new JsonService();
				
				FormDataMultiPart form = new FormDataMultiPart();
				form.bodyPart(new FileDataBodyPart("file", file));
				form.field("stickyPolicy", marshal(stickyPolicy));
				form.field("owner", String.valueOf(1));
				//System.out.println("Serialized request:" + js.serialize(request));
				//form.field("request", js.serialize(request));
				response = webResource
							.type(MediaType.MULTIPART_FORM_DATA)
							.accept(MediaType.APPLICATION_JSON)
							.put(ClientResponse.class, form);
			}
	
			if (response.getStatus() != 201) {
				System.err.println("Fi-ware return error status: " + response.getStatus());
				System.err.println("Response: ");

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(response.getEntityInputStream()));

				while (reader.ready()) {
					System.err.println(reader.readLine());
				}

				return null;
			}
			
			String repStr = response.getEntity(String.class);
			
	
			System.out.println("Fi-ware stored the pii and its policy with success. PiiId= " + repStr);
			return repStr;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String marshal(StickyPolicy stickyPolicy)
			throws JAXBException, WritingException {
		MarshallImpl marshaller =
				new MarshallImpl(StickyPolicy.class.getPackage());
		StringWriter writer = new StringWriter();
		marshaller.marshal(stickyPolicy, writer);
		return writer.toString();
	}
}
