package com.indragie.cmput301as1;

import android.content.Intent;
import android.os.Bundle;

public class DestinationEditActivity extends DestinationAddActivity {
	
	public static final String EDIT_DESTINATION = "com.indragie.cmput301as1.EDIT_DESTINATION";
	public static final String EDIT_DESTINATION_POSITION = "com.indragie.cmput301as1.EDIT_DESTINATION_POSITION";
	public static final String EDIT_DESTINATION_EDITABLE = "com.indragie.cmput301as1.EDIT_DESTINATION_EDITABLE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpFields();
		setUpListener();
		
		Intent intent = getIntent();
		Destination destination = (Destination)intent.getSerializableExtra(EDIT_DESTINATION);
		Geolocation location = destination.getLocation();

		boolean editable = intent.getBooleanExtra(EDIT_DESTINATION_EDITABLE, false);
		
		nameField.setText(destination.getName());
		nameField.setEnabled(editable);
		reasonField.setText(destination.getTravelReason());
		reasonField.setEnabled(editable);
		addLocationField.setText(location.getName() + "\n" + location.getAddress());
		addLocationField.setEnabled(editable);
		
	}

	@Override
	protected Intent addDestinationIntent(String name, String reason, Destination destination)  {
		Intent intent = super.addDestinationIntent(name, reason, destination);
		intent.putExtra(EDIT_DESTINATION_POSITION, getIntent().getIntExtra(EDIT_DESTINATION_POSITION, -1));
		return intent;
	}

}
