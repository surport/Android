package com.ruyicai.activity.buy.jc.score.zq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid168.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.ScoreListInterface;
import com.ruyicai.util.RWSharedPreferences;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TrackActivity extends Activity{
	private ListView listMain;
	private JcInfoAdapter adapter;
	protected Context context;
	private String TYPE = "0";
	private ProgressDialog mProgress = null;
    private ArrayAdapter<String> adapterArray; 
    private Spinner money_brank;
    private int index;
    private boolean isOne = true;
    private List<String> allcountries;
    private int todayIndex;
	protected List<ScoreInfo> listInfo;
	protected String reguestType = "immediateScore";
	protected RWSharedPreferences shellRw;
	protected Map<String, ?> mapTrack;
	public String SHARE_NAME = ShellRWConstants.JCZQ_PREFER;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jc_score_list);
		context = this;
		setShareName();
	
	}
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getPreferences();
		listInfo = getScoreInfo(mapTrack);
		initList();
	}
	public void setShareName(){
		SHARE_NAME = ShellRWConstants.JCZQ_PREFER;
	}
	/**
	 * 读取数据
	 */
	public void getPreferences(){
		shellRw = new RWSharedPreferences(this, SHARE_NAME);
		mapTrack = shellRw.getAllKey();
	}
	/**
	 * 遍历mapTrack数据集
	 */
	public boolean isTrack(String event){
		return mapTrack.containsKey(event);
	}
	private String dateString(String date) {
		String dateStr = "";
		String dates[] = date.split("-");
		for(int i=0;i<dates.length;i++){
			dateStr+=dates[i];
		}
		return dateStr;
	}
    private List<String> setSpinnerDate(String date){
    	List<String> allcountries = new ArrayList<String>();
    	String[] dates = date.split(";");
    	for(int i=0;i<dates.length;i++){
    		allcountries.add(dates[i]);
    	}
    	return allcountries;
    }
	protected List<ScoreInfo> getScoreInfo(Map<String, ?> map){
		List<ScoreInfo> listInfo = new ArrayList<ScoreInfo>();
        Set<String> keys = map.keySet();
        for (Iterator it = keys.iterator(); it.hasNext();) {
            try {
            	String key = (String) it.next();
			    JSONObject json = new JSONObject(shellRw.getStringValue(key));
			    ScoreInfo info = new ScoreInfo();
				info.setTeamName(json.getString("sclassName"));
				info.sethTeam(json.getString("homeTeam"));
				info.setState(json.getString("stateMemo"));
				info.setTime(json.getString("matchTime"));
				info.setvTeam(json.getString("guestTeam"));
				info.setEvent(json.getString("event"));
				listInfo.add(info);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
		return listInfo;
	}
	public void initList(){
		listMain = (ListView) findViewById(R.id.buy_jc_main_listview);
	    adapter = new JcInfoAdapter(context,listInfo);
	    listMain.setAdapter(adapter);
		/* 列表的点击后的背景 */
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				turnInfoActivity(position);
			}

		};
		listMain.setOnItemClickListener(clickListener);
	}
	public void turnInfoActivity(int position){
		Intent intent = new Intent(context, JcScoreInfoActivity.class);
		intent.putExtra("event", listInfo.get(position).getEvent());
		startActivity(intent);
	}
	/**
	 * 得到当前页面的下标
	 */
	public int getIntentInfo(){
		int index = 0;
		Intent intent = getIntent();
		index = intent.getIntExtra("index", 0);
		return index;
	}
	/**
	 * 竞彩的适配器
	 */
	public class JcInfoAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<ScoreInfo> mList;

		public JcInfoAdapter(Context context, List<ScoreInfo> list) {
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


		@Override
		public View getView(final int position,  View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ScoreInfo info = mList.get(position);
			ViewHolder holder1 = null;
			if(convertView==null){
				convertView = mInflater.inflate(R.layout.jc_score_list_iteam,null);
				holder1 = new ViewHolder();
				holder1.teamName = (TextView) convertView.findViewById(R.id.jc_score_text_team_name);
				holder1.hTeam = (TextView) convertView.findViewById(R.id.jc_score_text_hteam);
				holder1.state = (TextView) convertView.findViewById(R.id.jc_score_text_state);
				holder1.time = (TextView) convertView.findViewById(R.id.jc_score_text_time);
				holder1.vTeam = (TextView) convertView.findViewById(R.id.jc_score_text_vteam);
				holder1.startImg = (ImageView) convertView.findViewById(R.id.jc_score_btn_start);
				holder1.layoutLeft = (LinearLayout) convertView.findViewById(R.id.jc_score_list_item_layout_left);
				convertView.setTag(holder1);
			}else{
				holder1 = (ViewHolder) convertView.getTag();
			}
			final ViewHolder holder = holder1;
			holder.teamName.setTextColor(Color.GRAY);
			holder.time.setTextColor(Color.GRAY);
			holder.teamName.setText(info.getTeamName());
			holder.hTeam.setText(info.gethTeam());
			holder.state.setText(info.getState());
			holder.state.setTextColor(setStateColor(info.getState()));
			holder.time.setText("开赛："+info.getTime());
			holder.vTeam.setText(info.getvTeam());
			if(isTrack(info.getEvent())){
				info.setStart(true);
				holder.startImg.setBackgroundResource(R.drawable.jc_list_start_b);
			}else{
				info.setStart(false);
				holder.startImg.setBackgroundResource(R.drawable.jc_list_start);
			}
			holder1.layoutLeft.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(info.isStart()){
						info.setStart(false);
						holder.startImg.setBackgroundResource(R.drawable.jc_list_start);
						shellRw.removeKey(info.getEvent());
						getPreferences();
						listInfo = getScoreInfo(mapTrack);
						initList();
					}
					
				}
			});
			return convertView;
		}
		private int setStateColor(String state){
			int colorInt = 0;
			if(state.equals("未开赛")){
				colorInt = Color.GRAY;
			}else if(state.equals("已完场")){
				colorInt = Color.RED;
			}else if(state.equals("进行中")){
				colorInt = getResources().getColor(R.color.gree_black);
			}
			return colorInt;
		}
		class ViewHolder {
			TextView teamName;//联赛名称
			TextView hTeam;//主队
			TextView vTeam;//客队
			TextView time;//开赛时间
			TextView state;//开赛进度
			ImageView startImg;
			LinearLayout layoutLeft;
		}
	}
	public class ScoreInfo{
		private String teamName;//联赛名称
		private String hTeam;//主队
		private String vTeam;//客队
		private String time;//开赛时间
		private String state;//开赛进度
		private String event;
		private boolean isStart = false;
		private JSONObject json;
		
		public JSONObject getJson() {
			return json;
		}
		public void setJson(JSONObject json) {
			Log.e("json",json.toString() );
			this.json = json;
		}
		public boolean isStart() {
			return isStart;
		}
		public void setStart(boolean isStart) {
			this.isStart = isStart;
		}
		public ScoreInfo(){
			
		}
	    public String getEvent() {
			return event;
		}

		public void setEvent(String event) {
			this.event = event;
		}
		public String getTeamName() {
			return teamName;
		}
		public void setTeamName(String teamName) {
			this.teamName = teamName;
		}
		public String gethTeam() {
			return hTeam;
		}
		public void sethTeam(String hTeam) {
			this.hTeam = hTeam;
		}
		public String getvTeam() {
			return vTeam;
		}
		public void setvTeam(String vTeam) {
			this.vTeam = vTeam;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
	}
}

