package com.code.loop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static Context context = null;
	private static ImageView flag = null;
	private static TextView code = null;
	private static Button signup = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = getApplicationContext();
		/*
		 * String[]
		 * recourseList=this.getResources().getStringArray(R.array.CountryCodes
		 * );
		 * 
		 * final ListView listview = (ListView) findViewById(R.id.listView);
		 * listview.setAdapter(new CountriesListAdapter(this, recourseList));
		 */
		// taking the txtphone.
		// opening the keyboard

		flag = (ImageView) findViewById(R.id.flag);

		flag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(context, chooseFlag.class);
				startActivity(i);
			}

		});

		code = (TextView) findViewById(R.id.code);

		code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(context, chooseFlag.class);
				startActivity(i);
			}

		});

		signup = (Button) findViewById(R.id.signup);

		signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				
				EditText phonenumber = (EditText) findViewById(R.id.phone);
				EditText firstname = (EditText) findViewById(R.id.firstname);
				EditText lastname = (EditText) findViewById(R.id.lastname);
				TextView code = (TextView) findViewById(R.id.code);
				System.out.println(phonenumber.getText().toString().equals("") + " phonenumbe");
				if (phonenumber.getText().toString().equals("") ){
					Utilities.InformativeMessage("Please enter your phone number so your friends can recognize you on Loop.", "Phone number", MainActivity.this,"OK");
					return;
				}
	if (firstname.getText().toString().trim().equals("") ){
		Utilities.InformativeMessage("Please enter your first name so your friends can recognize you on Loop.", "First name", MainActivity.this,"OK");
		return;
				}
				
	if (lastname.getText().toString().trim().equals("") ){
		Utilities.InformativeMessage("Please enter your Last name so your friends can recognize you on Loop.", "Last name", MainActivity.this,"OK");
		return;
	}
	
			String fname = firstname.getText().toString();
			String lname = lastname.getText().toString();
			String phone = phonenumber.getText().toString();
			String tmp = "+(" + 	code.getText().toString().replace("+", "")
				+ ")"
				+ phone;
			String ccode = 	code.getText().toString().replace("+","");
			
			System.out.println("fname:" + fname + "lastname:" + lname + "phone:"+phone+"ccode:"+ccode);
				Utilities
						.AlertMessage(
								MainActivity.this,
								"Is this your correct number? \n\n "+ tmp
										+ "\n\n An SMS with your access code will be sent to this number",
								new Intent(MainActivity.this, FourDigit.class)
								.putExtra("phone", phone.toString())
								.putExtra("ccode", ccode.toString())
								.putExtra("firstname", fname.toString())
								.putExtra("lastname",lname.toString()),
								"Edit", "Yes", "Phone Number Verification");

			}

		});

		TextView txtphone = (TextView) findViewById(R.id.phone);
		txtphone.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(txtphone, InputMethodManager.SHOW_IMPLICIT);
		
	}

	public static void setFlag() {
		System.out.println("setFlag!");
		System.out.println(Utilities.flagchoosen.trim());

		flag.setImageResource(context.getResources().getIdentifier(
				"drawable/" + Utilities.flagchoosen.trim().toLowerCase(), null,
				context.getPackageName()));

		code.setText("+" + Utilities.codecountry);
	}

	

}
