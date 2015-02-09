package com.kylethetechguy.guestbook;

/**
 * Author:	Kyle Walters
 * Date:	12/8/2014
 */

import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Intents;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Activity_AttendeeDetails extends Activity {

	/**
	 * Interface object declarations
	 */

	TextView attendeeName;
	TextView attendeeEmail;
	TextView attendeePhone;
	TextView attendeeAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendee_details);

		/**
		 * Attaching interface components to their objects
		 */

		attendeeName = (TextView) findViewById(R.id.attendeeNameTextView);
		attendeeEmail = (TextView) findViewById(R.id.attendeeEmailTextView);
		attendeePhone = (TextView) findViewById(R.id.attendeePhoneTextView);
		attendeeAddress = (TextView) findViewById(R.id.attendeeAddressTextView);

		/**
		 * Setting TextViews to strings received from Intent
		 */

		attendeeName.setText(getIntent().getExtras().getString("name"));
		attendeeEmail.setText(getIntent().getExtras().getString("email"));
		attendeePhone.setText(getIntent().getExtras().getString("phone"));
		attendeeAddress.setText(getIntent().getExtras().getString("address"));

		// Listening for tap on TextView
		attendeeName.setOnClickListener(new View.OnClickListener() {

			/**
			 * When tapped, start Intent to add attendee to contact book, with
			 * all known information
			 */
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intents.Insert.ACTION);
				intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
				intent.putExtra(Intents.Insert.NAME, attendeeName.getText()
						.toString());
				intent.putExtra(Intents.Insert.EMAIL, attendeeEmail.getText()
						.toString());
				intent.putExtra(Intents.Insert.POSTAL, attendeeAddress
						.getText().toString());
				intent.putExtra(Intents.Insert.PHONE, attendeePhone.getText()
						.toString());
				intent.putExtra("finishActivityOnSaveCompleted", true);
				startActivity(intent);
			}
		});

		// Listening for tap on TextView
		attendeeEmail.setOnClickListener(new View.OnClickListener() {

			/**
			 * When tapped, start Intent to send an email with recipient filled
			 * in
			 */
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_EMAIL, attendeeEmail.getText()
						.toString());
				startActivity(Intent.createChooser(intent, "Send Email"));
			}
		});

		// Listening for tap on TextView
		attendeePhone.setOnClickListener(new OnClickListener() {

			/**
			 * When tapped, start Intent to start dialer with phone number
			 * filled in
			 */
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ attendeePhone.getText().toString()));
				startActivity(intent);
			}
		});

		// Listening for tap on TextView
		attendeeAddress.setOnClickListener(new OnClickListener() {

			/**
			 * When tapped, start Intent to start Google Maps with address
			 */
			@Override
			public void onClick(View v) {
				String uri = String.format(Locale.ENGLISH, "geo:0,0?q="
						+ attendeeAddress.getText().toString());
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				startActivity(intent);
			}
		});
	}
}
