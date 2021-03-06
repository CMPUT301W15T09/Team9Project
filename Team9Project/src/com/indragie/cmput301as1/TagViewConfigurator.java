/* 
 * Copyright (C) 2015 Andrew Zhong
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
 * View configurator for showing {@link Tag} in a {@link ListView} with
 * {@link SectionedListAdapter}
 */
public class TagViewConfigurator implements ViewConfigurator<ExpenseClaimDetailController.DetailItem> {
	//================================================================================
	// Constants
	//================================================================================

	private static final int TAG_VIEW_CODE = 3;

	//================================================================================
	// ViewConfigurator
	//================================================================================

	@Override
	public int getViewTypeCode() {
		return TAG_VIEW_CODE;
	}

	@Override
	public View createView(Context context, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
	}

	@Override
	public void configureView(Context context, View view, ExpenseClaimDetailController.DetailItem object) {
		Tag tag = (Tag)object.getModel();
		
		TextView nameTextView = (TextView)view.findViewById(android.R.id.text1);
		nameTextView.setText(tag.getName());
		
	}
}
