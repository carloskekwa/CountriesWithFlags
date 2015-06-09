package info.androidhive.imageslider;

public class comments {

	private String _id = "";
	private String commenttext = "";
	private String createdOn_epoch = "";
	private String commented_by = "";

	public String get_id() {
		return _id;
	}

	public String getCommenttext() {
		return commenttext;
	}

	public String getCreatedOn_epoch() {
		return createdOn_epoch;
	}

	public String getCommented_by() {
		return commented_by;
	}

	public comments(String _id, String commenttext, String createdOn_epoch,
			String commented_by) {
		super();
		this._id = _id;
		this.commenttext = commenttext;
		this.createdOn_epoch = createdOn_epoch;
		this.commented_by = commented_by;
	}

}
