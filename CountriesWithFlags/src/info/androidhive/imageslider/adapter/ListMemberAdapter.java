package info.androidhive.imageslider.adapter;

import info.androidhive.imageslider.GridViewActivity;

import java.util.List;

import org.apache.http.NameValuePair;

import com.code.home.AlbumsList;
import com.code.home.Home;
import com.code.loop.R;
import com.code.loop.Utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ListMemberAdapter extends ArrayAdapter<String> {

	private Context context = null;
	private TextView fname = null;
	private TextView status = null;
	private List<NameValuePair> nameValuePairs = null;

	public ListMemberAdapter(Context context) {
		super(context, android.R.layout.two_line_list_item);
		this.context = context;
	}

	@SuppressLint({ "ViewHolder", "ResourceAsColor" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Holder h = null;

		// if (convertView == null) {
		convertView = LayoutInflater.from(getContext()).inflate(
				R.layout.groupcellinfo, parent, false);
		fname = (TextView) convertView.findViewById(R.id.albumname);
		status = (TextView) convertView.findViewById(R.id.ago);

		h = new Holder(fname, status);

		final String p = getItem(position);
		System.out.println("p:" + p);
		// ListView in the Layout.
		if (p.toString().trim().equals(Utilities.codecountry + Utilities.phone)) {
			h.fname.setText("You");
		} else {
			if (Home.syncmap.containsKey(p)) {
				h.fname.setText(Home.syncmap.get(p));
			} else if (Home.contacts.containsKey(p)) {
				h.fname.setText(Home.syncmap.get(p));
			}
		}

		if (p.toString().trim()
				.equals(GridViewActivity.albumselected.get_adminphone())) {
			h.status.setText("admin");
		} else {

		}

		convertView.setTag(h);
		return convertView;
	}

	private static class Holder {
		public TextView fname = null;
		public TextView status = null;

		private Holder(TextView fname, TextView status) {
			this.fname = fname;
			this.status = status;
		}
	}

}
