package com.indragie.cmput301as1;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.widget.ListView;

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
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		setListAdapter(new TagFilterArrayAdapter(this, listModel.getItems()));
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
	protected void onHome() {
		Intent intent = new Intent();
		if(getListView().getCheckedItemCount() == 0) {
			setResult(RESULT_CANCELED, intent);
		} else {
			SparseBooleanArray selectedIndexes = getListView().getCheckedItemPositions();
			ArrayList<Tag> selectedTags = new ArrayList<Tag>();
			for(int index = 0; index < selectedIndexes.size(); index++) {
				int position = selectedIndexes.keyAt(index);
				selectedTags.add(listModel.getItems().get(position));
			}
			intent.putExtra(TAG_TO_FILTER, selectedTags);
			setResult(RESULT_OK, intent);
		}
		finish();
	}
	
	
}
