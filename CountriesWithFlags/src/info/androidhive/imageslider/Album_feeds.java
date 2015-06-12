package info.androidhive.imageslider;

public class Album_feeds {

	private String _id = "";
	private String user = "";
	private String actor = "";
	private String albumid = "";
	private String photoid = "";
	private String Type = "";
	private String createdOn_epoch = "";
	private String createdOn;
	private boolean read = false;
	private String edata,modifiedOn;

	public String get_id() {
		return _id;
	}

	public String getUser() {
		return user;
	}

	public String getActor() {
		return actor;
	}

	public String getAlbumid() {
		return albumid;
	}

	public String getPhotoid() {
		return photoid;
	}

	public String getType() {
		return Type;
	}

	public String getCreatedOn_epoch() {
		return createdOn_epoch;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public boolean isRead() {
		return read;
	}
	
	

	public String getEdata() {
		return edata;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public Album_feeds(String _id, String user, String actor, String albumid,
			String photoid, String type, String createdOn_epoch,
			String createdOn, boolean read,String edata,String modifiedOn) {
		super();
		this._id = _id;
		this.user = user;
		this.actor = actor;
		this.albumid = albumid;
		this.photoid = photoid;
		this.Type = type;
		this.createdOn_epoch = createdOn_epoch;
		this.createdOn = createdOn;
		this.read = read;
		this.edata = edata;
		this.modifiedOn = modifiedOn;
	}

}
