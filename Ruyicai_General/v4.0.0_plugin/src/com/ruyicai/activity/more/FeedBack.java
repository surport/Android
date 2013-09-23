/**
 * 
 */
package com.ruyicai.activity.more;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.interfaces.ReturnPage;
import com.ruyicai.net.newtransaction.FeedbackInterface;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * 用户反馈界面
 * 
 * @author Administrator
 * 
 */
public class FeedBack extends Activity {

	// ReturnPage returnPage;
	private ProgressDialog progressdialog;
	Handler handler = new Handler();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(showView());
	}

	public View showView() {
		RWSharedPreferences pre = new RWSharedPreferences(FeedBack.this,
				"addInfo");
		String phonenum = pre.getStringValue("phonenum");
		final String userno = pre.getStringValue("userno");
		LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = (LinearLayout) inflate.inflate(R.layout.yonghufankuiview,
				null);
		final InputMethodManager imm = (InputMethodManager) FeedBack.this
				.getSystemService(INPUT_METHOD_SERVICE);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {

				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

			}

		}, 200); // 在一秒后打开
		// final InputMethodManager imm =
		// (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		// imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		Button btnReturn = (Button) view
				.findViewById(R.id.ruyizhushou_btn_return);
		TextView title = (TextView) view.findViewById(R.id.ruyipackage_title);
		title.setText("用户反馈");
		final EditText feedbackContent = (EditText) view
				.findViewById(R.id.yonghufankui);
		final EditText feedbackConnect = (EditText) view
				.findViewById(R.id.feedbackconnection);
		feedbackConnect.setText(phonenum);
		feedbackContent.setFocusable(true);
		feedbackContent.requestFocus();
		Button btnCommitFeedback = (Button) view.findViewById(R.id.tijiao);
		btnCommitFeedback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String back = feedbackContent.getText().toString();
				final String backContent = back.replaceAll("\\s*", "");
				final String backContettpuls = backContent + "|"
						+ feedbackConnect.getText().toString();
				if (backContent.equals("")) {
					Toast.makeText(FeedBack.this,
							getString(R.string.feedbacknull),
							Toast.LENGTH_SHORT).show();
				} else if (backContent.length() < 5) {
					Toast.makeText(FeedBack.this,
							getString(R.string.feedbackless),
							Toast.LENGTH_SHORT).show();
				} else {
					showDialog(0);
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							imm.hideSoftInputFromWindow(
									feedbackContent.getWindowToken(), 0);
							String feedBackRetrun = FeedbackInterface
									.getInstance().feedback(backContettpuls,
											userno);
							// feedbackContent.setText("");
							try {
								final JSONObject obj = new JSONObject(
										feedBackRetrun);
								String errorCode = obj.getString("errorCode");
								if (errorCode.equals("success")) {
									handler.post(new Runnable() {
										public void run() {
											progressdialog.dismiss();
											MobclickAgent
													.onEvent(FeedBack.this,
															"wodeliuyan"); // BY贺思明
																			// 在我的留言里点击“提交反馈”。
											Toast.makeText(
													FeedBack.this,
													getString(R.string.feedbacksuccess),
													Toast.LENGTH_SHORT).show();
											finish();
										}
									});
								} else {
									handler.post(new Runnable() {
										public void run() {
											try {
												progressdialog.dismiss();
												String message = obj
														.getString("message");
												Toast.makeText(FeedBack.this,
														message,
														Toast.LENGTH_SHORT)
														.show();
											} catch (JSONException e) {
												e.printStackTrace();
											}
											finish();
										}
									});
								}
							} catch (JSONException e) {
								handler.post(new Runnable() {
									public void run() {
										progressdialog.dismiss();
										Toast.makeText(
												FeedBack.this,
												getString(R.string.feedbackfield),
												Toast.LENGTH_SHORT).show();
										finish();
									}
								});

							}

						}
					}).start();

				}
			}
		});

		// btnreturn.setGravity(Gravity.RIGHT);
		btnReturn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				/* 返回“更多”列表 */
				imm.hideSoftInputFromWindow(feedbackContent.getWindowToken(), 0);
				finish();
			}
		});
		return view;
	}

	/**
	 * 网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			return progressdialog;
		}
		}
		return null;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
