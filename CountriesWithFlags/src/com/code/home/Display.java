package com.code.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.code.album.Inalbum;
import com.code.list.EntryAdapter;
import com.code.list.EntryItem;
import com.code.list.Item;
import com.code.list.SectionItem;
import com.code.loop.R;
import com.code.loop.Utilities;

public class Display extends ListActivity implements
		android.widget.SearchView.OnQueryTextListener {
	/** Called when the activity is first created. */
	private List<String> name1 = null;
	private List<String> phno1 = null;
	private Map<String, String> contacts = null;
	private Map<String, String> found = null;
	public static ArrayList<Item> items = new ArrayList<Item>();
	private EntryAdapter adapter = null;
	public HashMap<String, String> checked = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newgroup);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		checked = new HashMap<String, String>();
		Utilities.dialog = ProgressDialog.show(Display.this, "", "Loading...",
				true);
		items = new ArrayList<Item>();
		name1 = new ArrayList<String>();
		phno1 = new ArrayList<String>();
		contacts = getAllContacts(this.getContentResolver());

		adapter = new EntryAdapter(this, items);
		new RequestTask(this).execute();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			// Do stuff

			return true;

		case R.id.create:
			EditText groupname = (EditText) findViewById(R.id.groupname);
			if (!groupname.getText().toString().equals("")) {
				new RequestTask(true).execute();
			}
			System.out.println(adapter.map.entrySet().toString());

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menucreate, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(
				R.id.menu_item_search_).getActionView();

		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		// searchView.setSubmitButtonEnabled(true);
		searchView.setOnQueryTextListener(this);

		return super.onCreateOptionsMenu(menu);
	}

	public void setCheckedItem(int item) {

		if (checked.containsKey(String.valueOf(item))) {
			checked.remove(String.valueOf(item));
		}

		else {
			checked.put(String.valueOf(item), String.valueOf(item));
		}
	}

	public HashMap<String, String> getCheckedItems() {
		return checked;
	}

	public Map<String, String> getAllContacts(ContentResolver cr) {
		contacts = new HashMap<String, String>();
		Cursor phones = cr.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		while (phones.moveToNext()) {
			String name = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String phoneNumber = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			contacts.put(phoneNumber, name);
			name1.add(name);
			phno1.add(phoneNumber);
		}

		phones.close();
		return contacts;
	}

	class RequestTask extends AsyncTask<String, String, String> {

		String response = "";
		private Display display = null;
		private boolean create = false;

		public RequestTask(Display display) {
			this.display = display;
		}

		public RequestTask(boolean create) {
			this.create = create;
		}

		@Override
		protected String doInBackground(String... uri) {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

			if (create == false) {

				phno1 = new ArrayList<String>();
				Iterator iterator = contacts.entrySet().iterator();
				contacts = new HashMap<String, String>();
				name1 = new ArrayList<String>();
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

					contacts.put(tmp, mapEntry.getValue().toString());
					phno1.add(tmp);
					name1.add(mapEntry.getValue().toString());

				}
				System.out.println(phno1.toString());
				nameValuePairs.add(new BasicNameValuePair("contactlist", phno1
						.toString()));

				try {
					response = Utilities.postData(nameValuePairs,
							Utilities.urlapp + "contactsync/");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (create == true) {
				EditText groupname = (EditText) findViewById(R.id.groupname);
				// curl -X POST http://dev.getloop.io/albums/ -H
				// 'Authorization: Token
				// eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.OTYxNzA4MDM1NTQ.y5df1lHkNBERwWj94bN12Bh8Yd9lNehWt-QDBzizcL4'
				// -F "name=Nature" -F "description=Nature+Pics" -F
				// "contacts=[9189513334, 9823213423,41231238723]";
				nameValuePairs.add(new BasicNameValuePair("name", groupname
						.getText().toString()));

				Iterator it = adapter.map.entrySet().iterator();

				nameValuePairs.add(new BasicNameValuePair("description",
						"AndroidUser"));
				try {
					String s = "[";

					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();

						if (it.hasNext()) {
							s += "\""
									+ pair.getKey().toString().replace("+", "")
									+ "\" ,";
						} else {
							s += "\""
									+ pair.getKey().toString().replace("+", "")
									+ "\"";
						}
						it.remove();
					}

					s += "]";
					nameValuePairs.add(new BasicNameValuePair("contacts", s));

					System.out.println(s + " " + groupname.getText().toString()
							+ " ");
					response = Utilities.postMultipart(nameValuePairs,
							Utilities.urlapp + "albums/");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return response;
		}

		@SuppressLint("ShowToast")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Do anything with response..
			if (create == false) {
				JSONArray a = null;
				try {

					a = new JSONArray(response);
					System.out.println("A" + a.toString());
					int len = a.length();
					if (len != 0) {
						// the user has friends on loop
						setContentView(R.layout.newgroupfriend);
						ActionBar actionBar = getActionBar();
						actionBar.setDisplayHomeAsUpEnabled(true);
						getActionBar().setHomeButtonEnabled(true);
						actionBar.setIcon(R.drawable.iconback);
						items.add(new SectionItem("Friends on Loop"));
						for (int i = 0; i < a.length(); i++) {
							JSONObject json = a.getJSONObject(i);
							System.out.println(json.get("name"));
							System.out.println(json.get("full_phone"));
							System.out.println(json.get("color"));
							if (contacts.containsKey(json.get("full_phone"))) {
								contacts.remove(json.get("full_phone"));
							}

							items.add(new EntryItem(
									json.get("name").toString(), "+"
											+ json.get("full_phone").toString()));

						}

						Iterator iterator = contacts.entrySet().iterator();
						if (contacts.size() > 0)
							items.add(new SectionItem("Contacts"));
						while (iterator.hasNext()) {
							@SuppressWarnings("rawtypes")
							Map.Entry mapEntry = (Map.Entry) iterator.next();

							String key = (String) mapEntry.getKey();
							String value = (String) mapEntry.getValue();

							items.add(new EntryItem(value, "+" + key));
							setListAdapter(adapter);
						}

						setListAdapter(adapter);

					} else if (a.length() == 0 && contacts.size() == 0) {
						// no
						setContentView(R.layout.newgroup);
						ActionBar actionBar = getActionBar();
						actionBar.setDisplayHomeAsUpEnabled(true);
						getActionBar().setHomeButtonEnabled(true);
						actionBar.setIcon(R.drawable.iconback);
					} else {

						Iterator iterator = contacts.entrySet().iterator();
						if (contacts.size() > 0)
							items.add(new SectionItem("Contacts"));
						while (iterator.hasNext()) {
							@SuppressWarnings("rawtypes")
							Map.Entry mapEntry = (Map.Entry) iterator.next();

							String key = (String) mapEntry.getKey();
							String value = (String) mapEntry.getValue();

							items.add(new EntryItem(value, "+" + key));
							setListAdapter(adapter);

						}
					}

					final EditText groupname = (EditText) findViewById(R.id.groupname);

					groupname.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							groupname.setText("");
						}

					});

					Utilities.dialog.dismiss();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

					Utilities.dialog.dismiss();
					Toast.makeText(getApplicationContext(),
							"Something went wrong, please restart the app!",
							Toast.LENGTH_LONG).show();

				}

			} else {

				// parse the response..

				JSONObject a = null;
				try {
					System.out.println("response:" + response);

					a = new JSONObject(response);
					AlbumsList albumlist = new AlbumsList(a.getString("id")
							.toString(), a.getString("name").toString(),
							a.getInt("photos_count"), a.getString("albumcover"));
					Intent i = new Intent(getApplicationContext(),
							Inalbum.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("albumcreated", albumlist);
					i.putExtra("isCreated", true);
					i.putExtras(bundle);
					finish();
					Home.comingfromGrid = true;
					startActivity(i);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Utilities.dialog.dismiss();

					try {
						System.out.println("err:"
								+ a.getString("err").trim().equals("87"));
						if (a.getString("err").trim().equals("87")) {

							Toast.makeText(
									getApplicationContext(),
									"You should share the albums with at least one person",
									6000).show();
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						Toast.makeText(
								getApplicationContext(),
								"Something went wrong while creating your album, please try again!",
								6000).show();
						e1.printStackTrace();
					}

				}

			}
			adapter.setArrays();
		}
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// this is your adapter that will be filtered
		System.out.println("newText:" + newText);
		adapter.getFilter().filter(newText);
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

}