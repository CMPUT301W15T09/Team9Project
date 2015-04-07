/* 
 * Copyright (C) 2015 Steven Chang
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

import java.io.Serializable;
import java.util.Date;
import android.content.res.Resources;

public class Comment implements Serializable, ElasticSearchDocument {
	private static final long serialVersionUID = -8266484958579212124L;
	/**
	 * Type used for indexing on ElasticSearch.
	 */
	public static final String ELASTIC_SEARCH_TYPE = "comment";
	
	/**
	 * Possible statuses for an comment.
	 */
	public enum Status {
		/**
		 * Expense has been returned, comment edits allowed 
		 */
		RETURNED,
		/** 
		 * Expense has been approved, no comment edits allowed 
		 */
		APPROVED
	}
	
	//================================================================================
	// Properties
	//================================================================================
	/**
	 * Unique document ID.
	 */
	public ElasticSearchDocumentID getDocumentID() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * User who returned or approved the comment and claim.
	 */
	private User approver;
	
	/**
	 * Textual comment for approver's use.
	 */
	private String comment;
	
	/**
	 * Current status of the expense comment.
	 */
	private Status status;

	/**
	 * Creation date of the comment.
	 */
	private Date creationDate;
	

	
	//================================================================================
	// Constructors
	//================================================================================

	public Comment(User user, String comment, Date creationDate, Status status) {
		this.approver = user;
		this.comment = comment;
		this.creationDate = creationDate;
		this.status = status;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * Get the creation date of the comment.
	 * @param creationDate The creationDate in type Date.
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	
	/**
	 * Get the comment.
	 * @param comments The approver comment in type String.
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Gets the status of the comment.
	 * @return A status of type Status.
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * Returns the status of the comment.
	 * @param resources The resources of the application of type Resource.
	 * @return The status in type String.
	 */
	public String getStatusString(Resources resources) {
		switch (status) {
		case RETURNED: 
			return resources.getString(R.string.status_returned);
		case APPROVED:
			return resources.getString(R.string.status_approved);
		default: 
			return null;
		}
	}
	
	/**
	 * @return Get the user who made the comment.
	 */
	public User getApprover() {
		return approver;
	}

	//================================================================================
	// Object
	//================================================================================
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((approver == null) ? 0 : approver.hashCode());
		result = prime * result
				+ ((comment == null) ? 0 : comment.hashCode());
		result = prime * result 
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result
				+ ((status == null)? 0: status.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());

		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (approver == null)
		{
			if (other.approver != null)
				return false;
		} else if (!approver.equals(other.approver))
			return false;
		if (comment == null)
		{
			if (other.comment !=null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (creationDate == null)
		{ 
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (status == null)
		{ 
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status)) 
			return false;
		return true;
	}


}