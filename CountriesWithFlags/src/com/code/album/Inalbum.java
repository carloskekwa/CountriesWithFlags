package com.code.album;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.code.home.AlbumsList;
import com.code.home.HomeAdapter;
import com.code.home.LoaderHome;
import com.code.loop.R;
import com.code.loop.Utilities;

public class Inalbum extends Activity {

	private int REQUEST_CAMERA = 501;
	private int SELECT_FILE = 502;
	private List<NameValuePair> nameValuePairs = null;
	private AlbumsList albumselected = null;
	private final ProdAdapter mAdapter = null;
	private ListView lv = null;
	
	
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
			setContentView(R.layout.inalbum);

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
				
				
				
			}else{
				
			//	Utilities.fetchGET(Utilities.urlapp + "albums/" + albumselected.getAlbumid() + "/details/");
				
				
				//create here the album..
			//	ProdAdapter mAdapter = new ProdAdapter(this);
			//	lv = (ListView) findViewById(R.id.listView1);
				
				//LoaderTask loadhome = new LoaderTask(this, mAdapter,albumselected);
				//lv.setAdapter(mAdapter);
				//loadhome.execute();
				
				
				
			}
			// "Loading...", true);

		} catch (Exception e) {

		}
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

		AlertDialog.Builder builder = new AlertDialog.Builder(Inalbum.this);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

				File destination = new File(
						Environment.getExternalStorageDirectory(),
						System.currentTimeMillis() + ".jpg");

				FileOutputStream fo;
				try {
					destination.createNewFile();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
//destination.get
// getPath
				nameValuePairs = new ArrayList<NameValuePair>(4);
				nameValuePairs.add(new BasicNameValuePair("photo1", "@" + 
						destination.getAbsolutePath().toString()));
				nameValuePairs.add(new BasicNameValuePair("caption", 
						"Android"));
				nameValuePairs.add(new BasicNameValuePair("albums", 
						"[" + albumselected.getAlbumid() + "]"));
				
				String json = "{" + "\"photo1\":" + "{\"height\":" + thumbnail.getHeight() + ",\"width\":" + thumbnail.getWidth() + "}}";
				
				System.out.println("json:" + json + "- destination:" + destination.getAbsolutePath().toString());
				nameValuePairs.add(new BasicNameValuePair("dimensions", json
						));
				
		
				new RequestTask(nameValuePairs, Utilities.urlapp + "photos").execute();
				//

			} else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();
				String[] projection = { MediaColumns.DATA };
				Cursor cursor = managedQuery(selectedImageUri, projection,
						null, null, null);
				int column_index = cursor
						.getColumnIndexOrThrow(MediaColumns.DATA);
				cursor.moveToFirst();

				String selectedImagePath = cursor.getString(column_index);

				Bitmap bm;
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(selectedImagePath, options);
				final int REQUIRED_SIZE = 200;
				int scale = 1;
				while (options.outWidth / scale / 2 >= REQUIRED_SIZE
						&& options.outHeight / scale / 2 >= REQUIRED_SIZE)
					scale *= 2;
				options.inSampleSize = scale;
				options.inJustDecodeBounds = false;
				bm = BitmapFactory.decodeFile(selectedImagePath, options);

				// ivImage.setImageBitmap(bm);
			}
		}
		

	}

	
	
	
	public  String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	
	
	
	class RequestTask extends AsyncTask<String, String, String>{

		private List<NameValuePair> namevaluepairs = null;
		private String url = "";
		public RequestTask(List<NameValuePair> namevaluepairs ,String url){
			this.namevaluepairs = namevaluepairs;
			this.url = url;
		}
		String response = "";
	    @Override
	    protected String doInBackground(String... uri) {
	    	response = 	Utilities.postMultipart(nameValuePairs, Utilities.urlapp + "photos/");
	    	return response;
	    	
	    
	    }
	    
	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        System.out.println("Response: " + response);
	    }
	    
	    
	    
	    
	}
	
	
	


}
