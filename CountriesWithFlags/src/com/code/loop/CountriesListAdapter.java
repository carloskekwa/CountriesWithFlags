package com.code.loop;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class CountriesListAdapter extends ArrayAdapter<String> implements
		Filterable {
	private final Context context;
	private List<String> values;
	private List<String> orig;

	public CountriesListAdapter(Context context, List<String> values) {
		super(context, R.layout.country_list_item, values);
		this.context = context;
		this.values = values;

	}

	public Filter getFilter() {
		return new Filter() {

			@SuppressLint("DefaultLocale")
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				final FilterResults oReturn = new FilterResults();
				final ArrayList<String> results = new ArrayList<String>();
				if (orig == null)
					orig = values;
				if (constraint != null) {
					if (orig != null && orig.size() > 0) {

						for (final String g : orig) {

							System.out.println(Utilities
									.GetCountryZipCode(g.split(",")[1])
									.toUpperCase()
									.contains(constraint.toString()));

							System.out.println(Utilities.GetCountryZipCode(
									g.split(",")[1]).toUpperCase()
									+ "-" + constraint.toString().toUpperCase());

							if (Utilities
									.GetCountryZipCode(g.split(",")[1])
									.toUpperCase()
									.contains(
											constraint.toString().toUpperCase()))
								results.add(g);
						}
					}
					oReturn.values = results;
				}
				return oReturn;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				values = (ArrayList<String>) results.values;
				notifyDataSetChanged();
			}
		};
	}

	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return values.size();
	}

	@Override
	public String getItem(int position) {
		return values.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
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

		h = new Holder(textView, imageView);
		String[] g = values.get(position).split(",");
		System.out.println("values[position]:" + values.get(position) + "="
				+ g[1]);

		h.t2.setText(Utilities.GetCountryZipCode(g[1]).trim());

		String pngName = g[1].trim().toLowerCase();
		h.imageview.setImageResource(context.getResources().getIdentifier(
				"drawable/" + pngName, null, context.getPackageName()));
		final int tmpposition = position;
		
		
		h.t2.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {

				String[] tmp = values.get(tmpposition).split(",");
				Utilities.flagchoosen = tmp[1];
				Utilities.codecountry = tmp[0];
				MainActivity.setFlag();
				((Activity) context).finish();

			}
		});

		h.imageview.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View arg0) {

				String[] tmp = values.get(tmpposition).split(",");
				Utilities.flagchoosen = tmp[1];
				Utilities.codecountry = tmp[0];
				MainActivity.setFlag();
				((Activity) context).finish();

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