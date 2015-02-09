package com.kylethetechguy.guestbook;

/**
 * Author:	Kyle Walters
 * Date:	12/8/2014
 */

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_EventCheckIn extends Activity {

	/**
	 * Interface object declarations
	 */

	EditText fullname;
	EditText email;
	EditText password;
	EditText confirmPass;
	EditText eventId;
	EditText address;
	EditText phone;
	Button submit;
	String eventName;
	ParseUser currentUser = ParseUser.getCurrentUser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_in);

		/**
		 * Attaching interface components to their objects
		 */

		fullname = (EditText) findViewById(R.id.checkinFullnameEditText);
		fullname.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		email = (EditText) findViewById(R.id.checkinEmailEditText);
		email.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		password = (EditText) findViewById(R.id.checkinPasswordEditText);
		password.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		confirmPass = (EditText) findViewById(R.id.checkinConfirmPassEditText);
		confirmPass.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		eventId = (EditText) findViewById(R.id.checkinEventIDEditText);
		eventId.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		address = (EditText) findViewById(R.id.checkinAddressEditText);
		address.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		phone = (EditText) findViewById(R.id.checkinPhoneEditText);
		phone.setImeOptions(EditorInfo.IME_ACTION_NEXT);

		/**
		 * If user is logged in, and information exists, pre-populate
		 * information
		 */
		if (currentUser != null && !currentUser.getBoolean("creator")) {
			fullname.setText(getIntent().getExtras().getString("fullname"));
			email.setText(getIntent().getExtras().getString("email"));
			password.setText("Already Logged In");
			confirmPass.setText("Already Logged In");
			address.setText(getIntent().getExtras().getString("address"));
			phone.setText(getIntent().getExtras().getString("phone"));
		}

		submit = (Button) findViewById(R.id.checkinSubmitButton);

		// Listen for submit button tap
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
						"Event");
				query.whereEqualTo("objectId", eventId.getText().toString());
				query.setLimit(1); // There should only ever be one value, so
									// this ensures it to make access safer.
				query.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> objects, ParseException e) {
						if (objects.size() > 0) {
							ParseObject event = objects.get(0);
							// Validate all fields are filled, and passwords match
							if (validateInfo()) {
								if (validatePasswords()) {
									registerUser(event);
								} else {
									Toast.makeText(Activity_EventCheckIn.this,
											"Passwords Do No Match",
											Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(Activity_EventCheckIn.this,
										"Name And Email Is Required",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(Activity_EventCheckIn.this,
									"Event Not Found", Toast.LENGTH_SHORT)
									.show();
						}
					}
				});

			}
		});

	}

	/**
	 * This method created a ParseUser and registers them
	 * @param event
	 */
	public void registerUser(final ParseObject event) {
		if (currentUser == null) {
			final ParseUser user = new ParseUser();
			user.setUsername(email.getText().toString());
			user.setEmail(email.getText().toString());
			user.setPassword(password.getText().toString());
			user.put("creator", false);
			user.signUpInBackground(new SignUpCallback() {
				@Override
				public void done(ParseException e) {

					if (e == null) {
						registerForEvent(user, event);
					} else {
						Toast.makeText(Activity_EventCheckIn.this,
								e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		if (currentUser != null) {
			registerForEvent(currentUser, event);
		}
	}

	/**
	 * This method registers the current user for an event
	 * @param user
	 * @param event
	 */
	public void registerForEvent(ParseUser user, final ParseObject event) {
		ParseObject attendee = new ParseObject("Attendee");
		attendee.put("fullname", fullname.getText().toString());
		attendee.put("email", email.getText().toString());
		attendee.put("address", address.getText().toString());
		attendee.put("phone", phone.getText().toString());
		attendee.put("owner", user);
		attendee.put("event", event);
		attendee.put("eventId", eventId.getText().toString());
		attendee.saveEventually(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(
							Activity_EventCheckIn.this,
							"You Have Registered For "
									+ event.getString("title"),
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(Activity_EventCheckIn.this, e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * This method validates that required fields are filled
	 * @return
	 */
	public boolean validateInfo() {
		if (fullname.getText().length() > 0 && email.getText().length() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * This method validates that password match
	 * @return
	 */
	public boolean validatePasswords() {
		if (!password.getText().toString()
				.equals(confirmPass.getText().toString())) {
			return false;
		}
		return true;
	}
}
