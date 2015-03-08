package com.indragie.comput301as1.test;

import java.util.Date;

import com.indragie.cmput301as1.ExpenseClaim;
import com.indragie.cmput301as1.ExpenseClaimListActivity;
import com.indragie.cmput301as1.ExpenseClaimListModel;

import android.test.ActivityInstrumentationTestCase2;

public class ExpenseClaimListModelTests extends ActivityInstrumentationTestCase2<ExpenseClaimListActivity> {
	private static final String CLAIMS_FILENAME = "claims.dat";
	private ExpenseClaimListModel listModel;
	
	public ExpenseClaimListModelTests() {
		super(ExpenseClaimListActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		listModel = new ExpenseClaimListModel(CLAIMS_FILENAME, getActivity());
		listModel.removeAll();
	}
	
	private static ExpenseClaim createExpenseClaim(String name) {
		return new ExpenseClaim(name, null, new Date(), new Date(), ExpenseClaim.Status.IN_PROGRESS);
	}
	
	public void testAdd() {
		assertEquals(0, listModel.count());
		
		ExpenseClaim claim = createExpenseClaim("URoma");
		listModel.add(claim);
		assertEquals(1, listModel.count());
		assertEquals(claim, listModel.getExpenseClaims().get(0));
	}
	
	public void testRemoveWithObject() {
		assertEquals(0, listModel.count());
		
		ExpenseClaim claim = createExpenseClaim("URoma");
		listModel.add(claim);
		assertEquals(1, listModel.count());
		
		listModel.remove(claim);
		assertEquals(0, listModel.count());
	}
	
	public void testRemoveWithNonexistentObject() {
		assertEquals(0, listModel.count());
		
		ExpenseClaim claim = createExpenseClaim("URoma");
		listModel.add(claim);
		assertEquals(1, listModel.count());
		
		listModel.remove(createExpenseClaim("Maui"));
		assertEquals(1, listModel.count());
	}
	
	public void testRemoveWithIndex() {
		assertEquals(0, listModel.count());
		
		ExpenseClaim claim1 = createExpenseClaim("URoma");
		ExpenseClaim claim2 = createExpenseClaim("Maui");
		listModel.add(claim1);
		listModel.add(claim2);
		assertEquals(2, listModel.count());
		
		listModel.remove(0);
		assertEquals(1, listModel.count());
		assertEquals(claim2, listModel.getExpenseClaims().get(0));
	}
	
	public void testRemoveAll() {
		assertEquals(0, listModel.count());
		
		ExpenseClaim claim1 = createExpenseClaim("URoma");
		ExpenseClaim claim2 = createExpenseClaim("Maui");
		listModel.add(claim1);
		listModel.add(claim2);
		assertEquals(2, listModel.count());
		
		listModel.removeAll();
		assertEquals(0, listModel.count());
	}
	
	public void testSet() {
		ExpenseClaim claim1 = createExpenseClaim("URoma");
		ExpenseClaim claim2 = createExpenseClaim("Maui");
		ExpenseClaim claim3 = createExpenseClaim("Paris");
		
		listModel.add(claim1);
		listModel.add(claim2);
		assertEquals(claim1, listModel.getExpenseClaims().get(0));
		assertEquals(claim2, listModel.getExpenseClaims().get(1));
		
		listModel.set(1, claim3);
		assertEquals(claim1, listModel.getExpenseClaims().get(0));
		assertEquals(claim3, listModel.getExpenseClaims().get(1));
	}
	
	public void testPersistence() {
		ExpenseClaim claim = createExpenseClaim("URoma");
		listModel.add(claim);
		
		ExpenseClaimListModel sameFileListModel = new ExpenseClaimListModel(CLAIMS_FILENAME, getActivity());
		assertEquals(1, sameFileListModel.count());
		assertEquals(claim, sameFileListModel.getExpenseClaims().get(0));
		
		ExpenseClaimListModel newListModel = new ExpenseClaimListModel("someotherfile.dat", getActivity());
		assertEquals(0, newListModel.count());
	}
}
