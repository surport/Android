package com.ruyicai.activity.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.json.action.ContentJson;
import com.ruyicai.json.action.TitleJson;
import com.ruyicai.net.newtransaction.ActionContentInterface;
import com.ruyicai.net.newtransaction.ActionTitleInterface;
import com.ruyicai.util.PublicMethod;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ActionActivity extends Activity implements HandlerMsg {
	public static JSONArray jsonArray = null;
	public static List<TitleJson> actions = new ArrayList<TitleJson>();
	private final static String TITLE = "TITLE"; /* 标题 */
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	private ListView listview;
	private View views;
	private boolean isMain = true;
	private ProgressDialog progressDialog;// 联网进度条
	MyHandler handler = new MyHandler(this);// 自定义handler

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		openTitleView();
		MobclickAgent.onEvent(this, "huodongzhongxin");// BY点击首页的“活动中心”。BY贺思明
	}

	/**
	 * 打开活动标题界面
	 */
	public void openTitleView() {
		isMain = true;
		setContentView(showView());
		isTitleNet();
	}

	/**
	 * 创建活动中心标题界面
	 * 
	 * @return
	 */
	public View showView() {
		LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		views = (LinearLayout) inflate.inflate(R.layout.action_center_title,
				null);
		listview = (ListView) views
				.findViewById(R.id.ruyihelper_listview_ruyihelper_id);
		Button btnreturn = (Button) views
				.findViewById(R.id.ruyizhushou_btn_return);
		TextView textView = (TextView) views
				.findViewById(R.id.ruyipackage_title);
		textView.setText(getResources().getString(R.string.action_title));
		btnreturn.setBackgroundResource(R.drawable.returnselecter);
		btnreturn.setOnClickListener(new ImageView.OnClickListener() {

			public void onClick(View v) {
				finish();
			}

		});

		return views;
	}

	/**
	 * 是否联网
	 * 
	 */
	public void isTitleNet() {
		if (actions.size() == 0) {// 联网
			titleNet();
		} else {// 不联网
			initList();
		}
	}

	/**
	 * 联网方法
	 */
	public void titleNet() {
		progressDialog = UserCenterDialog.onCreateDialog(this);
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String jsonStr;
				jsonStr = ActionTitleInterface.titleQuery();
				progressDialog.dismiss();
				try {
					JSONObject obj = new JSONObject(jsonStr);
					jsonArray = new JSONObject(jsonStr).getJSONArray("result");
					splitJsonArray();
					String msg = obj.getString("message");
					String error = obj.getString("error_code");
					handler.handleMsg(error, msg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 解析JsonArray对象
	 * 
	 */
	public void splitJsonArray() {
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				TitleJson action = new TitleJson(jsonArray.getJSONObject(i));
				actions.add(action);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 初始化列表
	 * 
	 */
	public void initList() {
		// 数据源
		list = getListForRuyiHelperAdapter();
		HelpCenterAdapter adapter = new HelpCenterAdapter(this, list);
		// 适配器
		listview.setAdapter(adapter);
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				openContentView(position);
			}

		};
		listview.setOnItemClickListener(clickListener);
		listview.setDividerHeight(0);
	}

	/**
	 * 获得如意助手列表适配器的数据源
	 * 
	 * @return
	 */
	protected List<Map<String, Object>> getListForRuyiHelperAdapter() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);

		for (int i = 0; i < actions.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TITLE, actions.get(i));
			list.add(map);
		}
		return list;

	}

	/**
	 * 活动中心的适配器
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
			TitleJson titleJson = (TitleJson) mList.get(position).get(TITLE);

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.action_center_list_item, null);
				holder = new ViewHolder();
				holder.title1 = (TextView) convertView
						.findViewById(R.id.action_center_list_item_text_title1);
				holder.title2 = (TextView) convertView
						.findViewById(R.id.action_center_list_item_text_title2);
				holder.title3 = (TextView) convertView
						.findViewById(R.id.action_center_list_item_text_title3);
				holder.imgView = (ImageView) convertView
						.findViewById(R.id.action_center_list_item_img);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title1.setText(titleJson.getTitle());
			holder.title2.setText(getString(R.string.action_time)
					+ titleJson.getTime());
			holder.title3.setText(getString(R.string.action_content)
					+ titleJson.getIntroduce());
			if (titleJson.getIsEnd()) {
				holder.title2.setTextColor(Color.BLACK);
				holder.imgView.setImageResource(R.drawable.action_title_end);
			} else {
				holder.title2.setTextColor(Color.RED);
				holder.imgView.setImageResource(R.drawable.xiangyou);
			}
			return convertView;
		}

		class ViewHolder {
			TextView title1;
			TextView title2;
			TextView title3;
			ImageView imgView;
		}
	}

	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		initList();
	}

	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub

	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	/****************************************** 活动内容 *******************************/
	private int index;// 哪个子列表
	private TextView titleText;
	private TextView contentText;

	/**
	 * 打开活动内容界面
	 */
	public void openContentView(int index) {
		isMain = false;
		this.index = index;
		setContentView(showContentView());
		isContentNet();
	}

	/**
	 * 子列表界面
	 * 
	 */
	public View showContentView() {
		View view = null;
		LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		view = (LinearLayout) inflate.inflate(R.layout.action_center_content,
				null);
		Button btnreturn = (Button) view
				.findViewById(R.id.ruyizhushou_btn_return);
		TextView textView = (TextView) view
				.findViewById(R.id.ruyipackage_title);
		titleText = (TextView) view
				.findViewById(R.id.aciton_content_text_title);
		contentText = (TextView) view
				.findViewById(R.id.aciton_content_text_content);

		textView.setText(getResources().getString(R.string.action_info));
		btnreturn.setBackgroundResource(R.drawable.returnselecter);
		btnreturn.setOnClickListener(new ImageView.OnClickListener() {
			public void onClick(View v) {
				openTitleView();
			}

		});

		return view;
	}

	/**
	 * 活动内容是否联网
	 * 
	 */
	public void isContentNet() {
		if (actions.get(index).infoJson == null) {// 联网
			contentNet(actions.get(index).getId());
		} else {// 不联网
			initContentView();
		}
	}

	/**
	 * 联网方法
	 */
	public void contentNet(final String id) {
		final Handler handler = new Handler();
		progressDialog = UserCenterDialog.onCreateDialog(this);
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String jsonStr = ActionContentInterface.contentQuery(id);
				progressDialog.dismiss();
				try {
					actions.get(index).infoJson = new ContentJson(
							new JSONObject(jsonStr));
					handler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							initContentView();
						}
					});

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 初始化活动内容界面
	 * 
	 */
	public void initContentView() {
		titleText.setText(actions.get(index).infoJson.getTitle());
		contentText.setText(actions.get(index).infoJson.getContent());
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
				openTitleView();
			}
			break;
		}
		return false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}
}
