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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

/**
 * Parent activity that provides a simple abstraction for picking
 * a place using the Google Place Picker API.
 */
public class PlacePickerParentActivity extends Activity {
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Request code for opening the place picker activity.
	 */
	private static final int PLACE_PICKER_REQUEST_CODE = 50;
	
	//================================================================================
	// Interfaces
	//================================================================================
	
	/**
	 * Listener that is called when a place is picked.
	 */
	public interface OnPlacePickedListener {
		/**
		 * Called when a place is picked.
		 * @param place The place that was picked.
		 */
		public void onPlacePicked(Place place);
	}
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * Listener that is called when a place is picked.
	 */
	private OnPlacePickedListener placePickedListener;
	
	//================================================================================
	// API
	//================================================================================
	
	/**
	 * Opens the place picker activity.
	 * @param placePickedListener Listener that is called when a place is picked.
	 */
	public void openPlacePicker(OnPlacePickedListener placePickedListener) {
		try {
			PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
			Intent intent = builder.build(this);
			this.placePickedListener = placePickedListener;
			startActivityForResult(intent, PLACE_PICKER_REQUEST_CODE);
		} catch (GooglePlayServicesRepairableException e) {
			e.printStackTrace();
			showUnableToConnectToast();
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
			showUnableToConnectToast();
		}
	}
	
	//================================================================================
	// Activity
	//================================================================================
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PLACE_PICKER_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				final Place place = PlacePicker.getPlace(data, this);
				placePickedListener.onPlacePicked(place);
			}
			placePickedListener = null;
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	//================================================================================
	// Private
	//================================================================================
	
	/**
	 * Shows a toast that indicates to the user that the application was
	 * unable to connect to the Google Places API.
	 */
	private void showUnableToConnectToast() {
		Toast.makeText(this, R.string.places_unavailable_error, Toast.LENGTH_LONG).show();
	}
}
