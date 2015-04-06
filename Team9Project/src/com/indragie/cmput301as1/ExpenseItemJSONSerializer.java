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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Date;

import org.joda.money.Money;

import android.net.Uri;
import android.util.Base64;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Serializes an {@link ExpenseItem} to JSON.
 */
public class ExpenseItemJSONSerializer implements JsonSerializer<ExpenseItem> {

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(ExpenseItem src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("name", src.getName());
		obj.addProperty("description", src.getDescription());
		obj.addProperty("category", src.getCategory());
		obj.addProperty("incomplete", src.isIncomplete());
		obj.add("amount", context.serialize(src.getAmount(), Money.class));
		Date date = src.getDate();
		if (date != null) {
			obj.addProperty("date", date.getTime());
		}
		
		Uri receiptUri = src.getReceiptUri();
		if (receiptUri != null) {
			InputStream inputStream;
			try {
				inputStream = new FileInputStream(receiptUri.getPath());
				byte[] buffer = new byte[inputStream.available()];
				inputStream.read(buffer);
				inputStream.close();
				
				String base64Str = Base64.encodeToString(buffer, Base64.DEFAULT);
				obj.addProperty("receipt_base64", base64Str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
}
