package com.indragie.cmput301as1;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;




/**
 * An activity representing a list of Claims. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ClaimDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ClaimListFragment} and the item details
 * (if present) is a {@link ClaimDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ClaimListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ClaimListActivity extends Activity
        implements ClaimListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_list);

        if (findViewById(R.id.claim_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ClaimListFragment) getFragmentManager()
                    .findFragmentById(R.id.claim_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link ClaimListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ClaimDetailFragment.ARG_ITEM_ID, id);
            ClaimDetailFragment fragment = new ClaimDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.claim_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ClaimDetailActivity.class);
            detailIntent.putExtra(ClaimDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
