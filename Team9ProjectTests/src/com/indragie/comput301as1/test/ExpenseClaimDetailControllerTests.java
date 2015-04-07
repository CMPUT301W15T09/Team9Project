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

import com.indragie.cmput301as1.Comment;
import com.indragie.cmput301as1.Destination;
import com.indragie.cmput301as1.ExpenseClaim;
import com.indragie.cmput301as1.ExpenseClaimDetailController;
import com.indragie.cmput301as1.ExpenseClaimDetailModel;
import com.indragie.cmput301as1.ExpenseItem;
import com.indragie.cmput301as1.SectionedListAdapter;
import com.indragie.cmput301as1.SectionedListIndex;
import com.indragie.cmput301as1.Tag;
import com.indragie.cmput301as1.User;

import android.test.AndroidTestCase;

public class ExpenseClaimDetailControllerTests extends AndroidTestCase {
	private ExpenseClaimDetailController controller;
	private Destination destination1;
	private Destination destination2;
	private ExpenseItem item1;
	private ExpenseItem item2;
	private Tag tag1;
	private Tag tag2;
	private Comment comment1;
	private Comment comment2;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ExpenseClaim claim = new ExpenseClaim("", "", new Date(), new Date(), new User("abcd", ""), ExpenseClaim.Status.IN_PROGRESS);
		destination1 = new Destination("Rome", "", null); 
		destination2 = new Destination("Paris", "", null); 
		claim.addDestination(destination1);
		claim.addDestination(destination2);
		
		tag1 = new Tag("testtag");
		tag2 = new Tag("testtag2");
		claim.addTag(tag1);
		claim.addTag(tag2);
		
		User approver = new User("bcde", "approver");
		comment1 = new Comment(approver, "testcomment", new Date(), Comment.Status.RETURNED);
		comment2 = new Comment(approver, "testcomment2", new Date(), Comment.Status.APPROVED);
		claim.addComments(comment1);
		claim.addComments(comment2);
		
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
		assertEquals(new SectionedListIndex(2, SectionedListAdapter.NOT_AN_ITEM_INDEX), controller.getSectionedIndex(6));
		assertEquals(new SectionedListIndex(2, 0), controller.getSectionedIndex(7));
		assertEquals(new SectionedListIndex(2, 1), controller.getSectionedIndex(8));
		assertEquals(new SectionedListIndex(3, SectionedListAdapter.NOT_AN_ITEM_INDEX), controller.getSectionedIndex(9));
		assertEquals(new SectionedListIndex(3, 0), controller.getSectionedIndex(10));
		assertEquals(new SectionedListIndex(3, 1), controller.getSectionedIndex(11));
	}
	
	public void testGetItemType() {
		assertNull(controller.getItemType(0));
		assertEquals(ExpenseClaimDetailController.DetailItem.ItemType.DESTINATION, controller.getItemType(1));
		assertEquals(ExpenseClaimDetailController.DetailItem.ItemType.DESTINATION, controller.getItemType(2));
		assertNull(controller.getItemType(3));
		assertEquals(ExpenseClaimDetailController.DetailItem.ItemType.TAG, controller.getItemType(4));
		assertEquals(ExpenseClaimDetailController.DetailItem.ItemType.TAG, controller.getItemType(5));
		assertNull(controller.getItemType(6));
		assertEquals(ExpenseClaimDetailController.DetailItem.ItemType.COMMENT, controller.getItemType(7));
		assertEquals(ExpenseClaimDetailController.DetailItem.ItemType.COMMENT, controller.getItemType(8));
		assertNull(controller.getItemType(9));
		assertEquals(ExpenseClaimDetailController.DetailItem.ItemType.EXPENSE_ITEM, controller.getItemType(10));
		assertEquals(ExpenseClaimDetailController.DetailItem.ItemType.EXPENSE_ITEM, controller.getItemType(11));
	}
	
	public void testGetDestination() {
		assertEquals(destination1, controller.getDestination(0));
		assertEquals(destination2, controller.getDestination(1));	
	}
	
	public void testGetTag() {
		assertEquals(tag1, controller.getTag(0));
		assertEquals(tag2, controller.getTag(1));
	}
	
	public void testGetComent() {
		assertEquals(comment1, controller.getComment(0));
		assertEquals(comment2, controller.getComment(1));
	}
	
	public void testGetExpenseItem() {
		assertEquals(item1, controller.getExpenseItem(0));
		assertEquals(item2, controller.getExpenseItem(1));
	}
	
	public void testRemove() throws Exception {
		controller.remove(1);
		assertEquals(destination2, controller.getDestination(0));
		controller.remove(3);
		assertEquals(tag2, controller.getTag(0));
	}
}
