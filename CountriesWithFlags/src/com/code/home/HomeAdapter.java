package com.code.home;

import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.ocpsoft.prettytime.PrettyTime;

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
import com.code.loop.Utilities;
import com.squareup.picasso.Picasso;

public class HomeAdapter extends ArrayAdapter<AlbumsList> {

	private Context context = null;
	private TextView albumtxt = null;
	private TextView subtitletxt = null;
	private TextView agotxt = null;
	private List<NameValuePair> nameValuePairs = null;
	private ImageView imageview = null;
	private ImageButton btnadd = null;
	private TextView numberofphoto = null;

	public HomeAdapter(Context context) {
		super(context, android.R.layout.two_line_list_item);
		this.context = context;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final Holder h;

		// if (convertView == null) {
		convertView = LayoutInflater.from(getContext()).inflate(R.layout.album,
				parent, false);

		albumtxt = (TextView) convertView.findViewById(R.id.albumname);
		subtitletxt = (TextView) convertView.findViewById(R.id.subtitle);
		agotxt = (TextView) convertView.findViewById(R.id.ago);
		btnadd = (ImageButton) convertView.findViewById(R.id.imageView2);
		imageview = (ImageView) convertView.findViewById(R.id.imgalbum);
		numberofphoto = (TextView) convertView.findViewById(R.id.photonumber);
		h = new Holder(albumtxt, subtitletxt, agotxt, btnadd, imageview,numberofphoto);

		final AlbumsList p = getItem(position);
		System.out.println("adapter:" + p.getName());
		h.albumname.setText(p.getName());
		System.out.println(" p.get_creatorphone().toString().trim():" +  p.get_creatorphone().toString().trim()
				+ " Utilities.phone.toString().trim():" +  Utilities.codecountry + Utilities.phone.toString().trim());
		boolean isCreator = p.get_creatorphone().toString().trim().equals(Utilities.codecountry + Utilities.phone.toString().trim());
	
		if (isCreator){
			h.subtitle.setText("You" + " . ");
		}else{
			if (Home.contacts.containsKey(p.get_creatorphone().toString().trim())){
				System.out.println(p.get_creatorphone().toString().trim() + " creator :" + Home.contacts.get(p.get_creatorphone().toString().trim()) ) ;
				h.subtitle.setText(Home.contacts.get(p.get_creatorphone().toString().trim()) + " . ");
			}else{
				h.subtitle.setText(Home.syncmap.get(p.get_creatorphone().toString().trim()) + " . ");
			}	
		}
		
		
		
		PrettyTime pretty = new PrettyTime();
		h.ago.setText(pretty.format(new Date(Long.valueOf(p.getCreateOn_epoch()))));
		h.numberofphoto.setText(p.getPhotos_count() +  " photos");
		Picasso.with(getContext()).load(p.getAlbum_cover()).resize(120, 120)
				.into(h.imageview);

		h.btnadd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Utilities.selectImage(context);

			}

		});
		
		
		h.albumname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final AlbumsList p = getItem(position);
				System.out.println("p:" + p.getName());
				Intent i = new Intent(context,info.androidhive.imageslider.GridViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("albumcreated", p);
				i.putExtra("isCreated", false);
				i.putExtras(bundle);
				context.startActivity(i);
			}

		});
		
		h.subtitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final AlbumsList p = getItem(position);
				System.out.println("p:" + p.getName());
				Intent i = new Intent(context,info.androidhive.imageslider.GridViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("albumcreated", p);
				i.putExtra("isCreated", false);
				i.putExtras(bundle);
				context.startActivity(i);
			}

		});
		
		h.ago.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final AlbumsList p = getItem(position);
				System.out.println("p:" + p.getName());
				Intent i = new Intent(context,info.androidhive.imageslider.GridViewActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("albumcreated", p);
				i.putExtra("isCreated", false);
				i.putExtras(bundle);
				context.startActivity(i);
			}

		});
		
		h.imageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				final AlbumsList p = getItem(position);
				System.out.println("p:" + p.getName());
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
		public TextView numberofphoto = null;

		private Holder(TextView albumname, TextView subtitle, TextView ago,
				ImageButton btnadd, ImageView imageview,TextView numberofphoto) {
			this.albumname = albumname;
			this.subtitle = subtitle;
			this.ago = ago;
			this.btnadd = btnadd;
			this.imageview = imageview;
			this.numberofphoto = numberofphoto;
		}
	}

}
