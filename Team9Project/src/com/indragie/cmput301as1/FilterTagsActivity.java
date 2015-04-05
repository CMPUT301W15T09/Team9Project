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
		
		for(Tag tag: selectedTags) {
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
		if(getListView().getCheckedItemCount() == 0) {
			setResult(RESULT_CANCELED, intent);
		} else {
			SparseBooleanArray selectedIndexes = getListView().getCheckedItemPositions();
			System.out.println("Index size" + selectedIndexes.size());
			System.out.println("Before clear" + selectedTags.size());
			selectedTags.clear();
			System.out.println("After clear" + selectedTags.size());
			for(int index = 0; index < selectedIndexes.size(); index++) {
				if(selectedIndexes.valueAt(index)){
					int position = selectedIndexes.keyAt(index);
					selectedTags.add(listModel.getItems().get(position));
				}
			}
			intent.putExtra(TAG_TO_FILTER, selectedTags);
			System.out.println("After adding" + selectedTags.size());
			setResult(RESULT_OK, intent);
		}
		finish();
	}
	
	
}
