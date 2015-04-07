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
package com.indragie.cmput301as1;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


/**
 * A fragment class for users own claims
 */
public class ClaimTabFragment extends ExpenseClaimTabFragment {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = getActivity();
		filteredListModel = new ListModel<ExpenseClaim>("filtered_List", activity);

		userManager = ((ExpenseClaimListActivity) activity).getUserManager();
		if (userManager.getActiveUser() == null) {
			promptForUserInformation();
		} else {
			loadData();
		}
		setHasOptionsMenu(true);


	}

	/**
	 * Loads the expense claim data to display in the {@link ListView}
	 */
	private void loadData() {
		// Access the application-wide session
		Session session = ((ExpenseClaimListActivity) activity).getSession();

		// Show the initial list of expense claims (persisted on disk)
		listModel = session.getOwnedClaims();
		listModel.addObserver(this);
		setListAdapter(new ExpenseClaimArrayAdapter(activity, listModel.getItems()));

		// Load the new list from the server
		final Context context = activity;
		session.loadOwnedClaims(new ElasticSearchAPIClient.APICallback<List<ExpenseClaim>>() {
			@Override
			public void onSuccess(Response response, final List<ExpenseClaim> model) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						listModel.replace(model);
					}
				});
			}

			@Override
			public void onFailure(Request request, Response response, IOException e) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(context, R.string.load_fail_error, Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}

	/**
	 * Prompts the user to enter their name.
	 */
	public void promptForUserInformation() {
		// http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog
		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		alert.setCancelable(false);
		alert.setTitle(R.string.user_alert_title);
		alert.setMessage(R.string.user_alert_message);

		final EditText input = new EditText(activity);
		alert.setView(input);

		alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String name = input.getText().toString();
				if (name == null || name.isEmpty()) {
					Toast.makeText(activity, R.string.user_alert_error, Toast.LENGTH_LONG).show();
				} else {
					// Device specific identifier
					String androidID = Secure.getString(activity.getContentResolver(), Secure.ANDROID_ID); 
					userManager.setActiveUser(new User(androidID, name));
					loadData();
				}
			}
		});
		alert.show();
	}


}