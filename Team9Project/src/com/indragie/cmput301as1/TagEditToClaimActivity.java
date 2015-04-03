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

import android.content.Intent;
import android.os.Bundle;

public class TagEditToClaimActivity extends TagAddToClaimActivity {
	
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Intent key for the position of the {@link Tag} object.
	 */
	public static final String EXTRA_TAG_POSITION = "com.indragie.cmput301as1.TAG_POSITION";
	
	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpActionBarAndModel();
		
	}

	@Override
	protected Intent getTagSelected(int position) {
		Intent intent = super.getTagSelected(position);
		intent.putExtra(EXTRA_TAG_POSITION, getIntent().getIntExtra(EXTRA_TAG_POSITION, -1));
		return intent;
	}
	
}
