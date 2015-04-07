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

import java.util.List;
import java.io.IOException;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Fragment that displays expense claims owned by the active user.
 */
public class ExpenseClaimOwnedListFragment extends ExpenseClaimListFragment {
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Request code used to start {@link ExpenseClaimAddActivity}
	 */
	private static final int ADD_EXPENSE_CLAIM_REQUEST = 1;

	//================================================================================
	// Fragment Callbacks
	//================================================================================
	
	/* (non-Javadoc)
	 * @see com.indragie.cmput301as1.ExpenseClaimListFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		getListView().setOnItemLongClickListener(
				new LongClickDeleteListener(getActivity(), new LongClickDeleteListener.OnDeleteListener() {
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
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.expense_claim_list_owned, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_claim:
			startAddExpenseClaimActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Starts the {@link ExpenseClaimAddActivity}
	 */
	private void startAddExpenseClaimActivity() {
		Intent addIntent = new Intent(getActivity(), ExpenseClaimAddActivity.class);
		addIntent.putExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM_USER, getUser());
		startActivityForResult(addIntent, ADD_EXPENSE_CLAIM_REQUEST);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_EXPENSE_CLAIM_REQUEST:
			onAddExpenseResult(data);
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}
		
	/**
	 * Adds a expense claim to list model from a intent.
	 * Displays the filteredListModel instead if there are filtered tags.
	 * @param data The intent to get the expense claim from.
	 */
	private void onAddExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM);
		getController().add(claim);
	}
	
	//================================================================================
	// ExpenseClaimListFragment
	//================================================================================
	
	/* (non-Javadoc)
	 * @see com.indragie.cmput301as1.ExpenseClaimListFragment#setupController()
	 */
	@Override
	protected void setupController() {
		final Activity activity = getActivity();
		Session session = Session.getSharedSession();
		ListModel<ExpenseClaim> listModel = session.getOwnedClaims();
		setController(new ExpenseClaimListController(getActivity(), listModel));
		setListAdapter(new ExpenseClaimArrayAdapter(getActivity(), listModel.getItems(), getUser()));
		
		session.loadOwnedClaims(new ElasticSearchAPIClient.APICallback<List<ExpenseClaim>>() {
			@Override
			public void onSuccess(Response response, final List<ExpenseClaim> claims) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getController().replace(claims);
					}
				});
			}

			@Override
			public void onFailure(Request request, Response response, IOException e) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(activity, R.string.load_fail_error, Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}
	
	//================================================================================
	// Private
	//================================================================================
	
	/**
	 * Prompts the user for confirmation in response to deleting an expense claim.
	 * @param index The index of the expense claim to remove.
	 */
	public void showDeleteAlertDialog(final int index) {
		AlertDialog.Builder openDialog = new AlertDialog.Builder(getActivity());
		openDialog.setTitle(R.string.action_delete_claim_confirm);
		
		openDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getController().remove(index);
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
}
