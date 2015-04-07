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

import com.google.android.gms.maps.model.LatLng;
import com.indragie.cmput301as1.Geolocation;

import junit.framework.TestCase;

public class GeolocationTests extends TestCase {
	public void testCoordinateConstructor() {
		final double latitude = 53.5333;
		final double longitude = 113.5000;
		
		Geolocation location = new Geolocation(latitude, longitude);
		assertEquals(latitude, location.getLatitude());
		assertEquals(longitude, location.getLongitude());
	}
	
	public void testLatLngConstructor() {
		final double latitude = 53.5333;
		final double longitude = 113.5000;
		
		LatLng latlng = new LatLng(latitude, longitude);
		Geolocation location = new Geolocation(latlng);
		assertEquals(latitude, location.getLatitude());
		assertEquals(longitude, location.getLongitude());
	}
	
	public void testGetLatLng() {
		final double latitude = 53.5333;
		final double longitude = 113.5000;
		Geolocation location = new Geolocation(latitude, longitude);
		LatLng latlng = location.getLatLng();
		assertEquals(latitude, latlng.latitude);
		assertEquals(longitude, latlng.longitude);
	}
}
