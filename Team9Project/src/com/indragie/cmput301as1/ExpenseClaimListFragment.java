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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

/**
 * Fragment for displaying a list of expense claims.
 */
public abstract class ExpenseClaimListFragment extends ListFragment implements TypedObserver<List<ExpenseClaim>> {
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Bundle key for the active user.
	 */
	private static final String BUNDLE_USER = "com.indragie.cmput301as1.BUNDLE_USER";
	
	/**
	 * Request code for starting {@link ExpenseClaimDetailActivity}
	 */
	private static final int EDIT_EXPENSE_CLAIM_REQUEST = 1;
	
	/**
	 * Request code for starting {@link ExpenseClaimSortActivity}
	 */
	private static final int SORT_EXPENSE_CLAIM_REQUEST = 2;
	
	/**
	 * Request code for starting {@link ManageTagsActivity}
	 */
	private static final int MANAGE_TAGS_REQUEST = 3;
	
	/**
	 * Request code for starting {@link FilterTagsActivity}
	 */
	private static final int FILTER_TAGS_REQUEST = 4;
	
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * The active user.
	 */
	private User user;
	
	/**
	 * The controller for this fragment.
	 */
	private ExpenseClaimListController controller;
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return The active user.
	 */ 
	protected User getUser() {
		return user;
	}
	
	/**
	 * @return The controller for this fragment.
	 */
	protected ExpenseClaimListController getController() {
		return controller;
	}
	
	/**
	 * Sets the controller for this fragment.
	 * @param controller The controller for this fragment.
	 */
	public void setController(ExpenseClaimListController controller) {
		this.controller = controller;
	}
	
	//================================================================================
	// Fragment Callbacks
	//================================================================================
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user = (User)savedInstanceState.getSerializable(BUNDLE_USER);
		setHasOptionsMenu(true);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupController();
	}
	
	/**
	 * Subclasses override this to set up their controller.
	 */
	protected abstract void setupController();
	
	@Override
	public void onDestroy() {
		controller.deleteObserver(this);
		super.onDestroy();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		 inflater.inflate(R.menu.expense_claim_list_base, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_sort_claim:
			startSortExpenseClaimActivity();
			return true;
		case R.id.action_manage_tags:
			startManageTagsActivity();
			return true;
		case R.id.action_filter_tags:
			startFilterTagsActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Starts the {@link ExpenseClaimSortActivity}
	 */
	private void startSortExpenseClaimActivity() {
		Intent intent = new Intent(getActivity(), ExpenseClaimSortActivity.class);
		startActivityForResult(intent, SORT_EXPENSE_CLAIM_REQUEST);
	}
		
	/**
	 * Starts the {@link ManageTagsActivity}
	 */
	private void startManageTagsActivity() {
		Intent manageTagsIntent = new Intent(getActivity(), ManageTagsActivity.class);
		startActivityForResult(manageTagsIntent, MANAGE_TAGS_REQUEST);
	}
	
	/**
	 * Starts the {@link FilterTagsActivity}
	 */
	private void startFilterTagsActivity() {
		Intent filterTagsIntent = new Intent(getActivity(), FilterTagsActivity.class);
		filterTagsIntent.putExtra(FilterTagsActivity.TAG_TO_FILTER, new ArrayList<Tag>(controller.getFilterTags()));
		startActivityForResult(filterTagsIntent, FILTER_TAGS_REQUEST);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
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
			if (resultCode == Activity.RESULT_OK) {
				onFilterTagsRequest(data);
			} else {
				controller.removeFilter();
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
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
	 * Changes the sorting mode based on a comparator chosen by {@link ExpenseClaimSortActivity}
	 * @param data The intent to get the comparator from.
	 */
	@SuppressWarnings("unchecked")
	private void onSortExpenseResult(Intent data) {
		Comparator<ExpenseClaim> comparator = (Comparator<ExpenseClaim>)data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT);
		controller.sort(comparator);
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
		Intent editIntent = new Intent(getActivity(), ExpenseClaimDetailActivity.class);
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM, controller.get(position));
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_INDEX, position);
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_USER, user);
		startActivityForResult(editIntent, EDIT_EXPENSE_CLAIM_REQUEST);
	}

	//================================================================================
	// TypedObserver<List<ExpenseClaim>>
	//================================================================================

	@Override
	public void update(TypedObservable<List<ExpenseClaim>> observable, List<ExpenseClaim> claims) {
		setListAdapter(new ExpenseClaimArrayAdapter(getActivity(), claims, user));
	}
}
