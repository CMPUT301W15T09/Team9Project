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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Activity that presents a user interface for entering information to 
 * add a tag to a claim.
 */
public class TagAddToClaimActivity extends ListActivity implements TypedObserver<CollectionMutation<Tag>>{

	//================================================================================
	// Constants
	//================================================================================
	
	protected static final int ADD_TAG_REQUEST= 1;
	
	/**
	 * Filename for storing tags.
	 */
	protected static final String TAG_FILENAME = "tags";
	
	/**
	 * Intent key for the {@link Tag} object.
	 */
	public static final String TAG_TO_ADD = "com.indragie.cmput301as1.TAG_TO_ADD";
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * List model of tags.
	 */
	protected ListModel<Tag> listModel;
	/**
	 * Index of a item that is pressed.
	 */
	protected int pressedItemIndex;

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
	protected void setUpActionBarAndModel() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		listModel = new ListModel<Tag>(TAG_FILENAME, this);
		listModel.addObserver(this);
		setListAdapter(new TagArrayAdapter(this, listModel.getItems()));
	}
	
	/**
	 * Sets up the item click listener for when a tag is selected.
	 * @param clickCallback The Callback for when selected.
	 */
	protected void setUpItemClickListener(final Callback clickCallback) {
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				pressedItemIndex = position;
				startActionMode(clickCallback);
			}
		});
	}
	
	@Override 
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contextual_add,menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onHome();
			return true;
		case R.id.action_add_tag:
			startAddTagActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void update(TypedObservable<CollectionMutation<Tag>> observable, CollectionMutation<Tag> mutation) {
		setListAdapter(new TagArrayAdapter(this, listModel.getItems()));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;
		switch (requestCode) {
		case ADD_TAG_REQUEST:
			onAddTag(data);
			break;
		}
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		setResult(RESULT_OK, getTagSelected(position));
		finish();
	}
	
	//================================================================================
	// Add/Get a tag
	//================================================================================
	
	/**
	 * Adds a tag to the list model from a resulting activity.
	 * @param data The intent from resulting activity.
	 */
	protected void onAddTag(Intent data) {
		Tag tag = (Tag)data.getSerializableExtra(TagAddActivity.ADDED_TAG);
		listModel.add(tag);
	}
	
	/**
	 * Starts the activity to add a tag.
	 */
	private void startAddTagActivity() {
		Intent addTagIntent = new Intent(this, TagAddActivity.class);
		startActivityForResult(addTagIntent, ADD_TAG_REQUEST);
	}
	
	/**
	 * Puts the selected tag in a returned intent.
	 * @return Intent with information about position of item. 
	 */
	protected Intent getTagSelected(int position) {
		Intent intent = new Intent();
		intent.putExtra(TAG_TO_ADD, getTagAt(position));
		return intent;
	}
	
	/**
	 * Sets intent as canceled so no changes are made when home button pressed.
	 */
	protected void onHome() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}
	
	/**
	 * Gets tag a specified position
	 * @param position The position of the tag in the listView.
	 * @return The Tag.
	 */
	protected Tag getTagAt(int position) {
		return listModel.getItems().get(position);
	}
	
}