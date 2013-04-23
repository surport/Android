package com.alipay.android.secure;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.recharge.AlipaySecurePayInterface;
import com.ruyicai.util.RWSharedPreferences;

public class AlipaySecurePayDialog extends Activity implements OnClickListener{
	
	Button secureOk,secureCancel;
	EditText accountnum;
	private ProgressDialog mProgress = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_alipay_secure_recharge_dialog);
		secureOk = (Button)findViewById(R.id.alipay_secure_ok);
		secureOk.setOnClickListener(this);
		secureCancel = (Button)findViewById(R.id.alipay_secure_cancel);
		secureCancel.setOnClickListener(this);
		accountnum = (EditText)findViewById(R.id.alipay_secure_recharge_value);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.alipay_secure_ok:
			isInstallSecurePay();
			break;

		case R.id.alipay_secure_cancel:
			AlipaySecurePayDialog.this.finish();
			break;
		}
	}
	
	public void isInstallSecurePay(){
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		boolean isMobile_spExist = mspHelper.detectMobile_sp();
		if (!isMobile_spExist){
			return;
		}else{
			getOrderInfo();
		}
			
	}

	private void getOrderInfo() {
		// TODO Auto-generated method stub
		(new Handler()).post(new Runnable() {
			
			@Override
			public void run() {
				RWSharedPreferences shellRW = new RWSharedPreferences(AlipaySecurePayDialog.this,ShellRWConstants.SHAREPREFERENCESNAME);
				String userno = shellRW.getStringValue(ShellRWConstants.USERNO);
				String phonenum = shellRW.getStringValue(ShellRWConstants.PHONENUM);
				String accountnumstr = accountnum.getText().toString();
				if(accountnumstr.length()>9){
					Toast.makeText(AlipaySecurePayDialog.this, "你有那么多钱吗?", Toast.LENGTH_SHORT).show();
					accountnum.setText("100");
					return;
				}
				if(accountnumstr.equals("")){
					Toast.makeText(AlipaySecurePayDialog.this, "请输入充值金额!", Toast.LENGTH_SHORT).show();
					return;
				}
				int accountnum1 = Integer.parseInt(accountnumstr); 
				if(accountnum1==0){
					Toast.makeText(AlipaySecurePayDialog.this, "充值金额不能为0！", Toast.LENGTH_SHORT).show();
					return;
				}
				// TODO Auto-generated method stub
				String rechargenum = Integer.parseInt(accountnumstr)*100+"";
				if(Constants.LOT_SERVER.equals("http://202.43.151.10:8080/lotserver/RuyicaiServlet")){
					rechargenum = "1";
				}
				String alipaysecure = AlipaySecurePayInterface.getInstance().alipaySecurePay(rechargenum, userno, phonenum);
				try {
					JSONObject json = new JSONObject(alipaysecure);
					String error_code = json.getString("error_code");
					if(error_code.equals("0000")){
						String info  = json.getString("value");
						MobileSecurePayer msp = new MobileSecurePayer();
						boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, AlipaySecurePayDialog.this);

						if (bRet) {
							// show the progress bar to indicate that we have started
							// paying.
							closeProgress();
							mProgress = BaseHelper.showProgress(AlipaySecurePayDialog.this, null, "正在支付", false,true);
						} 
					}else{
						AlipaySecurePayDialog.this.finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;
				Log.v("strRet==",strRet);
				switch (msg.what) {
					case AlixId.RQF_PAY: {
						//
						closeProgress();
						try {
							String resultStatus = "resultStatus={";
							int iresultStart = strRet.indexOf("resultStatus={"); 
							iresultStart += resultStatus.length();
							int iresultEnd = strRet.indexOf("};memo=");
							resultStatus = strRet.substring(iresultStart,iresultEnd);
							String memo = "memo=";
							int imemoStart = strRet.indexOf("memo=");
							imemoStart += memo.length();
							int imemoEnd = strRet.indexOf(";result=");
							memo = strRet.substring(imemoStart, imemoEnd);
							if(resultStatus.equals("9000")){
								memo="{充值成功}";
							}
							BaseHelper.showDialog(AlipaySecurePayDialog.this, "提示", memo,R.drawable.info);
							
						} catch (Exception e) {
							e.printStackTrace();
							BaseHelper.showDialog(AlipaySecurePayDialog.this, "提示", strRet,R.drawable.info);
						}
					}
						break;
					}
	
					super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}
		return false;
	}

//
@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			mProgress.dismiss();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
