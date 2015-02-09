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

public class Adapter_EventParseQuery extends ParseQueryAdapter<ParseObject> {
	public Adapter_EventParseQuery(Context context,
			QueryFactory<ParseObject> queryFactory) {
		super(context, queryFactory, R.layout.event_list_item);
	}

	@Override
	public View getItemView(ParseObject object, View v, ViewGroup parent) {

		if (v == null) {
			v = View.inflate(getContext(), R.layout.event_list_item, null);
		}
		
		final TextView title = (TextView) v.findViewById(R.id.eventListItemTitle);
		final TextView date = (TextView) v.findViewById(R.id.eventListItemSubTitle);
		final TextView descript = (TextView) v.findViewById(R.id.eventListDescripton);
		
		if(object.getParseObject("event") != null) {
			ParseObject event = object.getParseObject("event");
			title.setText(event.getString("title"));
			date.setText(event.getString("date"));
			descript.setText(event.getString("description"));
		}
		
		if(object.getParseObject("event") == null) {
			title.setText(object.getString("title"));
			date.setText(object.getString("date"));
			descript.setText(object.getString("description"));
		}
		
		return v;
	}

}
