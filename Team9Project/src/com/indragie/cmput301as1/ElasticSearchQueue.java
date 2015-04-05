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
import java.util.concurrent.ConcurrentLinkedQueue;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Handles queueing of requests and automatic retry of failed requests.
 */
public class ElasticSearchQueue<T> implements TypedObserver<NetworkInfo.State> {
	//================================================================================
	// Classes
	//================================================================================
	
	/**
	 * Tag used for all logging from {@link ElasticSearchQueue}
	 */
	private static final String LOG_TAG = "es_queue";
	
	//================================================================================
	// Classes
	//================================================================================
	
	/**
	 * Container class for an API call and corresponding callback.
	 */
	private class QueueItem {
		ElasticSearchAPIClient.APICall<T> call;
		ElasticSearchAPIClient.APICallback<T> callback;
		
		QueueItem(ElasticSearchAPIClient.APICall<T> call, ElasticSearchAPIClient.APICallback<T> callback) {
			this.call = call;
			this.callback = callback;
		}
	}
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * The queue of API calls to execute.
	 */
	private ConcurrentLinkedQueue<QueueItem> queue = new ConcurrentLinkedQueue<QueueItem>();
	
	/**
	 * Used to receive updates when the network state changes.
	 */
	private NetworkStateListener networkListener;
	
	/**
	 * Whether a request is currently executing.
	 */
	private boolean requestExecuting;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link ElasticSearchQueue}
	 * @param context The current context.
	 */
	public ElasticSearchQueue(Context context) {
		networkListener = new NetworkStateListener(context);
		networkListener.getObservable().addObserver(this);
	}
	
	//================================================================================
	// TypedObserver<NetworkState.info>
	//================================================================================
	
	/* (non-Javadoc)
	 * @see com.indragie.cmput301as1.TypedObserver#update(com.indragie.cmput301as1.TypedObservable, java.lang.Object)
	 */
	@Override
	public void update(TypedObservable<State> observable, State state) {
		if (state == State.CONNECTED) {
			Log.v(LOG_TAG, "Network connection became available. Retrying pending requests.");
			attemptNextAPICall();
		}
	}
	
	//================================================================================
	// API
	//================================================================================

	/**
	 * adds a call to the stack. Then attempts to execute it.
	 * @param APICall<T>
	 */
	public void enqueueCall(ElasticSearchAPIClient.APICall<T> call, ElasticSearchAPIClient.APICallback<T> callback) {
		queue.add(new QueueItem(call, callback));
		attemptNextAPICall();
	}

	/**
	 * checks for APICalls in the stack and for network connection then executes the APICall
	 */
	private void attemptNextAPICall() {
		if (requestExecuting 
			|| queue.size() == 0 
			|| networkListener.getNetworkState() != State.CONNECTED) return;
		
		final QueueItem item = queue.peek();
		final ElasticSearchAPIClient.APICallback<T> callback = item.callback;
		
		requestExecuting = true;
		item.call.enqueue(new ElasticSearchAPIClient.APICallback<T>() {
			@Override
			public void onSuccess(Response response, T document) {
				queue.poll();
				if (callback != null) {
					callback.onSuccess(response, document);
				}
				requestExecuting = false;
				attemptNextAPICall();
			}

			@Override
			public void onFailure(Request request, Response response, IOException e) {
				// Check if the request executed but failed (bad HTTP status code)
				// This scenario cannot be handled, so the API call should be discarded
				// and the failure callback should be called.
				//
				// The other scenario is that the request did not execute at all, likely
				// due to some connectivity issue. In this case, the request should
				// be retried.
				if (response != null) {
					Log.v(LOG_TAG, "Request failed with response " + response.toString());
					queue.poll();
					if (callback != null) {
						callback.onFailure(request, response, e);
					}
				} else {
					Log.v(LOG_TAG, "Request failed with exception " + e.toString());
				}
				requestExecuting = false;
			}
		});
	}
}
