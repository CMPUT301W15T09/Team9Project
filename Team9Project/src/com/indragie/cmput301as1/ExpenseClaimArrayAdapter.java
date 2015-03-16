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
		TextView destinationsTextView = (TextView)convertView.findViewById(R.id.tv_name);
		destinationsTextView.setText(buildDestinationsString(claim));
		
		TextView dateTextView = (TextView)convertView.findViewById(R.id.tv_date);
		dateTextView.setText(DateFormat.getDateInstance().format(claim.getStartDate()));
		
		TextView amountsTextView = (TextView)convertView.findViewById(R.id.tv_amounts);
		String amounts = claim.getSummarizedAmounts();
		if (amounts == null) {
			amounts = resources.getString(R.string.no_expenses);
		}
		amountsTextView.setText(amounts);
		
		TextView statusTextView = (TextView)convertView.findViewById(R.id.tv_status);
		statusTextView.setText(claim.getStatusString(resources));
		statusTextView.setBackground(drawableForStatus(claim.getStatus(), resources));
		
		return convertView;
	}
	
	/**
	 * Draws the status of the expense claim
	 * @param status The status.
	 * @param resources The application's resources.
	 * @return The drawable resource.
	 */
	private Drawable drawableForStatus(ExpenseClaim.Status status, Resources resources) {
		switch (status) {
		case IN_PROGRESS:
			return resources.getDrawable(R.drawable.bg_rounded_blue);
		case APPROVED:
			return resources.getDrawable(R.drawable.bg_rounded_green);
		case RETURNED:
			return resources.getDrawable(R.drawable.bg_rounded_red);
		case SUBMITTED:
			return resources.getDrawable(R.drawable.bg_rounded_yellow);
		default:
			return null;
		}
	}
	
	/**
	 * Builds a string containing every destination in the specified
	 * expense claim, each separated by a new line.
	 * @param claim The expense claim.
	 * @return String containing every destination in the expense claim, suitable
	 * for display in the UI.
	 */
	private String buildDestinationsString(ExpenseClaim claim) {
		List<Destination> destinations = claim.getDestinations();
		if (destinations.size() == 0) return "";
		
		StringBuilder builder = new StringBuilder();
		for (Destination destination : destinations) {
			builder.append(destination.getName());
			builder.append("\n");
		}
		
		// Remove trailing newline
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
}
