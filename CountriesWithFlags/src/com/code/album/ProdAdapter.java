package com.code.album;

import java.util.List;

import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.home.AlbumsList;
import com.code.loop.R;
import com.squareup.picasso.Picasso;

public class ProdAdapter extends ArrayAdapter<AlbumsList> {

	private Context context = null;
	private TextView albumtxt = null;
	private TextView subtitletxt = null;
	private TextView agotxt = null;
	private List<NameValuePair> nameValuePairs = null;
	private ImageView imageview = null;
	private ImageView imageview1 = null;

	public ProdAdapter(Context context) {
		super(context, android.R.layout.two_line_list_item);
		this.context = context;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final Holder h;

		// if (convertView == null) {
		convertView = LayoutInflater.from(getContext()).inflate(R.layout.inalbumpicture,
				parent, false);
		
		imageview = (ImageView) convertView.findViewById(R.id.imageView1);
		imageview1 = (ImageView) convertView.findViewById(R.id.imageView2);
		h = new Holder(imageview, imageview1);

		final AlbumsList p = getItem(position);
		System.out.println("adapter:" + p.getName());
	
		Picasso.with(getContext()).load(p.getAlbum_cover()).resize(120, 120)
				.into(h.imageview);
		
		
		Picasso.with(getContext()).load(p.getAlbum_cover()).resize(120, 120)
		.into(h.imageview1);
		
		
		convertView.setTag(h);
		return convertView;
	}

	private static class Holder {
		public ImageView imageview = null;
		public ImageView imageview1 = null;

		private Holder(ImageView imageview, ImageView imageview1) {
			this.imageview = imageview;
			this.imageview1 = imageview1;
		}
	}

}
