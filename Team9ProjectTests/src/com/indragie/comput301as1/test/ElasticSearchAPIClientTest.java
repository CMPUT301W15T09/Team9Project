/* 
 * Copyright (C) 2015 Jimmy Ho
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.indragie.comput301as1.test;

import java.io.IOException;

import com.indragie.cmput301as1.ElasticSearchAPIClient;
import com.indragie.cmput301as1.ElasticSearchAPIClient.RequestFailedException;
import com.indragie.cmput301as1.ElasticSearchDocument;
import com.indragie.cmput301as1.ElasticSearchDocumentID;
import com.squareup.okhttp.mockwebserver.*;

import junit.framework.TestCase;

public class ElasticSearchAPIClientTest extends TestCase 
{
	private ElasticSearchAPIClient client;
	private MockWebServer server;
	
	@Override
	protected void setUp() throws Exception {
		server = new MockWebServer();
		server.start();
		client = new ElasticSearchAPIClient(server.getUrl("/"));
	}
	
	@Override
	protected void tearDown() throws Exception {
		server.shutdown();
		super.tearDown();
	}
	
	public void testAdd() throws InterruptedException, RequestFailedException, IOException {
		MockResponse response = new MockResponse()
	    	.addHeader("Content-Type", "application/json; charset=utf-8")
	    	.addHeader("Cache-Control", "no-cache")
	    	.setBody("{\"_index\":\"test\",\"_type\":\"doc\",\"_id\":\"100\",\"_version\":1,\"created\":true}");
		server.enqueue(response);
		
		TestDocument document = new TestDocument("Indragie Karunaratne", 3);
		assertEquals(document, client.add(document).execute());
		
		RecordedRequest request = server.takeRequest();
		assertEquals("/test/doc/100", request.getPath());
		assertEquals("POST", request.getMethod());
	}
	
	public void testGet() throws InterruptedException, RequestFailedException, IOException {
		MockResponse response = new MockResponse()
			.addHeader("Content-Type", "application/json; charset=utf-8")
			.addHeader("Cache-Control", "no-cache")
			.setBody("{\"_index\":\"test\",\"_type\":\"doc\",\"_id\":\"100\",\"_version\":1,\"found\":true,\"_source\":{\"name\":\"Indragie Karunaratne\",\"year\":3}}");
		server.enqueue(response);
		
		TestDocument document = new TestDocument("Indragie Karunaratne", 3);
		assertEquals(document, client.get(new ElasticSearchDocumentID("test", "doc", "100")).execute());
		
		RecordedRequest request = server.takeRequest();
		assertEquals("/test/doc/100", request.getPath());
		assertEquals("GET", request.getMethod());
	}
	
	public void testUpdate() throws InterruptedException, RequestFailedException, IOException {
		MockResponse response = new MockResponse()
    		.addHeader("Content-Type", "application/json; charset=utf-8")
    		.addHeader("Cache-Control", "no-cache")
    		.setBody("{\"_index\":\"test\",\"_type\":\"doc\",\"_id\":\"100\",\"_version\":2,\"created\":false}");
		server.enqueue(response);
		
		TestDocument document = new TestDocument("Indragie Karunaratne", 4);
		assertEquals(document, client.update(document).execute());
		
		RecordedRequest request = server.takeRequest();
		assertEquals("/test/doc/100", request.getPath());
		assertEquals("PUT", request.getMethod());
	}
	
	public void testDelete() throws InterruptedException, RequestFailedException, IOException {
		MockResponse response = new MockResponse()
			.addHeader("Content-Type", "application/json; charset=utf-8")
			.addHeader("Cache-Control", "no-cache")
			.setBody("{\"_index\":\"test\",\"_type\":\"doc\",\"_id\":\"100\",\"_version\":2,\"found\":true}");
		server.enqueue(response);
		
		TestDocument document = new TestDocument("Indragie Karunaratne", 3);
		assertEquals(null, client.delete(document).execute());
		
		RecordedRequest request = server.takeRequest();
		assertEquals("/test/doc/100", request.getPath());
		assertEquals("DELETE", request.getMethod());
	}
	
	private class TestDocument implements ElasticSearchDocument {
		private String name;
		private int year;
		
		TestDocument(String name, int year) {
			this.name = name;
			this.year = year;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + year;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestDocument other = (TestDocument) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (year != other.year)
				return false;
			return true;
		}
		
		private ElasticSearchAPIClientTest getOuterType() {
			return ElasticSearchAPIClientTest.this;
		}
		
		@Override
		public ElasticSearchDocumentID getDocumentID() {
			return new ElasticSearchDocumentID("test", "doc", "100");
		}
	}
}
