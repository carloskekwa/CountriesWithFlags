package com.code.album;

public class PhotosList {

	private String id = "";
	private String creatorphone = "";
	private String filename = "";
	private String caption = "";
	private String height = "";
	private String width = "";
	private String thumb_path = "";
	private String modifiedOn_epoch = "";
	private String createdOn_epoch = "";
	private int count = 0;
	
	public PhotosList(String id, String creatorphone, String filename,
			String caption, String height, String width, String thumb_path,
			String modifiedOn_epoch, String createdOn_epoch,int count) {
		super();
		this.id = id;
		this.creatorphone = creatorphone;
		this.filename = filename;
		this.caption = caption;
		this.height = height;
		this.width = width;
		this.thumb_path = thumb_path;
		this.modifiedOn_epoch = modifiedOn_epoch;
		this.createdOn_epoch = createdOn_epoch;
		this.count = count;
	}
	
	public int getCount(){
		return this.count;
	}

	public String getId() {
		return id;
	}

	public String getCreatorphone() {
		return creatorphone;
	}

	public String getFilename() {
		return filename;
	}

	public String getCaption() {
		return caption;
	}

	public String getHeight() {
		return height;
	}

	public String getWidth() {
		return width;
	}

	public String getThumb_path() {
		return thumb_path;
	}

	public String getModifiedOn_epoch() {
		return modifiedOn_epoch;
	}

	public String getCreatedOn_epoch() {
		return createdOn_epoch;
	}
	
	
	
}
