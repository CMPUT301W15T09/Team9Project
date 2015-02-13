private ExpenseClaim createSampleExpenseClaim(String destination) {
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	Date startDate = dateFormat.parse("12-02-2015");
	Date endDate = dateFormat.parse("17-02-2015");
	ArrayList<String> destinations = new ArrayList<String>();
	destinations.add(destination);
	return new ExpenseClaim("Indragie Karunaratne", startDate, endDate, destinations);
}

//================================================================================
// Test 1.1
//================================================================================
public void testCreateExpenseClaim() {
	ExpenseClaimCollection collection = new ExpenseClaimCollection();
	assertEquals("Collection should have no claims to begin with", collection.size(), 0);

	Intent intent = new Intent();
	intent.putExtra(ExpenseClaimAddActivity.COLLECTION_KEY, collection);
	setActivityIntent(intent);

	ExpenseClaim claim = createSampleExpenseClaim("Paris");
	ExpenseClaimAddActivity activity = getActivity();
	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	activity.runOnUiThread(new Runnable() {
		public void run() {
			((EditText)activity.findViewById(R.id.name_field)).setText(claim.getName());
			((EditText)activity.findViewById(R.id.start_date_field)).setText(dateFormat.format(claim.getStartDate()));
			((EditText)activity.findViewById(R.id.end_date_field)).setText(dateFormat.format(claim.getEndDate()));
			activity.getDestinationsAdapter().addAll(claim.getDestinations());
			((Button)activity.findViewById(R.id.done_button)).performClick();
		}
	});
	getInstrumentation().waitForIdleSync();
	assertEquals("Collection should have 1 expense claim", collection.size(), 1);
	assertEquals("Collection's expense claim should match created claim", collection.get(0), claim);
}

//================================================================================
// Test 1.2
//================================================================================
public void testListExpenseClaims() {
	ExpenseClaimCollection collection = new ExpenseClaimCollection();
	ExpenseClaim claim1 = createSampleExpenseClaim("Paris");
	ExpenseClaim claim2 = createSampleExpenseClaim("Rome");
	collection.add(claim1);
	collection.add(claim2);

	Intent intent = new Intent();
	intent.putExtra(ExpenseClaimListActivity.COLLECTION_KEY, collection);
	setActivityIntent(intent);

	ArrayAdapter<ExpenseClaim> adapter = getActivity().getAdapter();
	assertEquals("First claim should be in the list", adapter.get(0), claim1);
	assertEquals("Second claim should be in the list", adapter.get(1), claim2);
}

//================================================================================
// Test 1.3
//================================================================================
public void testViewExpenseClaimDetails() {
	ExpenseClaim claim = createSampleExpenseClaim("Paris");

	Intent intent = new Intent();
	intent.putExtra(ExpenseClaimEditActivity.CLAIM_KEY, claim);
	setActivityIntent(intent);

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	assertEquals("Name should be shown", ((EditText)activity.findViewById(R.id.name_field)).getText(), claim.getName());
	assertEquals("Start date should be shown", ((EditText)activity.findViewById(R.id.start_date_field)).getText(), dateFormat.format(claim.getStartDate()));
	assertEquals("End date should be shown", ((EditText)activity.findViewById(R.id.end_date_field)).getText(), dateFormat.format(claim.getEndDate()));
	assertEquals("Destinations should be shown", activity.getDestinationsAdapter().get(0), claim.getDestinations().get(0));
}

//================================================================================
// Test 1.4.1
//================================================================================
public void testChangeTravelDates() {
	ExpenseClaim claim = createSampleExpenseClaim("Rome");
	Intent intent = new Intent();
	intent.putExtra(ExpenseClaimEditActivity.CLAIM_KEY, claim);
	setActivityIntent(intent);

	Date newStartDate = dateFormat.parse("12-03-2015");
	Date newEndDate = dateFormat.parse("17-03-2015");

	ExpenseClaimEditActivity activity = getActivity();
	activity.runOnUiThread(new Runnable() {
		public void run() {
			((EditText)activity.findViewById(R.id.start_date_field)).setText(dateFormat.format(newStartDate));
			((EditText)activity.findViewById(R.id.end_date_field)).setText(dateFormat.format(newEndDate));
		}
	});
	getInstrumentation().waitForIdleSync();

	assertEquals("Claim's start date should have been modified", activity.getClaim().getStartDate(), newStartDate);
	assertEquals("Claim's end date should have been modified", activity.getClaim().getEndDate(), newEndDate);
}

