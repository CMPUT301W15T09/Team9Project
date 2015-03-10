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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;

/**
 * Observable model object that contains a list of {@link ExpenseClaim} objects.
 * Observers will be notified when the list is modified by adding, removing, or
 * replacing existing expense claims.
 */
public class ExpenseClaimListModel extends ListModel<ExpenseClaim> {
	//================================================================================
	// Properties
	//================================================================================
	
	private String fileName;
	private Context context;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Create a new instance of {@link ExpenseClaimListModel}
	 * @param fileName Name of the file used to persist the expense claims.
	 * @param context The context used for I/O operations.
	 */
	public ExpenseClaimListModel(String fileName, Context context) {
		this.fileName = fileName;
		this.context = context;
		this.list = load();
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return An unmodifiable list of expense claims.
	 */
	public List<ExpenseClaim> getExpenseClaims() {
		return Collections.unmodifiableList(list);
	}
	
	//================================================================================
	// API
	//================================================================================
	
	/**
	 * Adds a new expense claim to the list of expense claims.
	 * @param claim The expense claim to add.
	 */
	public void add(ExpenseClaim claim) {
		super.add(claim);
		commitClaimsMutation();
	}
	
	/**
	 * Removes an existing expense claim from the list of expense claims.
	 * @param claim The expense claim to remove.
	 */
	public boolean remove(ExpenseClaim claim) {
		if (list.remove(claim)) {
			commitClaimsMutation();
			return true;
		}
		return false;
	}
	
	/**
	 * Removes an existing expense claim from the list of expense claims.
	 * @param index The index of the expense claim to remove.
	 */
	public void remove(int index) {
		super.remove(index);
		commitClaimsMutation();
	}
	
	/**
	 * Remove all expense claims from the list of expense claims.
	 */
	public void removeAll() {
		super.removeAll();
		commitClaimsMutation();
	}
	
	/**
	 * Replaces an existing expense claim.
	 * @param index The index of the expense claim to replace.
	 * @param newClaim The expense claim to replace the existing expense claim with.
	 */
	public void set(int index, ExpenseClaim newClaim) {
		super.set(index, newClaim);
		commitClaimsMutation();
	}
	

	//================================================================================
	// Helpers
	//================================================================================
	
	private void commitClaimsMutation() {
		save(list);
		setChanged();
		notifyObservers(list);
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<ExpenseClaim> load() {
		try {
			FileInputStream fis = context.openFileInput(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			ois.close();
			fis.close();
			return (ArrayList<ExpenseClaim>)obj;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ExpenseClaim>();
		}
	}
	
	private void save(ArrayList<ExpenseClaim> claims) {
		try {
			FileOutputStream fos = context.openFileOutput(fileName, 0);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(claims);
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
