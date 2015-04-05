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
import java.util.List;

import com.indragie.cmput301as1.CollectionMutation;
import com.indragie.cmput301as1.CreationDateAscendingComparator;
import com.indragie.cmput301as1.InsertionCollectionMutation;
import com.indragie.cmput301as1.RemovalCollectionMutation;
import com.indragie.cmput301as1.ReplacementCollectionMutation;
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
		private ArrayList<CollectionMutation<T>> mutations = new ArrayList<CollectionMutation<T>>();
		
		@Override
		public void update(TypedObservable<CollectionMutation<T>> observable, CollectionMutation<T> mutation) {
			mutations.add(mutation);
		}
		
		public List<CollectionMutation<T>> getMutations() {
			return mutations;
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
		listModel.setComparator(new CreationDateAscendingComparator());
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
		
		assertEquals(2, observer.getMutations().size());
		
		CollectionMutation<ExpenseClaim> insertMutation = observer.getMutations().get(0);
		assertEquals(CollectionMutation.MutationType.INSERT, insertMutation.getMutationType());
		assertEquals(claim, ((InsertionCollectionMutation<ExpenseClaim>)insertMutation).getObject());
		assertEquals(0, ((InsertionCollectionMutation<ExpenseClaim>)insertMutation).getIndex());
		
		CollectionMutation<ExpenseClaim> replacementMutation = observer.getMutations().get(1);
		assertEquals(CollectionMutation.MutationType.REPLACEMENT, replacementMutation.getMutationType());
	}
	
	public void testRemoveWithObject() {
		assertEquals(0, listModel.count());
		
		ExpenseClaim claim = createExpenseClaim("URoma");
		listModel.add(claim);
		assertEquals(1, listModel.count());
		
		listModel.remove(claim);
		assertEquals(0, listModel.count());
		
		assertEquals(3, observer.getMutations().size());
		
		CollectionMutation<ExpenseClaim> mutation = observer.getMutations().get(2);
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
		
		assertEquals(2, observer.getMutations().size());
		
		CollectionMutation<ExpenseClaim> mutation = observer.getMutations().get(0);
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
		
		assertEquals(5, observer.getMutations().size());
		
		CollectionMutation<ExpenseClaim> mutation = observer.getMutations().get(4);
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
		ArrayList<ExpenseClaim> claims = new ArrayList<ExpenseClaim>(listModel.getItems());
		
		listModel.removeAll();
		assertEquals(0, listModel.count());
		
		assertEquals(5, observer.getMutations().size());
		
		CollectionMutation<ExpenseClaim> replacementMutation = observer.getMutations().get(4);
		assertEquals(CollectionMutation.MutationType.REPLACEMENT, replacementMutation.getMutationType());
		assertEquals(claims, ((ReplacementCollectionMutation<ExpenseClaim>)replacementMutation).getOldContents());
		assertEquals(new ArrayList<ExpenseClaim>(), ((ReplacementCollectionMutation<ExpenseClaim>)replacementMutation).getNewContents());
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
		
		assertEquals(6, observer.getMutations().size());
		
		CollectionMutation<ExpenseClaim> mutation = observer.getMutations().get(4);
		assertEquals(CollectionMutation.MutationType.UPDATE, mutation.getMutationType());
		assertEquals(claim2, ((UpdateCollectionMutation<ExpenseClaim>)mutation).getOldObject());
		assertEquals(claim3, ((UpdateCollectionMutation<ExpenseClaim>)mutation).getNewObject());
		assertEquals(1, ((UpdateCollectionMutation<ExpenseClaim>)mutation).getIndex());
		
		CollectionMutation<ExpenseClaim> replacementMutation = observer.getMutations().get(5);
		assertEquals(CollectionMutation.MutationType.REPLACEMENT, replacementMutation.getMutationType());
	}
	
	public void testGetItems() {
		ExpenseClaim claim1 = createExpenseClaim("Beijing");
		ExpenseClaim claim2 = createExpenseClaim("Shanghai");
		
		assertEquals(0, listModel.getItems().size());
		
		listModel.add(claim1);
		assertEquals(1, listModel.getItems().size());
		assertEquals(claim1, listModel.getItems().get(0));
		
		listModel.add(claim2);
		assertEquals(2, listModel.getItems().size());
		assertEquals(claim1, listModel.getItems().get(0));
		assertEquals(claim2, listModel.getItems().get(1));
	}
	
	public void testReplace() {
		ExpenseClaim claim1 = createExpenseClaim("Beijing");
		ExpenseClaim claim2 = createExpenseClaim("Shanghai");
		ArrayList<ExpenseClaim> oldClaims = new ArrayList<ExpenseClaim>();
		oldClaims.add(claim1);
		listModel.add(claim1);
		
		assertEquals(oldClaims, listModel.getItems());
		
		ArrayList<ExpenseClaim> newClaims = new ArrayList<ExpenseClaim>();
		newClaims.add(claim2);
		
		listModel.replace(newClaims);
		assertEquals(newClaims, listModel.getItems());
		
		CollectionMutation<ExpenseClaim> replacementMutation = observer.getMutations().get(2);
		assertEquals(CollectionMutation.MutationType.REPLACEMENT, replacementMutation.getMutationType());
		assertEquals(oldClaims, ((ReplacementCollectionMutation<ExpenseClaim>)replacementMutation).getOldContents());
		assertEquals(newClaims, ((ReplacementCollectionMutation<ExpenseClaim>)replacementMutation).getNewContents());
		
		listModel.replace(new ArrayList<ExpenseClaim>());
		assertEquals(0, listModel.count());
		
		replacementMutation = observer.getMutations().get(3);
		assertEquals(CollectionMutation.MutationType.REPLACEMENT, replacementMutation.getMutationType());
		assertEquals(newClaims, ((ReplacementCollectionMutation<ExpenseClaim>)replacementMutation).getOldContents());
		assertEquals(new ArrayList<ExpenseClaim>(), ((ReplacementCollectionMutation<ExpenseClaim>)replacementMutation).getNewContents());
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
