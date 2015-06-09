package com.code.home;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.code.loop.R;
import com.code.loop.Utilities;

public class Home extends Activity {

	private ListView lv = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			Utilities.dialog = ProgressDialog.show(Home.this, "", "Loading...",
					true);
			HomeAdapter mAdapter = new HomeAdapter(this);
			lv = (ListView) findViewById(R.id.listView);
			
			LoaderHome loadhome = new LoaderHome(this, mAdapter,lv);
			lv.setAdapter(mAdapter);
			loadhome.execute();
		} catch (Exception e) {

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Event Handling for Individual menu item selected Identify single menu
	 * item by it's id
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.newgroup:

			Intent i = new Intent(getApplicationContext(), Display.class);
			startActivity(i);
			return true;

		case android.R.id.home:
			this.finish();
			// Do stuff

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
