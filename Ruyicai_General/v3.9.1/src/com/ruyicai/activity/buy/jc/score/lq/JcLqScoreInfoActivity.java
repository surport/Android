package com.ruyicai.activity.buy.jc.score.lq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreListActivity.ScoreInfo;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.LotnoGameInterface;
import com.ruyicai.net.newtransaction.ScoreInfoInterface;
import com.ruyicai.util.PublicMethod;

/**
 * 竞彩足球即时比分详情
 * 
 * @author win
 * 
 */
public class JcLqScoreInfoActivity extends Activity {
	private Context context;
	private Handler handler = new Handler();
	private LinearLayout layout;
	private String type = "immediateScoreDetailJcl";
	private boolean isOver = false;
	ProgressDialog progressdialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.jc_score_lq_info);
		context = this;
		initView();
		getScoreInfoNet(getIntentInfo(), type);

	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 得到当前页面的下标
	 */
	public String getIntentInfo() {
		Intent intent = getIntent();
		return intent.getStringExtra("event");
	}

	private void initView() {
		layout = (LinearLayout) findViewById(R.id.jc_score_info_layout);
		Button imgIcon = (Button) findViewById(R.id.layout_main_img_return);
		imgIcon.setBackgroundResource(R.drawable.returnselecter);
		imgIcon.setVisibility(View.VISIBLE);
		// ImageView的返回事件
		imgIcon.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 初始化顶部组件
	 */
	private void initTopView(JSONObject json) {
		TextView teamName = (TextView) findViewById(R.id.jc_score_text_team_name);
		TextView hTeam = (TextView) findViewById(R.id.jc_score_text_hteam);
		TextView state = (TextView) findViewById(R.id.jc_score_text_state);
		TextView time = (TextView) findViewById(R.id.jc_score_text_time);
		TextView vTeam = (TextView) findViewById(R.id.jc_score_text_vteam);
		TextView hBall = (TextView) findViewById(R.id.jc_score_text_h_ball);
		TextView vBall = (TextView) findViewById(R.id.jc_score_text_v_ball);
		teamName.setTextColor(Color.GRAY);
		time.setTextColor(Color.GRAY);
		try {
			teamName.setText(json.getString("sclassName"));
			vTeam.setText(json.getString("homeTeam") + "(主)");
			state.setText(json.getString("stateMemo"));
			state.setTextColor(setStateColor(state.getText().toString()));
			time.setText("开赛：" + json.getString("matchTime"));
			hTeam.setText(json.getString("guestTeam") + "(客)");
			hBall.setText(json.getString("guestScore"));
			vBall.setText(json.getString("homeScore"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private int setStateColor(String state) {
		int colorInt = 0;
		if (state.equals("未开赛")) {
			isOver = false;
			colorInt = Color.GRAY;
		} else if (state.equals("已完场")) {
			isOver = true;
			colorInt = Color.RED;
		} else if (state.equals("进行中")) {
			isOver = true;
			colorInt = context.getResources().getColor(R.color.gree_black);
		}
		return colorInt;
	}

	/**
	 * 获取玩法介绍联网
	 */
	public void getScoreInfoNet(final String event, final String type) {
		progressdialog = UserCenterDialog.onCreateDialog(context);
		progressdialog.show();
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = ScoreInfoInterface.getScore(event, type);
				progressdialog.dismiss();
				try {
					final JSONObject obj = new JSONObject(str);
					final String msg = obj.getString("message");
					String error = obj.getString("error_code");
					if (error.equals("0000")) {
						handler.post(new Runnable() {
							public void run() {
								initTopView(obj);
								isSession(obj);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
		t.start();
	}

	/**
	 * 判断是比赛场次 sclassType 比赛分几节进行 2:上下半场;4:四节
	 * 
	 * @throws JSONException
	 */
	private void isSession(JSONObject json) {
		try {
			String session = json.getString("sclassType");
			if (session.equals("4") && isOver) {
				create4View(json.getString("homeTeam"),
						json.getString("guestTeam"), json.getString("homeOne"),
						json.getString("guestOne"), json.getString("homeTwo"),
						json.getString("guestTwo"),
						json.getString("homeThree"),
						json.getString("guestThree"),
						json.getString("homeFour"), json.getString("guestFour"));
			} else if (session.equals("2") && isOver) {
				create2View(json.getString("homeTeam"),
						json.getString("guestTeam"), json.getString("homeOne"),
						json.getString("guestOne"),
						json.getString("homeThree"),
						json.getString("guestThree"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建4节比赛界面
	 */
	public void create4View(String hName, String vName, String homeOne,
			String guestOne, String homeTwo, String guestTwo, String homeThree,
			String guestThree, String homeFour, String guestFour) {
		LinearLayout layoutMain = (LinearLayout) findViewById(R.id.main_score_layout_bottom);
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewItem = inflate.inflate(R.layout.jc_lq_score_4view, null);
		TextView vTeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_vteam);
		TextView hTeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_hteam);
		TextView v1TeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_v1);
		TextView v2TeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_v2);
		TextView v3TeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_v3);
		TextView v4TeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_v4);
		TextView h1TeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_h1);
		TextView h2TeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_h2);
		TextView h3TeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_h3);
		TextView h4TeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_h4);
		vTeamText.setText(vName);
		hTeamText.setText(hName);
		v1TeamText.setText(guestOne);
		v2TeamText.setText(guestTwo);
		v3TeamText.setText(guestThree);
		v4TeamText.setText(guestFour);
		h1TeamText.setText(homeOne);
		h2TeamText.setText(homeTwo);
		h3TeamText.setText(homeThree);
		h4TeamText.setText(homeFour);
		layoutMain.addView(viewItem);
	}

	/**
	 * 创建2节比赛界面
	 */
	public void create2View(String hName, String vName, String homeOne,
			String guestOne, String homeTwo, String guestTwo) {
		LinearLayout layoutMain = (LinearLayout) findViewById(R.id.main_score_layout_bottom);
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewItem = inflate.inflate(R.layout.jc_lq_score_2view, null);
		TextView vTeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_vteam);
		TextView hTeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_hteam);
		TextView v1TeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_v1);
		TextView v2TeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_v2);
		TextView h1TeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_h1);
		TextView h2TeamText = (TextView) viewItem
				.findViewById(R.id.jc_lq_score_text_h2);
		vTeamText.setText(vName);
		hTeamText.setText(hName);
		v1TeamText.setText(guestOne);
		v2TeamText.setText(guestTwo);
		h1TeamText.setText(homeOne);
		h2TeamText.setText(homeTwo);
		layoutMain.addView(viewItem);
	}
}
