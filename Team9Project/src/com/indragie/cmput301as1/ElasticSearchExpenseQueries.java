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

/*
 * Mapping:
	 {
	    "expense_claim": {
	        "properties": {
	            "items": {
	                "type": "nested",
	                "properties": {
	                    "receipt_base64": {
	                        "include_in_all": false,
	                        "index": "no",
	                        "type": "string"
	                    }
	                }
	            },
	            "user": {
	                "type": "nested",
	                "properties": {
	                    "documentID": {
	                        "type": "nested"
	                    }
	                }
	            },
	            "approver": {
	                "type": "nested",
	                "properties": {
	                    "documentID": {
	                        "type": "nested"
	                    }
	                }
	            }
	        }
	    }
	}
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
		/*
		 * {
			  "query": {
			    "filtered": {
			      "filter": {
			        "nested": {
			          "path": "user.documentID",
			          "query":{
			            "filtered": {
			              "filter": {
			                "term": {"user.documentID.id": "<user ID>"}
			              }
			            }
			          }
			        }
			      }
			    }
			  }
			}
		 */
		String queryJSON = "{\"query\":{\"filtered\":{\"filter\":{\"nested\":{\"path\":\"user.documentID\",\"query\":{\"filtered\":{\"filter\":{\"term\":{\"user.documentID.id\":\"" + user.getDocumentID().getID() + "\"}}}}}}}}}";
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
	 * specified user and have a status of "Submitted"
	 * @param user The user for which to search for expense claims to review.
	 * @param client The API client.
	 * @return An API call representing the search query.
	 */
	public static ElasticSearchAPIClient.APICall<List<ExpenseClaim>> 
		expenseClaimsForReviewalByUser(User user, ElasticSearchAPIClient client) {
		String id = user.getDocumentID().getID();
		/*
		 * {
			  "query": {
			    "filtered": {
			      "filter": {
			        "bool": {
			          "must": [
			            {
			              "query": {
			                "match": {"status": "SUBMITTED"}
			              }
			            }
			          ],
			          "must_not": [
			          {
			            "nested": {
			              "path": "user.documentID",
			              "query": {
			                "filtered": {
			                  "filter": {
			                    "term": { "user.documentID.id": "<user id>" }
			                  }
			                }
			              }
			            }
			          }
			          ],
			          "should": [
			          { "missing" : {"field": "approver"}},
			          {
			            "nested": {
			              "path": "approver.documentID",
			              "query": {
			                "filtered": {
			                  "filter": {
			                    "term": { "approver.documentID.id": "<user id>" }
			                  }
			                }
			              }
			            }
			          }
			          ]
			        }
			      }
			    }
			  }
			}
		 */
		String queryJSON = "{\"query\":{\"filtered\":{\"filter\":{\"bool\":{\"must\":[{\"query\":{\"match\":{\"status\":\"SUBMITTED\"}}}],\"must_not\":[{\"nested\":{\"path\":\"user.documentID\",\"query\":{\"filtered\":{\"filter\":{\"term\":{\"user.documentID.id\":\"" + id + "\"}}}}}}],\"should\":[{\"missing\":{\"field\":\"approver\"}},{\"nested\":{\"path\":\"approver.documentID\",\"query\":{\"filtered\":{\"filter\":{\"term\":{\"approver.documentID.id\":\"" + id + "\"}}}}}}]}}}}}";
		return client.search(
			ElasticSearchConfiguration.INDEX_NAME, 
			ExpenseClaim.ELASTIC_SEARCH_TYPE, 
			queryJSON, 
			ExpenseClaim.class
		);
	}
}
