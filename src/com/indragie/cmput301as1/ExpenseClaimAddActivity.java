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

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

/**
 * Activity for creating a new expense claim by filling in the name, description
 * and start/end dates.
 */
public class ExpenseClaimAddActivity extends EditingActivity {
	public static final String EXTRA_EXPENSE_CLAIM = "com.indragie.cmput301as1.EXPENSE_CLAIM";
	
	// Cache references to these views so that they don't have to be found
	// a second time when the ExpenseClaim object is created.
	private DateEditText startDateField;
	private DateEditText endDateField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claim_header);
		
		startDateField = (DateEditText)findViewById(R.id.et_start_date);
		endDateField = (DateEditText)findViewById(R.id.et_end_date);
	}

	@Override
	protected void onCancel() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}

	@Override
	protected void onDone() {
		EditText nameField = (EditText)findViewById(R.id.et_name);
		EditText descriptionField = (EditText)findViewById(R.id.et_description);
		ExpenseClaim claim = new ExpenseClaim(
			nameField.getText().toString(), 
			descriptionField.getText().toString(), 
			startDateField.getDate(), 
			endDateField.getDate()
		);
		
		Intent intent = new Intent();
		intent.putExtra(EXTRA_EXPENSE_CLAIM, claim);
		setResult(RESULT_OK, intent);
		finish();
	}
}
