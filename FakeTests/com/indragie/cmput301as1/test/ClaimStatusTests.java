//================================================================================
// Test 4.1 from 6.1
//================================================================================
public void testSubmit(){
	ExpenseClaim claim = new ExpenseClaim("Test Claim","Test",new Date(), new Date(), ExpenseClaim.Status.IN_PROGRESS);
	submitClaim(claim);
	ExpenseClaimDetailActivity activity = loadClaim(claim);
	assertEquals("Claim submitted?", activity.findViewById(R.id.tv_status).toString(), ExpenseClaim.Status.SUBMITTED);
}

public void testSubmitEmpty(){
	ExpenseClaim claim = new ExpenseClaim(); //to be implemented
	submitClaim(claim);
	ExpenseClaimDetailActivity activity = loadClaim(claim);
	assertEquals("Claim not submitted?", activity.findViewById(R.id.tv_status), ExpenseClaim.Status.IN_PROGRESS);
}

public void testExpenses(){
	Boolean complete = true;
	ExpenseClaim claim = new ExpenseClaim("Test Claim","Test",new Date(), new Date(), ExpenseClaim.Status.IN_PROGRESS);
	List<ExpenseItem> list = claim.getItems();
	submitClaim(claim);
	ExpenseClaimDetailActivity activity = loadClaim(claim);
	for(int x=0;x<list.size();x++){
		if(!list.get(x).getComplete()){ //to be implemented
			complete = false;
		}
	}
	if(complete){
		assertEquals("Claim submitted?", activity.findViewById(R.id.tv_status), ExpenseClaim.Status.SUBMITTED);
	} else {
		assertEquals("Claim submitted?", activity.findViewById(R.id.tv_status), ExpenseClaim.Status.IN_PROGRESS);
	}
}

//================================================================================
// Test 4.2 from 6.2
//================================================================================
public void testDetails(){
	String name = "Joe";
	String comment = "Try again";
	ExpenseClaim claim = new ExpenseClaim("Test Claim","Test",new Date(), new Date(), ExpenseClaim.Status.IN_PROGRESS);
	claim.setApproverName(name); //to be implemented
	claim.setComment(comment); //to be implemented
	ExpenseClaimDetailActivity activity = loadClaim(claim);
	assertEquals("Name complete?", activity.findViewById(R.id.tv_ApproverName), name); //to be implemented
	assertEquals("Comments complete?", activity.findViewById(R.id.tv_comments), name); //to be implemented
	
}

//================================================================================
// Helper Methods
//================================================================================
private ExpenseClaimDetailActivity loadClaim(ExpenseClaim claim){
	Intent intent = new Intent();
	intent.putExtra(ExpenseClaimDetailActivity.EXTRA_EXPENSE_CLAIM, claim);
	setActivityIntent(intent);
	return (ExpenseClaimDetailActivity) getActivity();
	
}

private void submitClaim(ExpenseClaim claim){
	assertNotNull(activity.findViewById(com.indragie.cmput301as1.R.id.action_email));
	((Button) activity.findViewById(com.indragie.cmput301as1.R.id.action_email)).performClick();
}
