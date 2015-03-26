package com.indragie.cmput301as1;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import ca.ualberta.ssrg.movies.es.Movies;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class OnlineESManager {
	private static final String TAG = "UserSearch";
	private Gson gson;
	private User user;
	
	
	// CREATE A NEW CLASS THAT CONTAINS A LIST OF USERS
	public User getUsers() {
		return user;
	}

	public OnlineESManager(String search) {
		gson = new Gson();
		searchUser(search, null);
	}

	/**
	 * Get a movie with the specified id
	 */
	public User getUser(int id) {
		
		// NEED TO IMPLEMENT A SEARCHHIT CLASS
		//SearchHit<Movie> sr = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(/*user.getresourceurl + id*/);

		HttpResponse response = null;

		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e1) {
			throw new RuntimeException(e1);
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		
		//Type searchHitType = new TypeToken<SearchHit<Movie>>() {}.getType();

		try {
			sr = gson.fromJson(
					new InputStreamReader(response.getEntity().getContent()),
					searchHitType);
		} catch (JsonIOException e) {
			throw new RuntimeException(e);
		} catch (JsonSyntaxException e) {
			throw new RuntimeException(e);
		} catch (IllegalStateException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return sr.getSource();

	}

	/**
	 * Get movies with the specified search string. If the search does not
	 * specify fields, it searches on all the fields.
	 */
	public void searchUser(String searchString, String field) {
		
	}
}
