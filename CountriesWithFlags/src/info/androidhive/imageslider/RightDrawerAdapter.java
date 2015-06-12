package info.androidhive.imageslider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.loop.R;
import com.code.loop.Utilities;
import com.squareup.picasso.Picasso;

public class RightDrawerAdapter extends ArrayAdapter<Album_feeds> {

	private Context context = null;
	private TextView action = null;
	private TextView ago = null;
	private ImageView imageview = null;

	public RightDrawerAdapter(Context context) {
		super(context, android.R.layout.two_line_list_item);
		this.context = context;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final Holder h;

		// if (convertView == null) {
		convertView = LayoutInflater.from(getContext()).inflate(
				R.layout.album_feeds, parent, false);

		action = (TextView) convertView.findViewById(R.id.action);
		ago = (TextView) convertView.findViewById(R.id.ago);
		imageview = (ImageView) convertView.findViewById(R.id.imgstatus);

		h = new Holder(action, ago, imageview);
		final Album_feeds p = getItem(position);
		System.out.println("adapter:" + p.get_id());

		String commenter = p.getActor();
		String photoCreator = p.getEdata();
		boolean commenterIsCreator = commenter.equals(p.getUser());
		boolean isCurrentUser = photoCreator.equals(Utilities.phone);

		if (Integer.valueOf(p.getType()) == 0) {

			if (isCurrentUser) {
				h.action.setText("commented on your photo");
			} else if (commenterIsCreator) {
				h.action.setText("commented on their photo");
			} else if (photoCreator == "") {
				h.action.setText("commented on a photo");
			} else {
				// "commented on %@'s photo"
				h.action.setText("commented on  " + photoCreator + "'s photo");
			}
		} else if (Integer.valueOf(p.getType()) == 1) {
			/*
			 *  if(!isCreator)             action =
			 * NSLocalizedString(@"shared this album with you", nil);
			 *         else             action =
			 * NSLocalizedString(@"Album Shared", nil);
			 */
			h.action.setText("shared this album with you");

		} else if (Integer.valueOf(p.getType()) == 2) {
			//
			h.action.setText("added a photo");
		} else if (Integer.valueOf(p.getType()) == 3) {

			// joined the album
			h.action.setText("joined the album");
		} else if (Integer.valueOf(p.getType()) == 4) {
			h.action.setText("accepted the invitation");
		} else if (Integer.valueOf(p.getType()) == 5) {
			h.action.setText("rejected the invitation");
		} else if (Integer.valueOf(p.getType()) == 6) {
			h.action.setText("changed cover photo");
		} else if (Integer.valueOf(p.getType()) == 8) {
			h.action.setText("Album Created");
		}

//		Picasso.with(getContext()).load(p.getAlbum_cover()).resize(120, 120)
//				.into(h.imageview);

		convertView.setTag(h);
		return convertView;
	}

	private static class Holder {
		public TextView action = null;
		public TextView ago = null;
		public ImageView imageview = null;

		private Holder(TextView action, TextView ago, ImageView imageview) {
			this.action = action;
			this.ago = action;
			this.imageview = imageview;
		}
	}

}
