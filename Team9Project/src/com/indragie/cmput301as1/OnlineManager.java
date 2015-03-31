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
import com.indragie.cmput301as1.ElasticSearchAPIClient.APICallback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.content.Context;

public class OnlineManager {

	private static LinkedList<APICall<ElasticSearchDocument>> stack = new LinkedList<APICall<ElasticSearchDocument>>(); 
	
	public void enqueueCall(ElasticSearchAPIClient.APICall<ElasticSearchDocument> call) {
		stack.add(call);
		checkForNetwork();
	}
	
	private Context context;
	
	private void checkForNetwork() {
		if (NetworkStateReceiver.isNetworkAvailable(context) == true) {
			// we are connected so then we want to go through with our stack calls
			
			// while there is something inside of the stack
			while (stack.size() > 0) {
				// removes and returns the first item from the ll
				APICall<ElasticSearchDocument> call = stack.removeFirst();
				// now do something with the call
				call.enqueue(new ElasticSearchAPIClient.APICallback<T>() {
				    @Override
				    public void onFailure(Request request, Response response, IOException e) {

				    }

				    @Override
				    public void onSuccess(Response response, T document) {
				    
				    }
				});
			}
		}
	}
}
