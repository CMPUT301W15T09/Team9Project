/* 
 * Copyright (C) 2015 Indragie Karunaratne
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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.indragie.cmput301as1.ExpenseClaim.Status;

/**
 * Activity for editing an expense claim, including marking it as submitted/returned/approved
 * and adding/deleting/editing expense items.
 */
public class ExpenseClaimEditActivity extends ListActivity {
	//================================================================================
	// Constants
	//================================================================================
	public static final String EXTRA_CLAIM = "com.indragie.cmput301as1.EXTRA_CLAIM";
	public static final String EXTRA_CLAIM_POSITION = "com.indragie.cmput301as1.EXTRA_CLAIM_POSITION";
	private static final int ADD_EXPENSE_ITEM_REQUEST = 1;

	//================================================================================
	// Properties
	//================================================================================

	private Boolean editable;
	private ExpenseClaim claim;
	private int claimPosition;
	private EditText nameField;
	private EditText descriptionField;
	private DateEditText startDateField;
	private DateEditText endDateField;
	private ExpenseItemArrayAdapter adapter;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		claim = (ExpenseClaim)intent.getSerializableExtra(EXTRA_CLAIM);
		claimPosition = intent.getIntExtra(EXTRA_CLAIM_POSITION, -1);
		setTitle(claim.getName());

		setupListHeaderView();
		setEditable(claim.isEditable());
		
		adapter = new ExpenseItemArrayAdapter(this, claim.getItems());
		getListView().setAdapter(adapter);
	}

	private void setupListHeaderView() {
		View headerView = getLayoutInflater().inflate(R.layout.activity_claim_header, getListView(), false);
		
		nameField = (EditText)headerView.findViewById(R.id.et_name);
		nameField.setText(claim.getName());
		nameField.addTextChangedListener(new OnTextChangedWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				claim.setName(s.toString());
			}
		});

		descriptionField = (EditText)headerView.findViewById(R.id.et_description);
		descriptionField.setText(claim.getDescription());
		descriptionField.addTextChangedListener(new OnTextChangedWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				claim.setDescription(s.toString());
			}
		});

		startDateField = (DateEditText)headerView.findViewById(R.id.et_start_date);
		startDateField.setDate(claim.getStartDate());
		startDateField.addTextChangedListener(new OnTextChangedWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				claim.setStartDate(startDateField.getDate());
			}
		});
		
		endDateField = (DateEditText)headerView.findViewById(R.id.et_end_date);
		endDateField.setDate(claim.getEndDate());
		endDateField.addTextChangedListener(new OnTextChangedWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				claim.setEndDate(endDateField.getDate());
			}
		});

		getListView().addHeaderView(headerView);
	}
	
	private void setEditable(Boolean editable) {
		this.editable = editable;
		
		nameField.setEnabled(editable);
		descriptionField.setEnabled(editable);
		startDateField.setEnabled(editable);
		endDateField.setEnabled(editable);
		invalidateOptionsMenu();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;
		switch (requestCode) {
		case ADD_EXPENSE_ITEM_REQUEST:
			onAddExpenseItem(data);
			break;
		default:
			break;
		}
	}
	
	private void onAddExpenseItem(Intent data) {
		ExpenseItem item = (ExpenseItem)data.getSerializableExtra(ExpenseItemAddActivity.EXTRA_EXPENSE_ITEM);
		claim.addItem(item);
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.expense_claim_edit, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.action_add_item).setEnabled(editable);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			commitChangesAndFinish();
			return true;
		case R.id.action_add_item:
			Intent addIntent = new Intent(this, ExpenseItemAddActivity.class);
			startActivityForResult(addIntent, ADD_EXPENSE_ITEM_REQUEST);
			return true;
		case R.id.action_mark_submitted:
			claim.setStatus(Status.SUBMITTED);
			commitChangesAndFinish();
			return true;
		case R.id.action_mark_returned:
			claim.setStatus(Status.RETURNED);
			setEditable(true);
			return true;
		case R.id.action_mark_approved:
			claim.setStatus(Status.APPROVED);
			commitChangesAndFinish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void commitChangesAndFinish() {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_CLAIM, claim);
		intent.putExtra(EXTRA_CLAIM_POSITION, claimPosition);
		setResult(RESULT_OK, intent);
		finish();
	}
}

