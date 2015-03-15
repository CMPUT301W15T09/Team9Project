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

import org.joda.money.*;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
			item.setReceipt(receiptFileUri.toString());
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
	
	/*  Elements of scalingImage borrowed from
	 *  http://stackoverflow.com/questions/3331527/android-resize-a-large-bitmap-file-to-scaled-output-file
	 *  last accessed: 03/15/15 3:29pm
	 */ 
	protected void scaleImage(Uri imageFileUri) {
		try {
		    final int IMAGE_MAX_SIZE = 65536; 
		    InputStream fin = new FileInputStream(imageFileUri.getPath());

		    // Decode image size
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(fin, null, options);
		    fin.close();

		    int scale = 1;
		    while ((options.outWidth * options.outHeight) * (1 / Math.pow(scale, 2)) > 
		          IMAGE_MAX_SIZE) {
		       scale++;
		    }
		    
		    if (scale > 1) {
		    	Bitmap bmp = null;
		    	fin = new FileInputStream(imageFileUri.getPath());
		    	// scale to max possible inSampleSize that still yields an image
		        // larger than target
		        scale--;
		        options = new BitmapFactory.Options();
		        options.inSampleSize = scale;
		        bmp = BitmapFactory.decodeStream(fin, null, options);

		        // resize to desired dimensions
		        int height = bmp.getHeight();
		        int width = bmp.getWidth();

		        double y = Math.sqrt(IMAGE_MAX_SIZE
		                / (((double) width) / height));
		        double x = (y / height) * width;

		        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, (int) x, 
		           (int) y, true);
		        bmp.recycle();
		        bmp = scaledBitmap;
		        
		        /* System.gc(); 
		         * TODO: I heard this is bad practice so I left it out
		         * TODO: convert bitmap back to JPEG
		         * I'm inexperienced with java so how else should i free up memory?
		         */
		        
		        fin.close();	
		        
		        FileOutputStream fos = new FileOutputStream(imageFileUri.getPath());
		        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		        fos.flush();
		        fos.close();
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
			scaleImage(receiptFileUri);
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
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setDataAndType(receiptFileUri, "image/*");
						startActivity(intent);
					}
				}
				
				else if (dialogOptions[item].equals("Delete Photo")) {
					receiptFileUri = null;
					receiptButton = (ImageButton) findViewById(R.id.btn_receipt);
				}
				
				else if (dialogOptions[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		openDialog.show();
	}
}

