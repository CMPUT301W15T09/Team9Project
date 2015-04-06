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

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.location.places.Place;

/**
 * Presents a user interface for modifiying user settings.
 */
public class UserSettingsActivity extends PlacePickerParentActivity {
	//================================================================================
	// Constants
	//================================================================================
	/**
	 * Intent key for getting the user.
	 */
	private static final String EXTRA_USER = "com.indragie.cmput301as1.EXTRA_USER";
	
	/**
	 * Field for the user's name.
	 */
	private EditText nameField;
	
	/**
	 * Field for the user's home location.
	 */
	private EditText locationField;
	
	/**
	 * The home location selected using the place picker.
	 */
	private Geolocation homeLocation;
	
	/**
	 * The user passed into this activity.
	 */
	private User user;

	//================================================================================
	// Activity Callbacks
	//================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_settings);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		nameField = (EditText)findViewById(R.id.et_name);
		locationField = (EditText)findViewById(R.id.et_location);
		
		user = (User)getIntent().getSerializableExtra(EXTRA_USER);
		if (user != null) {
			nameField.setText(user.getName());
			locationField.setText(user.getLocation().toString());
		}
		
		final OnPlacePickedListener listener = new OnPlacePickedListener() {
            @Override
            public void onPlacePickerCanceled() {
                locationField.clearFocus();
            }

            @Override
            public void onPlacePicked(Place place) {
                homeLocation = new Geolocation(place);
                locationField.setText(homeLocation.toString());
                locationField.clearFocus();
            }
        };
        locationField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    openPlacePicker(listener);
                }
            }
        });
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			save();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onBackPressed() {
		save();
		super.onBackPressed();
	}
	
	/**
	 * Finishes the activity and passes the {@link User} in the
	 * result intent.
	 */
	private void save() {
		Intent intent = new Intent();
		String nameText = nameField.getText().toString();
		
		if (user == null) {
			String androidID = Secure.getString(getContentResolver(), Secure.ANDROID_ID); 
			user = new User(androidID, nameText);
		} else {
			user.setName(nameText);
			user.setLocation(homeLocation);
		}
		
		intent.putExtra(EXTRA_USER, user);
		setResult(RESULT_OK, intent);
		finish();
	}
}
