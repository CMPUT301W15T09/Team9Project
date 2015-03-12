package com.indragie.cmput301as1;

import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

public class ManageTagsActivity extends ListActivity implements TypedObserver<List<Tag>>{
	
	
	
	private static final String TAG_FILENAME = "tags";

	private Boolean editable = true;
	private ListModel<Tag> listModel;
	private Button addButton;
	private int longPressedItemIndex;
	private TagArrayAdapter adapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupListFooterView(); //need to set up footer view first before setting up list 
		
		listModel = new ListModel<Tag>(TAG_FILENAME, this);
		listModel.addObserver(this);
		setListAdapter(new TagArrayAdapter(this, listModel.getItems()));
		
		
		final ActionMode.Callback longClickCallback = new ActionMode.Callback() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.action_delete:
					listModel.getItems().remove(longPressedItemIndex);
					updateInterfaceForDataSetChange();
					mode.finish();
					return true;
				default:
					return false;
				}
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
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
				if (editable) {
					longPressedItemIndex = itemPositionForListViewPosition(position);
					startActionMode(longClickCallback);
					return true;
				} else {
					return false;
				}
			}
		});
		
		
		
		
	}

	protected void onCancel() {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}

	protected void onDone() {
		setResult(RESULT_OK, constructResultIntent());
		finish();
	}

	//If I don't save the tags
	private Intent constructResultIntent() {
		return new Intent();
	}
	
	
	
	private void setupListFooterView() {
		View footerView = getLayoutInflater().inflate(R.layout.activity_tag_add, getListView(), false);

		addButton = (Button)findViewById(R.id.button_add_tag);
		
		getListView().addFooterView(footerView);
	}

	@Override
	public void onBackPressed() {
		// Changes should persist even when the back button is pressed,
		// since this is for editing and not adding.
		commitChangesAndFinish();
	}
	
	private void commitChangesAndFinish() {
		/*
		claim.setName(nameField.getText().toString());
		claim.setDescription(descriptionField.getText().toString());
		claim.setStartDate(startDateField.getDate());
		claim.setEndDate(endDateField.getDate());
		 */
		finish();
	}
	
	private int itemPositionForListViewPosition(int position) {
		// Subtract 1 for the header
		return position - 1;
	}
	
	private void updateInterfaceForDataSetChange() {
		adapter.notifyDataSetChanged();
	}
	
	
	@Override
	public void update(TypedObservable<List<Tag>> o, List<Tag> tags) {
		setListAdapter(new TagArrayAdapter(this, tags));
	}

}
