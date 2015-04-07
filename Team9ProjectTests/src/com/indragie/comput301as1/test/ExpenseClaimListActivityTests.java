/* 
 * Copyright (C) 2015 Nic Carroll
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
package com.indragie.comput301as1.test;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.test.UiThreadTest;

import com.indragie.cmput301as1.ApprovalTabFragment;
import com.indragie.cmput301as1.ClaimTabFragment;
import com.indragie.cmput301as1.ExpenseClaimListActivity;
import com.indragie.cmput301as1.R;


public class ExpenseClaimListActivityTests
extends android.test.ActivityInstrumentationTestCase2<ExpenseClaimListActivity>{

	private Activity activity;
	private ActionBar actionBar;

	public ExpenseClaimListActivityTests() {
		super(ExpenseClaimListActivity.class);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		this.activity = getActivity();
		this.actionBar = this.activity.getActionBar();
	}


	//http://stackoverflow.com/questions/19472073/android-actionbar-unit-test-using-activityinstrumentationtestcase2/21989462#21989462
	@UiThreadTest
	public void testNavigationBar() {
		assertEquals(ActionBar.NAVIGATION_MODE_TABS, actionBar.getNavigationMode());
		assertEquals(2, actionBar.getNavigationItemCount());

		Tab tab0 = actionBar.getTabAt(0);
		assertNotNull(tab0);
		assertNotNull(tab0.getText());
		assertEquals(activity.getBaseContext().getString(R.string.title_tab_claims), tab0.getText());

		Tab tab1 = actionBar.getTabAt(1);
		assertNotNull(tab1);
		assertNotNull(tab1.getText());
		assertEquals(activity.getBaseContext().getString(R.string.title_tab_approval),
				tab1.getText());

		FragmentManager fragmentManager = activity.getFragmentManager();

		actionBar.selectTab(tab0);
		assertEquals(0, actionBar.getSelectedNavigationIndex());
		Fragment currentFragment = fragmentManager.findFragmentByTag("Claims");
		assertNotNull(currentFragment);
		assertTrue(currentFragment instanceof ClaimTabFragment);

		actionBar.selectTab(tab1);
		fragmentManager.executePendingTransactions();
		assertEquals(1, actionBar.getSelectedNavigationIndex());
		currentFragment = fragmentManager.findFragmentByTag("Approval");
		assertNotNull(currentFragment);
		assertTrue(currentFragment instanceof ApprovalTabFragment);
	}
}