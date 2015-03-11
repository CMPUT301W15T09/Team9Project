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

import java.io.File;

import org.joda.money.*;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

/**
 * Activity that presents a user interface for entering information to 
 * create a new expense item.
 */
public class ExpenseItemAddActivity extends AddActivity {
	
	protected Uri receiptFileUri;

	//================================================================================
	// Constants
	//================================================================================
	public static final String EXTRA_EXPENSE_ITEM = "com.indragie.cmput301as1.EXPENSE_ITEM";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int REQUEST_IMAGE_CAPTURE = 100;

	//================================================================================
	// Properties
	//================================================================================

	protected EditText nameField;
	protected EditText descriptionField;
	protected EditText amountField;
	protected DateEditText dateField;
	protected Spinner categorySpinner;
	protected Spinner currencySpinner;
	protected ImageButton receiptButton;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_item_add);

		nameField = (EditText)findViewById(R.id.et_name);
		descriptionField = (EditText)findViewById(R.id.et_description);
		amountField = (EditText)findViewById(R.id.et_amount);
		dateField = (DateEditText)findViewById(R.id.et_date);

		categorySpinner = (Spinner)findViewById(R.id.sp_category);
		SpinnerUtils.configureSpinner(this, categorySpinner, R.array.categories_array);

		currencySpinner = (Spinner)findViewById(R.id.sp_currency);
		SpinnerUtils.configureSpinner(this, currencySpinner, R.array.currency_array);
		
		receiptButton = (ImageButton) findViewById(R.id.btn_receipt);
		receiptButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				takePhoto();				
			}
		});
		
		receiptButton.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}

	//================================================================================
	// Subclass Overrides
	//================================================================================

	protected Intent getResultIntent()  {
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
		if (!receiptFileUri.equals(null)) {
			item.setReceiptUri(receiptFileUri);
		}

		Intent intent = new Intent();
		intent.putExtra(EXTRA_EXPENSE_ITEM, item);
		return intent;
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
		setResult(RESULT_OK, getResultIntent());
		finish();
	}
	
	//================================================================================
	// Receipt Handling
	//================================================================================
	
	protected void takePhoto() {
	
		// create folder to store receipt images
		String folder = Environment.getExternalStorageDirectory()
									.getAbsolutePath() + "/ExpenseReceipts";
		File receiptFolder = new File(folder);
		if (!receiptFolder.exists()) {
			receiptFolder.mkdir();
		}
		
		// create image file Uri
		String receiptFilePath = folder + "/"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg";
		File receiptFile = new File(receiptFilePath);
		receiptFileUri = Uri.fromFile(receiptFile);
		
		// create and dispatch picture intent 
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, receiptFileUri);
		
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			receiptButton = (ImageButton) findViewById(R.id.btn_receipt);
			Drawable receiptPic = Drawable.createFromPath(receiptFileUri.getPath());
			receiptButton.setImageDrawable(receiptPic);
		}
		
	}
}

