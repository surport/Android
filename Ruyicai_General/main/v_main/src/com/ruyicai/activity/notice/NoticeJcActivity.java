package com.ruyicai.activity.notice;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.commonBean.JsonBeanInfo;
import com.ruyicai.activity.buy.jc.explain.zq.JcExplainActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.NoticeJcInfo;
import com.umeng.analytics.MobclickAgent;

public class NoticeJcActivity extends Activity implements HandlerMsg {
	private String msg;
	private JSONObject jsonObj;
	ProgressDialog progressdialog;
	MyHandler handler = new MyHandler(this);// 自定义handler
	private String dateStr;// 联网返回的日期串
	private String[] /*dateShow = {}, */dateNet = {};// dateShow为显示用的日期数组
													// ，dateNet为联网上传用的日期格式数组
	private Button reBtn;
	private LinearLayout playLinear;
	private Button playBtn;
	private String playMethodType  = Constants.LOTNO_JCZQ;
	private int bachCodeIndex = 0;
	private String[] playType = {Constants.LOTNO_JCZQ, 
			Constants.LOTNO_JCZQ_RQSPF,
    		Constants.LOTNO_JCZQ_ZQJ, 
    		Constants.LOTNO_JCZQ_BF, 
    		Constants.LOTNO_JCZQ_BQC, 
    		Constants.LOTNO_JCZQ_HUN};
    private String[] playTypeText = {"胜平负","让球胜平负","总进球数","比分","半全场","混合投注"};

	private int initViewState = 1;// 设置初始化竞彩查询date的状态，当initViewState =
									// OTHER_JC_NOTICE时，不再初始化日期数组

	private final int FIRST_JC_NOTICE = 1;// 初次进入竞彩开奖查询的（点击竞彩查询进入页面）
	private final int OTHER_JC_NOTICE = 2;// 点击日期刷新（）
	private Context context;

