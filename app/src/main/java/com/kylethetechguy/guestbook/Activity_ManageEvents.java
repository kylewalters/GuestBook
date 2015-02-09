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
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_ManageEvents extends Activity {

	/**
	 * Interface object declarations
	 */
	
	ListView eventList;
	ParseUser currentUser = ParseUser.getCurrentUser();
	Adapter_EventParseQuery adapter;
	ParseQueryAdapter.QueryFactory<ParseObject> queryFactory;
	AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		/**
		 * Attaching interface components to their objects
		 */
		
		eventList = (ListView) findViewById(R.id.accountEventViewListView);

		// Calling method to load events that have been created
		loadEvents();

	}

	/**
	 * This method loads events that have been created by the current user.
	 * Due to the ParseQueryAdapter not supporting notifyDataSetChanged, this
	 * method must be used to refresh the view.
	 */
	public void loadEvents() {
		queryFactory = new ParseQueryAdapter.QueryFactory<ParseObject>() {

			@Override
			public ParseQuery<ParseObject> create() {
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Event");
				query.whereEqualTo("owner", currentUser);
				return query;
			}
		};

		adapter = new Adapter_EventParseQuery(this, queryFactory);

		eventList.setAdapter(adapter);
		eventList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentUser.getBoolean("creator")) {
					Intent intent = new Intent(Activity_ManageEvents.this,
							Activity_ManageEventAttendees.class);
					intent.putExtra("event", adapter.getItem(position)
							.getObjectId());
					intent.putExtra("eventName", adapter.getItem(position).getString("title"));
					startActivity(intent);
				}
			}
		});
		
		eventList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				//Alert dialog to show event details
				AlertDialog.Builder eventAlertBuilder = new AlertDialog.Builder(Activity_ManageEvents.this);
				View eventAlertInfo = View.inflate(Activity_ManageEvents.this, R.layout.event_popup_details, null);
				
				/**
				 * Resetting and attaching interface components to their objects
				 */
				TextView eventid = (TextView) eventAlertInfo.findViewById(R.id.eventPopupID);
				TextView eventtitle = (TextView) eventAlertInfo.findViewById(R.id.eventPopupTitle);
				TextView eventaddr = (TextView) eventAlertInfo.findViewById(R.id.eventPopupAddress);
				TextView eventdate = (TextView) eventAlertInfo.findViewById(R.id.eventPopupDate);
				TextView eventst = (TextView) eventAlertInfo.findViewById(R.id.eventPopupStartTime);
				TextView eventet = (TextView) eventAlertInfo.findViewById(R.id.eventPopupEndTime);
				TextView eventcontact = (TextView) eventAlertInfo.findViewById(R.id.eventPopupContact);
				TextView eventdscr = (TextView) eventAlertInfo.findViewById(R.id.eventPopupDescription);
				
				eventid.setText(adapter.getItem(position).getObjectId());
				eventtitle.setText(adapter.getItem(position).getString("title"));
				eventaddr.setText(adapter.getItem(position).getString("address"));
				eventdate.setText(adapter.getItem(position).getString("date"));
				eventst.setText(adapter.getItem(position).getString("startTime"));
				eventet.setText(adapter.getItem(position).getString("endTime"));
				eventcontact.setText(adapter.getItem(position).getString("contactEmail"));
				eventdscr.setText(adapter.getItem(position).getString("description"));
				
				eventAlertBuilder.setCancelable(true).setTitle("Event Details");
				eventAlertBuilder.setView(eventAlertInfo);
				/**
				 * Allow event manager to delete an event
				 */
				eventAlertBuilder.setNeutralButton("Delete Event", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						adapter.getItem(position).deleteInBackground(
								new DeleteCallback() {

									@Override
									public void done(ParseException e) {
										// TODO Auto-generated method
										// stub
										if (e == null) {
											Toast.makeText(
													Activity_ManageEvents.this,
													"Event Deleted",
													Toast.LENGTH_SHORT)
													.show();
										} else {
											Toast.makeText(
													Activity_ManageEvents.this,
													e.getMessage(),
													Toast.LENGTH_SHORT)
													.show();
										}
									}
								});
					}
				});
				AlertDialog eventAlert = eventAlertBuilder.create();
				
				eventAlert.show();
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account, menu);
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
		if (id == R.id.addEvent) {
			Intent intent = new Intent(Activity_ManageEvents.this,
					Activity_CreateEvent.class);
			startActivity(intent);

		}
		if (id == R.id.refresh) {
			loadEvents();
		}
		return super.onOptionsItemSelected(item);
	}

}
