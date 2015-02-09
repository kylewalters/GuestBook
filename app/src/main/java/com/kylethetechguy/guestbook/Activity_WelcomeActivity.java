package com.kylethetechguy.guestbook;

/**
 * Author:	Kyle Walters
 * Date:	12/8/2014
 */

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Activity_WelcomeActivity extends Activity {

	/**
	 * Interface object declarations
	 */

	TextView checkin;
	TextView login;
	TextView create;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/**
		 * If current user exists, go ahead and start proper activity
		 */
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null && currentUser.getBoolean("creator")) {
			Intent intent = new Intent(Activity_WelcomeActivity.this,
					Activity_ManageEvents.class);
			startActivity(intent);
		}
		if (currentUser != null && !currentUser.getBoolean("creator")) {
			Intent intent = new Intent(Activity_WelcomeActivity.this,
					Activity_AttendeeEvents.class);
			startActivity(intent);
		}

		/**
		 * Attaching interface components to their objects
		 */
		checkin = (TextView) findViewById(R.id.mainCheckInTextView);
		login = (TextView) findViewById(R.id.mainLoginTextView);
		create = (TextView) findViewById(R.id.mainSignUpTextView);

		// Listen for check in button tap...
		checkin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Activity_WelcomeActivity.this,
						Activity_EventCheckIn.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		});

		// Listen for login button tap...
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Activity_WelcomeActivity.this,
						Activity_AcctLogin.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		});
		// Listen for create event button tap...
		create.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Activity_WelcomeActivity.this,
						Activity_AcctSignUp.class);
				startActivity(intent);
			}
		});

	}
}
