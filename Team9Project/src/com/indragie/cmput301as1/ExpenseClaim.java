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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import android.content.res.Resources;

/**
 * Model object representing an expense claim.
 */

public class ExpenseClaim implements Serializable, ElasticSearchDocument {
	private static final long serialVersionUID = -3079284243806354009L;
	
	/**
	 * Type used for indexing on ElasticSearch.
	 */
	public static final String ELASTIC_SEARCH_TYPE = "expense_claim";
	
	//================================================================================
	// Properties
	//================================================================================
	/**
	 * Unique document ID.
	 */
	private ElasticSearchDocumentID documentID = new ElasticSearchDocumentID(
		ElasticSearchConfiguration.INDEX_NAME, 
		ELASTIC_SEARCH_TYPE,
		UUID.randomUUID().toString()
	);
	
	/**
	 * Textual description of the claim.
	 */
	private String description;
	
	/**
	 * Destinations of travel included in the expense claim.
	 */
	private List<Destination> destinations = new ArrayList<Destination>();
	
	/**
	 * Expense items contained in the claim.
	 */
	private List<ExpenseItem> items = new ArrayList<ExpenseItem>();
	
	/**
	 * Tags contained in the claim.
	 */
	private List<Tag> tags = new ArrayList<Tag>();
	
	/**
	 * Comments contained in the claim.
	 */
	private List<Comment> comments = new ArrayList<Comment>();
	
	/**
	 * Possible statuses for an expense claim.
	 */
	public enum Status {
		/**
		 * Default status when the expense is created
		 */
		IN_PROGRESS,
		/**
		 * Expense has been submitted, no edits allowed 
		 */
		SUBMITTED,
		/**
		 * Expense has been returned, edits allowed 
		 */
		RETURNED,
		/** 
		 * Expense has been approved, no edits allowed 
		 */
		APPROVED
	}

	/**
	 * Current status of the expense claim.
	 */
	private Status status = Status.IN_PROGRESS;
	
	/**
	 * Starting date of the claim.
	 */
	private Date startDate;
	
	/**
	 * Ending date of the claim.
	 */
	private Date endDate;
	
	/**
	 * Creation date of the claim.
	 */
	private Date creationDate;

	/**
	 * User who created this claim.
	 */
	private User user;
	
	/**
	 * User who returned or approved claim.
	 */
	private User approver;
	
	//================================================================================
	// Constructors
	//================================================================================

	public ExpenseClaim(String description, Date startDate, Date endDate, User user, Status status) {
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.creationDate = new Date();
		this.user = user;
	}

	//================================================================================
	// Accessors
	//================================================================================
	
	// modified for creation date
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Get the description of the expense claim.
	 * @return The description in type String.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the expense claim
	 * @param description The description of type String.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return The list of destinations for the expense claim.
	 */
	public List<Destination> getDestinations() {
		return Collections.unmodifiableList(destinations);
	}
	
	/**
	 * Sets the list of destinations for the expense claim.
	 * @param destinations List of destinations.
	 */
	public void setDestinations(List<Destination> destinations) {
		this.destinations = destinations;
	}
	
	/**
	 * Adds a destination to the list of destinations for the expense claim.
	 * @param destination The destination to add.
	 */
	public void addDestination(Destination destination) {
		destinations.add(destination);
	}
	
	/**
	 * Replaces an existing destination in the list of destinations for the expense claim.
	 * @param index The index of the destination to replace.
	 * @param destination The destination to replace it with.
	 */
	public void setDestination(int index, Destination destination) {
		destinations.set(index, destination);
	}
	
	/**
	 * Removes a destination from the list of destinations for the expense claim.
	 * @param index The index of the destination to remove.
	 */
	public void removeDestination(int index) {
		destinations.remove(index);
	}

	/**
	 * Retrieves the list of expense items.
	 * @return List of type ExpenseItem.
	 */
	public List<ExpenseItem> getItems() {
		return Collections.unmodifiableList(items);
	}
	
	/**
	 * Sets the list of expense items.
	 * @param items The list of expense items of type ExpenseItem.
	 */
	public void setItems(List<ExpenseItem> items) {
		this.items = items;
	}

	/**
	 * Adds a expense item to the expense claim
	 * @param item The expense item to add.
	 */
	public void addItem(ExpenseItem item) {
		items.add(item);
		Collections.sort(items);
	}

	/**
	 * Sets a expense item at specified position.
	 * @param position The positioned specified. 
	 * @param item The expense item to put. 
	 */
	public void setItem(int position, ExpenseItem item) {
		items.set(position, item);
		Collections.sort(items);
	}

	/**
	 * Removes a expense item at a specified index.
	 * @param index The index of the expense item to remove.
	 */
	public void removeItem(int index) {
		items.remove(index);
	}

	/**
	 * Gets the status of the claim.
	 * @return A status of type Status.
	 */
	public Status getStatus() {
		return status;
	}
	
	/**
	 * Returns the status of the expense claim.
	 * @param resources The resources of the application of type Resource.
	 * @return The status in type String.
	 */
	public String getStatusString(Resources resources) {
		switch (status) {
		case IN_PROGRESS: return resources.getString(R.string.status_in_progress);
		case SUBMITTED: return resources.getString(R.string.status_submitted);
		case RETURNED: return resources.getString(R.string.status_returned);
		case APPROVED: return resources.getString(R.string.status_approved);
		default: return null;
		}
	}
	
