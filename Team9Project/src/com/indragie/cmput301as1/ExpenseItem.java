/* 
 * Copyright (C) 2015 Indragie Karunaratne, Brandon Williams
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
import java.util.Date;

import org.joda.money.Money;

/**
 * Model object representing a single item on an expense claim.
 */
public class ExpenseItem implements Serializable, Comparable<ExpenseItem> {
	private static final long serialVersionUID = -5923561360068724438L;

	//================================================================================
	// Properties
	//================================================================================

	/**
	 * Name of the expense item.
	 */
	private String name;
	
	/**
	 * Date on which the expense occurred.
	 */
	private Date date;
	
	/**
	 * Category of the expense (e.g. air fare, accommodation, etc.)
	 */
	private String category;
	
	/**
	 * Textual description of the expense.
	 */
	private String description;
	
	/**
	 * Encapsulates the amount spent and the unit of currency.
	 * (Money class is from the Joda Money library)
	 */
	private Money amount;
	
	/**
	 * String representation of a Uri of a receipt image.
	 */
	private String receipt;
	/**
	 * Incompleteness indicator.
	 */
	private boolean incomplete;

	//================================================================================
	// Constructors
	//================================================================================

	public ExpenseItem(String name, String description, String category, Money amount, Date date) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.amount = amount;
		this.date = date;
		this.incomplete = false;
	}

	//================================================================================
	// Accessors
	//================================================================================

	/**
	 * Gets the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * @param name The name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the date.
	 * @return The date.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * @param date The date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Gets the category.
	 * @return The category.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 * @param category The category to set to.
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Gets the description.
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * @param description The description to set to.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return String representation of a Uri of a receipt image.
	 */
	public String getReceipt() {
		return receipt;
	}

	/**
	 * Sets the string representation of a Uri of a receipt image.
	 * @param receipt String representation of a Uri of a receipt image.
	 */
	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}
	
	/**
	 * Gets the amount of money.
	 * @return The amount of money.
	 */
	public Money getAmount() {
		return amount;
	}
	
	/**
	 * Sets the amount of money.
	 * @param amount The amount to set to.
	 */
	public void setAmount(Money amount) {
		this.amount = amount;
	}
	
	/**
	 * Checks if incomplete. 
	 * @return incomplete
	 */
	public boolean isIncomplete() {
		return incomplete;
	}

	/**
	 * Sets the incompleteness flag of the claim.
	 * @param incomplete
	 */
	public void setIncomplete(boolean incomplete) {
		this.incomplete = incomplete;
	}

	//================================================================================
	// Comparable
	//================================================================================
	
	@Override
	public int compareTo(ExpenseItem item) {
		return this.getDate().compareTo(item.getDate());
	}
	
	//================================================================================
	// Object
	//================================================================================
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ExpenseItem other = (ExpenseItem) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
