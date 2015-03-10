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

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adaptor for showing an array of {@link ExpenseItem} objects in a {@link ListView}
 * Based on https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */
public class TagArrayAdapter extends ArrayAdapter<ExpenseItem> {
	public TagArrayAdapter(Context context, List<ExpenseItem> items) {
		super(context, R.layout.expense_claim_list_row, items);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ExpenseItem item = getItem(position);
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.tag_list_row, parent, false);
		}
		TextView nameTextView = (TextView)convertView.findViewById(R.id.tv_name);
		nameTextView.setText(item.getName());
		
		return convertView;
	}
}
