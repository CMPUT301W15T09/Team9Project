package com.indragie.cmput301as1;

import android.content.Intent;
import android.os.Bundle;

public class DestinationEditActivity extends DestinationAddActivity {
	
	public static final String EXTRA_EDIT_DESTINATION_POSITION = "com.indragie.cmput301as1.EXTRA_EDIT_DESTINATION_POSITION";
	public static final String EXTRA_EDIT_DESTINATION_EDITABLE = "com.indragie.cmput301as1.EXTRA_EDIT_DESTINATION_EDITABLE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpFields();
		setUpListener();
		noLocationAdded = false;
		
		Intent intent = getIntent();
		Destination destination = (Destination)intent.getSerializableExtra(EXTRA_DESTINATION);
		location = destination.getLocation();

		boolean editable = intent.getBooleanExtra(EXTRA_EDIT_DESTINATION_EDITABLE, false);
		
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
		intent.putExtra(EXTRA_EDIT_DESTINATION_POSITION, getIntent().getIntExtra(EXTRA_EDIT_DESTINATION_POSITION, -1));
		return intent;
	}

}
