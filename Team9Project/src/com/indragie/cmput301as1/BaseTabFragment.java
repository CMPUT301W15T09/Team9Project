package com.indragie.cmput301as1;
 
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

 

public class BaseTabFragment extends ListFragment implements TypedObserver<CollectionMutation<ExpenseClaim>> {
 
    protected static final String EXPENSE_CLAIM_FILENAME = "claims";
    protected static final String EXPENSE_APPROVAL_FILENAME = "approval";
    private static final String PREFERENCE = "PREFERENCE";
	private static final int ADD_EXPENSE_CLAIM_REQUEST = 1;
	private static final int EDIT_EXPENSE_CLAIM_REQUEST = 2;
	private static final int SORT_EXPENSE_CLAIM_REQUEST = 3;
    
    protected ListModel<ExpenseClaim> listModel;
    
    ListView myButton;
    /**
	 * Index of a item that is long pressed.
	 */
	private int longPressedItemIndex;
	
	/**
	 * Active user.
	 */
	private User user;

	/** An array of items to display in ArrayList */
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	setUserFromPreferences();
    	setHasOptionsMenu(true);
        
    	
    	load();
		
    	/** Creating array adapter to set data in listview */
        ExpenseClaimArrayAdapter adapter = new ExpenseClaimArrayAdapter(getActivity(), listModel.getItems());
 
        /** Setting the array adapter to the listview */
        setListAdapter(adapter);
        
        
		
 
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);
    	
    	
    	final ActionMode.Callback longClickCallback = new ActionMode.Callback() {
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
				case R.id.action_delete:
					startDeleteAlertDialog();
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
    	getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				longPressedItemIndex = position;
				getActivity().startActionMode(longClickCallback);
				return true;
			}
		});
    }
    
    @Override
	public void onDestroy() {
		listModel.deleteObserver(this);
		super.onDestroy();
	}
    
    @SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != getActivity().RESULT_OK) return;
		switch (requestCode) {
		case ADD_EXPENSE_CLAIM_REQUEST:
			onAddExpenseResult(data);
			break;
		case EDIT_EXPENSE_CLAIM_REQUEST:
			onEditExpenseResult(data);
			break;
		case SORT_EXPENSE_CLAIM_REQUEST:
			onSortExpenseResult(data);
			break;
		}
	}
    
    /**
	 * Changes the sorting mode based on a comparator chosen by {@link ExpenseClaimSortActivity}
	 * @param data The intent to get the comparator from.
	 */
	@SuppressWarnings("unchecked")
	private void onSortExpenseResult(Intent data) {
		Comparator<ExpenseClaim> comparator = (Comparator<ExpenseClaim>)data.getSerializableExtra(ExpenseClaimSortActivity.EXPENSE_CLAIM_SORT);
		listModel.setComparator(comparator);
		refresh();
	}

	/**
	 * Adds a expense claim to list model from a intent.
	 * @param data The intent to get the expense claim from.
	 */
	private void onAddExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM);
		listModel.add(claim);
		refresh();
	}
	
	/**
	 * Sets a expense claim at a specified position in the list model from a intent.
	 * @param data The intent to get the expense claim from.
	 */
	private void onEditExpenseResult(Intent data) {
		ExpenseClaim claim = (ExpenseClaim)data.getSerializableExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM);
		int position = data.getIntExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_INDEX, -1);
		listModel.set(position, claim);
		refresh();
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater item) {
    	getActivity().getMenuInflater().inflate(R.menu.expense_claim_list, menu);
		super.onCreateOptionsMenu(menu, item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_claim:
			startAddExpenseClaimActivity();
			return true;
		case R.id.action_sort_claim:
			startSortExpenseClaimActivity();
			return true;
		case R.id.action_manage_tags:
			startManageTagsActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Starts the {@link ExpenseClaimAddActivity}
	 */
	private void startAddExpenseClaimActivity() {
		Intent addIntent = new Intent(getActivity(), ExpenseClaimAddActivity.class);
		addIntent.putExtra(ExpenseClaimAddActivity.EXTRA_EXPENSE_CLAIM_USER, user);
		startActivityForResult(addIntent, ADD_EXPENSE_CLAIM_REQUEST);
	}
	
	/**
	 * Starts the {@link ExpenseClaimSortActivity}
	 */
	private void startSortExpenseClaimActivity() {
		Intent intent = new Intent(getActivity(), ExpenseClaimSortActivity.class);
		startActivityForResult(intent, SORT_EXPENSE_CLAIM_REQUEST);
	}
		
	/**
	 * Starts the {@link ManageTagsActivity}
	 */
	private void startManageTagsActivity() {
		Intent manageTagsIntent = new Intent(getActivity(), ManageTagsActivity.class);
		startActivity(manageTagsIntent);
	}
    
	/**
	 * Prompts the user for confirmation in response to deleting an expense claim.
	 */
    public void startDeleteAlertDialog() {
		AlertDialog.Builder openDialog = new AlertDialog.Builder(getActivity());
		openDialog.setTitle(R.string.action_delete_claim_confirm);
		
		openDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				listModel.remove(longPressedItemIndex);
				refresh();
			}
		});
		
		openDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		openDialog.show();
	}
    
    private void setUserFromPreferences() {
    	SharedPreferences prefs = this.getActivity().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
		user = new User(prefs.getString("name", "USER DOES NOT EXIST"), prefs.getInt("id", -1));
	}
    
    

	
	
	
	//================================================================================
	// ListView Callbacks
	//================================================================================

    @Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		startEditExpenseClaimActivity(position);
	}
	
	/**
	 * Calls the intent to edit a expense claim at a specified position.
	 * @param position The position of the expense claim to edit.
	 */
	private void startEditExpenseClaimActivity(int position) {
		Intent editIntent = new Intent(getActivity(), ExpenseClaimDetailActivity.class);
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM, listModel.getItems().get(position));
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_INDEX, position);
		editIntent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM_USER, user);
		startActivityForResult(editIntent, EDIT_EXPENSE_CLAIM_REQUEST);
	}
	
	//================================================================================
	// TypedObserver
	//================================================================================
	
	
	public void refresh(){
		setListAdapter(new ExpenseClaimArrayAdapter(getActivity(), listModel.getItems()));
		
	}
	
	public void load(){
	}

	@Override
	public void update(TypedObservable<CollectionMutation<ExpenseClaim>> o,
			CollectionMutation<ExpenseClaim> arg) {
		// TODO Auto-generated method stub
		
	}
	
 
}