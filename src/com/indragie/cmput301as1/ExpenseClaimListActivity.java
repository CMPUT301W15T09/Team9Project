package com.indragie.cmput301as1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.app.Activity;

/**
 * An activity that presents a list of expense claims.
 */
public class ExpenseClaimListActivity extends Activity implements ExpenseClaimListFragment.Callbacks {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_list);
    }

    @Override
    public void onItemSelected(String id) {
    	Intent detailIntent = new Intent(this, ExpenseClaimDetailActivity.class);
        detailIntent.putExtra(ExpenseClaimDetailFragment.ARG_ITEM_ID, id);
        startActivity(detailIntent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_claim_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
