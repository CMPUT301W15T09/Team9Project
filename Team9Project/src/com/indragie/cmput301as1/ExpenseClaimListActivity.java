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

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.provider.Settings.Secure;

/**
 * An activity that presents a list of expense claims.
 */
public class ExpenseClaimListActivity extends ListActivity implements TypedObserver<CollectionMutation<ExpenseClaim>> {
	//================================================================================
	// Constants
	//================================================================================

	private static final int ADD_EXPENSE_CLAIM_REQUEST = 1;
	private static final int EDIT_EXPENSE_CLAIM_REQUEST = 2;
	private static final int SORT_EXPENSE_CLAIM_REQUEST = 3;
	private static final int MANAGE_TAGS_REQUEST = 4;
	
	//================================================================================
	// Properties
	//================================================================================

	/**
	 * List model of expense claim.
	 */
	private ListModel<ExpenseClaim> listModel;
	
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
			promptForUserInformation();
		} else {
			loadData();
		}
		
		getListView().setOnItemLongClickListener(new LongClickDeleteListener(this, new LongClickDeleteListener.OnDeleteListener() {
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
		listModel = session.getOwnedClaims();
		listModel.addObserver(this);
		setListAdapter(new ExpenseClaimArrayAdapter(this, listModel.getItems()));

		// Load the new list from the server
		final Context context = this;
		session.loadOwnedClaims(new ElasticSearchAPIClient.APICallback<List<ExpenseClaim>>() {
			@Override
			public void onSuccess(Response response, List<ExpenseClaim> model) {}

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
		listModel.deleteObserver(this);
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;
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
		}
	}
	
	/**
	 * Changes the sorting mode based on a comparator chosen by {@link ExpenseClaimSortActivity}
	 * @param data The intent to get the comparator from.
	 */
	@SuppressWarnings("unchecked")
	private void onSortExpenseResult(Intent data) {
		Comparator<ExpenseClaim> comparator = (Comparator<ExpenseClaim>)data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT);
		listModel.setComparator(comparator);
	}

	/**
	 * Adds a expense claim to list model from a intent.
	 * @param data The intent to get the expense claim from.
	 */
	private void onAddExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM);
		listModel.add(claim);
	}
	
	/**
	 * Sets a expense claim at a specified position in the list model from a intent.
	 * @param data The intent to get the expense claim from.
	 */
	private void onEditExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM);
		int position = data.getIntExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_INDEX, -1);
		listModel.set(position, claim);
	}
	
	/**
	 * Sets the list used in ListModel to the returned list of expense claims from the intent. 
	 * @param data The intent to get the list of expense claims from. 
	 */
	@SuppressWarnings("unchecked")
	private void onManageTagsResult(Intent data) {
		ArrayList<ManageTagsActivity.TagMutation> mutations =
				(ArrayList<ManageTagsActivity.TagMutation>)data.getSerializableExtra(ManageTagsActivity.EXTRA_TAG_MUTATIONS);
		SparseArray<ExpenseClaim> modifiedClaims = new SparseArray<ExpenseClaim>();
		
		for (ManageTagsActivity.TagMutation mutation : mutations) {
			Tag tag = mutation.getOldTag();
			int i = 0;
			for (ExpenseClaim claim : listModel.getItems()) {
				if (!claim.hasTag(tag)) continue;
				
				switch (mutation.getMutationType()) {
				case DELETE:
					claim.removeTag(tag);
					break;
				case EDIT:
					int tagIndex = claim.getTags().indexOf(tag);
					claim.setTag(tagIndex, mutation.getNewTag());
					break;
				}
				
				modifiedClaims.append(i, claim);
				i++;
			}
		}
		
		for (int i = 0; i < modifiedClaims.size(); i++) {
			listModel.set(modifiedClaims.keyAt(i), modifiedClaims.valueAt(i));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.expense_claim_list, menu);
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
	 * Prompts the user to enter their name.
	 */
	public void promptForUserInformation() {
		// http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setCancelable(false);
		alert.setTitle(R.string.user_alert_title);
		alert.setMessage(R.string.user_alert_message);
		
		final EditText input = new EditText(this);
		alert.setView(input);
		
		alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String name = input.getText().toString();
				if (name == null || name.isEmpty()) {
					Toast.makeText(getApplicationContext(), R.string.user_alert_error, Toast.LENGTH_LONG).show();
				} else {
					// Device specific identifier
					String androidID = Secure.getString(getContentResolver(), Secure.ANDROID_ID); 
					userManager.setActiveUser(new User(androidID, name));
					loadData();
				}
			}
		});
		alert.show();
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
				listModel.remove(index);
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
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM, listModel.getItems().get(position));
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_INDEX, position);
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_USER, userManager.getActiveUser());
		startActivityForResult(editIntent, EDIT_EXPENSE_CLAIM_REQUEST);
	}

	//================================================================================
	// TypedObserver
	//================================================================================

	@Override
	public void update(TypedObservable<CollectionMutation<ExpenseClaim>> observable, CollectionMutation<ExpenseClaim> mutation) {
		setListAdapter(new ExpenseClaimArrayAdapter(this, listModel.getItems()));
	}
}
