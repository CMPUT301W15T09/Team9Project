package com.indragie.cmput301as1;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

public class TagEditToClaimActivity extends TagAddToClaimActivity {
	
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Intent key for the position of the {@link ExpenseItem} object.
	 */
	public static final String EXTRA_TAG_POSITION = "com.indragie.cmput301as1.TAG_POSITION";
	
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
		
		
		final ActionMode.Callback clickCallback = new ActionMode.Callback() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
					case R.id.action_replace_tag:
						setResult(RESULT_OK, getTagSelected());
						finish();
						return true;
					default:
						return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				mode.getMenuInflater().inflate(R.menu.replace_tag, menu);
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {}

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false;
			}
		};

		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				pressedItemIndex = position;
				startActionMode(clickCallback);
			}
		});
				
		
	}

	@Override
	protected Intent getTagSelected() {
		Intent intent = super.getTagSelected();
		intent.putExtra(EXTRA_TAG_POSITION, getIntent().getIntExtra(EXTRA_TAG_POSITION, -1));
		return intent;
	}
	
}
