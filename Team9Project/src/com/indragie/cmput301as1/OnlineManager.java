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
	private int retry = 0;
	
	// use new methods of pull and then push it into the server
	
	public void checkForNetwork() {
		
		if (NetworkStateReceiver.isNetworkAvailable(context) == true) {
			// we are connected so then we want to go through with our stack calls
			
			// removes and returns the first item from the ll
			APICall<T> call = stack.getFirst();
			// now do something with the call
			call.enqueue(new ElasticSearchAPIClient.APICallback<T>() {
				@Override
				public void onFailure(Request request, Response response, IOException e) {
					// on failure you want to check the network
					if (NetworkStateReceiver.isNetworkAvailable(context)==false) {
						// if network is down, wait for connection
						
						
					} else {
						// the network is up but it has failed still
						retry++;
						// try again after a set amount of time
						try {
							Thread.sleep(retry*100);
							checkForNetwork();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

				@Override
				public void onSuccess(Response response, T document) {
				    // on success you want to recall if there is still something in the stack
					retry = 0; // reset the retry attempt number
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

}
