/* 
 * Copyright (C) 2015 Indragie Karunaratne, Brandon Williams
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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

/**
 * Handles tasks related to the storage and image processing of receipt
 * images for expense items.
 */
public class ExpenseItemReceiptController {
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Name of the folder used to store receipts.
	 */
	private static final String RECEIPTS_FOLDER_NAME = "ExpenseReceipts";
	
	/**
	 * File extension for receipt images.
	 */
	private static final String RECEIPT_IMAGE_EXT = "jpg";
	
	/**
	 * The maximum image size in bytes.
	 */
	private static int MAX_IMAGE_SIZE = 65536;

	//================================================================================
	// API
	//================================================================================
	
	/**
	 * Creates a new {@link Uri} representing a location on disk to
	 * store a receipt image.
	 * @return The {@link Uri} to write the receipt image to.
	 */
	public Uri constructNewReceiptImageUri() {
		String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + RECEIPTS_FOLDER_NAME;
		File receiptFolder = new File(folder);
		if (!receiptFolder.exists()) {
			receiptFolder.mkdir();
		}

		String receiptFilePath = folder + "/" + String.valueOf(System.currentTimeMillis()) + "." + RECEIPT_IMAGE_EXT;
		File receiptFile = new File(receiptFilePath);
		return Uri.fromFile(receiptFile);
	}
	
	/**
	 * Deletes the image at the specified {@link Uri} 
	 * @param imageUri The {@link Uri} of the image to delete.
	 * @return Whether the file was deleted.
	 */
	public boolean deleteReceiptImage(Uri imageUri) {
		File file = new File(imageUri.getPath());
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}
	
	/**
	 * Postprocesses an existing image to ensure that it is within
	 * the size constraints.
	 * @param imageUri The {@link Uri} to the image to post-process.
	 * @return Whether the post-processing operation was successful.
	 */
	public boolean postProcessReceiptImage(Uri imageUri) {
		try {
			String path = imageUri.getPath();
			InputStream fin = new FileInputStream(path);
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			// Decode only the image size.
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(fin, null, options);
		    fin.close();
		    
		    // Use the RGB_565 format because it only uses 2 bytes per pixel, and 
 			// receipt images do not need high color fidelity.
		    // More information here: http://developer.android.com/reference/android/graphics/Bitmap.Config.html#ARGB_8888
		    int maxPixels = MAX_IMAGE_SIZE / 2;
		    int scale = calculateScale(options.outWidth, options.outHeight, maxPixels);
		    
		    if (scale > 1) {
		    	// Decode the scaled bitmap image.
		    	options = new BitmapFactory.Options();
		    	options.inSampleSize = scale;
		    	options.inDither = true;
		    	options.inPreferredConfig = Bitmap.Config.RGB_565;
		    	
		    	fin = new FileInputStream(imageUri.getPath());
		    	Bitmap scaledBitmap = BitmapFactory.decodeStream(fin, null, options);
		    	fin.close();
		    	
		    	// Write the JPEG image to the original location.
		    	FileOutputStream fos = new FileOutputStream(path, false);
		        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		        fos.flush();
		        fos.close();
		    }
		    return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Calculates the scale factor necessary to scale an image down to
	 * be less than an upper bound on the total number of pixels.
	 * @param width The original width of the image.
	 * @param height The original height of the image.
	 * @param maxPixels The maximum number of pixels in the scaled image.
	 * @return The scale factor.
	 */
	private int calculateScale(int width, int height, int maxPixels) {
		int totalPixels = width * height;
		if (totalPixels <= maxPixels) {
			return 1;
		} else {
			return (int)Math.round(Math.sqrt((double)totalPixels / (double)maxPixels));
		}
	}
}
