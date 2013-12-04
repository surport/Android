package com.ruyicai.activity.notice;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.score.lq.JcLqScoreActivity;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreActivity;
import com.ruyicai.activity.common.PullRefreshListView;
import com.ruyicai.activity.common.PullRefreshListView.OnRefreshListener;
import com.ruyicai.constant.Constants;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.net.newtransaction.NoticeWinInterface;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.PublicMethod;
import com.umeng.analytics.MobclickAgent;

/**
 * 开奖公告
 * 
 * @author haojie
 * 
 */
public class NoticeMainActivity extends Activity implements OnRefreshListener {

	public static final String TAG = "NoticePrizesOfLottery";
	public final static String LOTTERYTYPE = "LOTTERYTYPE";
	public final static String WINNINGNUM = "WINNINGNUM";
	public final static String DATE = "DATA";
	public final static String ISSUE = "ISSUE";
	public final static String FINALDATE = "FINALDATE";
	public final static String MONEYSUM = "MONEYSUM";
	public final static String WINCODE = "winCode";
	public final static String OPENTIME = "openTime";
	public final static String BATCHCODE = "batchCode";
	public final static String TRYCODE = "tryCode";
	public final static int a = R.drawable.kaijiang_lotterytype;

	public final static int ID_MAINLISTVIEW = 1;
	public static float SCALE = 1;
	// 代码添加：超级大乐透
	int redBallViewNum = 7;
	int redBallViewWidth = 22;
	public static int BALL_WIDTH = 46;
	TextView text_lotteryName; // 彩票种类的TextView
	List<Map<String, Object>> list; // zlm 8.9 添加排列三、超级大乐透
	static BallTable kaiJiangGongGaoBallTable = null;
	static LinearLayout iV;
	static String strChuanZhi;
	private static int[] aRedColorResId = { R.drawable.red };
	private static int[] aBlueColorResId = { R.drawable.blue };
	// 周黎鸣 7.5 代码修改：添加代码
	private boolean iQuitFlag = true;
	// 进度条
	private ProgressDialog progressdialog;
	private static final int DIALOG1_KEY = 0;//
	private static final Integer[] mIcon = { R.drawable.join_ssq,
			R.drawable.join_fc3d, R.drawable.join_qlc, R.drawable.join_dlt,
			R.drawable.join_pl3, R.drawable.join_pl5, R.drawable.join_qxc,
			R.drawable.twenty, R.drawable.join_ssc, R.drawable.join_11x5,
			R.drawable.join_11ydj, R.drawable.join_gd11x5, R.drawable.join_sfc,
			R.drawable.join_jcz, R.drawable.join_jcl, R.drawable.notice_ten,
			R.drawable.nmk3, R.drawable.beijingsinglegame_lotterynotice,R.drawable.join_cq11xuan5 }; // zlm
	// 8.9
	// 添加排列三、超级大乐透图标
	private static final String[] titles = { "双色球", "福彩3D", "七乐彩", "大乐透",
			"排列三", "排列五", "七星彩", "22选5", "时时彩", "江西11选5", "11运夺金", "广东11选5",
			"足彩胜负", "竞彩足球", "竞彩篮球", "广东快乐十分", "快三", "北京单场","重庆11选5" };
	// 新加获取时时彩信息
	public static final String iGameName[] = { "ssq", "fc3d", "qlc", "cjdlt",
			"pl3", "pl5", "qxc", "22-5", "ssc", "11-5", "11-ydj", "gd-11-5",
			"sfc","jcz", "jcl", "gd-10", "nmk3",
			"beijingsinglegame","cq-11-5" }; // 8.9
	public static boolean isFirstNotice = true;
	public boolean isnoticefresh = true;
	public boolean ispushfresh = false;

