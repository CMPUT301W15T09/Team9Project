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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.Toast;

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
	protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	protected static final int REQUEST_IMAGE_CAPTURE = 200;
	protected final CharSequence[] dialogOptions = { "Take Photo", "Open in Gallery", 
			"Delete Photo", "Cancel"};

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
				startDialog();				
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
		if (receiptFileUri != null) {
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
	// Receipt-Image Capture and Handling
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
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
			receiptButton = (ImageButton) findViewById(R.id.btn_receipt);
			Drawable receiptPic = Drawable.createFromPath(receiptFileUri.getPath());
			receiptButton.setImageDrawable(receiptPic);
			Toast.makeText(getApplicationContext(), "Receipt saved", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(getApplicationContext(), "Save unsuccessful", Toast.LENGTH_SHORT).show();
		}
	}
	
	//================================================================================
	// Camera + Gallery Dialogue
	//================================================================================
	
	
	/** method startDialog borrowed from 
	 *  http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample/
	 *  last accessed: 03/12/2015 3:02pm
	 */
	protected void startDialog() {
		AlertDialog.Builder openDialog = new AlertDialog.Builder(this);
		openDialog.setTitle("Select an action");
		openDialog.setItems(dialogOptions, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int item) {
				
				if (dialogOptions[item].equals("Take Photo")) {
					takePhoto();
				}
				
				else if (dialogOptions[item].equals("Open in Gallery")) {
					if (receiptFileUri.equals(null)) {
						dialog.dismiss();
						Toast.makeText(getApplicationContext(), "No saved photo", Toast.LENGTH_SHORT).show();
					}
					else {
						Intent intent = new Intent(Intent.ACTION_VIEW, receiptFileUri);
						startActivity(intent);
						
					}
				}
				
				else if (dialogOptions[item].equals("Delete Photo")) {
					receiptFileUri = null;
					receiptButton.clearAnimation();
				}
				
				else if (dialogOptions[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		openDialog.show();
	}
}

