package com.ruyicai.activity.notice.LotnoDetail;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.NoticePrizeDetailInterface;
import com.ruyicai.util.JsonTools;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

/**
 * 
 * @author win
 * 
 */
public abstract class LotnoDetailView {
	int LOTLABEL;
	LinearLayout ballLinear;
	Activity context;
	ProgressDialog progress;
	Handler handler;
	public View view = null;
	public boolean isDialog = true;
	RWSharedPreferences shellRW;
	String token, expires_in;
	private boolean issharemove = false;
	private boolean isSinaTiaoZhuan = true;
	public String lotno;
	String tencent_token;
	String tencent_access_token_secret;
	public static String shareString = "";

	public LotnoDetailView(Activity context, String lotno, String batchcode,
			ProgressDialog progress, Handler handler, boolean isDialog) {
		LayoutInflater factory = LayoutInflater.from(context);
		view = factory.inflate(R.layout.notice_detail, null);
		this.isDialog = isDialog;
		this.handler = handler;
		this.context = context;
		this.progress = progress;
		this.lotno = lotno;
		shellRW = new RWSharedPreferences(context, "addInfo");
		isTopVisable(isDialog);
		initLotnoDetailViewWidget();
		getPrizeDetalilNet(lotno, batchcode);
		Constants.source = "2";
	}

	public void isTopVisable(boolean isVisable) {
		RelativeLayout layout = (RelativeLayout) view
				.findViewById(R.id.notice_detail_title);
		if (isVisable) {
			layout.setVisibility(RelativeLayout.VISIBLE);
		} else {
			layout.setVisibility(RelativeLayout.GONE);
		}
	}

	/**
	 * 初始化中奖详情布局中widget（设置id）
	 */
	abstract void initLotnoDetailViewWidget();

	/**
	 * 设置中奖详情布局中widget的属性和值
	 * 
	 * @param lotno
	 *            彩种编号
	 * @param batchcode
	 *            彩种期号
	 */

	public abstract void initLotonoView(JSONObject json) throws JSONException;

	public void getPrizeDetalilNet(final String lotno, final String batchcode) {
		progress.show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject PrizeDetailJson = NoticePrizeDetailInterface
						.getInstance().getNoticePrizeDetail(lotno, batchcode);
				progress.dismiss();
				try {
					String errorcode = JsonTools
							.safeParseJSONObjectForValueIsString(
									PrizeDetailJson, "error_code", "");
					final String message = JsonTools
							.safeParseJSONObjectForValueIsString(
									PrizeDetailJson, "message", "");
					if (errorcode.equals("0000")) {
						initLotonoView((JSONObject) (PrizeDetailJson));
						handler.post(new Runnable() {
							public void run() {
								if (isDialog) {
									prizeDetailDialog(view);
								} else {
									context.setContentView(view);
								}
							}
						});// 获取成功
					} else {
						handler.post(new Runnable() {
							public void run() {
								Toast.makeText(context, message,
										Toast.LENGTH_LONG).show();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void prizeDetailDialog(View view) {
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		Button button = (Button) view.findViewById(R.id.notice_return_main);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
		dialog.getWindow().setContentView(view);
		//add by yejc 20130802
		dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

	}

}
