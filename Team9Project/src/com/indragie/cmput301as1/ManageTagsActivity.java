package com.indragie.cmput301as1;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;


/**
 * Activity for viewing the current list of tags the use has defined. 
 * Can direct user to activities for adding or editing tags. 
 * Allows user to remove existing tags. 
 */
public class ManageTagsActivity extends ListActivity implements TypedObserver<List<Tag>>{

	//================================================================================
	// Constants
	//================================================================================
	
	private static final int ADD_TAG_REQUEST= 1;
	private static final int EDIT_TAG_REQUEST= 2;
	private static final String TAG_FILENAME = "tags";

	public static final String EXTRA_TAG = "com.indragie.cmput301as1.EXTRA_TAG";
	
	//================================================================================
	// Properties
	//================================================================================

	private ListModel<Tag> listModel;
	private int longPressedItemIndex;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		listModel = new ListModel<Tag>(TAG_FILENAME, this);
		listModel.addObserver(this);
		setListAdapter(new TagArrayAdapter(this, listModel.getItems()));
		
		
		final ActionMode.Callback longClickCallback = new ActionMode.Callback() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
					case R.id.action_edit:
						startEditTagActivity();
						mode.finish();
						return true;
					case R.id.action_delete:
						listModel.remove(longPressedItemIndex);
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

		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				longPressedItemIndex = position;
				startActionMode(longClickCallback);
				return true;
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
		case R.id.action_add_tag:
			startAddTagActivity();
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void update(TypedObservable<List<Tag>> o, List<Tag> tags) {
		setListAdapter(new TagArrayAdapter(this, tags));
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
	// Add/Edit a tag
	//================================================================================
	
	private void startAddTagActivity() {
		Intent addTagIntent = new Intent(this, TagAddActivity.class);
		startActivityForResult(addTagIntent, ADD_TAG_REQUEST);
	}
	
	private void onAddTag(Intent data) {
		Tag tag = (Tag)data.getSerializableExtra(TagAddActivity.ADDED_TAG);
		listModel.add(tag);
		
	}
	
	private void startEditTagActivity() {
		Intent editTagIntent = new Intent(this, TagEditActivity.class);
		editTagIntent.putExtra(EXTRA_TAG, listModel.getItems().get(longPressedItemIndex));
		startActivityForResult(editTagIntent, EDIT_TAG_REQUEST);
	}
	
	private void onEditTag(Intent data) {
		Tag tag = (Tag)data.getSerializableExtra(TagAddActivity.ADDED_TAG);
		listModel.set(longPressedItemIndex, tag);
	}

}
