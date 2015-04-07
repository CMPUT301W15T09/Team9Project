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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Controller for a user interface that allows the user to manually enter
 * GPS coordinates when the network connnection is not available.
 */
public class GPSFallbackController {
	//================================================================================
	// Interfaces
	//================================================================================
	
	/**
	 * Listener to be called when the user takes an action on
	 * the alert dialog.
	 */
	public interface AlertListener {
		/**
		 * Called when the user taps the OK button on the dialog.
		 * @param latitude The latitude entered by the user.
		 * @param longitude The longitude entered by the user.
		 */
		public void onOk(double latitude, double longitude);
		
		/**
		 * Called when the user taps the Cancel button on the dialog.
		 */
		public void onCancel();
	}
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * The current context.
	 */
	private Context context;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link GPSFallbackController}
	 * @param context The current context.
	 */
	public GPSFallbackController(Context context) {
		this.context = context;
	}
	
	//================================================================================
	// API
	//================================================================================
	
	/**
	 * Creates an alert dialog for entering GPS coordinates.
	 * @param existingLocation Optional existing location to be filled in.
	 * @param listener Listener to be called when the user takes an action on the alert dialog.
 	 * @return Alert dialog for entering GPS coordinates.
	 */
	@SuppressLint("InflateParams")
	public AlertDialog buildAlertDialog(Geolocation existingLocation, final AlertListener listener) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(R.layout.alert_gps, null);
		
		final EditText latitudeField = (EditText)dialogView.findViewById(R.id.et_latitude);
		final EditText longitudeField = (EditText)dialogView.findViewById(R.id.et_longitude);
		final Button currentLocationButton = (Button)dialogView.findViewById(R.id.btn_current_location);
		
		if (existingLocation != null) {
			latitudeField.setText(Double.toString(existingLocation.getLatitude()));
			longitudeField.setText(Double.toString(existingLocation.getLongitude()));
		}
		
		currentLocationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
				Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location == null) {
					Toast.makeText(context, R.string.no_location_toast, Toast.LENGTH_LONG).show();
				} else {
					latitudeField.setText(Double.toString(location.getLatitude()));
					longitudeField.setText(Double.toString(location.getLongitude()));
				}
			};
		});
		
		return new AlertDialog.Builder(context)
			.setTitle(R.string.gps_alert_title)
			.setMessage(R.string.gps_alert_message)
			.setView(dialogView)
			.setPositiveButton(android.R.string.ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					double latitude = Double.parseDouble(latitudeField.getText().toString());
					double longitude = Double.parseDouble(longitudeField.getText().toString());
					listener.onOk(latitude, longitude);
				}
			})
			.setNegativeButton(android.R.string.cancel, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					listener.onCancel();
				}
			})
			.create();
	}
}
