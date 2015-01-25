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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Model object representing an expense claim.
 */
public class ExpenseClaim implements Serializable {
	private static final long serialVersionUID = 4097224167619777631L;
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * Name of the claim.
	 */
	private String name;
	/**
	 * Textual description of the claim.
	 */
	private String description;
	/**
	 * Expense items contained in the claim.
	 */
	private List<ExpenseItem> items;

	/**
	 * Possible statuses for an expense claim.
	 */
	public enum Status {
		/**
		 * Expense has been submitted, no edits allowed 
		 */
		SUBMITTED,
		/**
		 * Expense has been returned, edits allowed 
		 */
		RETURNED,
		/** 
		 * Expense has been approved, no edits allowed 
		 */
		APPROVED
	}
	
	/**
	 * Current status of the expense claim.
	 */
	private Status status;
	
	//================================================================================
	// Constructors
	//================================================================================

	public ExpenseClaim(String name, String description, Status status) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.items = new ArrayList<ExpenseItem>();
	}

	//================================================================================
	// Accessors
	//================================================================================

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ExpenseItem> getItems() {
		return Collections.unmodifiableList(items);
	}
	
	public void setItems(List<ExpenseItem> items) {
		this.items = items;
	}

	public void addItem(ExpenseItem item) {
		items.add(item);
	}
	
	public void removeItem(int index) {
		items.remove(index);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String toString() {
		return name;
	}
	
	/**
	 * @return A list containing two elements: the earliest date and
	 * the latest date, respectively, for the expense items contained
	 * in the expense claim.
	 * @throws NoSuchElementException if there are no expense items.
	 */
	public List<Date> getDateRange() throws NoSuchElementException {
		Comparator<ExpenseItem> itemComparator = new Comparator<ExpenseItem>() {
			@Override
			public int compare(ExpenseItem item1, ExpenseItem item2) {
				return item1.getDate().compareTo(item2.getDate());
			}
		};
		ExpenseItem min = Collections.min(items, itemComparator);
		ExpenseItem max = Collections.max(items, itemComparator);
		return Arrays.asList(min.getDate(), max.getDate());
	}
}
