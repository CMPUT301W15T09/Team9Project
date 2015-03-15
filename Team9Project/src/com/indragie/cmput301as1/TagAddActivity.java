package com.indragie.cmput301as1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class TagAddActivity extends AddActivity {
	private EditText textField;

	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		setContentView(R.layout.activity_tag_name);
		textField = (EditText)findViewById(R.id.tagNameEditText);
		
	}
	protected Intent getResultIntent()  {
		Tag tag= new Tag(
			textField.getText().toString()
		);

		Intent intent = new Intent();
		//intent.(tag)
		return intent;
	}
	@Override
	protected void onCancel() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}

	@Override
	protected void onDone() {
		setResult(RESULT_OK, getResultIntent());
		finish();
	}

}
