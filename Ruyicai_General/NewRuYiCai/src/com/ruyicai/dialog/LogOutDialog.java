package com.ruyicai.dialog;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

/**
 * 注销对话框
 * 
 * @author Administrator
 * 
 */
public class LogOutDialog extends BaseDialog {
	static MyDialogListener dialogListener;
	private RWSharedPreferences shellRW;
	public static LogOutDialog dialog = null;

	public LogOutDialog(Activity activity, String title, String message) {
		super(activity, title, message);
		// TODO Auto-generated constructor stub
	}

	public static LogOutDialog createDialog(Activity activity) {
		// if(dialog == null){
		dialog = new LogOutDialog(activity,
				activity.getString(R.string.log_out_title),
				activity.getString(R.string.log_out_content));
		dialog.showDialog();
		dialog.createMyDialog();
		// }else{
		// dialog.showDialog();
		// }
		return dialog;
	}

	/**
	 * 按钮的回调函数
	 */
	public void setOnClik(MyDialogListener dialogListener) {
		this.dialogListener = dialogListener;
	}

	/**
	 * 清空上次的登录信息
	 */
	public void clearLastLoginInfo() {
		shellRW = new RWSharedPreferences(activity, "addInfo");
		String userno = shellRW.getStringValue("userno");
		if (userno.equals("") || userno == null) {
			Toast.makeText(activity,
					activity.getString(R.string.log_out_toast_no_login),
					Toast.LENGTH_SHORT).show();
		} else {
			shellRW.putStringValue("sessionid", "");
			/**add by pengcx 20130719 start*/
			String isRemainPassword = shellRW.getStringValue("remPwd_checkBox");
			if(!"true".equals(isRemainPassword)){
				shellRW.putStringValue("password", "");
			}
			/**add by pengcx 20130719 end*/
			
			/**add by yejc 20131028 start*/
			/**清空保存的数据用于QQ，支付宝等联合登陆时不再显示之前绑定的邮箱和身份证*/
			shellRW.putStringValue("email", "");
			shellRW.putStringValue("phonenum", "");
			shellRW.putStringValue("mobileid", "");
			shellRW.putStringValue("certid", "");
			/**add by yejc 20131028 end*/
			
			shellRW.putStringValue("userno", "");
			shellRW.putStringValue("zfbtruename", "");
			shellRW.putStringValue("zfbid", "");
			shellRW.putStringValue("banktruename", "");
			shellRW.putStringValue("bankid", "");
			shellRW.putBooleanValue(ShellRWConstants.AUTO_LOGIN, false);
			shellRW.putStringValue(ShellRWConstants.RANDOMNUMBER, "");
			MobclickAgent.onEvent(activity, "zhuxiao"); // 在用户中心点击“注销”按钮的:
			Toast.makeText(activity,
					activity.getString(R.string.log_out_toast_msg),
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent("logout");
			activity.sendBroadcast(intent);
		}

	}

	@Override
	public void onOkButton() {
		// TODO Auto-generated method stub
		dialogListener.onOkClick();
	}

	@Override
	public void onCancelButton() {
		// TODO Auto-generated method stub
	}

}
