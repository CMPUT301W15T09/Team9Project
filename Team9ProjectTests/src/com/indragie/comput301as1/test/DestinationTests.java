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

import com.indragie.cmput301as1.Destination;
import com.indragie.cmput301as1.Geolocation;

import junit.framework.TestCase;


public class DestinationTests extends TestCase {
	public void testNameAccessors() {
		Destination destination = new Destination("Rome", "", null);
		assertEquals("Rome", destination.getName());
		
		destination.setName("Paris");
		assertEquals("Paris", destination.getName());
	}
	
	public void testTravelReasonAccessors() {
		Destination destination = new Destination("Rome", "Some reason", null);
		assertEquals("Some reason", destination.getTravelReason());
		
		destination.setTravelReason("Another reason");
		assertEquals("Another reason", destination.getTravelReason());
	}
	
	public void testLocationAccessors() {
		Geolocation location = new Geolocation(0,0);
		Geolocation location2 = new Geolocation(100, 50000);
		
		
		Destination destination = new Destination("Rome", "Some reason", location);
		assertEquals(location, destination.getLocation());
		
		destination.setLocation(location2);
		assertEquals(location2, destination.getLocation());
		
		
	}
	
}
