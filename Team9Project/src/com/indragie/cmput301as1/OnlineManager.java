/* 
 * Copyright (C) 2015 Jimmy Ho
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

import java.io.IOException;
import java.util.LinkedList;

import com.indragie.cmput301as1.ElasticSearchAPIClient.APICall;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class OnlineManager<T extends ElasticSearchDocument> extends BroadcastReceiver{

	private LinkedList<ElasticSearchAPIClient.APICall<T>> stack = new LinkedList<ElasticSearchAPIClient.APICall<T>>(); 
	
	public void enqueueCall(ElasticSearchAPIClient.APICall<T> call) {
		stack.add(call);
		checkForNetwork();
	}
	
	private Context context;

	public void checkForNetwork() {
		// if there is something inside of the stack, do this, else do nothing
		if (stack.size() > 0) {	
			
			if (isNetworkAvailable(context) == true) {
				// we are connected so then we want to go through with our stack calls
				
				// removes and returns the first item from the linked list
				APICall<T> call = stack.getFirst();
				// now do something with the call
				call.enqueue(new ElasticSearchAPIClient.APICallback<T>() {
					@Override
					public void onFailure(Request request, Response response, IOException e) {
						// on failure you want to check the network
						if (isNetworkAvailable(context)==false) {
							// if network is down, wait for connection
							// do nothing as this will get called again once connection is established
						} 
					}
	
					@Override
					public void onSuccess(Response response, T document) {
					    // on success you want to recall if there is still something in the stack
						// remove the call from the stack
						stack.removeFirst();
						Log.d("test", "worked");
						if (stack.size()>0) {
							checkForNetwork();
						}
					}
				});
			}
		}
	}
	
	// http://stackoverflow.com/questions/12157130/internet-listener-android-example
	// we come into this code once we get connection / lose connection
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("app","Network connectivity change");
	     if(intent.getExtras()!=null) {
	        NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
	        if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED) {
	            Log.i("app","Network "+ni.getTypeName()+" connected");
	            checkForNetwork();
	        }
	     }
	     if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
	            Log.d("app","There's no network connectivity");
	     }
	}
	
	// http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
	public boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
