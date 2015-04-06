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

import java.io.Serializable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Model object that represents a geolocation specified by latitude
 * and longitude coordinates.
 */
public class Geolocation implements Serializable {
	private static final long serialVersionUID = -2951789773586259550L;
	//================================================================================
	// Properties
	//================================================================================

	/**
	 * Latitude coordinate.
	 */
	private double latitude;
	
	/**
	 * Longitude coordinate.
	 */
	private double longitude;
	
	//================================================================================
	// Constructors
	//================================================================================
	
	/**
	 * Creates a new instance of {@link Geolocation}
	 * @param latitude Latitude coordinate.
	 * @param longitude Longitude coordinate.
	 */
	public Geolocation(double latitude, double longitude)  {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * Creates a new instance of {@link Geolocation}
	 * @param latlng The {@link Latlng} instance to get the coordinates from.
	 */
	public Geolocation(LatLng latlng) {
		this.latitude = latlng.latitude;
		this.longitude = latlng.longitude;
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	/**
	 * @return The latitude coordinate.
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/**
	 * @return The longitude coordinate.
	 */
	public double getLongitude() {
		return longitude;
	}
	
	//================================================================================
	// Object
	//================================================================================
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Geolocation other = (Geolocation) obj;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		return true;
	}
}
