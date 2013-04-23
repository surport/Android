/**
 * 
 */
package com.palmdream.RuyicaiAndroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 时时彩
 * 
 * @author Administrator
 * 
 */
public class Ssc extends Activity {
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	private final static String TITLE = "TITLE"; /* 标题 */

	/**
	 * 入口方法
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ----- 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// ----- 加载框架 layout
		setContentView(R.layout.layout_ssc_main);
		ImageView returnBtn = (ImageView) findViewById(R.id.goucaitouzhu_title_return);
		returnBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});

		// 初始化列表
		initList();
	}

	/**
	 * 初始化列表
	 */
	public void initList() {
		// 数据源
		list = getListForJoinAdapter();

		ListView listview = (ListView) findViewById(R.id.layout_ssc_main_list);
		// listview.setDividerHeight(0);

		// 适配器
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.layout_ssc_main_list_item, new String[] { TITLE },
				new int[] { R.id.layout_ssc_main_list_item_text });

		PublicMethod.addHeight(this, listview);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);

		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					Intent intent = new Intent(Ssc.this, SscZhiXuan.class);
					startActivity(intent);
				} else if (position == 1) {
					Intent intent = new Intent(Ssc.this, SscTwoStar.class);
					startActivity(intent);
				} else if (position == 2) {
					Intent intent = new Intent(Ssc.this, SscBigSmall.class);
					startActivity(intent);
				} else if (position == 3) {
					Intent intent = new Intent(Ssc.this, SscFiveStar.class);
					startActivity(intent);
				}
			}

		};
		listview.setOnItemClickListener(clickListener);
	}

	/**
	 * 列表加载数据
	 */
	public List<Map<String, Object>> getListForJoinAdapter() {
		String[] titles = { getString(R.string.ssc_zhixuan),
				getString(R.string.ssc_erxing), getString(R.string.ssc_daxiao),
				getString(R.string.ssc_wuxing) };
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);

		for (int i = 0; i < titles.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TITLE, titles[i]);
			list.add(map);
		}

		return list;
	}

}
