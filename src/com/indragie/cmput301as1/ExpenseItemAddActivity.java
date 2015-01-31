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

import org.joda.money.*;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Activity for entering information to create a new expense item.
 */
public class ExpenseItemAddActivity extends EditingActivity {
	public static final String EXTRA_EXPENSE_ITEM = "com.indragie.cmput301as1.EXPENSE_ITEM";
	
	protected EditText nameField;
	protected EditText descriptionField;
	protected EditText amountField;
	protected DateEditText dateField;
	protected Spinner categorySpinner;
	protected Spinner currencySpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_item_add);
		
		nameField = (EditText)findViewById(R.id.et_name);
		descriptionField = (EditText)findViewById(R.id.et_description);
		amountField = (EditText)findViewById(R.id.et_amount);
		dateField = (DateEditText)findViewById(R.id.et_date);
		
		categorySpinner = (Spinner)findViewById(R.id.sp_category);
		configureSpinner(categorySpinner, R.array.categories_array);
		
		currencySpinner = (Spinner)findViewById(R.id.sp_currency);
		configureSpinner(currencySpinner, R.array.currency_array);
	}
	
	private void configureSpinner(Spinner spinner, int resourceID) {
		// From http://developer.android.com/guide/topics/ui/controls/spinner.html
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        resourceID, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	@Override
	protected void onCancel() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}

	@Override
	protected void onDone() {
		Money amount = Money.of(
			CurrencyUnit.of(currencySpinner.getSelectedItem().toString()), 
			Float.parseFloat(amountField.getText().toString())
		);
		ExpenseItem item = new ExpenseItem(
			nameField.getText().toString(),
			descriptionField.getText().toString(),
			categorySpinner.getSelectedItem().toString(),
			amount,
			dateField.getDate()
		);
		
		Intent intent = new Intent();
		intent.putExtra(EXTRA_EXPENSE_ITEM, item);
		setResult(RESULT_OK, intent);
		finish();
		finish();
	}
}
