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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Helper functions for working with JSON data with Gson.
 */
public class JSONHelpers {
	/**
	 * Gets the value of the JSON element as a string if possible.
	 * @param element The JSON element.
	 * @return A string if the JSON element was a string, or
	 * null otherwise.
	 */
	public static String getStringIfPossible(JsonElement element) {
		if (element == null) return null;
		
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isString()) {
				return primitive.getAsString();
			}
		}
		return null;
	}
	
	/**
	 * Gets the value of the JSON element as a boolean if possible.
	 * @param element The JSON element.
	 * @return A string if the JSON element was a boolean, or
	 * false otherwise.
	 */
	public static boolean getBooleanIfPossible(JsonElement element) {
		if (element == null) return false;
		
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isBoolean()) {
				return primitive.getAsBoolean();
			}
		}
		return false;
	}
	
	/**
	 * Gets the value of the JSON element as a long if possible.
	 * @param element The JSON element.
	 * @return A string if the JSON element was a long, or
	 * 0 otherwise.
	 */
	public static long getLongIfPossible(JsonElement element) {
		if (element == null) return 0;
		
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isNumber()) {
				return primitive.getAsLong();
			}
		}
		return 0;
	}
	
	/**
	 * Gets the value of the JSON element as a JSON object if possible.
	 * @param element The JSON element.
	 * @return A string if the JSON element was a JSON object, or
	 * null otherwise.
	 */
	public static JsonObject getJsonObjectIfPossible(JsonElement element) {
		if (element == null) return null;
		
		if (element.isJsonObject()) {
			return element.getAsJsonObject();
		}
		return null;
	}
}
