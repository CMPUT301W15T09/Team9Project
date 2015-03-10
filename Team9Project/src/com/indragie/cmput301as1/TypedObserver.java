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

/**
 * Interface implemented by classes when they want to be informed of
 * changes in observable objects. 
 * 
 * This class is the same as {@link java.util.Observer}, except that 
 * the argument passed into {@link update(TypedObservable<T>, T)} is typed.
 *
 */
public interface TypedObserver<T> {
	/**
	 * Informs the observer that the observable has been updated.
	 * @param o The observable that was updated.
	 * @param arg An optional argument containing data that can be used
	 * by the observer to update itself (e.g. updating UI)
	 */
	public void update(TypedObservable<T> o, T arg);
}
