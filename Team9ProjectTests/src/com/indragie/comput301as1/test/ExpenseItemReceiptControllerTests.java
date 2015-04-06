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

package com.indragie.comput301as1.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.Resources;
import android.net.Uri;
import android.test.ActivityTestCase;

import com.indragie.cmput301as1.ExpenseItemReceiptController;
import com.indragie.cmput301as1.test.R;

public class ExpenseItemReceiptControllerTests extends ActivityTestCase {
	private ExpenseItemReceiptController controller;
	
	protected void setUp() throws Exception {
		super.setUp();
		controller = new ExpenseItemReceiptController();
	}
	
	public void testReceiptUriDoesNotExist() {
		Uri receiptUri = controller.constructNewReceiptImageUri();
		File file = new File(receiptUri.getPath());
		assertFalse(file.exists());
	}
	
	public void testReceiptUrisAreDifferent() {
		Uri uri1 = controller.constructNewReceiptImageUri();
		Uri uri2 = controller.constructNewReceiptImageUri();
		assertFalse(uri1.equals(uri2));
	}
	
	public void testReceiptPostprocessing() throws IOException {
		Resources resources = getInstrumentation().getContext().getResources();
		
		// Read the test image into memory.
		// From http://www.baeldung.com/convert-input-stream-to-a-file
		//
		// Test image is public domain.
		// Source: https://www.flickr.com/photos/surveying/16819186299/in/pool-publicdomain
		InputStream inputStream = resources.openRawResource(R.raw.test_image);
		byte[] buffer = new byte[inputStream.available()];
		inputStream.read(buffer);
		
		// Write the original file contents to disk.
		Uri receiptUri = controller.constructNewReceiptImageUri();
		File receiptFile = new File(receiptUri.getPath());
		OutputStream outputStream = new FileOutputStream(receiptFile);
		outputStream.write(buffer);
		outputStream.flush();
		outputStream.close();
		
		assertTrue(receiptFile.exists());
		assertTrue(receiptFile.length() > ExpenseItemReceiptController.MAX_IMAGE_SIZE);
		
		// Post-process the image.
		assertTrue(controller.postProcessReceiptImage(receiptUri));
		
		// Check that the resulting file size is within the constraints.
		assertTrue(receiptFile.exists());
		assertTrue(receiptFile.length() <= ExpenseItemReceiptController.MAX_IMAGE_SIZE);
		
		// Check to see if the file is deleted correctly.
		controller.deleteReceiptImage(receiptUri);
		assertFalse(receiptFile.exists());
	}
}
