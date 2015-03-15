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

import java.text.DateFormat;

import android.content.Context;
import android.content.res.Resources;

/**
 * Handles the transformation of data from {@link ExpenseClaim} model
 * objects into representations suitable for display in the UI presented by
 * {@link ExpenseClaimDetailActivity}
 */
public class ExpenseClaimDetailController {
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * The current context.
	 */
	private Context context;
	
	/**
	 * Expense claim model.
	 */
	private ExpenseClaim claim;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link ExpenseClaimDetailController}
	 * @param context The current context.
	 * @param claim Expense claim model.
	 */
	public ExpenseClaimDetailController(Context context, ExpenseClaim claim) {
		this.context = context;
		this.claim = claim;
	}
	
	/**
	 * Creates a plain text representation of the expense claim;
	 * @return Plain text representation of the expense claim suitable for sending in an email.
	 */
	public String getPlainText() {
		Resources resources = context.getResources();
		StringBuilder builder = new StringBuilder(claim.getName() + "\n");
		String description = claim.getDescription();
		if (description.length() > 0) {
			builder.append(resources.getString(R.string.description) + ": " + description + "\n");
		}
		DateFormat dateFormat = DateFormat.getDateInstance();
		builder.append(resources.getString(R.string.dates) + ": " + dateFormat.format(claim.getStartDate()) + " - " + dateFormat.format(claim.getEndDate()) + "\n");
		builder.append(resources.getString(R.string.status) + ": " + claim.getStatusString(resources) + "\n\n");
		builder.append(resources.getString(R.string.expense_items) + ":\n");
		for (ExpenseItem item : claim.getItems()) {
			builder.append(getItemPlainText(item));
		}
		return builder.toString();
	}
	
	private static final String BULLET = "\u2022 ";
	private static final String INDENTED_BULLET = "\t\u25e6 ";
	
	/**
	 * Creates a plain text representation of an expense item.
	 * @param item The expense item for which to get a plain text representation.
	 * @return Plain text representation of the expense item suitable for sending in an email.
	 */
	private String getItemPlainText(ExpenseItem item) {
		Resources resources = context.getResources();
		StringBuilder builder = new StringBuilder(BULLET + item.getName() + "\n");
		String description = item.getDescription();
		if (description.length() > 0) {
			builder.append(attributeString(resources.getString(R.string.description), description));
		}
		builder.append(attributeString(resources.getString(R.string.category), item.getCategory()));
		builder.append(attributeString(resources.getString(R.string.amount), item.getAmount().toString()));
		builder.append(attributeString(resources.getString(R.string.date), DateFormat.getDateInstance().format(item.getDate())));
		return builder.toString();
	}
	
	/**
	 * Helper function used in getPlainText() to create strings representing
	 * each of the attributes of the expense item.
	 */
	private String attributeString(String name, String value) {
		return INDENTED_BULLET + name + ": " + value + "\n";
	}
}
