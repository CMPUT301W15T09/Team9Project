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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indragie.cmput301as1.ListSection.ViewConfigurator;

/**
 * Creates and configures section header views for use with
 * @{link SectionedListAdapter }
 */
public class XMLSectionHeaderConfigurator implements ViewConfigurator<String> {
	//================================================================================
	// Constants
	//================================================================================
	/**
	 * Type code for section headers. Row views should not use this code.
	 */
	private static final int HEADER_VIEW_TYPE_CODE = 0;
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * Resource ID of the header view layout.
	 */
	private int resource;
	
	/**
	 * ID of the text view used to display the section title.
	 */
	private int textViewResourceId;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link XMLSectionHeaderConfigurator}
	 * @param resource Resource ID of the header view layout.
	 * @param textViewResourceId ID of the text view used to display the section title.
	 */
	public XMLSectionHeaderConfigurator(int resource, int textViewResourceId) {
		this.resource = resource;
		this.textViewResourceId = textViewResourceId;
	}
	
	//================================================================================
	// ViewConfigurator
	//================================================================================
	
	@Override
	public int getViewTypeCode() {
		return HEADER_VIEW_TYPE_CODE;
	}
	
	@Override
	public View createView(Context context, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(resource, parent, false);
	}
	
	@Override
	public void configureView(Context context, View view, String object) {
		TextView textView = (TextView)view.findViewById(textViewResourceId);
		textView.setText(object);
	}
}
