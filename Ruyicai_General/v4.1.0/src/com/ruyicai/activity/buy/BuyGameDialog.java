package com.ruyicai.activity.buy;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.LotnoGameInterface;

/**
 * 玩法介绍
 * 
 * @author win
 * 
 */
public class BuyGameDialog {
	private AlertDialog gameDialog;
	private Context context;
	private String lotno;
	private View view;
	private Handler handler;
	private String title, content;
	ProgressDialog progressdialog;

	public BuyGameDialog(Context context, String lotno, Handler handler) {
		this.context = context;
		this.lotno = lotno;
		this.handler = handler;
	}

	/**
	 * 创建玩法介绍对话框
	 */
	private void createGameDialog() {
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		gameDialog = new AlertDialog.Builder(context).create();
		view = inflate.inflate(R.layout.buy_group_game_dialog, null);
		initDialogView();
	}

	public void initDialogView() {
		Button cancel = (Button) view
				.findViewById(R.id.buy_group_game_btn_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gameDialog.dismiss();
			}
		});
		TextView title = (TextView) view
				.findViewById(R.id.buy_group_game_title);
		TextView content = (TextView) view
				.findViewById(R.id.buy_group_game_content);
		title.setText(this.title);
		content.setText(this.content);
	}

	public void showDialog() {
		if (title == null) {
			getGameInfoNet();
		} else {
			gameDialog.show();
			gameDialog.getWindow().setContentView(view);
		}
	}

	/**
	 * 获取玩法介绍联网
	 */
	public void getGameInfoNet() {
		progressdialog = UserCenterDialog.onCreateDialog(context);
		progressdialog.show();
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = LotnoGameInterface.getInstance().lotnoGame(lotno);
				progressdialog.dismiss();
				try {
					final JSONObject obj = new JSONObject(str);
					final String msg = obj.getString("message");
					String error = obj.getString("error_code");
					if (error.equals("0000")) {
						title = obj.getString("title");
						content = obj.getString("introduce");
						handler.post(new Runnable() {
							public void run() {
								createGameDialog();
								gameDialog.show();
								gameDialog.getWindow().setContentView(view);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
		t.start();
	}
}
