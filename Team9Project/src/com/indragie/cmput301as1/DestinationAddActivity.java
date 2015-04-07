package com.indragie.cmput301as1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class DestinationAddActivity extends Activity {
	
	public static final String ADD_DESTINATION = "com.indragie.cmput301as1.ADD_DESTINATION";

	EditText nameField;
	EditText reasonField;
	Button addLocationButton;
	//TODO: get something back from the button
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_destination_add);
		
		nameField = (EditText)findViewById(R.id.et_name);
		reasonField = (EditText)findViewById(R.id.et_travel_reason);
		addLocationButton = (Button)findViewById(R.id.button_add_location);
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
		//location
		
		Destination destination = new Destination(name, reason, null);
		Intent intent = new Intent();
		intent.putExtra(ADD_DESTINATION, destination);
		return intent;
	}
	
}
