/**
 * 
 */
package com.ruyicai.activity.buy;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.dlt.Dlt;
import com.ruyicai.activity.buy.fc3d.Fc3d;
import com.ruyicai.activity.buy.pl3.PL3;
import com.ruyicai.activity.buy.qlc.Qlc;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.activity.buy.ssq.Ssq;
import com.ruyicai.activity.buy.zc.FootballLottery;
import com.ruyicai.activity.join.JoinHallActivity;
import com.ruyicai.activity.more.HelpCenter;
import com.ruyicai.activity.more.LuckChoose;
import com.ruyicai.activity.usercenter.NewUserCenter;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.ShellRWSharesPreferences;

/**
 * 购彩大厅界面
 * @author Administrator
 *
 */
public class BuyActivity extends Activity implements OnClickListener{
	private String messageflage = null;
	String messagetitle;        
	String messagedetail;
	String messageerrorcode;
	private String messageidflag = null;
	private JSONObject obj;
	private Dialog dialog;
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 4:
				showmessageDialog();
				break;
			}
		}

	};
	private LayoutInflater layoutinflater;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.buy_activity);
		initImgView();
		initRollingText();
		dialogMsg();
	}
	// 活动提示框
	private void showmessageDialog() {
		ShellRWSharesPreferences shellcheck = new ShellRWSharesPreferences(BuyActivity.this, "UserMessage");
		View view = layoutinflater.from(BuyActivity.this).inflate(R.layout.tanchuxinxi, null);
		TextView msg = (TextView) view.findViewById(R.id.tanchuxinxi_msg);
		msg.setText(messagedetail);
		dialog = new AlertDialog.Builder(BuyActivity.this).setView(view)
				.setTitle(messagetitle).setNeutralButton("确定", null).show();
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

	}
	/**
	 * 初始化imageView按钮
	 */
	public void initImgView() {

		final Class[] cla = { JoinHallActivity.class, Ssq.class, Fc3d.class,
				Dlc.class, Dlt.class, Ssc.class, Qlc.class,
				PL3.class, FootballLottery.class};


		int[] imgViews = { R.id.mainpage_hemai_sign, R.id.mainpage_ssq_sign,
				R.id.mainpage_fc3d_sign, R.id.mainpage_11x5_sign,
				R.id.mainpage_dlt_sign, R.id.mainpage_ssc_sign,
				R.id.mainpage_qlc_sign, R.id.mainpage_pl3_sign,
				R.id.mainpage_zucai_sign,};
		int [] buttons ={R.id.mainpage_usercenter,R.id.mainpage_luck_sign,R.id.mainpage_help};
		for (int i = 0; i < 9; i++) {
			final int n = i;
			ImageView imgView = (ImageView) findViewById(imgViews[i]);
			imgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					Intent intent = new Intent(BuyActivity.this,cla[n]);
					startActivity(intent);
				}
			});
		}
		for (int j = 0;j<3;j++){
			Button button = (Button)findViewById(buttons[j]);
			button.setBackgroundResource(R.drawable.join_info_btn_selecter);
			button.setOnClickListener(this);
		}
	}
	
	/**
	 * 初始化组件,滚动数据
	 */
	public void initRollingText() {
		ViewFlipper mFlipper = ((ViewFlipper) this.findViewById(R.id.notice_other_flipper));
		String str[] = splitStr(Constants.NEWS, 23);
//		Log.v("news", Constants.NEWS);
		for (int i = 0; i < str.length; i++) {
			mFlipper.addView(addTextByText(str[i]));
		}
		mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_up_in));
		mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_up_out));
		mFlipper.startFlipping();
	}
    
	/**
	 * 拆分滚动信息字符串 格式为逗号隔开
	 */
	public String[] splitStr(String str, int with) {
		String strss[] = str.split(",");
		if (strss.length == 0) {
			int indexs = str.length() / with + 1;
			String strs[] = new String[indexs];
			for (int i = 0; i < indexs; i++) {
				if (i == indexs - 1) {
					strs[i] = str.substring(i * with, str.length());
				} else {
					strs[i] = str.substring(i * with, with * (i + 1));
				}
			}
			return strs;
		}
		return strss;
	}
	
	
	public View addTextByText(String text) {
		TextView tv = new TextView(this);
		tv.setText(text);
		tv.setGravity(1);
		tv.setTextSize(15);
		tv.setTextColor(Color.BLACK);
		return tv;
	}
	protected void onStart() {
		super.onStart();
	}	
	protected void onResume() {
		super.onResume();
		Constants.MEMUTYPE = 0;
	}
	protected void onPause() {
		super.onPause();
	}
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.mainpage_usercenter:
			tendToUserCenter();
		break;
		case R.id.mainpage_luck_sign:
			tendToLuckCenter();
		break;
		case R.id.mainpage_help:
			tendToHelpCenter();
		break;
		}
	}
	public void tendToUserCenter(){
		Intent intent = new Intent(BuyActivity.this,NewUserCenter.class);
		startActivity(intent);
	}
	public void tendToHelpCenter(){
		Intent intent = new Intent(BuyActivity.this, HelpCenter.class);
		startActivity(intent);
	}
	public void tendToLuckCenter(){
		Intent intent = new Intent(BuyActivity.this,LuckChoose.class);
		startActivity(intent);
	}
	
	
	private void dialogMsg() {

		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(BuyActivity.this, "UserMessage");
		messageflage = shellRW.getPreferencesValue("tanchumessage");
		messageidflag = shellRW.getPreferencesValue("id");
		if (!PublicConst.MESSAGE.equals("")) {
			try {
				obj = new JSONObject(PublicConst.MESSAGE);
				String id = obj.getString("id");
				messagetitle = obj.getString("title");
				messagedetail = obj.getString("message");
				if (messageidflag == null) {
					shellRW.putPreferencesValue("id", id);
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				}else if (!messageidflag.equals(id)) {
					shellRW.putPreferencesValue("id", id);
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				}

			} catch (JSONException e) {

			}
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
	        ExitDialogFactory.createExitDialog(this);
           break;
		}
		return false;
	}

}
