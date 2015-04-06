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

package com.indragie.comput301as1.test;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.indragie.cmput301as1.DateJSONDeserializer;
import com.indragie.cmput301as1.DateJSONSerializer;

import junit.framework.TestCase;

public class DateJSONSerializationTests extends TestCase {
	public void testSerializationAndDeserialization() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new DateJSONSerializer());
		builder.registerTypeAdapter(Date.class, new DateJSONDeserializer());
		Gson gson = builder.create();
		
		Date date = new Date();
		String json = gson.toJson(date, Date.class);
		Date deserializedDate = gson.fromJson(json, Date.class);
		assertEquals(date, deserializedDate);
	}
}
