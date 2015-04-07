/* 
 * Copyright (C) 2015 Indragie Karunaratne, Andrew Zhong
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

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
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
	
	/**
	 * Intent key for the {@link User} object.
	 */
	public static final String EXTRA_EXPENSE_CLAIM_USER = "com.indragie.cmput301as1.EXTRA_EXPENSE_CLAIM_USER";
	
	/**
	 * Intent key for the position of the {@link ExpenseClaim} object in the expense claims list.
	 */
	public static final String EXTRA_EXPENSE_CLAIM_INDEX = "com.indragie.cmput301as1.EXTRA_EXPENSE_CLAIM_INDEX";
	
	/**
	 * Intent key for the position of the {@link ExpenseClaim} object in the filtered claims list.
	 */
	public static final String EXTRA_FILTERED_EXPENSE_CLAIM_INDEX = "com.indragie.cmput301as1.EXTRA_FILTERED_EXPENSE_CLAIM_INDEX";
	
	/**
	 * Request code for starting the {@link DestinationAddActivity}
	 */
	private static final int ADD_DESTINATION_REQUEST = 7;
	
	/**
	 * Request code for starting the {@link DestinationEditActivity}
	 */
	private static final int EDIT_DESTINATION_REQUEST = 8;
	
	/**
	 * Request code for starting {@link ExpenseItemAddActivity}
	 */
	private static final int ADD_EXPENSE_ITEM_REQUEST = 30;
	
	/**
	 * Request code for starting {@link ExpenseItemEditActivity}
	 */
	private static final int EDIT_EXPENSE_ITEM_REQUEST = 31;
	
	/**
	 * Request code for starting {@link TagAddToClaimActivity}
	 */
	private static final int ADD_TAG_REQUEST = 32;
	
	/**
	 * Request code for starting {@link TagEditToClaimActivity}
	 */

	private static final int EDIT_TAG_REQUEST = 33;

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
	
	/**
	 * Position of a long pressed item.
	 */
	private int longPressedItemPosition;

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
				
		model = new ExpenseClaimDetailModel(claim);
		model.addObserver(this);
		controller = new ExpenseClaimDetailController(this, model);
		
		setupListHeaderView();
		setupListFooterView();
		setListAdapter(controller.getAdapter());
		
		setEditable();
		setDeletable();
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
		User approver = claim.getApprover();
		if (approver != null) {
			approverField.append(approver.getName());
		}

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
		
		getListView().addFooterView(footerView, null, false);
	}
	
	/**
	 * Sets the editable state of the entire UI.
	 */
	private void setEditable(){
		boolean UserCheck = user.getName().contentEquals(claim.getUser().getName()); //SHOULD BE ID USING NAME FOR TESTING

		if(status == Status.SUBMITTED || status == Status.APPROVED ){
			nameField.setEnabled(false);
			descriptionField.setEnabled(false);
			startDateField.setEnabled(false);
			endDateField.setEnabled(false);
			comments.setEnabled(status != Status.APPROVED);
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
		invalidateOptionsMenu();
	}
	
	/**
	 * Sets the editable state of the entire UI.
	 */
	private void setDeletable() {
		getListView().setOnItemLongClickListener(new LongClickDeleteListener(this, new LongClickDeleteListener.OnDeleteListener() {
			@Override
			public void onDelete(int position) {
				if (getListView().getItemAtPosition(position) == null)
					return;
				ExpenseClaimDetailController.DetailItem.ItemType type = getTypeAt(position);
				startDeleteAlertDialog(type);
			}
			
			@Override
			public boolean shouldDelete(int position) {
				ExpenseClaimDetailController.DetailItem.ItemType type = getTypeAt(position);
				if (editable ||type == ExpenseClaimDetailController.DetailItem.ItemType.TAG) {
					return true;
				}
				return false;
			}
		}));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;
		switch (requestCode) {
		case ADD_DESTINATION_REQUEST:
			onAddDestination(data);
			break;
		case EDIT_DESTINATION_REQUEST:
			onEditDestination(data);
			break;
		case ADD_EXPENSE_ITEM_REQUEST:
			onAddExpenseItem(data);
			break;
		case EDIT_EXPENSE_ITEM_REQUEST:
			onEditExpenseItem(data);
			break;
		case ADD_TAG_REQUEST:
			onAddTag(data);
			break;
		case EDIT_TAG_REQUEST:
			onEditTag(data);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Retrieves the destination from a intent to add to the expense claim.
	 * @param data The intent.
	 */
	private void onAddDestination(Intent data) {
		Destination destination = (Destination)data.getSerializableExtra(DestinationAddActivity.EXTRA_DESTINATION);
		model.addDestination(destination);
	}

	/**
	 * Retrieves the destination from a intent to edit on the expense claim.
	 * @param data The intent.
	 */
	private void onEditDestination(Intent data) {
		Destination destination = (Destination)data.getSerializableExtra(DestinationEditActivity.EXTRA_DESTINATION);
		int position = data.getIntExtra(DestinationEditActivity.EXTRA_EDIT_DESTINATION_POSITION, -1);
		model.setDestination(position, destination);	
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
	 * @param data The intent.
	 */
	private void onEditExpenseItem(Intent data) {
		ExpenseItem item = (ExpenseItem)data.getSerializableExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM);
		int position = data.getIntExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM_POSITION, -1);
		model.setItem(position, item);
	}
	
	/**
	 * Retrieves the tag from intent to add to the expense claim.
	 * @param data The intent.
	 */
	private void onAddTag(Intent data) {
		Tag tag = (Tag)data.getSerializableExtra(TagAddToClaimActivity.TAG_TO_ADD);
		model.addTag(tag);
	}
	
	/**
	 * Retrieves the tag from intent to edit on the expense claim.
	 * @param data The intent.
	 */
	private void onEditTag(Intent data) {
		Tag tag = (Tag)data.getSerializableExtra(TagAddToClaimActivity.TAG_TO_ADD);
		int position = data.getIntExtra(TagEditToClaimActivity.EXTRA_TAG_POSITION, -1);
		model.setTag(position, tag);
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
		setEnabledMenuFields(menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			commitChangesAndFinish();
			return true;
		case R.id.action_add_destination:
			startDestinationAddActivity(); 
			return true;
		case R.id.action_add_item:
			startAddExpenseItemActivity();
			return true;
		case R.id.action_add_tag:
			startAddTagToClaimActivity();
			return true;
		case R.id.action_email:
			startEmailActivity();
			return true;
		case R.id.action_mark_submitted:
			startSubmitAlertDialog();
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
	
	/**
	 * Prompts the user for confirmation in response to marking an expense as submitted, additionally 
	 * the user will be notified if any incomplete expense items exist under the claim.
	 */
	private void startSubmitAlertDialog() {
		AlertDialog.Builder openDialog = new AlertDialog.Builder(this);
		openDialog.setTitle(R.string.alert_submit_title);
		
		for (ExpenseItem expense : model.getItems()) {
			if (expense.isIncomplete()) {
				openDialog.setMessage(R.string.alert_submit_message);
				break;
			}
		}
		
		openDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				claim.setStatus(Status.SUBMITTED);
				commitChangesAndFinish();
			}
		});
		
		openDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		openDialog.show();
	}
	
	/* 
	@SuppressLint("InflateParams")
	private AlertDialog buildDestinationAlertDialog(final int index) {
		View dialogView = getLayoutInflater().inflate(R.layout.activity_destination, null);
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
	*/
	/**
	 * Prompts the user for confirmation in response to deleting items in the activity.
	 * @param type The ItemType of the DetailItem.
	 */
	public void startDeleteAlertDialog(ExpenseClaimDetailController.DetailItem.ItemType type) {
		AlertDialog.Builder openDialog = new AlertDialog.Builder(this);
		switch (type) {
		case DESTINATION:
			openDialog.setTitle(R.string.action_delete_dest_confirm);
			break;
		case TAG:
			openDialog.setTitle(R.string.action_delete_tag_confirm);
			break;
		case EXPENSE_ITEM:
			openDialog.setTitle(R.string.action_delete_item_confirm);
			break;
		default: break;
		}
		openDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				controller.remove(longPressedItemPosition);
			}
		});
		
		openDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		openDialog.show();
	}
	
	/**
	 * Starts the {@link DestinationAddActivity}
	 */
	public void startDestinationAddActivity() {
		Intent addDestinationIntent = new Intent(this, DestinationAddActivity.class);
		addDestinationIntent.putExtra(DestinationAddActivity.EXTRA_USER, user);
		addDestinationIntent.putExtra(DestinationAddActivity.ADD_TO_CLAIM, true);
		startActivityForResult(addDestinationIntent, ADD_DESTINATION_REQUEST);
	}
	
	/**
	 * Starts the {@link ExpenseItemAddActivity}
	 */
	private void startAddExpenseItemActivity() {
		Intent addIntent = new Intent(this, ExpenseItemAddActivity.class);
		startActivityForResult(addIntent, ADD_EXPENSE_ITEM_REQUEST);
	}

	/**
	 * Starts the {@link TagAddToClaimActivity}
	 */
	private void startAddTagToClaimActivity() {
		Intent addTagIntent = new Intent(this, TagAddToClaimActivity.class);
		startActivityForResult(addTagIntent, ADD_TAG_REQUEST);
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
	
	/**
	 * Sets the menu fields as enabled or not depending on status of claim.
	 * @param menu The menu to set.
	 */
	public void setEnabledMenuFields(Menu menu) {

		boolean UserCheck = user.getName().contentEquals(claim.getUser().getName());//SHOULD BE ID USING NAME FOR TESTING
		MenuItem addDestination = menu.findItem(R.id.action_add_destination); 
		MenuItem addItem = menu.findItem(R.id.action_add_item);
		MenuItem submit = menu.findItem(R.id.action_mark_submitted);
		MenuItem approve = menu.findItem(R.id.action_mark_approved);
		MenuItem returned = menu.findItem(R.id.action_mark_returned);

		if (status == Status.APPROVED){
			addDestination.setEnabled(false);
			addItem.setEnabled(false);
			submit.setEnabled(false);
			approve.setEnabled(false);
			returned.setEnabled(false);
		}
		if(status == Status.RETURNED || status == Status.IN_PROGRESS){
			addDestination.setEnabled(UserCheck);
			addItem.setEnabled(UserCheck);
			submit.setEnabled(UserCheck);
			approve.setEnabled(false);
			returned.setEnabled(false);
		}
		if(status == Status.SUBMITTED){
			approve.setEnabled(!UserCheck);
			returned.setEnabled(!UserCheck);
			addDestination.setEnabled(false);
			addItem.setEnabled(false);
			submit.setEnabled(false);
		}
	}

	//================================================================================
	// ListView Callbacks
	//================================================================================

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		if (listView.getItemAtPosition(position) == null) return;
		
		int itemPosition = itemPositionForListViewPosition(position);
		ExpenseClaimDetailController.DetailItem.ItemType type = getTypeAt(position);
		SectionedListIndex index = controller.getSectionedIndex(itemPosition);
		
		switch (type) {
		case DESTINATION:
			startEditDestinationActivity(index.getItemIndex());
			break;
		case TAG:
			startEditTagActivity(index.getItemIndex());
			break;
		case EXPENSE_ITEM:
			startEditExpenseItemActivity(index.getItemIndex());
			break;
		default: break;
		}
	}
	
	/**
	 * Starts the {@link DestinationEditActivity}
	 * @param position The position of the {@link Destination} to edit.
	 */
	private void startEditDestinationActivity(int position) {
		Intent editDestinationIntent = new Intent(this, DestinationEditActivity.class);
		editDestinationIntent.putExtra(DestinationEditActivity.EXTRA_DESTINATION, controller.getDestination(position));
		editDestinationIntent.putExtra(DestinationEditActivity.ADD_TO_CLAIM, false);
		editDestinationIntent.putExtra(DestinationEditActivity.EXTRA_EDIT_DESTINATION_POSITION, position);
		editDestinationIntent.putExtra(DestinationEditActivity.EXTRA_EDIT_DESTINATION_EDITABLE, editable);
		startActivityForResult(editDestinationIntent, EDIT_DESTINATION_REQUEST);
	}
	
	/**
	 * Starts the {@link EditExpenseItemActivity}
	 * @param position The position of the {@link ExpenseItem} to edit.
	 */
	private void startEditExpenseItemActivity(int position) {
		Intent editIntent = new Intent(this, ExpenseItemEditActivity.class);
		editIntent.putExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM, controller.getExpenseItem(position));
		editIntent.putExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM_POSITION, position);
		editIntent.putExtra(ExpenseItemEditActivity.EXTRA_EXPENSE_ITEM_EDITABLE, editable);
		startActivityForResult(editIntent, EDIT_EXPENSE_ITEM_REQUEST);
	}
	
	/**
	 * Starts the {@link TagEditToClaimActivity}
	 * @param position The index of the {@link Tag} to edit.
	 */
	private void startEditTagActivity(int position) {
		Intent editTagIntent = new Intent(this, TagEditToClaimActivity.class);
		editTagIntent.putExtra(TagEditToClaimActivity.EXTRA_TAG_POSITION, position);
		startActivityForResult(editTagIntent, EDIT_TAG_REQUEST);
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
	
	/**
	 * Get the ItemType of a item at a specified position
	 * @param position The position of the item.
	 * @return The ItemType of the item.
	 */
	public ExpenseClaimDetailController.DetailItem.ItemType getTypeAt(int position) {
		longPressedItemPosition = itemPositionForListViewPosition(position);
		return controller.getItemType(longPressedItemPosition);
	}

	//================================================================================
	// Observable
	//================================================================================
	
	@Override
	public void update(TypedObservable<Object> observable, Object object) {
		amountsTextView.setText(claim.getSummarizedAmounts());
	}
	
}

