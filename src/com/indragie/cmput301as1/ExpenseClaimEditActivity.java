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
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.indragie.cmput301as1.ExpenseClaim.Status;

/**
 * Activity for editing an expense claim, including marking it as submitted/returned/approved
 * and adding/deleting/editing expense items.
 */
public class ExpenseClaimEditActivity extends ListActivity {
	//================================================================================
	// Constants
	//================================================================================
	public static final String EXTRA_EXPENSE_CLAIM = "com.indragie.cmput301as1.EXTRA_CLAIM";
	public static final String EXTRA_EXPENSE_CLAIM_POSITION = "com.indragie.cmput301as1.EXTRA_EXPENSE_CLAIM_POSITION";
	private static final int ADD_EXPENSE_ITEM_REQUEST = 1;
	private static final int EDIT_EXPENSE_ITEM_REQUEST = 2;

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
	private TextView amountsTextView;
	private ExpenseItemArrayAdapter adapter;
	private int longPressedItemIndex;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		claim = (ExpenseClaim)intent.getSerializableExtra(EXTRA_EXPENSE_CLAIM);
		claimPosition = intent.getIntExtra(EXTRA_EXPENSE_CLAIM_POSITION, -1);
		setTitle(claim.getName());

		setupListHeaderView();
		setupListFooterView();
		setEditable(claim.isEditable());

		adapter = new ExpenseItemArrayAdapter(this, claim.getItems());
		setListAdapter(adapter);
		
		final ActionMode.Callback longClickCallback = new ActionMode.Callback() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.action_delete:
					claim.removeItem(longPressedItemIndex);
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
					longPressedItemIndex = (int)id;
					startActionMode(longClickCallback);
					return true;
				} else {
					return false;
				}
			}
		});
	}

	private void setupListHeaderView() {
		View headerView = getLayoutInflater().inflate(R.layout.activity_claim_header, getListView(), false);

		nameField = (EditText)headerView.findViewById(R.id.et_name);
		nameField.setText(claim.getName());
		nameField.requestFocus();

		descriptionField = (EditText)headerView.findViewById(R.id.et_description);
		descriptionField.setText(claim.getDescription());

		startDateField = (DateEditText)headerView.findViewById(R.id.et_start_date);
		startDateField.setDate(claim.getStartDate());

		endDateField = (DateEditText)headerView.findViewById(R.id.et_end_date);
		endDateField.setDate(claim.getEndDate());

		getListView().addHeaderView(headerView);
	}

	private void setupListFooterView() {
		View footerView = getLayoutInflater().inflate(R.layout.activity_claim_footer, getListView(), false);

		amountsTextView = (TextView)footerView.findViewById(R.id.tv_amounts);
		amountsTextView.setText(claim.getSummarizedAmounts());

		getListView().addFooterView(footerView);
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
		case EDIT_EXPENSE_ITEM_REQUEST:
			onEditExpenseItem(data);
			break;
		default:
			break;
		}
	}

	private void onAddExpenseItem(Intent data) {
		ExpenseItem item = (ExpenseItem)data.getSerializableExtra(ExpenseItemAddActivity.EXTRA_EXPENSE_ITEM);
		claim.addItem(item);
		
		updateInterfaceForDataSetChange();
	}

	private void onEditExpenseItem(Intent data) {
		ExpenseItem item = (ExpenseItem)data.getSerializableExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM);
		int position = data.getIntExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM_POSITION, -1);
		claim.setItem(position, item);
		
		updateInterfaceForDataSetChange();
	}

	private void updateInterfaceForDataSetChange() {
		amountsTextView.setText(claim.getSummarizedAmounts());
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onBackPressed() {
		// Changes should persist even when the back button is pressed,
		// since this is for editing and not adding.
		commitChangesAndFinish();
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
			startAddExpenseItemActivity();
			return true;
		case R.id.action_email:
			startEmailActivity();
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
	
	private void startAddExpenseItemActivity() {
		Intent addIntent = new Intent(this, ExpenseItemAddActivity.class);
		startActivityForResult(addIntent, ADD_EXPENSE_ITEM_REQUEST);
	}
	
	private void startEmailActivity() {
		// Based on http://stackoverflow.com/a/2745702/153112
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Expense Claim: " + claim.getName());
		
		// Originally planned to use HTML for rich text in the email, but it turns
		// out that most email clients on Android (including K-9) don't support HTML
		// for composing emails, so I decided to use plain text instead.
		emailIntent.putExtra(Intent.EXTRA_TEXT, claim.getPlainText(this));
		startActivity(Intent.createChooser(emailIntent, "Send Email"));
	}
	
	private void commitChangesAndFinish() {
		claim.setName(nameField.getText().toString());
		claim.setDescription(descriptionField.getText().toString());
		claim.setStartDate(startDateField.getDate());
		claim.setEndDate(endDateField.getDate());

		Intent intent = new Intent();
		intent.putExtra(EXTRA_EXPENSE_CLAIM, claim);
		intent.putExtra(EXTRA_EXPENSE_CLAIM_POSITION, claimPosition);
		setResult(RESULT_OK, intent);
		finish();
	}

	//================================================================================
	// ListView Callbacks
	//================================================================================

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		// If the header or footer was clicked, the item will be null.
		ExpenseItem item = (ExpenseItem)listView.getItemAtPosition(position);
		if (item == null) return;
		
		Intent editIntent = new Intent(this, ExpenseItemEditActivity.class);
		editIntent.putExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM, item);
		editIntent.putExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM_POSITION, (int)id);
		editIntent.putExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM_EDITABLE, editable);
		startActivityForResult(editIntent, EDIT_EXPENSE_ITEM_REQUEST);
	}
}

