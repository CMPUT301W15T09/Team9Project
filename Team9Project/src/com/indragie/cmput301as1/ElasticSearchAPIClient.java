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

import android.text.TextUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Communicates with the ElasticSearch service.
 */
public class ElasticSearchAPIClient {
	//================================================================================
	// Constants
	//================================================================================
	/**
	 * Media type for JSON data.
	 */
	private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

	//================================================================================
	// Properties
	//================================================================================
	/**
	 * The URL to the ElasticSearch instance.
	 */
	private URL baseURL;

	/**
	 * Client used to create and configure HTTP connections.
	 */
	private OkHttpClient client = new OkHttpClient();

	/**
	 * Used for serializing objects to and from JSON.
	 */
	private Gson gson = new Gson();

	//================================================================================
	// Interfaces
	//================================================================================

	/**
	 * Interface for being notified when a request sent via the {@link ElasticSearchAPIClient}
	 * succeeds or fails.
	 */
	public interface Callback<T extends ElasticSearchDocument> {
		/**
		 * Called when the request succeeds.
		 * @param request The request for which the callback is being called for.
		 * @param response The HTTP response.
		 * @param document See documentation in {@link ElasticSearchAPIClient} for details
		 * on what this parameter will contain.
		 */
		public void onSuccess(Response response, T document);

		/**
		 * Called when the request fails.
		 * @param request The request for which the callback is being called for.
		 * If the request could not be created, this parameter will be null and e
		 * will be non-null.
		 * @param response The HTTP response, if the server returned one. If 
		 * the request could not be executed, this parameter will be null and e
		 * will be non-null.
		 * @param e Exception containing information about a failed request when
		 * the request either cannot be created because of a malformed URL or fails 
		 * to execute due to a connectivity problem or timeout. If the request 
		 * executed but returned a bad HTTP status code, this parameter will be null 
		 * and response will be non-null.
		 */
		public void onFailure(Request request, Response response, IOException e);
	}

	/**
	 * Interface for an object that can create an ElasticSearch document
	 * from an HTTP response.
	 * @param <T> The type of the document.
	 */
	private interface DocumentFactory<T> {
		/**
		 * Creates an ElasticSearch document from an HTTP response.
		 * @param response The HTTP response.
		 * @return An ElasticSearch document.
		 */
		T documentFromResponse(Response response);
	}

	/**
	 * A document factory that does nothing with the response and returns the 
	 * original document from the factory method.
	 * @param <T> The type of the document.
	 */
	private class IdentityDocumentFactory<T> implements DocumentFactory<T> {
		/** The original document */
		private T document;

		/**
		 * Creates a new instance of {@link ElasticSearchIdentityDocumentFactory<T>}
		 * @param document The original document.
		 */
		IdentityDocumentFactory(T document) {
			this.document = document;
		}

		@Override
		public T documentFromResponse(Response response) {
			return document;
		}
	}

