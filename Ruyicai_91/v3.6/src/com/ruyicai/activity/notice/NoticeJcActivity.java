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
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.NoticeJcInfo;
import com.umeng.analytics.MobclickAgent;

public class NoticeJcActivity extends Activity implements HandlerMsg{
	private String msg;
	private JSONObject jsonObj;
	ProgressDialog progressdialog;
	MyHandler handler = new MyHandler(this);//自定义handler
	private String dateStr;//联网返回的日期串
	private String[] dateShow = {},dateNet = {};//dateShow为显示用的日期数组 ，dateNet为联网上传用的日期格式数组
	private Button reBtn;
	
	private int initViewState = 1;//设置初始化竞彩查询date的状态，当initViewState = OTHER_JC_NOTICE时，不再初始化日期数组
	
	private final int FIRST_JC_NOTICE = 1;//初次进入竞彩开奖查询的（点击竞彩查询进入页面）
	private final int OTHER_JC_NOTICE = 2;//点击日期刷新（）

	
	public void onCreate(Bundle savedInstanceState) {
		// RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notice_prizes_single_specific_main);
		initView();
		if(Constants.noticeJcz.equals("")){
			initViewState = FIRST_JC_NOTICE;
			notiecJcNet("");
		}else{
			try {
				showJcListView(new JSONObject(Constants.noticeJcz));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	
	}
	public void initView(){
		// 设置标题
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.notice_prizes_single_specific_main_relative01);
		rLayout.setVisibility(RelativeLayout.VISIBLE);
		TextView noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.jingcai_kaijianggonggao);
		noticePrizesTitle.setTextSize(20);
		// 返回主列表
		reBtn = (Button) findViewById(R.id.notice_prizes_single_specific_main_returnID);
		if(dateShow.length == 0){
			reBtn.setClickable(false);
		}else{
			reBtn.setClickable(true);
		}
		reBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showBatchcodesDialog();
			}
		});
	}
	
	/**
	 * 开奖信息联网
	 */
	public void notiecJcNet(final String date){
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
				str = NoticeJcInfo.getInstance().getLotteryAllNotice("1",date);
				try {
					jsonObj = new JSONObject(str);		
					msg = jsonObj.getString("message");
					String error = jsonObj.getString("error_code");
					handler.handleMsg(error,msg);
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
		List<JcInfo> list_jc = getSubInfoForListView(jsonObj);//获取缓存中的数据 
		//初始化列表
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
		private List<JcInfo> mList;

		public JcInfoAdapter(Context context, List<JcInfo> list) {
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
			final JcInfo info = (JcInfo) mList.get(position);
			convertView = mInflater.inflate(R.layout.notice_jc_listview_item,null);
			final ViewHolder holder = new ViewHolder();
			holder.time = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time);
			holder.team = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team);
			holder.home = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name1);
			holder.away = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_team_name2);
			holder.letPoint = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_vs);
			holder.timeEnd = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_time_end);
			holder.teamId = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_id);
			holder.result = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_jieguo);
			holder.score = (TextView) convertView.findViewById(R.id.jc_main_list_item_text_score);
			convertView.setTag(holder);
			holder.time.append(info.getTime());
			holder.team.append(info.getTeam());
			holder.home.append(info.getHome());
			holder.away.append(info.getAway());
			holder.letPoint.setText(info.getLetPoint());
			holder.letPoint.setTextColor(Color.BLUE);
			holder.timeEnd.append(info.getTimeEnd());
			holder.teamId.append(info.getWeek()+info.getTeamId());
			holder.result.append(info.getResult());
			holder.score.append(info.getScore());
			return convertView;
		}
		class ViewHolder {
			TextView time;
			TextView team;
			TextView home;
			TextView away;
			TextView letPoint;
			TextView timeEnd;
			TextView teamId;
			TextView result;
			TextView score;
		}
	}
	/**
	 * 子列表中相应的数据
	 */
	protected  List<JcInfo> getSubInfoForListView(JSONObject jsonObj) {
			ArrayList<JcInfo> list = new ArrayList<JcInfo>();
			try {
				if(initViewState == FIRST_JC_NOTICE){
					dateStr = jsonObj.getString("date");
					formatDate(dateStr); 
				}
				JSONArray jsonArray = jsonObj.getJSONArray("result");
				for (int i = 0; i < jsonArray.length(); i++) {
					JcInfo itemInfo = new JcInfo();
					JSONObject jsonItem = jsonArray.getJSONObject(i);
					itemInfo.setTime(jsonItem.getString("dayForamt"));
					itemInfo.setWeek(jsonItem.getString("week"));
					itemInfo.setTeamId(jsonItem.getString("teamId"));
					itemInfo.setTeam(jsonItem.getString("league"));
					itemInfo.setResult(jsonItem.getString("result"));
					itemInfo.setScore(jsonItem.getString("score"));
					itemInfo.setTimeEnd(jsonItem.getString("time"));
					itemInfo.setLetPoint(jsonItem.getString("letPoint"));
					String teams[] = jsonItem.getString("team").split(":");
					itemInfo.setHome(teams[0]);
					itemInfo.setAway(teams[1]);
					list.add(itemInfo);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return list;
     }
  	class JcInfo{
  		String time = "";
		String day = "";
		String team = "";
		String home = "";
		String away = "";
		String score = "";
		String timeEnd = "";
		String week = "";
		String teamId = "";
		String letPoint = "";
		String result = "";

		public JcInfo() {

		}
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public String getTeamId() {
			return teamId;
		}
		public void setTeamId(String teamId) {
			this.teamId = teamId;
		}
		public String getLetPoint() {
			return letPoint;
		}
		public void setLetPoint(String letPoint) {
			this.letPoint = letPoint;
		}
		public String getWeek() {
			return week;
		}
		public void setWeek(String week) {
			this.week = week;
		}
		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
		}


		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getTeam() {
			return team;
		}

		public void setTeam(String team) {
			this.team = team;
		}

		public String getHome() {
			return home;
		}

		public void setHome(String home) {
			this.home = home;
		}

		public String getAway() {
			return away;
		}

		public void setAway(String away) {
			this.away = away;
		}

		public String getScore() {
			return score;
		}

		public void setScore(String score) {
			this.score = score;
		}

		public String getTimeEnd() {
			return timeEnd;
		}

		public void setTimeEnd(String timeEnd) {
			this.timeEnd = timeEnd;
		}

	}
	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		Constants.noticeJcz = jsonObj.toString();
		showJcListView(jsonObj);
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
	/**
	 * 将日期串转化为日期数组
	 */
	private void formatDate(String dateStr){
		dateShow = dateStr.split(";");
		reBtn.setText(dateShow[0]);
		dateNet = (dateStr.replaceAll("-", "")).split(";");
	}
	
	private void  showBatchcodesDialog(){
		AlertDialog batchCodedialog =  new AlertDialog.Builder(NoticeJcActivity.this)
	      .setItems(dateShow, new DialogInterface.OnClickListener() {
	          public void onClick(DialogInterface dialog, int which) {
	              /* User clicked so do some stuff */
	        	  reBtn.setText(dateShow[which]);
	        	  initViewState = OTHER_JC_NOTICE;
	        	  notiecJcNet(dateNet[which]);
	          }
	      }).create();
		batchCodedialog.show();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);//BY贺思明 2012-7-24
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);//BY贺思明 2012-7-24
	}
}
