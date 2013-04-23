/**
 * 
 */
package com.ruyicai.activity.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.util.PublicMethod;

/**
 * 
 * 帮助中心
 * 
 * @author Administrator
 * 
 */
public class HelpCenter extends Activity {
	// 如意助手列表各项ID
	public final static int ID_GAMEINTRODUTION = 1;/* 玩法介绍 */
	public final static int ID_GOUCAI = 2;/* 购彩流程 */
	public final static int ID_ZHONGJIANG = 3;/* 中奖领奖 */
	public final static int ID_MIMAZHAOHUI = 4;/* 密码找回 */
	public final static int ID_KEFUDIANHUA = 5;/* 客服电话 */
	public final static int ID_ABOUT = 6;// 关于本软件
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	private TextView text;
	private static final String IICON = "IICON";
	private final static String TITLE = "TITLE"; /* 标题 */
	private View views;
	private boolean isMain = true;
	Activity activity;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(showView());
	}

	/**
	 * 创建帮助中心界面
	 * 
	 * @return
	 */
	public View showView() {
		isMain = true;
		LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		views = (LinearLayout) inflate.inflate(R.layout.ruyihelper_listview,
				null);
		ListView listview = (ListView) views
				.findViewById(R.id.ruyihelper_listview_ruyihelper_id);
		Button btnreturn = (Button) views
				.findViewById(R.id.ruyizhushou_btn_return);
		TextView textView = (TextView) views
				.findViewById(R.id.ruyipackage_title);
		textView.setText(getResources().getString(R.string.bangzhuzhongxin));
		btnreturn.setBackgroundResource(R.drawable.returnselecter);
		btnreturn.setOnClickListener(new ImageView.OnClickListener() {

			public void onClick(View v) {
				finish();
			}

		});

		// 数据源
		list = getListForRuyiHelperAdapter();
		HelpCenterAdapter adapter = new HelpCenterAdapter(this, list);
		// 适配器
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				text = (TextView) view.findViewById(R.id.ruyihelper_icon_text);
				String str = text.getText().toString();
				MoreActivity.iQuitFlag = 20;
				/* 功能指引 */
				if ("功能指引".equals(str)) {
					Intent intent = new Intent(HelpCenter.this,
							HelpTitles.class);
					intent.putExtra("type", "1");
					intent.putExtra("title", str);
					startActivity(intent);
				}
				/* 特色功能 */
				if ("特色功能".equals(str)) {
					Intent intent = new Intent(HelpCenter.this,
							HelpTitles.class);
					intent.putExtra("type", "2");
					intent.putExtra("title", str);
					startActivity(intent);
				}
				/* 常见问题 */
				if ("常见问题".equals(str)) {
					Intent intent = new Intent(HelpCenter.this,
							HelpTitles.class);
					intent.putExtra("type", "4");
					intent.putExtra("title", str);
					startActivity(intent);
				}
				/* 彩票术语 */
				if ("彩票术语".equals(str)) {
					Intent intent = new Intent(HelpCenter.this,
							HelpTitles.class);
					intent.putExtra("type", "5");
					intent.putExtra("title", str);
					startActivity(intent);
				}
				if ("彩票玩法".equals(str)) {
					Intent intent = new Intent(HelpCenter.this,
							HelpTitles.class);
					intent.putExtra("type", "3");
					intent.putExtra("title", str);
					startActivity(intent);
				}
			}

		};
		listview.setOnItemClickListener(clickListener);
		return views;
	}

	/**
	 * 获得如意助手列表适配器的数据源
	 * 
	 * @return
	 */
	protected List<Map<String, Object>> getListForRuyiHelperAdapter() {

		String[] titles = { "功能指引", "特色功能", "彩票玩法", "常见问题", "彩票术语" };
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		int it = R.drawable.xiangyou;
		for (int i = 0; i < titles.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TITLE, titles[i]);
			map.put(IICON, it);
			list.add(map);
		}

		return list;

	}

	/**
	 * 帮助中心的适配器
	 */
	public class HelpCenterAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;

		public HelpCenterAdapter(Context context, List<Map<String, Object>> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		int index;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			index = position;
			ViewHolder holder = null;
			String title = (String) mList.get(position).get(TITLE);
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.ruyihelper_listview_icon_item, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.ruyihelper_icon_text);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.ruyihelper_iicon);
				// holder.title.setGravity(Gravity.CENTER);
				holder.title.setText(title);
				holder.icon.setBackgroundResource(R.drawable.xiangyou);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

		class ViewHolder {
			TextView title;
			ImageView icon;
		}
	}

	/**
	 * 重写放回建
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case 4:
			if (isMain) {
				finish();
			} else {
				setContentView(showView());
			}
			break;
		}
		return false;
	}
}
