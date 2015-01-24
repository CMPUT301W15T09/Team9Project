package com.indragie.cmput301as1;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

/**
 * An activity that presents a list of expense claims.
 */
public class ClaimListActivity extends Activity implements ClaimListFragment.Callbacks {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_list);
    }

    @Override
    public void onItemSelected(String id) {
    	Intent detailIntent = new Intent(this, ClaimDetailActivity.class);
        detailIntent.putExtra(ClaimDetailFragment.ARG_ITEM_ID, id);
        startActivity(detailIntent);
    }
}
