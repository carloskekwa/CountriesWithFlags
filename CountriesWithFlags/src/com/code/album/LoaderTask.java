package com.code.album;

import info.androidhive.imageslider.FullScreenViewActivity;
import info.androidhive.imageslider.adapter.FullScreenImageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.code.home.AlbumsList;
import com.code.loop.Utilities;

public class LoaderTask extends
		AsyncTask<Void, List<AlbumsList>, List<AlbumsList>> {

	// private final Context mContext;
	private String tmp = "";
	private List<NameValuePair> nameValuePairs = null;
	private Map<String, AlbumsList> mapalbumslist = null;
	private ArrayList<String> notaccepted = null;
	private ArrayList<String> accepted = null;
	private Context mContext = null;
	private FullScreenImageAdapter mAdapter = null;
	private AlbumsList albumslist = null;
	private String response = "";

	// true for images, false for events.
	public LoaderTask(Context context, FullScreenImageAdapter mAdapter,
			AlbumsList albumslist) {

		mContext = context;
		this.mAdapter = mAdapter;
		this.albumslist = albumslist;

	}

	@SuppressLint({ "DefaultLocale", "NewApi" })
	@Override
	protected List<AlbumsList> doInBackground(Void... params) {
		// calling the database.
		System.out.println(Utilities.urlapp + "albums/"
				+ albumslist.getAlbumid() + "/details/");
		try {
			// here
			response = Utilities.fetchGET(Utilities.urlapp + "albums/"
					+ albumslist.getAlbumid() + "/details/");

			System.out.println("response:" + response);
		} catch (Exception e) {
			// Log.e("EA_DEMO", "Error fetching product list", e);
			System.out.print("Error in the Parsing!");
		}

		return null;
	}

	@Override
	protected void onPostExecute(List<AlbumsList> products) {
		super.onPostExecute(products);
		// mapalbumslist = new HashMap<String, AlbumsList>();
		// accepted = new ArrayList<String>();
		// notaccepted = new ArrayList<String>();
		/*
		 * for (AlbumsList p : products) { // mAdapter.add(p); //
		 * mapalbumslist.put(p.getAlbumid(), p); }
		 */

		/*
		 * if (products.size() == 0) { // ((Home)
		 * mContext).setContentView(R.layout.home); } else { //
		 * mAdapter.setFooter("No More Items"); }
		 */

		Utilities.dialog.dismiss();
		System.out.println("finishLoader Task OnPost");
	}

	class RequestTask extends AsyncTask<String, String, String> {

		String response = "";
		private List<NameValuePair> nameValuePairs = null;
		private String url = "";

		public RequestTask(List<NameValuePair> nameValuePairs, String Url) {
			this.nameValuePairs = nameValuePairs;
			this.url = Url;
		}

		@Override
		protected String doInBackground(String... uri) {

			try {
				response = Utilities.postData(nameValuePairs, url);
			} catch (Exception e) {
				// TODO Auto-generated catch blockk
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Do anything with response..
			JSONObject a = null;
			try {
				System.out.println("response2: " + response);
				a = new JSONObject(response);

				if (a.get("message").toString().equals("Accepted")) {
					// LoaderHome t = new LoaderHome(mContext, mAdapter);
					// t.execute();
				} else {

				}

				Utilities.dialog.dismiss();

			} catch (JSONException e) {
				Utilities.dialog.dismiss();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
