/* 
 * Copyright (C) 2015 Andrew Zhong
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
import java.util.Collections;
import java.util.List;

import com.indragie.cmput301as1.Tag;

import junit.framework.TestCase;

public class TagsTests extends TestCase {

	String funsies = "funsies";
	
	public TagsTests() {
		super();
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testTagName() {
		Tag tag = new Tag(funsies);
		Tag tag2 = new Tag(null);
		
		assertEquals(funsies, tag.getName());
		assertNull(tag2.getName());
	}
	
	public void testCompare() {
		String moreFunsies = "more funsies";
		String aFan = "A fan";
		Tag tag1 = new Tag(aFan);
		Tag tag2 = new Tag (funsies);
		Tag tag3 = new Tag(moreFunsies);
		
		List<Tag> unsortedList = new ArrayList<Tag>();
		unsortedList.add(tag3);
		unsortedList.add(tag1);
		unsortedList.add(tag2);
		Collections.sort(unsortedList);
		
		assertEquals(tag1, unsortedList.get(0));
		assertEquals(tag2, unsortedList.get(1));
		assertEquals(tag3, unsortedList.get(2));
		
	}
}
