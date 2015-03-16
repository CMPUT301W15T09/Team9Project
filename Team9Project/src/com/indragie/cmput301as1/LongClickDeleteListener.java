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
	}
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link LongClicKDeleteListener}
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
}
