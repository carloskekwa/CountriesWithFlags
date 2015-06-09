package com.code.loop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CountriesListAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;

	public CountriesListAdapter(Context context, String[] values) {
		super(context, R.layout.country_list_item, values);
		this.context = context;
		this.values = values;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Holder h = null;
		View rowView = inflater.inflate(R.layout.country_list_item, parent,
				false);
		TextView textView = (TextView) rowView
				.findViewById(R.id.txtViewCountryName);
		ImageView imageView = (ImageView) rowView
				.findViewById(R.id.imgViewFlag);

		h = new Holder(textView,imageView);
		String[] g = values[position].split(",");
		h.t2.setText(Utilities.GetCountryZipCode(g[1]).trim());

		String pngName = g[1].trim().toLowerCase();
		h.imageview.setImageResource(context.getResources().getIdentifier(
				"drawable/" + pngName, null, context.getPackageName()));
		
		final int tmpposition = position;

		h.t2.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				
				String[] tmp = values[tmpposition].split(",");	
				Utilities.flagchoosen = tmp[1];
				Utilities.codecountry = tmp[0];
				MainActivity.setFlag();
				((Activity)context).finish();
				
			}			
			});
		
		
		h.imageview.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {
				
				String[] tmp = values[tmpposition].split(",");	
				Utilities.flagchoosen = tmp[1];
				Utilities.codecountry = tmp[0];
				MainActivity.setFlag();
				((Activity)context).finish();
				
			}			
			});
		
		
		
		
		rowView.setTag(h);
		return rowView;
	}

	static class Holder {
		public final TextView t2;
		public final ImageView imageview;

		Holder(TextView t2, ImageView imageview) {
			this.t2 = t2;
			this.imageview = imageview;

		}
	}

}