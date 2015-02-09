package com.kylethetechguy.guestbook;

/**
 * Author:	Kyle Walters
 * Date:	12/8/2014
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Activity_ManageEventAttendees extends Activity {

	/**
	 * Interface object declarations
	 */

	ListView attendeeList;
	ParseUser currentUser = ParseUser.getCurrentUser();
	Adapter_AttendeeParseQuery adapter;
	ParseQueryAdapter.QueryFactory<ParseObject> queryFactory;
	List<ParseObject> eventList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_manager);

		/**
		 * Attaching interface components to their objects
		 */

		attendeeList = (ListView) findViewById(R.id.attendeeListView);

		eventList = new ArrayList<ParseObject>();

		// Calling method to load attendees that have checked in to event
		loadEvents();
	}

	/**
	 * This method loads attendees who have registered for the selected event.
	 * Due to the ParseQueryAdapter not supporting notifyDataSetChanged, this
	 * method must be used to refresh the view.
	 */
	public void loadEvents() {
		queryFactory = new ParseQueryAdapter.QueryFactory<ParseObject>() {

			@Override
			public ParseQuery<ParseObject> create() {
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
						"Attendee");
				query.whereEqualTo("eventId", getIntent().getExtras()
						.getString("event"));
				query.include("event"); // Include object associated with
										// pointer
				query.include("owner"); // Include object associated with
										// pointer
				Toast.makeText(Activity_ManageEventAttendees.this,
						getIntent().getExtras().getString("event"),
						Toast.LENGTH_SHORT).show();
				return query;
			}
		};

		adapter = new Adapter_AttendeeParseQuery(this, queryFactory);

		adapter.addOnQueryLoadListener(new OnQueryLoadListener<ParseObject>() {

			@Override
			public void onLoaded(List<ParseObject> objects, Exception e) {
				// TODO Auto-generated method stub
				eventList = objects;
				if (e != null) {
					Log.d("Error", e.getMessage());
				}
			}

			@Override
			public void onLoading() {
				// TODO Auto-generated method stub

			}
		});

		adapter.setTextKey("fullname");

		attendeeList.setAdapter(adapter);
		attendeeList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Activity_ManageEventAttendees.this,
						Activity_AttendeeDetails.class);
				intent.putExtra("name",
						adapter.getItem(position).getString("fullname"));
				intent.putExtra("email",
						adapter.getItem(position).getString("email"));
				intent.putExtra("phone",
						adapter.getItem(position).getString("phone"));
				intent.putExtra("address",
						adapter.getItem(position).getString("address"));
				startActivity(intent);
			}
		});

	}

	/**
	 * This method takes all Parse objects received, and writes the entries of
	 * each object to a CSV file.
	 */
	public void exportToCSV() {
		Toast.makeText(Activity_ManageEventAttendees.this, "Exporting...",
				Toast.LENGTH_LONG).show();
		try {
			FileWriter writer = new FileWriter(
			// Checks for directory existence and creates file to write to
					getCSVStorageDir("GuestBookExports") + "/"
							+ getIntent().getExtras().get("eventName")
							+ "_Attendees.csv");
			// Writes column headers for CSV file
			writer.append("Name");
			writer.append(',');
			writer.append("Address");
			writer.append(',');
			writer.append("Phone");
			writer.append(',');
			writer.append("Email");
			writer.append(',');
			writer.append('\n');

			// Loops through each event in ParseObject list
			for (ParseObject o : eventList) {

				writer.append("\"" + o.getString("fullname") + "\"");
				writer.append(',');
				writer.append("\"" + removeCommas(o.getString("address"))
						+ "\"");
				writer.append(',');
				writer.append("\"" + o.getString("phone") + "\"");
				writer.append(',');
				writer.append("\"" + o.getString("email") + "\"");
				writer.append(',');
				writer.append('\n');
			}

			writer.flush();
			writer.close();

		} catch (IOException e) {
			Log.d("Error", e.getMessage());
			Toast.makeText(Activity_ManageEventAttendees.this, e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * This method removes commas from a string to ensure the CSV file is
	 * properly formatted
	 * 
	 * @param string
	 * @return
	 */
	public String removeCommas(String string) {
		return string = string.replace(",", "");
	}

	/**
	 * This method checks to make sure storage is available to write to
	 * 
	 * @return
	 */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * This method checks for existence of file, create it if necessary, and
	 * stores it in a directory
	 * 
	 * @param csvName
	 * @return
	 */
	public File getCSVStorageDir(String csvName) {
		// Get the directory for the user's public Documents directory.
		File file = new File(
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),csvName);
		if (!file.mkdirs()) {
			Log.e("CSV Storage Directory", "Directory not created");
		}
		return file;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.exportAttendees) {
			if (isExternalStorageWritable()) {

				exportToCSV();

			}
		}
		return super.onOptionsItemSelected(item);
	}
}
