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

import android.content.Context;
import android.content.res.Resources;

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
	
	private static final String BULLET = "\u2022 ";
	private static final String INDENTED_BULLET = "\t\u25e6 ";
	
	/**
	 * Creates a plain text representation of the expense item.
	 * @param context Context to use for getting localized string resources.
	 * @return Plain text representation of the expense item suitable for sending in an email.
	 */
	public String getPlainText(Context context) {
		Resources resources = context.getResources();
		StringBuilder builder = new StringBuilder(BULLET + name + "\n");
		
		if (description.length() > 0) {
			builder.append(attributeString(resources.getString(R.string.description), description));
		}
		builder.append(attributeString(resources.getString(R.string.category), category));
		builder.append(attributeString(resources.getString(R.string.amount), amount.toString()));
		builder.append(attributeString(resources.getString(R.string.date), DateFormat.getDateInstance().format(date)));
		return builder.toString();
	}
	
	/**
	 * Helper function used in getPlainText() to create strings representing
	 * each of the attributes of the expense item.
	 */
	private String attributeString(String name, String value) {
		return INDENTED_BULLET + name + ": " + value + "\n";
	}

	//================================================================================
	// Comparable
	//================================================================================
	
	@Override
	public int compareTo(ExpenseItem item) {
		return this.getDate().compareTo(item.getDate());
	}
}
