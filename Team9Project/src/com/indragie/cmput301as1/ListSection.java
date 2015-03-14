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
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Model object containing sectioned data displayed by a {@link ListView}, via
 * {@link SectionedListAdapter}
 */
public class ListSection<T> {
	//================================================================================
	// Interfaces
	//================================================================================
	
	/**
	 * An object that creates and configures views for display in a {@link ListView}
	 * using model objects contained within the section.
	 */
	public interface ViewConfigurator<T> {
		/**
		 * Type code for the view used to display items from the section. Sections
		 * that use the same view should return the same type code.
		 * @return View type code.
		 */
		public int getViewTypeCode();
		/**
		 * Creates a new view.
		 * @param context The current context.
		 * @param parent The parent view that the created view will be attached to.
		 * @return A new view for displaying an item from the section, e.g. by
		 * inflating from XML.
		 */
		public View createView(Context context, ViewGroup parent);
		/**
		 * Configures a view for display using a model.
		 * @param context The current context.
		 * @param view The view to configure.
		 * @param object The model object to get data from.
		 */
		public void configureView(Context context, View view, T object);
	}
	
	//================================================================================
	// Properties
	//================================================================================
	
	private String title;
	private List<T> items;
	private ViewConfigurator<T> viewConfigurator;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link ListSection}
	 * @param title The title of the section. Pass null for this parameter to hide the
	 * section header.
	 * @param items Objects contained within the section.
	 * @param viewConfigurator Configurator used to create and configure views used
	 * to display items from the section in a {@link ListView}
	 */
	public ListSection(String title, List<T> items, ViewConfigurator<T> viewConfigurator) {
		if (viewConfigurator == null) {
			throw new IllegalArgumentException("viewConfigurator must be non null");
		}
		this.title = title;
		this.items = new ArrayList<T>(items); // Shallow copy
		this.viewConfigurator = viewConfigurator;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return The title of the section.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title of the section.
	 * @param title The title of the section.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return An unmodifiable list of the items contained within the section.
	 */
	public List<T> getItems() {
		return Collections.unmodifiableList(items);
	}
	
	/**
	 * Sets the items in the section. The objects are shallow-copied.
	 * @param items The items in the section.
	 */
	public void setItems(List<T> items) {
		this.items = new ArrayList<T>(items); // Shallow copy
	}
	
	/**
	 * @return Configurator used to create and configure views used
	 * to display items from the section in a {@link ListView}
	 */
	public ViewConfigurator<T> getViewConfigurator() {
		return viewConfigurator;
	}
}
