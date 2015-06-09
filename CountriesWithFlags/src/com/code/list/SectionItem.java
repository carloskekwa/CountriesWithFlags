package com.code.list;

public class SectionItem implements Item{

	private final String title;
	
	public SectionItem(String title) {
		this.title = title;
	}
	
	public String getTitle(){
		return title;
	}
	
	@Override
	public boolean isSection() {
		return true;
	}
	
	

	@Override
	public void setSelected(boolean checked) {
		// TODO Auto-generated method stub
		
	}

}
