package com.indragie.cmput301as1;

import android.os.Bundle;
import android.widget.EditText;

/**
 * Activity that presents a user interface for entering information to 
 * create edit a existing tag.
 */
public class TagEditActivity extends TagAddActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_tag_name);
		textField = (EditText)findViewById(R.id.tagNameEditText);
		

		Tag tag = (Tag)getIntent().getSerializableExtra(ManageTagsActivity.EXTRA_TAG);
		
		setuptextField(tag);
		
	}
	
	private void setuptextField(Tag tag) {
		textField.setText(tag.getName());
	}

}
