package com.indragie.comput301as1.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.indragie.cmput301as1.Tag;

import junit.framework.Assert;
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
