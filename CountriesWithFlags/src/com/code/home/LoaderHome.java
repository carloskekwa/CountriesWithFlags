package com.code.home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.ListView;

import com.code.loop.R;
import com.code.loop.Utilities;

public class LoaderHome extends
		AsyncTask<Void, List<AlbumsList>, List<AlbumsList>> {

	private final Context mContext;
	private HomeAdapter mAdapter;
	private List<AlbumsList> listalbum = null;
	private List<AlbumsList> pendingalbum = null;
	private String tmp = "";
	private List<NameValuePair> nameValuePairs = null;

	private ArrayList<String> notaccepted = null;
	private ArrayList<String> accepted = null;
	private ListView lv = null;

	// true for images, false for events.
	public LoaderHome(Context context, HomeAdapter adapter, ListView lv) {
		this.lv = lv;
		mContext = context;
		mAdapter = adapter;

	}

	@SuppressLint({ "DefaultLocale", "NewApi" })
	@Override
	protected List<AlbumsList> doInBackground(Void... params) {
		// calling the database.

		System.out.println(Utilities.urlapp + "albums/list/");

		try {
			listalbum = new ArrayList<AlbumsList>();
			pendingalbum = new ArrayList<AlbumsList>();
			tmp = Utilities.fetchGET(Utilities.urlapp + "albums/list/");
			JSONObject response = null;
			response = new JSONObject(tmp);
			JSONArray products = response.getJSONArray("album_list");

			System.out.println("products:" + products.toString());
			for (int i = 0; i < products.length(); i++) {
				JSONObject p = products.getJSONObject(i);
				JSONArray membertmp = p.getJSONArray("member_list");
				ArrayList<String> arraymembers = new ArrayList<String>();

				for (int j = 0; j < membertmp.length(); j++) {
					System.out.println("member_list" + (j + 1) + ":"
							+ membertmp.getString(j));
					arraymembers.add(membertmp.getString(j));
				}
				System.out.println("creatorphone LoaderHome:"
						+ p.getString("_creatorphone"));
				String _creatorphone = p.getString("_creatorphone");
				String admin_phone = p.getString("admin_phone");
				String createOn_epoch = p.getString("createOn_epoch");
				String albumid = p.getString("albumid");
				String name = p.getString("name");
				int photos_count = p.getInt("photos_count");
				String modifiedOn_epoch = p.getString("modifiedOn_epoch");
				int ucount = p.getInt("ucount");
				String album_cover = p.getString("album_cover");

				listalbum.add(new AlbumsList(arraymembers, _creatorphone,
						admin_phone, createOn_epoch, albumid, name,
						photos_count, modifiedOn_epoch, ucount, album_cover));

			}
		} catch (Exception e) {
			System.out.println("Error1");
		}
		// pending one
		try {
			JSONObject response = null;
			response = new JSONObject(tmp);
			JSONArray products = response.getJSONArray("pending_list");
			for (int i = 0; i < products.length(); i++) {
				JSONObject p = products.getJSONObject(i);
				ArrayList<String> arraymembers = new ArrayList<String>();

				try {
					JSONArray membertmp = p.getJSONArray("member_list");

					for (int j = 0; j < membertmp.length(); j++) {
						System.out.println("member_list" + (j + 1) + ":"
								+ membertmp.getString(j));
						arraymembers.add(membertmp.getString(j));
					}

				} catch (Exception e) {

				}

				String _creatorphone = p.getString("_creatorphone");
				String admin_phone = p.getString("admin_phone");
				String createOn_epoch = p.getString("createOn_epoch");
				String albumid = p.getString("albumid");
				String name = p.getString("name");
				int photos_count = Integer.valueOf(p.getString("photos_count"));
				String modifiedOn_epoch = p.getString("modifiedOn_epoch");
				int ucount = Integer.valueOf(p.getString("ucount"));
				String album_cover = p.getString("album_cover");

				pendingalbum.add(new AlbumsList(arraymembers, _creatorphone,
						admin_phone, createOn_epoch, albumid, name,
						photos_count, modifiedOn_epoch, ucount, album_cover));

			}

		} catch (Exception e) {
			// Log.e("EA_DEMO", "Error fetching product list", e);
			System.out.print("Error in the Parsing!");
		}

		return listalbum;
	}

	@Override
	protected void onPostExecute(List<AlbumsList> products) {
		super.onPostExecute(products);

		accepted = new ArrayList<String>();
		notaccepted = new ArrayList<String>();
		for (AlbumsList p : products) {
			mAdapter.add(p);
			Home.mapalbumslist.put(p.getAlbumid(), p);
		}

		if (products.size() == 0) {
			((Home) mContext).setContentView(R.layout.home);
		} else {
			// mAdapter.setFooter("No More Items");
		}

		Utilities.dialog.dismiss();
		mAdapter.notifyDataSetChanged();
		for (AlbumsList p : pendingalbum) {

			AlertMessagewithPOST(mContext, p.get_creatorphone()
					+ " invited you to join the group " + p.getName(),
					Utilities.urlapp + "albums/" + p.getAlbumid() + "/RSVP",
					nameValuePairs, "Reject", "Accept", "You've been invited",
					p.getAlbumid());
		}

		Utilities.dialog = ProgressDialog
				.show(mContext, "", "Loading...", true);

		Utilities.dialog.dismiss();
		System.out.println("finishLoader Task OnPost");

	}

	@SuppressWarnings("unused")
	public void AlertMessagewithPOST(final Context context, String Message,
			final String Url, final List<NameValuePair> nameValuePairs,
			String leftbutton, String rightbutton, String title,
			final String albumid) {
		AlertDialog alert = null;
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(Message)
				.setTitle(title)
				.setCancelable(false)
				.setPositiveButton(rightbutton,
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								// here is the call
								accepted.add(albumid);
								System.out.println("accepted:" + albumid);
								ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
										1);
								nameValuePairs.add(new BasicNameValuePair(
										"accepted", "1"));
								new RequestTask(nameValuePairs,
										Utilities.urlapp + "albums/" + albumid
												+ "/RSVP").execute();

							}
						})
				.setNegativeButton(leftbutton,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								System.out.println("notaccepted:" + albumid);
								ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
										1);
								nameValuePairs.add(new BasicNameValuePair(
										"accepted", "0"));
								new RequestTask(nameValuePairs,
										Utilities.urlapp + "albums/" + albumid
												+ "/RSVP").execute();
								notaccepted.add(albumid);

							}
						});

		alert = builder.create();
		alert.show();

	}

	public static String convertinputStreamToString(InputStream ists)
			throws IOException {
		if (ists != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader r1 = new BufferedReader(new InputStreamReader(
						ists, "UTF-8"));
				while ((line = r1.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				ists.close();
			}
			return sb.toString();
		} else {
			return "";
		}
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
				System.out.println("responding Invitation:" + response);
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

				if (a.get("message").toString().trim()
						.equals("Invite Accepted")) {
					System.out.println("OnPostExecute message accepted");
					mAdapter = new HomeAdapter(mContext);
					LoaderHome t = new LoaderHome(mContext, mAdapter, lv);
					lv.setAdapter(mAdapter);
					t.execute();
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
