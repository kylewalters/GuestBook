package com.kylethetechguy.guestbook;

/**
 * Author:	Kyle Walters
 * Date:	12/8/2014
 */

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_AcctLogin extends Activity {

	/**
	 * Interface object declarations
	 */
	EditText email;
	EditText password;
	Button submit;
	ParseUser currentUser = ParseUser.getCurrentUser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		/**
		 * Determine if user is already logged in, and if so, send them to the
		 * appropriate activity.
		 */
		if (currentUser != null) {
			if (currentUser.getBoolean("creator")) {
				Intent intent = new Intent(Activity_AcctLogin.this,
						Activity_ManageEvents.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		}

		/**
		 * Attaching interface components to their objects
		 */
		email = (EditText) findViewById(R.id.loginUsernameEditText);
		email.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		password = (EditText) findViewById(R.id.loginPasswordEditText);
		password.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		submit = (Button) findViewById(R.id.loginSubmitButton);

		/**
		 * Listen for a submit button tap
		 */
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// On tap, start the user login process, and get result
				ParseUser.logInInBackground(email.getText().toString(),
						password.getText().toString(), new LogInCallback() {

							@Override
							public void done(ParseUser user, ParseException e) {
								// If there are no exceptions, continue forward
								if (user != null) {
									Intent intent = null;
									/**
									 * If the user is an event creator, (not a
									 * Check-in account), send them to
									 * appropriate activity
									 */

									if (user.getBoolean("creator")) {
										intent = new Intent(
												Activity_AcctLogin.this,
												Activity_ManageEvents.class);
									}
									/**
									 * If the user is a Check-In user, send them
									 * to appropriate activity.
									 */
									if (!user.getBoolean("creator")) {
										intent = new Intent(
												Activity_AcctLogin.this,
												Activity_AttendeeEvents.class);
									}
									/**
									 * Welcome user, if they are an event
									 * creator
									 */
									if (currentUser == null) {
										Toast.makeText(Activity_AcctLogin.this,
												"Welcome", Toast.LENGTH_SHORT)
												.show();
									} else {
										Toast.makeText(Activity_AcctLogin.this,
										"Welcome, "
												+ user.getString("fullname"),
										Toast.LENGTH_SHORT).show();
									}
									// Remove the AcctLogin activity after
									// completion
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									startActivity(intent);
									finish();
								} else {
									Toast.makeText(Activity_AcctLogin.this,
											e.getMessage(), Toast.LENGTH_SHORT)
											.show();
								}
							}
						});
			}
		});
	}

}
