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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

public class TagAddToClaimActivity extends ListActivity implements TypedObserver<List<Tag>>{

	//================================================================================
	// Constants
	//================================================================================
	
	protected static final String TAG_FILENAME = "tags";

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
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		listModel = new ListModel<Tag>(TAG_FILENAME, this);
		listModel.addObserver(this);
		setListAdapter(new TagArrayAdapter(this, listModel.getItems()));
		
		
		final ActionMode.Callback clickCallback = new ActionMode.Callback() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
					case R.id.action_add_tag_to_claim_string:
						setResult(RESULT_OK, getTagSelected());
						finish();
						return true;
					default:
						return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				mode.getMenuInflater().inflate(R.menu.add_tag_to_claim, menu);
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
	
	/**
	 * Sets intent as canceled so no changes are made when home button pressed.
	 */
	protected void onHome() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}
	
	/**
	 * Puts the selected tag in a returned intent.
	 * @return Intent with information about position of item. 
	 */
	protected Intent getTagSelected() {
		Intent intent = new Intent();
		intent.putExtra(TAG_TO_ADD, listModel.getItems().get(pressedItemIndex));
		return intent;
	}
	
}