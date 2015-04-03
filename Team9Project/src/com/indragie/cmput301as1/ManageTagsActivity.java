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

import com.indragie.cmput301as1.ExpenseClaim.Status;

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
	
	public static final String CLAIM_LIST = "com.indragie.cmput301as1.CLAIM_LIST";
	
	//================================================================================
	// Properties
	//================================================================================
	
	private ArrayList<ExpenseClaim> claimList;
	
	Boolean listChanged;
	
	//================================================================================
	// Activity Callbacks
	//================================================================================

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setUpActionBarAndModel();
		
		Intent intent = getIntent();
		
		claimList = (ArrayList<ExpenseClaim>)intent.getSerializableExtra(CLAIM_LIST);
		listChanged = false;
		
		final ActionMode.Callback clickCallback = new ActionMode.Callback() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
					case R.id.action_edit:
						startEditTagActivity();
						mode.finish();
						return true;
					case R.id.action_delete:
						Tag tag = getTagAt(pressedItemIndex);
						deleteTagInClaims(tag);
						
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
	
	/**
	 * Starts intent to return.
	 */
	@Override
	protected void onHome() {
		Intent intent = new Intent();
		if(listChanged) {
			intent.putExtra(CLAIM_LIST, claimList);
			setResult(RESULT_OK, intent);
		} else {
			setResult(RESULT_CANCELED, intent);
		}
		finish();
	}
	
	//================================================================================
	// Edit a tag
	//================================================================================
	
	/**
	 * Starts the activity to edit a tag.
	 */
	private void startEditTagActivity() {
		Intent editTagIntent = new Intent(this, TagEditActivity.class);
		editTagIntent.putExtra(EXTRA_TAG, getTagAt(pressedItemIndex));
		startActivityForResult(editTagIntent, EDIT_TAG_REQUEST);
	}
	 	
	/**
	 * Edits a tag in list model from resulting activity.
	 * @param data The intent form resulting activity.
	 */
	private void onEditTag(Intent data) {
		Tag newTag = (Tag)data.getSerializableExtra(TagAddActivity.ADDED_TAG);
		Tag oldTag = getTagAt(pressedItemIndex);
		
		listModel.set(pressedItemIndex, newTag);
		updateTagsInClaims(oldTag, newTag);
	}
	
	/**
	 * Update the old tag with the new tag in claims that are returned or in progress.
	 * @param oldTag The old tag to update.
	 * @param newTag The new tag.
	 */
	private void updateTagsInClaims(Tag oldTag, Tag newTag) {
		for (ExpenseClaim claim: claimList) {
			if(claim.hasTag(oldTag)) {
				listChanged = true;
				claim.setTag(oldTag, newTag);
			}
		
		}

	}
	
	/**
	 * Deletes the removed tag in claims that are returned or in progress.
	 * @param tag Tag to remove in claims.
	 */
	private void deleteTagInClaims(Tag tag) {
		for(ExpenseClaim claim: claimList) {
			if(claim.hasTag(tag)) {
				listChanged = true;
				claim.removeTag(tag);
			}
		}
	}
	
}
