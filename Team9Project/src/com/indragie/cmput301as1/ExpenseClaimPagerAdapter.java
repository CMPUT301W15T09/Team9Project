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

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Pager adapter for the expense claims list.
 */
public class ExpenseClaimPagerAdapter extends FragmentStatePagerAdapter {
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Tab identifier for the owned claims tab.
	 */
	private static final int TAB_OWNED_CLAIMS = 0;
	
	/**
	 * Tab identifier for the reviewal claims tab.
	 */
	private static final int TAB_REVIEWAL_CLAIMS = 1;
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * The current context.
	 */
	private Context context;
	
	/**
	 * The active user.
	 */
	private User user;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link ExpenseClaimPagerAdapter}
	 * @param context The current context;
	 * @param fm The fragment manager.
	 * @param user The active user.
	 */
	public ExpenseClaimPagerAdapter(Context context, FragmentManager fm, User user) {
		super(fm);
		this.context = context;
		this.user = user;
	}
	
	//================================================================================
	// FragmentStatePagerAdapter
	//================================================================================

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = null;
		switch (i) {
		case TAB_OWNED_CLAIMS:
			fragment = new ExpenseClaimOwnedListFragment();
			break;
		case TAB_REVIEWAL_CLAIMS:
			fragment = new ExpenseClaimReviewalListFragment();
		}
		Bundle args = new Bundle();
		args.putSerializable(ExpenseClaimListFragment.BUNDLE_USER, user);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public int getCount() {
		return 2;
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
		Resources resources = context.getResources();
		switch (position) {
		case TAB_OWNED_CLAIMS:
			return resources.getString(R.string.tab_owned);
		case TAB_REVIEWAL_CLAIMS:
			return resources.getString(R.string.tab_reviewal);
		}
        return null;
    }
}
