package com.code.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.code.list.EntryItem;
import com.code.loop.R;
import com.code.loop.Utilities;

public class Home extends Activity {

	private ListView lv = null;
	public static Map<String, String> syncmap = null;
	public static Map<String, String> contacts = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			syncmap = new Hashtable<String, String>();
			
			contacts = new HashMap<String, String>();

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

			nameValuePairs.add(new BasicNameValuePair("contactlist", "[]"
					.toString()));

			new RequestTask2(nameValuePairs, Utilities.urlapp + "contactsync/")
					.execute();
			
			contacts = Utilities.getAllContacts(this.getContentResolver());

			Iterator iterator = contacts.entrySet().iterator();
			contacts = new HashMap<String, String>();

			while (iterator.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry mapEntry = (Map.Entry) iterator.next();

				String tmp = (String) mapEntry.getKey();
				if (!tmp.trim().equals("")) {
					tmp = tmp.replace("-", "");
					tmp = tmp.replace(" ", "");
					tmp = tmp.replace("#", "");
					tmp = tmp.replace("(", "");
					tmp = tmp.replace(")", "");
					tmp = tmp.trim();
					tmp = tmp.replaceAll("(?<=\\d) +(?=\\d)", "");
					tmp = tmp.replace(" ", "");
					tmp = tmp.replaceAll("\\s+", "");

					if (tmp.substring(0, 2).equals("00")) {
						tmp = tmp.substring(2, tmp.length());
					} else if (tmp.substring(0, 1).equals("+")) {
						tmp = tmp.substring(1, tmp.length());
					} else if (tmp.substring(0, 1).equals("0")) {
						tmp = tmp.substring(1, tmp.length());
						tmp = Utilities.codecountry + tmp;
					} else {
						tmp = Utilities.codecountry + tmp;
					}
				}
				System.out.println("tmp:" + tmp);
				contacts.put(tmp, mapEntry.getValue().toString());

			}
			Utilities.dialog = ProgressDialog.show(Home.this, "", "Loading...",
					true);
			final HomeAdapter mAdapter = new HomeAdapter(this);
			lv = (ListView) findViewById(R.id.listView);

			LoaderHome loadhome = new LoaderHome(this, mAdapter, lv);
			lv.setAdapter(mAdapter);
			loadhome.execute();
		
		} catch (Exception e) {
			System.out.println("Error listview");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Event Handling for Individual menu item selected Identify single menu
	 * item by it's id
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.newgroup:

			Intent i = new Intent(getApplicationContext(), Display.class);
			startActivity(i);
			return true;

		case android.R.id.home:
			this.finish();
			// Do stuff

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

	// call the empty contactsync to get the numbers !

	class RequestTask2 extends AsyncTask<String, String, String> {

		private String response = "";
		private List<NameValuePair> nameValuePairs = null;
		private String url = "";

		public RequestTask2(List<NameValuePair> nameValuePairs, String Url) {
			this.nameValuePairs = nameValuePairs;
			this.url = Url;
		}

		@Override
		protected String doInBackground(String... uri) {
			// calling the database.
			System.out.println(Utilities.urlapp + "contactsync/");
			try {
				// here
				response = Utilities.postData(nameValuePairs, this.url);

				// System.out.println("response:" + response);
			} catch (Exception e) {
				// Log.e("EA_DEMO", "Error fetching product list", e);
				System.out.print("Error in the Parsing!");
			}

			System.out.println("empty call contactsync!:" + response);
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Do anything with response..

			JSONArray a = null;
			try {
				a = new JSONArray(response);
				for (int i = 0; i < a.length(); i++) {
					JSONObject json = a.getJSONObject(i);
					System.out.println(json.get("name"));
					System.out.println(json.get("full_phone"));
					System.out.println(json.get("color"));
					syncmap.put(json.getString("full_phone"),
							json.getString("name"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("contactsync: " + response);
		}

	}
}
