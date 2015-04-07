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

import com.google.android.gms.location.places.Place;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Activity that presents a user interface for entering information to 
 * add a destination to a claim.
 */
public class DestinationAddActivity extends PlacePickerParentActivity {
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Intent key for the {@link Destination} object.
	 */
	public static final String EXTRA_DESTINATION = "com.indragie.cmput301as1.EXTRA_DESTINATION";
	
	/**
	 * Intent key for the {@link User} object.
	 */
	public static final String EXTRA_USER = "com.indragie.cmput301as1.EXTRA_USER";
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * Field for name of the destination.
	 */
	protected EditText nameField;
	
	/**
	 * Field for reason of travel.
	 */
	protected EditText reasonField;
	
	/**
	 * Field for adding a location to the destination.
	 */
	protected EditText addLocationField;
	
	/**
	 * Geolocation of the location for the destination.
	 */
	protected Geolocation location;
	
	//================================================================================
	// Activity Callbacks
	//================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setUpFields();
		setUpListener();
		
		User user = (User)getIntent().getSerializableExtra(EXTRA_USER);
		location = user.getLocation();
		
		if (location == null) {
			System.out.println("was null");
			location = new Geolocation(0, 0);
		}
		addLocationField.setText(location.getName() + "\n" + location.getAddress());
	}

	/**
	 * Sets up the text fields for the activity.
	 */
	protected void setUpFields() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_destination_add);
		
		nameField = (EditText)findViewById(R.id.et_name);
		reasonField = (EditText)findViewById(R.id.et_travel_reason);
		addLocationField = (EditText)findViewById(R.id.et_add_location);
		
	}
	
	/**
	 * Sets up the PlacePickedListener for the activity.
	 */
	protected void setUpListener() {
		final OnPlacePickedListener listener = new OnPlacePickedListener() {
			
			@Override
			public void onPlacePickerCanceled() {
				addLocationField.clearFocus();
				
			}
			
			@Override
			public void onPlacePicked(Place place) {
				addLocationField.setText(place.getName() + "\n" + place.getAddress());
				addLocationField.clearFocus();
				
				location = new Geolocation(place);
			}
		};
		
		addLocationField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    openPlacePicker(listener);
                }
            }
        });
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contextual_accept, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_accept:
			String name = nameField.getText().toString();
			String reason = reasonField.getText().toString();
			
			Destination destination = new Destination(name, reason, location);
			System.out.println("Name after saving in destination: " +location.getName());
			System.out.println("Address after saving in destination: " + location.getAddress());
			setResult(RESULT_OK, addDestinationIntent(name, reason, destination)); 
			finish();
			return true;
		case android.R.id.home:
			setResult(RESULT_CANCELED, new Intent());
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Calls method for adding a destination
	 * @return Intent that contains the destination to add.
	 */
	protected Intent addDestinationIntent(String name, String reason, Destination destination)  {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_DESTINATION, destination);
		return intent;
	}
	
}
