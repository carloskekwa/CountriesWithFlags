package info.androidhive.imageslider;

import java.util.Date;

import org.ocpsoft.prettytime.PrettyTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.home.Home;
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

		long epoch = System.currentTimeMillis();
		System.out.println("Days difference = "
				+ Utilities.daysBetween(Long.valueOf(p.getCreatedOn_epoch()),
						epoch));
		PrettyTime pretty = new PrettyTime();
		System.out.println(pretty.format(new Date(Long.valueOf(p
				.getCreatedOn_epoch()))));
		h.ago.setText(pretty.format(new Date(Long.valueOf(p
				.getCreatedOn_epoch()))));

		if (p.isRead()) {
			if (Integer.valueOf(p.getType()) == 0) {

				if (isCurrentUser) {
					if (Home.contacts.containsKey(commenter)) {
						h.action.setText(Home.contacts.get(commenter)
								+ " commented on your photo");
					} else {
						h.action.setText(Home.syncmap.get(commenter)
								+ " commented on your photo");
					}
					Picasso.with(getContext()).load(R.drawable.readcomment)
							.resize(120, 120).into(h.imageview);
				} else if (commenterIsCreator) {
					if (Home.contacts.containsKey(commenter)) {
						h.action.setText(Home.contacts.get(commenter)
								+ " commented on their photo");
					} else {
						h.action.setText(Home.syncmap.get(commenter)
								+ " commented on their photo");
					}
					Picasso.with(getContext()).load(R.drawable.readcomment)
							.resize(120, 120).into(h.imageview);
				} else if (photoCreator == "") {
					if (Home.contacts.containsKey(commenter)) {
						h.action.setText(Home.contacts.get(commenter)
								+ " commented on a photo");
					} else {
						h.action.setText(Home.syncmap.get(commenter)
								+ " commented on a photo");
					}
					Picasso.with(getContext()).load(R.drawable.readcomment)
							.resize(120, 120).into(h.imageview);
				} else {
					if (Home.contacts.containsKey(commenter)
							&& Home.contacts.containsKey(photoCreator)) {
						h.action.setText(Home.contacts.get(commenter)
								+ " commented on  "
								+ Home.contacts.get(photoCreator) + "'s photo");
					} else {
						h.action.setText(Home.syncmap.get(commenter)
								+ " commented on  "
								+ Home.syncmap.get(photoCreator) + "'s photo");
					}
					// "commented on %@'s photo"
					Picasso.with(getContext()).load(R.drawable.readcomment)
							.resize(120, 120).into(h.imageview);
				}
			} else if (Integer.valueOf(p.getType()) == 1) {

				boolean isCreator = p.getUser().toString().trim()
						.equals(p.getActor().toString().trim());

				if (!isCreator) {
					if (Home.contacts.containsKey(commenter)
							&& Home.contacts.containsKey(photoCreator)) {
						h.action.setText(Home.contacts.get(commenter)
								+ " shared this album with you");
					} else {
						h.action.setText(Home.syncmap.get(commenter)
								+ " shared this album with you");
					}
				} else {
					h.action.setText("Album Shared");
				}

				Picasso.with(getContext()).load(R.drawable.readalbum)
						.resize(120, 120).into(h.imageview);

			} else if (Integer.valueOf(p.getType()) == 2) {

				if (Home.contacts.containsKey(commenter)) {
					h.action.setText(Home.contacts.get(commenter)
							+ " added a photo");
				} else {
					h.action.setText(Home.syncmap.get(commenter)
							+ " added a photo");
				}
				Picasso.with(getContext()).load(R.drawable.readphoto)
						.resize(120, 120).into(h.imageview);

				//

			} else if (Integer.valueOf(p.getType()) == 3) {
				// joined the album
				if (Home.contacts.containsKey(commenter)) {
					h.action.setText(Home.syncmap.get(commenter)
							+ " joined the album");
				} else {
					h.action.setText(Home.syncmap.get(commenter)
							+ " joined the album");
				}

				Picasso.with(getContext()).load(R.drawable.readjoin)
						.resize(120, 120).into(h.imageview);

			} else if (Integer.valueOf(p.getType()) == 4) {
				if (Home.contacts.containsKey(commenter)) {
					h.action.setText(Home.contacts.get(commenter)
							+ " accepted the invitation");
				} else {
					h.action.setText(Home.syncmap.get(commenter)
							+ " accepted the invitation");
				}

				Picasso.with(getContext()).load(R.drawable.readjoin)
						.resize(120, 120).into(h.imageview);

			} else if (Integer.valueOf(p.getType()) == 5) {
				if (Home.contacts.containsKey(commenter)) {
					h.action.setText(Home.contacts.get(commenter)
							+ " rejected the invitation");
				} else {
					h.action.setText(Home.syncmap.get(commenter)
							+ " rejected the invitation");
				}
				Picasso.with(getContext()).load(R.drawable.readjoin)
						.resize(120, 120).into(h.imageview);

			} else if (Integer.valueOf(p.getType()) == 6) {
				if (Home.contacts.containsKey(commenter)) {
					h.action.setText(Home.contacts.get(commenter)
							+ " changed cover photo");
				} else {
					h.action.setText(Home.syncmap.get(commenter)
							+ " changed cover photo");
				}
				Picasso.with(getContext()).load(R.drawable.readcoverchange)
						.resize(120, 120).into(h.imageview);

			} else if (Integer.valueOf(p.getType()) == 8) {

				h.action.setText(" Album Created");

				Picasso.with(getContext()).load(R.drawable.readalbum)
						.resize(120, 120).into(h.imageview);

			}
		} else {

			if (Integer.valueOf(p.getType()) == 0) {

				if (isCurrentUser) {
					if (Home.contacts.containsKey(commenter)) {
						h.action.setText(Home.contacts.get(commenter)
								+ " commented on your photo");
					} else {
						h.action.setText(Home.syncmap.get(commenter)
								+ " commented on your photo");
					}
					Picasso.with(getContext()).load(R.drawable.newcomment)
							.resize(120, 120).into(h.imageview);
				} else if (commenterIsCreator) {
					if (Home.contacts.containsKey(commenter)) {
						h.action.setText(Home.contacts.get(commenter)
								+ " commented on their photo");
					} else {
						h.action.setText(Home.syncmap.get(commenter)
								+ " commented on their photo");
					}
					Picasso.with(getContext()).load(R.drawable.newcomment)
							.resize(120, 120).into(h.imageview);
				} else if (photoCreator == "") {
					if (Home.contacts.containsKey(commenter)) {
						h.action.setText(Home.contacts.get(commenter)
								+ " commented on a photo");
					} else {
						h.action.setText(Home.syncmap.get(commenter)
								+ " commented on a photo");
					}
					Picasso.with(getContext()).load(R.drawable.newcomment)
							.resize(120, 120).into(h.imageview);
				} else {
					if (Home.contacts.containsKey(commenter)
							&& Home.contacts.containsKey(photoCreator)) {
						h.action.setText(Home.contacts.get(commenter)
								+ " commented on  "
								+ Home.contacts.get(photoCreator) + "'s photo");
					} else {
						h.action.setText(Home.syncmap.get(commenter)
								+ " commented on  "
								+ Home.syncmap.get(photoCreator) + "'s photo");
					}
					// "commented on %@'s photo"
					Picasso.with(getContext()).load(R.drawable.newcomment)
							.resize(120, 120).into(h.imageview);
				}
			} else if (Integer.valueOf(p.getType()) == 1) {
				/*
				 *  if(!isCreator)             action =
				 * NSLocalizedString(@"shared this album with you", nil);
				 *         else             action =
				 * NSLocalizedString(@"Album Shared", nil);
				 */

				if (Home.contacts.containsKey(commenter)
						&& Home.contacts.containsKey(photoCreator)) {
					h.action.setText(Home.contacts.get(commenter)
							+ " shared this album with you");
				} else {
					h.action.setText(Home.syncmap.get(commenter)
							+ " shared this album with you");
				}
				Picasso.with(getContext()).load(R.drawable.newalbum)
						.resize(120, 120).into(h.imageview);

			} else if (Integer.valueOf(p.getType()) == 2) {

				if (Home.contacts.containsKey(commenter)) {
					h.action.setText(Home.contacts.get(commenter)
							+ " added a photo");
				} else {
					h.action.setText(Home.syncmap.get(commenter)
							+ " added a photo");
				}
				Picasso.with(getContext()).load(R.drawable.newphoto)
						.resize(120, 120).into(h.imageview);

				//

			} else if (Integer.valueOf(p.getType()) == 3) {
				// joined the album
				if (Home.contacts.containsKey(commenter)) {
					h.action.setText(Home.syncmap.get(commenter)
							+ " joined the album");
				} else {
					h.action.setText(Home.syncmap.get(commenter)
							+ " joined the album");
				}

				Picasso.with(getContext()).load(R.drawable.newjoin)
						.resize(120, 120).into(h.imageview);

			} else if (Integer.valueOf(p.getType()) == 4) {
				if (Home.contacts.containsKey(commenter)) {
					h.action.setText(Home.contacts.get(commenter)
							+ " accepted the invitation");
				} else {
					h.action.setText(Home.syncmap.get(commenter)
							+ " accepted the invitation");
				}

				Picasso.with(getContext()).load(R.drawable.newjoin)
						.resize(120, 120).into(h.imageview);

			} else if (Integer.valueOf(p.getType()) == 5) {
				if (Home.contacts.containsKey(commenter)) {
					h.action.setText(Home.contacts.get(commenter)
							+ " rejected the invitation");
				} else {
					h.action.setText(Home.syncmap.get(commenter)
							+ " rejected the invitation");
				}
				Picasso.with(getContext()).load(R.drawable.newjoin)
						.resize(120, 120).into(h.imageview);

			} else if (Integer.valueOf(p.getType()) == 6) {
				if (Home.contacts.containsKey(commenter)) {
					h.action.setText(Home.contacts.get(commenter)
							+ " changed cover photo");
				} else {
					h.action.setText(Home.syncmap.get(commenter)
							+ " changed cover photo");
				}
				Picasso.with(getContext()).load(R.drawable.newcoverchange)
						.resize(120, 120).into(h.imageview);

			} else if (Integer.valueOf(p.getType()) == 8) {

				h.action.setText(" Album Created");

				Picasso.with(getContext()).load(R.drawable.newalbum)
						.resize(120, 120).into(h.imageview);

			}

		}
		convertView.setTag(h);
		return convertView;
	}

	private static class Holder {
		public TextView action = null;
		public TextView ago = null;
		public ImageView imageview = null;

		private Holder(TextView action, TextView ago, ImageView imageview) {
			this.action = action;
			this.ago = ago;
			this.imageview = imageview;
		}
	}

}
