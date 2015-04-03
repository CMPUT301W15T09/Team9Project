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

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * An activity that initializes tab fragments for displaying claims.
 */
public class ExpenseClaimListActivity extends FragmentActivity implements TypedObserver<List<ExpenseClaim>> {
	//================================================================================
	// Constants
	//================================================================================
	
	private static final String PREFERENCE = "PREFERENCE";

	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * Active user.
	 */
	private User user;

	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		checkFirstRun();
		
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.setDisplayShowTitleEnabled(true);

		/* Creating ANDROID Tab */
		Tab tab = actionBar.newTab()
			.setText("Claims")
			.setTabListener(new ClaimTabListener<ClaimTabFragment>(this, "Claims", ClaimTabFragment.class));

		actionBar.addTab(tab);

		/* Creating APPLE Tab */
		tab = actionBar.newTab()
			.setText("Approval")
			.setTabListener(new ClaimTabListener<ApprovalTabFragment>(this, "Approval", ApprovalTabFragment.class));

		actionBar.addTab(tab);;
	}
	
	private void setUserFromPreferences() {
		SharedPreferences prefs = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
		user = new User(prefs.getString("name", "USER DOES NOT EXIST"), prefs.getInt("id", -1));
	}


	public void checkFirstRun() {
		boolean isFirstRun = getSharedPreferences(PREFERENCE, MODE_PRIVATE).getBoolean("isFirstRun", true);
		if (isFirstRun){ 
			// http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setCancelable(false);

			alert.setTitle("Username");
			alert.setMessage("Please enter your name:");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String value = input.getText().toString();

					if(value != null && !value.isEmpty()){
						SharedPreferences.Editor editor = getSharedPreferences(PREFERENCE, MODE_PRIVATE).edit();
						editor.putString("name", value);
						editor.putInt("id", 1);
						editor.putBoolean("isFirstRun", false);
						editor.apply();
						setUserFromPreferences();
					}
					else{
						checkFirstRun();
						Toast.makeText(getApplicationContext(), "You must enter a username", Toast.LENGTH_LONG).show(); 
					}

				}
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					checkFirstRun();
					Toast.makeText(getApplicationContext(), "You must enter a username", Toast.LENGTH_LONG).show();
				}
			});

			alert.show();
		}
	}
	
	//================================================================================
	// TypedObserver
	//================================================================================

	@Override
	public void update(TypedObservable<List<ExpenseClaim>> o, List<ExpenseClaim> claims) {
	}

}
