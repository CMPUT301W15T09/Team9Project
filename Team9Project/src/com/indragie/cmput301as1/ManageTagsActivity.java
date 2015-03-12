package com.indragie.cmput301as1;

import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ManageTagsActivity extends ListActivity implements TypedObserver<List<Tag>>{
	
	
	
	private static final String TAG_FILENAME = "tags";

	private ListModel<Tag> listModel;
	private List<Tag> tags; 
	private Button addButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupListFooterView(); //need to set up footer view first before setting up list 
		
		listModel = new ListModel<Tag>(TAG_FILENAME, this);
		listModel.addObserver(this);
		setListAdapter(new TagArrayAdapter(this, listModel.getItems()));
		
		
		View actionBarButtons = getLayoutInflater().inflate(R.layout.activity_editing_actionbar, new LinearLayout(this), false);
		View cancelButton = actionBarButtons.findViewById(R.id.action_cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onCancel();
			}
		});
		View doneButton = actionBarButtons.findViewById(R.id.action_done);
		doneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onDone();
			}
		});

		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(actionBarButtons);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		
		//Above taken from AddActivity, cannot have multiple inheritance
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
	public void update(TypedObservable<List<Tag>> o, List<Tag> tags) {
		setListAdapter(new TagArrayAdapter(this, tags));
	}

}
