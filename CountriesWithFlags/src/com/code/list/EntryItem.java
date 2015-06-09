package com.code.list;


public class EntryItem implements Item{

	public final String title;
	public final String subtitle;
	public boolean selected = false;

	public EntryItem(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
	}
	
	@Override
	public boolean isSection() {
		return false;
	}
	
	 public void setSelected(boolean selected) {
		  this.selected = selected;
		 }
	
	

}
