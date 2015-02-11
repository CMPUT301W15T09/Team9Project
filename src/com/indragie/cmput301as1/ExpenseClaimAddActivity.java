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

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Activity that presents a user interface for entering information to 
 * create a new expense claim.
 */
public class ExpenseClaimAddActivity extends AddActivity {
	//================================================================================
	// Constants
	//================================================================================
	public static final String EXTRA_EXPENSE_CLAIM = "com.indragie.cmput301as1.EXPENSE_CLAIM";

	//================================================================================
	// Properties
	//================================================================================

	private EditText nameField;
	private EditText descriptionField;
	private DateEditText startDateField;
	private DateEditText endDateField;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_header);

		nameField = (EditText)findViewById(R.id.et_name);
		descriptionField = (EditText)findViewById(R.id.et_description);

		startDateField = (DateEditText)findViewById(R.id.et_start_date);
		startDateField.setOnDateChangedListener(new DateEditText.OnDateChangedListener() {
			@Override
			public void onDateChanged(DateEditText view, Date date) {
				endDateField.setMinDate(date);
			}
		});

		endDateField = (DateEditText)findViewById(R.id.et_end_date);
		endDateField.setOnDateChangedListener(new DateEditText.OnDateChangedListener() {
			@Override
			public void onDateChanged(DateEditText view, Date date) {
				startDateField.setMaxDate(date);
			}
		});
	}

	//================================================================================
	// EditingActivity
	//================================================================================

	@Override
	protected void onCancel() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}

	@Override
	protected void onDone() {
		setResult(RESULT_OK, constructResultIntent());
		finish();
	}

	private Intent constructResultIntent() {
		ExpenseClaim claim = new ExpenseClaim(
			nameField.getText().toString(), 
			descriptionField.getText().toString(), 
			startDateField.getDate(), 
			endDateField.getDate(),
			ExpenseClaim.Status.IN_PROGRESS
		);

		Intent intent = new Intent();
		intent.putExtra(EXTRA_EXPENSE_CLAIM, claim);
		return intent;
	}
}
