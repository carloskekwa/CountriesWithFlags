package info.androidhive.imageslider.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.code.loop.R;
import com.squareup.picasso.Picasso;

public class GridViewSDAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> _filePaths = new ArrayList<String>();
	private int imageWidth;
	private static int REQUEST_CAMERA = 501;

	public GridViewSDAdapter(Context c, ArrayList<String> _filePaths,
			int imageWidth) {
		mContext = c;
		this._filePaths = _filePaths;
		// this.web = web;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _filePaths.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View grid;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {

			if (position == 0) {
				grid = new View(mContext);
				grid = inflater.inflate(R.layout.grid_single, null);
				CheckBox chk = (CheckBox) grid.findViewById(R.id.chk);
				chk.setVisibility(View.GONE);
				ImageView imageView = (ImageView) grid.findViewById(R.id.imgsd);

				// get screen dimensions
				//BitmapDrawable image = new BitmapDrawable(_filePaths.get(position));


				/*imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
						imageWidth));*/
				Picasso.with(mContext).load(R.drawable.camera).into(imageView);
				
				imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub  fff
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						((Activity) mContext).startActivityForResult(intent,
								REQUEST_CAMERA);
					}
				});
				//Picasso.with(mContext).
				//Picasso.with(mContext).load(_filePaths.get(position)).into(imageView);
				//imageView.setBackgroundDrawable(Integer.valueOf(_filePaths.get(position)));
			} else {

				grid = new View(mContext);
				grid = inflater.inflate(R.layout.grid_single, null);
				CheckBox chk = (CheckBox) grid.findViewById(R.id.chk);
				ImageView imageView = (ImageView) grid.findViewById(R.id.imgsd);

				// get screen dimensions
				Bitmap image = decodeFile(_filePaths.get(position), imageWidth,
						imageWidth);

				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
						imageWidth));
				imageView.setImageBitmap(image);

				// textView.setText(web[position]);
				// imageView.setImageResource();
			}
		} else {
			grid = (View) convertView;
		}

		return grid;
	}

	/*
	 * Resizing image size
	 */
	public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
		try {

			File f = new File(filePath);

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			final int REQUIRED_WIDTH = WIDTH;
			final int REQUIRED_HIGHT = HIGHT;
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
					&& o.outHeight / scale / 2 >= REQUIRED_HIGHT)
				scale *= 2;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}