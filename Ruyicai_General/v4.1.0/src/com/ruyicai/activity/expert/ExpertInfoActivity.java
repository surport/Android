package com.ruyicai.activity.expert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.dialog.ExpertDialog;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.json.expert.ExpertInfoJson;
import com.ruyicai.net.newtransaction.ExpertInfoInterface;
import com.umeng.analytics.MobclickAgent;

public class ExpertInfoActivity extends Activity implements HandlerMsg {
	public final static String KEY = "key";
	public static JSONArray jsonArray = null;
	public List<ExpertInfoJson> ExpertInfos = new ArrayList<ExpertInfoJson>();
	private String TYPE = "1";
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	private static Map<String, Object> expertMap = new HashMap<String, Object>();
	MyHandler handler = new MyHandler(this);// 自定义handler
	ProgressDialog progressDialog;
	Activity activity;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.expert_info_main);
		setType();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
		getIntentInfo();
		setType();
		isInfoNet();

	}

	public int getIntentInfo() {
		int index = 0;
		Intent intent = getIntent();
		index = intent.getIntExtra("index", 0);
		return index;
	}

	/**
	 * type=1:开奖信息 type=2:双色球 type=3:福彩3D type=4:排列三
	 */
	public void setType() {
		int index = getIntentInfo();
		switch (index) {
		case 0:
			TYPE = "1";
			break;
		case 1:
			TYPE = "2";
			break;
		case 2:
			TYPE = "3";
			break;
		case 3:
			TYPE = "4";
			break;
		default:
			break;
		}
	}

	/**
	 * 是否联网
	 */
	public void isInfoNet() {
		List<ExpertInfoJson> ExpertInfos = (List<ExpertInfoJson>) expertMap
				.get(TYPE);
		if (ExpertInfos == null) {
			infoNet();
		} else {
			this.ExpertInfos = ExpertInfos;
			initList();
		}
	}

	/**
	 * 联网获取信息
	 */
	public void infoNet() {
		progressDialog = UserCenterDialog.onCreateDialog(this);
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String returnStr = ExpertInfoInterface.getInstance()
						.expertInfoQuery(TYPE, "", "");
				progressDialog.cancel();
				try {
					JSONObject obj = new JSONObject(returnStr);
					String msg = obj.getString("message");
					String error = obj.getString("error_code");
					if (error.equals("0000")) {
						jsonArray = obj.getJSONArray("result");
					}
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
				ExpertInfoJson info = new ExpertInfoJson(
						jsonArray.getJSONObject(i));
				ExpertInfos.add(info);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		expertMap.put(TYPE, ExpertInfos);
	}

	/**
	 * 初始化列表
	 */
	public void initList() {
		ListView listview = (ListView) findViewById(R.id.expert_main_listview);
		// 数据源
		list = getListForRuyiHelperAdapter();
		ExpertInfoAdapter adapter = new ExpertInfoAdapter(this, list);
		// 适配器
		listview.setAdapter(adapter);
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

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

		for (int i = 0; i < ExpertInfos.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(KEY, ExpertInfos.get(i));
			list.add(map);
		}
		return list;

	}

	/**
	 * 专家荐号的适配器
	 */
	public class ExpertInfoAdapter extends BaseAdapter {
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;

		public ExpertInfoAdapter(Context context, List<Map<String, Object>> list) {
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
			final ExpertInfoJson info = (ExpertInfoJson) mList.get(position)
					.get(KEY);

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.expert_info_main_list_item, null);
				holder = new ViewHolder();
				holder.title1 = (TextView) convertView
						.findViewById(R.id.expert_info_main_list_item_title);
				holder.title2 = (TextView) convertView
						.findViewById(R.id.expert_info_main_list_item_code);
				holder.title3 = (TextView) convertView
						.findViewById(R.id.expert_info_main_list_item_content);
				holder.btn = (Button) convertView
						.findViewById(R.id.expert_info_main_list_item_btn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title1.setText(info.getTitle());
			holder.title2.setText("发送" + info.getCode() + "到"
					+ info.getToPhone());
			holder.title3.setHint(info.getContent());
			holder.btn.setHint(info.getBtnText() + " >");
			holder.btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ExpertDialog dialog = new ExpertDialog(activity,
							"确认是否发送短信？", info.getAlsetMsg(), info.getCode(),
							info.getToPhone(), new Handler());
					dialog.showDialog();
					dialog.createMyDialog();
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView title1;
			TextView title2;
			TextView title3;
			Button btn;
		}
	}

	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		splitJsonArray();
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}
}
