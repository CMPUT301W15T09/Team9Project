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

import java.text.DateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;

/**
 * Handles the transformation of data from {@link ExpenseClaim} model
 * objects into representations suitable for display in the UI presented by
 * {@link ExpenseClaimDetailActivity}
 */
public class ExpenseClaimDetailController implements TypedObserver<Object> {
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * The current context.
	 */
	private Context context;
	
	/**
	 * Observable model for expense claim details.
	 */
	private ExpenseClaimDetailModel model;
	
	/**
	 * Adapter that adapts expense items, destinations, and tags
	 * to the list view.
	 */
	private SectionedListAdapter<DetailItem> adapter;
	
	/**
	 * Section of the list view that displays destinations.
	 */
	private ListSection<DetailItem> destinationsSection;
	
	/**
	 * Section of the list view that displays expense items.
	 */
	private ListSection<DetailItem> expenseItemsSection;
	
	/**
	 * Section of the list view that displays tags.
	 */
	private ListSection<DetailItem> tagsSection;
	
	//================================================================================
	// Classes
	//================================================================================
	
	/**
	 * Class that can hold the different types of objects that are 
	 * displayed inside the detail {@link ListView}
	 */
	public static class DetailItem {
		//================================================================================
		// Properties
		//================================================================================
		/**
		 * Possible types for the item.
		 */
		public enum ItemType {
			/**
			 * Model object is an instance of {@link Destination}
			 */
			DESTINATION,
			/**
			 * Model object is an instance of {@link Tag}
			 */
			TAG,
			/**
			 * Model object is an instance of {@link ExpenseItem}
			 */
			EXPENSE_ITEM
		}
		
		/**
		 * The type of the item.
		 */
		private ItemType type;
		
		/**
		 * Model object. If the type is 
		 */
		private Object model;
		
		//================================================================================
		// Constructors
		//================================================================================
		
		/**
		 * Creates a new instance of {@link DetailItem}
		 * @param type The type of the model object.
		 * @param model The model object.
		 */
		DetailItem(ItemType type, Object model) {
			this.type = type;
			this.model = model;
		}
		
		//================================================================================
		// Accessors
		//================================================================================
		
		public ItemType getType() {
			return type;
		}
		
