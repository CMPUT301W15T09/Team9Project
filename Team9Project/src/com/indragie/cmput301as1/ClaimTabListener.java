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

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

//http://wptrafficanalyzer.in/blog/adding-navigation-tabs-containing-listview-to-action-bar-in-android/

/**
 * Listener to enable switching of tabs
 */
public class ClaimTabListener<T extends Fragment> implements TabListener {
	private Fragment fragment;
	private final Activity activity;
	private final String tag;
	private final Class<T> clz;


	public ClaimTabListener(Activity activity, String tag, Class<T> clz){
		this.activity = activity;
		this.tag = tag;
		this.clz = clz;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if(fragment==null){
			fragment = Fragment.instantiate(activity, clz.getName());
			ft.add(android.R.id.content,fragment, tag);
		}else{
			ft.attach(fragment);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if(fragment!=null)
			ft.detach(fragment);
	}
}