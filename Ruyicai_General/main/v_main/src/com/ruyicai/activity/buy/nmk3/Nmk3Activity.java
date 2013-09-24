package com.ruyicai.activity.buy.nmk3;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.miss.AddViewMiss;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;

public class Nmk3Activity extends BuyActivityGroup {
	private int lesstime = 0;
	public static String batchCode;
	private String[] titles = { "和值", "三同号", "二同号", "不同号", "三连号" };
	/** modify by pengcx 20130517 start */
	private String[] topTitles = { "快三", "快三", "快三", "快三", "快三", "快三" };
	/** modify by pengcx 20130517 end */
	private Class[] allId = { Nmk3HeZhiActivity.class,
			Nmk3ThreeSameActivty.class, Nmk3TwoSameActivty.class,
			Nmk3DiffActivity.class, Nmk3ThreeLinkActivity.class };
	private boolean isFirst = true;

	public AddView addView = new AddView(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置彩种
		setLotno(Constants.LOTNO_NMK3);
		// 初始化标题
		init(titles, topTitles, allId);
		// 获取彩种的期号和上期的开奖号码等信息
		setIssue();
		refreshBtn.setVisibility(View.VISIBLE);
		relativeLayout1.setVisibility(View.GONE);
		betInfoTextView.setVisibility(View.VISIBLE);
	}

	private void updateAddMissViewNum() {
		addView.updateTextNum();
	}

	/**
	 * 赋值给当前期
	 */
	public void setIssue() {
		final Handler sscHandler = new Handler();
		issue.setText("期号获取中....");
		time.setText("获取中...");
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String error_code = "00";
				String re = "";
				String message = "";
				batchCode = "";
				re = GetLotNohighFrequency.getInstance().getInfo(
						Constants.LOTNO_NMK3);
				if (!re.equalsIgnoreCase("")) {
					try {
						JSONObject obj = new JSONObject(re);
						message = obj.getString("message");
						error_code = obj.getString("error_code");
						lesstime = Integer.valueOf(obj
								.getString("time_remaining"));
						batchCode = obj.getString("batchcode");
						while (isRun) {
							if (isEnd(lesstime)) {
								sscHandler.post(new Runnable() {
									public void run() {
										issue.setText("第" + batchCode + "期");
										time.setText("剩余时间:"
												+ PublicMethod
														.isTen(lesstime / 60)
												+ ":"
												+ PublicMethod
														.isTen(lesstime % 60));
									}
								});
								Thread.sleep(1000);
								lesstime--;
							} else {
								sscHandler.post(new Runnable() {
									public void run() {
										issue.setText("第" + batchCode + "期");
										time.setText("剩余时间:00:00");
										nextIssue();
									}
								});
								break;
							}
						}
					} catch (Exception e) {
						sscHandler.post(new Runnable() {
							public void run() {
								issue.setText("获取期号失败");
								time.setText("获取失败");
							}
						});
					}
				} else {

				}
			}
		});
		thread.start();
	}

	private void nextIssue() {
		new AlertDialog.Builder(Nmk3Activity.this)
				.setTitle("提示")
				.setMessage("快三第" + batchCode + "期已经结束,是否转入下一期")
				.setNegativeButton("转入下一期", new Dialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						setIssue();
					}

				})
				.setNeutralButton("返回主页面",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								Nmk3Activity.this.finish();
							}
						}).create().show();
	}

	private boolean isEnd(int time) {
		if (time > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**add by yejc 20130918 start*/
	protected void onResume() {
		super.onResume();
		if (Constants.isDebug) {
			PublicMethod.outLog(this.getClass().getSimpleName(), "onResume()");
		}
		JSONObject obj = PublicMethod.getIssueJSONObject(Constants.LOTNO_NMK3);
		setIssueJSONObject(obj);
	}
	
	
	protected void setIssueJSONObject(JSONObject obj) {
		if (obj != null && !isFirst) {
			try {
				lesstime = Integer.valueOf(CheckUtil.isNull(obj
						.getString("time_remaining")));
				batchCode = obj.getString("batchcode");
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		isFirst = false;
    }
	/**add by yejc 20130918 end*/
}
