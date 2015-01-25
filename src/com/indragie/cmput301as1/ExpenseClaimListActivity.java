package com.indragie.cmput301as1;

import com.indragie.cmput301as1.dummy.DummyContent;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.ListActivity;

/**
 * An activity that presents a list of expense claims.
 */
public class ExpenseClaimListActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(
                this,
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                DummyContent.ITEMS));
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
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
