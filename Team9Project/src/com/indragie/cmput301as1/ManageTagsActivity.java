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
import android.widget.Button;
import android.widget.DatePicker;
public class ManageTagsActivity extends ListActivity implements TypedObserver<List<Tag>>{
	
	
	
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
		listModel.add(new Tag("some tag"));
		listModel.add(new Tag("this other tag"));
		setListAdapter(new TagArrayAdapter(this, listModel.getItems()));
		
		
		final ActionMode.Callback longClickCallback = new ActionMode.Callback() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
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

}
