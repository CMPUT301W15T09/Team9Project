package com.indragie.cmput301as1;

import android.os.Bundle;
import android.widget.EditText;

public class TagEditActivity extends TagAddActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_tag_name);
		textField = (EditText)findViewById(R.id.tagNameEditText);
		

		Tag tag = (Tag)getIntent().getSerializableExtra(ManageTagsActivity.EXTRA_TAG);
		
		setupField(tag);
		
	}
	
	private void setupField(Tag tag) {
		textField.setText(tag.getName());
	}

}
