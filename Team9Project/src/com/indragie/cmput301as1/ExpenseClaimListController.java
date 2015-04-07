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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;

/**
 * Controller that manages displaying a list of expense claims.
 */
public class ExpenseClaimListController extends TypedObservable<List<ExpenseClaim>> 
	implements TypedObserver<CollectionMutation<ExpenseClaim>> {
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * Model that stores the list of expense claims.
	 */
	private ListModel<ExpenseClaim> listModel;
	
	/**
	 * Model that stores the filtered list of expense claims.
	 */
	private ListModel<ExpenseClaim> filteredListModel;
	
	/**
	 * The list of tags to filter by.
	 */
	private List<Tag> filterTags;
	
	//================================================================================
	// Constructors
	//================================================================================

	/**
	 * Creates an instance of {@link ExpenseClaimListController}
	 * @param context The current context.
	 * @param listModel Model that stores the list of expense claims.
	 */
	public ExpenseClaimListController(Context context, ListModel<ExpenseClaim> listModel) {
		this.listModel = listModel;
		this.listModel.addObserver(this);
		this.filteredListModel = new ListModel<ExpenseClaim>(null, context);
	}
	
	//================================================================================
	// Public API
	//================================================================================
	
	/**
	 * @return The currently active list model, depending on whether
	 * the list has been filtered or not.
	 */
	public ListModel<ExpenseClaim> getListModel() {
		return isFiltered() ? filteredListModel : listModel;
	}
	
	/**
	 * Filter the list to expense claims containing one or more 
	 * of the specified tags.
	 * @param tags The tags to filter the expense claims using.
	 */
	public void filter(List<Tag> tags) {
		if (tags.isEmpty()) {
			filterTags = null;
			filteredListModel.removeAll();
			filteredListModel.deleteObserver(this);
			listModel.addObserver(this);
		} else {
			filterTags = tags;
			ArrayList<ExpenseClaim> filteredClaims = new ArrayList<ExpenseClaim>();
			for (ExpenseClaim claim : listModel.getItems()) {
				if (expenseClaimPassesFilter(claim)) {
					filteredClaims.add(claim);
				}
			}
			filteredListModel.addObserver(this);
			listModel.deleteObserver(this);
			filteredListModel.replace(filteredClaims);
		}
	}
	
	/**
	 * @return Whether the list has been filtered by tags.
	 */
	public boolean isFiltered() {
		return (filterTags != null);
	}
	
	/**
	 * @return The list of tags used to filter the list, or an empty list if
	 * the list is not filtered.
	 */
	public List<Tag> getFilterTags() {
		return isFiltered() ? filterTags : new ArrayList<Tag>();
	}
	
	/**
	 * Removes a tag filter, if there is one.
	 */
	public void removeFilter() {
		if (isFiltered()) {
			filter(new ArrayList<Tag>());
		}
	}
	
	/**
	 * Sorts the expense claims using the specified comparator.
	 * @param comparator The comparator to sort using.
	 */
	public void sort(Comparator<ExpenseClaim> comparator) {
		listModel.setComparator(comparator);
		filteredListModel.setComparator(comparator);
	}
	
	/**
	 * Gets the expense claim at the specified index.
	 * @param index The index of the expense claim.
	 * @return The expense claim.
	 */
	public ExpenseClaim get(int index) {
		return getListModel().getItems().get(index);
	}
	
	/**
	 * Adds an expense claim to the list of expense claims.
	 * @param claim The expense claim to add.
	 */
	public void add(ExpenseClaim claim) {
		listModel.add(claim);
		if (isFiltered()) {
			if (expenseClaimPassesFilter(claim)) {
				filteredListModel.add(claim);
			} else {
				removeFilter();
			}
		}
	}
	
	/**
	 * Updates an existing expense claim in the list of expense claims.
	 * @param index The index of the expense claim to update.
	 * @param claim The expense claim to replace the existing one with.
	 */
	public void set(int index, ExpenseClaim claim) {
		if (isFiltered()) {
			filteredListModel.set(index, claim);
			
			int originalIndex = indexInListModel(claim, listModel);
			if (originalIndex != -1) {
				listModel.set(originalIndex, claim);
			}
		} else {
			listModel.set(index, claim);
		}
	}
	
	/**
	 * Removes the expense claim at the specified index.
	 * @param index The index of the expense claim to remove.
	 */
	public void remove(int index) {
		if (isFiltered()) {
			ExpenseClaim claim = filteredListModel.getItems().get(index);
			filteredListModel.remove(index);
			
			int originalIndex = indexInListModel(claim, listModel);
			if (originalIndex != -1) {
				listModel.remove(originalIndex);
			}
		} else {
			listModel.remove(index);
		}
	}
	
	/**
	 * Replaces the list of expense claims with a new list.
	 * @param claims The new list of expense claims.
	 */
	public void replace(List<ExpenseClaim> claims) {
		removeFilter();
		listModel.replace(claims);
	}
	
	/**
	 * Process mutations to tags from the {@link ManageTagsActivity}
	 * @param mutations The mutations to process.
	 */
	public void processTagMutations(ArrayList<ManageTagsActivity.TagMutation> mutations) {
		SparseArray<ExpenseClaim> modifiedClaims = new SparseArray<ExpenseClaim>();
		
		for (ManageTagsActivity.TagMutation mutation : mutations) {
			Tag tag = mutation.getOldTag();
			int i = 0;
			for (ExpenseClaim claim : listModel.getItems()) {
				int tagIndex = -1;
				if (isFiltered()) {
					tagIndex = filterTags.indexOf(tag);
				}
				
				switch (mutation.getMutationType()) {
				case DELETE:
					if (claim.hasTag(tag)) {
						claim.removeTag(tag);
					}
					if (tagIndex != -1) {
						filterTags.remove(tagIndex);
					}
					break;
				case EDIT:
					Tag newTag = mutation.getNewTag();
					if (claim.hasTag(tag)) {
						claim.setTag(claim.getTags().indexOf(tag), newTag);
					}
					if (tagIndex != -1) {
						filterTags.set(tagIndex, newTag);
					}
					break;
				}
				
				modifiedClaims.append(i, claim);
				i++;
			}
		}
		
		for (int i = 0; i < modifiedClaims.size(); i++) {
			ExpenseClaim claim = modifiedClaims.valueAt(i);
			listModel.set(modifiedClaims.keyAt(i), claim);
			if (isFiltered()) {
				int filteredIndex = indexInListModel(claim, filteredListModel);
				if (filteredIndex != -1) {
					filteredListModel.set(filteredIndex, claim);
				}
			}
		}
	}
	
	//================================================================================
	// TypedObserver<CollectionMutation<ExpenseClaim>>
	//================================================================================
	
	@Override
	public void update(TypedObservable<CollectionMutation<ExpenseClaim>> observable, 
			CollectionMutation<ExpenseClaim> mutation) {
		setChanged();
		notifyObservers(getListModel().getItems());
	}
	
	//================================================================================
	// Private
	//================================================================================
	
	/**
	 * Checks whether an expense claim passes a tag filter.
	 * @param claim The expense claim.
	 * @return Whether the expense claim passes the tag filter.
	 */
	private boolean expenseClaimPassesFilter(ExpenseClaim claim) {
		if (isFiltered()) {
			for (Tag tag : filterTags) {
				if (claim.hasTag(tag)) {
					return true;
				}
			}
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Finds the index of the expense claim in the specified list model
	 * by using its document ID for equality comparison rather than the
	 * expense claim itself.
	 * @param claim The expense claim.
	 * @param listModel The list model to search.
	 * @return The index of the expense claim in the specified list model, or -1
	 * if it could not be found.
	 */
	private static int indexInListModel(ExpenseClaim claim, ListModel<ExpenseClaim> listModel) {
		ElasticSearchDocumentID documentID = claim.getDocumentID();
		int i = 0;
		for (ExpenseClaim originalClaim : listModel.getItems()) {
			if (originalClaim.getDocumentID().equals(documentID)) {
				return i;
			}
			i++;
		}
		return -1;
	}
}
