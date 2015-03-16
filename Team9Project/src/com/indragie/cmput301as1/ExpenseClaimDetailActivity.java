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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
public class ExpenseClaimDetailActivity extends ListActivity implements TypedObserver<Object> {
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Intent key for the {@link ExpenseClaim} object.
	 */
	public static final String EXTRA_EXPENSE_CLAIM = "com.indragie.cmput301as1.EXTRA_CLAIM";
	public static final String EXTRA_EXPENSE_CLAIM_POSITION = "com.indragie.cmput301as1.EXTRA_EXPENSE_CLAIM_POSITION";
	public static final String EXTRA_EXPENSE_CLAIM_USER = "com.indragie.cmput301as1.EXTRA_EXPENSE_CLAIM_USER";
	
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
	
	/**
	 * Index used to indicate the nonexistence of an index.
	 */
	private static final int NO_INDEX = -1;

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
	 * Field that displays approver comments.
	 */
	private EditText comments;
	
	/**
	 * Field that displays the name of the user.
	 */
	private TextView userField;
	
	/**
	 * Field that displays the name of the approver.
	 */
	private TextView approverField;
	
	/**
	 * The current user.
	 */
	private User user;
	
	/**
	 * The status of the expense claim.
	 */
	private Status status;
	
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
		user = (User)intent.getSerializableExtra(EXTRA_EXPENSE_CLAIM_USER);
		status = claim.getStatus();

		setEditable();
		
		claim = (ExpenseClaim)getIntent().getSerializableExtra(EXTRA_EXPENSE_CLAIM);
		
		model = new ExpenseClaimDetailModel(claim);
		model.addObserver(this);
		controller = new ExpenseClaimDetailController(this, model);
		
