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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;

/**
 * Observable model that contains a list of items that can be mutated.
 */
public class ListModel<T extends Serializable> extends TypedObservable<CollectionMutation<T>> implements Serializable{
	private static final long serialVersionUID = 5371138997651829224L;

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
	 * Comparator used to sort items in the list.
	 */
	private Comparator<T> comparator;
	
	/**
	 * List that backs the model.
	 */
	private ArrayList<T> list;
	
	
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
	
	/**
	 * @return Comparator used to sort the items.
	 */
	public Comparator<T> getComparator() {
		return comparator;
	}
	
	/**
	 * Sets the comparator used to sort the items.
	 * @param comparator The comparator used to sort the items.
	 */
	public void setComparator(Comparator<T> comparator) {
		this.comparator = comparator;
		sort();
	}
	
	//================================================================================
	// API
	//================================================================================
	
	/**
	 * Adds a object to the list of objects.
	 * @param object The object to add.
	 */
	public void add(T object) {
		list.add(object);
		mutate(new InsertionCollectionMutation<T>(list.size() - 1, object));
		sort();
	}

	/**
	 * Removes an existing object from the list of objects.
	 * @param o The object to remove.
	 */
	public boolean remove(T o) {
		int index = list.indexOf(o);
		if (index != -1) {
			remove(index);
			return true;
		}
		return false;
	}

	/**
	 * Removes an existing object from the list of objects.
	 * @param index The index of the object to remove.
	 */
	public void remove(int index) {
		mutate(new RemovalCollectionMutation<T>(index, list.remove(index)));
	}
	
	/**
	 * Remove all objects from the list of objects.
	 */
	public void removeAll() {
		ArrayList<T> oldContents = new ArrayList<T>(list);
		list.clear();
		mutate(new ReplacementCollectionMutation<T>(oldContents, new ArrayList<T>()));
	}
	
	/**
	 * Replaces an existing object.
	 * @param index The index of the object to replace.
	 * @param newObject The new object to replace the existing object with.
	 */
	public void set(int index, T newObject) {
		mutate(new UpdateCollectionMutation<T>(index, list.set(index, newObject), newObject));
		sort();
	}
	
	/**
	 * @return The number of items in the list.
	 */
	public int count() {
		return list.size();
	}
	
	/**
	 * Replaces the current contents of the model.
	 * @param newContents The new contents of the list.
	 */
	public void replace(List<T> newContents) {
		ArrayList<T> oldContents = new ArrayList<T>(list);
		list = new ArrayList<T>(newContents);
		mutate(new ReplacementCollectionMutation<T>(oldContents, new ArrayList<T>(newContents)));
	}
	
	//================================================================================
	// Helpers
	//================================================================================
	
	/**
	 * Commits the changes.
	 */
	private void mutate(CollectionMutation<T> mutation) {
		setChanged();
		notifyObservers(mutation);
		save(list);
	}
	
	/**
	 * Sorts the contents of the list.
	 */
	private void sort() {
		if (comparator == null) return;
		ArrayList<T> oldContents = new ArrayList<T>(list);
		Collections.sort(list, comparator);
		mutate(new ReplacementCollectionMutation<T>(oldContents, new ArrayList<T>(list)));
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
