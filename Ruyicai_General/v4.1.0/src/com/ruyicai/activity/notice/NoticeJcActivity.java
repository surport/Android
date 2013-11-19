package com.ruyicai.activity.notice;

import java.util.ArrayList;
import java.util.Arrays;
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
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.commonBean.JsonBeanInfo;
import com.ruyicai.activity.buy.jc.explain.zq.JcExplainActivity;
import com.ruyicai.activity.buy.jc.zq.ZqMainActivity;
import com.ruyicai.activity.notice.NoticeMenuAdapter.OnClickItem;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.NoticeJcInfo;
import com.ruyicai.util.PublicMethod;
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
	private Button playBtn;
	private String playMethodType  = Constants.LOTNO_JCZQ;
	private int bachCodeIndex = 0;
	private String[] playType = {Constants.LOTNO_JCZQ, 
			Constants.LOTNO_JCZQ_RQSPF,
    		Constants.LOTNO_JCZQ_ZQJ, 
    		Constants.LOTNO_JCZQ_BF, 
    		Constants.LOTNO_JCZQ_BQC/*, 
    		Constants.LOTNO_JCZQ_HUN*/};
    private String[] playTypeText = {"胜平负","让球胜平负","总进球数","比分","半全场"/*,"混合投注"*/};
    private String[]zqtitleName={"竞足胜平负","竞足让球胜平负","竞足总进球数","竞足比分","竞足半全场"};

	private int initViewState = 1;// 设置初始化竞彩查询date的状态，当initViewState =
									// OTHER_JC_NOTICE时，不再初始化日期数组

	private final int FIRST_JC_NOTICE = 1;// 初次进入竞彩开奖查询的（点击竞彩查询进入页面）
	private final int OTHER_JC_NOTICE = 2;// 点击日期刷新（）
	private Context context;
	private Button jc_notice_img_return,jc_notice_date_sort,go_jc_touzhu;
	private GridView menu_gridview;
	private RelativeLayout relateive_date,relativelayout03;
	private PopupWindow popupwindow;
	private NoticeMenuAdapter noticeMenuAdapter;
	private TextView title,dateshow,xinqishow;
	private RelativeLayout  relativelayout_center;
	private JsonBeanInfo itemInfo;
	private JSONObject jsonItem;
	private int itemId=0;

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
		Constants.currentTickType = "jingCai";
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
		relateive_date=(RelativeLayout) findViewById(R.id.relateive_date);
		relateive_date.setVisibility(View.VISIBLE);
		dateshow=(TextView) findViewById(R.id.dateshow);
		xinqishow=(TextView) findViewById(R.id.xinqi);  //星期
		relativelayout03=(RelativeLayout) findViewById(R.id.relativelayout03);
		relativelayout03.setVisibility(View.VISIBLE);
		go_jc_touzhu=(Button) findViewById(R.id.go_jc_touzhu);
		go_jc_touzhu.setText("竞彩足球投注");
		go_jc_touzhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			   startActivity(new Intent(NoticeJcActivity.this,ZqMainActivity.class));	
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
		
		
		/*
		 * 竞彩开奖公告标题
		 */
		
		 title = (TextView) findViewById(R.id.jc_notice_text_title);
		 title.setText(zqtitleName[0]);
		jc_notice_img_return = (Button) findViewById(R.id.jc_notice_img_return);
		jc_notice_date_sort=(Button) findViewById(R.id.jc_notice_date_sort);
		relativelayout_center=(RelativeLayout) findViewById(R.id.relativelayout_center);
		//jc_returnLayout=(RelativeLayout)findViewById(R.id.jc_returnLayout);
		//title.setText(PublicMethod.toLotno(lotno));
		jc_notice_img_return.setBackgroundResource(R.drawable.notice_top_down);
		// ImageView的返回事件
		relativelayout_center.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onCreateMenuPopWindow();
			}
		});
		
		jc_notice_date_sort.setOnClickListener(new OnClickListener() {
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
			ViewHolder holder;
			if(convertView==null){
				convertView = mInflater.inflate(R.layout.jc_notice_list_item,
						null);
				holder = new ViewHolder();
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
				holder.homescore = (TextView) convertView
						.findViewById(R.id.jc_main_li_bifen_zu);
				holder.awayscore = (TextView) convertView
						.findViewById(R.id.jc_main_li_bifen_ke);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.team.setText(info.getTeam());
			holder.teamId.setText(info.getTeamId());
			holder.result.setText(info.getResult());
			holder.home.setText(info.getHome());
			holder.away.setText(info.getAway());
			
			if (Constants.LOTNO_JCZQ_RQSPF.equals(playMethodType)) {
				holder.letPoint.setVisibility(View.VISIBLE);
				if (!"".equals(info.getLetPoint()) && info.getLetPoint().startsWith("+")) {
						holder.letPoint.setTextColor(Color.RED);
				}
				holder.letPoint.setText("("+info.getLetPoint()+")");
			} else if (Constants.LOTNO_JCZQ.equals(playMethodType)) {
				holder.letPoint.setText("0");
			}
			holder.homescore.setText(info.getHomeScore());
			holder.awayscore.setText(info.getGuestScore());
			
			
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
			TextView homescore;
			TextView awayscore;
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
				xinqishow.setText(PublicMethod.getWeek(Integer.parseInt(jsonItem.getString("weekId"))));

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
		dateshow.setText(new StringBuffer(dateNet[0]).insert(4, "年").insert(7, "月").insert(10, "日"));
	}

	private void showBatchcodesDialog() {
		AlertDialog batchCodedialog = new AlertDialog.Builder(
				NoticeJcActivity.this).setItems(dateNet,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						/* User clicked so do some stuff */
						bachCodeIndex = which;
						dateshow.setText(new StringBuffer(dateNet[which]).insert(4, "年").insert(7, "月").insert(10, "日"));
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
	
	private void onCreateMenuPopWindow(){
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = (LinearLayout) inflate.inflate(R.layout.notice_menu_window, null);
		menu_gridview = (GridView) popupView.findViewById(R.id.notice_gridView);
		String[] str=getResources().getStringArray(R.array.jc_wanfa);
		List<String> jc_data = Arrays.asList(str);
	    noticeMenuAdapter=new NoticeMenuAdapter(this,new OnItemListener(),jc_data);
		menu_gridview.setAdapter(noticeMenuAdapter);
		
		
		
		popupwindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		popupwindow.setFocusable(true);
		popupwindow.setOutsideTouchable(true);
		popupwindow.update();
		popupwindow.setBackgroundDrawable(new BitmapDrawable());
		popupwindow.showAsDropDown(jc_notice_img_return);
		
		popupwindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
                if(popupwindow.isShowing() && popupwindow!=null){
                	jc_notice_img_return.setBackgroundResource(R.drawable.notice_top_down);
                }				
			}
		});
		noticeMenuAdapter.setItemSelect(itemId);
		noticeMenuAdapter.notifyDataSetInvalidated();
	}
	
	public class OnItemListener implements OnClickItem{
		@Override
		public void onChickItem(View view, int position) {
			itemId=position;
			noticeMenuAdapter.setItemSelect(position);
			noticeMenuAdapter.notifyDataSetInvalidated();
			title.setText(zqtitleName[position]);
			playMethodType = playType[position];
			if (dateNet.length > bachCodeIndex) {
				notiecJcNet(dateNet[bachCodeIndex]);
			}
			popupwindow.dismiss();
		}
		
	}
}
