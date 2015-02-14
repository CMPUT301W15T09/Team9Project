//================================================================================
// Test 6.1 from 8.1
//================================================================================
protected void testOfflinePush() {
	Claim claim = new Claim("Some claim", new Date(2015, 01, 20), new Date(2015, 01, 31));

	saveInFile(claim);

	assertEquals(claim, loadFromFile());

	claim.addTag("Adding a tag to make this claim different");

	assertNotEquals(claim, loadFromFile());
}
