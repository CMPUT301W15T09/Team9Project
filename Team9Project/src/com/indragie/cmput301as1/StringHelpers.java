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

import android.annotation.SuppressLint;

/**
 * Utility functions for working with strings.
 */
public class StringHelpers {
	
	/**
	 * Normalizes the string by removing non-alphanumeric characters
	 * and making all characters lowercase.
	 * @param str The string to normalize.
	 * @return The normalized representation of the string.
	 */
	@SuppressLint("DefaultLocale")
	public static String normalize(String str) {
		if (str == null) return null;
		return str.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
	}
}
