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

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.indragie.cmput301as1.ExpenseClaim.Status;

/**
 * Activity for viewing details of and editing an expense claim, including marking 
 * it as submitted/returned/approved and adding/deleting/editing expense items.
 */
public class ExpenseClaimDetailActivity extends ListActivity implements Observer {
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Intent key for the {@link ExpenseClaim} object.
	 */
	public static final String EXTRA_EXPENSE_CLAIM = "com.indragie.cmput301as1.EXTRA_CLAIM";
	
	/**
	 * Intent key for the position of the {@link ExpenseClaim} object in the expense claims list.
	 */
	public static final String EXTRA_EXPENSE_CLAIM_INDEX = "com.indragie.cmput301as1.EXTRA_EXPENSE_CLAIM_INDEX";
	
	/**
	 * Request code for starting {@link ExpenseItemAddActivity}
	 */
	private static final int ADD_EXPENSE_ITEM_REQUEST = 1;
	
	/**
	 * Request code for starting {@link ExpenseItemEditActivity}
	 */
	private static final int EDIT_EXPENSE_ITEM_REQUEST = 2;

	//================================================================================
	// Properties
	//================================================================================

	/**
	 * Whether the fields should be editable or not. This is dependent
	 * on the status of the expense claim.
	 */
	private Boolean editable;
	
	/**
	 * The expense claim for which details are being displayed.
	 */
	private ExpenseClaim claim;
	
	/**
	 * Field that displays the name of the expense claim.
	 */
	private EditText nameField;
	
	/**
	 * Field that displays the description of the expense claim.
	 */
	private EditText descriptionField;
	
	/**
	 * Field that displays the start date of the expense claim.
	 */
	private DateEditText startDateField;
	
	/**
	 * Field that displays the end date of the expense claim.
	 */
	private DateEditText endDateField;
	
	/**
	 * Field that displays the summarized amounts for the expense items.
	 */
	private TextView amountsTextView;
	
	/**
	 * Observable model for expense claim details.
	 */
	private ExpenseClaimDetailModel model;
	
	/**
	 * Controller for transforming model data into presentable
	 * data for the user interface.
	 */
	private ExpenseClaimDetailController controller;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		claim = (ExpenseClaim)intent.getSerializableExtra(EXTRA_EXPENSE_CLAIM);
		setTitle(claim.getName());
		
		model = new ExpenseClaimDetailModel(claim);
		model.addObserver(this);
		controller = new ExpenseClaimDetailController(this, model);
		
