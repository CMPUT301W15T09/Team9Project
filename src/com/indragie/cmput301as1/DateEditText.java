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
import java.text.ParseException;
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
	private Context context;
	private DateFormat dateFormat;
	private DatePickerDialog dialog;

	public DateEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.dateFormat = DateFormat.getDateInstance();
		
		setInputType(InputType.TYPE_CLASS_DATETIME);
		
		String currentDate = dateFormat.format(new Date());
		setText(currentDate);
		
		setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showDatePickerDialog();
				}
			}
		});
	}
	
	public Date getDate() {
		try {
			return dateFormat.parse(getText().toString());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setDate(Date date) {
		setText(dateFormat.format(date));
	}
	
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
