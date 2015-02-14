//================================================================================
// Test 2.1
//================================================================================
protected void testGetList() {
	Intent intent = new Intent();
	TagsActivity activity = getActivity();

	Tag tag = new Tag("Some new tag");
	Tag tag2 = new Tag("Some other tag");
	Tag tag3 = new Tag("This other tag");

	ArrayAdapter<Tag> adapter = activity.getAdapter();

	assertEquals(adapter.getCount, 0));

	adapter.add(tag);
	adapter.add(tag2);
	adapter.add(tag3);

	assertEquals(tag, adapter.getItem(0));
	assertEquals(tag3, adapter.getItem(2));
}


//================================================================================
// Test 2.2
//================================================================================
public void testAddTags() {
	Tag tag1 = new Tag("tag 1");
	Tag tag5 = new Tag("tag 5");
	ExpenseClaim claim = new ExpenseClaim("Some claim","description", new Date(2015, 01, 20), new Date(2015, 01, 31), null);
	ExpenseClaim claim2 = new ExpenseClaim("Some other claim", "description", new Date(2015, 01, 20), new Date(2015, 01, 25), null);

	claim.addTag(tag1);

	assertTrue(claim.hasTag());
	assertFalse(claim2.hasTag());

	assertEquals(claim.numTags() == 1);
	assertEquals(claim2.numTags() == 0);
}

//================================================================================
// Test 2.3
//================================================================================
public void testManipulateTags() {
	Tags tag = new Tags();

	assertNull(tags.listTags());

	tags.addTag(tag1);
	tags.addTag(tag5);

	List<String> check = new ArrayList<String>();
	check.add(tag1);
	check.add(tag5);
	assertArrayEquals(check, tags.listTags());

	tags.renameTag(tag1, tag3);

	check.set(check.indexOf(tag1), tag5);
	assertArrayEquals(check,tag.listTags());

	tags.removeTag(tag3);
	check.remove(tag3);
	assertArrayEquals(check, tags.ListTag());
}

//================================================================================
// Test 2.4
//================================================================================
public void testMatchTags() {
	Tag tag1 = new Tag("tag 1");
	Tag tag5 = new Tag("tag 5");
	Tag tag3 = new Tag("tag 3");

	ExpenseClaim claim = new ExpenseClaim("Some claim","description", new Date(2015, 01, 20), new Date(2015, 01, 31), null);
	ExpenseClaim claim2 = new ExpenseClaim("Some other claim", "description", new Date(2015, 01, 20), new Date(2015, 01, 25), null)

	claim.addTag(tag1);
	claim2.addTag(tag5);
	claim2.addTag(tag3);

	assertTrue(claim.hasTag(tag1));
	assertFalse(claim.hasTag(tag3));

	assertTrue(claim2.hasTag(tag3));
	assertTrue(claim2.hasTag(tag5));
}
