/**
 * 
 */
package com.ruyicai.activity.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.AccountDetailsActivity;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.net.newtransaction.HelpCenterInterface;
import com.ruyicai.net.newtransaction.NewInfoGetNewsContentInterface;
import com.ruyicai.net.newtransaction.recharge.RechargeDescribeInterface;
import com.ruyicai.util.PublicMethod;
import com.umeng.analytics.MobclickAgent;
import com.umeng.common.Log;
import com.umeng.common.net.l;

/**
 * 
 * 帮助中心
 * 
 * @author Administrator
 * 
 */
public class HelpTitles extends Activity {
	// 如意助手列表各项ID
	public final static int ID_GAMEINTRODUTION = 1;/* 玩法介绍 */
	public final static int ID_GOUCAI = 2;/* 购彩流程 */
	public final static int ID_ZHONGJIANG = 3;/* 中奖领奖 */
	public final static int ID_MIMAZHAOHUI = 4;/* 密码找回 */
	public final static int ID_KEFUDIANHUA = 5;/* 客服电话 */
	public final static int ID_ABOUT = 6;// 关于本软件
	private boolean isMain = true;
	private String type;
	private String title;
	private Context context;
	Activity activity;
	// 获取网络信息的所有页面数
	private int allPage;
	// 获取信息的当前页数
	private int pageIndex = 1;
	private ProgressBar progressbar;
	private ListView listview;
	private View moreView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ruyihelper_listview);
		context = this;
		getIntentInfo();
		setTitle();
		getJSONByLotno();
	}

	private void setTitle() {
		TextView textView = (TextView) findViewById(R.id.ruyipackage_title);
		textView.setText(title);
	}

	/**
	 * 得到当前页面的下标
	 */
	public void getIntentInfo() {
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		title = intent.getStringExtra("title");
	}

	public ProgressDialog progressdialog;
	private List<Map<String, String>> titleList = new ArrayList<Map<String, String>>();

	private void getJSONByLotno() {
		progressdialog = UserCenterDialog.onCreateDialog(this);
		progressdialog.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JSONObject jsonObjectByLotno = HelpCenterInterface
						.getInstance().accountDetailQuery(type, pageIndex);
				try {
					String error_code = jsonObjectByLotno
							.getString("error_code");
					final String message = jsonObjectByLotno
							.getString("message");
					if (error_code.equals("0000")) {
						getList(jsonObjectByLotno);
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								showView(titleList);
							}
						});
					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(context, message,
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (progressdialog != null) {
					progressdialog.dismiss();
				}
			}
		}).start();

	}

	private void getList(JSONObject jsonObjectByLotno) throws JSONException {
		JSONObject jsonObject = jsonObjectByLotno;
		JSONArray result = null;
		Map<String, String> map = null;

		allPage = jsonObject.getInt("totalPage");

		if (jsonObject.has("result")) {
			result = (JSONArray) jsonObject.get("result");
			for (int i = 0; i < result.length(); i++) {
				JSONObject obj = result.getJSONObject(i);
				map = new HashMap<String, String>();
				map.put("id", obj.getString("id").toString());
				map.put("title", obj.getString("title").toString());
				titleList.add(map);
			}
		}
	}

	/**
	 * 创建帮助中心界面
	 * 
	 * @return
	 */
	public void showView(List<Map<String, String>> map) {
		isMain = true;
		listview = (ListView) findViewById(R.id.ruyihelper_listview_ruyihelper_id);
		Button btnreturn = (Button) findViewById(R.id.ruyizhushou_btn_return);
		btnreturn.setBackgroundResource(R.drawable.returnselecter);

		/**
		 * 添加获取更多按钮
		 */
		if (moreView == null) {
			LayoutInflater mInflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			moreView = mInflater.inflate(R.layout.lookmorebtn, null);
			ProgressBar progressbar = (ProgressBar) moreView
					.findViewById(R.id.getmore_progressbar);
			listview.addFooterView(moreView);
			moreView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					addmore();
				}
			});
		}

		btnreturn.setOnClickListener(new ImageView.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		// 数据源
		HelpCenterAdapter adapter = new HelpCenterAdapter(this, map);
		// 适配器
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HelpTitles.this,
						HelpCenterItemActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("id", titleList.get(arg2).get("id").toString());
				bundle.putString("title", titleList.get(arg2).get("title")
						.toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		PublicMethod.setmydividerHeight(listview);
	}

	private void addmore() {
		pageIndex++;

		if (pageIndex > allPage) {
			pageIndex = allPage - 1;
			listview.removeFooterView(moreView);
			Toast.makeText(HelpTitles.this, R.string.usercenter_hasgonelast,
					Toast.LENGTH_SHORT).show();
		} else {
			getJSONByLotno();
		}
	}

	/**
	 * 帮助中心的适配器
	 */
	public class HelpCenterAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, String>> list;

		public HelpCenterAdapter(Context context, List<Map<String, String>> list) {
			mInflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.ruyihelper_listview_icon_item, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.ruyihelper_icon_text);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.ruyihelper_iicon);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.title.setText(list.get(position).get("title").toString());
			convertView.setId(Integer.parseInt(list.get(position).get("id")
					.toString()));
			holder.icon.setBackgroundResource(R.drawable.xiangyou);
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
				// setContentView(showView());
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
