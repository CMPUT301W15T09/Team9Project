/* 
 * Copyright (C) 2015 Jimmy Ho
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

/**
 * Presents a user interface for changing the sort mode of the expense claims.
 */
public class ExpenseClaimSortActivity extends Activity {
	
	public static final String EXPENSE_CLAIM_SORT = "com.indragie.cmput301as1.EXPENSE_CLAIM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_claim_sort);
		ActionBarUtils.showCancelDoneActionBar(
			this,
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setResult(RESULT_CANCELED, new Intent());
					finish();
				}
			},
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setResult(RESULT_OK, getSortType());
					finish();
				}
			}
		);
	}
	
	/**
	 * gets the intent for the type of sort from the user interface
	 * @return
	 */
	private Intent getSortType() {
		// now we want to grab the sort type from the spinners
		Spinner sortTypeSpinner = (Spinner)findViewById(R.id.sort_type_spinner);
		String sortType = sortTypeSpinner.getSelectedItem().toString();
		
		Spinner sortOrderSpinner = (Spinner) findViewById(R.id.sort_order_spinner);
		String sortOrder = sortOrderSpinner.getSelectedItem().toString();
		
		String type1 = "Date of Travel";
		String type2 = "Order of Entry";
		String time1 = "Ascending";
		// String time2 = "Descending";
		
		Intent intent = new Intent();
		
		// Date of Travel
		if (type1.equals(sortType)) {
			if (time1.equals(sortOrder)) {
				// Ascending order
				intent.putExtra(EXPENSE_CLAIM_SORT, new StartDateAscendingComparator());
			} else {
				// Descending order
				intent.putExtra(EXPENSE_CLAIM_SORT, new StartDateDescendingComparator());
			}
		// Order of Entry
		} else if (type2.equals(sortType)) {
			if (time1.equals(sortOrder)) {
				// Ascending order
				intent.putExtra(EXPENSE_CLAIM_SORT, new CreationDateAscendingComparator());
			} else {
				// Descending order
				intent.putExtra(EXPENSE_CLAIM_SORT, new CreationDateDescendingComparator());
			}
		}
		
		return intent;
	}
		
}
