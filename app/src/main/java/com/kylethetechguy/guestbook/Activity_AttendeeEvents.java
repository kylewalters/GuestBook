package com.kylethetechguy.guestbook;

/**
 * Author:	Kyle Walters
 * Date:	12/8/2014
 */

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Activity_AttendeeEvents extends Activity {

	/**
	 * Interface object declarations
	 */

	ListView eventList;
	ParseUser currentUser = ParseUser.getCurrentUser();
	Adapter_EventParseQuery adapter;
	ParseQueryAdapter.QueryFactory<ParseObject> queryFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendee_registered_events);

		/**
		 * Attaching interface components to their objects
		 */

		eventList = (ListView) findViewById(R.id.attendeeRegisteredEventsListView);

		// Calling method to load events that have been checked in to
		loadEvents();
	}

	/**
	 * Due to the ParseQueryAdapter not supporting notifyDataSetChanged, this
	 * method must be used to refresh the view.
	 */
	public void loadEvents() {
		queryFactory = new ParseQueryAdapter.QueryFactory<ParseObject>() {

			@Override
			public ParseQuery<ParseObject> create() {
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
						"Attendee");
				query.whereEqualTo("owner", currentUser);
				// Includes the event object associated with the object pointer
				// in Parse
				query.include("event");
				return query;
			}
		};

		adapter = new Adapter_EventParseQuery(this, queryFactory);

		eventList.setAdapter(adapter);
		eventList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Alert dialog to show event information popup
				AlertDialog.Builder eventAlertBuilder = new AlertDialog.Builder(
						Activity_AttendeeEvents.this);
				View eventAlertInfo = View.inflate(
						Activity_AttendeeEvents.this,
						R.layout.event_popup_details, null);

				/**
				 * Attaching interface components to their objects after refresh
				 */

				TextView eventid = (TextView) eventAlertInfo
						.findViewById(R.id.eventPopupID);
				TextView eventtitle = (TextView) eventAlertInfo
						.findViewById(R.id.eventPopupTitle);
				TextView eventaddr = (TextView) eventAlertInfo
						.findViewById(R.id.eventPopupAddress);
				TextView eventdate = (TextView) eventAlertInfo
						.findViewById(R.id.eventPopupDate);
				TextView eventst = (TextView) eventAlertInfo
						.findViewById(R.id.eventPopupStartTime);
				TextView eventet = (TextView) eventAlertInfo
						.findViewById(R.id.eventPopupEndTime);
				TextView eventcontact = (TextView) eventAlertInfo
						.findViewById(R.id.eventPopupContact);
				TextView eventdscr = (TextView) eventAlertInfo
						.findViewById(R.id.eventPopupDescription);

				/**
				 * After getting objects from Parse, grab string from current
				 * object and set TextViews
				 */

				eventid.setText(adapter.getItem(position)
						.getParseObject("event").getObjectId());

				eventtitle.setText(adapter.getItem(position)
						.getParseObject("event").getString("title"));
				eventaddr.setText(adapter.getItem(position)
						.getParseObject("event").getString("address"));
				eventdate.setText(adapter.getItem(position)
						.getParseObject("event").getString("date"));
				eventst.setText(adapter.getItem(position)
						.getParseObject("event").getString("startTime"));
				eventet.setText(adapter.getItem(position)
						.getParseObject("event").getString("endTime"));
				eventcontact.setText(adapter.getItem(position)
						.getParseObject("event").getString("contactEmail"));
				eventdscr.setText(adapter.getItem(position)
						.getParseObject("event").getString("description"));

				eventAlertBuilder.setCancelable(true).setTitle("Event Details");
				eventAlertBuilder.setView(eventAlertInfo);
				AlertDialog eventAlert = eventAlertBuilder.create();

				eventAlert.show();

			}

		});

		// Listening for long tap
		eventList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// Alert dialog to allow event check in to be deleted
				AlertDialog.Builder deleteAlertDialogBuilder = new AlertDialog.Builder(
						Activity_AttendeeEvents.this);
				deleteAlertDialogBuilder
						.setTitle("Remove")
						.setMessage("Remove your registration from this event?")
						.setCancelable(true);

				deleteAlertDialogBuilder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							/**
							 * When selected, remove Attendee object from Parse
							 */
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								adapter.getItem(position).deleteInBackground(
										new DeleteCallback() {

											@Override
											public void done(ParseException e) {
												// TODO Auto-generated method
												// stub
												if (e == null) {
													Toast.makeText(
															Activity_AttendeeEvents.this,
															"Registration Deleted",
															Toast.LENGTH_SHORT)
															.show();
												} else {
													Toast.makeText(
															Activity_AttendeeEvents.this,
															e.getMessage(),
															Toast.LENGTH_SHORT)
															.show();
												}
											}
										});
							}
						});
				deleteAlertDialogBuilder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				AlertDialog deleteAlert = deleteAlertDialogBuilder.create();
				deleteAlert.show();
				return true;
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.attendee_registered_events, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.logout) {
			ParseUser.logOut();
			finish();
		}
		if (id == R.id.refresh) {
			loadEvents();
		}
		if (id == R.id.checkin) {
			Intent intent = new Intent(Activity_AttendeeEvents.this,
					Activity_EventCheckIn.class);
			// Pass known information to Check In so replicated information
			// isn't retyped
			intent.putExtra("fullname", currentUser.getString("fullname"));
			intent.putExtra("email", currentUser.getString("email"));
			intent.putExtra("password", currentUser.getString("password"));
			intent.putExtra("address", currentUser.getString("address"));
			intent.putExtra("phone", currentUser.getString("phone"));
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
