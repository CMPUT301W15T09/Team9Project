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

import java.text.DateFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indragie.cmput301as1.ExpenseClaimDetailController.DetailItem;
import com.indragie.cmput301as1.ListSection.ViewConfigurator;

public class CommentViewConfigurator implements ViewConfigurator<ExpenseClaimDetailController.DetailItem> {
	//================================================================================
	// Constants
	//================================================================================
	
	private static final int COMMENT_VIEW_CODE = 4;
	
	//================================================================================
	// ViewConfigurator
	//================================================================================
	@Override
	public int getViewTypeCode() {
		return COMMENT_VIEW_CODE;
	}

	@Override
	public View createView(Context context, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.comment_list_row, parent, false);
	}

	@Override
	public void configureView(Context context, View view, DetailItem object) {
		Comment comment = (Comment)object.getModel();
		
		//TextView approverTextView = (TextView)view.findViewById(R.id.tv_name);
		//approverTextView.setText(comment.getApprover().getName());
		
		TextView commentTextView = (TextView)view.findViewById(R.id.tv_comment);
		commentTextView.setText(comment.getComment());
		
		TextView dateTextView = (TextView)view.findViewById(R.id.tv_date);
		dateTextView.setText(DateFormat.getDateInstance().format(comment.getCreationDate()));
		
		TextView categoryTextView = (TextView)view.findViewById(R.id.tv_status);
		categoryTextView.setBackground(context.getResources().getDrawable(R.drawable.bg_rounded_grey));
	}

}
