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

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adaptor for showing an array of {@link ExpenseClaim} objects in a {@link ListView}
 * Based on https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */
public class ExpenseClaimArrayAdapter extends ArrayAdapter<ExpenseClaim> {
	public ExpenseClaimArrayAdapter(Context context, List<ExpenseClaim> claims) {
		super(context, R.layout.expense_claim_list_row, claims);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ExpenseClaim claim = getItem(position);
		Resources resources = getContext().getResources();
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_claim_list_row, parent, false);
		}
		TextView nameTextView = (TextView)convertView.findViewById(R.id.tv_name);
		nameTextView.setText(claim.getName());
		
		TextView amountsTextView = (TextView)convertView.findViewById(R.id.tv_amounts);
		String amounts = claim.getSummarizedAmounts();
		if (amounts == null) {
			amounts = resources.getString(R.string.no_expenses);
		}
		amountsTextView.setText(amounts);
		
		TextView statusTextView = (TextView)convertView.findViewById(R.id.tv_status);
		
		String text = null;
		Drawable background = null;
		switch (claim.getStatus()) {
		case IN_PROGRESS:
			text = resources.getString(R.string.in_progress_label);
			background = resources.getDrawable(R.drawable.bg_rounded_blue);
			break;
		case APPROVED:
			text = resources.getString(R.string.approved_label);
			background = resources.getDrawable(R.drawable.bg_rounded_green);
			break;
		case RETURNED:
			text = resources.getString(R.string.returned_label);
			background = resources.getDrawable(R.drawable.bg_rounded_red);
			break;
		case SUBMITTED:
			text = resources.getString(R.string.submitted_label);
			background = resources.getDrawable(R.drawable.bg_rounded_yellow);
			break;
		default:
			break;
		}
		statusTextView.setText(text);
		statusTextView.setBackground(background);
		
		return convertView;
	}
}
