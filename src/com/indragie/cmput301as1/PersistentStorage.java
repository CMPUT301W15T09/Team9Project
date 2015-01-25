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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Persists and loads an object conforming to Serializable on disk.
 */
public class PersistentStorage<T extends Serializable> {
	/**
	 * The name of the file to read and write from.
	 */
	private String filename;
	
	public PersistentStorage(String filename) {
		this.filename = filename;
	}
	
	/**
	 * Reads an object from disk.
	 * @param expectedClass The expected class of the object.
	 * @return The object that was read.
	 * @throws IOException if the file cannot be read.
	 * @throws ClassNotFoundException if the class of the deserialized object does not exist.
	 * @throws UnsafeDeserializationException if the class of the deserialized object does not match the expected class.
	 */
	@SuppressWarnings("unchecked")
	// Expected class has to be passed in explicitly because the type of T
	// can not be accessed at runtime (due to type erasure).
	public T read(Class<? extends Serializable> expectedClass) throws IOException, ClassNotFoundException, UnsafeDeserializationException {
		FileInputStream fis = new FileInputStream(filename);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object obj = ois.readObject();
		ois.close();
		fis.close();
		
		if (!expectedClass.isInstance(obj)) {
			throw new UnsafeDeserializationException(String.format("Object class %s does not match expected class %s.", obj.getClass(), expectedClass));
		}
		
		return (T)obj;
	}
	
	/**
	 * Writes an object to disk.
	 * @warning Any existing data in the file will be overwritten.
	 * @param object The object to write.
	 * @throws IOException if the file cannot be written to.
	 */
	public void write(T object) throws IOException {
		FileOutputStream fos = new FileOutputStream(filename);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(object);
		oos.close();
		fos.close();
	}
}
