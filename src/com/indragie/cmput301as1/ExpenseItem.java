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

	//================================================================================
	// Constructors
	//================================================================================

	public ExpenseItem(String name, String description, String category, Money amount, Date date) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.amount = amount;
		this.date = date;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Money getAmount() {
		return amount;
	}

	public void setAmount(Money amount) {
		this.amount = amount;
	}
	
	/**
	 * @return HTML representation of the expense item suitable for sending in an email.
	 */
	public String getHTMLString() {
		StringBuilder builder = new StringBuilder(name + "<ul>");
		if (description.length() > 0) {
			builder.append("<li><b>Description:</b> " + description + "</li>");
		}
		builder.append("<li><b>Category:</b> " + category + "</li>");
		builder.append("<li><b>Amount:</b> " + amount.toString() + "</li>");
		builder.append("<li><b>Date:</b> " + DateFormat.getDateInstance().format(date) + "</li></ul>");
		return builder.toString();
	}

	//================================================================================
	// Comparable
	//================================================================================

	public int compareTo(ExpenseItem item) {
		return this.getDate().compareTo(item.getDate());
	}
}
