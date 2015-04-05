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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Activity that presents a user interface for entering information to 
 * create a new expense item.
 */
public class ExpenseItemAddActivity extends Activity {
	//================================================================================
	// Constants
	//================================================================================
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
	
	/**
	 * Maximum size for receipt image files.
	 */
    protected static final int IMAGE_MAX_SIZE = 65536; 

	//================================================================================
	// Properties
	//================================================================================

	/**
	 * Field that displays the name of the expense item.
	 */
	protected EditText nameField;

	/**
	 * Field that displays the description of the expense item.
	 */
	protected EditText descriptionField;

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
	 * Uri reference of the receipt image.
	 */
	protected Uri receiptFileUri;
	
	/**
	 * Incompleteness flag.
	 */
	protected boolean incomplete;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_item_add);
		dialogOptions = getResources().getStringArray(R.array.receipt_dialog_array);

		ActionBarUtils.showCancelDoneActionBar(
			this,
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onCancel();
				}
			},
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onDone();
				}
			}
		);

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
				startImagePickerDialog();				
			}
		});
		
		incomplete = false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.expense_item_edit, menu);
		menu.findItem(R.id.action_set_incomplete).setChecked(incomplete); 
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
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

	//================================================================================
	// Subclass Overrides
	//================================================================================
	
	/**
	 * Creates a new expense item based on the contents of the
	 * data fields in the user interface.
	 * @return The new expense item.
	 */
	private ExpenseItem buildExpenseItem() {
		Money amount = Money.of(
			CurrencyUnit.of(currencySpinner.getSelectedItem().toString()), 
			Float.parseFloat(amountField.getText().toString()),
			RoundingMode.HALF_UP
		);
		ExpenseItem item = new ExpenseItem(
			nameField.getText().toString(),
			descriptionField.getText().toString(),
			categorySpinner.getSelectedItem().toString(),
			amount,
			dateField.getDate()
		);
		if (receiptFileUri != null) {
			item.setReceipt(receiptFileUri.toString());
		}
		item.setIncomplete(incomplete);
		return item;
	}
	
	/**
	 * @return The intent to be passed back to the parent activity.
	 */
	protected Intent getResultIntent()  {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_EXPENSE_ITEM, buildExpenseItem());
		return intent;
	}

	protected void onCancel() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}

	protected void onDone() {
		setResult(RESULT_OK, getResultIntent());
		finish();
	}

	//================================================================================
	// Receipt-Image Capture and Handling
	//================================================================================

	/**
	 * Sets up an external storage directory for receipt images and
	 * creates a file to store a new one, then passes an intent
	 * to a Camera application to take a photo.
	 */
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

	/*  Elements of the following methods borrowed from
	 *  http://stackoverflow.com/questions/3331527/android-resize-a-large-bitmap-file-to-scaled-output-file
	 *  last accessed: 03/15/15 3:29pm
	 */ 
	/**
	 * Resizes a .jpeg file to be under the maximum size
	 * @param imageFileUri The Uri of the image file to be resized
	 */
	public void scaleJpeg(Uri jpegFileUri) {
		try {
			final int IMAGE_MAX_SIZE = 65536; 
		    InputStream fin = new FileInputStream(jpegFileUri.getPath());

		    // Decode image size
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(fin, null, options);
		    fin.close();

		    int scale = calculateScale(options, IMAGE_MAX_SIZE);
		    
		    if (scale > 1) {
		    	Bitmap bmp = null;
		    	fin = new FileInputStream(jpegFileUri.getPath());
		    	// scale to max possible inSampleSize that still yields an image
		        // larger than target
		        scale--;
		        options = new BitmapFactory.Options();
		        options.inSampleSize = scale;
		        bmp = BitmapFactory.decodeStream(fin, null, options);
		        
		        resizeBitmap(bmp);
		        fin.close();
		        
		        FileOutputStream fos = new FileOutputStream(jpegFileUri.getPath());
		        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		        fos.flush();
		        fos.close();
		    }

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param options An options object created from the image to be resized.
	 * @param maxSize Maximum size of a receipt image.
	 * @return the scale factor.
	 */
	protected int calculateScale(BitmapFactory.Options options, int maxSize) {
		int scale = 1;
	    while ((options.outWidth * options.outHeight) * (1 / Math.pow(scale, 2)) > 
	          maxSize) {
	       scale++;
	    }
	    return scale;
	}
	
	/**
	 * Resizes the dimensions of the scaled bitmap.
	 * @param bmp the bitmap to be resized.
	 * @return the resized bitmap.
	 */
	protected Bitmap resizeBitmap(Bitmap bmp) {
        // resize to desired dimensions
		int height = bmp.getHeight();
        int width = bmp.getWidth();

        double y = Math.sqrt(IMAGE_MAX_SIZE
                / (((double) width) / height));
        double x = (y / height) * width;

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, (int) x, 
           (int) y, true);
        bmp = scaledBitmap;
		return bmp;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
			scaleJpeg(receiptFileUri);
			receiptButton = (ImageButton) findViewById(R.id.btn_receipt);
			Drawable receiptPic = Drawable.createFromPath(receiptFileUri.getPath());
			receiptButton.setImageDrawable(receiptPic);
			Toast.makeText(getApplicationContext(), getString(R.string.toast_receipt_success), Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(getApplicationContext(), getString(R.string.toast_receipt_failed), Toast.LENGTH_SHORT).show();
		}
	}

	//================================================================================
	// Camera + Gallery Dialogue
	//================================================================================

	/* 
	 *  Elements of method startDialog borrowed from 
	 *  http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample/
	 *  last accessed: 03/12/2015 3:02pm
	 */
	/**
	 * Opens an alert dialog to allow the user to select a task.
	 */
	protected void startImagePickerDialog() {
		AlertDialog.Builder openDialog = new AlertDialog.Builder(this);
		openDialog.setTitle("Select an action");
		openDialog.setItems(dialogOptions, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {

				if (dialogOptions[item].equals("Take Photo")) {
					takePhoto();
				}

				else if (dialogOptions[item].equals("Open in Gallery")) {
					if (receiptFileUri == null) {
						dialog.dismiss();
						Toast.makeText(getApplicationContext(), 
								getString(R.string.toast_receipt_nonexistent), Toast.LENGTH_SHORT).show();
					}
					else {
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setDataAndType(receiptFileUri, "image/*");
						startActivity(intent);
					}
				}

				else if (dialogOptions[item].equals("Delete Photo")) {
					receiptFileUri = null;
					Toast.makeText(getApplicationContext(), 
							getString(R.string.toast_receipt_deleted), Toast.LENGTH_SHORT).show();
					receiptButton.setImageResource(R.drawable.ic_action_search);
				}

				else if (dialogOptions[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		openDialog.show();
	}
}

