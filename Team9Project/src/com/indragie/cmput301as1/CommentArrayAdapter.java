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
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter for showing an array of {@link Comment} objects in a {@link ListView}
 * Based on https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */
public class CommentArrayAdapter extends ArrayAdapter<Comment> {
	public CommentArrayAdapter(Context context, List<Comment> comments) {
		super(context, R.layout.comment_list_row, comments);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Comment comment = getItem(position);
		Resources resources = getContext().getResources();
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_list_row, parent, false);
		}

		TextView commentName = (TextView)convertView.findViewById(R.id.tv_name);
		commentName.setText("Approver: " + comment.getApprover().getName());
		
		TextView commentTextView = (TextView)convertView.findViewById(R.id.tv_comment);
		commentTextView.setText(comment.getComment());
		
		TextView dateTextView = (TextView)convertView.findViewById(R.id.tv_date);
		dateTextView.setText(DateFormat.getDateInstance().format(comment.getCreationDate()));
		

		TextView statusTextView = (TextView)convertView.findViewById(R.id.tv_status);
		statusTextView.setText(comment.getStatusString(resources));
		statusTextView.setBackground(drawableForStatus(comment.getStatus(), resources));
		
		return convertView;
	}
	/**
	 * Draws the status of the comment
	 * @param status The status.
	 * @param resources The application's resources.
	 * @return The drawable resource.
	 */
	private Drawable drawableForStatus(Comment.Status status, Resources resources) {
		switch (status) {
		case APPROVED:
			return resources.getDrawable(R.drawable.bg_rounded_green);
		case RETURNED:
			return resources.getDrawable(R.drawable.bg_rounded_red);
		default:
			return resources.getDrawable(R.drawable.bg_rounded_red);
		}
	}
}
