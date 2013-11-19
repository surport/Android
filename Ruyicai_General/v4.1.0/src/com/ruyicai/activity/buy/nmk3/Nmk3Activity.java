package com.ruyicai.activity.buy.nmk3;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.miss.AddViewMiss;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;

public class Nmk3Activity extends BuyActivityGroup implements HandlerMsg {
	private int lesstime = 0;
	public static String batchCode;
	private String[] titles = { "和值", "三不同", "二不同", "三同号", "二同号" };
	/** modify by pengcx 20130517 start */
	private String[] topTitles = { "快三--和值", "快三--三不同", "快三--二不同", "快三--三同号",
			"快三--二同号" };
	/** modify by pengcx 20130517 end */
	private Class[] allId = { Nmk3HeZhiActivity.class,
			Nmk3ThreeDiffActivity.class, Nmk3TwoDiffActivity.class,
			Nmk3ThreeSameActivty.class, Nmk3TwoSameActivty.class };
	private boolean isFirst = true;

	public AddView addView = new AddView(this);
	private Controller controller = null;
	private Nmk3ActivityHandler handler = new Nmk3ActivityHandler(this);

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
		initSubViews();
		// betInfoTextView.setVisibility(View.VISIBLE);
	}

	/** add by yejc 20130928 start **/
	private void initSubViews() {
		int padding = PublicMethod.getPxInt(8, this);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.LinearLayout0000);
		linearLayout.setBackgroundResource(R.drawable.nmk3_bg);
		RelativeLayout titleLayout = (RelativeLayout) findViewById(R.id.main_buy_title);
		titleLayout.setBackgroundResource(R.drawable.nmk3_head_bg);
		TextView textView = (TextView) findViewById(R.id.bet_info);
		textView.setVisibility(View.GONE);
		relativeLayout.setBackgroundResource(R.drawable.nmk3_title_bar);
		relativeLayout.setPadding(0, padding, 0, padding);
		LinearLayout line = (LinearLayout) findViewById(R.id.LinearLayout02);
		line.setBackgroundResource(R.color.transparent);
		padding = PublicMethod.getPxInt(5, this);
		LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout01);
		layout.setBackgroundResource(R.drawable.nmk3_change_paly_bg);
		layout.setPadding(padding, padding, padding, padding);
		TabWidget tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		tabWidget.setBackgroundResource(R.drawable.nmk3_change_paly_bg_white);
		padding = PublicMethod.getPxInt(2, this);
		tabWidget.setPadding(padding, padding, padding, padding);
	}

	/** add by yejc 20130928 end **/

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
				String re = "";
				batchCode = "";
				re = GetLotNohighFrequency.getInfo(Constants.LOTNO_NMK3);
				if (!re.equalsIgnoreCase("")) {
					try {
						JSONObject obj = new JSONObject(re);
						lesstime = Integer.valueOf(obj
								.getString("time_remaining"));
						batchCode = obj.getString("batchcode");
						while (isRun) {
							if (isEnd(lesstime)) {
								sscHandler.post(new Runnable() {
									public void run() {
										issue.setText("第" + batchCode + "期");
										time.setText("距开奖还有:"
												+ PublicMethod
														.isTen(lesstime / 60)
												+ "分"
												+ PublicMethod
														.isTen(lesstime % 60)
												+ "秒");
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
		controller = Controller.getInstance(Nmk3Activity.this);
		controller.getIssueJSONObject(handler, Constants.LOTNO_NMK3);
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
	class Nmk3ActivityHandler extends MyHandler {

		public Nmk3ActivityHandler(HandlerMsg msg) {
			super(msg);
			// TODO Auto-generated constructor stub
		}

		public void handleMessage(Message msg) {
			//super.handleMessage(msg);
			if (controller != null) {
				JSONObject obj = controller.getRtnJSONObject();
				setIssueJSONObject(obj);
			}
		}
	}
	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}
}
