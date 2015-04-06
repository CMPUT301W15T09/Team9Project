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
		
		String name = JSONHelpers.getStringIfPossible(obj.get("name"));
		String description = JSONHelpers.getStringIfPossible(obj.get("description"));
		String category = JSONHelpers.getStringIfPossible(obj.get("category"));
		boolean incomplete = JSONHelpers.getBooleanIfPossible(obj.get("incomplete"));
		JsonObject amountObject = JSONHelpers.getJsonObjectIfPossible(obj.get("amount"));
		long time = JSONHelpers.getLongIfPossible(obj.get("date"));
		String receiptBase64Str = JSONHelpers.getStringIfPossible(obj.get("receipt_base64"));
		
		Money amount = null;
		if (amountObject != null) {
			amount = context.deserialize(amountObject, Money.class);
		}
		
		ExpenseItem item = new ExpenseItem(name, description, category, amount, new Date(time));
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
}
