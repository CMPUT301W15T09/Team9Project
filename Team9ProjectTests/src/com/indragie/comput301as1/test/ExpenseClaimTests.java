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

import org.joda.money.Money;

import com.indragie.cmput301as1.Destination;
import com.indragie.cmput301as1.ExpenseClaim;
import com.indragie.cmput301as1.ExpenseItem;
import com.indragie.cmput301as1.Tag;

import junit.framework.TestCase;

public class ExpenseClaimTests extends TestCase {
	public void testNameAccessors() {
		ExpenseClaim claim = new ExpenseClaim("Indragie", "", new Date(), new Date(), ExpenseClaim.Status.IN_PROGRESS);
		assertEquals("Indragie", claim.getName());
		
		claim.setName("Karunaratne");
		assertEquals("Karunaratne", claim.getName());
	}
	
	public void testDescriptionAccessors() {
		ExpenseClaim claim = new ExpenseClaim("Indragie", "Some description", new Date(), new Date(), ExpenseClaim.Status.IN_PROGRESS);
		assertEquals("Some description", claim.getDescription());
		
		claim.setDescription("Another description");
		assertEquals("Another description", claim.getDescription());
	}
	
	public void testDestinationsAccessors() {
		ExpenseClaim claim = new ExpenseClaim("Indragie", "", new Date(), new Date(), ExpenseClaim.Status.IN_PROGRESS);
		
		Destination destination1 = new Destination("Rome", "");
		Destination destination2 = new Destination("Paris", "");
		claim.addDestination(destination1);
		claim.addDestination(destination2);
		
		assertEquals(destination1, claim.getDestinations().get(0));
		assertEquals(destination2, claim.getDestinations().get(1));
		
		Destination destination3 = new Destination("NYC", "");
		claim.setDestination(0, destination3);
		assertEquals(destination3, claim.getDestinations().get(0));
		assertEquals(destination2, claim.getDestinations().get(1));
		
		claim.removeDestination(0);
		assertEquals(destination2, claim.getDestinations().get(0));
		
		ArrayList<Destination> newDestinations = new ArrayList<Destination>();
		newDestinations.add(destination1);
		newDestinations.add(destination2);
		claim.setDestinations(newDestinations);
		assertEquals(destination1, claim.getDestinations().get(0));
		assertEquals(destination2, claim.getDestinations().get(1));
	}
	
	public void testItemsAccessors() {
		ExpenseClaim claim = new ExpenseClaim("Indragie", "", new Date(), new Date(), ExpenseClaim.Status.IN_PROGRESS);
		ExpenseItem item1 = new ExpenseItem("Hotel", "", "", Money.parse("USD 2000.00"), new Date());
		ExpenseItem item2 = new ExpenseItem("Taxi", "", "", Money.parse("USD 50.00"), new Date());
		claim.addItem(item1);
		claim.addItem(item2);
		
		assertEquals(item1, claim.getItems().get(0));
		assertEquals(item2, claim.getItems().get(1));
		
		ExpenseItem item3 = new ExpenseItem("Flight", "", "", Money.parse("USD 500.00"), new Date(0));
		claim.setItem(0, item3);
		assertEquals(item3, claim.getItems().get(0));
		assertEquals(item2, claim.getItems().get(1));
		
		claim.removeItem(0);
		assertEquals(item2, claim.getItems().get(0));
		
		ArrayList<ExpenseItem> newItems = new ArrayList<ExpenseItem>();
		newItems.add(item1);
		newItems.add(item2);
		claim.setItems(newItems);
		assertEquals(item1, claim.getItems().get(0));
		assertEquals(item2, claim.getItems().get(1));
	}
	
	public void testStatusAccessors() {
		ExpenseClaim claim = new ExpenseClaim("Indragie", "", new Date(), new Date(), ExpenseClaim.Status.IN_PROGRESS);
		assertEquals(ExpenseClaim.Status.IN_PROGRESS, claim.getStatus());
		
		claim.setStatus(ExpenseClaim.Status.APPROVED);
		assertEquals(ExpenseClaim.Status.APPROVED, claim.getStatus());
	}
	
	public void testStartDateAccessors() {
		Date currentDate = new Date();
		ExpenseClaim claim = new ExpenseClaim("Indragie", "", currentDate, new Date(), ExpenseClaim.Status.IN_PROGRESS);
		assertEquals(currentDate, claim.getStartDate());
		
		Date newDate = new Date(5000);
		claim.setStartDate(newDate);
		assertEquals(newDate, claim.getStartDate());
	}
	
	public void testEndDateAccessors() {
		Date currentDate = new Date();
		ExpenseClaim claim = new ExpenseClaim("Indragie", "", new Date(), currentDate, ExpenseClaim.Status.IN_PROGRESS);
		assertEquals(currentDate, claim.getEndDate());
		
		Date newDate = new Date(5000);
		claim.setEndDate(newDate);
		assertEquals(newDate, claim.getEndDate());
	}
	
	public void testTagAccessors() {
		ExpenseClaim claim = new ExpenseClaim("Indragie", "", new Date(), new Date(), ExpenseClaim.Status.IN_PROGRESS);
		Tag tag1 = new Tag("business");
		Tag tag2 = new Tag("fun");
		claim.addTag(tag1);
		claim.addTag(tag2);
		
		assertEquals(tag1, claim.getTags().get(0));
		assertEquals(tag2, claim.getTags().get(1));
		
		claim.removeTag(0);
		assertEquals(tag2, claim.getTags().get(0));
		
		claim.removeTag(tag2);
		assertEquals(0, claim.getTags().size());
		
		assertFalse(claim.hasTag(tag1));
		claim.addTag(tag1);
		assertTrue(claim.hasTag(tag1));
	}
	
	public void testGetSummarizedAmounts() {
		ExpenseClaim claim = new ExpenseClaim("Indragie", "", new Date(), new Date(), ExpenseClaim.Status.IN_PROGRESS);
		ExpenseItem item1 = new ExpenseItem("Hotel", "", "", Money.parse("USD 2000.00"), new Date());
		ExpenseItem item2 = new ExpenseItem("Taxi", "", "", Money.parse("USD 50.00"), new Date());
		claim.addItem(item1);
		claim.addItem(item2);
		assertEquals("USD 2050.00", claim.getSummarizedAmounts());
		
		ExpenseItem item3 = new ExpenseItem("Flight", "", "", Money.parse("CAD 500.00"), new Date(0));
		claim.addItem(item3);
		assertEquals("CAD 500.00\nUSD 2050.00", claim.getSummarizedAmounts());
	}
}
