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

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.widget.ListView;

/**
 * Activity for viewing the current list of tags the user has defined. 
 * Allows user to select one or more existing tags to filter claims in ExpenseClaimListActivity.
 */

public class FilterTagsActivity extends ListActivity {

	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Filename for storing tags.
	 */
	private static final String TAG_FILENAME = "tags";
	
	/**
	 * Intent key for the filter.
	 */
	public static final String TAG_TO_FILTER = "com.indragie.cmput301as1.TAG_TO_FILTER";
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * List model of tags.
	 */
	private ListModel<Tag> listModel;

	/**
	 * List of selected tags.
	 */
	private ArrayList<Tag> selectedTags;
	
	//================================================================================
	// Activity Callbacks
	//================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpActionBarAndModel();
	}
	
	/**
	 * Sets up the action bar and ListModel to use.
	 */
	@SuppressWarnings("unchecked")
	protected void setUpActionBarAndModel() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		listModel = new ListModel<Tag>(TAG_FILENAME, this);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		setListAdapter(new TagFilterArrayAdapter(this, listModel.getItems()));
		
		selectedTags = (ArrayList<Tag>) getIntent().getSerializableExtra(TAG_TO_FILTER);
		
		for (Tag tag: selectedTags) {
			int position = listModel.getItems().indexOf(tag);
			getListView().setItemChecked(position, true);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onHome();
			return true;	
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Sets intent as canceled so no changes are made when home button pressed.
	 */
	private void onHome() {
		Intent intent = new Intent();
		if (getListView().getCheckedItemCount() == 0) {
			setResult(RESULT_CANCELED, intent);
		} else {
			SparseBooleanArray selectedIndexes = getListView().getCheckedItemPositions();
			selectedTags.clear();
			for (int index = 0; index < selectedIndexes.size(); index++) {
				if (selectedIndexes.valueAt(index)){
					int position = selectedIndexes.keyAt(index);
					selectedTags.add(listModel.getItems().get(position));
				}
			}
			intent.putExtra(TAG_TO_FILTER, selectedTags);
			setResult(RESULT_OK, intent);
		}
		finish();
	}
	
	
}
