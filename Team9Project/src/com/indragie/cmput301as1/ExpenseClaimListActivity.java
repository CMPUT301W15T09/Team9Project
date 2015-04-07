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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * An activity that presents a list of expense claims.
 */
public class ExpenseClaimListActivity extends ListActivity {
	//================================================================================
	// Constants
	//================================================================================
	
	private static final int USER_SETTINGS_REQUEST = 6;
	
	//================================================================================
	// Properties
	//================================================================================
	
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
	 * Loads the expense claim data.
	 */
	private void loadData() {
		Session session = new Session(this, userManager.getActiveUser());
		Session.setSharedSession(session);
	}
	
	/**
	 * Sets the active user of the application.
	 * @param data The intent to get the user from.
	 */
	private void onUserSettingsResult(Intent data) {
		User user = (User)data.getSerializableExtra(UserSettingsActivity.EXTRA_USER);
		userManager.setActiveUser(user);
		loadData();
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
