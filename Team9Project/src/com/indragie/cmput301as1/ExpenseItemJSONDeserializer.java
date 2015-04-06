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

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import org.joda.money.Money;

import android.net.Uri;
import android.util.Base64;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

/**
 * Deserializes an {@link ExpenseItem} from JSON.
 */
public class ExpenseItemJSONDeserializer implements JsonDeserializer<ExpenseItem> {
	/**
	 * Controller used to generate receipt URIs.
	 */
	private ExpenseItemReceiptController receiptController;

	/**
	 * Creates an instance of {@link ExpenseItemJSONDeserializer}
	 * @param receiptController Controller used to generate receipt URIs.
	 */
	public ExpenseItemJSONDeserializer(ExpenseItemReceiptController receiptController) {
		this.receiptController = receiptController;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public ExpenseItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		if (obj == null) return null;
		
		String name = getStringIfPossible(obj.get("name"));
		String description = getStringIfPossible(obj.get("description"));
		String category = getStringIfPossible(obj.get("category"));
		boolean incomplete = getBooleanIfPossible(obj.get("incomplete"));
		JsonObject amountObject = getJsonObjectIfPossible(obj.get("amount"));
		JsonObject dateObject = getJsonObjectIfPossible(obj.get("date"));
		String receiptBase64Str = getStringIfPossible(obj.get("receipt_base64"));
		
		Money amount = null;
		if (amountObject != null) {
			amount = context.deserialize(amountObject, Money.class);
		}
		
		Date date = null;
		if (dateObject != null) {
			date = context.deserialize(dateObject, Money.class);
		}
		
		ExpenseItem item = new ExpenseItem(name, description, category, amount, date);
		item.setIncomplete(incomplete);
		
		if (receiptBase64Str != null) {
			Uri receiptUri = receiptController.constructNewReceiptImageUri();
			byte buffer[] = Base64.decode(receiptBase64Str, Base64.DEFAULT);
			try {
				FileOutputStream fos = new FileOutputStream(receiptUri.getPath(), false);
				fos.write(buffer);
				fos.flush();
				fos.close();
				item.setReceiptUri(receiptUri);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return item;
	}
	
	private static String getStringIfPossible(JsonElement element) {
		if (element == null) return null;
		
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isString()) {
				return primitive.getAsString();
			}
		}
		return null;
	}
	
	private static boolean getBooleanIfPossible(JsonElement element) {
		if (element == null) return false;
		
		if (element.isJsonPrimitive()) {
			JsonPrimitive primitive = element.getAsJsonPrimitive();
			if (primitive.isBoolean()) {
				return primitive.getAsBoolean();
			}
		}
		return false;
	}
	
	private static JsonObject getJsonObjectIfPossible(JsonElement element) {
		if (element == null) return null;
		
		if (element.isJsonObject()) {
			return element.getAsJsonObject();
		}
		return null;
	}
}
