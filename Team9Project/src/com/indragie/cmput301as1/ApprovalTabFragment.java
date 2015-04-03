package com.indragie.cmput301as1;

import java.util.List;
 
 
/** This is a listfragment class */
public class ApprovalTabFragment extends BaseTabFragment {
	
    
	@SuppressWarnings("unchecked")
	@Override
	public void load(){
		listModel = new ListModel<ExpenseClaim>(EXPENSE_APPROVAL_FILENAME, getActivity());
		listModel.addObserver((TypedObserver<List<ExpenseClaim>>) getActivity());
	}
}