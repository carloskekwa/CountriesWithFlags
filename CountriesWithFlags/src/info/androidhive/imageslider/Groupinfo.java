package info.androidhive.imageslider;

import info.androidhive.imageslider.adapter.GridViewImageAdapter;
import info.androidhive.imageslider.adapter.ListMemberAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.code.album.PhotosList;
import com.code.home.Home;
import com.code.loop.R;
import com.code.loop.Utilities;

public class Groupinfo extends Activity {

	private ListView lv = null;
	private ListMemberAdapter ad = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {

			if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
				// Activity was brought to front and not created,
				// Thus finishing this will get us to the last viewed activity
				finish();
				return;
			}
		} catch (Exception e) {

		}

		try {

			setContentView(R.layout.albuminfo);
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			setTitle("Album Info");
			EditText albumname = (EditText) findViewById(R.id.albumname);
			TextView Created = (TextView) findViewById(R.id.created);
			TextView nbrphoto = (TextView) findViewById(R.id.nbrphoto);
			Button b = (Button) findViewById(R.id.btnexitgroup);

			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							1);

					AlertMessagewithPOST(
							Groupinfo.this,
							"Are you sure you want to exit this group?",
							Utilities.urlapp
									+ "albums/"
									+ GridViewActivity.albumselected
											.getAlbumid() + "/remove/",
							nameValuePairs, "Cancel", "Yes", "Exit Album",
							GridViewActivity.albumselected.getAlbumid());
				}
			});

			// album name
			PrettyTime pretty = new PrettyTime();
			albumname.setText(GridViewActivity.albumselected.getName());
			// number of photos
			nbrphoto.setText(GridViewActivity.albumselected.getPhotos_count()
					+ " photos . updated "
					+ pretty.format(new Date(Long
							.valueOf(GridViewActivity.albumselected
									.getModifiedOn_epoch()))));

			System.out.println("Phone of  the creator : "
					+ GridViewActivity.albumselected.get_creatorphone()
							.toString().trim() + "Utilities.phone.trim():"
					+ Utilities.phone.trim());

			if (GridViewActivity.albumselected.get_creatorphone().toString()
					.trim()
					.equals(Utilities.codecountry + Utilities.phone.trim())) {
				Created.setText("Created "
						+ pretty.format(new Date(Long
								.valueOf(GridViewActivity.albumselected
										.getCreateOn_epoch()))) + " by You");
			} else if (Home.syncmap.containsKey(GridViewActivity.albumselected
					.get_creatorphone().toString().trim())) {
				Created.setText("Created "
						+ pretty.format(new Date(Long
								.valueOf(GridViewActivity.albumselected
										.getCreateOn_epoch())))
						+ " by "
						+ Home.syncmap.get(GridViewActivity.albumselected
								.get_creatorphone()));
			} else {
				Created.setText("Created "
						+ pretty.format(new Date(Long
								.valueOf(GridViewActivity.albumselected
										.getCreateOn_epoch())))
						+ " by "
						+ Home.contacts.get(GridViewActivity.albumselected
								.get_creatorphone()));
			}

			// update the members of the album
			ad = new ListMemberAdapter(getApplicationContext());
			lv = (ListView) findViewById(R.id.listmembers);
			ad.addAll(GridViewActivity.albumselected.getMember_list());
			lv.setAdapter(ad);
			ad.notifyDataSetChanged();

		} catch (Exception e) {
			e.getStackTrace();
		}
		// Utilities.dialog = ProgressDialog.show(Inalbum.this, "",

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			// Do stuff
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

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
								System.out
										.println("accepting of quitting album:"
												+ albumid);
								ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
										1);
								nameValuePairs.add(new BasicNameValuePair(
										"accepted", "1"));
								new RequestTask(nameValuePairs, Url).execute();

							}
						})
				.setNegativeButton(leftbutton,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								dialog.dismiss();

							}
						});

		alert = builder.create();
		alert.show();

	}

	class RequestTask extends AsyncTask<String, String, String> {

		private String response = "";
		private List<NameValuePair> nameValuePairs = null;
		private String url = "";

		public RequestTask(List<NameValuePair> nameValuePairs, String Url) {
			this.nameValuePairs = nameValuePairs;
			this.url = Url;
		}

		@Override
		protected String doInBackground(String... uri) {
			// calling the database.
			System.out.println("Url:" + url);
			try {
				// here
				response = Utilities.postData(nameValuePairs, url);
			} catch (Exception e) {
				// Log.e("EA_DEMO", "Error fetching product list", e);
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

				if (a.get("err").toString().trim().equals("0")) {
					finish();
					GridViewActivity.finishAll();
				} else {
					Utilities.InformativeMessage(
							"Something went wrong please try again! ", "Exit",
							Groupinfo.this, "Ok");
				}

			} catch (JSONException e) {
				Utilities.dialog.dismiss();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
