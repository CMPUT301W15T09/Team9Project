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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.joda.money.*;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * Model object representing an expense claim.
 */
public class ExpenseClaim implements Serializable, Comparable<ExpenseClaim> {
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
		 * Default status when the expense is created
		 */
		IN_PROGRESS,
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
	private Status status = Status.IN_PROGRESS;
	
	/**
	 * Starting date of the claim.
	 */
	private Date startDate;
	
	/**
	 * Ending date of the claim.
	 */
	private Date endDate;
	

	// added private variable creation date
	private Date creationDate;

	//================================================================================
	// Constructors
	//================================================================================

	public ExpenseClaim(String name, String description, Date startDate, Date endDate, Status status, Date creationDate) {
		this.name = name;
		this.description = description;
		this.items = new ArrayList<ExpenseItem>();
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		
		// modified for creation date
		this.creationDate = creationDate;
	}

	//================================================================================
	// Accessors
	//================================================================================
	
	// modified for creation date
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

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
		Collections.sort(items);
	}
	
	public void setItem(int position, ExpenseItem item) {
		items.set(position, item);
		Collections.sort(items);
	}

	public void removeItem(int index) {
		items.remove(index);
	}

	public Status getStatus() {
		return status;
	}
	
	public String getStatusString(Resources resources) {
		switch (status) {
		case IN_PROGRESS: return resources.getString(R.string.status_in_progress);
		case SUBMITTED: return resources.getString(R.string.status_submitted);
		case RETURNED: return resources.getString(R.string.status_returned);
		case APPROVED: return resources.getString(R.string.status_approved);
		default: return null;
		}
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Boolean isEditable() {
		return (status == Status.IN_PROGRESS || status == Status.RETURNED);
	}

	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;	
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return If the claim has expense items, this method returns a string
	 * containing the totaled amounts of all of the currencies in its expense
	 * items. For example: "USD 5.64, CAD 100.79". If there are no expense
	 * items, this method returns null.
	 */
	public String getSummarizedAmounts() {
		if (items.size() == 0) return null;

		HashMap<CurrencyUnit, Money> unitToMoneyMap = new HashMap<CurrencyUnit, Money>();
		for (ExpenseItem item : items) {
			Money amount = item.getAmount();
			CurrencyUnit unit = amount.getCurrencyUnit();

			Money current = unitToMoneyMap.get(unit);
			if (current == null) {
				current = Money.zero(unit);
			}

			unitToMoneyMap.put(unit, current.plus(item.getAmount()));
		}

		StringBuilder builder = new StringBuilder();
		for (Money amount : unitToMoneyMap.values()) {
			builder.append(amount.toString());
			builder.append("\n");
		}
		// Remove trailing newline
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
	
	/**
	 * Creates a plain text representation of the expense claim;
	 * @param context Context to use for getting localized string resources.
	 * @return Plain text representation of the expense claim suitable for sending in an email.
	 */
	public String getPlainText(Context context) {
		Resources resources = context.getResources();
		StringBuilder builder = new StringBuilder(name + "\n");
		if (description.length() > 0) {
			builder.append(resources.getString(R.string.description) + ": " + description + "\n");
		}
		DateFormat dateFormat = DateFormat.getDateInstance();
		builder.append(resources.getString(R.string.dates) + ": " + dateFormat.format(startDate) + " - " + dateFormat.format(endDate) + "\n");
		builder.append(resources.getString(R.string.status) + ": " + getStatusString(resources) + "\n\n");
		builder.append(resources.getString(R.string.expense_items) + ":\n");
		for (ExpenseItem item : items) {
			builder.append(item.getPlainText(context));
		}
		return builder.toString();
	}

	//================================================================================
	// Object
	//================================================================================

	public String toString() {
		return name;
	}

	//================================================================================
	// Comparable
	//================================================================================

	public int compareTo(ExpenseClaim claim) {
		String sort_type = ExpenseClaimSortType.getSortType();
		String sort_time = ExpenseClaimSortType.getSortTime();
		
		String type1 = "Date of Travel";
		String type2 = "Order of Entry";
		
		String time1 = "Ascending";
		String time2 = "Descending";
		
		// Date of Travel
		if (type1.equals(sort_type)) {
			if (time1.equals(sort_time)) {
				// Ascending order
				return getStartDate().compareTo(claim.getStartDate());
			} else {
				// Descending order
				return getStartDate().compareTo(claim.getStartDate());
			}
		// Order of Entry
		} else if (type2.equals(sort_type)) {
			if (time1.equals(sort_time)) {
				// Ascending order
				return getCreationDate().compareTo(claim.getCreationDate());
			} else {
				// Descending order
				return getStartDate().compareTo(claim.getStartDate());
			}
		}
		return getStartDate().compareTo(claim.getStartDate());
		
		
	}


}
