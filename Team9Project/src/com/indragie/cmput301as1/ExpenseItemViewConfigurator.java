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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indragie.cmput301as1.ListSection.ViewConfigurator;

/**
 * View configurator for showing {@link ExpenseItem} in a {@link ListView} with
 * {@link SectionedListAdapter}
 */
public class ExpenseItemViewConfigurator implements ViewConfigurator<ExpenseItem> {
	//================================================================================
	// Constants
	//================================================================================
	
	private static final int EXPENSE_ITEM_VIEW_CODE = 1;
	
	//================================================================================
	// ViewConfigurator
	//================================================================================
	
	@Override
	public int getViewTypeCode() {
		return EXPENSE_ITEM_VIEW_CODE;
	}
	
	@Override
	public View createView(Context context, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.expense_claim_list_row, parent, false);
	}
	
	@Override
	public void configureView(Context context, View view, ExpenseItem item) {
		TextView nameTextView = (TextView)view.findViewById(R.id.tv_name);
		nameTextView.setText(item.getName());
		
		TextView dateTextView = (TextView)view.findViewById(R.id.tv_date);
		dateTextView.setText(DateFormat.getDateInstance().format(item.getDate()));
		
		TextView amountsTextView = (TextView)view.findViewById(R.id.tv_amounts);
		amountsTextView.setText(item.getAmount().toString());
		
		TextView categoryTextView = (TextView)view.findViewById(R.id.tv_status);
		categoryTextView.setBackground(context.getResources().getDrawable(R.drawable.bg_rounded_grey));
		categoryTextView.setText(item.getCategory());
	}
}
