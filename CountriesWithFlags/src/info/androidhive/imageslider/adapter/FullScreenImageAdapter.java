package info.androidhive.imageslider.adapter;

import info.androidhive.imageslider.GridViewActivity;
import info.androidhive.imageslider.helper.TouchImageView;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.code.home.Home;
import com.code.loop.R;
import com.code.loop.Utilities;
import com.squareup.picasso.Picasso;

public class FullScreenImageAdapter extends PagerAdapter {

	private Activity _activity;
	private ArrayList<String> _imagePaths;
	private LayoutInflater inflater;
	private Map<String, String> map = null;

	// constructor
	public FullScreenImageAdapter(Activity activity,
			ArrayList<String> imagePaths) {
		this._activity = activity;
		this._imagePaths = imagePaths;
}

	@Override
	public int getCount() {
		return this._imagePaths.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		TouchImageView imgDisplay;
		Button btnClose;
		TextView ago;
		TextView seen;
	
		

		inflater = (LayoutInflater) _activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image,
				container, false);

		imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
		btnClose = (Button) viewLayout.findViewById(R.id.btnClose);
		ago = (TextView) viewLayout.findViewById(R.id.ago);
		seen = (TextView) viewLayout.findViewById(R.id.seen);

		System.out.println("url::" + _imagePaths.get(position));
		Picasso.with(_activity).load(_imagePaths.get(position))
				.into(imgDisplay);
		

		// close button click event
		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_activity.finish();
			}
		});

		if (GridViewActivity.mapdetails.containsKey(_imagePaths.get(position))) {
		//_activity.setTitle(GridViewActivity.map.get(_imagePaths.get(position)).getCount() + " of " + (getCount() - 1));
			String uploaded_by = GridViewActivity.mapdetails.get(
					_imagePaths.get(position)).getUploaded_by();
			String createdon = GridViewActivity.mapdetails.get(
					_imagePaths.get(position)).getCreatedOn_epoch();

			long epoch = System.currentTimeMillis();
			System.out.println("uploaded_by:" + uploaded_by);
			System.out.println("Days difference = "
					+ Utilities.daysBetween(Long.valueOf(createdon), epoch));
			ago.setText(Utilities.daysBetween(Long.valueOf(createdon), epoch)
					+ " ago by " + Home.contacts.get(uploaded_by));
			ArrayList<String> viewedby = GridViewActivity.mapdetails.get(
					_imagePaths.get(position)).getViewedby();
			if (viewedby.size() == GridViewActivity.albumselected
					.getMember_list().size()) {
				seen.setText("Seen by everyone");
			} else {
				ArrayList<String> member_list = GridViewActivity.albumselected
						.getMember_list();
				// TO-DO
			}
		} else {

		}
		((ViewPager) container).addView(viewLayout);
		return viewLayout;
	}


	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);
	}

}
