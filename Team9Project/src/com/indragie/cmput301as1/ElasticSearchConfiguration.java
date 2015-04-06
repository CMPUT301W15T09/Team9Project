/* 
 * Copyright (C) 2015 Indragie Karunaratne
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

package com.indragie.cmput301as1;

import java.net.MalformedURLException;
import java.net.URL;

/** Stores information about our instance of ElasticSearch */
public class ElasticSearchConfiguration {
	private static URL baseURL;
	
	/** The name of the ElasticSearch index for our team */
	public static final String INDEX_NAME = "cmput301w15t09";
	
	/**
	 * @return The base URL for the shared ElasticSearch instance.
	 */
	public static URL getBaseURL() {
		if (baseURL == null) {
			try {
				baseURL = new URL("http://cmput301.softwareprocess.es:8080");
			} catch (MalformedURLException e) {
				// This should never happen, we know that the URL is valid.
				e.printStackTrace();
			}
		}
		return baseURL;
	}
}
