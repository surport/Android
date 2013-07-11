package com.ruyicai.activity.join;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid168.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.CustomizeInterface;
import com.ruyicai.net.newtransaction.FollowCanelInterface;
import com.ruyicai.net.newtransaction.ModifyInterface;
import com.ruyicai.util.PublicMethod;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 修改定制跟单
 * 
 * @author win
 * 
 */
public class JoinModifyActivity extends JoinDingActivity {
	private String dingId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initModifyView();
	}

	private void initModifyView() {
		String stateStr = "";
		String times = "";
		String amt = "";
		TextView titleText = (TextView) findViewById(R.id.ding_text_title);
		LinearLayout layoutNum1 = (LinearLayout) findViewById(R.id.ding_group1_layout_num);
		LinearLayout layoutNum2 = (LinearLayout) findViewById(R.id.ding_group2_layout_num);
		TextView timeText = (TextView) findViewById(R.id.ding_text_time_id);
		TextView stateText = (TextView) findViewById(R.id.ding_text_state_id);
		TextView numText = (TextView) findViewById(R.id.ding_text_num_id);
		Button cancelText = (Button) findViewById(R.id.ding_btn_chedan);
		layoutNum1.setVisibility(View.GONE);
		layoutNum2.setVisibility(View.GONE);
		timeText.setVisibility(View.VISIBLE);
		stateText.setVisibility(View.VISIBLE);
		numText.setVisibility(View.VISIBLE);
		titleText.setText(getString(R.string.ding_modify_title));
		Intent intent = getIntent();
		dingId = intent.getStringExtra("id");
		timeText.append(intent.getStringExtra("time"));
		stateStr = intent.getStringExtra("state");
		times = intent.getStringExtra("times");
		amt = intent.getStringExtra("joinamt");
		stateText.append(getState(stateStr));
		numText.append(times);
		if (stateStr.equals("1")) {
			cancelText.setVisibility(View.VISIBLE);
			cancelText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					cancelNet(dingId);
				}
			});
		}

		if (!amt.equals("") && amt != null) {
			amt = PublicMethod.toIntYuan(amt);
			amtEdit.setText(amt);
		}
		numEdit.setText(times);
		setAmtText();
	}

	private String getState(String stateId) {
		String state = "";
		if (stateId.equals("0")) {
			state = "无效";
		} else if (stateId.equals("1")) {
			state = "进行中";
		}
		return state;
	}

	/**
	 * 初始化定制按钮
	 */
	protected void initbottomView() {
		Button buy = (Button) findViewById(R.id.join_ding_btn_buy);
		buy.setText("保存");
		buy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isLogin();
			}
		});
	}

	/**
	 * 撤销
	 * 
	 * @param rechargepojo
	 */
	protected void cancelNet(final String id) {
		if (mProgress == null) {
			mProgress = UserCenterDialog.onCreateDialog(context);
		}
		mProgress.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String re = FollowCanelInterface.Joinfollowcanel(id);
					final JSONObject obj = new JSONObject(re);
					String error_code = obj.getString("error_code");
					final String message = obj.getString("message");
					closeProgress();
					if (error_code.equals(Constants.SUCCESS_CODE)) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, message,
										Toast.LENGTH_SHORT).show();
								finish();
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
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * 发起定制跟单
	 * 
	 * @param rechargepojo
	 */
	protected void dingNet() {
		customizeInfo.setId(dingId);
		if (mProgress == null) {
			mProgress = UserCenterDialog.onCreateDialog(context);
		}
		mProgress.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String re = ModifyInterface.customizeNet(customizeInfo);
					final JSONObject obj = new JSONObject(re);
					String error_code = obj.getString("error_code");
					final String message = obj.getString("message");
					closeProgress();
					if (error_code.equals(Constants.SUCCESS_CODE)) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, message,
										Toast.LENGTH_SHORT).show();
								finish();
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
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	protected void getInfo() {
		Intent intent = getIntent();
		starterUserNo = intent.getStringExtra(JoinInfoActivity.USER_NO);
		lotno = intent.getStringExtra(JoinHallActivity.LOTNO);

		infoNet(starterUserNo, lotno);
	}

}
