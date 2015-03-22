package com.indragie.cmput301as1;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

public class TagListActivity extends ListActivity implements TypedObserver<List<Tag>>{

	//================================================================================
	// Constants
	//================================================================================
	
	private static final String TAG_FILENAME = "tags";

	public static final String TAG_TO_ADD = "com.indragie.cmput301as1.TAG_TO_ADD";
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * List model of tags.
	 */
	private ListModel<Tag> listModel;
	/**
	 * Index of a item that is long pressed.
	 */
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
					case R.id.action_accept:
						setResult(RESULT_OK, getTagSelected());
						finish();
						return true;
					default:
						return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				mode.getMenuInflater().inflate(R.menu.contextual_accept, menu);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onHome();
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
	
	protected void onHome() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}
	
	protected Intent getTagSelected() {
		Intent intent = new Intent();
		intent.putExtra(TAG_TO_ADD, listModel.getItems().get(longPressedItemIndex));
		return intent;
	}
	
}