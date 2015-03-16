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
	/*
	 * Tags contained in the claim.
	 */
	private List<Tag> tags;
	
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

	//================================================================================
	// Constructors
	//================================================================================

	public ExpenseClaim(String name, String description, Date startDate, Date endDate, Status status) {
		this.name = name;
		this.description = description;
		this.items = new ArrayList<ExpenseItem>();
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}

	//================================================================================
	// Accessors
	//================================================================================

	/**
	 * Get the name of the expense claim.
	 * @return The name in type String.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the expense claim
	 * @param name The name in type String.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the description of the expense claim.
	 * @return The description in type String.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the expense claim
	 * @param description The description of type String.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Retrieves the list of expense items.
	 * @return List of type ExpenseItem.
	 */
	public List<ExpenseItem> getItems() {
		return Collections.unmodifiableList(items);
	}
	
	/**
	 * Sets the list of expense items.
	 * @param items The list of expense items of type ExpenseItem.
	 */
	public void setItems(List<ExpenseItem> items) {
		this.items = items;
	}

	/**
	 * Adds a expense item to the expense claim
	 * @param item The expense item to add.
	 */
	public void addItem(ExpenseItem item) {
		items.add(item);
		Collections.sort(items);
	}
	
	/**
	 * Sets a expense item at specified position.
	 * @param position The positioned specified. 
	 * @param item The expense item to put. 
	 */
	public void setItem(int position, ExpenseItem item) {
		items.set(position, item);
		Collections.sort(items);
	}

	/**
	 * Removes a expense item at a specified index.
	 * @param index The index of the expense item to remove.
	 */
	public void removeItem(int index) {
		items.remove(index);
	}

	/**
	 * Gets the status of the claim.
	 * @return A status of type Status.
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * Returns the status of the expense claim.
	 * @param resources The resources of the application of type Resource.
	 * @return The status in type String.
	 */
	public String getStatusString(Resources resources) {
		switch (status) {
		case IN_PROGRESS: return resources.getString(R.string.status_in_progress);
		case SUBMITTED: return resources.getString(R.string.status_submitted);
		case RETURNED: return resources.getString(R.string.status_returned);
		case APPROVED: return resources.getString(R.string.status_approved);
		default: return null;
		}
	}
	
	/**
	 * Set the status of the expense claim.
	 * @param status The status you want to set to of type Status.
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	/**
	 * Checks if expense claim is editable or not. Will be editable if 
	 * the status of the expense claim is in progress or returned.
	 * @return A boolean representing to determine if editable or not. 
	 */
	public Boolean isEditable() {
		return (status == Status.IN_PROGRESS || status == Status.RETURNED);
	}

	/**
	 * Retrieves the start date of the expense claim.
	 * @return A date object of the start date.
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * Sets the start date of the expense claim.
	 * @param startDate The start date in type date.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Retrieves the end date of the expense claim.
	 * @return The end date in type Date.
	 */
	public Date getEndDate() {
		return endDate;	
	}
	
	/**
	 * Sets the end date of the expense claim.
	 * @param endDate The end date in type Date.
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * Adds a Tag object to the expense claim.
	 * @param tag The tag to add.
	 */
	public void addTag(Tag tag) {
		tags.add(tag);
	}
	
	/**
	 * Removes a specified Tag object from the expense claim.
	 * @param tag The tag to remove.
	 */
	public void removeTag(Tag tag) {
		tags.remove(tag);
	}
	
	/**
	 * Removes a Tag object from the expense claim at the specified location.
	 * @param index The index of the tag.
	 */
	public void removeTag(int index) {
		tags.remove(index);
	}
	
	/**
	 * Verifies if the expense claim contains specified Tag object.
	 * @param tag The Tag object you want to verify.
	 * @return A boolean if the expense claim contains the tag or not. 
	 */
	public boolean hasTag(Tag tag) {
		return(tags.contains(tag));
	}
	
	/**
	 * Replaces a old tag with a new tag you want to replace it with. 
	 * @param oldTag The old Tag object you wish to remove. 
	 * @param newTag The new Tag object you wish to replace the old one with.
	 */
	public void replaceTag(Tag oldTag, Tag newTag) {
		tags.set(tags.indexOf(oldTag), newTag);
	}
	
	/**
	 * Gets the entire list of tags in a expense claim.
	 * @return A List of Tag objects.
	 */
	public List<Tag> getTags() {
		return tags; //Does not return unmodifiable List<Tag>
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

	/**
	 * Gets the name of the Expense Claim.
	 * @return A string of the name of the expense claim.
	 */
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExpenseClaim other = (ExpenseClaim) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	//================================================================================
	// Comparable
	//================================================================================
	
	@Override
	public int compareTo(ExpenseClaim claim) {
		return getStartDate().compareTo(claim.getStartDate());
	}
}