		setupListHeaderView();
		setupListFooterView();
		setEditable(claim.isEditable());
		setListAdapter(controller.getAdapter());
	}

	/**
	 * Sets up the header view for the list, containing the fields
	 * for editing the name, description, and start/end dates.
	 */
	private void setupListHeaderView() {
		View headerView = getLayoutInflater().inflate(R.layout.activity_claim_header, getListView(), false);

		nameField = (EditText)headerView.findViewById(R.id.et_name);
		nameField.setText(claim.getName());

		descriptionField = (EditText)headerView.findViewById(R.id.et_description);
		descriptionField.setText(claim.getDescription());

		startDateField = (DateEditText)headerView.findViewById(R.id.et_start_date);
		startDateField.setDate(claim.getStartDate());
		startDateField.setOnDateChangedListener(new DateEditText.OnDateChangedListener() {
			@Override
			public void onDateChanged(DateEditText view, Date date) {
				endDateField.setMinDate(date);
			}
		});

		endDateField = (DateEditText)headerView.findViewById(R.id.et_end_date);
		endDateField.setDate(claim.getEndDate());
		endDateField.setOnDateChangedListener(new DateEditText.OnDateChangedListener() {
			@Override
			public void onDateChanged(DateEditText view, Date date) {
				startDateField.setMaxDate(date);
			}
		});

		getListView().addHeaderView(headerView);
	}

	/**
	 * Sets up the footer view for the list, containing the summarized
	 * display of the expense item amounts.
	 */
	private void setupListFooterView() {
		View footerView = getLayoutInflater().inflate(R.layout.activity_claim_footer, getListView(), false);

		amountsTextView = (TextView)footerView.findViewById(R.id.tv_amounts);
		amountsTextView.setText(claim.getSummarizedAmounts());

		getListView().addFooterView(footerView);
	}

	/**
	 * Sets the editable state of the entire UI.
	 * @param editable Whether the claim is editable or not.
	 */
	private void setEditable(Boolean editable) {
		this.editable = editable;

		nameField.setEnabled(editable);
		descriptionField.setEnabled(editable);
		startDateField.setEnabled(editable);
		endDateField.setEnabled(editable);
		invalidateOptionsMenu();
		
		if (editable) {
			getListView().setOnItemLongClickListener(
				new LongClickDeleteListener(this, 
					new LongClickDeleteListener.OnDeleteListener() {
						@Override
						public void onDelete(int position) {
							model.removeItem(itemPositionForListViewPosition(position));
						}
					}
				)
			);
		} else {
			getListView().setOnItemLongClickListener(null);
		}
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
		model.addItem(item);
	}

	private void onEditExpenseItem(Intent data) {
		ExpenseItem item = (ExpenseItem)data.getSerializableExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM);
		int position = data.getIntExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM_POSITION, -1);
		model.setItem(position, item);
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
	
	/**
	 * Starts the {@link ExpenseItemAddActivity}
	 */
	private void startAddExpenseItemActivity() {
		Intent addIntent = new Intent(this, ExpenseItemAddActivity.class);
		startActivityForResult(addIntent, ADD_EXPENSE_ITEM_REQUEST);
	}
	
	/**
	 * Starts a choose activity for sending the {@link ExpenseClaim} contents
	 * as an email.
	 */
	private void startEmailActivity() {
		// Based on http://stackoverflow.com/a/2745702/153112
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Expense Claim: " + claim.getName());
		
		// Originally planned to use HTML for rich text in the email, but it turns
		// out that most email clients on Android (including K-9) don't support HTML
		// for composing emails, so I decided to use plain text instead.
		emailIntent.putExtra(Intent.EXTRA_TEXT, controller.getPlainText());
		startActivity(Intent.createChooser(emailIntent, "Send Email"));
	}
	
	/**
	 * Saves changes made to the expense claim and finishes the activity.
	 */
	private void commitChangesAndFinish() {
		claim.setName(nameField.getText().toString());
		claim.setDescription(descriptionField.getText().toString());
		claim.setStartDate(startDateField.getDate());
		claim.setEndDate(endDateField.getDate());

		Intent intent = new Intent();
		intent.putExtra(EXTRA_EXPENSE_CLAIM, claim);
		intent.putExtra(EXTRA_EXPENSE_CLAIM_INDEX, intent.getIntExtra(EXTRA_EXPENSE_CLAIM_INDEX, -1));
		setResult(RESULT_OK, intent);
		finish();
	}

	//================================================================================
	// ListView Callbacks
	//================================================================================

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		if (listView.getItemAtPosition(position) == null) return;
		
		int itemPosition = itemPositionForListViewPosition(position);
		ExpenseClaimDetailController.DetailItem.ItemType type = controller.getItemType(itemPosition);
		SectionedListIndex index = controller.getSectionedIndex(position);
		switch (type) {
		case DESTINATION:
			// TODO
			break;
		case EXPENSE_ITEM:
			startEditExpenseItemActivity(index.getItemIndex());
			break;
		default: break;
		}
	}
	
	/**
	 * Starts the {@link EditExpenseItemActivity}
	 * @param position The position of the {@link ExpenseItem} to edit.
	 */
	private void startEditExpenseItemActivity(int index) {
		Intent editIntent = new Intent(this, ExpenseItemEditActivity.class);
		editIntent.putExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM, controller.getExpenseItem(index));
		editIntent.putExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM_POSITION, index);
		editIntent.putExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM_EDITABLE, editable);
		startActivityForResult(editIntent, EDIT_EXPENSE_ITEM_REQUEST);
	}
	
	/**
	 * Returns the item position from a list view position by adjusting
	 * it to account for header and footer views.
	 * @param position The unadjusted list view position.
	 * @return The adjusted item position.
	 */
	private int itemPositionForListViewPosition(int position) {
		// Subtract 1 for the header
		return position - 1;
	}

	//================================================================================
	// Observable
	//================================================================================
	
	@Override
	public void update(Observable observable, Object object) {
		amountsTextView.setText(claim.getSummarizedAmounts());
	}
}

