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


import java.io.Serializable;
import java.util.ArrayList;

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
	// Classes
	//================================================================================
	
	/**
	 * Class that encapsulates a mutation to a tag.
	 */
	public static class TagMutation implements Serializable {
		private static final long serialVersionUID = -7792735815468100896L;
		/**
		 * The type of mutation that occurred.
		 */
		public enum MutationType {
			/**
			 * Tag was deleted. {@link ChangeType#oldTag} will contain the
			 * tag that was deleted, and {@link ChangeType#newTag} will be
			 * null.
			 */
			DELETE,
			/**
			 * Tag was edited. {@link ChangeType#oldTag} will contain the
			 * original tag, and {@link ChangeType#newTag} will contain the
			 * modified tag.
			 */
			EDIT
		}
		
		//================================================================================
		// Properties
		//================================================================================
		
		/**
		 * The type of mutation that occurred.
		 */
		private MutationType mutationType;
		
		/**
		 * The previous version of the tag.
		 */
		private Tag oldTag;
		
		/**
		 * The new version of the tag.
		 */
		private Tag newTag;
		
		//================================================================================
		// Constructors
		//================================================================================
		
		/**
		 * Creates a new instance of {@link TagMutation}
		 * @param mutationType The type of mutation that occurred.
		 * @param oldTag The previous version of the tag.
		 * @param newTag The new version of the tag.
		 */
		public TagMutation(MutationType mutationType, Tag oldTag, Tag newTag) {
			this.mutationType = mutationType;
			this.oldTag = oldTag;
			this.newTag = newTag;
		}
		
		//================================================================================
		// Accessors
		//================================================================================
		
		/**
		 * @return The type of mutation that occurred.
		 */
		public MutationType getMutationType() {
			return mutationType;
		}
		
		/**
		 * @return The previous version of the tag.
		 */
		public Tag getOldTag() {
			return oldTag;
		}
		
		/**
		 * @return The new version of the tag.
		 */
		public Tag getNewTag() {
			return newTag;
		}
	}

	//================================================================================
	// Constants
	//================================================================================
	
	private static final int EDIT_TAG_REQUEST= 2;

	public static final String EXTRA_TAG = "com.indragie.cmput301as1.EXTRA_TAG";
	
	public static final String EXTRA_TAG_MUTATIONS = "com.indragie.cmput301as1.TAG_MUTATIONS";
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * Used to store pending mutations to the tags.
	 */
	private ArrayList<TagMutation> mutations = new ArrayList<TagMutation>();
	
	//================================================================================
	// Activity Callbacks
	//================================================================================

	@SuppressWarnings("unchecked")
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
						Tag tag = getTagAt(pressedItemIndex);
						listModel.remove(pressedItemIndex);
						mutations.add(new TagMutation(TagMutation.MutationType.DELETE, tag, null));
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
		intent.putExtra(EXTRA_TAG_MUTATIONS, mutations);
		setResult(RESULT_OK, intent);
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
		mutations.add(new TagMutation(TagMutation.MutationType.EDIT, oldTag, newTag));
	}
}
