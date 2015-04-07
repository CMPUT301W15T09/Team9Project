/* 
 * Copyright (C) 2015 Steven Chang
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

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class CommentAddActivity extends Activity {

	//================================================================================
	// Constants
	//================================================================================
	public static final String COMMENT_TO_ADD = "com.indragie.cmput301as1.COMMENT";
	public static final String COMMENT_APPROVER = "com.indragie.cmput301as1.EXTRA_EXPENSE_CLAIM_USER";

	//================================================================================
	// Properties
	//================================================================================
	/**
	 * The comment of the comment.
	 */
	private EditText commentField;
	/**
	 * The approver.
	 */
	private User approver;
	/**
	 * The way to choose a status.
	 */
	private Spinner statusSpinner;
	
	//================================================================================
	// Activity Callbacks
	//================================================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_add);

		Intent intent = getIntent();
		approver = (User)intent.getSerializableExtra(COMMENT_APPROVER);
		
		statusSpinner = (Spinner)findViewById(R.id.spinner_status);
		SpinnerUtils.configureSpinner(this, statusSpinner, R.array.status_array);
		
		ActionBarUtils.showCancelDoneActionBar(
				this,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						setResult(RESULT_CANCELED, new Intent());
						finish();
					}
				},
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						setResult(RESULT_OK, constructResultIntent());
						finish();
					}
				}
			);

		commentField = (EditText)findViewById(R.id.et_comment);
	}
	
	/**
	 * Creates a new comment to put into another activity.
	 * @return The intent with the new comment.
	 */
	private Intent constructResultIntent() {
		Comment.Status status;
		if (statusSpinner.getSelectedItem().toString() == "Returned") {
			status = Comment.Status.RETURNED;
		} else {
			status = Comment.Status.APPROVED;
		}
		
		
		Comment comment= new Comment( 
			approver,
			commentField.getText().toString(),
			new Date(),
			status
		);
		
		Intent intent = new Intent();
		intent.putExtra(COMMENT_TO_ADD, comment);
		return intent;
	}
}