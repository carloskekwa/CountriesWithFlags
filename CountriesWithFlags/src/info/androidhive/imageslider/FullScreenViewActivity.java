package info.androidhive.imageslider;

import info.androidhive.imageslider.adapter.FullScreenImageAdapter;
import info.androidhive.imageslider.helper.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import org.apache.http.NameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
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
	public ViewPager viewPager;
	private AlbumsList albumselected = null;
	private int REQUEST_CAMERA = 501;
	private int SELECT_FILE = 502;
	private List<NameValuePair> nameValuePairs = null;
	private Map<String, PhotosList> map = null;
	private ArrayList<String> urls = null;
	public static int position = -1;
	public static boolean flag = false;
    private MenuDrawer mDrawer;

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
			setTitle((position + 1) + " of "
					+ String.valueOf(GridViewActivity.urls.size() - 1));
			FullScreenViewActivity.position = position;
			adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
					GridViewActivity.urls);

			viewPager.setAdapter(adapter);

			// displaying selected image first
			viewPager.setCurrentItem(position);

			viewPager
					.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
						@Override
						public void onPageSelected(int position) {
							System.out.println("increment!");
							FullScreenViewActivity.position = position + 1;
							setTitle(FullScreenViewActivity.position
									+ " of "
									+ String.valueOf(GridViewActivity.urls
											.size() - 1));
						}

						@Override
						public void onPageScrollStateChanged(int state) {
						}

						@Override
						public void onPageScrolled(int position,
								float positionOffset, int positionOffsetPixels) {
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

		case R.id.coverphoto:

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			new RequestTask(nameValuePairs, Utilities.urlapp
					+ "albums/"
					+ GridViewActivity.map.get(
							GridViewActivity.urls
									.get(FullScreenViewActivity.position))
							.getId() + "/update_cover/").execute();
			Utilities.dialog = ProgressDialog.show(FullScreenViewActivity.this,
					"", "Updating your cover photo album...", true);
			return true;
		case R.id.share:

			/*
			 * Intent intent = new Intent();
			 * intent.setAction(Intent.ACTION_SEND);
			 * 
			 * //change the !type of data you need to share, // for image use
			 * "image/*" intent.setType("text/plain");
			 * System.out.println("FullScreenViewActivity.position:" +
			 * FullScreenViewActivity.position);
			 * intent.putExtra(Intent.EXTRA_TEXT,
			 * GridViewActivity.map.get(GridViewActivity
			 * .urls.get(position)).getThumb_path());
			 * startActivity(Intent.createChooser(intent, "Share"));
			 */
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

			try {
				// here
				List<NameValuePair> namevaluepairs = new ArrayList<NameValuePair>();
				response = Utilities.postData(namevaluepairs,
						this.url);

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

			Utilities.dialog.dismiss();
		}
	}

}
