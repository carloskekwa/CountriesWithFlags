package com.code.loop;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.code.*;

public class FourDigit extends Activity {

	private Context context = null;
	private Button decimal = null;
	private CountDownTimer countDownTimer = null;
	private long totalTimeCountInMilliseconds = 180000; // total count down time
														// in milliseconds
	private long timeBlinkInMilliseconds = 1000;
	private boolean blink = true;
	private String phone = "";
	private String firstname = "";
	private String lastname = "";
	private String ccode = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.codevalidation);
		try {
			ActionBar actionBar = getActionBar();
			context = getApplicationContext();
			actionBar.setDisplayHomeAsUpEnabled(true);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			TextView info = (TextView) findViewById(R.id.info);
			final EditText fourdigit = (EditText) findViewById(R.id.validationcode);
			
			fourdigit.setOnKeyListener(new OnKeyListener()
			{
			    public boolean onKey(View v, int keyCode, KeyEvent event)
			    {
			        if (event.getAction() == KeyEvent.ACTION_DOWN)
			        {
			            switch (keyCode)
			            {
			                case KeyEvent.KEYCODE_DPAD_CENTER:
			                case KeyEvent.KEYCODE_ENTER:
			               
			                	if (!fourdigit.getText().toString().trim().equals("")){
			                	 	new RequestTask().execute("sendConfirmation,"+ 	fourdigit.getText().toString());
			                	}
			               
			                    
			                    return true;
			                default:
			                    break;
			            }
			        }
			        return false;
			    }
			});
			
			
			fourdigit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					fourdigit.setText("");
				}

			});

			Intent i = getIntent();

			decimal = (Button) findViewById(R.id.decimal);
			decimal.setEnabled(false);
			phone = i.getStringExtra("phone");
			firstname = i.getStringExtra("firstname");
			lastname = i.getStringExtra("lastname");
			ccode = i.getStringExtra("ccode");
			Utilities.codecountry = ccode;

			decimal.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// call phone url
					decimal.setEnabled(false);
					new RequestTask().execute("sendvalidation");

					setActionListeners();

				}
			});
			String tmp = "+(" + ccode + ")" + phone;
			info.setText(tmp);
			
			// call phone url
			new RequestTask().execute("sendvalidation");

			setActionListeners();	

		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
		}
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

	private void setActionListeners() {

		decimal.setTextAppearance(getApplicationContext(), R.style.normalText);

		countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 500) {
			// 500 means, onTick function will be called at every 500
			// milliseconds

			@Override
			public void onTick(long leftTimeInMilliseconds) {
				long seconds = leftTimeInMilliseconds / 1000;

				if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {
					decimal.setTextAppearance(getApplicationContext(),
							R.style.blinkText);
					// change the style of the textview .. giving a red alert
					// style

					if (blink) {
						decimal.setVisibility(View.VISIBLE);
						// if blink is true, textview will be visible
					} else {
						decimal.setVisibility(View.INVISIBLE);
					}

					blink = !blink; // toggle the value of blink
				}

				decimal.setText(String.format("%02d", seconds / 60) + ":"
						+ String.format("%02d", seconds % 60));
				// format the textview to show the easily readable format
			}

			@Override
			public void onFinish() {
				// this function will be called when the timecount is finished
				decimal.setText("Resend Verification Code");
				decimal.setVisibility(View.VISIBLE);
				decimal.setEnabled(true);
			}

		}.start();

	}

	class RequestTask extends AsyncTask<String, String, String>{

		String response = "";
	    @Override
	    protected String doInBackground(String... uri) {
	       System.out.println("doInBackground:" + uri[0].toString());
	    	if (uri[0].toString().equals("sendvalidation")){
	    	 	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
				nameValuePairs.add(new BasicNameValuePair("ccode",  ccode));
				nameValuePairs.add(new BasicNameValuePair("phone", phone));
				nameValuePairs.add(new BasicNameValuePair("fname", firstname));
				nameValuePairs.add(new BasicNameValuePair("lname", lastname));
				try {
					response = Utilities.postData(nameValuePairs,Utilities.urlapp + "users/");
					System.out.println("sendvalidation!!!");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
	    	}else if (uri[0].toString().contains("sendConfirmation")){
	    		String[] tmp  = uri[0].toString().split(",");
	    		System.out.println("4digit:" + tmp[1]);
	    		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
				nameValuePairs.add(new BasicNameValuePair("code", tmp[1]));
				
			Utilities.deviceid = Secure.getString(getApplicationContext()
						.getContentResolver(), Secure.ANDROID_ID);
				Log.e("LOG", "android id >>" + 	
						Utilities.deviceid);
				
				
				nameValuePairs.add(new BasicNameValuePair("device", Utilities.deviceid));
				try {
					 response = Utilities.postData(nameValuePairs, Utilities.urlapp +  "users/" + ccode + phone + "/verify/");
					System.out.println("SndConfirmation!!");
					return "sendConfirmation";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
	    	}
	   
	    	
	    	return "";
	    }

	    @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        //Do anything with response..
	        System.out.println("onPostExecute");
	        
	        if (result.equals("sendConfirmation")){
	        		System.out.println(response);
	        		JSONObject a = null;
					try {
						a = new JSONObject(response);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		try {
						Utilities.key = a.getString("key");
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		
	        		System.out.println("key: " + Utilities.key);
	        		if (Utilities.key != ""){
	        			Utilities.phone = phone;
	        			Intent i = new Intent(context,com.code.home.Home.class);
	        			startActivity(i);
	        		}
	        }
	    }
	}	
	
	
}
