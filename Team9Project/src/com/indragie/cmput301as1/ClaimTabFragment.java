package com.indragie.cmput301as1;

import java.util.List;
 
 
/** This is a listfragment class */
public class ClaimTabFragment extends BaseTabFragment {
 
    
	@Override
	public void load(){
		listModel = new ListModel<ExpenseClaim>(EXPENSE_CLAIM_FILENAME, getActivity(), 2);
		listModel.addObserver((TypedObserver<List<ExpenseClaim>>) getActivity());
	}


}