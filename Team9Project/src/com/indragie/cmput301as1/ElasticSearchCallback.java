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

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Interface for being notified when a request sent via the {@link ElasticSearchAPIClient}
 * succeeds or fails.
 */
public interface ElasticSearchCallback<T extends ElasticSearchDocument> {
	/**
	 * Called when the request succeeds.
	 * @param request The request for which the callback is being called for.
	 * @param response The HTTP response.
	 * @param document See documentation in {@link ElasticSearchAPIClient} for details
	 * on what this parameter will contain.
	 */
	public void onSuccess(Request request, Response response, T document);
	
	/**
	 * Called when the request fails.
	 * @param request The request for which the callback is being called for.
	 * @param response The HTTP response, if the server returned one. If 
	 * the request could not be executed, this parameter will be null and e
	 * will be non-null.
	 * @param e Exception containing information about a failed request when
	 * the request fails to execute due to a connectivity problem or timeout. 
	 * If the request executed but returned a bad HTTP status code, this 
	 * parameter will be null and response will be non-null.
	 */
	public void onFailure(Request request, Response response, IOException e);
}
