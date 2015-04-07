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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter for showing an array of {@link ExpenseClaim} objects in a {@link ListView}
 * Based on https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */
public class ExpenseClaimArrayAdapter extends ArrayAdapter<ExpenseClaim> {
	
	private User user;
	
	public ExpenseClaimArrayAdapter(Context context, List<ExpenseClaim> claims, User user) {
		super(context, R.layout.expense_claim_list_row, claims);
		this.user = user;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ExpenseClaim claim = getItem(position);
		Resources resources = getContext().getResources();
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_claim_list_row, parent, false);
		}
		TextView destinationsTextView = (TextView)convertView.findViewById(R.id.tv_name);

		// get the Geolocation of the current destination
		List<Destination> destinations = claim.getDestinations();

		// change the background color for destinations
		
		setColorCoding(destinations, convertView);

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
	 * Changes the background color of the claim depending on the distance from the first destination to the home location.
	 * @param destinations list of destinations
	 * @param convertView the background of the claim
	 */
	private void setColorCoding(List<Destination> destinations, View convertView) {
		if ((destinations.size() > 0)&&(user.getLocation()!=null)){
			// get the home location
			Geolocation home = user.getLocation();
			ImageView destinationsBar = (ImageView)convertView.findViewById(R.id.destination_color_bar);
			destinationsBar.setVisibility(View.VISIBLE);
		
			// get the first destination
			if (destinations.get(0).getLocation() != null) {
				Geolocation location = destinations.get(0).getLocation();

				// get the computed distance between the home location and destination
				float[] results = new float[1];
				double startLatitude = home.getLatitude();
				double startLongitude = home.getLongitude();
				double endLatitude = location.getLatitude();
				double endLongitude = location.getLongitude();
				Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
				float distanceBetween = results[0];

				// change the color of the background of destination depending on the distance
				if (distanceBetween < 10000000.0) {
					destinationsBar.setImageResource(R.drawable.green);
				} else if (distanceBetween < 20000000.0) {
					destinationsBar.setImageResource(R.drawable.lime);
				} else if (distanceBetween < 30000000.0) {
					destinationsBar.setImageResource(R.drawable.orange);
				} else if (distanceBetween < 40000000.0) {
					destinationsBar.setImageResource(R.drawable.red);
				} else {
					destinationsBar.setImageResource(R.drawable.grey);
				}				
			}
		}
	}
	
	/**
	 * Draws the status of the expense claim.
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
	 * Builds a string containing every destination in the specified.
	 * expense claim, each separated by a new line.
	 * @param claim The expense claim.
	 * @return String containing every destination in the expense claim, suitable
	 * for display in the UI.
	 */
	private String buildDestinationsString(ExpenseClaim claim) {
		List<Destination> destinations = claim.getDestinations();
		if (destinations.size() == 0) {
			return getContext().getResources().getString(R.string.no_destinations);
		}
		
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
