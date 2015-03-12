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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
	private List<ListSection<?>> sections;
	
	/**
	 * Flattened representation of sections.
	 */
	private ArrayList<ItemMetadata> flattenedItems;
	
	/**
	 * Configurator for section header views.
	 */
	private SectionHeaderConfigurator headerConfigurator;
	
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
	 * @param headerResource Resource ID of the layout for the section headers.
	 * @param headerTextViewResourceId ID of the text view in the section header layout used
	 * to display the section title.
	 */
	public SectionedListAdapter(Context context, List<ListSection<?>> sections, int headerResource, int headerTextViewResourceId) {
		this.context = context;
		this.sections = sections;
		this.headerConfigurator = new SectionHeaderConfigurator(headerResource, headerTextViewResourceId);
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
	
	//================================================================================
	// BaseAdapter
	//================================================================================
	
	@Override
	public int getCount() {
		return flattenedItems.size();
	}
	
	@Override
	public Object getItem(int position) {
		return flattenedItems.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		return flattenedItems.get(position).viewConfigurator.getViewTypeCode();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemMetadata metadata = flattenedItems.get(position);
		ViewConfigurator configurator = metadata.viewConfigurator;
		if (convertView == null) {
			convertView = configurator.createView(context, parent);
		}
		configurator.configureView(context, convertView, metadata.object);
		return convertView;
	}
	
	@Override
	public int getViewTypeCount() {
		HashSet<Integer> types = new HashSet<Integer>();
		for (ListSection<?> section : sections) {
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
		for (ListSection<?> section : sections) {
			if (section.getTitle() != null) {
				items.add(new ItemMetadata(headerConfigurator, section.getTitle()));
				indexMapping.put(flattenedIndex, new SectionedListIndex(sectionIndex, NOT_AN_ITEM_INDEX));
				flattenedIndex++;
			}
			for (Object item : section.getItems()) {
				flattenedItems.add(new ItemMetadata(section.getViewConfigurator(), item));
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
	private static class ItemMetadata {
		//================================================================================
		// Properties
		//================================================================================
		/**
		 * Object used to create and configure views.
		 */
		ViewConfigurator viewConfigurator;
		/**
		 * Model object.
		 */
		Object object;
		
		//================================================================================
		// Constructors
		//================================================================================
		
		/**
		 * Creates a new instance of {@link ViewConfigurator}
		 * @param viewConfigurator Object used to create and configure views.
		 * @param object Model object.
		 */
		ItemMetadata(ViewConfigurator viewConfigurator, Object object) {
			this.viewConfigurator = viewConfigurator;
			this.object = object;
		}
	}
	
	/**
	 * Creates and configures section header views.
	 */
	private static class SectionHeaderConfigurator implements ViewConfigurator {
		//================================================================================
		// Constants
		//================================================================================
		/**
		 * Type code for section headers. Row views should not use this code.
		 */
		private static final int HEADER_VIEW_TYPE_CODE = -1;
		
		//================================================================================
		// Properties
		//================================================================================
		
		/**
		 * Resource ID of the header view layout.
		 */
		private int resource;
		
		/**
		 * ID of the text view used to display the section title.
		 */
		private int textViewResourceId;
		
		//================================================================================
		// Constructors
		//================================================================================
		
		/**
		 * Creates a new instance of {@link SectionHeaderConfigurator{
		 * @param resource Resource ID of the header view layout.
		 * @param textViewResourceId ID of the text view used to display the section title.
		 */
		SectionHeaderConfigurator(int resource, int textViewResourceId) {
			this.resource = resource;
			this.textViewResourceId = textViewResourceId;
		}
		
		//================================================================================
		// ViewConfigurator
		//================================================================================
		
		@Override
		public int getViewTypeCode() {
			return HEADER_VIEW_TYPE_CODE;
		}
		
		@Override
		public View createView(Context context, ViewGroup parent) {
			return LayoutInflater.from(context).inflate(resource, parent, false);
		}
		
		@Override
		public void configureView(Context context, View view, Object object) {
			TextView textView = (TextView)view.findViewById(textViewResourceId);
			textView.setText((String)object);
		}
	}
}
