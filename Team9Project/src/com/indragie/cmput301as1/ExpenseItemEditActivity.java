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
import android.view.Menu;

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
	 * Intent key for editing an {@link ExpenseItem} object.
	 */
	public static final String EXTRA_EXPENSE_ITEM_EDITABLE = "com.indragie.cmput301as1.EXPENSE_ITEM_EDITABLE";
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * Flag for setting the activity fields as editable or not.
	 */
	private static boolean editable;
	
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
		editable = intent.getBooleanExtra(EXTRA_EXPENSE_ITEM_EDITABLE, false);
		if (item.getReceiptPath() != null) {
			receiptFileUri = Uri.parse(item.getReceiptPath());
		}
		incomplete = item.isIncomplete();
		
		setTitle(item.getName());
		setupFields(item, editable);
	}
	
	/**
	 * Sets up the fields of the expense item to edit.
	 * @param item The expense item.
	 * @param editable Boolean if fields are editable. 
	 */
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
		
		receiptButton.setClickable(editable);
		if (receiptFileUri != null) {
			Drawable receiptPic = Drawable.createFromPath(receiptFileUri.getPath());
			receiptButton.setImageDrawable(receiptPic);
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.expense_item_edit, menu);
		menu.findItem(R.id.action_set_incomplete).setChecked(incomplete).setEnabled(editable); 
		return true;
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
