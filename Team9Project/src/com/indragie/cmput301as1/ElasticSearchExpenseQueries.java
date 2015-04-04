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

import java.util.List;

/**
 * Queries used for searching expense claims via ElasticSearch.
 */
public class ElasticSearchExpenseQueries {
	/**
	 * Creates a search query for expense claims owned by a particular user.
	 * @param user The user for which to search for expense claims.
	 * @param client The API client.
	 * @return An API call representing the search query.
	 */
	public static ElasticSearchAPIClient.APICall<List<ExpenseClaim>> 
		expenseClaimsOwnedByUser(User user, ElasticSearchAPIClient client) {
		String queryJSON = "{"
			+ " 	\"filtered\": {"
			+ " 		\"filter\": {"
			+ "				\"term\": {"
			+ "					\"user.documentID.id\": \"" + user.getDocumentID().getID() + "\""
			+ "				}"
			+ "			}"
			+ " 	}"
			+ "}";
		return client.search(
			ElasticSearchConfiguration.INDEX_NAME, 
			ExpenseClaim.ELASTIC_SEARCH_TYPE, 
			queryJSON, 
			ExpenseClaim.class
		);
	}
	
	/**
	 * Creates a search query for expense claims to be reviewed by a particular user.
	 * The list of returned expense claims are those that were not created by the
	 * specified user and have a status of "Submitted
	 * @param user The user for which to search for expense claims to review.
	 * @param client The API client.
	 * @return An API call representing the search query.
	 */
	public static ElasticSearchAPIClient.APICall<List<ExpenseClaim>> 
		expenseClaimsForReviewalByUser(User user, ElasticSearchAPIClient client) {
		String queryJSON = "{"
			+ " 	\"filtered\": {"
			+ " 		\"filter\": {"
			+ "				\"and\": ["
			+ "					\"term\": {"
			+ "						\"status\": \"SUBMITTED\""
			+ "					},"
			+ "					\"not\": {"
			+ "						\"term\" {"
			+ "							\"user.documentID.id\": \"" + user.getDocumentID().getID() + "\""
			+ "						}"
			+ "					}"
			+ "				]"
			+ "			}"
			+ " 	}"
			+ "}";
		return client.search(
			ElasticSearchConfiguration.INDEX_NAME, 
			ExpenseClaim.ELASTIC_SEARCH_TYPE, 
			queryJSON, 
			ExpenseClaim.class
		);
	}
}
