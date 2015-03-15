package com.indragie.cmput301as1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnCreateContextMenuListener;
import android.widget.EditText;

public class TagAddActivity extends Activity{
	
	public static final String ADDED_TAG = "com.indragie.cmput301as1.ADDED_TAG";
	
	private EditText textField;

	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_tag_name);
		textField = (EditText)findViewById(R.id.tagNameEditText);
		
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contextual_accept, menu);
		 return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_accept:
			addTagIntent();
			setResult(RESULT_OK, addTagIntent());
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
	
	
	protected Intent addTagIntent()  {
		Tag tag= new Tag(
			textField.getText().toString()
		);

		Intent intent = new Intent();
		intent.putExtra(ADDED_TAG, tag);
		return intent;
	}

}
