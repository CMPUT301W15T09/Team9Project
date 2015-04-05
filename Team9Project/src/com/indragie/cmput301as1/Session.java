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

import java.io.IOException;
import java.util.List;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import android.content.Context;
import android.os.Handler;

/**
 * Encapsulates shared state for the application-wide session.
 */
public class Session implements TypedObserver<CollectionMutation<ExpenseClaim>> {
	//================================================================================
	// Constants
	//================================================================================
	
	/**
	 * Prefix of the file name used for local persistent storage of claims that the user owns.
	 */
	private static final String OWNED_CLAIMS_FILENAME_PREFIX = "owned_claims";
	
	/**
	 * Prefix of the file name used for local persistent storage of claims that the user
	 * can review.
	 */
	private static final String REVIEWAL_CLAIMS_FILENAME_PREFIX = "reviewal_claims";
	
	//================================================================================
	// Singleton
	//================================================================================
	/**
	 * The shared session for the entire application.
	 */
	private static Session sharedSession = null;
	
	/**
	 * @return The shared instance of {@link Session}
	 */
	public static Session getSharedSession() {
		return sharedSession;
	}
	
	/**
	 * Sets the shared session instance.
	 * @param session The shared session.
	 */
	public static void setSharedSession(Session session) {
		sharedSession = session;
	}
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * The current context.
	 */
	private Context context;
	
	/**
	 * The user that the session belongs to.
	 */
	private User user;
	
	/**
	 * Observable model for expense claims that the user owns.
	 */
	private ListModel<ExpenseClaim> ownedClaims;
	
	/**
	 * Observable model for other users' expense claims that the current 
	 * user can see for reviewal purposes.
	 */
	private ListModel<ExpenseClaim> reviewalClaims;
	
	/**
	 * Used to queue up API calls to push changes upstream.
	 */
	private ElasticSearchQueue<ExpenseClaim> pushQueue;
	
	/**
	 * Used to queue up API calls to pull changes downstream.
	 */
	private ElasticSearchQueue<List<ExpenseClaim>> pullQueue;
	
	/**
	 * Used to make API calls to the ElasticSearch servers.
	 */
	private ElasticSearchAPIClient apiClient = new ElasticSearchAPIClient(ElasticSearchConfiguration.getBaseURL());
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link Session}
	 * @param context The current context.
	 * @param user The user that the session belongs to.
	 */
	public Session(Context context, User user) {
		this.user = user;
		this.context = context;
		
		ownedClaims = new ListModel<ExpenseClaim>(modelFilename(OWNED_CLAIMS_FILENAME_PREFIX, user), context);
		ownedClaims.addObserver(this);
		
		reviewalClaims = new ListModel<ExpenseClaim>(modelFilename(REVIEWAL_CLAIMS_FILENAME_PREFIX, user), context);
		reviewalClaims.addObserver(this);
		
		pushQueue = new ElasticSearchQueue<ExpenseClaim>(context);
		pullQueue = new ElasticSearchQueue<List<ExpenseClaim>>(context);
	}
	
	/**
	 * Creates the file name to use for storing model data.
	 * @param prefix The prefix of the filename.
	 * @param user The user that the data belongs to.
	 * @return File name to use for storing model data.
	 */
	private static String modelFilename(String prefix, User user) {
		return prefix + "_" + user.getDocumentID().getID() + ".db";
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return Observable model of the expense claims that are owned by the user. 
	 */
	public ListModel<ExpenseClaim> getOwnedClaims() {
		return ownedClaims;
	}
	
	/**
	 * @return Observable model of the expense claims that are reviewable by the user.
	 * @note There's nothing stopping you from making other modifications to the
	 * expense claims in this list model (other than approval, e.g. editing the
	 * expense claim details). Anything using this model should be careful to only
	 * allow for modifications pertaining to the approval status.
	 */
	public ListModel<ExpenseClaim> getReviewalClaims() {
		return reviewalClaims;
	}
	
	//================================================================================
	// API
	//================================================================================
	
	/**
	 * Loads the list of expense claims that have been created by the user from the server
	 * and automatically updates the {@link Session#ownedClaims} list model.
	 * @param callback Optional callback to be called on failure or on success, after the
	 * list model has been updated.
	 */
	public void loadOwnedClaims(final ElasticSearchAPIClient.APICallback<List<ExpenseClaim>> callback) {
		ElasticSearchAPIClient.APICall<List<ExpenseClaim>> call = 
				ElasticSearchExpenseQueries.expenseClaimsOwnedByUser(user, apiClient);
		loadClaims(call, callback, ownedClaims);
	}
	
	/**
	 * Loads the list of expense claims for reviewal by the user from the server
	 * and automatically updates the {@link Session#reviewalClaims} list model.
	 * @param callback Optional callback to be called on failure or on success, after the
	 * list model has been updated.
	 */
	public void loadReviewalClaims(ElasticSearchAPIClient.APICallback<List<ExpenseClaim>> callback) {
		ElasticSearchAPIClient.APICall<List<ExpenseClaim>> call = 
				ElasticSearchExpenseQueries.expenseClaimsForReviewalByUser(user, apiClient);
		loadClaims(call, callback, reviewalClaims);
	}
	
	/**
	 * Loads a list of expense claims from the server and automatically updates
	 * the specified list model.
	 * @param call The API call used to get a list of expense claims from the server.
	 * @param callback Optional callback to be called on failure or on success, after the
	 * list model has been updated.
	 * @param listModel The list model to update.
	 */
	private void loadClaims(
			ElasticSearchAPIClient.APICall<List<ExpenseClaim>> call, 
			final ElasticSearchAPIClient.APICallback<List<ExpenseClaim>> callback, 
			final ListModel<ExpenseClaim> listModel) {
		
		pullQueue.enqueueCall(call, new ElasticSearchAPIClient.APICallback<List<ExpenseClaim>>() {
			@Override
			public void onSuccess(Response response, final List<ExpenseClaim> results) {
				Handler mainHandler = new Handler(context.getMainLooper());
				mainHandler.post(new Runnable() {
					@Override
					public void run() {
						listModel.replace(results);
					}
				});
				if (callback != null) {
					callback.onSuccess(response, results);
				}
			}

			@Override
			public void onFailure(Request request, Response response, IOException e) {
				if (callback != null) {
					callback.onFailure(request, response, e);
				}
			}
		});
	}
	
	//================================================================================
	// TypedObserver<CollectionMutation<ExpenseClaim>>
	//================================================================================
	
	/* (non-Javadoc)
	 * @see com.indragie.cmput301as1.TypedObserver#update(com.indragie.cmput301as1.TypedObservable, java.lang.Object)
	 */
	@Override
	public void update(TypedObservable<CollectionMutation<ExpenseClaim>> observable,
			CollectionMutation<ExpenseClaim> mutation) {
		ElasticSearchAPIClient.APICall<ExpenseClaim> call = null;
		switch (mutation.getMutationType()) {
		case INSERT:
			call = apiClient.add(((InsertionCollectionMutation<ExpenseClaim>)mutation).getObject());
			break;
		case REMOVE:
			call = apiClient.delete(((RemovalCollectionMutation<ExpenseClaim>)mutation).getObject());
			break;
		case UPDATE:
			call = apiClient.update(((UpdateCollectionMutation<ExpenseClaim>)mutation).getNewObject());
			break;
		default: break;
		}
		if (call != null) {
			pushQueue.enqueueCall(call, null);
		}
	}
}
