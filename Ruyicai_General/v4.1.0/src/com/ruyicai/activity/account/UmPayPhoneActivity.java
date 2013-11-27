package com.ruyicai.activity.account;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import com.alipay.android.secure.MobileSecurePayHelper;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.RechargeInterface;
import com.ruyicai.net.newtransaction.pojo.RechargePojo;
import com.ruyicai.net.newtransaction.recharge.RechargeDescribeInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umpay.huafubao.Huafubao;
import com.umpay.huafubao.HuafubaoListener;
import com.umpay.huafubao.PayType;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 联动优势话费充值
 * 
 * @author win
 * 
 */
public class UmPayPhoneActivity extends Activity implements HandlerMsg, HuafubaoListener{
	public ProgressDialog progressdialog;
	Button secureOk;
	EditText accountnum;
//	private TextView alipayContent = null;
	private WebView alipayContent = null;
	private TextView accountTitleTextView = null;
	private String money = "";
	public static final String UMPAY_RECHARGE_AMOUNT = "UMPAY_RECHARGE_AMOUNT";
	RadioGroup radioGroup;
	private MyHandler handler = new MyHandler(this);
	private String rechargeType = "12";
	Huafubao huafubao = null;
	JSONObject obj = null;
	RWSharedPreferences shellRW = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_alipay_secure_recharge_dialog);
		accountTitleTextView = (TextView) findViewById(R.id.accountTitle_text);
		accountTitleTextView.setText("话费充值");
//		TextView title = (TextView)findViewById(R.id.recharge_title);
//		title.setText(R.string.umpay_phone_text_moneny);
		LinearLayout layout = (LinearLayout)findViewById(R.id.umpay_phone_linear);
		layout.setVisibility(View.VISIBLE);
		alipayContent = (WebView) findViewById(R.id.alipay_content);
		initTextViewContent();
		radioGroup = (RadioGroup)findViewById(R.id.umpay_recharge_radiogroup);

		secureOk = (Button) findViewById(R.id.alipay_secure_ok);
		accountnum = (EditText) findViewById(R.id.alipay_secure_recharge_value);
		accountnum.setVisibility(View.GONE);
		secureOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				beginUmpayRecharge();
			}
		});
		PublicMethod.setTextViewContent(this); //add by yejc 20130718
	}

	private void beginUmpayRecharge() {
		RWSharedPreferences pre = new RWSharedPreferences(this, "addInfo");
		String sessionIdStr = pre.getStringValue("sessionid");
		if (sessionIdStr.equals("")) {
			Intent intentSession = new Intent(this, UserLogin.class);
			startActivity(intentSession);
		} else {
			// 检测安全支付服务是否被安装，
			MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
			boolean isInstall = mspHelper.detectMobile_sp(Constants.PAY_PLUGIN_NAME, Constants.UMPAY_PHONE_PACK_NAME);
			
			if (isInstall) {
				int id = radioGroup.getCheckedRadioButtonId();
				if (id == R.id.umpay_phone_thirty) {
					money = "30";
				} else {
					money = "3";
				}
				TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				String number = mTelephonyMgr.getLine1Number();
				recharge(number);
			}
		}
	}
	
	
	private void recharge(String phoneNumber) {
		final RechargePojo rechargepojo = new RechargePojo();
		RWSharedPreferences pre = new RWSharedPreferences(this,
				"addInfo");
		rechargepojo.setUserno(pre.getStringValue(ShellRWConstants.USERNO));
		rechargepojo.setMobileId(phoneNumber);
		rechargepojo.setAmount(money);
		rechargepojo.setRechargetype(rechargeType);
		progressdialog = UserCenterDialog.onCreateDialog(this);
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
	
	private void initTextViewContent() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject jsonObject = RechargeDescribeInterface.getInstance()
						.rechargeDescribe("umpayHfChargeDescriptionHtml");
				try {
					final String conten = jsonObject.get("content").toString();
					handler.post(new Runnable() {
						public void run() {
//							alipayContent.setText(Html.fromHtml(conten));
//							alipayContent.loadData(conten, "text/html; charset=UTF-8", null);
							alipayContent.loadDataWithBaseURL("", conten, "text/html", "UTF-8", "");
						}
					});
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void errorCode_0000() {
		huafubaoRecharge(obj);
	}

	@Override
	public void errorCode_000000() {
	}

	@Override
	public Context getContext() {
		return this;
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
				} else {
					Toast.makeText(this, "支付失败", Toast.LENGTH_LONG).show();
				}	
			}	
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}