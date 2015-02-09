package com.kylethetechguy.guestbook;

/**
 * Author:	Kyle Walters
 * Date:	12/8/2014
 */

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

public class Adapter_AttendeeParseQuery extends ParseQueryAdapter<ParseObject> {
	public Adapter_AttendeeParseQuery(Context context,
			QueryFactory<ParseObject> queryFactory) {
		super(context, queryFactory, R.layout.event_list_item);
	}

	public View getItemView(ParseObject object, View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.event_list_item, null);
		}
		
		TextView name = (TextView) v.findViewById(R.id.eventListItemTitle);
		TextView email = (TextView) v.findViewById(R.id.eventListItemSubTitle);
		TextView phone = (TextView) v.findViewById(R.id.eventListDescripton);
		name.setText(object.getString("fullname"));
		email.setText("");
		phone.setText("");
		return v;

	}

}
