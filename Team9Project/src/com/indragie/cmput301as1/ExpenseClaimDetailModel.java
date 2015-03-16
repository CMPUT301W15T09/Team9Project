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

import java.util.List;

/**
 * Model for {@link ExpenseClaimDetailActivity}, which handles displaying
 * the details for an expense claim -- destinations, tags, and expense items.
 */
public class ExpenseClaimDetailModel extends TypedObservable<Object> {
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * The expense claim for which details are displayed.
	 */
	private ExpenseClaim claim;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link ExpenseClaimDetailModel}
	 * @param claim The expense claim for which details are displayed.
	 */
	public ExpenseClaimDetailModel(ExpenseClaim claim) {
		this.claim = claim;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return The expense claim for which details are displayed.
	 */
	public ExpenseClaim getExpenseClaim() {
		return claim;
	}
	
	//================================================================================
	// API
	//================================================================================
	
	/**
	 * @return The travel destinations for the expense claim.
	 */
	public List<Destination> getDestinations() {
		return claim.getDestinations();
	}
	
	/**
	 * Adds a destination to the expense claim.
	 * @param destination The destination to add.
	 */
	public void addDestination(Destination destination) {
		claim.addDestination(destination);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Replaces an existing destination on the expense claim.
	 * @param index The index of the destination to replace.
	 * @param destination The destination to replace it with.
	 */
	public void setDestination(int index, Destination destination) {
		claim.setDestination(index, destination);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Removes a destination from the expense claim.
	 * @param index The index of the destination to remove.
	 */
	public void removeDestination(int index) {
		claim.removeDestination(index);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * @return The expense items for the expense claim.
	 */
	public List<ExpenseItem> getItems() {
		return claim.getItems();
	}
	
	/**
	 * Adds an expense item to the expense claim.
	 * @param item The expense item to add.
	 */
	public void addItem(ExpenseItem item) {
		claim.addItem(item);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Replaces an existing item on the expense claim.
	 * @param index The index of the item to replace.
	 * @param item The item to replace it with.
	 */
	public void setItem(int index, ExpenseItem item) {
		claim.setItem(index, item);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Removes an item from the expense claim.
	 * @param index The index of the item to remove.
	 */
	public void removeItem(int index) {
		claim.removeItem(index);
		setChanged();
		notifyObservers();
	}
}
