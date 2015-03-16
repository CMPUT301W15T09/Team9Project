/* 
 * Copyright (C) 2015 Indragie Karunaratne, Andrew Zhong
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

public class ListModel<T extends Comparable<? super T>> extends TypedObservable<List<T>> {


	//================================================================================
	// Properties
	//================================================================================
	/**
	 * The filename to save to.
	 */
	private String fileName;
	/**
	 * Context of the list model.
	 */
	private Context context;
	/**
	 * List of type T.
	 */
	protected ArrayList<T> list;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Create a new instance of {@link ExpenseClaimListModel}
	 * @param fileName Name of the file used to persist the expense claims.
	 * @param context The context used for I/O operations.
	 */
	public ListModel(String fileName, Context context) {
		this.fileName = fileName;
		this.context = context;
		this.list = load();
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return An unmodifiable list of items.
	 */
	public List<T> getItems() {
		return Collections.unmodifiableList(list);
	}
	
	//================================================================================
	// API
	//================================================================================
	
	/**
	 * Adds a object to the list of objects.
	 * @param o The object to add.
	 */
	public void add(T o) {
		list.add(o);
		Collections.sort(list);
		commitClaimsMutation();
	}
	

	/**
	 * Removes an existing object from the list of objects.
	 * @param o The object to remove.
	 */
	public boolean remove(T o) {
		if (list.remove(o)) {
			commitClaimsMutation();
			return true;
		}
		return false;
	}
	
	/**
	 * Removes an existing object from the list of objects.
	 * @param index The index of the object to remove.
	 */
	public void remove(int index) {
		list.remove(index);
		commitClaimsMutation();
	}
	
	/**
	 * Remove all objects from the list of objects.
	 */
	public void removeAll() {
		list.clear();
		commitClaimsMutation();
	}
	
	/**
	 * Replaces an existing object.
	 * @param index The index of the object to replace.
	 * @param newO The new object to replace the existing object with.
	 */
	public void set(int index, T newO) {
		list.set(index, newO);
		Collections.sort(list);
		commitClaimsMutation();
	}
	
	/**
	 * @return The number of items in the list.
	 */
	public int count() {
		return list.size();
	}
	
	//================================================================================
	// Helpers
	//================================================================================
	
	/**
	 * Commits the changes.
	 */
	private void commitClaimsMutation() {
		save(list);
		setChanged();
		notifyObservers(list);
	}
	
	
	/**
	 * Loads a list from a save file. 
	 * @return A list of specified type.
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<T> load() {
		try {
			FileInputStream fis = context.openFileInput(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			ois.close();
			fis.close();
			return (ArrayList<T>)obj;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}
	/**
	 * Saves a list into a file.
	 * @param o The list to save.
	 */
	private void save(ArrayList<T> o) {
		try {
			FileOutputStream fos = context.openFileOutput(fileName, 0);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
