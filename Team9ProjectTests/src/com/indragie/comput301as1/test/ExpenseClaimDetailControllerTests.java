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

import java.util.Date;

import org.joda.money.Money;

import com.indragie.cmput301as1.Destination;
import com.indragie.cmput301as1.ExpenseClaim;
import com.indragie.cmput301as1.ExpenseClaimDetailController;
import com.indragie.cmput301as1.ExpenseClaimDetailModel;
import com.indragie.cmput301as1.ExpenseItem;
import com.indragie.cmput301as1.SectionedListAdapter;
import com.indragie.cmput301as1.SectionedListIndex;

import android.test.AndroidTestCase;

public class ExpenseClaimDetailControllerTests extends AndroidTestCase {
	private ExpenseClaimDetailController controller;
	private Destination destination1;
	private Destination destination2;
	private ExpenseItem item1;
	private ExpenseItem item2;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ExpenseClaim claim = new ExpenseClaim("", "", new Date(), new Date(), ExpenseClaim.Status.IN_PROGRESS);
		destination1 = new Destination("Rome", "");
		destination2 = new Destination("Paris", "");
		claim.addDestination(destination1);
		claim.addDestination(destination2);
		
		item1 = new ExpenseItem("Hotel", "", "accomodation", Money.parse("USD 2000.00"), new Date());
		item2 = new ExpenseItem("Taxi", "", "ground transport", Money.parse("USD 50.00"), new Date());
		claim.addItem(item1);
		claim.addItem(item2);
		
		ExpenseClaimDetailModel model = new ExpenseClaimDetailModel(claim);
		controller = new ExpenseClaimDetailController(getContext(), model);
	}
	
	public void testGetAdapter() {
		assertNotNull(controller.getAdapter());
	}
	
	public void testGetSectionedIndex() {
		assertEquals(new SectionedListIndex(0, SectionedListAdapter.NOT_AN_ITEM_INDEX), controller.getSectionedIndex(0));
		assertEquals(new SectionedListIndex(0, 0), controller.getSectionedIndex(1));
		assertEquals(new SectionedListIndex(0, 1), controller.getSectionedIndex(2));
		assertEquals(new SectionedListIndex(1, SectionedListAdapter.NOT_AN_ITEM_INDEX), controller.getSectionedIndex(3));
		assertEquals(new SectionedListIndex(1, 0), controller.getSectionedIndex(4));
		assertEquals(new SectionedListIndex(1, 1), controller.getSectionedIndex(5));
	}
	
	public void testGetItemType() {
		assertNull(controller.getItemType(0));
		assertEquals(ExpenseClaimDetailController.DetailItem.ItemType.DESTINATION, controller.getItemType(1));
		assertEquals(ExpenseClaimDetailController.DetailItem.ItemType.DESTINATION, controller.getItemType(2));
		assertNull(controller.getItemType(3));
		assertEquals(ExpenseClaimDetailController.DetailItem.ItemType.EXPENSE_ITEM, controller.getItemType(4));
		assertEquals(ExpenseClaimDetailController.DetailItem.ItemType.EXPENSE_ITEM, controller.getItemType(5));
	}
	
	public void testGetDestination() {
		assertEquals(destination1, controller.getDestination(0));
		assertEquals(destination2, controller.getDestination(1));	
	}
	
	public void testGetExpenseItem() {
		assertEquals(item1, controller.getExpenseItem(0));
		assertEquals(item2, controller.getExpenseItem(1));
	}
	
	public void testRemove() throws Exception {
		controller.remove(1);
		assertEquals(destination2, controller.getDestination(0));
		controller.remove(3);
		assertEquals(item2, controller.getExpenseItem(0));
	}
}
