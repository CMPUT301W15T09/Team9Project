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

import java.text.DateFormat;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adaptor for showing an array of {@link ExpenseItem} objects in a {@link ListView}
 * Based on https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */
public class ExpenseItemArrayAdapter extends ArrayAdapter<ExpenseItem> {
	public ExpenseItemArrayAdapter(Context context, List<ExpenseItem> items) {
		super(context, R.layout.expense_claim_list_row, items);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ExpenseItem item = getItem(position);
		Resources resources = getContext().getResources();
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_claim_list_row, parent, false);
		}
		TextView nameTextView = (TextView)convertView.findViewById(R.id.tv_name);
		nameTextView.setText(item.getName());
		
		TextView dateTextView = (TextView)convertView.findViewById(R.id.tv_date);
		dateTextView.setText(DateFormat.getDateInstance().format(item.getDate()));
		
		TextView amountsTextView = (TextView)convertView.findViewById(R.id.tv_amounts);
		amountsTextView.setText(item.getAmount().toString());
		
		TextView categoryTextView = (TextView)convertView.findViewById(R.id.tv_status);
		categoryTextView.setBackground(resources.getDrawable(R.drawable.bg_rounded_grey));
		categoryTextView.setText(item.getCategory());
		
		return convertView;
	}
}
