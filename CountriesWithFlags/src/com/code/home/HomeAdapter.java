package com.code.home;

import java.util.List;

import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.loop.R;
import com.squareup.picasso.Picasso;

public class HomeAdapter extends ArrayAdapter<AlbumsList> {

	private Context context = null;
	private TextView albumtxt = null;
	private TextView subtitletxt = null;
	private TextView agotxt = null;
	private List<NameValuePair> nameValuePairs = null;
	private ImageView imageview = null;
	private ImageButton btnadd = null; 

	public HomeAdapter(Context context) {
		super(context, android.R.layout.two_line_list_item);
		this.context = context;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final Holder h;

		// if (convertView == null) {
		convertView = LayoutInflater.from(getContext()).inflate(R.layout.album,
				parent, false);

		albumtxt = (TextView) convertView.findViewById(R.id.albumname);
		subtitletxt = (TextView) convertView.findViewById(R.id.subtitle);
		agotxt = (TextView) convertView.findViewById(R.id.ago);
		btnadd = (ImageButton) convertView.findViewById(R.id.imageView2);
		imageview = (ImageView) convertView.findViewById(R.id.imgalbum);
		h = new Holder(albumtxt, subtitletxt, agotxt, btnadd,imageview);

		final AlbumsList p = getItem(position);
		System.out.println("adapter:" + p.getName());
		h.albumname.setText(p.getName());
		Picasso.with(getContext()).load(p.getAlbum_cover())
		.resize(120, 120).into(h.imageview);
		
		h.btnadd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context,info.androidhive.imageslider.GridViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("albumcreated", p);
				i.putExtra("isCreated", false);
				i.putExtras(bundle);
				context.startActivity(i);
			}
			
			
		});
		
		
		
		
	
		convertView.setTag(h);
		return convertView;
	}

	private static class Holder {
		public TextView albumname = null;
		public TextView subtitle = null;
		public TextView ago = null;
		public ImageButton btnadd = null;
		public ImageView imageview = null;

		private Holder(TextView albumname, TextView subtitle, TextView ago,
				ImageButton btnadd,ImageView imageview) {
			this.albumname = albumname;
			this.subtitle = subtitle;
			this.ago = ago;
			this.btnadd = btnadd;
			this.imageview = imageview;
		}
	}

}
