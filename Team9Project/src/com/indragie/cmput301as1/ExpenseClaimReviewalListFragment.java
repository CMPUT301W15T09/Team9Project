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
import java.util.List;

import android.app.Activity;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Fragment that shows a list of expense claims for reviewal by the user.
 */
public class ExpenseClaimReviewalListFragment extends ExpenseClaimListFragment {

	/* (non-Javadoc)
	 * @see com.indragie.cmput301as1.ExpenseClaimListFragment#setupController()
	 */
	@Override
	protected void setupController() {
		final Activity activity = getActivity();
		Session session = Session.getSharedSession();
		ListModel<ExpenseClaim> listModel = session.getReviewalClaims();
		setController(new ExpenseClaimListController(getActivity(), listModel));
		setListAdapter(new ExpenseClaimArrayAdapter(getActivity(), listModel.getItems(), getUser()));
		
		session.loadReviewalClaims(new ElasticSearchAPIClient.APICallback<List<ExpenseClaim>>() {
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
}
