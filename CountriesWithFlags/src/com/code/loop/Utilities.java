package com.code.loop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utilities {

	public static String flagchoosen = "";
	public static String codecountry = "";
	public static String urlapp = "http://Prod.getloop.io/";
	public static String deviceid = "";
	public static String key = "";
	public static ProgressDialog dialog = null;// dialog box
	private static int REQUEST_CAMERA = 501;
	private static int SELECT_FILE = 502;
	public static String phone = "";

	public static String GetCountryZipCode(String ssid) {
		Locale loc = new Locale("", ssid);

		return loc.getDisplayCountry().trim();
	}

	
	

	public static int daysBetween(long t1, long t2) {
		return (int) ((t2 - t1) / (1000 * 60 * 60 * 24));
	}
	
	/*
	 * Ask button
	 */
	@SuppressWarnings("unused")
	public static void AlertMessage(final Context context, String Message,
			final Intent i, String leftbutton, String rightbutton, String title) {
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

								((Activity) context).startActivity(i);
							}
						})
				.setNegativeButton(leftbutton,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {

								dialog.cancel();

							}
						});

		alert = builder.create();
		alert.show();

	}

	/*
	 * informative alert
	 */
	public static void InformativeMessage(String s, String Title,
			Context context, String buttontxt) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(Title);
		builder.setMessage(s);
		builder.setPositiveButton(buttontxt,
				new DialogInterface.OnClickListener() {
					public void onClick(
							@SuppressWarnings("unused") final DialogInterface dialog,
							@SuppressWarnings("unused") final int id) {
						dialog.dismiss();
					}
				});
		builder.create();
		builder.show();

	}
/*
 * Post call
 */
	public static String postData(List<NameValuePair> nameValuePairs, String url) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		System.out.println(url);
		HttpPost httppost = new HttpPost(url);
		System.out.println("postData@!");
		HttpResponse response = null;
		try {
			// Add your data
			// authorization
			if (!Utilities.key.equals(""))
				httppost.setHeader("Authorization", "Token " + Utilities.key);
			System.out.println("postdata");
			if (nameValuePairs != null)
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			response = httpclient.execute(httppost);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		HttpEntity entity = response.getEntity();
		String responseString = null;
		try {
			responseString = EntityUtils.toString(entity, "UTF-8");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(responseString);
		return responseString;
	}

	/*
	 * post with multi-part
	 */
	public static String postMultipart(List<NameValuePair> nameValuePairs,
			String url) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		System.out.println(url);
		HttpPost httppost = new HttpPost(url);
		System.out.println("postData@!Multipart");
		HttpResponse response = null;
		try {
			// Add your data
			// authorization
			if (!Utilities.key.equals(""))
				httppost.setHeader("Authorization", "Token " + Utilities.key);
			System.out.println("postdata");

			MultipartEntity multipartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			for (int i = 0; i < nameValuePairs.size(); i++) {

				multipartEntity.addPart(nameValuePairs.get(i).getName(),
						new StringBody(nameValuePairs.get(i).getValue()));
			}
			httppost.setEntity(multipartEntity);
			// Execute HTTP Post Request
			response = httpclient.execute(httppost);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		HttpEntity entity = response.getEntity();
		String responseString = null;
		try {
			responseString = EntityUtils.toString(entity, "UTF-8");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(responseString);
		return responseString;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
	/*
	 * Getcall
	 */
	public static String fetchGET(String urltofetch)
			throws InterruptedException {

		// --this is a fake method to generate json and take some time--
		// --to simulate network loading--

		String parsedString = "";

		try {

			URL url = new URL(urltofetch);
			System.out.println(url);
			URLConnection conn = url.openConnection();

			System.out.println("postdata");
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			if (!Utilities.key.equals(""))
				httpConn.setRequestProperty("Authorization", "Token "
						+ Utilities.key);
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");

			httpConn.addRequestProperty("idplacefoursquare",
					"idplacefoursquare");
			httpConn.connect();
			InputStream is = httpConn.getInputStream();
			parsedString = Utilities.convertinputStreamToString(is);
			System.out.println(parsedString + "fd");

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(parsedString);
		return parsedString;
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

	/*
	 * get contacts of the phone
	 */
	public static Map<String, String> getAllContacts(ContentResolver cr) {
		Map<String, String> contacts = new HashMap<String, String>();
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
		}

		phones.close();
		return contacts;
	}

	/*
	 * select image from camera or the other one!
	 */
	public static void selectImage(final Context context) {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					((Activity) context).startActivityForResult(intent,
							REQUEST_CAMERA);
				} else if (items[item].equals("Choose from Library")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					((Activity) context).startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

}
