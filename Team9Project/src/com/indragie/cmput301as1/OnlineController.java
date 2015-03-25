package com.indragie.cmput301as1;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import ca.ualberta.ssrg.movies.es.Movies;

import com.google.gson.Gson;

public class OnlineController {
	private Gson gson = new Gson();
	private static final String TAG = "OnlineController";
	private User users;
	
	public OnlineController(User users) {
		super();
		this.users = users;
	}

	public void addOnline(/*add user info*/) {
		HttpClient httpClient = new DefaultHttpClient();
		
		// need try and except block here
		try {
			HttpPost addRequest = new HttpPost(/* the user information*/);

			StringEntity stringEntity = new StringEntity(gson.toJson(/*user*/));
			addRequest.setEntity(stringEntity);
			addRequest.setHeader("Accept", "application/json");

			HttpResponse response = httpClient.execute(addRequest);
			String status = response.getStatusLine().toString();
			Log.i(TAG, status);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteOnline(/*User info*/) {
		HttpClient httpClient = new DefaultHttpClient();
		// try except block NEEDS FIXING
		try {
			HttpDelete deleteRequest = new HttpDelete(/* the user information */);
			deleteRequest.setHeader("Accept", "application/json");

			HttpResponse response = httpClient.execute(deleteRequest);
			String status = response.getStatusLine().toString();
			Log.i(TAG, status);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
