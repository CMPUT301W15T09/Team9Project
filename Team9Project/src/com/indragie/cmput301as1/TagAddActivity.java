/* 
 * Copyright (C) 2015 Andrew Zhong
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.indragie.cmput301as1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * Activity that presents a user interface for entering information to 
 * create a new tag.
 */
public class TagAddActivity extends Activity{
	
	public static final String ADDED_TAG = "com.indragie.cmput301as1.ADDED_TAG";
	
	/**
	 * Text field for name of tag.
	 */
	protected EditText textField;

	
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
	
	/**
	 * Calls method for adding the tag.
	 * @return Intent that contains tag to add.
	 */
	protected Intent addTagIntent()  {
		Tag tag= new Tag(textField.getText().toString());

		Intent intent = new Intent();
		intent.putExtra(ADDED_TAG, tag);
		return intent;
	}

}
