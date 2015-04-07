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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * An activity that presents a list of expense claims.
 */
public class ExpenseClaimListActivity extends ListActivity implements TypedObserver<List<ExpenseClaim>> {
	//================================================================================
	// Constants
	//================================================================================

	private static final int ADD_EXPENSE_CLAIM_REQUEST = 1;
	private static final int EDIT_EXPENSE_CLAIM_REQUEST = 2;
	private static final int SORT_EXPENSE_CLAIM_REQUEST = 3;
	private static final int MANAGE_TAGS_REQUEST = 4;
	private static final int FILTER_TAGS_REQUEST = 5;
	private static final int USER_SETTINGS_REQUEST = 6;
	
	//================================================================================
	// Properties
	//================================================================================

	/**
	 * Controller for this activity.
	 */
	private ExpenseClaimListController controller;
	
	/**
	 * Manages the user and associated preferences.
	 */
	private UserManager userManager;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		userManager = new UserManager(this);
		if (userManager.getActiveUser() == null) {
			startUserSettingsActivity();
		} else {
			loadData();
		}
		
		getListView().setOnItemLongClickListener(
				new LongClickDeleteListener(this, new LongClickDeleteListener.OnDeleteListener() {
			@Override
			public void onDelete(int position) {
				showDeleteAlertDialog(position);
			}
			
			@Override
			public boolean shouldDelete(int position) {
				return true;
			}
		}));
	}
	
	/**
	 * Loads the expense claim data to display in the {@link ListView}
	 */
	private void loadData() {
		// Create the application-wide session
		Session session = new Session(this, userManager.getActiveUser());
		Session.setSharedSession(session);

		// Show the initial list of expense claims (persisted on disk)
		ListModel<ExpenseClaim> listModel = session.getOwnedClaims();
		controller = new ExpenseClaimListController(this, listModel);
		controller.addObserver(this);
		setListAdapter(new ExpenseClaimArrayAdapter(this, listModel.getItems(), userManager.getActiveUser()));

		// Load the new list from the server
		final Context context = this;
		session.loadOwnedClaims(new ElasticSearchAPIClient.APICallback<List<ExpenseClaim>>() {
			@Override
			public void onSuccess(Response response, final List<ExpenseClaim> claims) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						controller.replace(claims);
					}
				});
			}

			@Override
			public void onFailure(Request request, Response response, IOException e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(context, R.string.load_fail_error, Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}

	@Override
	public void onDestroy() {
		controller.deleteObserver(this);
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			if (resultCode == RESULT_CANCELED && requestCode == FILTER_TAGS_REQUEST) {
				controller.removeFilter();
			}
			return;
		}
		switch (requestCode) {
		case ADD_EXPENSE_CLAIM_REQUEST:
			onAddExpenseResult(data);
			break;
		case EDIT_EXPENSE_CLAIM_REQUEST:
			onEditExpenseResult(data);
			break;
		case SORT_EXPENSE_CLAIM_REQUEST:
			onSortExpenseResult(data);
			break;
		case MANAGE_TAGS_REQUEST:
			onManageTagsResult(data);
			break;
		case FILTER_TAGS_REQUEST:
			onFilterTagsRequest(data);
			break;
		case USER_SETTINGS_REQUEST:
			onUserSettingsResult(data);
			break;
		}
	}
	
	/**
	 * Sets the active user of the application.
	 * @param data The intent to get the user from.
	 */
	private void onUserSettingsResult(Intent data) {
		User user = (User)data.getSerializableExtra(UserSettingsActivity.EXTRA_USER);
		userManager.setActiveUser(user);
	}
	
	/**
	 * Changes the sorting mode based on a comparator chosen by {@link ExpenseClaimSortActivity}
	 * @param data The intent to get the comparator from.
	 */
	@SuppressWarnings("unchecked")
	private void onSortExpenseResult(Intent data) {
		Comparator<ExpenseClaim> comparator = (Comparator<ExpenseClaim>)data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT);
		controller.sort(comparator);
	}

	/**
	 * Adds a expense claim to list model from a intent.
	 * Displays the filteredListModel instead if there are filtered tags.
	 * @param data The intent to get the expense claim from.
	 */
	private void onAddExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM);
		controller.add(claim);
	}
	
	/**
	 * Sets a expense claim at a specified position in the list model from a intent.
	 * @param data The intent to get the expense claim from.
	 */
	private void onEditExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM);
		int position = data.getIntExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_INDEX, -1);
		controller.set(position, claim);
	}

	/**
	 * Sets the list used in ListModel to the returned list of expense claims from the intent. 
	 * If tag is in the filteredTagsList, we have to update the list to accommodate for the changes.
	 * Displays the filteredListModel instead if there are filtered tags.
	 * @param data The intent to get the list of expense claims from. 
	 */
	@SuppressWarnings("unchecked")
	private void onManageTagsResult(Intent data) {
		ArrayList<ManageTagsActivity.TagMutation> mutations =
				(ArrayList<ManageTagsActivity.TagMutation>)data.getSerializableExtra(ManageTagsActivity.EXTRA_TAG_MUTATIONS);
		controller.processTagMutations(mutations);
	}
	
	/**
	 * Sets the listModel used to filteredListModel.
	 * @param data The intent to get the filteredTagsList.
	 */
	@SuppressWarnings("unchecked")
	private void onFilterTagsRequest(Intent data) {
		ArrayList<Tag> tags = (ArrayList<Tag>)data.getSerializableExtra(FilterTagsActivity.TAG_TO_FILTER);
		controller.filter(tags);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.expense_claim_list_owned, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_claim:
			startAddExpenseClaimActivity();
			return true;
		case R.id.action_sort_claim:
			startSortExpenseClaimActivity();
			return true;
		case R.id.action_manage_tags:
			startManageTagsActivity();
			return true;
		case R.id.action_user_settings:
			startUserSettingsActivity();
			return true;
		case R.id.action_filter_tags:
			startFilterTagsActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Starts the {@link ExpenseClaimAddActivity}
	 */
	private void startAddExpenseClaimActivity() {
		Intent addIntent = new Intent(this, ExpenseClaimAddActivity.class);
		addIntent.putExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM_USER, userManager.getActiveUser());
		startActivityForResult(addIntent, ADD_EXPENSE_CLAIM_REQUEST);
	}
	
	/**
	 * Starts the {@link ExpenseClaimSortActivity}
	 */
	private void startSortExpenseClaimActivity() {
		Intent intent = new Intent(this, ExpenseClaimSortActivity.class);
		startActivityForResult(intent, SORT_EXPENSE_CLAIM_REQUEST);
	}
		
	/**
	 * Starts the {@link ManageTagsActivity}
	 */
	private void startManageTagsActivity() {
		Intent manageTagsIntent = new Intent(this, ManageTagsActivity.class);
		startActivityForResult(manageTagsIntent, MANAGE_TAGS_REQUEST);
	}
	
	/**
	 * Starts the {@link FilterTagsActivity}
	 */
	private void startFilterTagsActivity() {
		Intent filterTagsIntent = new Intent(this, FilterTagsActivity.class);
		filterTagsIntent.putExtra(FilterTagsActivity.TAG_TO_FILTER, new ArrayList<Tag>(controller.getFilterTags()));
		startActivityForResult(filterTagsIntent, FILTER_TAGS_REQUEST);
	}
	
	/**
	 * Starts the {@link UserSettingsActivity}
	 */
	private void startUserSettingsActivity() {
		Intent userSettingsIntent = new Intent(this, UserSettingsActivity.class);
		userSettingsIntent.putExtra(UserSettingsActivity.EXTRA_USER, userManager.getActiveUser());
		startActivityForResult(userSettingsIntent, USER_SETTINGS_REQUEST);
	}
	
	/**
	 * Prompts the user for confirmation in response to deleting an expense claim.
	 * @param index The index of the expense claim to remove.
	 */
	public void showDeleteAlertDialog(final int index) {
		AlertDialog.Builder openDialog = new AlertDialog.Builder(this);
		openDialog.setTitle(R.string.action_delete_claim_confirm);
		
		openDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				controller.remove(index);
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

	//================================================================================
	// ListView Callbacks
	//================================================================================

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		startEditExpenseClaimActivity(position);
	}
	
	/**
	 * Calls the intent to edit a expense claim at a specified position.
	 * @param position The position of the expense claim to edit.
	 */
	private void startEditExpenseClaimActivity(int position) {
		Intent editIntent = new Intent(this, ExpenseClaimDetailActivity.class);
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM, controller.get(position));
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_INDEX, position);
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_USER, userManager.getActiveUser());
		startActivityForResult(editIntent, EDIT_EXPENSE_CLAIM_REQUEST);
	}

	//================================================================================
	// TypedObserver<List<ExpenseClaim>>
	//================================================================================

	@Override
	public void update(TypedObservable<List<ExpenseClaim>> observable, List<ExpenseClaim> claims) {
		setListAdapter(new ExpenseClaimArrayAdapter(this, claims, userManager.getActiveUser()));
	}
}
