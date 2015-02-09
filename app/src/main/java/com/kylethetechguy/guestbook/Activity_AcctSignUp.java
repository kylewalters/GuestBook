package com.kylethetechguy.guestbook;

/**
 * Author:	Kyle Walters
 * Date:	12/8/2014
 */

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_AcctSignUp extends Activity {

	/**
	 * Interface object declarations
	 */

	EditText fullname;
	EditText email;
	EditText password;
	EditText confirmPass;
	Button submit;
	String valName;
	String valEmail;
	String valPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		/**
		 * Attaching interface components to their objects
		 */

		submit = (Button) findViewById(R.id.signupSubmitButton);

		// Listen for submit button tap
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Determine is required fields are filled, and passwords match
				if (validateInformation() && validatePassword()) {
					// Creating a new ParseUser to upload
					ParseUser user = new ParseUser();
					user.setUsername(valEmail);
					user.setEmail(valEmail);
					user.setPassword(valPassword);
					user.put("fullname", valName);
					user.put("creator", true);
					user.signUpInBackground(new SignUpCallback() {

						@Override
						public void done(ParseException e) {
							// If there are no exceptions, continue forward
							if (e == null) {
								Toast.makeText(Activity_AcctSignUp.this,
										"Account Created", Toast.LENGTH_SHORT)
										.show();
								finish();
								// If there is an exception, toast to user
							} else {
								Toast.makeText(Activity_AcctSignUp.this,
										e.getMessage(), Toast.LENGTH_SHORT)
										.show();
							}

						}
					});
				}
			}
		});

	}

	/**
	 * This method validates that required fields have been filled by
	 * calculating the length of information entered.
	 * 
	 * @return
	 */
	public boolean validateInformation() {
		fullname = (EditText) findViewById(R.id.signupFirstNameEditText);
		fullname.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		email = (EditText) findViewById(R.id.signupEmailEditText);
		email.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		if (fullname.getText().length() > 0 && email.getText().length() > 0) {
			valName = fullname.getText().toString();
			valEmail = email.getText().toString();
			return true;
		} else {
			Toast.makeText(Activity_AcctSignUp.this, "All fields are required",
					Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	/**
	 * This method validates whether passwords match
	 * 
	 * @return
	 */
	public boolean validatePassword() {
		password = (EditText) findViewById(R.id.signupPasswordEditText);
		password.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		confirmPass = (EditText) findViewById(R.id.signupConfirmPasswordEditText);
		confirmPass.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		if (password.getText().toString()
				.equals(confirmPass.getText().toString())) {
			valPassword = password.getText().toString();
			return true;
		} else {
			Toast.makeText(Activity_AcctSignUp.this, "Passwords do not match",
					Toast.LENGTH_SHORT).show();
		}
		return false;
	}
}
