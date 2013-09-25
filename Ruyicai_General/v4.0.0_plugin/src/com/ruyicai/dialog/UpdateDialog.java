package com.ruyicai.dialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

/**
 * 软件更新对话框
 * 
 * @author Administrator
 * 
 */
public abstract class UpdateDialog extends BaseDialog {
	String url;
	Handler handler;
	ProgressDialog pBar;

	public UpdateDialog(Activity activity, Handler handler, String url,
			String message, String title) {
		super(activity, title, message);
		this.url = url;
		this.handler = handler;
		// TODO Auto-generated constructor stub
	}

	// /**
	// *
	// * 在线自动更新新版本
	// */
	// public void update(final String url, String message, String title) {
	// this.url = url;
	// Dialog dialog = new AlertDialog.Builder(activity).setTitle(
	// "有新版本更新").setMessage(message)// 设置内容
	// .setCancelable(false).setNegativeButton("确定",// 设置确定按钮
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// onOkButton();
	// }
	// }).setPositiveButton("取消",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	// onCancelButton();
	// }
	// }).create();
	//
	// try {
	// dialog.show();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	public void onOkButton() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			pBar = new ProgressDialog(activity);
			pBar.setTitle("正在下载");
			pBar.setMessage("请稍候…");
			pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置风格为长进度条
			pBar.setIndeterminate(false);
			pBar.incrementProgressBy(1); // 增加和减少进度
			downFile(url);
		} else {
			Toast.makeText(activity, "你未插入SD卡，请插入SD卡之后再更新", Toast.LENGTH_SHORT)
					.show();
			onCancelButton();
		}
	};

	/**
	 * 下载文件
	 * 
	 * @param url
	 */
	void downFile(final String url) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					double onePiece = length / 100;
					int percent = 1;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								"RuyicaiAndroid_update.apk");
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
								if (count >= (onePiece * percent)) {
									percent = percent + 1;
									pBar.incrementProgressBy(1);
								}
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {

				} catch (IOException e) {

				}
			}
		}.start();
	}

	void down() {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});
	}

	void update() {
		activity.finish();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(
				Uri.fromFile(new File("/sdcard/RuyicaiAndroid_update.apk")),
				"application/vnd.android.package-archive");
		activity.startActivity(intent);

	}
}
