package com.palmdream.RuyicaiAndroid;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class PhoneCardsRechargeDialog extends Activity {
	/** Called when the activity is first created. */
	private List<String> list = new ArrayList<String>();
	private Spinner mySpinner;
	private ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_cards_recharge_dialog);
		// list.add("移动充值卡");
		// list.add("联通充值卡");
		mySpinner = (Spinner) findViewById(R.id.phone_card_spinner);
		// 为下拉列表定义一个适配器，这里就用到里前面定义的list
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.recharge_card_type,
				android.R.layout.simple_spinner_item);
		// adapter = new
		// ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
		// 为适配器设置下拉列表下拉时的菜单样式
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将适配器添加到下拉列表上
		mySpinner.setAdapter(adapter);
		mySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// 点击下拉框。。。

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// 没有任何的触发事件时

			}

		});
	}
}