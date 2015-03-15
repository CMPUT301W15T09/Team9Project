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
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
public class ManageTagsActivity extends ListActivity implements TypedObserver<List<Tag>>{
	
	
	private static final int ADD_TAG_REQUEST= 1;
	private static final int EDIT_TAG_REQUEST= 2;
	private static final String TAG_FILENAME = "tags";

	private ListModel<Tag> listModel;
	private Button addButton;
	private int longPressedItemIndex;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		setupListFooterView(); //need to set up footer view first before setting up list 
		
		listModel = new ListModel<Tag>(TAG_FILENAME, this);
		listModel.addObserver(this);
		setListAdapter(new TagArrayAdapter(this, listModel.getItems()));
		
		
		final ActionMode.Callback longClickCallback = new ActionMode.Callback() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
					case R.id.action_edit:
						return false;
						//Need to call to edit tag
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
	
	private void setupListFooterView() {
		View footerView = getLayoutInflater().inflate(R.layout.activity_tag_add, getListView(), false);

		addButton = (Button)findViewById(R.id.button_add_tag);
		
		getListView().addFooterView(footerView);
	}
	
	@Override
	public void update(TypedObservable<List<Tag>> o, List<Tag> tags) {
		setListAdapter(new TagArrayAdapter(this, tags));
	}
	
	private void startAddTagActivity() {
		Intent addTagIntent = new Intent(this, TagAddActivity.class);
		startActivityForResult(addTagIntent, ADD_TAG_REQUEST);
	}
	/*
	private void editTagActivity() {
		Intent editTagIntent = new Intent(this, EditTagActivity.class);
		startActivityForResult(EditTagIntent, ADD_TAG_REQUEST);
	}
	*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;
		switch (requestCode) {
		case ADD_TAG_REQUEST:
			onAddTag(data);
			break;
		case EDIT_TAG_REQUEST:
			onEditTag(data);
			break;
		}
	}
	
	private void onAddTag(Intent data) {
		Tag tag = (Tag)data.getSerializableExtra(TagAddActivity.ADDED_TAG);
		listModel.add(tag);
		
	}
	
	private void onEditTag(Intent data) {
		Tag tag = (Tag)data.getSerializableExtra(TagAddActivity.ADDED_TAG);
		listModel.set(longPressedItemIndex, tag);
	}

}
