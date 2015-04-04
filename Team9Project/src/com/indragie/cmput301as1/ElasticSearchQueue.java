/* 
 * Copyright (C) 2015 Indragie Karunaratne, Jimmy Ho
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

/**
 * Handles queueing of requests and automatic retry of failed requests.
 */
public class ElasticSearchQueue<T extends ElasticSearchDocument> extends BroadcastReceiver{

	private LinkedList<ElasticSearchAPIClient.APICall<T>> stack = new LinkedList<ElasticSearchAPIClient.APICall<T>>();
	private Context context;

	public ElasticSearchQueue(Context context) {
		super();
		this.context = context;
	}

	/**
	 * adds a call to the stack. Then attempts to execute it.
	 * @param APICall<T>
	 */
	public void enqueueCall(ElasticSearchAPIClient.APICall<T> call, ElasticSearchAPIClient.APICallback<T> callback) {
		stack.add(call);
		attemptNextAPICall();
	}

	/**
	 * checks for APICalls in the stack and for network connection then executes the APICall
	 */
	private void attemptNextAPICall() {
		// if there is something inside of the stack, do this, else do nothing
		if (stack.size() > 0) {	

			if (isNetworkAvailable(context)) {
				// we are connected so then we want to go through with our stack calls
				APICall<T> call = stack.getFirst();

				// now do something with the call
				call.enqueue(new ElasticSearchAPIClient.APICallback<T>() {
					@Override
					public void onFailure(Request request, Response response, IOException e) {
						// if response is not null then it means that server returned a bad HTTP status code
						// otherwise the response is null which means connection has been lost
						if (response!=null) {
							stack.removeFirst();
							try {
								throw new Exception("Bad HTTP status code");
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} 
					}

					@Override
					public void onSuccess(Response response, T document) {
						// on success you want to recall if there is still something in the stack
						// remove the call from the stack
						stack.removeFirst();
						Log.d("test", "worked2");
						if (stack.size()>0) {
							attemptNextAPICall();
						}
					}
				});
			}
			Log.d("test", "no internet");
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
				Log.d("test", "reconnected");
				attemptNextAPICall();
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
