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
import com.indragie.cmput301as1.ExpenseClaimDetailModel;
import com.indragie.cmput301as1.ExpenseItem;
import com.indragie.cmput301as1.User;

import junit.framework.TestCase;

public class ExpenseClaimDetailModelTests extends TestCase {
	private ExpenseClaimDetailModel model;
	private Destination destination1;
	private Destination destination2;
	private ExpenseItem item1;
	private ExpenseItem item2;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ExpenseClaim claim = new ExpenseClaim("", "", new Date(), new Date(), new User(""), ExpenseClaim.Status.IN_PROGRESS);
		destination1 = new Destination("Rome", "");
		destination2 = new Destination("Paris", "");
		claim.addDestination(destination1);
		claim.addDestination(destination2);
		
		item1 = new ExpenseItem("Hotel", "", "accomodation", Money.parse("USD 2000.00"), new Date());
		item2 = new ExpenseItem("Taxi", "", "ground transport", Money.parse("USD 50.00"), new Date());
		claim.addItem(item1);
		claim.addItem(item2);
		
		model = new ExpenseClaimDetailModel(claim);
	}
	
	public void testGetDestinations() {
		assertEquals(destination1, model.getDestinations().get(0));
		assertEquals(destination2, model.getDestinations().get(1));
	}
	
	public void testAddDestination() {
		Destination destination = new Destination("Berlin", "");
		model.addDestination(destination);
		assertEquals(destination, model.getDestinations().get(2));
	}
	
	public void testSetDestination() {
		Destination destination = new Destination("Berlin", "");
		model.setDestination(0, destination);
		assertEquals(destination, model.getDestinations().get(0));
	}
	
	public void testRemoveDestination() {
		assertEquals(2, model.getDestinations().size());
		model.removeDestination(0);
		assertEquals(1, model.getDestinations().size());
		assertEquals(destination2, model.getDestinations().get(0));
	}
	
	public void testGetItems() {
		assertEquals(item1, model.getItems().get(0));
		assertEquals(item2, model.getItems().get(1));
	}
	
	public void testAddItem() {
		ExpenseItem item = new ExpenseItem("Dinner", "", "meal", Money.parse("USD 10.00"), new Date());
		model.addItem(item);
		assertEquals(item, model.getItems().get(2));
	}
	
	public void testSetItem() {
		ExpenseItem item = new ExpenseItem("Dinner", "", "meal", Money.parse("USD 10.00"), new Date());
		model.setItem(0, item);
		assertEquals(item, model.getItems().get(0));
	}
	
	public void testRemoveItem() {
		assertEquals(2, model.getItems().size());
		model.removeItem(0);
		assertEquals(1, model.getItems().size());
		assertEquals(item2, model.getItems().get(0));
	}
}
