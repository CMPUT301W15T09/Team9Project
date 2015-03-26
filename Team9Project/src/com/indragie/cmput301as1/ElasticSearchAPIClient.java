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

import java.net.URL;

/**
 * Communicates with the ElasticSearch service.
 */
public class ElasticSearchAPIClient {
	/**
	 * The URL to the ElasticSearch instance.
	 */
	private URL baseURL;
	
	/**
	 * Creates a new instance of {@link ElasticSearchAPIClient}
	 * @param baseURL The URL to the ElasticSearch instance.
	 */
	public ElasticSearchAPIClient(URL baseURL) {
		this.baseURL = baseURL;
	}
	
	/**
	 * Adds a document to the index.
	 * @param document Document model object to add to the index.
	 * @param callback Callback object to be called upon success or failure.
	 * @note When {@link ElasticSearchCallback#onSuccess(Request, Response, T)} is called, 
	 * the document parameter will be the same as the document argument passed to this method.
	 */
	public <T extends ElasticSearchDocument> void add(T document, ElasticSearchCallback<T> callback) {
		
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
}
