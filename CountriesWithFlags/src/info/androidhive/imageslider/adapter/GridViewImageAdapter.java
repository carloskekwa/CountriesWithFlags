package info.androidhive.imageslider.adapter;

import info.androidhive.imageslider.FullScreenViewActivity;
import info.androidhive.imageslider.GridViewActivity;
import info.androidhive.imageslider.Photodetails;
import info.androidhive.imageslider.comments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.code.loop.Utilities;
import com.squareup.picasso.Picasso;

public class GridViewImageAdapter extends BaseAdapter {

	private Activity _activity;
	private ArrayList<String> _filePaths = new ArrayList<String>();
	private int imageWidth;

	public GridViewImageAdapter(Activity activity, ArrayList<String> filePaths,
			int imageWidth) {
		this._activity = activity;
		this._filePaths = filePaths;
		this.imageWidth = imageWidth;
	}

	@Override
	public int getCount() {
		return this._filePaths.size();
	}

	@Override
	public Object getItem(int position) {
		return this._filePaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		com.joooonho.SelectableRoundedImageView imageView;
		if (convertView == null) {
			imageView = new com.joooonho.SelectableRoundedImageView(_activity);
		} else {
			imageView = (com.joooonho.SelectableRoundedImageView) convertView;
		}

		// get screen dimensions
		// Bitmap image = decodeFile(_filePaths.get(position), imageWidth,
		// imageWidth);

		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
				imageWidth));
		imageView.setPadding(10, 10, 10, 10);
		imageView.setCornerRadiiDP(16, 16, 16, 16);
		// imageView.setImageBitmap(image);
		
		Picasso.with(_activity).load(_filePaths.get(position)).into(imageView);
		
		// image view click listener
		imageView.setOnClickListener(new OnImageClickListener(position));

		System.out.println("contains::" + _filePaths.get(position).toString());

		if (!(GridViewActivity.mapdetails.containsKey(_filePaths.get(position)
				.toString().trim()))) {

			new RequestTask(Utilities.urlapp
					+ "photos/"
					+ GridViewActivity.map.get(_filePaths.get(position))
							.getId() + "/view/", position).execute();

		} else {
			System.out.println("Map Contains key");
		}
		return imageView;
	}

	class OnImageClickListener implements OnClickListener {

		int _postion;

		// constructor
		public OnImageClickListener(int position) {
			this._postion = position;
		}

		@Override
		public void onClick(View v) {
			// on selecting grid view image
			// launch full screen activity
			Intent i = new Intent(_activity, FullScreenViewActivity.class);
			i.putExtra("position", _postion);
			_activity.startActivity(i);
		}

	}

	/*
	 * Resizing image size
	 */
	public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
		try {

			File f = new File(filePath);

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			final int REQUIRED_WIDTH = WIDTH;
			final int REQUIRED_HIGHT = HIGHT;
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
					&& o.outHeight / scale / 2 >= REQUIRED_HIGHT)
				scale *= 2;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	// RequestTask that load picture details.

	class RequestTask extends AsyncTask<String, String, String> {

		private int position = 0;
		private String response = "";
		private String url = "";

		public RequestTask(String Url, int position) {
			this.url = Url;
			this.position = position;
		}

		@Override
		protected String doInBackground(String... uri) {

			try {
				// here
				response = Utilities.fetchGET(this.url);
			} catch (Exception e) {
				System.out.print("Error in the Parsing!");
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Do anything with response..

			JSONObject a = null;

			try {

				a = new JSONObject(response);
				JSONObject b = a.getJSONObject("photo");
				System.out.println(b.getString("media_url").trim());
				System.out.println("Urladded to mapdetails:"
						+ _filePaths.get(position));
				ArrayList<comments> comment = null;
				comment = new ArrayList<comments>();
				try {
					JSONArray c = b.getJSONArray("comments");

					for (int z = 0; z < c.length(); z++) {
						JSONObject tmp = c.getJSONObject(z);
						comment.add(new comments(tmp.getString("_id"), tmp
								.getString("comment"), tmp
								.getString("commented_by"), tmp
								.getString("createdOn_epoch")));
					}
				} catch (Exception e) {

				}

				JSONArray d = null;
				ArrayList<String> viewedby = new ArrayList<String>();
				try {
					d = b.getJSONArray("viewedby");
					for (int j = 0; j < d.length(); j++) {
						JSONObject tmp = d.getJSONObject(j);
						viewedby.add(tmp.getString("phone"));
					}
				} catch (Exception e) {

				}

				GridViewActivity.mapdetails.put(
						b.getString("thumb_path").trim(),
						new Photodetails(b.getString("id"), b
								.getString("createdOn_epoch"), b
								.getString("modifiedOn_epoch"), b
								.getString("image"),
								b.getString("uploaded_by"), b
										.getString("album"), b
										.getString("caption"), b
										.getString("media_url"), b

								.getString("thumb_path"), comment, viewedby));
				Utilities.dialog.dismiss();

			} catch (JSONException e) {
				Utilities.dialog.dismiss();
				// TODO Auto-generated catch block
				System.out
						.println("error in the parsing GridViewImageAdapter!");
				e.printStackTrace();
			}
		}
	}

}
