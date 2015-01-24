/**
 * 
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
