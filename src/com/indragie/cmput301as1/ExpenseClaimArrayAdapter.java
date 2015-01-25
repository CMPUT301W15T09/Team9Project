package com.indragie.cmput301as1;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adaptor for showing an array of {@link ExpenseClaim} objects in a {@link ListView}
 * Based on https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */
public class ExpenseClaimArrayAdapter extends ArrayAdapter<ExpenseClaim> {
	public ExpenseClaimArrayAdapter(Context context, List<ExpenseClaim> claims) {
		super(context, R.layout.expense_claim_list_row, claims);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ExpenseClaim claim = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_claim_list_row, parent, false);
		}
		TextView tvName = (TextView)convertView.findViewById(R.id.tv_name);
		tvName.setText(claim.getName());
		
		TextView tvAmounts = (TextView)convertView.findViewById(R.id.tv_amounts);
		String amounts = claim.getSummarizedAmounts();
		if (amounts == null) {
			amounts = getContext().getResources().getString(R.string.no_expenses);
		}
		tvAmounts.setText(amounts);
		return convertView;
	}
}
