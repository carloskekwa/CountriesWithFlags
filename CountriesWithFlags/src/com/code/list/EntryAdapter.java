package com.code.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.code.loop.R;

public class EntryAdapter extends ArrayAdapter<Item> {

	private Context context;
	private ArrayList<Item> items;
	private LayoutInflater vi;
	public Map<String, String> map = null;

	public EntryAdapter(Context context, ArrayList<Item> items) {
		super(context, 0, items);
		map = new HashMap<String, String>();
		this.context = context;
		this.items = items;
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	class ViewHolder {
		TextView name;
		TextView phone;
		CheckBox chk;

	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		ViewHolder holder = null;
		Log.v("ConvertView", String.valueOf(position));

		final Item i = items.get(position);
		if (i != null) {

			if (i.isSection()) {
				SectionItem si = (SectionItem) i;
				v = vi.inflate(R.layout.list_item_section, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);

				final TextView sectionView = (TextView) v
						.findViewById(R.id.list_item_section_text);
				sectionView.setText(si.getTitle());
			} else {
				EntryItem ei = (EntryItem) i;
				v = vi.inflate(R.layout.list_item_entry, null);
				final TextView title = (TextView) v
						.findViewById(R.id.textView1);
				final TextView subtitle = (TextView) v
						.findViewById(R.id.textView2);

				if (title != null)
					title.setText(ei.title);
				if (subtitle != null)
					subtitle.setText(ei.subtitle);

				holder = new ViewHolder();
				holder.name = title;
				holder.phone = subtitle;
				holder.chk = (CheckBox) v.findViewById(R.id.checkBox1);
				final ViewHolder h = holder;

				if (map.containsKey(h.phone.getText().toString())) {

					holder.chk.setChecked(true);
				} else {
					holder.chk.setChecked(false);
				}

				h.chk.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						if (map.containsKey(h.phone.getText().toString())) {
							map.remove(h.phone.getText().toString());

						} else {
							map.put(h.phone.getText().toString(), h.name
									.getText().toString());
						}
						System.out.println("checked");

					}
				});
				convertView.setTag(holder);

			}

		}

		return v;
	}

}
