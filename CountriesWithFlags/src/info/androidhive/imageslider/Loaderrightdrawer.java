package info.androidhive.imageslider;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.code.loop.Utilities;

public class Loaderrightdrawer extends
		AsyncTask<Void, List<Album_feeds>, List<Album_feeds>> {

	private List<NameValuePair> nameValuePairs = null;
	private String url = "";
	private final Context mContext;
	private RightDrawerAdapter mAdapter;
	private ListView lv = null;
	private String response = "";

	public Loaderrightdrawer(List<NameValuePair> nameValuePairs, String Url,
			Context context, RightDrawerAdapter adapter, ListView lv) {
		this.nameValuePairs = nameValuePairs;
		this.url = Url;
		this.mContext = context;
		this.lv = lv;
		this.mAdapter = adapter;

	}

	@Override
	protected List<Album_feeds> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		List<Album_feeds> album_feed = new ArrayList<Album_feeds>();
		try {

			String tmp = Utilities.fetchGET(this.url);
			System.out.println("notifications:" + tmp);
			JSONObject response = null;
			try {
				response = new JSONObject(tmp);
				JSONArray products = response.getJSONArray("Notifications");
				System.out.println("products.length:" + products.length());
				for (int i = 0; i < products.length(); i++) {
					try {
						JSONObject p = products.getJSONObject(i);
						String _id = p.getString("_id");
						String edata = "";
						if (p.isNull("edata")) {
							edata = "";
						} else {
							edata = p.getString("edata");
						}
						String user = p.getString("user");
						String actor = p.getString("actor");
						String albumid = p.getString("albumid");
						String photoid = "";
						if (p.isNull("photoid")) {
							photoid = "";
						} else {
							photoid = p.getString("photoid");
						}

						String type = p.getString("type");
						String createdOn_epoch = p.getString("createdOn_epoch");
						String modifiedOn = p.getString("modifiedOn");
						String createdOn = p.getString("createdOn");
						boolean read = p.getBoolean("read");

						GridViewActivity.mapfeeds.put(_id, new Album_feeds(_id,
								user, actor, albumid, photoid, type,
								createdOn_epoch, createdOn, read, edata,
								modifiedOn));
						album_feed.add(new Album_feeds(_id, user, actor,
								albumid, photoid, type, createdOn_epoch,
								createdOn, read, edata, modifiedOn));
						
					} catch (Exception e) {
						System.out
								.println("Error in the parsing right drawer: ");
						e.printStackTrace();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0 ; i < album_feed.size() ;i++)
			System.out.println("getActor:" + album_feed.get(i).getActor());
		return album_feed;
	}

	@Override
	protected void onPostExecute(List<Album_feeds> products) {
		super.onPostExecute(products);

		for (Album_feeds p : products) {
			mAdapter.add(p);
		}
		mAdapter.notifyDataSetChanged();

	}

}
