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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import android.content.res.Resources;
import android.net.Uri;
import android.test.ActivityTestCase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.indragie.cmput301as1.ExpenseItem;
import com.indragie.cmput301as1.ExpenseItemJSONDeserializer;
import com.indragie.cmput301as1.ExpenseItemJSONSerializer;
import com.indragie.cmput301as1.ExpenseItemReceiptController;
import com.indragie.cmput301as1.test.R;

public class ExpenseItemJSONSerializationTests extends ActivityTestCase {
	public void testSerializationAndDeserialization() throws IOException {
		ExpenseItem item = 
				new ExpenseItem("Taxi", "Taxi from airport", "ground transportation", Money.of(CurrencyUnit.CAD, 60.0), new Date());
		item.setIncomplete(true);
		Resources resources = getInstrumentation().getContext().getResources();
		ExpenseItemReceiptController controller = new ExpenseItemReceiptController();
		
		// Read the test image into memory.
		// From http://www.baeldung.com/convert-input-stream-to-a-file
		//
		// Test image is public domain.
		// Source: https://www.flickr.com/photos/surveying/16819186299/in/pool-publicdomain
		InputStream inputStream = resources.openRawResource(R.raw.test_image);
		byte[] buffer = new byte[inputStream.available()];
		inputStream.read(buffer);
		inputStream.close();
		
		// Write the original file contents to disk.
		Uri receiptUri = controller.constructNewReceiptImageUri();
		File receiptFile = new File(receiptUri.getPath());
		OutputStream outputStream = new FileOutputStream(receiptFile);
		outputStream.write(buffer);
		outputStream.flush();
		outputStream.close();
		
		// Post-process the image.
		assertTrue(controller.postProcessReceiptImage(receiptUri));
		item.setReceiptUri(receiptUri);
		
		// Serialize, then deserialize and check if the objects are the same
		ExpenseItemJSONSerializer serializer = new ExpenseItemJSONSerializer();
		ExpenseItemJSONDeserializer deserializer = new ExpenseItemJSONDeserializer(controller);
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(ExpenseItem.class, serializer);
		builder.registerTypeAdapter(ExpenseItem.class, deserializer);
		Gson gson = builder.create();
		
		String json = gson.toJson(item);
		ExpenseItem deserializedItem = gson.fromJson(json, ExpenseItem.class);
		assertEquals(item, deserializedItem);
		
		// Since ExpenseItem.equals() doesn't check the receipts, we have to manually
		// compare the receipt file data.
		Uri deserializedUri = deserializedItem.getReceiptUri();
		assertNotNull(deserializedUri);
		
		InputStream originalInputStream = new FileInputStream(receiptUri.getPath());
		byte[] originalBuffer = new byte[originalInputStream.available()];
		originalInputStream.read(buffer);
		originalInputStream.close();
		
		InputStream deserializedInputStream = new FileInputStream(deserializedUri.getPath());
		byte[] deserializedBuffer = new byte[deserializedInputStream.available()];
		deserializedInputStream.read(buffer);
		deserializedInputStream.close();
		
		assertTrue(Arrays.equals(originalBuffer, deserializedBuffer));
	}
}
