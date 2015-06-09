package info.androidhive.imageslider;

import info.androidhive.imageslider.adapter.FullScreenImageAdapter;
import info.androidhive.imageslider.helper.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.code.album.PhotosList;
import com.code.home.AlbumsList;
import com.code.loop.R;
import com.code.loop.Utilities;

public class FullScreenViewActivity extends Activity {

	private Utils utils;
	private FullScreenImageAdapter adapter;
	public  ViewPager viewPager;
	private AlbumsList albumselected = null;
	private int REQUEST_CAMERA = 501;
	private int SELECT_FILE = 502;
	private List<NameValuePair> nameValuePairs = null;
	private Map<String, PhotosList> map = null;
	private ArrayList<String> urls = null;
	public static int position = -1;
	public static boolean flag = false;

	
	


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
		
		

		try {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		
			setContentView(R.layout.activity_fullscreen_view);

			viewPager = (ViewPager) findViewById(R.id.pager);
			
			utils = new Utils(getApplicationContext());

			Intent i = getIntent();
			int position = i.getIntExtra("position", 0);
			setTitle((position + 1) + " of " + String.valueOf(GridViewActivity.urls.size() - 1));

			adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
					GridViewActivity.urls);

			
			viewPager.setAdapter(adapter);
			
			// displaying selected image first
			viewPager.setCurrentItem(position);
			

			
			viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			     @Override
			     public void onPageSelected(int position) 
			     {     
			    	 System.out.println("increment");
			    	 FullScreenViewActivity.position = position + 1;
			    	 setTitle(FullScreenViewActivity.position + " of " +  String.valueOf(GridViewActivity.urls.size() - 1));
			     }
			     @Override
			     public void onPageScrollStateChanged(int state)
			     {
			     }
			     @Override
			     public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			     {
			     }
			});
			
			
			// "Loading...", true);

		} catch (Exception e) {
			System.out.println("Error in the onCreateFullScrenView");
		}

		
		
	   
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
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menuphoto, menu);

		return super.onCreateOptionsMenu(menu);
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

				System.out.println("response:" + response);
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
				System.out.println("response2: " + response);
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
									.getString("createdOn_epoch"),0));
					urls.add(json.getString("thumb_path"));
				}

				// displaying selected image first
				// viewPager.setCurrentItem(position);
				// LoaderTask loadhome = new LoaderTask(this, adapter,
				// albumselected);

				// loadhome.execute();

				Utilities.dialog.dismiss();

			} catch (JSONException e) {
				Utilities.dialog.dismiss();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	

	  
}
