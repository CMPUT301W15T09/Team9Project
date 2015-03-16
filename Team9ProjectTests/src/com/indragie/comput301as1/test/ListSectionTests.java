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
package com.indragie.comput301as1.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.indragie.cmput301as1.ListSection;
import com.indragie.cmput301as1.ListSection.ViewConfigurator;

import junit.framework.TestCase;

public class ListSectionTests extends TestCase {
	private static class StubViewConfigurator implements ViewConfigurator<Integer> {
		@Override
		public int getViewTypeCode() {
			return 0;
		}

		@Override
		public View createView(Context context, ViewGroup parent) {
			return null;
		}
		
		@Override
		public void configureView(Context context, View view, Integer object) {
		}
	}
	
	public void testTitleAccessors() {
		ListSection<Integer> section = new ListSection<Integer>("Numbers", Arrays.asList(new Integer[] {1, 2, 3}), new StubViewConfigurator());
		assertEquals("Numbers", section.getTitle());
		
		section.setTitle("Another title");
		assertEquals("Another title", section.getTitle());
	}
	
	public void testItemsAccessors() {
		List<Integer> numbers = Arrays.asList(new Integer[] {1, 2, 3});
		ListSection<Integer> section = new ListSection<Integer>("Numbers", numbers, new StubViewConfigurator());
		assertEquals(numbers, section.getItems());
		
		List<Integer> newNumbers = Arrays.asList(new Integer[] {4, 5, 6});
		section.setItems(newNumbers);
		assertEquals(newNumbers, section.getItems());
	}
	
	public void testViewConfiguratorAccessor() {
		StubViewConfigurator configurator = new StubViewConfigurator();
		ListSection<Integer> section = new ListSection<Integer>("Numbers", Arrays.asList(new Integer[] {1, 2, 3}), configurator);
		assertEquals(configurator, section.getViewConfigurator());
	}
	
	public void testSize() {
		ListSection<Integer> section = new ListSection<Integer>("Numbers", Arrays.asList(new Integer[] {1, 2, 3}), new StubViewConfigurator());
		assertEquals(3, section.size());
		
		section.setItems(new ArrayList<Integer>());
		assertEquals(0, section.size());
	}
}
