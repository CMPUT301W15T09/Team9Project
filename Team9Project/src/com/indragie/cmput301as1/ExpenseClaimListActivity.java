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

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * An activity that initializes tab fragments for displaying claims.
 */


public class ExpenseClaimListActivity extends FragmentActivity {

	//================================================================================
	// Properties
	//================================================================================

	/**
	 * Manages the user and associated preferences.
	 */
	protected UserManager userManager;


	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userManager = new UserManager(this);

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

	public UserManager getUserManager(){
		return userManager;
	}




}
