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

import android.view.View;

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
		public int getItemViewTypeCode();
		/**
		 * @return A new view for displaying an item from the section, e.g. by
		 * inflating from XML.
		 */
		public View createView();
		/**
		 * Configures a view for display using a model.
		 * @param view The view to configure.
		 * @param obj The model object to get data from.
		 */
		public void configureView(View view, T obj);
	}
	
	//================================================================================
	// Properties
	//================================================================================
	
	private String title;
	private List<T> objects;
	private ViewConfigurator<T> viewConfigurator;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link ListSection}
	 * @param title The title of the section.
	 * @param objects Objects contained within the section.
	 * @param viewConfigurator Configurator used to create and configure views used
	 * to display items from the section in a {@link ListView}
	 */
	public ListSection(String title, List<T> objects, ViewConfigurator<T> viewConfigurator) {
		this.title = title;
		this.objects = objects;
		this.viewConfigurator = viewConfigurator;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<T> getObjects() {
		return objects;
	}
	
	public void setObjects(List<T> objects) {
		this.objects = objects;
	}
	
	public ViewConfigurator<T> getViewConfigurator() {
		return viewConfigurator;
	}
}