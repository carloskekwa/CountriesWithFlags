package com.code.loop;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

public class chooseFlag extends Activity {
	private Context context = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
	
		
		setContentView(R.layout.activity_main);
		context = getApplicationContext();
		ActionBar actionBar = getActionBar();
		context = getApplicationContext();
		actionBar.setDisplayHomeAsUpEnabled(true);
		String[] recourseList=this.getResources().getStringArray(R.array.CountryCodes);
	    final ListView listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(new CountriesListAdapter(this, recourseList));
        
        
        
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; goto parent activity.
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	

}
