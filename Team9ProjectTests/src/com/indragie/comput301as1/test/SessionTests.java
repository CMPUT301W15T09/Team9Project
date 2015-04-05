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

import com.indragie.cmput301as1.ExpenseClaim;
import com.indragie.cmput301as1.Session;
import com.indragie.cmput301as1.User;

import android.test.AndroidTestCase;

public class SessionTests extends AndroidTestCase {
	public void testSingleton() {
		User user = new User("test_id_1", "Indragie Karunaratne");
		Session session = new Session(getContext(), user);
		Session.setSharedSession(session);
		assertEquals(session, Session.getSharedSession());
	}
	
	public void testUserDataSiloing() {
		User user1 = new User("test_id_1", "Indragie Karunaratne");
		User user2 = new User("test_id_2", "Evil Twin");
		
		ExpenseClaim claim = new ExpenseClaim("URoma", "", new Date(), new Date(), user1, ExpenseClaim.Status.IN_PROGRESS);
		
		Session session1 = new Session(getContext(), user1);
		session1.getOwnedClaims().removeAll();
		session1.getReviewalClaims().removeAll();
		
		assertEquals(0, session1.getOwnedClaims().count());
		assertEquals(0, session1.getReviewalClaims().count());
		
		session1.getOwnedClaims().add(claim);
		session1.getReviewalClaims().add(claim);
		assertEquals(1, session1.getOwnedClaims().count());
		assertEquals(1, session1.getReviewalClaims().count());
		
		// Data for a different user should be separate
		Session session2 = new Session(getContext(), user2);
		assertEquals(0, session2.getOwnedClaims().count());
		assertEquals(0, session2.getReviewalClaims().count());
		
		// Data for the same user should be shared
		Session session3 = new Session(getContext(), user1);
		assertEquals(1, session3.getOwnedClaims().count());
		assertEquals(1, session3.getReviewalClaims().count());
	}
}
