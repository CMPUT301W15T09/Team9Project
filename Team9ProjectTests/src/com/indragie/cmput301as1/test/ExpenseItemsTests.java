package com.indragie.cmput301as1.test;

import java.util.ArrayList;

import com.indragie.cmput301as1.ExpenseClaim;
import com.indragie.cmput301as1.ExpenseItem;
import com.indragie.cmput301as1.R;
import com.indragie.cmput301as1.ExpenseClaim.Status;

import android.test.ActivityInstrumentationTestCase2;

public class ExpenseItemsTests extends ActivityInstrumentationTestCase2<T> {

	public ExpenseItemsTests(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	//================================================================================
	// Test 3_1
	//================================================================================
	
	public void testExpenseClaim() {
		ExpenseClaim claim = new ExpenseClaim(null, null, null, null, null);
	    	ExpenseItem item = new ExpenseItem(null, null, null, null, null);
		ExpenseItem item2 = new ExpenseItem(null, null, null, null, null);
		
		claim.addItem(item);
		assert (claim.getItems().size() == 1);
		claim.addItem(item2);
		assert (claim.getItems().size() == 2);   
	}
	
	//================================================================================
	// Test 3_2
	//================================================================================
	
	public void testCategories() {
		//As a claimant, I want the category for an expense item to be one of air fare, 
		//ground transport, vehicle rental, private automobile, fuel, 
		//parking, registration, accommodation, meal, or supplies.

		//create an array of the different categories
		ArrayList<String> categories = new ArrayList<String>();
		categories.add("air fare");
		categories.add("ground transport");
		categories.add("vehicle rental");
		categories.add("private automobile");
		categories.add("fuel");
		categories.add("parking");
		categories.add("registration");
		categories.add("accomodation");
		categories.add("meal");
		categories.add("supplies");
		
		//assert that these categories exist inside the array
		for (int i=0; i<categories.size(); i++) {
			String category = categories.get(i);
			Spinner categorySpinner = (Spinner)findViewById(R.id.sp_category);
			String item = (String) categorySpinner.getItemAtPosition(i);

			// create function contains which will see return true if it contains the item
			assertTrue(categories.contains(item));
		}
	}
	
	//================================================================================
	// Test 3_3
	//================================================================================
	
	public void testCurrencies() {
		// As a claimant, I want the currency for an expense amount to be
		// one of CAD, USD, EUR, GBP, CHF, JPY, or CNY.
		ArrayList<String> currencies = new ArrayList<String>();
		currencies.add("CAD");
		currencies.add("USD");
		currencies.add("EUR");
		currencies.add("GBP");
		currencies.add("CHF");
		currencies.add("JPY");
		currencies.add("CNY");
		
		for (int i=0; i<currencies.size(); i++) {
			String currency = currencies.get(i);
			Spinner currencySpinner = (Spinner)findViewById(R.id.sp_category);
			String item = (String) currencySpinner.getItemAtPosition(i);

			assertTrue(currencies.contains(item));
		}
	}
	
	//================================================================================
	// Test 3_4
	//================================================================================
	
	public void testIncomplete() {
		// create an attribute getFlag that will determine if the expense is complete or not
		// Returns a boolean, false if incomplete, true if complete.
		ExpenseItem expenseItem = new ExpenseItem(null, null, null, null, null);
		
		assertFlase(expenseItem.getFlag()); //Incomplete by default, therefore set to false
		
		expenseItem.setComplete();
		
		assertTrue(expenseItem.getFlag());
		
		expenseItem.setIncomplete();
		assertFalse(expenseItem.getFlag());

	}
	
	//================================================================================
	// Test 3_5
	//================================================================================
	
	public void testDetails() {
		ExpenseClaim claim = new ExpenseClaim(null, null, null, null, null);
		ExpenseItem item = new ExpenseItem(null, null, null, null, null);
		
		claim.addItem(item);
		assert (claim.getItems().size() == 1);
		
		String description = ((ExpenseClaim) claim.getItems()).getDescription();
		
		assertNotNull(description);
	}
	
	//================================================================================
	// Test 3_6
	//================================================================================
	
	public void testEditItem() {
		// get the status first of the claim
		ExpenseClaim claim = new ExpenseClaim(null, null, null, null, null);
		Status status = claim.getStatus();
		
		assert (status != status.APPROVED);
		assert (status != status.SUBMITTED);
		
		// now add a new item
		ExpenseItem item = new ExpenseItem(null, null, null, null, null);
		claim.addItem(item);
		
		// edit the item and check to see if the editted item is the same as before or not
		ExpenseItem item2 = new ExpenseItem("test", null, null, null, null);
		claim.setItem(0, item2);
		assertNotNull(((ExpenseClaim) claim.getItems()).getName());
		
	}
	
	//================================================================================
	// Test 3_7
	//================================================================================
	
	public void testDeleteItem() {
		ExpenseClaim claim = new ExpenseClaim(null, null, null, null, null);
		Status status = claim.getStatus();
		
		assert (status != status.APPROVED);
		assert (status != status.SUBMITTED);
		
		// now test to delete the item
		ExpenseItem item = new ExpenseItem(null, null, null, null, null);
		claim.addItem(item);
		
		claim.removeItem(0);
		assert (claim.getItems().size() == 0);
	}
	
	//================================================================================
	// Test 3_8
	//================================================================================
	
	// manual test is required. have clickcount increment everything the user clicks. 
	// see how many clicks it takes the user to finish adding an item.
	public void testUserInterface() {
		int clickcount = 0;	
	}
	
	//================================================================================
	// Test 3_9 from 4_1
	//================================================================================
	
	public void testListAllItems() {
		// first add a claim and then add three items
		ExpenseClaim claim = new ExpenseClaim(null, null, null, null, null);
		ExpenseItem item1 = new ExpenseItem(null, null, null, null, null);
		ExpenseItem item2 = new ExpenseItem(null, null, null, null, null);
		ExpenseItem item3 = new ExpenseItem(null, null, null, null, null);
		
		claim.addItem(item1);
		claim.addItem(item2);
		claim.addItem(item3);
		
		// check to see if items exist by seeing if size() is 3
		assert(claim.getItems().size() == 3);

		// attempt to retrieve these items and see if they exist, repeat for the different 3 items
		// assume that we have inputted values that are not null and that valid values return
		Intent intent = getIntent();
		ExpenseItem item = (ExpenseItem)getIntent().getSerializableExtra(EXTRA_EXPENSE_ITEM);
		
		Money amount = item.getAmount();
		assertNotNull(amount);

		String name = item.getName();
		assertNotNull(name);
		
		String description = item.getDescription();
		assertNotNull(description!=null);
		
		Date date = item.getDate();
		assertNotNull(date!=null);
		
		String category = item.getCategory();
		assertNotNull(category);		
	}
	
	//===============================================================
	// Test 3_10 from 5_1: add photographic receipt to expense item
	//===============================================================
	public void addReceiptTest() {

		ExpenseItem expense = new ExpenseItem();
		assertNull(expense.getReceiptUri());
		
		// addReceipt() sends an intent to a camera app to take the picture
		// then updates the expense item to save the file
		expense.addReceipt();
		assertTrue("Receipt not attached to expense item!", expense.getReceiptUri().equals(null));
	}
	
	//===============================================================
	// Test 3_11 from 5_2: view receipt
	//===============================================================
	public void viewReceiptTest() {
		
		ExpenseItem expense = new ExpenseItem();
		ImageView iv = (ImageView) findViewById(R.id.iv_receipt);
		expense.addReceipt();
		
		assertFalse("Image isn't showing properly", iv.getDrawable().equals(null));
		
	}
	//===============================================================
	// Test 3_12 from 5_3: delete receipt from expense item
	//===============================================================
	public void deleteReceiptTest() {
		
		ExpenseItem expense = new ExpenseItem();
		
		// attach receipt first and make sure it's there
		expense.addReceipt();
		assertTrue("No receipt", expense.getReceiptUri());	
		
		// remove the receipt
		expense.deleteReceipt();
		assertTrue("Receipt still attached!", expense.getReceiptUri().equals(null));
	}
	
	//===============================================================
	// Test 3_13 from 5_4: check whether receipt is under 65536 bytes
	//===============================================================
	public void fileSizeValidTest() {
		
		ExpenseItem expense = new ExpenseItem();
		
		// attach a test receipt, should be a valid file size
		expense.addReceipt(); // validSize is called in addReceipt()
		
		File image = new File(getpathFromUri(expense.getReceiptUri()));
		long imageSize = image.length(); // size of photo attached
		
		long sizeL = 65536;
		long sizeS = 512;
		RandomAccessFile testFileL = new RandomAccessFile("test.txt", "rw"); 	// invalid size
		RandomAccessFile testFileS = new RandomAccessFile("test2.txt", "rw");	// valid size
		
		try {
			testFileL.setLength(sizeL);
			testFileS.setLength(sizeS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertTrue("validSize failed, photo is too large ", imageSize < sizeL);
		assertTrue("testFileS is considered too big", validSize(testFileS));
		assertFalse("testFileL is considered valid", validSize(testFileL));
	}
}
