package com.code.home;

import java.io.Serializable;
import java.util.ArrayList;

public class AlbumsList implements Serializable {


	private ArrayList<String> member_list = null;
	private String _creatorphone = "";
	private String _adminphone = "";
	private String createOn_epoch = "";
	private String albumid = "";
	private String name = "";
	private int photos_count = 0;
	private String modifiedOn_epoch = "";
	private int ucount = 0;
	private String album_cover = "";

	public AlbumsList(ArrayList<String> member_list,
			String _creatorphone, String _adminphone, String createOn_epoch,
			String albumid, String name, int photos_count,
			String modifiedOn_epoch, int ucount, String album_cover) {
		super();
		this.member_list = member_list;
		this._creatorphone = _creatorphone;
		this._adminphone = _adminphone;
		this.createOn_epoch = createOn_epoch;
		this.albumid = albumid;
		this.name = name;
		this.photos_count = photos_count;
		this.modifiedOn_epoch = modifiedOn_epoch;
		this.ucount = ucount;
		this.album_cover = album_cover;
	}

	public AlbumsList(String albumid, String name, int photo_count,
			String album_cover) {
		this.albumid = albumid;
		this.name = name;
		this.photos_count = photo_count;
		this.album_cover = album_cover;

	}
	
	public ArrayList<String> getMember_list(){
		return this.member_list;
	}

	public String get_creatorphone() {
		return _creatorphone;
	}

	public String get_adminphone() {
		return _adminphone;
	}

	public String getCreateOn_epoch() {
		return createOn_epoch;
	}

	public String getAlbumid() {
		return albumid;
	}

	public String getName() {
		return name;
	}

	public int getPhotos_count() {
		return photos_count;
	}

	public String getModifiedOn_epoch() {
		return modifiedOn_epoch;
	}

	public int getUcount() {
		return ucount;
	}

	public String getAlbum_cover() {
		return album_cover;
	}

}
