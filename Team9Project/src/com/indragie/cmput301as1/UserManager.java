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

package com.indragie.cmput301as1;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages the creation and registration of users.
 */
public class UserManager {
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Preference key used to store the JSON data for the active user.
	 */
	private static final String PREFS_USER_KEY = "user";
	
	/**
	 * Name of the preference file used to store user preferences.
	 */
	private static final String PREFS_NAME = "user_preferences";
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * Used to read and write from the preference file.
	 */
	private SharedPreferences preferences;
	
	/**
	 * The active user.
	 */
	private User activeUser;
	
	/**
	 * Used to serialize the {@link User} object to and from JSON.
	 */
	private Gson gson = new Gson();
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link UserManager}
	 * @param context The current context.
	 */
	public UserManager(Context context) {
		this.preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return The active user, or null if there is no active user.
	 */
	public User getActiveUser() {
		if (activeUser == null) {
			String json = preferences.getString(PREFS_USER_KEY, null);
			if (json != null) {
				activeUser = gson.fromJson(json, User.class);
			}
		}
		return activeUser;
	}
	
	/**
	 * Sets the active user.
	 * @param user The active user.
	 */
	public void setActiveUser(User user) {
		activeUser = user;
		String json = gson.toJson(user);
		preferences.edit().putString(PREFS_USER_KEY, json).apply();
	}
}
