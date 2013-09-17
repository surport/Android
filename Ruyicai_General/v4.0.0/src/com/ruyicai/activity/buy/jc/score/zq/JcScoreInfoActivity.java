package com.ruyicai.activity.buy.jc.score.zq;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.net.newtransaction.ScoreInfoInterface;

/**
 * 竞彩足球即时比分详情
 * 
 * @author win
 * 
 */
public class JcScoreInfoActivity extends Activity {
	private int[] imageViews = { R.drawable.kind_1, R.drawable.kind_7,
			R.drawable.kind_8, R.drawable.kind_3, R.drawable.kind_2,
			R.drawable.kind_9, R.drawable.kind_10, R.drawable.kind_11,
			R.drawable.kind_4, R.drawable.kind_5 };
	private String[] imageTitle = { "入球", "点球", "乌龙", "黄牌", "红牌", "两黄变红",
			"入球无效", "换人", "换入", "换出" };
	private Context context;
	private Handler handler = new Handler();
	private LinearLayout layout;
	private String type = "immediateScoreDetail";
	ProgressDialog progressdialog;
	
	public boolean isBeiDanInfo = false; //add by yejc 20130716

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.jc_score_info);
		context = this;
		initView();
		addExplain();
		getScoreInfoNet(getIntentInfo(), type);

	}

	/**
	 * 图例说明添加方法
	 */
	public void addExplain() {
		LinearLayout layoutMain = (LinearLayout) findViewById(R.id.jc_score_info_layout_bottom_layout);
		addLine(layoutMain, 6, imageViews, imageTitle);
	}

	/**
	 * 加载每一行的单选按钮
	 * 
	 * @param layoutMain
	 * @param lineNum
	 *            每一行的个数
	 */
	private void addLine(LinearLayout layoutMain, int lineNum,
			int[] imageViews, String[] imgTitle) {
		int allNum = imageViews.length;
		int line = allNum / lineNum;
		int linelast = allNum % lineNum;// 最后一行 个数

		for (int i = 0; i < line; i++) {
			lineView(layoutMain, lineNum, imageViews, imgTitle, i, lineNum);
		}
		if (linelast > 0) {
			lineView(layoutMain, lineNum, imageViews, imgTitle, line, linelast);
		}

	}

	private void lineView(LinearLayout layoutMain, int lineNum,
			int[] imageViews, String[] imgTitle, int line, int linelast) {
		LinearLayout layoutOne = new LinearLayout(context);
		for (int j = 0; j < linelast; j++) {
			ImageView imgeView = new ImageView(context);
			imgeView.setBackgroundResource(imageViews[line * lineNum + j]);
			TextView titleText = new TextView(context);
			titleText.setTextColor(getResources().getColor(R.color.black));
			titleText.setText(imgTitle[line * lineNum + j]);
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			param.setMargins(0, 0, 10, 0);
			layoutOne.setGravity(Gravity.CENTER);
			layoutOne.addView(imgeView);
			layoutOne.addView(titleText, param);
		}
		layoutMain.addView(layoutOne);
	}

	protected void onResume() {
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
	 * 初始化列表项
	 * 
	 * @param jsonArray
	 */
	private void initListInfo(JSONArray jsonArray) {
		for (int i = 0; i < jsonArray.length(); i++) {
			LayoutInflater inflate = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View viewItem = inflate.inflate(R.layout.jc_score_info_item, null);
			TextView textLeft = (TextView) viewItem
					.findViewById(R.id.jc_score_info_item_text_left);
			TextView textTime = (TextView) viewItem
					.findViewById(R.id.jc_score_info_item_text_time);
			TextView textRight = (TextView) viewItem
					.findViewById(R.id.jc_score_info_item_text_right);
			ImageView imgRight = (ImageView) viewItem
					.findViewById(R.id.jc_score_info_item_img_right);
			ImageView imgLeft = (ImageView) viewItem
					.findViewById(R.id.jc_score_info_item_img_left);
			if (i % 2 != 0 && i <= 15) {
				viewItem.setBackgroundColor(context.getResources().getColor(
						R.color.grey_little));
			}
			try {
				JSONObject json = jsonArray.getJSONObject(i);
				textTime.setText(json.getString("happenTime") + "'");
				String kind = json.getString("kind");
				if (json.getString("teamID").equals("1")) {// 主队
					setImgeState(kind, imgLeft);
					textLeft.setText(json.getString("playerName"));
				} else if (json.getString("teamID").equals("0")) {// 客队
					setImgeState(kind, imgRight);
					textRight.setText(json.getString("playerName"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			layout.addView(viewItem);
		}
	}

	private void setImgeState(String kind, ImageView imgView) {
		if (kind.equals("1")) {
			imgView.setBackgroundResource(R.drawable.kind_1);
		} else if (kind.equals("2")) {
			imgView.setBackgroundResource(R.drawable.kind_2);
		} else if (kind.equals("3")) {
			imgView.setBackgroundResource(R.drawable.kind_3);
		} else if (kind.equals("4")) {
			imgView.setBackgroundResource(R.drawable.kind_4);
		} else if (kind.equals("5")) {
			imgView.setBackgroundResource(R.drawable.kind_5);
		} else if (kind.equals("7")) {
			imgView.setBackgroundResource(R.drawable.kind_7);
		} else if (kind.equals("8")) {
			imgView.setBackgroundResource(R.drawable.kind_8);
		} else if (kind.equals("9")) {
			imgView.setBackgroundResource(R.drawable.kind_9);
		} else if (kind.equals("11")) {
			imgView.setBackgroundResource(R.drawable.kind_11);
		}
	}

	/**
	 * 主客队进球数
	 */
	private void initBall() {

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
			hTeam.setText(json.getString("homeTeam") + "(主)");
			state.setText(json.getString("stateMemo"));
			state.setTextColor(setStateColor(state.getText().toString()));
			time.setText("开赛：" + json.getString("matchTime"));
			vTeam.setText(json.getString("guestTeam") + "(客)");
			hBall.setText(json.getString("homeScore"));
			vBall.setText(json.getString("guestScore"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private int setStateColor(String state) {
		int colorInt = 0;
		if (state.equals("未开赛")) {
			colorInt = Color.GRAY;
		} else if (state.equals("已完场")) {
			colorInt = Color.RED;
		} else if (state.equals("进行中")) {
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
				if (isBeiDanInfo) {
					str = ScoreInfoInterface.getBeiDanScore(event, type);
				} else {
					str = ScoreInfoInterface.getScore(event, type);
				}
				progressdialog.dismiss();
				try {
					final JSONObject obj = new JSONObject(str);
					final String msg = obj.getString("message");
					String error = obj.getString("error_code");
					if (error.equals("0000")) {
						final JSONArray jsonArray = obj
								.getJSONArray("detailResults");
						handler.post(new Runnable() {
							public void run() {
								initTopView(obj);
								initListInfo(jsonArray);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
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
}
