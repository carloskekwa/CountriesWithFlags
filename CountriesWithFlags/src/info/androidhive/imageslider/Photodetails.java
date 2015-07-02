package info.androidhive.imageslider;

import java.util.ArrayList;

public class Photodetails {

	private String id = "";
	private String createdOn_epoch = "";
	private String modifiedOn_epoch = "";
	private String image = "";
	private String uploaded_by = "";
	private String albumid = "";
	private String caption = "";
	private String media_url = "";
	private String thumb_path = "";
	private ArrayList<comments> comments = null;
	private ArrayList<String> viewedby = null;

	public String getId() {
		return id;
	}

	public String getCreatedOn_epoch() {
		return createdOn_epoch;
	}

	public String getModifiedOn_epoch() {
		return modifiedOn_epoch;
	}

	public String getImage() {
		return image;
	}

	public String getUploaded_by() {
		return uploaded_by;
	}

	public String getAlbumid() {
		return albumid;
	}

	public String getCaption() {
		return caption;
	}

	public String getMedia_url() {
		return media_url;
	}

	public String getThumb_path() {
		return thumb_path;
	}

	public ArrayList<comments> getComments() {
		return comments;
	}

	public ArrayList<String> getViewedby() {
		return viewedby;
	}

	public Photodetails(String id, String createdOn_epoch,
			String modifiedOn_epoch, String image, String uploaded_by,
			String albumid, String caption, String media_url,
			String thumb_path,
			ArrayList<info.androidhive.imageslider.comments> comments,
			ArrayList<String> viewedby ) {
		super();
		this.id = id;
		this.createdOn_epoch = createdOn_epoch;
		this.modifiedOn_epoch = modifiedOn_epoch;
		this.image = image;
		this.uploaded_by = uploaded_by;
		this.albumid = albumid;
		this.caption = caption;
		this.media_url = media_url;
		this.thumb_path = thumb_path;
		this.comments = comments;
		this.viewedby = viewedby;
	}

}
