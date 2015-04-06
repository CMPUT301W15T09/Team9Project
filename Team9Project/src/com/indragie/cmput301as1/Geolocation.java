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

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

/**
 * Model object that represents a geolocation specified by latitude
 * and longitude coordinates.
 */
public class Geolocation implements Serializable {
	private static final long serialVersionUID = 1223468022963281449L;
	
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
	
	/**
	 * The name of the location.
	 */
	private String name;
	
	/**
	 * The address of the location.
	 */
	private String address;
	
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
	
	/**
	 * Creates a new instance of {@link Geolocation}
	 * @param place The {@link Place} instance to get information from.
	 */
	public Geolocation(Place place) {
		this(place.getLatLng());
		this.name = place.getName().toString();
		this.address = place.getAddress().toString();
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
	 * Sets the latitude coordinate.
	 * @param latitude The latitude coordinate.
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * @return The longitude coordinate.
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * Sets the longitude coordinate.
	 * @param longitude The longitude coordinate.
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * @return The name of the location.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the location.
	 * @param name The name of the location.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return The address of the location.
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Sets the address of the location.
	 * @param address The address.
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	//================================================================================
	// Object
	//================================================================================
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
