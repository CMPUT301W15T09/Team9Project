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

import android.content.Context;
import android.test.AndroidTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.database.DataSetObserver;

import com.indragie.cmput301as1.ListSection;
import com.indragie.cmput301as1.ListSection.ViewConfigurator;
import com.indragie.cmput301as1.SectionedListAdapter;
import com.indragie.cmput301as1.SectionedListIndex;

public class SectionedListAdapterTests extends AndroidTestCase {
	private static final int HEADER_VIEW_TYPE_CODE = 0;
	private static final int VIEW_1_TYPE_CODE = 1;
	private static final int VIEW_2_TYPE_CODE = 2;
	
	private class MockDataSetObserver extends DataSetObserver {
		boolean onChangedCalled;
		
		@Override
		public void onChanged() {
			onChangedCalled = true;
		}
	}
	
	private class TestViewConfigurator implements ViewConfigurator<String> {
		int id;
		
		TestViewConfigurator(int id) {
			this.id = id;
		}
		
		@Override
		public int getViewTypeCode() {
			return id;
		}
		
		@Override
		public View createView(Context context, ViewGroup parent) {
			return new View(context);
		}
		
		@Override
		public void configureView(Context context, View view, String object) {
			view.setId(id);
		}
	}
	
	private SectionedListAdapter<String> adapter;
	private ListSection<String> section1;
	private ListSection<String> section2;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		section1 = new ListSection<String>("A", Arrays.asList(new String[] { "Audi" }), new TestViewConfigurator(VIEW_1_TYPE_CODE));
		section2 = new ListSection<String>("C", Arrays.asList(new String[] {
			"Cadillac", "Chevrolet"
		}), new TestViewConfigurator(VIEW_2_TYPE_CODE));
		
		ArrayList<ListSection<String>> sections = new ArrayList<ListSection<String>>();
		sections.add(section1);
		sections.add(section2);
		adapter = new SectionedListAdapter<String>(getContext(), sections, new TestViewConfigurator(HEADER_VIEW_TYPE_CODE));
	}
	
	public void testGetCount() {
		assertEquals(5, adapter.getCount()); // 3 items + 2 sections
	}
	
	public void testGetItem() {
		assertEquals(null, adapter.getItem(0));
		assertEquals("Audi", adapter.getItem(1));
		assertEquals(null, adapter.getItem(2));
		assertEquals("Cadillac", adapter.getItem(3));
		assertEquals("Chevrolet", adapter.getItem(4));
	}
	
	public void testGetTypedItem() {
		assertEquals(null, adapter.getTypedItem(0));
		assertEquals("Audi", adapter.getTypedItem(1));
		assertEquals(null, adapter.getTypedItem(2));
		assertEquals("Cadillac", adapter.getTypedItem(3));
		assertEquals("Chevrolet", adapter.getTypedItem(4));
	}
	
	public void testGetItemId() {
		assertEquals(0, adapter.getItemId(0));
		assertEquals(5, adapter.getItemId(5));
		assertEquals(8, adapter.getItemId(8));
	}
	
	public void testGetItemViewType() {
		assertEquals(HEADER_VIEW_TYPE_CODE, adapter.getItemViewType(0));
		assertEquals(VIEW_1_TYPE_CODE, adapter.getItemViewType(1));
		assertEquals(HEADER_VIEW_TYPE_CODE, adapter.getItemViewType(2));
		assertEquals(VIEW_2_TYPE_CODE, adapter.getItemViewType(3));
		assertEquals(VIEW_2_TYPE_CODE, adapter.getItemViewType(4));
	}
	
	public void testGetViewTypeCount() {
		assertEquals(3, adapter.getViewTypeCount());
	}
	
	public void testGetView() {
		assertEquals(HEADER_VIEW_TYPE_CODE, adapter.getView(0, null, null).getId());
		assertEquals(VIEW_1_TYPE_CODE,  adapter.getView(1, null, null).getId());
		assertEquals(HEADER_VIEW_TYPE_CODE,  adapter.getView(2, null, null).getId());
		assertEquals(VIEW_2_TYPE_CODE,  adapter.getView(3, null, null).getId());
		assertEquals(VIEW_2_TYPE_CODE,  adapter.getView(4, null, null).getId());
	}
	
	public void testIsEmpty() {
		assertFalse(adapter.isEmpty());
	}
	
	public void testIsEnabled() {
		assertFalse(adapter.isEnabled(0));
		assertTrue(adapter.isEnabled(1));
		assertFalse(adapter.isEnabled(2));
		assertTrue(adapter.isEnabled(3));
		assertTrue(adapter.isEnabled(4));
	}
	
	public void testGetSectionedIndex() {
		assertEquals(new SectionedListIndex(0, SectionedListAdapter.NOT_AN_ITEM_INDEX), adapter.getSectionedIndex(0));
		assertEquals(new SectionedListIndex(0, 0), adapter.getSectionedIndex(1));
		assertEquals(new SectionedListIndex(1, SectionedListAdapter.NOT_AN_ITEM_INDEX), adapter.getSectionedIndex(2));
		assertEquals(new SectionedListIndex(1, 0), adapter.getSectionedIndex(3));
		assertEquals(new SectionedListIndex(1, 1), adapter.getSectionedIndex(4));
	}
	
	public void testRemove() {
		adapter.remove(0);
		assertEquals(3, adapter.getCount());
		assertEquals(null, adapter.getItem(0));
		assertEquals("Cadillac", adapter.getItem(1));
		assertEquals("Chevrolet", adapter.getItem(2));
		
		adapter.remove(1);
		assertEquals(null, adapter.getItem(0));
		assertEquals("Chevrolet", adapter.getItem(1));
	}
	
	public void testSectionMutation() {
		MockDataSetObserver observer = new MockDataSetObserver();
		assertFalse(observer.onChangedCalled);
		adapter.registerDataSetObserver(observer);
		
		ArrayList<String> newSection1Items = new ArrayList<String>(section1.getItems());
		newSection1Items.add("Alfa Romeo");
		newSection1Items.add("Aston Martin");
		section1.setItems(newSection1Items);
		
		ArrayList<String> newSection2Items = new ArrayList<String>(section2.getItems());
		newSection2Items.remove(1);
		section2.setItems(newSection2Items);
		
		adapter.noteSectionsChanged();
		
		assertTrue(observer.onChangedCalled);
		assertEquals(6, adapter.getCount());
		assertEquals("Alfa Romeo", adapter.getItem(2));
		assertEquals("Aston Martin", adapter.getItem(3));
	}
}
