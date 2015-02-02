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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * Subclass of {@link EditText} that shows a date picker dialog when tapped,
 * and updates the date text when a new date is picked.
 */
public class DateEditText extends EditText {
	//================================================================================
	// Properties
	//================================================================================
	private Context context;
	private DateFormat dateFormat;
	private DatePickerDialog dialog;
	private Date date;
	private Date minDate;
	private Date maxDate;
	private OnDateChangedListener onDateChangedListener;
	
	//================================================================================
	// Interfaces
	//================================================================================
	
	/**
	 * Listener to be called when the user picks a different date.
	 */
	public interface OnDateChangedListener {
		/**
		 * Called when a different date is picked.
		 * @param view Field whose date changed.
		 * @param date The new date.
		 */
		public void onDateChanged(DateEditText view, Date date);
	}

	//================================================================================
	// Constructors
	//================================================================================
	
	public DateEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.dateFormat = DateFormat.getDateInstance();
		
		setInputType(InputType.TYPE_CLASS_DATETIME);
		setDate(new Date());
		
		setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showDatePickerDialog();
				}
			}
		});
	}
	
	//================================================================================
	// Accessors
	//================================================================================
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
		setText(dateFormat.format(date));
		if (onDateChangedListener != null) {
			onDateChangedListener.onDateChanged(this, date);
		}
	}
	
	public Date getMinimumDate() {
		return minDate;
	}
	
	public void setMinDate(Date minDate) {
		this.minDate = minDate;
		if (dialog != null) {
			dialog.getDatePicker().setMinDate(minDate.getTime());
		}
		if (date.compareTo(minDate) < 0) {
			setDate(minDate);
		}
	}
	
	public Date getMaxDate() {
		return maxDate;
	}
	
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
		if (dialog != null) {
			dialog.getDatePicker().setMaxDate(maxDate.getTime());
		}
		if (date.compareTo(maxDate) > 0) {
			setDate(maxDate);
		}
	}
	
	public OnDateChangedListener getOnDateChangedListener() {
		return onDateChangedListener;
	}
	
	public void setOnDateChangedListener(OnDateChangedListener listener) {
		onDateChangedListener = listener;
	}
	
	//================================================================================
	// Private
	//================================================================================
	
	private void showDatePickerDialog() {
		if (dialog != null && dialog.isShowing()) return;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDate());
		
		// DatePickerDialog instance is lazily instantiated.
		if (dialog == null) {
			dialog = new DatePickerDialog(
				context, 
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day) {
						GregorianCalendar newCalendar = new GregorianCalendar(year, month, day);
						setDate(newCalendar.getTime());
						clearFocus();
					}
				}, 
				calendar.get(Calendar.YEAR), 
				calendar.get(Calendar.MONTH), 
				calendar.get(Calendar.DAY_OF_MONTH)
			);
			if (minDate != null) {
				dialog.getDatePicker().setMinDate(minDate.getTime());
			}
			if (maxDate != null) {
				dialog.getDatePicker().setMaxDate(maxDate.getTime());
			}
		} else {
			dialog.updateDate(
				calendar.get(Calendar.YEAR), 
				calendar.get(Calendar.MONTH), 
				calendar.get(Calendar.DAY_OF_MONTH)
			);
		}
		dialog.show();
	}
}
