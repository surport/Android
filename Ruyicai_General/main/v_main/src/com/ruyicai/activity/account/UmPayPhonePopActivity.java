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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
	private CheckBox checkBox = null;
	RWSharedPreferences shellRW = null;
	private String checked = "checked";
	private String phoneNumber = "phoneNumber";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.umpay_recharge_bindphone);
		money = getIntent().getStringExtra(UmPayPhoneActivity.UMPAY_RECHARGE_AMOUNT);
		shellRW = new RWSharedPreferences(this, "umpay_recharge");
		ViewClickListener clickListener = new ViewClickListener();
		Button cancel = (Button) findViewById(R.id.usercenter_bindphone_back);
		cancel.setOnClickListener(clickListener);
		Button summit = (Button) findViewById(R.id.usercenter_bindphone_ok);
		summit.setOnClickListener(clickListener);
		Button ok = (Button) findViewById(R.id.usercenter_bindphone_complete);
		ok.setOnClickListener(clickListener);
		numberEdit = (EditText) findViewById(R.id.input_phone_number);
		checkBox = (CheckBox)findViewById(R.id.umpay_phone_thirty);
		if (shellRW.getBooleanValue(checked)) {
			numberEdit.setText(shellRW.getStringValue(phoneNumber));
			checkBox.setChecked(true);
		}
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
			if (checkBox.isChecked()) {
				shellRW.putBooleanValue(checked, true);
				shellRW.putStringValue(phoneNumber, number);
			} else {
				shellRW.putBooleanValue(checked, false);
			}
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
			map.put(Huafubao.MERID_STRING, obj.getString("merId"));		//商户号
			map.put(Huafubao.GOODSID_STRING, obj.getString("goodsId")); //商品号
			map.put(Huafubao.ORDERID_STRING, obj.getString("orderId")); //订单号
			map.put(Huafubao.MERDATE_STRING, obj.getString("merDate")); //订单日期
			map.put(Huafubao.AMOUNT_STRING, obj.getString("amount"));   //商品金额
			map.put(Huafubao.MERPRIV_STRING, obj.getString("merPriv")); //商户私有信息 商户自由定义的数据，支付平台会回传给商户的系统（详见接口5.1）
			map.put(Huafubao.EXPAND_STRING, obj.getString("expand"));   //扩展字段
			map.put(Huafubao.GOODSINF_STRING, obj.getString("goodsInf"));   //扩展字段
		} catch (JSONException e) {
			e.printStackTrace();
		}
		huafubao.setRequest(map, false, PayType.HFB);
	}

	@Override
	public void errorCode_0000() {
		huafubaoRecharge(obj);
//		setupView();
	}

	@Override
	public void errorCode_000000() {
	}

	@Override
	public Context getContext() {
		return this;
	}
	

	@Override
	public boolean onError(int code, String msg) {
		boolean flag = false;
		switch (code) {
		case Huafubao.ERROR_NO_INSTALL: // 没有安装话付宝支付服务
			flag = true;
			break;
		case Huafubao.ERROR_NO_MERID: // 商户号为空
			flag = false;
			break;
		case Huafubao.ERROR_NO_GOODSID: // 商品号(计费点)为空
			flag = false;
			break;
		case Huafubao.ERROR_NO_MERDATE: // 日期为空
			flag = false;
			break;
		case Huafubao.ERROR_NO_AMOUNT: // 商品金额为空
			flag = true; // 返回值为false表示此错误由话付宝处理，为true表示由商户自己写代码处理。
			break;
		case Huafubao.ERROR_NO_NETWORK: // 手机没有联网
			flag = true;
			break;
		default:
			break;
		}
		if (!flag) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("商户提示").setMessage(msg)
					.setPositiveButton("确定", null);
			builder.create().show();
		}
		Log.i("yejc", "==========flag="+flag);
		return flag;
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Huafubao.HUAFUBAOREQCODE) {
			if (data == null) {
				Toast.makeText(this, "支付失败", Toast.LENGTH_LONG).show();
			} else {
				boolean succ = data.getExtras().getBoolean(Huafubao.SUCC);
				if (succ) {
					Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
					setupView();
				} else {
					Toast.makeText(this, "支付失败", Toast.LENGTH_LONG).show();
				}	
			}	
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}