package com.indragie.cmput301as1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class TagAddActivity extends AddActivity {
	public static final String EXTRA_EXPENSE_CLAIM_TAG = "com.indragie.cmput301as1.EXPENSE_CLAIM_TAG";
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
		//missing
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
