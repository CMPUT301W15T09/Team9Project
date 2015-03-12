package com.indragie.cmput301as1;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.content.Context;

public class TagListModel extends ListModel<Tag>{
	
	private String fileName;
	private Context context;


	public TagListModel(String fileName, Context context) {
		this.fileName = fileName;
		this.context = context;
		this.list = load();
	}
	
	
	public void setTags(ArrayList<Tag> tags) {
		this.list = tags;
	}
	
	public ArrayList<Tag> getTags() {
		return this.list;
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<Tag> load() {
		try {
			FileInputStream fis = context.openFileInput(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			ois.close();
			fis.close();
			return (ArrayList<Tag>)obj;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Tag>();
		}
	}
	

}
