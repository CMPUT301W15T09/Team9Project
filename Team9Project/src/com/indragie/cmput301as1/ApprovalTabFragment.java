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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A fragment class for other users claims
 */
public class ApprovalTabFragment extends ExpenseClaimTabFragment {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		activity = getActivity();
		filteredListModel = new ListModel<ExpenseClaim>("filtered_List", activity);
		
		userManager = new UserManager(activity);
		if (userManager.getActiveUser() == null) {
			promptForUserInformation();
		} else {
			loadData(1);
		}
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

//		userManager = new UserManager(activity);
//		if (userManager.getActiveUser() == null) {
//			promptForUserInformation();
//		} else {
//			loadData();
//		}
		
		setHasOptionsMenu(true);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	
}