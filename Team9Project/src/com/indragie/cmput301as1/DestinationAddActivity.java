package com.indragie.cmput301as1;

import com.google.android.gms.location.places.Place;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class DestinationAddActivity extends PlacePickerParentActivity {
	
	public static final String ADD_DESTINATION = "com.indragie.cmput301as1.ADD_DESTINATION";

	EditText nameField;
	EditText reasonField;
	EditText addLocationField;
	
	Geolocation location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpFields();
		
	}

	private void setUpFields() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_destination_add);
		
		nameField = (EditText)findViewById(R.id.et_name);
		reasonField = (EditText)findViewById(R.id.et_travel_reason);
		addLocationField = (EditText)findViewById(R.id.et_add_location);

		
		final OnPlacePickedListener listener = new OnPlacePickedListener() {
			
			@Override
			public void onPlacePickerCanceled() {
				addLocationField.clearFocus();
				
			}
			
			@Override
			public void onPlacePicked(Place place) {
				addLocationField.setText(place.getName() + "\n" + place.getAddress());
				addLocationField.clearFocus();
				
				location = new Geolocation(place.getLatLng());
				
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
			setResult(RESULT_OK, addDestinationIntent()); 
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
	private Intent addDestinationIntent()  {
		String name = nameField.getText().toString();
		String reason = reasonField.getText().toString();
		
		Destination destination = new Destination(name, reason, location);
		Intent intent = new Intent();
		intent.putExtra(ADD_DESTINATION, destination);
		return intent;
	}
	
}
