package info.androidhive.imageslider;

import info.androidhive.imageslider.adapter.GridViewImageAdapter;
import info.androidhive.imageslider.helper.AppConstant;
import info.androidhive.imageslider.helper.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.code.album.PhotosList;
import com.code.home.AlbumsList;
import com.code.loop.R;
import com.code.loop.Utilities;

public class GridViewActivity extends Activity {

	private Utils utils;
	private ArrayList<String> imagePaths = new ArrayList<String>();
	private GridViewImageAdapter adapter;
	private GridView gridView;
	private int columnWidth;
	public static AlbumsList albumselected = null;
	private int REQUEST_CAMERA = 501;
	private int SELECT_FILE = 502;
	private List<NameValuePair> nameValuePairs = null;
	public static Map<String, PhotosList> map = null;
	public static ArrayList<String> urls = null;
	private int position = 0;
	public static Map<String, Photodetails> mapdetails = null;
	public static Map<String, String> contacts = null;
	private int count = 0 ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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

		setContentView(R.layout.activity_grid_view);
		contacts = new HashMap<String, String>();

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

		gridView = (GridView) findViewById(R.id.grid_view);
		utils = new Utils(this);
		// Initilizing Grid View
		InitilizeGridLayout();
		try {
			mapdetails = new HashMap<String, Photodetails>();
			map = new HashMap<String, PhotosList>();
			urls = new ArrayList<String>();
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);

			// Utilities.dialog = ProgressDialog.show(Inalbum.this, "",
			albumselected = (AlbumsList) getIntent().getExtras()
					.getSerializable("albumcreated");
			boolean isCreated = getIntent().getBooleanExtra("isCreated", false);
			setTitle(albumselected.getName());
			if (isCreated) {
				// is well-created ..
				System.out.println("well-done creation");
			} else {
				Utilities.dialog = ProgressDialog.show(GridViewActivity.this,
						"", "Loading...", true);
				nameValuePairs = new ArrayList<NameValuePair>(1);
				new RequestTask(nameValuePairs, Utilities.urlapp + "albums/"
						+ albumselected.getAlbumid() + "/details/").execute();
			}
			// "Loading...", true);

		} catch (Exception e) {
			System.out.println("Error in the onCreateFullScrenView");
		}

	}

	private void InitilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				AppConstant.GRID_PADDING, r.getDisplayMetrics());

		columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

		gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
		gridView.setColumnWidth(columnWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);
		gridView.setHorizontalSpacing((int) padding);
		gridView.setVerticalSpacing((int) padding);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.inalbummenu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(
				GridViewActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from Library")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.newphoto:
			// Go to picture or the other one !
			// Intent i = new Intent(getApplicationContext(), Display.class);
			// startActivity(i);
			selectImage();
			return true;
		case android.R.id.home:
			this.finish();
			// Do stuff

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

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
			System.out.println(Utilities.urlapp + "albums/"
					+ albumselected.getAlbumid() + "/details/");
			try {
				// here
				response = Utilities.fetchGET(this.url);

				// System.out.println("response:" + response);
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
				// System.out.println("response2: " + response);
				JSONArray c = a.getJSONArray("photo_list");

				for (int i = 0; i < c.length(); i++) {
					JSONObject json = c.getJSONObject(i);
					JSONObject d = json.getJSONObject("dimensions");
					String height = d.getString("height");
					String width = d.getString("width");

					map.put(json.getString("thumb_path"),
							new PhotosList(json.getString("_id"), json
									.getString("_creatorphone"), json
									.getString("filename"), json
									.getString("caption"), height, width, json
									.getString("thumb_path"), json
									.getString("modifiedOn_epoch"), json
									.getString("createdOn_epoch"),(i+1)));
					System.out.println("i:" + (i+1) + "url:" + json.getString("thumb_path") );
					urls.add(json.getString("thumb_path"));
				}
				
				// Gridview adapter
				adapter = new GridViewImageAdapter(GridViewActivity.this, urls,
						columnWidth);

				// setting grid view adapter
				gridView.setAdapter(adapter);

				Utilities.dialog.dismiss();

			} catch (JSONException e) {
				Utilities.dialog.dismiss();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
