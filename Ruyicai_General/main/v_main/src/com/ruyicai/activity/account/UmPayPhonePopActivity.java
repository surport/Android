package com.ruyicai.activity.account;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.RechargeInterface;
import com.ruyicai.net.newtransaction.pojo.RechargePojo;
import com.ruyicai.util.RWSharedPreferences;
import com.umpay.huafubao.Huafubao;
import com.umpay.huafubao.HuafubaoListener;
import com.umpay.huafubao.PayType;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 联动优势话费充值弹出信息
 * 
 * @author win
 * 
 */
public class UmPayPhonePopActivity extends Activity implements HandlerMsg, HuafubaoListener  {
	private ProgressDialog progressdialog;
	private MyHandler handler = new MyHandler(this);
	private String rechargeType = "12";
	private String money = "";
	private EditText numberEdit;
	Huafubao huafubao = null;
	JSONObject obj = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.umpay_recharge_bindphone);
		money = getIntent().getStringExtra(UmPayPhoneActivity.UMPAY_RECHARGE_AMOUNT);
		ViewClickListener clickListener = new ViewClickListener();
		Button cancel = (Button) findViewById(R.id.usercenter_bindphone_back);
		cancel.setOnClickListener(clickListener);
		Button summit = (Button) findViewById(R.id.usercenter_bindphone_ok);
		summit.setOnClickListener(clickListener);
		Button ok = (Button) findViewById(R.id.usercenter_bindphone_complete);
		ok.setOnClickListener(clickListener);
		numberEdit = (EditText) findViewById(R.id.input_phone_number);
	}

	private class ViewClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.usercenter_bindphone_ok:
				summit();
				break;

			case R.id.usercenter_bindphone_back:
			case R.id.usercenter_bindphone_complete:
				finish();
				break;

			default:
				break;
			}
		}
	}
	
	private void summit() {
		String number = numberEdit.getText().toString();
		if (number.length() != 11) {
			Toast.makeText(this, "请输入正确的手机号格式",
					Toast.LENGTH_SHORT).show();
		} else {
			recharge(number);
		}
	}

	private void setupView() {
		LinearLayout contentLayout = (LinearLayout) findViewById(R.id.usercenter_changepwd_content);
		LinearLayout summitLayout = (LinearLayout) findViewById(R.id.umpay_recharge_summit);
		LinearLayout completeLayout = (LinearLayout) findViewById(R.id.umpay_recharge_complete);
		TextView completeContent = (TextView) findViewById(R.id.umpay_recharge_complete_content);
		contentLayout.setVisibility(View.GONE);
		summitLayout.setVisibility(View.GONE);
		completeLayout.setVisibility(View.VISIBLE);
		completeContent.setVisibility(View.VISIBLE);
	}
	
	private void recharge(String phoneNumber) {
		final RechargePojo rechargepojo = new RechargePojo();
		RWSharedPreferences pre = new RWSharedPreferences(UmPayPhonePopActivity.this,
				"addInfo");
		rechargepojo.setUserno(pre.getStringValue(ShellRWConstants.USERNO));
		rechargepojo.setMobileId(phoneNumber);
		rechargepojo.setAmount(money);
		rechargepojo.setRechargetype(rechargeType);
		ConnectivityManager ConnMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = ConnMgr.getActiveNetworkInfo();
		if (info.getExtraInfo() != null
				&& info.getExtraInfo().equalsIgnoreCase("3gwap")) {
			Toast.makeText(this, "提醒：检测到您的接入点为3gwap，可能无法正确充值,请切换到3gnet！",
					Toast.LENGTH_LONG).show();
		}
		progressdialog = UserCenterDialog.onCreateDialog(UmPayPhonePopActivity.this);
		progressdialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String error_code = "00";
				String message = "";
				try {
					String re = RechargeInterface.getInstance().recharge(
							rechargepojo);
					obj = new JSONObject(re);
					error_code = obj.getString("error_code");
					message = obj.getString("message");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				handler.handleMsg(error_code, message);
				progressdialog.dismiss();
			}
		}).start();
	}
	
	private void huafubaoRecharge(JSONObject obj) {
		huafubao = new Huafubao(this, this);
		Map<String, String> map = new HashMap<String, String>();
		try {
			map.put(Huafubao.MERID_STRING, obj.getString("merId"));
			map.put(Huafubao.GOODSID_STRING, obj.getString("goodsId")); // 计费点代码
			map.put(Huafubao.ORDERID_STRING, obj.getString("orderId"));
			map.put(Huafubao.MERDATE_STRING, obj.getString("merDate"));  // 当天日期
			map.put(Huafubao.AMOUNT_STRING, obj.getString("amount"));   
			map.put(Huafubao.MERPRIV_STRING, obj.getString("merPriv")); //商户私有信息 商户自由定义的数据，支付平台会回传给商户的系统（详见接口5.1）
			map.put(Huafubao.EXPAND_STRING, obj.getString("expand"));  //扩展字段
		} catch (JSONException e) {
			e.printStackTrace();
		}
		huafubao.setRequest(map, true, PayType.HFB);
	}

	@Override
	public void errorCode_0000() {
		huafubaoRecharge(obj);
		setupView();
	}

	@Override
	public void errorCode_000000() {
	}

	@Override
	public Context getContext() {
		return null;
	}
	

	@Override
	public boolean onError(int arg0, String arg1) {
		return false;
	}
}