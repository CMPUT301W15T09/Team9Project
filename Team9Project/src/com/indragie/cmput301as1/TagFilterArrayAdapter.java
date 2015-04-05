package com.indragie.cmput301as1;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TagFilterArrayAdapter extends ArrayAdapter<Tag> {
	
	public TagFilterArrayAdapter(Context context, List<Tag> tags) {
		super(context, android.R.layout.simple_list_item_multiple_choice, tags);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tag tags = getItem(position);
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
		}
		TextView nameTextView = (TextView)convertView.findViewById(android.R.id.text1);
		nameTextView.setText(tags.getName().toString());
		
		return convertView;
	}

	
}