	public void onCreate(Bundle savedInstanceState) {
		// RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notice_prizes_single_specific_main);
		context = this;
		initView();
		
		initViewState = FIRST_JC_NOTICE;
		notiecJcNet("");
//		if (Constants.noticeJcz.equals("")) {
//			initViewState = FIRST_JC_NOTICE;
//			notiecJcNet("");
//		} else {
//			try {
//				showJcListView(new JSONObject(Constants.noticeJcz));
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}

	}

	/**
	 * 跳转到分析界面
	 */
	public void trunExplain(String event, String home, String away) {
		Intent intent = new Intent(context, JcExplainActivity.class);
		intent.putExtra("event", event);
		intent.putExtra("home", home);
		intent.putExtra("away", away);
		startActivity(intent);
	}

	public String getEvent(String type, JsonBeanInfo info) {
		return type + "_" + info.getDay() + "_" + info.getWeekId() + "_"
				+ info.getTeamId();
	}

	public void initView() {
		// 设置标题
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.notice_prizes_single_specific_main_relative01);
		rLayout.setVisibility(RelativeLayout.VISIBLE);
		TextView noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.jingcai_kaijianggonggao);
		noticePrizesTitle.setTextSize(20);
		// 返回主列表
		/**add by yejc 20130529 start*/
		((Button)findViewById(R.id.notice_prizes_single_specific_main_returnID)).setVisibility(View.GONE);
		playLinear = (LinearLayout)findViewById(R.id.notice_beijing_single_item_play);
		playLinear.setVisibility(View.VISIBLE);
		playBtn = (Button) findViewById(R.id.buy_lq_main_btn_type);
		playBtn.setText(playTypeText[0]);
		playBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showPlayDialog();
			}
		});
		/**add by yejc 20130529 end*/
		
		reBtn = (Button) findViewById(R.id.notice_beijing_single_main_batch_code);
		if (dateNet.length == 0) {
			reBtn.setClickable(false);
		} else {
			reBtn.setClickable(true);
		}
		reBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showBatchcodesDialog();
			}
		});
	}
	

	/**
	 * 开奖信息联网
	 */
	public void notiecJcNet(final String date) {
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
//				str = NoticeJcInfo.getInstance().getLotteryAllNotice("1", date);
				str = NoticeJcInfo.getInstance().getLotteryNoticeByLotNo(Constants.NEW_JINGCAI, playMethodType, date);
				try {
					jsonObj = new JSONObject(str);
					msg = jsonObj.getString("message");
					String error = jsonObj.getString("error_code");
					handler.handleMsg(error, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}

		});
		t.start();
	}

	/**
	 * 竞彩子列表view
	 */
	private void showJcListView(JSONObject jsonObj) {
		List<JsonBeanInfo> list_jc = getSubInfoForListView(jsonObj);// 获取缓存中的数据
		// 初始化列表
		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);
		JcInfoAdapter adapter = new JcInfoAdapter(this, list_jc);
		listview.setDividerHeight(0);
		listview.setAdapter(adapter);

	}

	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}

	/**
	 * 竞彩的适配器
	 */
	public class JcInfoAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<JsonBeanInfo> mList;

		public JcInfoAdapter(Context context, List<JsonBeanInfo> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		int index;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			index = position;
			final JsonBeanInfo info = (JsonBeanInfo) mList.get(position);
			convertView = mInflater.inflate(R.layout.notice_beijing_single_listview_item,
					null);
			final ViewHolder holder = new ViewHolder();
			holder.teamId = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_team_id);
			holder.team = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_team);
			holder.home = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_team_name1);
			holder.away = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_team_name2);
			holder.letPoint = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_vs);
			holder.result = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_jieguo);
			holder.score = (TextView) convertView
					.findViewById(R.id.jc_main_list_item_text_score);
			holder.sp = (TextView) convertView
					.findViewById(R.id.notice_beijing_single_item_odds);
			convertView.setTag(holder);
			
			holder.sp.setVisibility(View.GONE);
			holder.team.append(info.getTeam());
			holder.teamId.append(info.getTeamId());
			holder.result.append(info.getResult());
			holder.home.append(info.getHome()+"(主)");
			holder.away.append(info.getAway()+"(客)");
			if (!"".equals(info.getLetPoint())) {
				holder.letPoint.setText(info.getLetPoint());
				holder.letPoint.setTextColor(Color.BLUE);
			}
			String score = "("+info.getHomeHalfScore()+":"+info.getGuestHalfScore()+")"+info.getHomeScore()+":"+info.getGuestScore();
			holder.score.append(score);
			
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					trunExplain(getEvent(Constants.JCFOOT, info),
							info.getHome(), info.getAway());
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView team;
			TextView home;
			TextView away;
			TextView letPoint;
			TextView teamId;
			TextView result;
			TextView score;
			TextView sp;
		}
	}

	/**
	 * 子列表中相应的数据
	 */
	protected List<JsonBeanInfo> getSubInfoForListView(JSONObject jsonObj) {
		ArrayList<JsonBeanInfo> list = new ArrayList<JsonBeanInfo>();
		try {
			if (initViewState == FIRST_JC_NOTICE) {
				dateStr = jsonObj.getString("date");
				formatDate(dateStr);
			}
			JSONArray jsonArray = jsonObj.getJSONArray("result");
			for (int i = 0; i < jsonArray.length(); i++) {
				JsonBeanInfo itemInfo = new JsonBeanInfo();
				JSONObject jsonItem = jsonArray.getJSONObject(i);
				itemInfo.setDay(jsonItem.getString("day"));
				itemInfo.setWeekId(jsonItem.getString("weekId"));
				itemInfo.setTeamId(jsonItem.getString("teamId"));
				itemInfo.setTeam(jsonItem.getString("league"));
				itemInfo.setResult(jsonItem.getString("matchResult"));
				itemInfo.setLetPoint(jsonItem.getString("letPoint"));
//				itemInfo.setPeiLv(jsonItem.getString("peiLv"));
				itemInfo.setHome(jsonItem.getString("homeTeam"));
				itemInfo.setAway(jsonItem.getString("guestTeam"));
				itemInfo.setHomeScore(jsonItem.getString("homeScore"));
				itemInfo.setGuestScore(jsonItem.getString("guestScore"));
				itemInfo.setHomeHalfScore(jsonItem.getString("homeHalfScore"));
				itemInfo.setGuestHalfScore(jsonItem.getString("guestHalfScore"));
				
				list.add(itemInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void errorCode_0000() {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		Constants.noticeJcz = jsonObj.toString();
		showJcListView(jsonObj);
	}

	@Override
	public void errorCode_000000() {
	}

	@Override
	public Context getContext() {
		return this;
	}

	/**
	 * 将日期串转化为日期数组
	 */
	private void formatDate(String dateStr) {
		dateNet = (dateStr.replaceAll("-", "")).split(";");
		reBtn.setText(dateNet[0]);
	}

	private void showBatchcodesDialog() {
		AlertDialog batchCodedialog = new AlertDialog.Builder(
				NoticeJcActivity.this).setItems(dateNet,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						/* User clicked so do some stuff */
						bachCodeIndex = which;
						reBtn.setText(dateNet[which]);
						initViewState = OTHER_JC_NOTICE;
						notiecJcNet(dateNet[which]);
					}
				}).create();
		batchCodedialog.show();
	}
	
	private void showPlayDialog() {
		AlertDialog batchCodedialog = new AlertDialog.Builder(
				NoticeJcActivity.this).setItems(playTypeText,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						playBtn.setText(playTypeText[which]);
						playMethodType = playType[which];
						if (dateNet.length > bachCodeIndex) {
							notiecJcNet(dateNet[bachCodeIndex]);
						}
					}
				}).create();
		batchCodedialog.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}
}
