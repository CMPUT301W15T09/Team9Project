package com.indragie.cmput301as1;

import java.util.List;
 
 
/** This is a listfragment class */
public class ClaimTabFragment extends BaseTabFragment {
 
    
	@SuppressWarnings("unchecked")
	@Override
	public void load(){
		listModel = new ListModel<ExpenseClaim>(EXPENSE_CLAIM_FILENAME, getActivity());
		listModel.addObserver((TypedObserver<CollectionMutation<ExpenseClaim>>) getActivity());
	}


}