//================================================================================
// Test 1.4.3
//================================================================================
public void testAddTags() {
	ExpenseClaim claim = createSampleExpenseClaim("Rome");
	Intent intent = new Intent();
	intent.putExtra(ExpenseClaimEditActivity.CLAIM_KEY, claim);
	setActivityIntent(intent);

	ExpenseClaimEditActivity activity = getActivity();
	Tag tag = new Tag("tourism");
	activity.runOnUiThread(new Runnable() {
		public void run() {
			Toast toast = activity.startAddTagToast();
			((EditText)toast.getView().findViewById(R.id.name_field)).setText(tag.toString());
			((Button)toast.getView().findViewById(R.id.ok_button)).performClick();
		}
	});
	getInstrumentation().waitForIdleSync();
	assertEquals("Claim should have tag added", activity.getClaim().getTags().get(0), tag);
}

//================================================================================
// Test 1.4.4
//================================================================================
public void testDeleteTags() {
	ExpenseClaim claim = createSampleExpenseClaim("Rome");
	Tag tag1 = new Tag("business");
	Tag tag2 = new Tag("tourism")
	Intent intent = new Intent();
	intent.putExtra(ExpenseClaimEditActivity.CLAIM_KEY, claim);
	setActivityIntent(intent);

	ExpenseClaimEditActivity activity = getActivity();
	ArrayAdapter<Tag> tagsAdapter = activity.getTagsAdapter()
	assertEquals("There should be 2 tags initially", tagsAdapter.getCount(), 2);
	
	activity.getClaim().removeTag(1);
	tagsAdapter.notifyDataSetChanged();

	assertEquals("There should be 1 tag after removing one", tagsAdapter.getCount(), 1);
	assertEquals("Remaining tag should be the first one", tagsAdapter.get(0), tag1);
}

//================================================================================
// Test 1.4.5
//================================================================================
public void testAddDestinations() {
	ExpenseClaim claim = createSampleExpenseClaim("Rome");
	Intent intent = new Intent();
	intent.putExtra(ExpenseClaimEditActivity.CLAIM_KEY, claim);
	setActivityIntent(intent);

	ExpenseClaimEditActivity activity = getActivity();
	String destination = "Paris";
	activity.runOnUiThread(new Runnable() {
		public void run() {
			Toast toast = activity.startAddDestinationToast();
			((EditText)toast.getView().findViewById(R.id.name_field)).setText(destination);
			((Button)toast.getView().findViewById(R.id.ok_button)).performClick();
		}
	});
	getInstrumentation().waitForIdleSync();
	assertEquals("Claim should have destination added", activity.getClaim().getDestinations().get(1), destination);
}

//================================================================================
// Test 1.4.6
//================================================================================
public void testDeleteDestinations() {
	ExpenseClaim claim = createSampleExpenseClaim("Rome");
	claim.addDestination("Paris");
	Intent intent = new Intent();
	intent.putExtra(ExpenseClaimEditActivity.CLAIM_KEY, claim);
	setActivityIntent(intent);

	ExpenseClaimEditActivity activity = getActivity();
	ArrayAdapter<String> destinationsAdapter = activity.getDestinationsAdapter();
	assertEquals("There should be 2 destinations initially", destinationsAdapter.getCount(), 2);
	
	activity.getClaim().removeTag(1);
	destinationsAdapter.notifyDataSetChanged();

	assertEquals("There should be 1 destination after removing one", destinationsAdapter.getCount(), 1);
	assertEquals("Remaining destination should be the first one", destinationsAdapter.get(0), "Rome");
}

//================================================================================
// Test 1.5
//================================================================================
public void testDeleteExpenseClaim() {
	ExpenseClaimCollection collection = new ExpenseClaimCollection();
	ExpenseClaim claim1 = createSampleExpenseClaim("Paris");
	ExpenseClaim claim2 = createSampleExpenseClaim("Rome");
	collection.add(claim1);
	collection.add(claim2);

	Intent intent = new Intent();
	intent.putExtra(ExpenseClaimListActivity.COLLECTION_KEY, collection);
	setActivityIntent(intent);

	ArrayAdapter<ExpenseClaim> adapter = getActivity().getAdapter();
	assertEquals("There should be 2 claims initially", adapter.getCount(), 2);

	collection.removeClaim(1);
	adapter.notifyDataSetChanged();

	assertEquals("There should be 1 claim after deleting one", adapter.getCount(), 1);
	assertEquals("Remaining claim should be the first claim", adapter.get(0), claim1);
}