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
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Utility functions for working with {@link ActionBar}
 */
public class ActionBarUtils {
	/**
	 * Displays a custom action bar that shows "Cancel" and "Done" buttons.
	 * @param activity The activity in which to show the custom action bar.
	 * @param cancelListener Listener to be notified when the Cancel button is clicked.
	 * @param doneListener Listener to be notified when the Done button is clicked.
	 */
	public static void showCancelDoneActionBar(Activity activity, View.OnClickListener cancelListener, View.OnClickListener doneListener) {
		// Based on http://stackoverflow.com/a/15104281/153112
		View actionBarButtons = LayoutInflater.from(activity).inflate(R.layout.activity_editing_actionbar, new LinearLayout(activity), false);
		View cancelButton = actionBarButtons.findViewById(R.id.action_cancel);
		cancelButton.setOnClickListener(cancelListener);
		View doneButton = actionBarButtons.findViewById(R.id.action_done);
		doneButton.setOnClickListener(doneListener);

		ActionBar actionBar = activity.getActionBar();
		actionBar.setCustomView(actionBarButtons);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	}
}
