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
import android.widget.ListView;
import android.widget.TextView;

import com.indragie.cmput301as1.ListSection.ViewConfigurator;

/**
 * View configurator for showing {@link Destination} in a {@link ListView} with
 * {@link SectionedListAdapter}
 */
public class DestinationViewConfigurator implements ViewConfigurator<Destination> {
	//================================================================================
	// Constants
	//================================================================================

	private static final int DESTINATION_VIEW_CODE = 2;

	//================================================================================
	// ViewConfigurator
	//================================================================================

	@Override
	public int getViewTypeCode() {
		return DESTINATION_VIEW_CODE;
	}

	@Override
	public View createView(Context context, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
	}

	@Override
	public void configureView(Context context, View view, Destination destination) {
		TextView nameTextView = (TextView)view.findViewById(android.R.id.text1);
		nameTextView.setText(destination.getName());
		
		TextView reasonTextView = (TextView)view.findViewById(android.R.id.text2);
		reasonTextView.setText(destination.getTravelReason());
	}
}
