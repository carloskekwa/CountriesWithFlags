package info.androidhive.imageslider;

import info.androidhive.imageslider.adapter.GridViewImageAdapter;
import info.androidhive.imageslider.helper.AppConstant;
import info.androidhive.imageslider.helper.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.code.album.PhotosList;
import com.code.home.AlbumsList;
import com.code.home.Home;
import com.code.loop.R;
import com.code.loop.Utilities;

public class GridViewActivity extends Activity {

	private Utils utils;
	private ArrayList<String> imagePaths = new ArrayList<String>();
	private GridViewImageAdapter adapter;
	private GridView gridView;
	private int columnWidth;
	public static AlbumsList albumselected = null;
	private List<NameValuePair> nameValuePairs = null;
	public static Map<String, PhotosList> map = null;
	public static ArrayList<String> urls = null;
	private int position = 0;
	public static Map<String, Photodetails> mapdetails = null;

	private MenuDrawer mDrawerright;
	private MenuDrawer mDrawerleft;
	private Button activity = null;
	private Button group = null;
	private ListView listview = null;
	public static Map<String, Album_feeds> mapfeeds = null;
	private ListView lv = null;
	private Bundle bundle = null;
	static ArrayList<Activity> activities = new ArrayList<Activity>();
	
	public static void finishAll() {
		Home.comingfromGrid = true;
		for (Activity activity : GridViewActivity.activities)
			activity.finish();
			
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish(); // or do something else
	}
	
	
	
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

			setContentView(R.layout.activity_grid_view);
			mDrawerright = MenuDrawer.attach(this, Position.RIGHT);
			mDrawerright.setContentView(R.layout.activity_grid_view);
			mDrawerright.setMenuView(R.layout.activity_menu);
			mDrawerright.setMenuSize(600);
			GridViewActivity.activities.add(this);
			/*
			 * getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
			 * getSlidingMenu()
			 * .setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			 */

			activity = (Button) findViewById(R.id.Activity);

			activity.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mDrawerright.openMenu();
				}

			});

			group = (Button) findViewById(R.id.group);

			group.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(),Groupinfo.class);
				
					startActivity(i);
					
				}

			});

			gridView = (GridView) findViewById(R.id.grid_view);

			utils = new Utils(this); // Initilizing Grid View
			InitilizeGridLayout();
			try {
				mapdetails = new HashMap<String, Photodetails>();
				map = new HashMap<String, PhotosList>();
				urls = new ArrayList<String>();
				mapfeeds = new HashMap<String, Album_feeds>();

				// Utilities.dialog = ProgressDialog.show(Inalbum.this, "",
				albumselected = (AlbumsList) getIntent().getExtras()
						.getSerializable("albumcreated");

				bundle = new Bundle();
				bundle.putSerializable("albumcreated", albumselected);

				boolean isCreated = getIntent().getBooleanExtra("isCreated",
						false);
				setTitle(albumselected.getName());
				if (isCreated) { // is
					// well-created ..
					System.out.println("well-done creation!");
					Utilities.dialog = ProgressDialog.show(
							GridViewActivity.this, "", "Loading...", true);
					nameValuePairs = new ArrayList<NameValuePair>(1);
					new RequestTask(nameValuePairs, Utilities.urlapp
							+ "albums/" + albumselected.getAlbumid()
							+ "/details/").execute();

					new RequestTask2(nameValuePairs, Utilities.urlapp
							+ "albums/" + albumselected.getAlbumid()
							+ "/get_details/").execute();
				} else {
					Utilities.dialog = ProgressDialog.show(
							GridViewActivity.this, "", "Loading...", true);
					nameValuePairs = new ArrayList<NameValuePair>(1);
					new RequestTask(nameValuePairs, Utilities.urlapp
							+ "albums/" + albumselected.getAlbumid()
							+ "/details/").execute();

				} // "Loading...", true);

				ActionBar actionBar = getActionBar();
				actionBar.setDisplayHomeAsUpEnabled(true);

				RightDrawerAdapter rightdraweradapter = new RightDrawerAdapter(
						this);
				lv = (ListView) findViewById(R.id.lvrightdrawer);

				Loaderrightdrawer loaderdrawer = new Loaderrightdrawer(
						nameValuePairs, Utilities.urlapp + "albums/"
								+ albumselected.getAlbumid() + "/get_details/",
						this, rightdraweradapter, lv);
				lv.setAdapter(rightdraweradapter);
				loaderdrawer.execute();

			} catch (Exception e) {
				System.out.println("Error in the onCreateFullScrenView");
			}

		} catch (Exception e) {

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.newphoto:
			// Go to picture or the other one !
			// Intent i = new Intent(getApplicationContext(), Display.class);
			// startActivity(i);
			Utilities.selectImage(GridViewActivity.this);
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
									.getString("createdOn_epoch"), (i + 1)));
					System.out.println("i:" + (i + 1) + "url:"
							+ json.getString("thumb_path"));
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
				response = Utilities.postData(nameValuePairs, Utilities.urlapp
						+ "contactsync/");

				// System.out.println("response:" + response);
			} catch (Exception e) {
				// Log.e("EA_DEMO", "Error fetching product list", e);
				System.out.print("Error in the Parsing!");
			}

			System.out.println("empty call contactsync!");
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Do anything with response..

			JSONObject a = null;
			System.out.println("contactsync: " + response);

		}

	}
}
