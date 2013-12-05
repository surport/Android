package com.ruyicai.util;

import com.palmdream.RuyicaiAndroid.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/**
 * 创建拨打客服电话确认的提示框
 * 
 * @author win
 * 
 */
public class CallServicePhoneConfirm {
	public static void phoneKefu(final Context mContext) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(R.drawable.icon);
		builder.setTitle("提示");
		builder.setMessage("是否确认拨打客服电话");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ mContext.getString(R.string.phone_kefu)));
				// 根据意图过滤器参数激活电话拨号功能
				mContext.startActivity(intent);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
}
