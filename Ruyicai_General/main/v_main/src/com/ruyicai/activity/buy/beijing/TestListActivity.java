package com.ruyicai.activity.buy.beijing;

import com.ruyicai.activity.buy.beijing.adapter.TestAdapter;

import android.app.ListActivity;
import android.os.Bundle;

public class TestListActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TestAdapter testAdapter = new TestAdapter(this);
		getListView().setAdapter(testAdapter);
	}
}
