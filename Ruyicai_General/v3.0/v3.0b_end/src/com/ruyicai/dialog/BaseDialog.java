package com.ruyicai.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
/**
 * 对话框基类
 * @author Administrator
 *
 */
public abstract class BaseDialog {
	Activity activity;
	AlertDialog dialog;
	String title;
	String message;
	Button ok ;
	Button cancel;
	View view;

	public BaseDialog(Activity activity, String title, String message) {// 系统提示框
		this.activity = activity;
		dialog = new AlertDialog.Builder(activity).create();
		this.title = title;
		this.message = message;
		setTitle(title);
		setMessage(message);
		setOkButton();
		setCancelButton();
	}

	public void createMyDialog() {// 自定义提示框
		dialog.getWindow().setContentView(createDefaultView());
	}

	/**
	 * 设置标题
	 */
	public void setTitle(String title) {
		dialog.setTitle(title);
	}

	/**
	 * 设置内容
	 * 
	 */
	public void setMessage(String message) {
		dialog.setMessage(message);
	}

	/**
	 * 设置确定按钮事件
	 */
	public void setOkButton() {
		dialog.setButton(activity.getString(R.string.ok),// 设置确定按钮
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						onOkButton();
					}
				});
	}

	/**
	 * 设置取消按钮事件
	 */
	public void setCancelButton() {
		dialog.setButton2(activity.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						onCancelButton();
					}
				});
	}

	public abstract void onOkButton();

	public abstract void onCancelButton();

	/**
	 * 显示对话框
	 * 
	 */
	public void showDialog() {
		dialog.show();
	}
    /**
     * 设置确定按钮文字
     */
	public void setOkButtonStr(String ok) {
       this.ok.setText(ok);
	}
    /**
     * 设置取消按钮文字
     */
	public void setCancelButtonStr(String cancel) {
        this.cancel.setText(cancel);
	}

	/**
	 * 自定义提示框默认的view
	 */
	public View createDefaultView() {
		LayoutInflater factory = LayoutInflater.from(activity);
		view = factory.inflate(R.layout.base_dialog_default_view,null);
		TextView title = (TextView) view.findViewById(R.id.zfb_text_title);
		TextView alertText = (TextView) view.findViewById(R.id.zfb_text_alert);
		title.setText(this.title);
		alertText.setText(this.message);
		ok = (Button) view.findViewById(R.id.ok);
		cancel = (Button) view.findViewById(R.id.canel);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				onOkButton();
				
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				onCancelButton();
			}
		});
		return view;
	}
	/**
	 * 设置对话框宽和高
	 * 
	 */
	public void setDialogWH(int width,int height){
	  if(width!=0){
		  view.setMinimumWidth(width);
	  }
	  if(height!=0){
		  view.setMinimumHeight(height); 
	  }
//	  view.setLayoutParams(new ViewGroup.LayoutParams(width, height));
	}
}