	/**
	 * Set the status of the expense claim.
	 * @param status The status you want to set to of type Status.
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	
	/**
	 * Checks if expense claim is editable or not. Will be editable if 
	 * the status of the expense claim is in progress or returned.
	 * @return A boolean representing to determine if editable or not. 
	 */
	public Boolean isEditable() {
		return (status == Status.IN_PROGRESS || status == Status.RETURNED);
	}

	/**
	 * Retrieves the start date of the expense claim.
	 * @return A date object of the start date.
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * Sets the start date of the expense claim.
	 * @param startDate The start date in type date.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Retrieves the end date of the expense claim.
	 * @return The end date in type Date.
	 */
	public Date getEndDate() {
		return endDate;	
	}
	
	/**
	 * Sets the end date of the expense claim.
	 * @param endDate The end date in type Date.
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * @return The user who created the claim.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return Approval comments for the claim.
	 */
	public List<Comment> getComments() {
		return comments;
	}
	
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	/**
	 * Adds approval comments for the claim.
	 * @param comments The approval comments.
	 */
	public void addComments(int position, Comment comment) {
		this.comments.add(position,comment);
	}

	/**
	 * @return The user who approved the claim.
	 */
	public User getApprover() {
		return approver;
	}

	/**
	 * Sets the approver of the claim.
	 * @param approver
	 */
	public void setApprover(User approver) {
		this.approver = approver;
	}
	
	/**
	 * Adds a Tag object to the expense claim.
	 * @param tag The tag to add.
	 */
	public void addTag(Tag tag) {
		tags.add(tag);
	}
	
	/**
	 * Removes a specified Tag object from the expense claim.
	 * @param tag The tag to remove.
	 */
	public void removeTag(Tag tag) {
		tags.remove(tag);
	}
	
	/**
	 * Removes a Tag object from the expense claim at the specified location.
	 * @param index The index of the tag.
	 */
	public void removeTag(int index) {
		tags.remove(index);
	}
	
	/**
	 * Verifies if the expense claim contains specified Tag object.
	 * @param tag The Tag object you want to verify.
	 * @return A boolean if the expense claim contains the tag or not. 
	 */
	public boolean hasTag(Tag tag) {
		return(tags.contains(tag));
	}
	
	/**
	 * Replaces a old tag at a specified index with a new tag you want to replace it with. 
	 * @param index The index of the old tag you wish to remove. 
	 * @param newTag The new Tag object you wish to replace the old one with.
	 */
	public void setTag(int index, Tag newTag) {
		tags.set(index, newTag);
		Collections.sort(tags);
	}
	
	/**
	 * Replaces old tag with new tag you want to replace it with. 
	 * @param oldTag The old tag object you wish to remove. 
	 * @param newTag The new tag object you wish to replace the old one with.
	 */
	public void setTag(Tag oldTag, Tag newTag) {
		int index = tags.indexOf(oldTag);
		tags.set(index, newTag);
		Collections.sort(tags);
	}
	
	/**
	 * Gets the entire list of tags in a expense claim.
	 * @return A List of Tag objects.
	 */
	public List<Tag> getTags() {
		return tags; //Does not return unmodifiable List<Tag>
	}

	/**
	 * @return If the claim has expense items, this method returns a string
	 * containing the totaled amounts of all of the currencies in its expense
	 * items. For example: "USD 5.64, CAD 100.79". If there are no expense
	 * items, this method returns null.
	 */
	public String getSummarizedAmounts() {
		if (items.size() == 0) return "";

		HashMap<CurrencyUnit, Money> unitToMoneyMap = new HashMap<CurrencyUnit, Money>();
		for (ExpenseItem item : items) {
			Money amount = item.getAmount();
			CurrencyUnit unit = amount.getCurrencyUnit();

			Money current = unitToMoneyMap.get(unit);
			if (current == null) {
				current = Money.zero(unit);
			}

			unitToMoneyMap.put(unit, current.plus(item.getAmount()));
		}

		StringBuilder builder = new StringBuilder();
		for (Money amount : unitToMoneyMap.values()) {
			builder.append(amount.toString());
			builder.append("\n");
		}
		
		// Remove trailing newline
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}
	
	//================================================================================
	// ElasticSearchDocument
	//================================================================================
	
	public ElasticSearchDocumentID getDocumentID() {
		return documentID;
	}

	//================================================================================
	// Object
	//================================================================================
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((approver == null) ? 0 : approver.hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((destinations == null) ? 0 : destinations.hashCode());
		result = prime * result
				+ ((documentID == null) ? 0 : documentID.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExpenseClaim other = (ExpenseClaim) obj;
		if (approver == null) {
			if (other.approver != null)
				return false;
		} else if (!approver.equals(other.approver))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (destinations == null) {
			if (other.destinations != null)
				return false;
		} else if (!destinations.equals(other.destinations))
			return false;
		if (documentID == null) {
			if (other.documentID != null)
				return false;
		} else if (!documentID.equals(other.documentID))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (status != other.status)
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}


}
