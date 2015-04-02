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

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Activity for viewing the current list of tags the use has defined. 
 * Can direct user to activities for adding or editing tags. 
 * Allows user to remove existing tags. 
 */
public class ManageTagsActivity extends TagAddToClaimActivity{

	//================================================================================
	// Constants
	//================================================================================
	
	private static final int EDIT_TAG_REQUEST= 2;

	public static final String EXTRA_TAG = "com.indragie.cmput301as1.EXTRA_TAG";
	
	//Added
	private static final String EXPENSE_CLAIM_FILENAME = "claims";

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setUpActionBarAndModel();
		
		final ActionMode.Callback clickCallback = new ActionMode.Callback() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
					case R.id.action_edit:
						startEditTagActivity();
						mode.finish();
						return true;
					case R.id.action_delete:
						listModel.remove(pressedItemIndex);
						mode.finish();
						return true;
					default:
						return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				mode.getMenuInflater().inflate(R.menu.contextual_edit, menu);
				mode.getMenuInflater().inflate(R.menu.contextual_delete, menu);
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}
		};

		setUpItemClickListener(clickCallback);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;
		switch (requestCode) {
		case ADD_TAG_REQUEST:
			onAddTag(data);
			break;
		case EDIT_TAG_REQUEST:
			onEditTag(data);
		}
	}
	
	//================================================================================
	// Edit a tag
	//================================================================================
	
	/**
	 * Starts the activity to edit a tag.
	 */
	private void startEditTagActivity() {
		Intent editTagIntent = new Intent(this, TagEditActivity.class);
		editTagIntent.putExtra(EXTRA_TAG, listModel.getItems().get(pressedItemIndex));
		startActivityForResult(editTagIntent, EDIT_TAG_REQUEST);
	}
	
	/**
	 * Edits a tag in list model from resulting activity.
	 * @param data The intent form resulting activity.
	 */
	private void onEditTag(Intent data) {
		Tag tag = (Tag)data.getSerializableExtra(TagAddActivity.ADDED_TAG);
		
		Tag editedTag = listModel.getItems().get(pressedItemIndex);
		
		listModel.set(pressedItemIndex, tag);
		
		
		ListModel claimListModel = new ListModel<ExpenseClaim>(EXPENSE_CLAIM_FILENAME, this);
		List<ExpenseClaim> list = claimListModel.getItems();
		/*
		for (ExpenseClaim claim: list) {
			if(claim.hasTag(editedTag)) {
				claim.setTag(index, newTag);
			}
		}
		*/
	}

}
