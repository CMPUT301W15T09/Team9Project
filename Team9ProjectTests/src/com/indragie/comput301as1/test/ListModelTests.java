/* 
 * Copyright (C) 2015 Indragie Karunaratne
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.indragie.comput301as1.test;

import java.util.ArrayList;
import java.util.Date;

import com.indragie.cmput301as1.CollectionMutation;
import com.indragie.cmput301as1.InsertionCollectionMutation;
import com.indragie.cmput301as1.RemovalCollectionMutation;
import com.indragie.cmput301as1.UpdateCollectionMutation;
import com.indragie.cmput301as1.ExpenseClaim;
import com.indragie.cmput301as1.ExpenseClaimListActivity;
import com.indragie.cmput301as1.TypedObservable;
import com.indragie.cmput301as1.TypedObserver;
import com.indragie.cmput301as1.User;
import com.indragie.cmput301as1.ListModel;

import android.test.ActivityInstrumentationTestCase2;

public class ListModelTests extends ActivityInstrumentationTestCase2<ExpenseClaimListActivity> {
	private class TestObserver<T> implements TypedObserver<CollectionMutation<T>> {
		private CollectionMutation<T> mutation;
		
		@Override
		public void update(TypedObservable<CollectionMutation<T>> observable, CollectionMutation<T> mutation) {
			this.mutation = mutation;
		}
		
		public CollectionMutation<T> getMutation() {
			return mutation;
		}
	}
	
	private static final String CLAIMS_FILENAME = "claims.dat";
	private ListModel<ExpenseClaim> listModel;
	private TestObserver<ExpenseClaim> observer;
	
	public ListModelTests() {
		super(ExpenseClaimListActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		listModel = new ListModel<ExpenseClaim>(CLAIMS_FILENAME, getActivity());
		listModel.removeAll();
		observer = new TestObserver<ExpenseClaim>();
		listModel.addObserver(observer);
	}
	
	private static ExpenseClaim createExpenseClaim(String name) {
		return new ExpenseClaim(name, null, new Date(), new Date(), new User("test_id", "Test User"), ExpenseClaim.Status.IN_PROGRESS);
	}
	
	public void testAdd() {
		assertEquals(0, listModel.count());
		
		ExpenseClaim claim = createExpenseClaim("URoma");
		listModel.add(claim);
		assertEquals(1, listModel.count());
		assertEquals(claim, listModel.getItems().get(0));
		
		CollectionMutation<ExpenseClaim> mutation = observer.getMutation();
		assertEquals(CollectionMutation.MutationType.INSERT, mutation.getMutationType());
		assertEquals(claim, ((InsertionCollectionMutation<ExpenseClaim>)mutation).getObject());
		assertEquals(0, ((InsertionCollectionMutation<ExpenseClaim>)mutation).getIndex());
	}
	
	public void testRemoveWithObject() {
		assertEquals(0, listModel.count());
		
		ExpenseClaim claim = createExpenseClaim("URoma");
		listModel.add(claim);
		assertEquals(1, listModel.count());
		
		listModel.remove(claim);
		assertEquals(0, listModel.count());
		
		CollectionMutation<ExpenseClaim> mutation = observer.getMutation();
		assertEquals(CollectionMutation.MutationType.REMOVE, mutation.getMutationType());
		assertEquals(claim, ((RemovalCollectionMutation<ExpenseClaim>)mutation).getObject());
		assertEquals(0, ((RemovalCollectionMutation<ExpenseClaim>)mutation).getIndex());
	}
	
	public void testRemoveWithNonexistentObject() {
		assertEquals(0, listModel.count());
		
		ExpenseClaim claim = createExpenseClaim("URoma");
		listModel.add(claim);
		assertEquals(1, listModel.count());
		
		listModel.remove(createExpenseClaim("Maui"));
		assertEquals(1, listModel.count());
		
		CollectionMutation<ExpenseClaim> mutation = observer.getMutation();
		assertEquals(CollectionMutation.MutationType.INSERT, mutation.getMutationType());
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
		assertEquals(claim2, listModel.getItems().get(0));
		
		CollectionMutation<ExpenseClaim> mutation = observer.getMutation();
		assertEquals(CollectionMutation.MutationType.REMOVE, mutation.getMutationType());
		assertEquals(claim1, ((RemovalCollectionMutation<ExpenseClaim>)mutation).getObject());
		assertEquals(0, ((RemovalCollectionMutation<ExpenseClaim>)mutation).getIndex());
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
		assertEquals(claim1, listModel.getItems().get(0));
		assertEquals(claim2, listModel.getItems().get(1));
		
		listModel.set(1, claim3);
		assertEquals(claim1, listModel.getItems().get(0));
		assertEquals(claim3, listModel.getItems().get(1));
		
		CollectionMutation<ExpenseClaim> mutation = observer.getMutation();
		assertEquals(CollectionMutation.MutationType.UPDATE, mutation.getMutationType());
		assertEquals(claim2, ((UpdateCollectionMutation<ExpenseClaim>)mutation).getOldObject());
		assertEquals(claim3, ((UpdateCollectionMutation<ExpenseClaim>)mutation).getNewObject());
		assertEquals(1, ((UpdateCollectionMutation<ExpenseClaim>)mutation).getIndex());
	}
	
	public void testGet() {
		ArrayList<ExpenseClaim> claims = new ArrayList<ExpenseClaim>();
		ExpenseClaim claim1 = createExpenseClaim("Beijing");
		ExpenseClaim claim2 = createExpenseClaim("Shanghai");
		
		claims.add(claim1);
		assertFalse("Did not get the same ArrayList", listModel.getArrayList().equals(claims));
		
		listModel.add(claim1);
		assertEquals(claims, listModel.getArrayList());
		
		claims.add(claim2);
		listModel.add(claim2);
		assertEquals(claims, listModel.getArrayList());
	}
	
	public void testReplace() {
		ExpenseClaim claim1 = createExpenseClaim("Beijing");
		ExpenseClaim claim2 = createExpenseClaim("Shanghai");
		listModel.add(claim1);

		ArrayList<ExpenseClaim> oldClaims = new ArrayList<ExpenseClaim>();
		oldClaims.add(claim1);
		oldClaims.add(claim2);
		assertFalse("Not the same ArrayList after replacing", listModel.getArrayList().equals(oldClaims));
		
		listModel.replaceList(oldClaims);
		assertEquals(oldClaims, listModel.getArrayList());
		
		ExpenseClaim claim3 = createExpenseClaim("Britain");
		ArrayList<ExpenseClaim> newClaims = new ArrayList<ExpenseClaim>();
		newClaims.add(claim3);
		
		listModel.replaceList(newClaims);
		assertEquals(newClaims, listModel.getArrayList());
		
	}
	
	
	public void testPersistence() {
		ExpenseClaim claim = createExpenseClaim("URoma");
		listModel.add(claim);
		
		ListModel<ExpenseClaim> sameFileListModel = new ListModel<ExpenseClaim>(CLAIMS_FILENAME, getActivity());
		assertEquals(1, sameFileListModel.count());
		assertEquals(claim, sameFileListModel.getItems().get(0));
		
		ListModel<ExpenseClaim> newListModel = new ListModel<ExpenseClaim>("someotherfile.dat", getActivity());
		assertEquals(0, newListModel.count());
	}
}
