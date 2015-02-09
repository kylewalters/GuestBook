package com.kylethetechguy.guestbook;

/**
 * Author:	Kyle Walters
 * Date:	12/8/2014
 */

import java.util.Calendar;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_CreateEvent extends Activity {

	/**
	 * Interface object declarations
	 */

	EditText eventTitle;
	EditText eventAddress;
	EditText eventDate;
	EditText eventStartTime;
	EditText eventEndTime;
	EditText eventContactEmail;
	EditText eventDescription;
	Button submit;
	ParseUser currentUser = ParseUser.getCurrentUser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);

		/**
		 * Attaching interface components to their objects
		 */

		eventTitle = (EditText) findViewById(R.id.createEventTitleEditText);
		eventAddress = (EditText) findViewById(R.id.createEventLocationEditText);
		eventDate = (EditText) findViewById(R.id.createEventDateEditText);
		eventStartTime = (EditText) findViewById(R.id.createEventStartTimeEditText);
		eventEndTime = (EditText) findViewById(R.id.createEventEndTimeEditText);
		eventContactEmail = (EditText) findViewById(R.id.createEventContactEmailEditText);
		eventDescription = (EditText) findViewById(R.id.createEventDescriptionEditText);
		submit = (Button) findViewById(R.id.createEventStartButton);

		// Listen for Submit button tap
		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Create a ParseObject to store in the "Event" table
				ParseObject event = new ParseObject("Event");
				event.put("title", eventTitle.getText().toString());
				event.put("address", eventAddress.getText().toString());
				event.put("date", eventDate.getText().toString());
				event.put("startTime", eventStartTime.getText().toString());
				event.put("endTime", eventEndTime.getText().toString());
				event.put("contactEmail", eventContactEmail.getText()
						.toString());
				event.put("description", eventDescription.getText().toString());
				// Storing the currentUser object along with the event object to
				// keep track of the owner
				event.put("owner", currentUser);
				event.saveEventually(new SaveCallback() {

					@Override
					public void done(ParseException e) {
						// If there are no exceptions, continue forward
						if (e == null) {
							Toast.makeText(Activity_CreateEvent.this,
									"Event Created", Toast.LENGTH_SHORT).show();
							currentUser.put("creator", true);
							/**
							 * Used saveEventually, in case of limited network
							 * connectivity
							 */
							currentUser.saveEventually();
							finish();
						} else {
							Toast.makeText(Activity_CreateEvent.this,
									e.getMessage(), Toast.LENGTH_SHORT).show();
						}

					}
				});
			}

		});

		/**
		 * Event listener to start Date Picker
		 */
		eventDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				int mYear = c.get(Calendar.YEAR);
				int mMonth = c.get(Calendar.MONTH);
				int mDay = c.get(Calendar.DAY_OF_MONTH);
				DatePickerDialog dialog = new DatePickerDialog(
						Activity_CreateEvent.this, new DatePicker(), mYear,
						mMonth, mDay);
				dialog.show();
			}
		});

		/**
		 * Event listener to start Time Picker
		 */
		eventStartTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				int mHourOfDay = c.get(Calendar.HOUR_OF_DAY);
				int mMinuteOfHour = c.get(Calendar.MINUTE);
				TimePickerDialog dialog = new TimePickerDialog(
						Activity_CreateEvent.this, new StartTimePicker(),
						mHourOfDay, mMinuteOfHour, false);
				dialog.setTitle("Set Start Time");
				dialog.show();
			}
		});

		/**
		 * Event listener to start Time Picker
		 */
		eventEndTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				int mHourOfDay = c.get(Calendar.HOUR_OF_DAY);
				int mMinuteOfHour = c.get(Calendar.MINUTE);
				TimePickerDialog dialog = new TimePickerDialog(
						Activity_CreateEvent.this, new EndTimePicker(),
						mHourOfDay, mMinuteOfHour, false);
				dialog.setTitle("Set End Time");
				dialog.show();
			}
		});

	}

	class DatePicker implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(android.widget.DatePicker view, int year,
				int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			int mYear = year;
			int mMonth = monthOfYear;
			int mDay = dayOfMonth;
			eventDate.setText(new StringBuilder().append(mMonth + 1)
					.append("/").append(mDay).append("/").append(mYear)
					.append(" "));
			Log.d("Date", new StringBuilder().append(mMonth + 1).append("/")
					.append(mDay).append("/").append(mYear).append(" ")
					.toString());
		}

	}

	class StartTimePicker implements TimePickerDialog.OnTimeSetListener {

		@Override
		public void onTimeSet(android.widget.TimePicker view, int hourOfDay,
				int minute) {
			// TODO Auto-generated method stub
			int mHourOfDay = hourOfDay;
			int mMinuteOfHour = minute;
			String amOrPm = null;
			if(mHourOfDay > 12) {
				int hour = mHourOfDay - 12;
				amOrPm = "PM";
				eventStartTime.setText(new StringBuilder().append(hour)
						.append(":").append(zeroFix(mMinuteOfHour)).append(""+amOrPm).toString());
			} else {
				int hour = mHourOfDay;
				amOrPm = "AM";
				eventStartTime.setText(new StringBuilder().append(hour)
						.append(":").append(zeroFix(mMinuteOfHour)).append(""+amOrPm).toString());
			}
		}

	}

	class EndTimePicker implements TimePickerDialog.OnTimeSetListener {

		@Override
		public void onTimeSet(android.widget.TimePicker view, int hourOfDay,
				int minute) {
			// TODO Auto-generated method stub
			int mHourOfDay = hourOfDay;
			int mMinuteOfHour = minute;
			String amOrPm = null;
			if(mHourOfDay > 12) {
				int hour = mHourOfDay - 12;
				amOrPm = "PM";
				eventEndTime.setText(new StringBuilder().append(hour)
						.append(":").append(zeroFix(mMinuteOfHour)).append(""+amOrPm).toString());
			} else {
				int hour = mHourOfDay;
				amOrPm = "AM";
				eventEndTime.setText(new StringBuilder().append(hour)
						.append(":").append(zeroFix(mMinuteOfHour)).append(""+amOrPm).toString());
			}
		}

	}
	
	private static String zeroFix(int i) {
		if(i >= 10) {
			return String.valueOf(i);
		} else {
			return "0"+String.valueOf(i);
		}
	}

}