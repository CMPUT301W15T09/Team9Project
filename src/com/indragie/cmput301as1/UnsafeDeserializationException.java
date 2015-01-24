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

/**
 * Thrown when the class of a deserialized object does not match the
 * expected class.
 * 
 * Deserialization of untrusted data is a security vulnerability.
 */
public class UnsafeDeserializationException extends Exception {
	private static final long serialVersionUID = -3589540683570596962L;

	public UnsafeDeserializationException(String message) {
		super(message);
	}
}
