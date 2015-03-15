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

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * An activity that presents a list of expense claims.
 */
public class ExpenseClaimListActivity extends ListActivity implements TypedObserver<List<ExpenseClaim>> {
	//================================================================================
	// Constants
	//================================================================================
	
	private static final int ADD_EXPENSE_CLAIM_REQUEST = 1;
	private static final int EDIT_EXPENSE_CLAIM_REQUEST = 2;
	private static final String EXPENSE_CLAIM_FILENAME = "claims";

	//================================================================================
	// Properties
	//================================================================================
	
	private ExpenseClaimListModel listModel;
	private int longPressedItemIndex;
	private User user;

	//================================================================================
	// Activity Callbacks
	//================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		checkFirstRun();
		
		listModel = new ExpenseClaimListModel(EXPENSE_CLAIM_FILENAME, this);
		listModel.addObserver(this);
		setListAdapter(new ExpenseClaimArrayAdapter(this, listModel.getExpenseClaims()));
		
		final ActionMode.Callback longClickCallback = new ActionMode.Callback() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.action_delete:
					listModel.remove(longPressedItemIndex);
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
		};
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				longPressedItemIndex = position;
				startActionMode(longClickCallback);
				return true;
			}
		});
	}
	
	@Override
	public void onDestroy() {
		listModel.deleteObserver(this);
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;
		switch (requestCode) {
		case ADD_EXPENSE_CLAIM_REQUEST:
			onAddExpenseResult(data);
			break;
		case EDIT_EXPENSE_CLAIM_REQUEST:
			onEditExpenseResult(data);
			break;
		}
	}
	
	private void onAddExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM);
		listModel.add(claim);
	}
	
	private void onEditExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM);
		int position = data.getIntExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_POSITION, -1);
		listModel.set(position, claim);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.expense_claim_list, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_claim:
			startAddExpenseClaimActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void startAddExpenseClaimActivity() {
		Intent addIntent = new Intent(this, ExpenseClaimAddActivity.class);
		addIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_USER, user);
		startActivityForResult(addIntent, ADD_EXPENSE_CLAIM_REQUEST);
	}
	
	
	public void checkFirstRun() {
	    boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
	    if (isFirstRun){ 
	        
	        //http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog
	        AlertDialog.Builder alert = new AlertDialog.Builder(this);
	        alert.setCancelable(false);

	        alert.setTitle("Username");
	        alert.setMessage("Please enter your name:");

	        // Set an EditText view to get user input 
	        final EditText input = new EditText(this);
	        alert.setView(input);

	        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	          String value = input.getText().toString();
	          
	          if(value != ""){
	        	  getSharedPreferences("PREFERENCE", MODE_PRIVATE)
	        	  .edit()
	        	  .putString("name", value)
	        	  .apply();
					  
	        	  getSharedPreferences("PREFERENCE", MODE_PRIVATE)
	        	  .edit()
	        	  //setup as default ID will change later
	        	  .putInt("id", 1)
	        	  .apply();
					  
	        	  getSharedPreferences("PREFERENCE", MODE_PRIVATE)
	        	  .edit()
	        	  .putBoolean("isFirstRun", false)
	        	  .apply();
				  
				
	        	  int id = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getInt("id", -1);
	        	  user = new User(value,id);
	          }
	          else{
	        	  checkFirstRun();
		          Toast.makeText(getApplicationContext(), "You must enter a username", Toast.LENGTH_LONG).show(); 
	          }
	          
	          }
	        });

	        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int whichButton) {
	            checkFirstRun();
	            Toast.makeText(getApplicationContext(), "You must enter a username", Toast.LENGTH_LONG).show();
	          }
	        });

	        alert.show();
	        

	        
	    }
	}

	//================================================================================
	// ListView Callbacks
	//================================================================================
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		startEditExpenseClaimActivity(position);
	}
	
	private void startEditExpenseClaimActivity(int position) {
		Intent editIntent = new Intent(this, ExpenseClaimDetailActivity.class);
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM, listModel.getExpenseClaims().get(position));
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_POSITION, position);
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_USER, user);
		startActivityForResult(editIntent, EDIT_EXPENSE_CLAIM_REQUEST);
	}

	//================================================================================
	// TypedObserver
	//================================================================================
	
	@Override
	public void update(TypedObservable<List<ExpenseClaim>> o, List<ExpenseClaim> claims) {
		setListAdapter(new ExpenseClaimArrayAdapter(this, claims));
	}
}
