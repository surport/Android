package com.ruyicai.activity.account;

import org.json.JSONException;
import org.json.JSONObject;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.BalanceQueryInterface;
import com.ruyicai.net.newtransaction.recharge.RechargeDescribeInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

/**
 * 免费获取彩金
 * 
 * @author win
 * 
 */
public class GetFreeGoldActivity extends Activity {
	private Button secureOk;
	private TextView currentGold;
	WebView alipay_content;
	public static  String adUnitID = "9c697272e78036382b35056bdf53904b";//这里是广告墙的广告位id
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_get_free_glod_for_limei);
		currentGold = (TextView)findViewById(R.id.current_gold);
		secureOk = (Button) findViewById(R.id.alipay_secure_ok);
		secureOk.setText(R.string.start_get_free_gold_title);
		secureOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getFreeGold();
			}
		});
		setCurrentGold();
		PublicMethod.setTextViewContent(this); //add by yejc 20130718
		alipay_content = (WebView) findViewById(R.id.alipay_content);
		initTextViewContent();
//		String date = "<span style=\"color:#ff0000;\">" +
//				"1、免费获得的彩金会自动加入到您的如意彩账户中，可到用户中心进行查询。<br/>\r\n" +
//				"2、获赠的彩金一般会在完成操作后1-5分钟到账，个别特殊软件可能需要一至两天才能到账。<br/>\r\n" +
//				"3、需要安装本机未安装过的软件才可获取彩金。<br/>\r\n" +
//				"4、获赠的彩金只能购彩，不能提现。<br/>\r\n</span>" +
//				"5、如意彩客服热线：400-665-1000。";
	}
	
	private void initTextViewContent() {
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject jsonObject = RechargeDescribeInterface.getInstance()
							.rechargeDescribe("scoreWallDescriptionHtml");
					final String conten = jsonObject.get("content").toString();
					handler.post(new Runnable() {
						public void run() {
							alipay_content.loadDataWithBaseURL("", conten, "text/html", "UTF-8", "");
						}
					});
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void getFreeGold() {
		RWSharedPreferences pre = new RWSharedPreferences(this, "addInfo");
		String sessionIdStr = pre.getStringValue("sessionid");
		if (sessionIdStr.equals("")) {
			Intent intentSession = new Intent(this, UserLogin.class);
			startActivity(intentSession);
		} else {
			Intent intent = new Intent(this, AdWallActivity.class);
			startActivity(intent);
		}
	}
	
	private void setCurrentGold() {
		currentGold.setText("查询中....");
		final RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		final String  phonenum = shellRW.getStringValue(ShellRWConstants.PHONENUM);
		final String sessionid = shellRW.getStringValue(ShellRWConstants.SESSIONID);
		final String userno = shellRW.getStringValue(ShellRWConstants.USERNO);
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			public void run() {
				try {
					String str = BalanceQueryInterface.balanceQuery(userno,
							sessionid, phonenum);
					JSONObject json = new JSONObject(str);
					String error_code = json.getString("error_code");
					if (error_code.equals("0000")) {
						final String money = json.getString("bet_balance");
						handler.post(new Runnable() {
							public void run() {
								currentGold.setText(money);
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.post(new Runnable() {
						public void run() {
							currentGold.setText("查询失败");
						}
					});
				}
			}
		}).start();
	}
}