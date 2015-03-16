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

import com.indragie.cmput301as1.SectionedListIndex;

import junit.framework.TestCase;

public class SectionedListIndexTests extends TestCase {
	public void testItemIndexAccessors() {
		assertEquals(3, new SectionedListIndex(2, 3).getItemIndex());
		assertEquals(0, new SectionedListIndex(0, 0).getItemIndex());
	}
	
	public void testSectionIndexAccessors() {
		assertEquals(2, new SectionedListIndex(2, 3).getSectionIndex());
		assertEquals(0, new SectionedListIndex(0, 0).getSectionIndex());
	}
}
