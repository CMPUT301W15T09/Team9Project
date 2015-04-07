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

import java.math.RoundingMode;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import com.google.android.gms.location.places.Place;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Activity that presents a user interface for entering information to create a
 * new expense item.
 */
public class ExpenseItemAddActivity extends PlacePickerParentActivity {
	// ================================================================================
	// Constants
	// ================================================================================
	/**
	 * Intent key for the position of the {@link ExpenseItem} object.
	 */
	public static final String EXTRA_EXPENSE_ITEM = "com.indragie.cmput301as1.EXPENSE_ITEM";

	/**
	 * Request code for starting {@link Camera}.
	 */
	protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	/**
	 * String array of dialog prompts.
	 */
	protected String[] dialogOptions;

	// ================================================================================
	// Properties
	// ================================================================================

	/**
	 * Field that displays the name of the expense item.
	 */
	protected EditText nameField;

	/**
	 * Field that displays the description of the expense item.
	 */
	protected EditText descriptionField;

	/**
	 * Field for the location where the expense was incurred.
	 */
	protected EditText locationField;

	/**
	 * Field that displays the cost amount of the expense item.
	 */
	protected EditText amountField;

	/**
	 * Field that displays the date that the expense item was incurred.
	 */
	protected DateEditText dateField;

	/**
	 * Dropdown menu that displays the category of the expense item.
	 */
	protected Spinner categorySpinner;

	/**
	 * Dropdown menu that displays the currency of the expense item transaction.
	 */
	protected Spinner currencySpinner;

	/**
	 * Button that displays the receipt image taken, if any.
	 */
	protected ImageButton receiptButton;

	/**
	 * The location where the expense was incurred.
	 */
	protected Geolocation expenseLocation;

	/**
	 * Uri reference of the receipt image.
	 */
	protected Uri receiptFileUri;

	/**
	 * Uri reference of the receipt image that is in the process of being taken.
	 */
	private Uri newReceiptFileUri;

	/**
	 * Incompleteness flag.
	 */
	protected boolean incomplete;

	/**
	 * Controller used for handling receipt images.
	 */
	private ExpenseItemReceiptController receiptController;

	// ================================================================================
	// Activity Callbacks
	// ================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_item_add);
		dialogOptions = getResources().getStringArray(
				R.array.receipt_dialog_array);
		receiptController = new ExpenseItemReceiptController();