		public Object getModel() {
			return model;
		}
	}
	
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link ExpenseClaimDetailController}
	 * @param context The current context.
	 * @param claim Observable model for expense claim details.
	 */
	public ExpenseClaimDetailController(Context context, ExpenseClaimDetailModel model) {
		this.context = context;
		this.model = model;
		model.addObserver(this);
		
		Resources resources = context.getResources();
		
		destinationsSection = new ListSection<DetailItem>(
			resources.getString(R.string.destinations_title),
			getDestinationDetailItems(),
			new DestinationViewConfigurator()
		);
		expenseItemsSection = new ListSection<DetailItem>(
			resources.getString(R.string.items_title), 
			getExpenseItemDetailItems(), 
			new ExpenseItemViewConfigurator()
		);
		
		tagsSection = new ListSection<DetailItem>(
			resources.getString(R.string.tags_title),
			getTagDetailItems(),
			new TagViewConfigurator()
		);
		
		ArrayList<ListSection<DetailItem>> sections = new ArrayList<ListSection<DetailItem>>();
		sections.add(destinationsSection);
		sections.add(expenseItemsSection);
		sections.add(tagsSection);
		
		XMLSectionHeaderConfigurator headerConfigurator = new XMLSectionHeaderConfigurator(R.layout.list_header, R.id.title_label);
		adapter = new SectionedListAdapter<DetailItem>(context, sections, headerConfigurator);
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * Creates a plain text representation of the expense claim;
	 * @return Plain text representation of the expense claim suitable for sending in an email.
	 */
	public String getPlainText() {
		ExpenseClaim claim = model.getExpenseClaim();
		Resources resources = context.getResources();
		StringBuilder builder = new StringBuilder(claim.getName() + "\n");
		String description = claim.getDescription();
		if (description.length() > 0) {
			builder.append(resources.getString(R.string.description) + ": " + description + "\n");
		}
		DateFormat dateFormat = DateFormat.getDateInstance();
		builder.append(resources.getString(R.string.dates) + ": " + dateFormat.format(claim.getStartDate()) + " - " + dateFormat.format(claim.getEndDate()) + "\n");
		builder.append(resources.getString(R.string.status) + ": " + claim.getStatusString(resources) + "\n\n");
		builder.append(resources.getString(R.string.expense_items) + ":\n");
		for (ExpenseItem item : claim.getItems()) {
			builder.append(getItemPlainText(item));
		}
		return builder.toString();
	}
	
	/**
	 * @return The adapter used to display {@link DetailItem} objects in a {@link ListView}
	 */
	public SectionedListAdapter<DetailItem> getAdapter() {
		return adapter;
	}
	
	/**
	 * @param position The position of the item in the {@link ListView}
	 * @return The {@link SectionedListIndex} corresponding to the position.
	 */
	public SectionedListIndex getSectionedIndex(int position) {
		return adapter.getSectionedIndex(position);
	}
	
	/**
	 * @param position The position of the item in the {@link ListView}
	 * @return The {@link DetailItem.ItemType} corresponding to the position if
	 * the item is valid, or null if the item is a section header.
	 */
	public DetailItem.ItemType getItemType(int position) {
		DetailItem item = adapter.getTypedItem(position);
		if (item == null) {
			return null;
		} else {
			return item.getType();
		}
	}
	
	/**
	 * @param index The index of the {@link Destination} relative to its section.
	 * @return The {@link Destination} at the specified index.
	 */
	public Destination getDestination(int index) {
		return (Destination)destinationsSection.get(index).getModel();
	}
	
	/**
	 * @param index The index of the {@link ExpenseItem} relative to its section.
	 * @return The {@link ExpenseItem} at the specified index.
	 */
	public ExpenseItem getExpenseItem(int index) {
		return (ExpenseItem)expenseItemsSection.get(index).getModel();
	}
	
	/**
	 * @param index The index of the {@link Tag} relative to its section.
	 * @return The {@link Tag} at the specified index.
	 */
	public Tag getTag(int index) {
		return (Tag)tagsSection.get(index).getModel();
	}
	
	/**
	 * Removes the item at the specified position.
	 * @param position The position of the item to remove.
	 */
	public void remove(int position) {
		DetailItem.ItemType type = getItemType(position);
		SectionedListIndex index = getSectionedIndex(position);
		
		switch (type) {
		case DESTINATION:
			model.removeDestination(index.getItemIndex());
			break;
		case TAG:
			model.removeTag(index.getItemIndex());
			break;
		case EXPENSE_ITEM:
			model.removeItem(index.getItemIndex());
			break;
		}
	}
	
	
	//================================================================================
	// Private
	//================================================================================
	
	private static final String BULLET = "\u2022 ";
	private static final String INDENTED_BULLET = "\t\u25e6 ";
	
	/**
	 * Creates a plain text representation of an expense item.
	 * @param item The expense item for which to get a plain text representation.
	 * @return Plain text representation of the expense item suitable for sending in an email.
	 */
	private String getItemPlainText(ExpenseItem item) {
		Resources resources = context.getResources();
		StringBuilder builder = new StringBuilder(BULLET + item.getName() + "\n");
		String description = item.getDescription();
		if (description.length() > 0) {
			builder.append(attributeString(resources.getString(R.string.description), description));
		}
		builder.append(attributeString(resources.getString(R.string.category), item.getCategory()));
		builder.append(attributeString(resources.getString(R.string.amount), item.getAmount().toString()));
		builder.append(attributeString(resources.getString(R.string.date), DateFormat.getDateInstance().format(item.getDate())));
		return builder.toString();
	}
	
	/**
	 * Helper function used in getPlainText() to create strings representing
	 * each of the attributes of the expense item.
	 */
	private String attributeString(String name, String value) {
		return INDENTED_BULLET + name + ": " + value + "\n";
	}
	
	/**
	 * @return The destinations for the expense claim, wrapped in {@link DetailItem} objects.
	 */
	private ArrayList<DetailItem> getDestinationDetailItems() {
		ArrayList<DetailItem> items = new ArrayList<DetailItem>();
		for (Destination destination : model.getExpenseClaim().getDestinations()) {
			items.add(new DetailItem(DetailItem.ItemType.DESTINATION, destination));
		}
		return items;
	}
	
	/**
	 * @return The expense items for the expense claim, wrapped in {@link ExpenseItem} objects.
	 */
	private ArrayList<DetailItem> getExpenseItemDetailItems() {
		ArrayList<DetailItem> items = new ArrayList<DetailItem>();
		for (ExpenseItem item : model.getExpenseClaim().getItems()) {
			items.add(new DetailItem(DetailItem.ItemType.EXPENSE_ITEM, item));
		}
		return items;
	}
	
	/**
	 * @return The tags for the expense claim, wrapped in {@link Tag} objects.
	 */
	private ArrayList<DetailItem> getTagDetailItems() {
		ArrayList<DetailItem> items = new ArrayList<DetailItem>();
		for (Tag tag : model.getExpenseClaim().getTags()) {
			items.add(new DetailItem(DetailItem.ItemType.TAG, tag));
		}
		return items;
	}

	//================================================================================
	// Observer
	//================================================================================
	
	@Override
	public void update(TypedObservable<Object> observable, Object object) {
		destinationsSection.setItems(getDestinationDetailItems());
		expenseItemsSection.setItems(getExpenseItemDetailItems());
		tagsSection.setItems(getTagDetailItems());
		adapter.noteSectionsChanged();
	}
}
