package com.code.uploadphoto;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import nl.changer.polypicker.ImagePickerActivity;
import nl.changer.polypicker.utils.ImageInternalFetcher;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.code.home.AlbumsList;
import com.code.loop.R;
import com.code.loop.Utilities;

public class MainActivity extends FragmentActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private static final int INTENT_REQUEST_GET_IMAGES = 13;
	private static final int INTENT_REQUEST_GET_N_IMAGES = 14;
	private AlbumsList albumselected = null;
	private Context mContext;

	private ViewGroup mSelectedImagesContainer;
	private ViewGroup mSelectedImagesNone;
	HashSet<Uri> mMedia = new HashSet<Uri>();
	private List<String> url = null;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menutakephoto, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Event Handling for Individual menu item selected Identify single menu
	 * item by it's id
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.addphotos:

			View v = null;
			Iterator<Uri> iterator = mMedia.iterator();
			for (int i = 0; i < mSelectedImagesContainer.getChildCount(); i++) {
				v = mSelectedImagesContainer.getChildAt(i);
				TextView t = (TextView) v.findViewById(R.id.editText1);
				System.out.println("Type: " + t.getText());

				String tmp = url.get(i);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);
				nameValuePairs.add(new BasicNameValuePair("photo1", tmp
						.split(",")[0]));
				nameValuePairs.add(new BasicNameValuePair("caption", t
						.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("albums", "[" + "\""
						+ albumselected.getAlbumid() + "\"" + "]"));

				String json = "{" + "\"photo1\":" + "{\"height\":"
						+ tmp.split(",")[1] + ",\"width\":" + tmp.split(",")[2]
						+ "}}";

				nameValuePairs.add(new BasicNameValuePair("dimensions", json));

				new RequestTask(nameValuePairs, Utilities.urlapp + "photos")
						.execute();

			}
			return true;

		case android.R.id.home:
			this.finish();
			// Do stuff

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
		mContext = MainActivity.this;

		albumselected = (AlbumsList) getIntent().getExtras().getSerializable(
				"albumcreated");

		setTitle("Add to:" + albumselected.getName());
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);
		getImages();
	}

	private void getImages() {
		Intent intent = new Intent(mContext, ImagePickerActivity.class);
		startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
	}

	private void getNImages() {
		Intent intent = new Intent(mContext, ImagePickerActivity.class);

		// limit image pick count to only 3 images.
		intent.putExtra(ImagePickerActivity.EXTRA_SELECTION_LIMIT, 3);
		startActivityForResult(intent, INTENT_REQUEST_GET_N_IMAGES);
	}

	@Override
	protected void onActivityResult(int requestCode, int resuleCode,
			Intent intent) {
		super.onActivityResult(requestCode, resuleCode, intent);
		System.out.println("parcelvalue:" + resuleCode);
		if (resuleCode == Activity.RESULT_OK) {
			if (requestCode == INTENT_REQUEST_GET_IMAGES
					|| requestCode == INTENT_REQUEST_GET_N_IMAGES) {
				Parcelable[] parcelableUris = intent
						.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

				System.out.println("parcelvalue : " + parcelableUris);
				if (parcelableUris == null) {
					return;
				}

				// Java doesn't allow array casting, this is a little hack
				Uri[] uris = new Uri[parcelableUris.length];
				System.arraycopy(parcelableUris, 0, uris, 0,
						parcelableUris.length);

				if (uris != null) {
					for (Uri uri : uris) {
						Log.i(TAG, " uri: " + uri);
						mMedia.add(uri);
					}

					showMedia();
				}
			}
		} else if (resuleCode == Activity.RESULT_CANCELED) {
			finish();
		}
	}

	private void showMedia() {

		url = new ArrayList<String>();
		// Remove all views before
		// adding the new ones.
		mSelectedImagesContainer.removeAllViews();
		Iterator<Uri> iterator = mMedia.iterator();
		ImageInternalFetcher imageFetcher = new ImageInternalFetcher(this, 500);
		while (iterator.hasNext()) {
			Uri uri = iterator.next();

			// showImage(uri);
			Log.i(TAG, " uri: " + uri);
			if (mMedia.size() >= 1) {
				mSelectedImagesContainer.setVisibility(View.VISIBLE);
			}

			View imageHolder = LayoutInflater.from(this).inflate(
					R.layout.media_layout, null);

			// View removeBtn = imageHolder.findViewById(R.id.remove_media);
			// initRemoveBtn(removeBtn, imageHolder, uri);
			ImageView thumbnail = (ImageView) imageHolder
					.findViewById(R.id.media_image);

			if (!uri.toString().contains("content://")) {
				// probably a relative uri
				uri = Uri.fromFile(new File(uri.toString()));
			}
			System.out.println("uri:" + uri);
			imageFetcher.loadImage(uri, thumbnail);
			url.add(uri.toString() + "," + thumbnail.getHeight() + ","
					+ thumbnail.getWidth());
			mSelectedImagesContainer.addView(imageHolder);
			// set the dimension to correctly
			// show the image thumbnail.
			int wdpx = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 300, getResources()
							.getDisplayMetrics());
			int htpx = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 200, getResources()
							.getDisplayMetrics());
			thumbnail
					.setLayoutParams(new LinearLayout.LayoutParams(wdpx, htpx));
		}
	}

	class RequestTask extends AsyncTask<String, String, String> {

		private List<NameValuePair> namevaluepairs = null;
		private String url = "";

		public RequestTask(List<NameValuePair> namevaluepairs, String url) {
			this.namevaluepairs = namevaluepairs;
			this.url = url;
		}

		String response = "";

		@Override
		protected String doInBackground(String... uri) {
			response = Utilities.postMultipart(namevaluepairs, Utilities.urlapp
					+ "photos/");
			return response;

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("Response: " + response);
		}

	}

}
