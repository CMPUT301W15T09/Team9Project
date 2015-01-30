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
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Activity that displays Cancel and Done buttons in its action bar.
 */
public abstract class EditingActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Based on http://stackoverflow.com/a/15104281/153112
		View actionBarButtons = getLayoutInflater().inflate(R.layout.activity_editing_actionbar, new LinearLayout(this), false);
		View cancelButton = actionBarButtons.findViewById(R.id.action_cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onCancel();
			}
		});
		View doneButton = actionBarButtons.findViewById(R.id.action_done);
		doneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onDone();
			}
		});

		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(actionBarButtons);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	}

	/**
	 * Called when the Cancel button is clicked.
	 */
	protected abstract void onCancel();

	/**
	 * Called when the Done button is clicked.
	 */
	protected abstract void onDone();
}
