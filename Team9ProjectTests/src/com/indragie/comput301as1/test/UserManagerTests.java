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

import com.indragie.cmput301as1.User;
import com.indragie.cmput301as1.UserManager;

import android.test.AndroidTestCase;

public class UserManagerTests extends AndroidTestCase {
	public void testActiveUser() {
		UserManager manager = new UserManager(getContext());
		User user = new User("test_id", "Indragie Karunaratne");
		manager.setActiveUser(user);
		assertEquals(user, manager.getActiveUser());
		
		// UserManager for the same Context should be accessing the
		// same shared data.
		UserManager anotherManager = new UserManager(getContext());
		assertEquals(user, anotherManager.getActiveUser());
	}
}
