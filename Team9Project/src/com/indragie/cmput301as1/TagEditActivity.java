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