		ActionBarUtils.showCancelDoneActionBar(this,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onCancel();
					}
				}, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onDone();
					}
				});

		nameField = (EditText) findViewById(R.id.et_name);
		descriptionField = (EditText) findViewById(R.id.et_description);
		locationField = (EditText) findViewById(R.id.et_location);
		amountField = (EditText) findViewById(R.id.et_amount);
		dateField = (DateEditText) findViewById(R.id.et_date);

		categorySpinner = (Spinner) findViewById(R.id.sp_category);
		SpinnerUtils.configureSpinner(this, categorySpinner,
				R.array.categories_array);

		currencySpinner = (Spinner) findViewById(R.id.sp_currency);
		SpinnerUtils.configureSpinner(this, currencySpinner,
				R.array.currency_array);

		receiptButton = (ImageButton) findViewById(R.id.btn_receipt);
		receiptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startImagePickerDialog();
			}
		});

		incomplete = false;

		final OnPlacePickedListener listener = new OnPlacePickedListener() {

			@Override
			public void onPlacePickerCanceled() {
				locationField.clearFocus();
			}

			@Override
			public void onPlacePicked(Place place) {
				expenseLocation = new Geolocation(place);
				locationField.setText(expenseLocation.toString());
				locationField.clearFocus();
			}
		};

		locationField
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							openPlacePicker(listener);
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.expense_item_edit, menu);
		menu.findItem(R.id.action_set_incomplete).setChecked(incomplete);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onDone();
			return true;
		case R.id.action_set_incomplete:
			item.setChecked(!item.isChecked());
			incomplete ^= true;
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// ================================================================================
	// Subclass Overrides
	// ================================================================================

	/**
	 * Creates a new expense item based on the contents of the data fields in
	 * the user interface.
	 * 
	 * @return The new expense item.
	 */
	private ExpenseItem buildExpenseItem() {
		Money amount = Money.of(
				CurrencyUnit.of(currencySpinner.getSelectedItem().toString()),
				Float.parseFloat(amountField.getText().toString()),
				RoundingMode.HALF_UP);
		ExpenseItem item = new ExpenseItem(nameField.getText().toString(),
				descriptionField.getText().toString(), categorySpinner
						.getSelectedItem().toString(), amount,
				dateField.getDate());
		if (receiptFileUri != null) {
			item.setReceiptUri(receiptFileUri);
		}
		item.setIncomplete(incomplete);
		item.setLocation(expenseLocation);
		return item;
	}

	/**
	 * @return The intent to be passed back to the parent activity.
	 */
	protected Intent getResultIntent() {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_EXPENSE_ITEM, buildExpenseItem());
		return intent;
	}

	protected void onCancel() {
		setReceiptFileUri(null); // Delete receipt image if it exists
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}

	protected void onDone() {
		setResult(RESULT_OK, getResultIntent());
		finish();
	}

	// ================================================================================
	// Receipt-Image Capture and Handling
	// ================================================================================

	/**
	 * Starts an activity to take a receipt image.
	 */
	protected void takePhoto() {
		newReceiptFileUri = receiptController.constructNewReceiptImageUri();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, newReceiptFileUri);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			if (receiptController.postProcessReceiptImage(newReceiptFileUri)) {
				setReceiptFileUri(newReceiptFileUri);
				newReceiptFileUri = null;

				Drawable receiptPic = Drawable.createFromPath(receiptFileUri
						.getPath());
				receiptButton.setImageDrawable(receiptPic);

				Toast.makeText(getApplicationContext(),
						getString(R.string.toast_receipt_success),
						Toast.LENGTH_SHORT).show();
				return;
			}
		} else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
		newReceiptFileUri = null;
		Toast.makeText(getApplicationContext(),
				getString(R.string.toast_receipt_failed), Toast.LENGTH_SHORT)
				.show();
		}
	}

	/**
	 * Sets the {@link ExpenseItemAddActivity#receiptFileUri} and deletes the
	 * receipt at the existing URI, if it exists.
	 * 
	 * @param receiptFileUri
	 *            The new receipt file URI.
	 */
	private void setReceiptFileUri(Uri receiptFileUri) {
		if (this.receiptFileUri != null) {
			receiptController.deleteReceiptImage(this.receiptFileUri);
		}
		this.receiptFileUri = receiptFileUri;
	}

	// ================================================================================
	// Camera + Gallery Dialogue
	// ================================================================================

	/*
	 * Elements of method startDialog borrowed from
	 * http://www.theappguruz.com/blog
	 * /android-take-photo-camera-gallery-code-sample/ last accessed: 03/12/2015
	 * 3:02pm
	 */
	/**
	 * Opens an alert dialog to allow the user to select a task.
	 */
	protected void startImagePickerDialog() {
		AlertDialog.Builder openDialog = new AlertDialog.Builder(this);
		openDialog.setTitle("Select an action");
		openDialog.setItems(dialogOptions,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int item) {

						if (dialogOptions[item].equals("Take Photo")) {
							takePhoto();
						}

						else if (dialogOptions[item].equals("Open in Gallery")) {
							if (receiptFileUri == null) {
								dialog.dismiss();
								Toast.makeText(
										getApplicationContext(),
										getString(R.string.toast_receipt_nonexistent),
										Toast.LENGTH_SHORT).show();
							} else {
								Intent intent = new Intent();
								intent.setAction(Intent.ACTION_VIEW);
								intent.setDataAndType(receiptFileUri, "image/*");
								startActivity(intent);
							}
						}

						else if (dialogOptions[item].equals("Delete Photo")) {
							setReceiptFileUri(null);
							Toast.makeText(getApplicationContext(),
									getString(R.string.toast_receipt_deleted),
									Toast.LENGTH_SHORT).show();
							receiptButton
									.setImageResource(R.drawable.ic_action_search);
						}

						else if (dialogOptions[item].equals("Cancel")) {
							dialog.dismiss();
						}
					}
				});
		openDialog.show();
	}
}
