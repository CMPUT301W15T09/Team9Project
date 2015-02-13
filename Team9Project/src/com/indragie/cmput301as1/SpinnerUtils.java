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

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Utility functions for working with Spinner widgets.
 */
public class SpinnerUtils {
	/**
	 * Configures a Spinner widget using items loaded from a resource.
	 * @param context Context of the Spinner widget
	 * @param spinner Spinner widget
	 * @param resourceID Resource used to populate the Spinner's items
	 */
	public static void configureSpinner(Context context, Spinner spinner, int resourceID) {
		// From http://developer.android.com/guide/topics/ui/controls/spinner.html
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
				resourceID, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	
	/**
	 * Sets the selected item of a spinner by value.
	 * @param spinner Spinner to set selected item of.
	 * @param value The value to select.
	 */
	@SuppressWarnings("unchecked")
	public static void setSelectedItem(Spinner spinner, String value) {
		ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>)spinner.getAdapter();
		int position = adapter.getPosition(value);
		spinner.setSelection(position);
	}
}
