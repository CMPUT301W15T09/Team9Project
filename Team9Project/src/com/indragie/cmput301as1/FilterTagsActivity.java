package com.indragie.cmput301as1;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ActionMode.Callback;
import android.widget.AdapterView;

public class FilterTagsActivity extends ListActivity {

	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Filename for storing tags.
	 */
	protected static final String TAG_FILENAME = "tags";
	
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
		//listModel.addObserver(this);
		setListAdapter(new TagFilterArrayAdapter(this, listModel.getItems()));
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onHome();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	/*
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
	*/
	
	/*
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		setResult(RESULT_OK, getTagSelected(position));
		finish();
	}
	*/
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
