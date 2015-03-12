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
	private static final int NOT_A_ROW_INDEX = -1;
	
	//================================================================================
	// Properties
	//================================================================================
	
	private Context context;
	private List<ListSection<?>> sections;
	private ArrayList<ItemMetadata> flattenedItems;
	private SectionHeaderConfigurator headerConfigurator;
	private SparseArray<SectionedListIndex> indexMapping = new SparseArray<SectionedListIndex>();
	
	//================================================================================
	// Constructors
	//================================================================================
	
	public SectionedListAdapter(Context context, List<ListSection<?>> sections, int headerResource, int headerTextViewResourceId) {
		this.context = context;
		this.sections = sections;
		this.flattenedItems = flattenSections(sections);
		this.headerConfigurator = new SectionHeaderConfigurator(headerResource, headerTextViewResourceId);
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
		return numberOfViewTypesInSections(sections);
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
		return indexMapping.get(position).getItemIndex() != NOT_A_ROW_INDEX;
	}
	
	//================================================================================
	// Private
	//================================================================================
	
	private ArrayList<ItemMetadata> flattenSections(List<ListSection<?>> sections) {
		ArrayList<ItemMetadata> flattenedItems = new ArrayList<ItemMetadata>();
		int flattenedIndex = 0, sectionIndex = 0, rowIndex = 0;
		for (ListSection<?> section : sections) {
			if (section.getTitle() != null) {
				flattenedItems.add(new ItemMetadata(headerConfigurator, section.getTitle()));
				indexMapping.put(flattenedIndex, new SectionedListIndex(sectionIndex, NOT_A_ROW_INDEX));
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
		return flattenedItems;
	}
	
	private static int numberOfViewTypesInSections(List<ListSection<?>> sections) {
		HashSet<Integer> types = new HashSet<Integer>();
		for (ListSection<?> section : sections) {
			types.add(section.getViewConfigurator().getViewTypeCode());
		}
		return types.size();
	}
	
	private static class ItemMetadata {
		ViewConfigurator viewConfigurator;
		Object object;
		
		ItemMetadata(ViewConfigurator viewConfigurator, Object object) {
			this.viewConfigurator = viewConfigurator;
			this.object = object;
		}
	}
	
	private static class SectionHeaderConfigurator implements ViewConfigurator {
		private static final int HEADER_VIEW_TYPE_CODE = -1;
		
		private int resource;
		private int textViewResourceId;
		
		SectionHeaderConfigurator(int resource, int textViewResourceId) {
			this.resource = resource;
			this.textViewResourceId = textViewResourceId;
		}
		
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
