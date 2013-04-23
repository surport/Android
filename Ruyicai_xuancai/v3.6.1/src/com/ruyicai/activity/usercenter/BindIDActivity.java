package com.ruyicai.activity.usercenter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BindIDInterface;
import com.ruyicai.net.newtransaction.ChangePasswordInterface;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;


/**
 * 绑定身份证
 * @author miao
 *
 */
public class BindIDActivity extends Activity {
	RWSharedPreferences shellRW ;
	private String phonenum,sessionid,userno;
	private String re;
	private JSONObject obj;	
	ProgressDialog progressDialog;
	EditText realnameEdit,cerdidEdit;
	Button submit,cancle;
	final int DIALOG1_KEY = 0;
	String cerdidStr,nameStr;
	
	
	/**
	 * 处理登录的消息及注册的消息
	 */
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				progressDialog.dismiss();
				Toast.makeText(BindIDActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
				shellRW.putStringValue("certid", cerdidStr);
				shellRW.putStringValue("name", nameStr);
				finish();
				break;
			case 2:
				progressDialog.dismiss();
				Toast.makeText(BindIDActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
				finish();
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_bindidentify);
		realnameEdit = (EditText)findViewById(R.id.usercenter_edittext_name);
		cerdidEdit = (EditText)findViewById(R.id.usercenter_edittext_certid);
		submit = (Button)findViewById(R.id.usercenter_bindID_bind);
		submit.setOnClickListener(changepawdlistener);
		cancle = (Button)findViewById(R.id.usercenter_bindID_back);
		cancle.setOnClickListener(changepawdlistener);
		shellRW = new RWSharedPreferences(this, "addInfo");
		initPojo();
	}
	private void initPojo(){
		phonenum = shellRW.getStringValue("phonenum");
		sessionid = shellRW.getStringValue("sessionid");
		userno = shellRW.getStringValue("userno");
	}
	public void editPassword() {

		 nameStr = realnameEdit.getText().toString();
		cerdidStr = cerdidEdit.getText().toString();

		// wangyl 7.21 验证密码长度
		if (nameStr.length() > 1&& cerdidStr.length() >1) {
				showDialog(0);
				new Thread(new Runnable() {
					public void run() {
						String bindIDback = BindIDInterface.getInstance().bindID(phonenum, nameStr,cerdidStr,userno);
							try {
								JSONObject bindIDjson = new JSONObject(bindIDback);
								String errorCode = bindIDjson.getString("error_code");
								String message = bindIDjson.getString("message");
								Message msg = new Message();
								if(errorCode.equals("0000")){
									msg.what = 1;
									msg.obj = message;
								}else{
									msg.what = 2;
									msg.obj = message;
								}
								handler.sendMessage(msg);
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
							}
					}
				}).start();
			} else {
			Toast.makeText(this, R.string.usercenter_notnull, Toast.LENGTH_SHORT).show();
		}

	}
	OnClickListener changepawdlistener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.usercenter_bindID_bind:
				editPassword();
				break;
			case R.id.usercenter_bindID_back:
				finish();
				break;
			default:
				break;
			}
		}
	};
	
	  protected Dialog onCreateDialog(int id) {
		    progressDialog = new ProgressDialog(this);
	        switch (id) {
	            case DIALOG1_KEY: {
	            	progressDialog.setTitle(R.string.usercenter_netDialogTitle);
	            	progressDialog.setMessage(getString(R.string.usercenter_netDialogRemind));
	            	progressDialog.setIndeterminate(true);
	            	progressDialog.setCancelable(true);
	                return progressDialog;
	            }
	        }
	        return progressDialog;
	    }
	  @Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			MobclickAgent.onPause(this);//BY贺思明 2012-7-24
		}
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			MobclickAgent.onResume(this);//BY贺思明 2012-7-24
		}
}
