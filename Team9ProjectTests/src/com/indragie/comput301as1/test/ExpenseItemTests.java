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

import com.indragie.cmput301as1.ExpenseItem;

import junit.framework.TestCase;

public class ExpenseItemTests extends TestCase {
	public void testNameAccessors() {
		ExpenseItem item = new ExpenseItem("Hotel", "", "", Money.parse("USD 2000.00"), new Date());
		assertEquals("Hotel", item.getName());
		
		item.setName("Taxi");
		assertEquals("Taxi", item.getName());
	}
	
	public void testDateAccessors() {
		Date currentDate = new Date();
		ExpenseItem item = new ExpenseItem("Hotel", "", "", Money.parse("USD 2000.00"), currentDate);
		assertEquals(currentDate, item.getDate());
		
		Date newDate = new Date(0);
		item.setDate(newDate);
		assertEquals(newDate, item.getDate());
	}
	
	public void testCategoryAccessors() {
		ExpenseItem item = new ExpenseItem("Hotel", "", "accomodation", Money.parse("USD 2000.00"), new Date());
		assertEquals("accomodation", item.getCategory());
		
		item.setCategory("transport");
		assertEquals("transport", item.getCategory());
	}
	
	public void testDescriptionAccessors() {
		ExpenseItem item = new ExpenseItem("Hotel", "Some description", "", Money.parse("USD 2000.00"), new Date());
		assertEquals("Some description", item.getDescription());
		
		item.setDescription("Another description");
		assertEquals("Another description", item.getDescription());
	}
	
	public void testAmountAccessors() {
		Money amount = Money.parse("USD 2000.00");
		ExpenseItem item = new ExpenseItem("Hotel", "Some description", "", amount, new Date());
		assertEquals(amount, item.getAmount());
		
		Money newAmount = Money.parse("CAD 60.00");
		item.setAmount(newAmount);
		assertEquals(newAmount, item.getAmount());
	}
	
	public void testComparison() {
		ExpenseItem item1 = new ExpenseItem("Hotel", "Some description", "", Money.parse("USD 2000.00"), new Date(0));
		ExpenseItem item2 = new ExpenseItem("Hotel", "Some description", "", Money.parse("USD 2000.00"), new Date(5000));
		
		assertEquals(0, item1.compareTo(item1));
		assertTrue(item2.compareTo(item1) > 0);
		assertTrue(item1.compareTo(item2) < 0);
	}
}