	/**
	 * A document factory that extracts the source JSON document from the
	 * ElasticSearch API response and converts it to a document model object.
	 * @param <T> The type of the document.
	 */
	private class JSONDocumentFactory<T> implements DocumentFactory<T> {
		@Override
		public T documentFromResponse(Response response) {
			JsonParser parser = new JsonParser();
			try {
				JsonElement rootElement = parser.parse(response.body().string());
				if (rootElement.isJsonObject()) {
					JsonObject rootObject = rootElement.getAsJsonObject();
					JsonElement sourceElement = rootObject.get("_source");
					Type docType = new TypeToken<T>() {}.getType();
					return gson.fromJson(sourceElement, docType);
				}

			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	//================================================================================
	// Constructors
	//================================================================================
	/**
	 * Creates a new instance of {@link ElasticSearchAPIClient}
	 * @param baseURL The URL to the ElasticSearch instance.
	 */
	public ElasticSearchAPIClient(URL baseURL) {
		this.baseURL = baseURL;
	}

	//================================================================================
	// Public API
	//================================================================================
	/**
	 * Adds a document to the index.
	 * @param document Document model object to add to the index.
	 * @param callback Callback object to be called upon success or failure.
	 * @throws MalformedURLException 
	 * @note When {@link ElasticSearchCallback#onSuccess(Request, Response, T)} is called, 
	 * the document parameter will be the same as the document argument passed to this method.
	 */
	public <T extends ElasticSearchDocument> void add(T document, Callback<T> callback) {
		try {
			Request request = new Request.Builder()
			.url(constructDocumentURL(document.getDocumentID()))
			.post(RequestBody.create(MEDIA_TYPE_JSON, documentToJSON(document)))
			.build();
			client.newCall(request).enqueue(wrapCallback(callback, new IdentityDocumentFactory<T>(document)));
		} catch (MalformedURLException e) {
			callback.onFailure(null, null, e);
		}
	}

	/**
	 * Retrieves a document from the index.
	 * @param docID The document ID of the document to retrieve.
	 * @param callback Callback object to be called upon success or failure. 
	 * @note When {@link ElasticSearchCallback#onSuccess(Request, Response, T)} is called,
	 * the document argument will be the document model object, deserialized from JSON.
	 */
	public <T extends ElasticSearchDocument> void get(ElasticSearchDocumentID docID, ElasticSearchCallback<T> callback) {

	}

	/**
	 * Updates an existing document in the index.
	 * @param newDocument The updated version of the document. This will be used to 
	 * replace the existing document with the same document ID.
	 * @param callback Callback object to be called upon success or failure.
	 * @note When {@link ElasticSearchCallback#onSuccess(Request, Response, T)} is called, 
	 * the document argument will be the same as the newDocument argument passed to this method.
	 */
	public <T extends ElasticSearchDocument> void update(T newDocument, ElasticSearchCallback<T> callback) {

	}

	/**
	 * Deletes an existing document from the index.
	 * @param document The document to delete.
	 * @param callback Callback object to be called upon success or failure.
	 * @note When {@link ElasticSearchCallback#onSuccess(Request, Response, T)} is called, 
	 * the document argument will be null.
	 */
	public <T extends ElasticSearchDocument> void delete(T document, ElasticSearchCallback<T> callback) {

	}

	//================================================================================
	// Private
	//================================================================================

	/**
	 * Wraps {@link ElasticSearchAPIClient.Callback} in {@link com.squareup.okhttp.Callback}
	 * @param callback The callback to wrap.
	 * @param documentFactory An optional document factory for deserializing a document from
	 * an HTTP response.
	 * @return The callback wrapped in an instance of {@link com.squareup.okhttp.Callback}
	 */
	private <T extends ElasticSearchDocument> com.squareup.okhttp.Callback wrapCallback(final Callback<T> callback, final DocumentFactory<T> documentFactory) {
		return new com.squareup.okhttp.Callback() {
			@Override
			public void onFailure(Request request, IOException e) {
				callback.onFailure(request, null, e);
			}

			@Override
			public void onResponse(Response response) throws IOException {
				if (response.isSuccessful()) {
					T document = null;
					if (documentFactory != null) {
						document = documentFactory.documentFromResponse(response);
					}
					callback.onSuccess(response, document);
				} else {
					callback.onFailure(response.request(), response, null);
				}
			}
		};
	}

	/**
	 * Constructs the ElasticSearch URL for a document.
	 * @param docID The ID of the document.
	 * @return A URL to the ElasticSearch document.
	 * @throws MalformedURLException If the URL could not be constructed.
	 */
	private URL constructDocumentURL(ElasticSearchDocumentID docID) throws MalformedURLException {
		String tokens[] = new String[] { docID.getIndex(), docID.getType(), docID.getID() };
		String path = "/" + TextUtils.join("/", tokens);
		return new URL(baseURL, path);
	}

	/**
	 * Converts a document model object to JSON for use with the ElasticSearch API.
	 * @param document The document model object.
	 * @return JSON representation of the document as a string.
	 */
	private <T extends ElasticSearchDocument> String documentToJSON(T document) {
		Type docType = new TypeToken<T>() {}.getType();
		return gson.toJson(document, docType);
	}
}
