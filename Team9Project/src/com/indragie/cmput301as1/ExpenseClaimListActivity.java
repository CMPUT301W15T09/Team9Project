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
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

/**
 * An activity that presents a list of expense claims.
 */
public class ExpenseClaimListActivity extends FragmentActivity {
	//================================================================================
	// Constants
	//================================================================================

	private static final int USER_SETTINGS_REQUEST = 20;

	//================================================================================
	// Properties
	//================================================================================

	/**
	 * Manages the user and associated preferences.
	 */
	private UserManager userManager;

	/**
	 * Adapter used to show fragments in the {@link ViewPager}
	 */
	private ExpenseClaimPagerAdapter pagerAdapter;

	/**
	 * Pager view for the owned/reviewal claims tabs.
	 */
	private ViewPager pager;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_expense_claim_list);
		pager = (ViewPager)findViewById(R.id.pager);

		userManager = new UserManager(this);
		if (userManager.getActiveUser() == null) {
			startUserSettingsActivity();
		} else {
			setupFragments();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case USER_SETTINGS_REQUEST:
			onUserSettingsResult(data);
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.expense_claim_list_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_user_settings:
			startUserSettingsActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Set up fragments to display expense claim data.
	 */
	private void setupFragments() {
		User user = userManager.getActiveUser();
		Session session = new Session(this, user);
		Session.setSharedSession(session);

		pagerAdapter = new ExpenseClaimPagerAdapter(this, getSupportFragmentManager(), user);
		pager.setAdapter(pagerAdapter);

		// From http://developer.android.com/training/implementing-navigation/lateral.html
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				pager.setCurrentItem(tab.getPosition());
			}
			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}
			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}
		};

		pager.setOnPageChangeListener(
			new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					actionBar.setSelectedNavigationItem(position);
				}
			});

		Tab ownedTab = actionBar.newTab()
			.setText(R.string.tab_owned)
			.setTabListener(tabListener);
		Tab reviewalTab = actionBar.newTab()
			.setText(R.string.tab_reviewal)
			.setTabListener(tabListener);

		actionBar.addTab(ownedTab);
		actionBar.addTab(reviewalTab);
	}

	/**
	 * Sets the active user of the application.
	 * @param data The intent to get the user from.
	 */
	private void onUserSettingsResult(Intent data) {
		User user = (User)data.getSerializableExtra(UserSettingsActivity.EXTRA_USER);
		boolean noExistingUser = (userManager.getActiveUser() == null);
		userManager.setActiveUser(user);
		if (noExistingUser) {
			setupFragments();
		}
	}

	/**
	 * Starts the {@link UserSettingsActivity}
	 */
	private void startUserSettingsActivity() {
		Intent userSettingsIntent = new Intent(this, UserSettingsActivity.class);
		userSettingsIntent.putExtra(UserSettingsActivity.EXTRA_USER, userManager.getActiveUser());
		startActivityForResult(userSettingsIntent, USER_SETTINGS_REQUEST);
	}
}