		setupListHeaderView();
		setupListFooterView();
		setEditable(claim.isEditable());
		setListAdapter(controller.getAdapter());
	}
	
	@Override
	protected void onDestroy() {
		model.deleteObserver(this);
		super.onDestroy();
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

		userField = (TextView)headerView.findViewById(R.id.tv_user);
		userField.append(claim.getUser().getName());

		approverField = (TextView)headerView.findViewById(R.id.tv_approver);
		approverField.append(claim.getApprover().getName());


		comments = (EditText)headerView.findViewById(R.id.et_comments);
		comments.setText(claim.getComments());


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
	private void setEditable(){
		boolean UserCheck = user.getName().contentEquals(claim.getUser().getName());//SHOULD BE ID USING NAME FOR TESTING


		if(status == Status.SUBMITTED ){
			nameField.setEnabled(false);
			descriptionField.setEnabled(false);
			startDateField.setEnabled(false);
			endDateField.setEnabled(false);
			comments.setEnabled(!UserCheck);
			this.editable = false;
		}
		else if(status == Status.APPROVED){
			nameField.setEnabled(false);
			descriptionField.setEnabled(false);
			startDateField.setEnabled(false);
			endDateField.setEnabled(false);
			comments.setEnabled(false);	
			this.editable = false;
		}
		else {
			nameField.setEnabled(UserCheck);
			descriptionField.setEnabled(UserCheck);
			startDateField.setEnabled(UserCheck);
			endDateField.setEnabled(UserCheck);
			comments.setEnabled(false);
			this.editable= UserCheck;
		}
	}
	
	/**
	 * Sets the editable state of the entire UI.
	 * @param editable Whether the claim is editable or not.
	 */
	private void setEditable(Boolean editable) {
		this.editable = editable;
		invalidateOptionsMenu();
		if (editable) {
			getListView().setOnItemLongClickListener(
				new LongClickDeleteListener(this, 
					new LongClickDeleteListener.OnDeleteListener() {
						@Override
						public void onDelete(int position) {
							controller.remove(itemPositionForListViewPosition(position));
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

	/**
	 * Retrieves the expense item from a intent to add to the expense claim.
	 * @param data The intent.
	 */
	private void onAddExpenseItem(Intent data) {
		ExpenseItem item = (ExpenseItem)data.getSerializableExtra(ExpenseItemAddActivity.EXTRA_EXPENSE_ITEM);
		model.addItem(item);
	}

	/**
	 * Retrieves the expense item from a intent to edit on the expense claim.
	 * @param data The intent
	 */
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
		boolean UserCheck = user.getName().contentEquals(claim.getUser().getName());//SHOULD BE ID USING NAME FOR TESTING
		MenuItem add= menu.findItem(R.id.action_add_item);
		MenuItem submit = menu.findItem(R.id.action_mark_submitted);
		MenuItem approve = menu.findItem(R.id.action_mark_approved);
		MenuItem returned = menu.findItem(R.id.action_mark_returned);

		if (status ==Status.APPROVED){
			add.setEnabled(false);
			submit.setEnabled(false);
			approve.setEnabled(false);
			returned.setEnabled(false);
		}
		if(status == Status.RETURNED){
			if(UserCheck){
				add.setEnabled(true);
				submit.setEnabled(true);
			}
			else{
				add.setEnabled(false);
				submit.setEnabled(false);
			}
			approve.setEnabled(false);
			returned.setEnabled(false);
		}
		if(status==Status.SUBMITTED){
			if(UserCheck){
				approve.setEnabled(false);
				returned.setEnabled(false);
			}
			else{
				approve.setEnabled(true);
				returned.setEnabled(true);
			}
			add.setEnabled(false);
			submit.setEnabled(false);
		}
		if(status== Status.IN_PROGRESS){
			if(UserCheck){
				add.setEnabled(true);
				submit.setEnabled(true);
			}
			else{
				add.setEnabled(false);
				submit.setEnabled(false);
			}
			approve.setEnabled(false);
			returned.setEnabled(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			commitChangesAndFinish();
			return true;
		case R.id.action_add_destination:
			buildDestinationAlertDialog().show();
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
			claim.setApprover(user);
			setEditable();
			return true;
		case R.id.action_mark_approved:
			claim.setStatus(Status.APPROVED);
			claim.setApprover(user);
			commitChangesAndFinish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private AlertDialog buildDestinationAlertDialog() {
		return buildDestinationAlertDialog(NO_INDEX);
	}
	
	@SuppressLint("InflateParams")
	private AlertDialog buildDestinationAlertDialog(final int index) {
		View dialogView = getLayoutInflater().inflate(R.layout.destination_alert, null);
		final EditText nameField = (EditText)dialogView.findViewById(R.id.et_name);
		final EditText reasonField = (EditText)dialogView.findViewById(R.id.et_travel_reason);
		
		if (index != NO_INDEX) {
			Destination destination = controller.getDestination(index);
			nameField.setText(destination.getName());
			reasonField.setText(destination.getTravelReason());
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
		.setTitle(R.string.action_add_destination)
		.setView(dialogView)
		.setPositiveButton(android.R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Destination destination = new Destination(nameField.getText().toString(), reasonField.getText().toString());
				if (index != NO_INDEX) {
					model.setDestination(index, destination);
				} else {
					model.addDestination(destination);
				}
			}
		})
		.setNegativeButton(android.R.string.cancel, null);
		return builder.create();
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
		claim.setComments(comments.getText().toString());

		Intent intent = new Intent();
		intent.putExtra(EXTRA_EXPENSE_CLAIM, claim);
		intent.putExtra(EXTRA_EXPENSE_CLAIM_INDEX, getIntent().getIntExtra(EXTRA_EXPENSE_CLAIM_INDEX, -1));
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
		SectionedListIndex index = controller.getSectionedIndex(itemPosition);
		
		switch (type) {
		case DESTINATION:
			buildDestinationAlertDialog(index.getItemIndex()).show();
			break;
		case TAG:
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
	public void update(TypedObservable<Object> observable, Object object) {
		amountsTextView.setText(claim.getSummarizedAmounts());
	}
}

