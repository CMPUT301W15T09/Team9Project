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

import java.net.MalformedURLException;
import java.net.URL;

import com.indragie.cmput301as1.ElasticSearchAPIClient;
import com.indragie.cmput301as1.ElasticSearchCallback;
import com.indragie.cmput301as1.ElasticSearchDocument;
import com.indragie.cmput301as1.ElasticSearchDocumentID;

import junit.framework.TestCase;


public class ElasticSearchAPIClientTest extends TestCase 
{
	
	private URL test;
	
	// create the URL link to the 301 software site
	protected void setUp() throws Exception {
		super.setUp();
		try
		{
			test = new URL("http://cmput301.softwareprocess.es:8080/");
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public class ElasticSearchModel implements ElasticSearchDocument {
		private String index;
		private String type;
		private String id;
		
		public ElasticSearchDocumentID getObjectID() {
			return new ElasticSearchDocumentID("testing", "type", "abc123");
		}
		
		public ElasticSearchModel(String index, String type, String id) {
			this.index = index;
			this.type = type;
			this.id = id;
		}
	}
	
	public void testadd() throws InterruptedException{
		ElasticSearchModel esmodel = new ElasticSearchModel("testing","type","abc123");
		ElasticSearchAPIClient api = new ElasticSearchAPIClient(test);
		
		ElasticSearchCallback<ElasticSearchDocument> callback = null;
		
		api.add(esmodel, callback);
		Thread.sleep(3000);
		
		// check to see if the ids match to see if the api model added the id
		ElasticSearchDocumentID model = esmodel.getObjectID();
		String id = model.getID();
		
		assertEquals(id, "something");
	}
	
	
}
