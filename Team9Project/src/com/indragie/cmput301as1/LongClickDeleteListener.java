/* 
 * Copyright (C) 2015 Indragie Karunaratne, Andrew Zhong
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

import android.app.Activity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

/**
 * Long click listener for use with a {@link ListView} that displays the contextual action 
 * bar and shows a delete button.
 */
public class LongClickDeleteListener implements AdapterView.OnItemLongClickListener {
	//================================================================================
	// Properties
	//================================================================================
	/**
	 * The position of the item that was long clicked.
	 */
	private int longClickedPosition;
	
	/**
	 * The parent activity.
	 */
	private Activity activity;
	
	/**
	 * Listener that is notified when a long clicked item is deleted.
	 */
	private OnDeleteListener onDeleteListener;
	
	//================================================================================
	// Interfaces
	//================================================================================
	
	/**
	 * Interface for an object that is notified when a long clicked item is deleted.
	 */
	public interface OnDeleteListener {
		/**
		 * Called when the delete button is clicked.
		 * @param position The position of the item to be deleted.
		 */
		public void onDelete(int position);
		
		/**
		 * Called when the listener needs to determine whether to show
		 * the contextual action menu for an item.
		 * @param position The position of the item to be deleted.
		 * @return Whether the item should be deleted.
		 */
		public boolean shouldDelete(int position);
	}
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link LongClickDeleteListener}
	 * @param activity The parent activity.
	 * @param onDeleteListener Listener that is notified when a long clicked item is deleted.
	 */
	public LongClickDeleteListener(Activity activity, OnDeleteListener onDeleteListener) {
		this.activity = activity;
		this.onDeleteListener = onDeleteListener;
	}
	
	//================================================================================
	// OnItemLongClickListener
	//================================================================================
	
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if (onDeleteListener.shouldDelete(position)) {
			longClickedPosition = position;
			activity.startActionMode(new ActionMode.Callback() {
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
					case R.id.action_delete:
						onDeleteListener.onDelete(longClickedPosition);
						mode.finish();
						return true;
					default:
						return false;
					}
				}
	
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					mode.getMenuInflater().inflate(R.menu.contextual_delete, menu);
					return true;
				}
	
				@Override
				public void onDestroyActionMode(ActionMode mode) {}
	
				@Override
				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}
			});
			return true;
		}
		return true;
	}
	
}