	/**
	 * 消息处理函数
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				showListView(ID_MAINLISTVIEW);
				if (!ispushfresh) {
					progressdialog.dismiss();
				}
				break;
			}

		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_prizes_main);
		setScale();
		ispushfresh = false;
		MobclickAgent.onEvent(this, "kaijianggonggao"); // BY贺思明 点击主导航上的“开奖公告”。
	}

	/**
	 * 根据手机屏幕设置球的大小和球的缩放比例
	 */
	private void setScale() {
		int screenWith = PublicMethod.getDisplayWidth(this);
		if (screenWith <= 240) {
			BALL_WIDTH = 46 * 120 / 240;
			SCALE = (float) 140 / 240;
		} else if (screenWith > 240 && screenWith <= 320) {
			BALL_WIDTH = 46 * 160 / 240;
			SCALE = (float) 180 / 240;
		} else if (screenWith == 480) {
			BALL_WIDTH = 46;
			SCALE = 1;
		} else if (screenWith > 480) {
			BALL_WIDTH = screenWith / 480 * 46;
			SCALE = (float) 1.5;
		}
	}

	/**
	 * 将获取到的开奖信息放到常量类中
	 */
	public void analysisLotteryNoticeJsonObject(JSONObject jobject) {
		// 双色球信息获取
		try {
			Constants.ssqJson = jobject.getJSONObject("ssq");
		} catch (Exception e) {
			// 获取双色球数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.ssqJson == null || !jobject.has("ssq")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "00000000000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.ssqJson = tempObj;
			}
		}
		try {
			Constants.fc3dJson = jobject.getJSONObject("ddd");
		} catch (Exception e) {
			// 获取福彩3D数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.fc3dJson == null || !jobject.has("ddd")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "00000000000000");
						tempObj.put(OPENTIME, "");
						tempObj.put("tryCode", "000000");
					} catch (JSONException e) {

					}
				}
				Constants.fc3dJson = tempObj;
			}
		}

		try {
			Constants.qlcJson = jobject.getJSONObject("qlc");
		} catch (Exception e) {
			// 获取七乐彩数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.qlcJson == null || !jobject.has("qlc")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000000000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.qlcJson = tempObj;
			}
		}

		try {
			Constants.pl3Json = jobject.getJSONObject("pl3");
		} catch (Exception e) {
			// 获取排列三数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.pl3Json == null || !jobject.has("pl3")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "00000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.pl3Json = tempObj;
			}
		}

		try {
			Constants.dltJson = jobject.getJSONObject("dlt");
		} catch (Exception e) {
			// 获取大乐透数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.dltJson == null || !jobject.has("dlt")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "00 00 00 00 00+00 0000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.dltJson = tempObj;
			}
		}

		try {
			Constants.sscJson = jobject.getJSONObject("ssc");
		} catch (Exception e) {
			// 获取实时彩数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.sscJson == null || !jobject.has("ssc")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.sscJson = tempObj;
			}
		}

		try {
			Constants.dlcJson = jobject.getJSONObject("11-5");
		} catch (Exception e) {
			// 获取实时彩数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.dlcJson == null || !jobject.has("11-5")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.dlcJson = tempObj;
			}
		}

		try {
			Constants.ydjJson = jobject.getJSONObject("11-ydj");
		} catch (Exception e) {
			// 获取实时彩数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.ydjJson == null || !jobject.has("11-ydj")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.ydjJson = tempObj;
			}
		}

		try {
			Constants.twentyJson = jobject.getJSONObject("22-5");
		} catch (Exception e) {
			// 获取22-5数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.twentyJson == null || !jobject.has("22-5")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.twentyJson = tempObj;
			}
		}
		try {
			Constants.sfcJson = jobject.getJSONObject("sfc");
		} catch (Exception e) {
			// 获取胜负彩数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.sfcJson == null || !jobject.has("sfc")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.sfcJson = tempObj;
			}
		}

		try {
			Constants.rx9Json = jobject.getJSONObject("rx9");
		} catch (Exception e) {
			// 获取任选9数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.rx9Json == null || !jobject.has("rx9")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.rx9Json = tempObj;
			}
		}

		try {
			Constants.half6Json = jobject.getJSONObject("6cb");
		} catch (Exception e) {
			// 获取6场半数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.half6Json == null || !jobject.has("6cb")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.half6Json = tempObj;
			}
		}
		// 广东11-5
		try {
			Constants.gd115Json = jobject.getJSONObject("gd-11-5");
		} catch (Exception e) {
			// 获取进球彩数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.gd115Json == null || !jobject.has("gd-11-5")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.gd115Json = tempObj;
			}
		}
		// 广东快乐十分
		try {
			Constants.gd10Json = jobject.getJSONObject("gd-happy-10");
		} catch (Exception e) {
			// 获取进球彩数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.gd10Json == null || !jobject.has("gd-happy-10")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000000000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.gd10Json = tempObj;
			}
		}
		try {
			Constants.jqcJson = jobject.getJSONObject("jqc");
		} catch (Exception e) {
			// 获取进球彩数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.jqcJson == null || !jobject.has("jqc")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.jqcJson = tempObj;
			}
		}
		// 排列五
		try {
			Constants.pl5Json = jobject.getJSONObject("pl5");
		} catch (Exception e) {
			// 获取进球彩数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.pl5Json == null || !jobject.has("pl5")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "00000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.pl5Json = tempObj;
			}
		}
		// 七星彩
		try {
			Constants.qxcJson = jobject.getJSONObject("qxc");
		} catch (Exception e) {
			// 获取进球彩数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.qxcJson == null || !jobject.has("qxc")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.qxcJson = tempObj;
			}
		}

		// 内蒙快三
		try {
			Constants.nmk3Json = jobject.getJSONObject("nmks");
		} catch (Exception e) {
			// 获取进球彩数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.nmk3Json == null || !jobject.has("nmks")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.nmk3Json = tempObj;
			}
		}
		
		// 重庆11选5
		try {
			Constants.cq11x5Json = jobject.getJSONObject("cq-11-5");
		} catch (Exception e) {
			// 获取进球彩数据出现异常
			e.printStackTrace();
		} finally {
			// 判断是否已经从网络上获取到了数据
			if (Constants.cq11x5Json == null || !jobject.has("cq-11-5")) {
				// 没数据,初始化点数居
				JSONObject tempObj = new JSONObject();
				for (int i = 0; i < 5; i++) {
					try {
						tempObj.put(BATCHCODE, "");
						tempObj.put(WINCODE, "0000000000000000");
						tempObj.put(OPENTIME, "");
					} catch (JSONException e) {

					}
				}
				Constants.cq11x5Json = tempObj;
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
		if (isnoticefresh) {
			noticeNet();
		} else {
			isnoticefresh = true;
		}
	}

	private void noticeNet() {
		if (!ispushfresh) {
			showDialog(DIALOG1_KEY);
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject lotteryInfos = NoticeWinInterface.getInstance()
						.getLotteryAllNotice();// 开奖信息json对象
				// 将获取到的开奖信息放到常量类中
				analysisLotteryNoticeJsonObject(lotteryInfos);
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		}).start();
	}

	/**
	 * 开奖公告里主列表与子列表之间的跳转
	 * 
	 * @param listViewID
	 *            列表ID
	 */
	private void showListView(int listViewID) {
		iQuitFlag = false;
		switch (listViewID) {
		case ID_MAINLISTVIEW:
			iQuitFlag = true;
			showMainListView();
			break;

		}
	}

	/**
	 * 退出检测
	 * 
	 * @param keyCode
	 *            返回按键的号码
	 * @param event
	 *            事件
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case 4: {
			if (iQuitFlag == false) {
				setContentView(R.layout.notice_prizes_main);
				showListView(ID_MAINLISTVIEW);
			} else {
				ExitDialogFactory.createExitDialog(this);
			}
			break;
		}
		}
		return false;
	}

	/**
	 * 显示主列表
	 */
	private void showMainListView() {
		setContentView(R.layout.notice_prizes_main);

		PullRefreshListView listview = (PullRefreshListView) findViewById(R.id.notice_prizes_listview);
		list = NoticeDataProvider
				.getListForMainListViewSimpleAdapter(NoticeMainActivity.this);// 获取开奖信息数据

		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE };
		MainEfficientAdapter adapter = new MainEfficientAdapter(this, str, list);
		listview.setDividerHeight(0);
		listview.setAdapter(adapter);
		listview.setonRefreshListener(this);
		// 设置点击监听
		OnItemClickListener clickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String iIssue = (String) list.get(position - 1).get(ISSUE);
				NoticeActivityGroup.ISSUE = iIssue;
				TextView nameText = (TextView) view
						.findViewById(R.id.notice_prizes_main_title);
				String name = nameText.getText().toString();
				isnoticefresh = false;
				// 点击福彩3D跳转到福彩3D子列表中
				if (name.equals("福彩3D")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					intent.putExtra("isPosition", false);
					startActivity(intent);
				}
				// 点击七乐彩跳转到七乐彩子列表中
				if (name.equals("七乐彩")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					intent.putExtra("isPosition", false);
					startActivity(intent);
				}
				// 点击双色球跳转到双色球子列表中
				if (name.equals("双色球")) {
					MobclickAgent.onEvent(NoticeMainActivity.this,
							"kaijiangshuangseqiu"); // BY贺思明 点击“双色球”图标及开奖号区域。
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					intent.putExtra("isPosition", false);
					startActivity(intent);
				}
				// 点击排列三跳转到排列三子列表中
				if (name.equals("排列三")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					intent.putExtra("isPosition", false);
					startActivity(intent);
				}
				// 点击超级大乐透跳转到超级大乐透子列表中
				if (name.equals("大乐透")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_DLT_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					intent.putExtra("isPosition", false);
					startActivity(intent);
				}
				// 点击时时彩跳转到时时彩子列表中
				if (name.equals("时时彩")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					startActivity(intent);
				}
				// 点击11-5跳转到11-5子列表中
				if (name.equals("江西11选5")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_DLC_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					intent.putExtra("isPosition", false);
					startActivity(intent);
				}
				// 点击11-5跳转到11运夺金子列表中
				if (name.equals("11运夺金")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_YDJ_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					startActivity(intent);
				}
				// 点击22选5子列表中
				if (name.equals("22选5")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					startActivity(intent);
				}
				// 点击时时彩跳转到胜负彩子列表中
				if (name.equals("足彩胜负")) {
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeZCActivity.class);
					startActivity(intent);
				}
				// 点击时时彩跳转到进球彩子列表中
				if (name.equals("排列五")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_PL5_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					startActivity(intent);
				}
				// 点击时时彩跳转到进球彩子列表中
				if (name.equals("七星彩")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_QXC_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					startActivity(intent);
				}
				if (name.equals("广东11选5")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_GD115_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					intent.putExtra("isPosition", false);
					startActivity(intent);
				}
				// 广东快乐十分
				if (name.equals("广东快乐十分")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_GD10_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					startActivity(intent);
				}

				// 内蒙快三
				if (name.equals("快三")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_NMK3_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					startActivity(intent);
				}
				
				if (name.equals("重庆11选5")) {
					NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_CQ11X5_LISTVIEW;
					Intent intent = new Intent(NoticeMainActivity.this,
							NoticeActivityGroup.class);
					startActivity(intent);
				}
			}

		};

		listview.setOnItemClickListener(clickListener);
	}

	/**
	 * 购彩大厅 主列表适配器
	 * 
	 */
	public class MainEfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;
		private String[] mIndex;
		private Context context;

		public MainEfficientAdapter(Context context, String[] index,
				List<Map<String, Object>> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
			mIndex = index;
			this.context = context;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// 设置主列表布局中的详细内容
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String iGameType = (String) mList.get(position).get(mIndex[0]);

			String Codetry = "";
			if (iGameType.equals("fc3d")) {
				Codetry = (String) mList.get(position).get("tryCode");
			}
			String iNumbers = (String) mList.get(position).get(mIndex[1]);
			String iDate = (String) mList.get(position).get(mIndex[2]);
			final String iIssue = (String) mList.get(position).get(mIndex[3]);
			String iIssueNo = "第" + iIssue + "期";
			ViewHolder holder = null;

			convertView = mInflater.inflate(R.layout.notice_prizes_main_layout,
					null);

			holder = new ViewHolder();
			// 彩种图标
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			// 彩种名称
			holder.name = (TextView) convertView
					.findViewById(R.id.notice_prizes_main_title);
			// 开奖号码布局
			holder.numbers = (LinearLayout) convertView
					.findViewById(R.id.ball_linearlayout);
			// 开奖日期
			holder.date = (TextView) convertView
					.findViewById(R.id.notice_prizes_dateAndTimeId);
			// 期号
			holder.issue = (TextView) convertView
					.findViewById(R.id.notice_prizes_issueId);
			holder.lookBtn = (Button) convertView
					.findViewById(R.id.notice_prizes_main_btn_jc);
			holder.scoreBtn = (Button) convertView
					.findViewById(R.id.notice_prizes_main_btn_score);
			holder.rLayout = (RelativeLayout) convertView
					.findViewById(R.id.notice_prizes_relative);
			holder.numbers.removeAllViews();
			convertView.setTag(holder);

			// 设置彩种名称
			holder.name.setText(getName(iGameType));
			// 设置彩种图标
			holder.icon.setImageResource(getIcon(iGameType));

			if (iGameType.equals("ssq")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);

				// zlm 7.28 代码修改：添加号码排序
				int i1, i2, i3;
				String iShowNumber;
				OneBallView tempBallView;
				int[] ssqInt01 = new int[6];
				int[] ssqInt02 = new int[6];
				String[] ssqStr = new String[6];

				for (i2 = 0; i2 < 6; i2++) {
					iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
					ssqInt01[i2] = Integer.valueOf(iShowNumber);
				}

				ssqInt02 = PublicMethod.sort(ssqInt01);

				for (i3 = 0; i3 < 6; i3++) {
					if (ssqInt02[i3] < 10) {
						ssqStr[i3] = "0" + ssqInt02[i3];
					} else {
						ssqStr[i3] = "" + ssqInt02[i3];
					}
				}
				for (i1 = 0; i1 < 6; i1++) {
					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ssqStr[i1],
							aRedColorResId);
					tempBallView.setScaleType(ScaleType.CENTER_INSIDE);
					holder.numbers.addView(tempBallView);
				}

				iShowNumber = iNumbers.substring(12, 14);
				tempBallView = new OneBallView(convertView.getContext(), 1);
				tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
						aBlueColorResId);

				holder.numbers.addView(tempBallView);
			} else if (iGameType.equals("fc3d")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				// zlm 7.30 代码修改：修改福彩3D号码
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 3; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1 * 2,
							i1 * 2 + 2));
					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
							+ iShowNumber, aRedColorResId);
					holder.numbers.addView(tempBallView);
				}
				TextView trycode = new TextView(NoticeMainActivity.this);
				trycode.setTextColor(Color.BLUE);
				trycode.setTextSize(17);
				trycode.setPadding(10, 10, 0, 0);
				String codeshow = "";
				try {
					for (i1 = 0; i1 < 3; i1++) {
						codeshow += Codetry.substring(i1 * 2 + 1, i1 * 2 + 2);
						if (i1 != 2) {
							codeshow += ",";
						}

					}
				} catch (Exception e) {

				}
				trycode.setText("试机号:" + codeshow);
				holder.numbers.addView(trycode);
			} else if (iGameType.equals("qlc")) {
				int deletW = 4;
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				// zlm 7.28 代码修改：添加号码排序
				int i1, i2, i3;
				String iShowNumber;
				OneBallView tempBallView;

				int[] ssqInt01 = new int[7];
				int[] ssqInt02 = new int[7];
				String[] ssqStr = new String[7];

				for (i2 = 0; i2 < 7; i2++) {
					iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
					ssqInt01[i2] = Integer.valueOf(iShowNumber);
				}

				ssqInt02 = PublicMethod.sort(ssqInt01);

				for (i3 = 0; i3 < 7; i3++) {
					if (ssqInt02[i3] < 10) {
						ssqStr[i3] = "0" + ssqInt02[i3];
					} else {
						ssqStr[i3] = "" + ssqInt02[i3];
					}
				}
				for (i1 = 0; i1 < 7; i1++) {

					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH - deletW, BALL_WIDTH
							- deletW, ssqStr[i1], aRedColorResId);
					holder.numbers.addView(tempBallView);
				}
				// zlm 8.3 代码修改 ：添加七乐彩蓝球
				iShowNumber = iNumbers.substring(14, 16);
				tempBallView = new OneBallView(convertView.getContext(), 1);
				tempBallView.initBall(BALL_WIDTH - deletW, BALL_WIDTH - deletW,
						iShowNumber, aBlueColorResId);

				holder.numbers.addView(tempBallView);
			} else if (iGameType.equals("cjdlt")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				// zlm 7.28 代码修改：添加号码排序
				int i1, i2, i3;
				String iShowNumber = "";
				OneBallView tempBallView;
				int[] cjdltInt01 = new int[5];
				int[] cjdltInt02 = new int[5];
				int[] cjdltInt03 = new int[2];
				int[] cjdltInt04 = new int[2];
				String[] cjdltStr = new String[5];
				String[] cjdltStr1 = new String[2];

				for (i2 = 0; i2 < 5; i2++) {
					iShowNumber = iNumbers.substring(i2 * 3, i2 * 3 + 2);
					cjdltInt01[i2] = Integer.valueOf(iShowNumber);
				}

				cjdltInt02 = PublicMethod.sort(cjdltInt01);

				for (i3 = 0; i3 < 5; i3++) {
					if (cjdltInt02[i3] < 10) {
						cjdltStr[i3] = "0" + cjdltInt02[i3];
					} else {
						cjdltStr[i3] = "" + cjdltInt02[i3];
					}
				}
				for (i1 = 0; i1 < 5; i1++) {

					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, cjdltStr[i1],
							aRedColorResId);
					holder.numbers.addView(tempBallView);
				}

				for (i2 = 0; i2 < 2; i2++) {
					try {
						iShowNumber = iNumbers.substring(i2 * 3 + 15,
								i2 * 3 + 17);
					} catch (Exception e) {
						e.printStackTrace();
					}
					cjdltInt03[i2] = Integer.valueOf(iShowNumber);
				}

				cjdltInt04 = PublicMethod.sort(cjdltInt03);

				for (i3 = 0; i3 < 2; i3++) {
					if (cjdltInt04[i3] < 10) {
						cjdltStr1[i3] = "0" + cjdltInt04[i3];
					} else {
						cjdltStr1[i3] = "" + cjdltInt04[i3];
					}
				}

				for (i1 = 0; i1 < 2; i1++) {
					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
							cjdltStr1[i1], aBlueColorResId);

					holder.numbers.addView(tempBallView);
				}
			} else if (iGameType.equals("pl3")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 3; i1++) {
					iShowNumber = Integer.valueOf(iNumbers
							.substring(i1, i1 + 1));
					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
							+ iShowNumber, aRedColorResId);
					holder.numbers.addView(tempBallView);
				}
			} else if (iGameType.equals("pl5")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 5; i1++) {
					iShowNumber = Integer.valueOf(iNumbers
							.substring(i1, i1 + 1));
					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
							+ iShowNumber, aRedColorResId);
					holder.numbers.addView(tempBallView);
				}
			} else if (iGameType.equals("qxc")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 7; i1++) {
					iShowNumber = Integer.valueOf(iNumbers
							.substring(i1, i1 + 1));
					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
							+ iShowNumber, aRedColorResId);
					holder.numbers.addView(tempBallView);
				}
			} else if (iGameType.equals("ssc")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 5; i1++) {
					iShowNumber = Integer.valueOf(iNumbers
							.substring(i1, i1 + 1));
					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
							+ iShowNumber, aRedColorResId);
					holder.numbers.addView(tempBallView);
				}

				TextView singleOrSmallTextView = new TextView(context);
				String resultString = PublicMethod.formatSSCNum(iNumbers, 1);
				String singleOrSmall = resultString.substring(10);
				singleOrSmall = singleOrSmall.replace(',', ' ');

				singleOrSmallTextView.setGravity(Gravity.CENTER);
				singleOrSmallTextView.setTypeface(Typeface
						.defaultFromStyle(Typeface.BOLD));
				singleOrSmallTextView.getPaint().setFakeBoldText(true);
				singleOrSmallTextView.setTextColor(Color.WHITE);

				singleOrSmallTextView.setText(singleOrSmall);
				singleOrSmallTextView
						.setBackgroundResource(R.drawable.singleorsmall);

				holder.numbers.addView(singleOrSmallTextView);
			} else if (iGameType.equals("11-5")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 5; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1 * 2,
							i1 * 2 + 2));
					String isNum = PublicMethod.getZhuMa(iShowNumber);
					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, isNum,
							aRedColorResId);
					holder.numbers.addView(tempBallView);

				}

			} else if (iGameType.equals("11-ydj")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 5; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1 * 2,
							i1 * 2 + 2));
					String isNum = PublicMethod.getZhuMa(iShowNumber);
					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, isNum,
							aRedColorResId);
					holder.numbers.addView(tempBallView);

				}
			} else if (iGameType.equals("22-5")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 5; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1 * 2,
							i1 * 2 + 2));
					String isNum = PublicMethod.getZhuMa(iShowNumber);
					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, isNum,
							aRedColorResId);
					holder.numbers.addView(tempBallView);

				}
			} else if (iGameType.equals("sfc")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);

				TextView tvFootball = new TextView(convertView.getContext());
				tvFootball.setTextSize(25);
				tvFootball.setGravity(Gravity.RIGHT);
				tvFootball.setText(iNumbers);
				holder.numbers.addView(tvFootball);
			} else if (iGameType.equals("rxj")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				TextView tvFootball = new TextView(convertView.getContext());
				tvFootball.setTextSize(25);
				tvFootball.setGravity(Gravity.RIGHT);
				tvFootball.setText(iNumbers);
				holder.numbers.addView(tvFootball);
			} else if (iGameType.equals("lcb")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				TextView tvFootball = new TextView(convertView.getContext());
				tvFootball.setGravity(Gravity.RIGHT);
				tvFootball.setTextSize(25);
				tvFootball.setText(iNumbers);
				holder.numbers.addView(tvFootball);
			} else if (iGameType.equals("jqc")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				TextView tvFootball = new TextView(convertView.getContext());
				tvFootball.setTextSize(25);
				tvFootball.setGravity(Gravity.RIGHT);
				tvFootball.setText(iNumbers);
				holder.numbers.addView(tvFootball);
			} else if (iGameType.equals("gd-11-5")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 5; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1 * 2,
							i1 * 2 + 2));
					String isNum = PublicMethod.getZhuMa(iShowNumber);
					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, isNum,
							aRedColorResId);
					holder.numbers.addView(tempBallView);
				}

			} else if (iGameType.equals("gd-10")) {
				int deletW = 4;
				/** add by pengcx 20130802 start */
				int width = PublicMethod.getDisplayWidth(context);
				float scale = 480.0f / width;
				float textSize = 10 * scale;
				holder.name.setTextSize(PublicMethod
						.getPxInt(textSize, context));
				/** add by pengcx 20130802 end */
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 8; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1 * 2,
							i1 * 2 + 2));
					if (iShowNumber == 19 || iShowNumber == 20) {
						tempBallView = new OneBallView(
								convertView.getContext(), 1);
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
								+ iShowNumber, aRedColorResId);
						holder.numbers.addView(tempBallView);
					} else {
						tempBallView = new OneBallView(
								convertView.getContext(), 1);
						tempBallView.initBall(BALL_WIDTH - deletW, BALL_WIDTH
								- deletW, "" + iShowNumber, aBlueColorResId);
						holder.numbers.addView(tempBallView);
					}
				}

			} else if (iGameType.equals("jcz")) {
				holder.rLayout.setVisibility(RelativeLayout.GONE);
				holder.lookBtn.setVisibility(Button.VISIBLE);
				holder.lookBtn
						.setBackgroundResource(R.drawable.join_info_btn_selecter);
				holder.lookBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						isnoticefresh = false;
						Intent intent = new Intent(context,
								NoticeJcActivity.class);

						context.startActivity(intent);
					}
				});
				holder.scoreBtn.setVisibility(Button.VISIBLE);
				holder.scoreBtn
						.setBackgroundResource(R.drawable.join_info_btn_selecter);
				holder.scoreBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						isnoticefresh = false;
						Intent intent = new Intent(context,
								JcScoreActivity.class);

						context.startActivity(intent);
					}
				});
			} else if (iGameType.equals("jcl")) {
				holder.rLayout.setVisibility(RelativeLayout.GONE);
				holder.lookBtn.setVisibility(Button.VISIBLE);
				holder.lookBtn
						.setBackgroundResource(R.drawable.join_info_btn_selecter);
				holder.lookBtn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						isnoticefresh = false;
						Intent intent = new Intent(context,
								NoticeJclActivity.class);
						context.startActivity(intent);
					}
				});
				holder.scoreBtn.setVisibility(Button.VISIBLE);
				holder.scoreBtn
						.setBackgroundResource(R.drawable.join_info_btn_selecter);
				holder.scoreBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						isnoticefresh = false;
						Intent intent = new Intent(context,
								JcLqScoreActivity.class);

						context.startActivity(intent);
					}
				});
			}
			// 显示内蒙快三
			else if (iGameType.equals("nmk3")) {
				// 显示日期和期号
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);

				// 显示开奖球号
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 3; i1++) {
					/** modify by pengcx 20130808 start */
					iShowNumber = Integer.valueOf(iNumbers.substring(
							i1 * 2 + 1, i1 * 2 + 2));
					/** modify by pengcx 20130808 end */
					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber
							+ "", aRedColorResId);
					holder.numbers.addView(tempBallView);
				}
			}

			// 北京单场
			else if (iGameType.equals(iGameName[17])) {
				holder.rLayout.setVisibility(RelativeLayout.GONE);
				holder.lookBtn.setVisibility(Button.VISIBLE);
				holder.lookBtn
						.setBackgroundResource(R.drawable.join_info_btn_selecter);
				holder.lookBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						isnoticefresh = false;
						Intent intent = new Intent(context,
								NoticeBeijingSingleActivity.class);
						intent.putExtra(Constants.PLAY_METHOD_TYPE,
								Constants.LOTNO_BEIJINGSINGLEGAME_WINTIELOSS);
						context.startActivity(intent);
					}
				});
				holder.scoreBtn.setVisibility(View.GONE);
			}
			
			else if (iGameType.equals("cq-11-5")) {
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 5; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1 * 2,
							i1 * 2 + 2));
					String isNum = PublicMethod.getZhuMa(iShowNumber);
					tempBallView = new OneBallView(convertView.getContext(), 1);
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, isNum,
							aRedColorResId);
					holder.numbers.addView(tempBallView);

				}
			}

			return convertView;
		}

		class ViewHolder {
			ImageView icon;
			TextView name;
			LinearLayout numbers;
			TextView date;
			TextView issue;
			Button lookBtn;
			Button scoreBtn;
			RelativeLayout rLayout;
		}
	}

	public String getName(String type) {
		String name = null;
		for (int i = 0; i < iGameName.length; i++) {
			if (type.equals(iGameName[i])) {
				name = titles[i];
				break;
			}
		}
		return name;
	}

	public Integer getIcon(String type) {
		int imgId = 0;
		for (int i = 0; i < iGameName.length; i++) {
			if (type.equals(iGameName[i])) {
				imgId = mIcon[i];
				break;
			}
		}
		return imgId;
	}

	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG1_KEY: {
			progressdialog = new ProgressDialog(this);

			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}

	@Override
	public void onRefresh() {
		ispushfresh = true;
		noticeNet();
	}
}
