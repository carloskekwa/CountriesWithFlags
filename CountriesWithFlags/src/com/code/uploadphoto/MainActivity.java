package com.code.uploadphoto;

import info.androidhive.imageslider.adapter.GridViewSDAdapter;
import info.androidhive.imageslider.helper.AppConstant;
import info.androidhive.imageslider.helper.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.code.loop.R;

public class MainActivity extends Activity {
	GridView grid;
	private Utils utils = null;
	private ArrayList<String> _filepaths = null;
	private int columnWidth = -1;
	private static final int REQUEST_IMAGE = 501;
	private String imagePath;
	private File destination;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gridview_updatecamera);
		grid = (GridView) findViewById(R.id.grid_view);
		utils = new Utils(this); // Initilizing Grid View
		InitilizeGridLayout();

		_filepaths = utils.getFilePaths();
		GridViewSDAdapter adapter = new GridViewSDAdapter(MainActivity.this,
				_filepaths, columnWidth);

		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast.makeText(MainActivity.this, "You Clicked at " +web[+
				// position], Toast.LENGTH_SHORT).show();

			}
		});

	}

	private void InitilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				AppConstant.GRID_PADDING, r.getDisplayMetrics());

		columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);

		grid.setNumColumns(AppConstant.NUM_OF_COLUMNS);
		grid.setColumnWidth(columnWidth);
		grid.setStretchMode(GridView.NO_STRETCH);
		grid.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);
		grid.setHorizontalSpacing((int) padding);
		grid.setVerticalSpacing((int) padding);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("requestCode" + requestCode  + "resultCode:"  + resultCode);
		
		final Intent d = data;

		// instagramm------------------------------------------------------
		if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
			super.onActivityResult(requestCode, resultCode, data);
			try {

				// Get our saved file into a bitmap object:
				File f = new File(Environment.getExternalStorageDirectory()
						+ File.separator + "image.jpg");
				System.out.println(f.getAbsolutePath());

				final Bitmap bitmap = BitmapFactory
						.decodeStream(new FileInputStream(f));

				// final Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0,
				// bmp.getWidth(), bmp.getHeight(), mat, true);
				ByteArrayOutputStream outstudentstreamOutputStream = new ByteArrayOutputStream();
				// bitmap.compress(Bitmap.CompressFormat.PNG, 100,
				// outstudentstreamOutputStream);

				System.out.println(requestCode + "requestcode");
				System.out.println(resultCode + "Resultcodee");
				System.out.println();
				
			}catch(Exception e){
				
			}
		}
	}

	public String dateToString(Date date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

}