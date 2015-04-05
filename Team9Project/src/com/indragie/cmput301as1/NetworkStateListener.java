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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Listens to state changes for network connectivity and notifies observers when
 * the state changes.
 */
public class NetworkStateListener extends BroadcastReceiver {
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * The current context.
	 */
	private Context context;
	
	/**
	 * The observable that observers use to receive updates when the 
	 * network state changes.
	 */
	private TypedObservable<NetworkInfo.State> observable;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link NetworkStateListener}
	 * @param context The current context.
	 */
	public NetworkStateListener(Context context) { 
		this.context = context;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return The observable that observers use to receive updates when
	 * the network state changes.
	 */
	public TypedObservable<NetworkInfo.State> getObservable() {
		return observable;
	}
	
	/**
	 * @return Whether a network connection is currently available.
	 */
	public NetworkInfo.State getNetworkState() {
		// From http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork == null) {
			return NetworkInfo.State.UNKNOWN;
		} else {
			return activeNetwork.getState();
		}
	}

	//================================================================================
	// BroadcastReceiver
	//================================================================================
	
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		observable.setChanged();
		observable.notifyObservers(getNetworkState());
	}
}
