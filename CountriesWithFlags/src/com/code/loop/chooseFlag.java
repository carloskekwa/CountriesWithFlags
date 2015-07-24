package com.code.loop;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class chooseFlag extends Activity implements
		android.widget.SearchView.OnQueryTextListener {

	private Context context = null;
	private CountriesListAdapter ad = null;
	private SearchView searchView = null;
	ListView listview = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_flags);

		context = getApplicationContext();
		ActionBar actionBar = getActionBar();
		context = getApplicationContext();
		actionBar.setDisplayHomeAsUpEnabled(true);
		searchView = (SearchView) findViewById(R.id.search_country);
		String[] recourseList = this.getResources().getStringArray(
				R.array.CountryCodes);
		List<String> values = new ArrayList<String>();
		for (int i = 0 ; i < recourseList.length ; i++ )
			values.add(recourseList[i]);
		
		ad = new CountriesListAdapter(this, values);
		listview = (ListView) findViewById(R.id.listView);
		listview.setAdapter(ad);
		// Sets the default or resting state of the search field.
		// If true, a single search icon is shown by default and
		// expands to show the text field and other buttons when pressed
		listview.setTextFilterEnabled(true);
		setupSearchView();
		

	}

	@Override
	public boolean onQueryTextChange(String newText) {

		if (TextUtils.isEmpty(newText)) {
			listview.clearTextFilter();
		} else {
			listview.setFilterText(newText);
		}
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	private void setupSearchView() {
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Choose your country");
		 int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
		        TextView textView = (TextView) searchView.findViewById(id);
		        textView.setTextColor(Color.GRAY);
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
