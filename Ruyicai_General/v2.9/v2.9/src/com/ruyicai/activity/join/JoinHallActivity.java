package com.ruyicai.activity.join;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.util.ClockThread;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.ShellRWSharesPreferences;

/**
 * 合买大厅
 * 
 * @author haojie
 * 
 */

public class JoinHallActivity extends Activity {
	public static final String PAGENUM = "10";//每页显示的条数
	public static final String LOTNO = "Lotno";
	public static final String ISSUE = "Issue";
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	private final static String INFO = "INFO"; /* 标题 */
	private final int MAX = 11;
    private JoinInfo infos[] = new JoinInfo[MAX];//合买信息
    private TextView timeText[] = new TextView[MAX];
    Handler handler = new Handler();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.join_hall);
		init();
	}

	/**
	 * 初始化组件
	 */
	public void init() {

		TextView title = (TextView) findViewById(R.id.join_hall_text_title);
		Button imgRetrun = (Button) findViewById(R.id.join_hall_img_return);
		title.setText("合买大厅");
		imgRetrun.setBackgroundResource(R.drawable.returnselecter);
		// ImageView的返回事件
		imgRetrun.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ImageButton imgJoin = (ImageButton) findViewById(R.id.join_hall_img_join);
		imgJoin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			   isLogin();

			}
		});
		initInfos();
		initView();
		timeThread();
	}
    /**
     * 初始化列表
     */
	public void initView(){
		LinearLayout layout = (LinearLayout) findViewById(R.id.join_hall_linear_views);
		LayoutInflater mInflater = mInflater = LayoutInflater.from(this);
		for(int i=0;i<infos.length;i++){
			final JoinInfo info = infos[i];
			View convertView = mInflater.inflate(R.layout.join_hall_listview_item, null);
		    TextView title = (TextView) convertView.findViewById(R.id.join_hall_listview_text_icon_title);
		    TextView issue = (TextView) convertView.findViewById(R.id.join_hall_listview_text_issue);
			timeText[i] = (TextView) convertView.findViewById(R.id.join_hall_listview_text_time);
			ImageView icon = (ImageView) convertView.findViewById(R.id.join_hall_listview_img_icon);
			ImageButton join = (ImageButton) convertView.findViewById(R.id.join_hall_listview_img_join);
			icon.setImageResource(info.icon);
			title.setText(info.title);
			issue.setText("第"+info.issue+"期");
			timeText[i].setText(info.time);
			timeText[i].setTextColor(info.getColor());
			join.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
			           Intent intent = new Intent(JoinHallActivity.this,JoinInfoActivity.class);
			           intent.putExtra(LOTNO, info.type);
			           intent.putExtra(ISSUE, info.issue);
		               startActivity(intent);
			        
				}
			});
			layout.addView(convertView);
		}
	}
    /**
     * 
     * 初始化信息
     * 
     */
	public void initInfos(){
		for(int i=0;i<infos.length;i++){
			infos[i] = new JoinInfo(ClockThread.lotnos[i]);
			String time = ClockThread.getVaule(ClockThread.lotnos[i]);
			if(time==null){
				time = "0";
			}
			if(Integer.valueOf(time)<7200){
				infos[i].setColor(0xffcc0000);//小于两小时红色
			}else{
				infos[i].setColor(0xff000000);//黑色
			}
			infos[i].setTime(ClockThread.formatTime(time));
		}
	}
	/**
	 * 刷新时间
	 */
	public void setTime(){
		for(int i=0;i<infos.length;i++){
			String time = ClockThread.getVaule(ClockThread.lotnos[i]);
			timeText[i].setText(ClockThread.formatTime(time));
		}
	}


	/**
	 * 倒计时线程
	 */
	public void timeThread(){
		new Thread(new Runnable() {			
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);//休眠1秒
						handler.post(new Runnable() {
							public void run() {
							  setTime();
							}
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	class JoinInfo{
	   private int icon;
	   private String title;
	   private String issue;
	   private String time;
	   private String type;
	   private int color;
	   public int getColor() {
		   return color;
	   }

		public void setColor(int color) {
			this.color = color;
		}
	
		public JoinInfo(String type){
			   this.type = type;
		   initInfo(type);
		}
	   
	   public void initInfo(String type){
		   if(type.equals(Constants.LOTNO_SSQ)){
			   icon = R.drawable.join_ssq;
			   title = "双色球";
			   issue = getIssue(type);
		   }else if(type.equals(Constants.LOTNO_FC3D)){
			   icon = R.drawable.join_fc3d;
			   title = "福彩3D";
			   issue = getIssue(type);
		   }else if(type.equals(Constants.LOTNO_QLC)){
			   icon = R.drawable.join_qlc;
			   title = "七乐彩";
			   issue = getIssue(type);
		   }else if(type.equals(Constants.LOTNO_PL3)){
			   icon = R.drawable.join_pl3;
			   title = "排列三";
			   issue = getIssue(type);
		   }else if(type.equals(Constants.LOTNO_PL5)){
			   icon = R.drawable.join_pl5;
			   title = "排列五";
			   issue = getIssue(type);
			   time += "";
		   }else if(type.equals(Constants.LOTNO_QXC)){
			   icon = R.drawable.join_qxc;
			   title = "七星彩";
			   issue = getIssue(type);
			   time += "";
		   }else if(type.equals(Constants.LOTNO_DLT)){
			   icon = R.drawable.join_dlt;
			   title = "大乐透";
			   issue = getIssue(type);
			   time += "";
		   }else if(type.equals(Constants.LOTNO_SFC)){
			   icon = R.drawable.join_sfc;
			   title = "胜负彩";
			   issue = getIssue(type);
			   time += "";
		   }else if(type.equals(Constants.LOTNO_JQC)){
			   icon = R.drawable.join_jqc;
			   title = "进球彩";
			   issue = getIssue(type);
		   }else if(type.equals(Constants.LOTNO_LCB)){
			   icon = R.drawable.join_6cb;
			   title = "六场半";
			   issue = getIssue(type);
			   time += "";
		   }else if(type.equals(Constants.LOTNO_RX9)){
			   icon = R.drawable.join_rx9;
			   title = "任选九";
			   issue = getIssue(type);

		   }
	   }
	   public void setTime(String time){
		   this.time =time;
	   }
		/**
		 * 赋值给当前期
		 * @param type彩种编号
		 */
		public String getIssue(String type){
			String issue = "";
			// 获取期号和截止时间
			JSONObject ssqLotnoInfo = PublicMethod.getCurrentLotnoBatchCode(type);
			if (ssqLotnoInfo != null) {
				// 成功获取到了期号信息
				try {
					issue  = ssqLotnoInfo.getString("batchCode");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				// 没有获取到期号信息,重新联网获取期号

			}
			return issue;
		}
	    public String getType(){
	    	return type;
	    }
	}
	/**
	 * 判断是否登录
	 */
	public void isLogin(){
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(JoinHallActivity.this, "addInfo");
		String sessionid = shellRW.getUserLoginInfo("sessionid");
		if (sessionid == null || sessionid.equals("")) {
			Intent intentSession = new Intent(JoinHallActivity.this,UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
            Intent intent = new Intent(JoinHallActivity.this,JoinCheckActivity.class);
            startActivity(intent);
		}
	}
	/**
	 * 从上一个activity返回当前activity执行的方法
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
       switch(resultCode){
       case RESULT_OK:
   		   isLogin();
    	   break;
       }
	}
}
