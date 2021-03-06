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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Activity that presents a user interface for entering information to 
 * create a new expense claim.
 */
public class ExpenseClaimAddActivity extends Activity {
	//================================================================================
	// Constants
	//================================================================================
	public static final String EXTRA_EXPENSE_CLAIM = "com.indragie.cmput301as1.EXPENSE_CLAIM";
	public static final String EXTRA_EXPENSE_CLAIM_USER = "com.indragie.cmput301as1.EXTRA_EXPENSE_CLAIM_USER";

	//================================================================================
	// Properties
	//================================================================================
	/**
	 * The name of the expense claim.
	 */
	private EditText nameField;
	/**
	 * The description of the expense claim.
	 */
	private EditText descriptionField;
	/**
	 * The start date of the expense claim.
	 */
	private DateEditText startDateField;
	/**
	 * The end date of the expense claim.
	 */
	private DateEditText endDateField;
	private User user;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_header);
		
		Intent intent = getIntent();
		user = (User)intent.getSerializableExtra(EXTRA_EXPENSE_CLAIM_USER);

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
					setResult(RESULT_OK, constructResultIntent());
					finish();
				}
			}
		);

		nameField = (EditText)findViewById(R.id.et_name);
		nameField.setText(user.getName());
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
	
	/**
	 * Creates a new expense claim to put into another activity.
	 * @return The intent with the new expense claim.
	 */
	private Intent constructResultIntent() {
		ExpenseClaim claim = new ExpenseClaim(
			descriptionField.getText().toString(), 
			startDateField.getDate(), 
			endDateField.getDate(),
			user,
			ExpenseClaim.Status.IN_PROGRESS
		);

		Intent intent = new Intent();
		intent.putExtra(EXTRA_EXPENSE_CLAIM, claim);
		return intent;
	}
}
