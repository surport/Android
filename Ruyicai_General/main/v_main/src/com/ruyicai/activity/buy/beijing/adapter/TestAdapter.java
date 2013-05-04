package com.ruyicai.activity.buy.beijing.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TestAdapter extends BaseAdapter{
	private static final String TAG = "TestAdapter";
	Context context;
	String[] strings = {"1","2","3"};

	public TestAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		return strings.length;
	}

	@Override
	public Object getItem(int position) {
		return strings[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i(TAG, TAG + " getView()");
		TextView textView = new TextView(context);
		textView.setText(strings[position]);
		return textView;
	}

}
