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

import org.joda.money.Money;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Activity for editing the attributes of an expense item.
 */
public class ExpenseItemEditActivity extends ExpenseItemAddActivity {
	
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Intent key for the position of the {@link ExpenseItem} object.
	 */
	public static final String EXTRA_EXPENSE_ITEM_POSITION = "com.indragie.cmput301as1.EXPENSE_ITEM_POSITION";
	
	/**
	 * Intent key for editting an {@link ExpenseItem} object.
	 */
	public static final String EXTRA_EXPENSE_ITEM_EDITABLE = "com.indragie.cmput301as1.EXPENSE_ITEM_EDITABLE";
	
	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// The superclass shows a custom action bar with Cancel and Done buttons by default
		// and that needs to be disabled to show the Up button.
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

		Intent intent = getIntent();
		ExpenseItem item = (ExpenseItem)getIntent().getSerializableExtra(EXTRA_EXPENSE_ITEM);
		Boolean editable = intent.getBooleanExtra(EXTRA_EXPENSE_ITEM_EDITABLE, false);
		
		receiptFileUri = Uri.parse(item.getReceipt());
		
		setTitle(item.getName());
		setupFields(item, editable);
		
	}

	private void setupFields(ExpenseItem item, Boolean editable) {
		Money amount = item.getAmount();

		nameField.setText(item.getName());
		nameField.setEnabled(editable);
		
		descriptionField.setText(item.getDescription());
		descriptionField.setEnabled(editable);
		
		SpinnerUtils.setSelectedItem(currencySpinner, amount.getCurrencyUnit().toString());
		currencySpinner.setEnabled(editable);
		
		amountField.setText(amount.getAmount().toString());
		amountField.setEnabled(editable);
		
		dateField.setDate(item.getDate());
		dateField.setEnabled(editable);
		
		SpinnerUtils.setSelectedItem(categorySpinner, item.getCategory());
		categorySpinner.setEnabled(editable);
		
		if (receiptFileUri != null) {
			Drawable receiptPic = Drawable.createFromPath(receiptFileUri.getPath());
			receiptButton.setImageDrawable(receiptPic);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onDone();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		// Changes should persist even when the back button is pressed,
		// since this is for editing and not adding.
		onDone();
	}

	//================================================================================
	// ExpenseItemAddActivity Overrides
	//================================================================================

	@Override
	protected Intent getResultIntent()  {
		Intent intent = super.getResultIntent();
		intent.putExtra(EXTRA_EXPENSE_ITEM_POSITION, getIntent().getIntExtra(EXTRA_EXPENSE_ITEM_POSITION, -1));
		return intent;
	}
}
