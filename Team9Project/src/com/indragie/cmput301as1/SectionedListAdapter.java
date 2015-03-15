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
import java.util.HashSet;
import java.util.List;

import com.indragie.cmput301as1.ListSection.ViewConfigurator;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * A list view adapter that displays data grouped by section. Sections are defined 
 * using instances of {@link ListSection}.
 */
public class SectionedListAdapter<T> extends BaseAdapter {
	//================================================================================
	// Constants
	//================================================================================
	/**
	 * Used as a placeholder index for list items that are section headers and
	 * not actual content.
	 */
	private static final int NOT_AN_ITEM_INDEX = -1;
	
	//================================================================================
	// Properties
	//================================================================================
	
	/**
	 * The current context.
	 */
	private Context context;
	
	/**
	 * Sections to display in the adapter.
	 */
	private List<ListSection<T>> sections;
	
	/**
	 * Flattened representation of sections.
	 */
	private ArrayList<ItemMetadata> flattenedItems;
	
	/**
	 * Configurator for section header views.
	 */
	private ViewConfigurator<String> headerConfigurator;
	
	/**
	 * Maps list positions to sectioned indices.
	 */
	private SparseArray<SectionedListIndex> indexMapping = new SparseArray<SectionedListIndex>();
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link SectionedListAdapter}
	 * @param context The current context.
	 * @param sections The sections to display in the adapter. The adapter does not
	 * perform a deep copy of the sections. Thus, it is legal to modify the {@link ListSection}
	 * objects that are passed to this adapter after initialization. After sections have
	 * been modified, {@link #noteSectionsChanged()} must be called to update internal
	 * state and notify observers to reload the data set.
	 * @param headerConfigurator View configurator used to create and configure section headers.
	 */
	public SectionedListAdapter(Context context, List<ListSection<T>> sections, ViewConfigurator<String> headerConfigurator) {
		this.context = context;
		this.sections = sections;
		this.headerConfigurator = headerConfigurator;
		flattenSections();
	}
	
	//================================================================================
	// API
	//================================================================================
	
	/**
	 * Called when the sections are mutated. This updates the adapter's internal
	 * state and also notifies all observers that the data set has changed.
	 */
	public void noteSectionsChanged() {
		flattenSections();
		notifyDataSetChanged();
	}
	
	/**
	 * Strongly typed version of {@link #getItem(int)}
	 * @param position The position of the item in the list.
	 * @return The item, or null if the position is for a section header.
	 */
	public T getTypedItem(int position) {
		ItemMetadata metadata = flattenedItems.get(position);
		if (metadata.isSectionHeader) {
			return null;
		} else {
			return metadata.object;
		}
	}
	
	//================================================================================
	// BaseAdapter
	//================================================================================
	
	@Override
	public int getCount() {
		return flattenedItems.size();
	}
	
	@Override
	public Object getItem(int position) {
		return getTypedItem(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		ItemMetadata metadata = flattenedItems.get(position);
		if (metadata.isSectionHeader) {
			return headerConfigurator.getViewTypeCode();
		} else {
			return metadata.viewConfigurator.getViewTypeCode();
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemMetadata metadata = flattenedItems.get(position);
		if (metadata.isSectionHeader) {
			if (convertView == null) {
				convertView = headerConfigurator.createView(context, parent);
			}
			headerConfigurator.configureView(context, convertView, metadata.sectionTitle);
		} else {
			ViewConfigurator<T> configurator = metadata.viewConfigurator;
			if (convertView == null) {
				convertView = configurator.createView(context, parent);
			}
			configurator.configureView(context, convertView, metadata.object);
		}
		return convertView;
	}
	
	@Override
	public int getViewTypeCount() {
		HashSet<Integer> types = new HashSet<Integer>();
		types.add(headerConfigurator.getViewTypeCode());
		for (ListSection<T> section : sections) {
			types.add(section.getViewConfigurator().getViewTypeCode());
		}
		return types.size();
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}
	
	@Override
	public boolean isEmpty() {
		return getCount() == 0;
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return indexMapping.get(position).getItemIndex() != NOT_AN_ITEM_INDEX;
	}
	
	//================================================================================
	// Private
	//================================================================================
	
	/**
	 * Creates a flattened representation of the section contents and
	 * updates the position -> {@link SectionedListIndex} mapping.
	 */
	private void flattenSections() {
		ArrayList<ItemMetadata> items = new ArrayList<ItemMetadata>();
		int flattenedIndex = 0, sectionIndex = 0, rowIndex = 0;
		for (ListSection<T> section : sections) {
			if (section.getTitle() != null) {
				items.add(new ItemMetadata(section.getTitle()));
				indexMapping.put(flattenedIndex, new SectionedListIndex(sectionIndex, NOT_AN_ITEM_INDEX));
				flattenedIndex++;
			}
			for (T item : section.getItems()) {
				items.add(new ItemMetadata(section.getViewConfigurator(), item));
				indexMapping.put(flattenedIndex, new SectionedListIndex(sectionIndex, rowIndex));
				flattenedIndex++;
				rowIndex++;
			}
			sectionIndex++;
			rowIndex = 0;
		}
		this.flattenedItems = items;
	}
	
	/**
	 * Holds metadata about each item in the list.
	 */
	private class ItemMetadata {
		//================================================================================
		// Properties
		//================================================================================
		/**
		 * Object used to create and configure views.
		 */
		ViewConfigurator<T> viewConfigurator;
		
		/**
		 * Model object.
		 */
		T object;
		
		/**
		 * Whether the item is for a section header.
		 */
		boolean isSectionHeader;
		
		/**
		 * The title of the section, if {@link #isSectionHeader()} returns true,
		 * otherwise null.
		 */
		String sectionTitle;
		
		//================================================================================
		// Constructors
		//================================================================================
		
		/**
		 * Creates a new instance of {@link ViewConfigurator} representing an item.
		 * @param viewConfigurator Object used to create and configure views.
		 * @param object Model object.
		 */
		ItemMetadata(ViewConfigurator<T> viewConfigurator, T object) {
			this.viewConfigurator = viewConfigurator;
			this.object = object;
		}
		
		/**
		 * Creates a new instance of {@link ViewConfigurator} representing a section.
		 * @param sectionTitle The title of the section.
		 */
		ItemMetadata(String sectionTitle) {
			this.isSectionHeader = true;
			this.sectionTitle = sectionTitle;
		}
	}
}
