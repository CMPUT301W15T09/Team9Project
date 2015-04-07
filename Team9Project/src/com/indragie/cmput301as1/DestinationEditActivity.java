package com.indragie.cmput301as1;

import android.content.Intent;
import android.os.Bundle;

/**
 * Activity that presents a user interface for entering information to 
 * edit a destination on a claim.
 */
public class DestinationEditActivity extends DestinationAddActivity {
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Intent key for the position of the object.
	 */
	public static final String EXTRA_EDIT_DESTINATION_POSITION = "com.indragie.cmput301as1.EXTRA_EDIT_DESTINATION_POSITION";
	
	/**
	 * Intent key for the status of the claim.
	 */
	public static final String EXTRA_EDIT_DESTINATION_EDITABLE = "com.indragie.cmput301as1.EXTRA_EDIT_DESTINATION_EDITABLE";

	//================================================================================
	// Activity Callbacks
	//================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpFields();
		setUpListener();
		
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
	
	/**
	 * Calls method for adding a destination
	 * @return Intent that contains the destination to add.
	 */
	@Override
	protected Intent addDestinationIntent(String name, String reason, Destination destination)  {
		Intent intent = super.addDestinationIntent(name, reason, destination);
		intent.putExtra(EXTRA_EDIT_DESTINATION_POSITION, getIntent().getIntExtra(EXTRA_EDIT_DESTINATION_POSITION, -1));
		return intent;
	}

}
