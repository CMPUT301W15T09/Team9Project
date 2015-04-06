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

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * An activity that initializes tab fragments for displaying claims.
 */


public class ExpenseClaimListActivity extends FragmentActivity {

	private static final int ADD_EXPENSE_CLAIM_REQUEST = 1;
	private static final int EDIT_EXPENSE_CLAIM_REQUEST = 2;
	private static final int SORT_EXPENSE_CLAIM_REQUEST = 3;
	private static final int MANAGE_TAGS_REQUEST = 4;
	private static final int FILTER_TAGS_REQUEST = 5;
	

	//================================================================================
	// Properties
	//================================================================================

	/**
<<<<<<< HEAD
=======
	 * List model of expense claim.
	 */
	private ListModel<ExpenseClaim> listModel;
	
	/**
	 * List Model of filtered expense claim.
	 */
	private ListModel<ExpenseClaim> filteredListModel = new ListModel<ExpenseClaim>("filtered_List", this);
	
	/**
	 * List of tags to filter expense claims.
	 */
	private ArrayList<Tag> filteredTagsList = new ArrayList<Tag>();
	
	/**
>>>>>>> master
	 * Manages the user and associated preferences.
	 */
	private UserManager userManager;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		/* Creating claim Tab */
		Tab tab = actionBar.newTab()
				.setText(R.string.title_tab_claims)
				.setTabListener(new ClaimTabListener<ClaimTabFragment>(this, "Claims", ClaimTabFragment.class));
		actionBar.addTab(tab);

		/* Creating approval Tab */
		tab = actionBar.newTab()
				.setText(R.string.title_tab_approval)
				.setTabListener(new ClaimTabListener<ApprovalTabFragment>(this, "Approval", ApprovalTabFragment.class));
		actionBar.addTab(tab);;

	}
	
	
	

}
