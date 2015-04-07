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
		
		Intent intent = getIntent();
		
	}


}
