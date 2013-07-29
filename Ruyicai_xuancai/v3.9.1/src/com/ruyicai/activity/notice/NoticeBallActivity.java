package com.ruyicai.activity.notice;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.miss.AddViewMiss;
import com.ruyicai.activity.buy.miss.OrderDetails;
import com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfo;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.code.dlt.DltNormalSelectCode;
import com.ruyicai.code.qlc.QlcZiZhiXuanCode;
import com.ruyicai.code.ssq.SsqZiZhiXuanCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.PrizeInfoInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 开奖走势基类
 * 
 */
public class NoticeBallActivity extends Activity {
	LinearLayout layout;
	// 红球走势图控件
	NoticeBallView ballRedView;
	// 篮球走势图控件
	NoticeBallView ballBlueView;

	protected boolean isRed;
	List<JSONObject> listall;
	protected TextView textRedCode;
	protected TextView textBlueCode;
	RelativeLayout bottomlayout;
	Button touzhuBtn;
	private RWSharedPreferences shellRW;
	protected HorizontalScrollView hScrollView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_ball_main);
		// 选号栏相对布局
		bottomlayout = (RelativeLayout) findViewById(R.id.notice_ball_relative_bottom);
		// 开奖走势滑动布局
		hScrollView = (HorizontalScrollView) findViewById(R.id.HorizontalScrollView01);

		// 开奖走势滑动布局
		layout = (LinearLayout) findViewById(R.id.notice_ball_main_linear);
		// 选号红球
		textRedCode = (TextView) findViewById(R.id.notice_ball_red_text_code);
		// 选号篮球
		textBlueCode = (TextView) findViewById(R.id.notice_ball_blue_text_code);
		// 投注按钮
		touzhuBtn = (Button) findViewById(R.id.notice_ball_btn_touzhu);
		touzhuBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				begainTouZhu();
			}
		});
	}

	public List<JSONObject> getballlist() {
		return listall;
	}

	/**
	 * 联网获取所有彩种开奖信息
	 */
	public void noticeAllNet(final boolean isRed) {
		// 如果是第一次加载，则使用独立线程
		if (NoticeMainActivity.isFirstNotice) {
			final ProgressDialog dialog = UserCenterDialog.onCreateDialog(this);
			dialog.show();
			final Handler handler = new Handler();
			new Thread(new Runnable() {
				@Override
				public void run() {
					dialog.cancel();
					handler.post(new Runnable() {
						@Override
						public void run() {
							addBallView(isRed);
						}
					});
				}

			}).start();
		} else {
			addBallView(isRed);
		}
	}

	/**
	 * 开奖公告里主列表与子列表之间的跳转
	 * 
	 * @param listViewID
	 *            列表ID
	 */
	public void addBallView(boolean isRed) {
		this.isRed = isRed;
		List<JSONObject> list = null;
		switch (NoticeActivityGroup.LOTNO) {
		// 广东11-5
		case NoticeActivityGroup.ID_SUB_GD115_LISTVIEW:
			// 创建红球走势图控件对象
			ballRedView = new NoticeBallView(this);
			// 创建篮球走势图控件对象
			ballBlueView = new NoticeBallView(this);
			// 联网获取开奖信息
			list = getSubInfoForListView(Constants.LOTNO_GD115);
			// 初始化控球走势图控件对象
			ballRedView.initNoticeBall(list.size(), 11, 1, list, true,
					"gd11-5", 1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);

			break;
		// 广东快乐十分
		case NoticeActivityGroup.ID_SUB_GD10_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_ten);
			ballRedView.initNoticeBall(list.size(), 18, 1, list, false,
					"gd-10", 1 * NoticeMainActivity.SCALE);
			ballBlueView.initNoticeBall(list.size(), 2, 19, list, true,
					"gd-10", 1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);
			hScrollView.setPadding(0, 0, 0, 0);

			break;
		case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_SSQ);
			ballRedView.setTextCode(textRedCode);
			ballBlueView.setTextCode(textBlueCode);
			ballRedView.initNoticeBall(list.size(), 33, 1, list, true, "ssq",
					1 * NoticeMainActivity.SCALE);
			ballBlueView.initNoticeBall(list.size(), 16, 1, list, false, "ssq",
					1 * NoticeMainActivity.SCALE);
			bottomlayout.setVisibility(View.VISIBLE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);

			break;
		case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_FC3D);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "fc3d",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_QLC);
			ballRedView.initNoticeBall(list.size(), 30, 1, list, true, "qlc",
					1 * NoticeMainActivity.SCALE);
			ballBlueView.initNoticeBall(list.size(), 30, 1, list, false, "qlc",
					1 * NoticeMainActivity.SCALE);
			ballRedView.setTextCode(textRedCode);
			ballBlueView.setTextCode(textBlueCode);
			bottomlayout.setVisibility(View.VISIBLE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);
			break;
		case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
			// zlm 排列三
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_PL3);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "pl3",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_PL5_LISTVIEW:
			// zlm 排列五
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_PL5);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "pl5",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_QXC_LISTVIEW:
			// zlm 七星彩
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_QXC);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "qxc",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			// zlm 超级大乐透
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_DLT);
			ballRedView.initNoticeBall(list.size(), 35, 1, list, true, "cjdlt",
					1 * NoticeMainActivity.SCALE);
			ballBlueView.initNoticeBall(list.size(), 12, 1, list, false,
					"cjdlt", 1 * NoticeMainActivity.SCALE);
			ballRedView.setTextCode(textRedCode);
			ballBlueView.setTextCode(textBlueCode);
			bottomlayout.setVisibility(View.VISIBLE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);
			break;
		case NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW:
			// zlm 时时彩
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_SSC);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "ssc",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_DLC_LISTVIEW:
			// zlm 11-5彩
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_11_5);
			ballRedView.initNoticeBall(list.size(), 11, 1, list, isRed, "11-5",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_YDJ_LISTVIEW:
			// zlm 11-5彩
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_eleven);
			ballRedView.initNoticeBall(list.size(), 11, 1, list, isRed,
					"11-ydj", 1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_22_5);
			ballRedView.initNoticeBall(list.size(), 22, 1, list, isRed, "22-5",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;

		// 内蒙快三
		case NoticeActivityGroup.ID_SUB_NMK3_LISTVIEW:
			// 创建红球走势图控件对象
			ballRedView = new NoticeBallView(this);
			// 联网获取开奖信息
			list = getSubInfoForListView(Constants.LOTNO_NMK3);
			// 初始化控球走势图控件对象
			ballRedView.initNoticeBall(list.size(), 6, 1, list, true, "nmk3",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		}
		listall = list;
	}

	private List<Integer> getList(String codeStr) {
		List<Integer> list = new ArrayList<Integer>();
		String codeStrs[] = codeStr.split(",");
		for (int i = 0; i < codeStrs.length; i++) {
			if (!codeStrs[i].equals("")) {
				try {
					Integer codeInt = new Integer(codeStrs[i]);
					list.add(codeInt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	private void begainTouZhu() {
		if (!isLogin()) {
			Intent intent = new Intent(this, UserLogin.class);
			startActivity(intent);
		} else {
			String redStr = textRedCode.getText().toString();
			String blueStr = textBlueCode.getText().toString();
			if (blueStr.contains("|")) {
				blueStr = blueStr.substring(1, blueStr.length());
			}
			List<Integer> redList = getList(redStr);
			List<Integer> blueList = getList(blueStr);

			setTouZhuInfo(redList, blueList);

		}

	}

	private void setTouZhuInfo(List<Integer> redList, List<Integer> blueList) {
		String code = "";
		long betNums = 0;
		String lotno = "";
		int redNum = 0, blueNum = 0;
		switch (NoticeActivityGroup.LOTNO) {
		case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
			redNum = 6;
			blueNum = 1;
			if (isBetLegitimacy(redList.size(), blueList.size(), redNum,
					blueNum)) {
				lotno = Constants.LOTNO_SSQ;
				code = SsqZiZhiXuanCode.simulateZhuma(redList, blueList);
				betNums = caculateBetNums(redList.size(), blueList.size(),
						redNum, blueNum);
				addViewAndTouZhu(betNums, redList, blueList, lotno, code);
			}
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			redNum = 7;
			if (isBetLegitimacy(redList.size(), blueList.size(), redNum,
					blueNum)) {
				lotno = Constants.LOTNO_QLC;
				code = QlcZiZhiXuanCode.simulateZhuma(redList, blueList);
				betNums = caculateBetNums(redList.size(), redNum);
				addViewAndTouZhu(betNums, redList, blueList, lotno, code);
			}
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			redNum = 5;
			blueNum = 2;
			if (isBetLegitimacy(redList.size(), blueList.size(), redNum,
					blueNum)) {
				lotno = Constants.LOTNO_DLT;
				code = DltNormalSelectCode.simulateZhuma(redList, blueList);
				betNums = caculateBetNums(redList.size(), blueList.size(),
						redNum, blueNum);
				addViewAndTouZhu(betNums, redList, blueList, lotno, code);
			}

			break;

		}

	}

	private boolean isLogin() {
		boolean isLogin = true;

		shellRW = new RWSharedPreferences(this, "addInfo");
		String sessionidStr = shellRW.getStringValue("sessionid");
		if (sessionidStr == null || sessionidStr.equals("")) {
			isLogin = false;
		}

		return isLogin;
	}

	private boolean isBetLegitimacy(int redSize, int blueSize, int redNum,
			int blueNum) {

		String promptString = null;
		boolean isTrue = true;

		if (redSize < redNum) {
			promptString = "请选择至少" + redNum + "个红球";
			isTrue = false;
		} else if (blueSize < blueNum) {
			promptString = "请选择" + blueNum + "个蓝球";
			isTrue = false;
		}
		if (!isTrue) {
			Toast.makeText(this, promptString, Toast.LENGTH_LONG).show();
		}

		return isTrue;
	}

	private void addViewAndTouZhu(long betNums, List<Integer> reds,
			List<Integer> blues, String lotno, String code) {

		if (betNums > 10000) {
			dialogExcessive(10000);
		} else {
			BetAndGiftPojo betAndGiftPojo = new BetAndGiftPojo();

			String sessionid = shellRW.getStringValue("sessionid");
			String phonenum = shellRW.getStringValue("phonenum");
			String userno = shellRW.getStringValue("userno");

			betAndGiftPojo.setSessionid(sessionid);
			betAndGiftPojo.setPhonenum(phonenum);
			betAndGiftPojo.setUserno(userno);
			betAndGiftPojo.setBettype("bet");
			betAndGiftPojo.setBet_code("");
			betAndGiftPojo.setLotmulti("1");
			betAndGiftPojo.setBatchnum("1");
			betAndGiftPojo.setSellway("0");
			betAndGiftPojo.setLotno(lotno);
			betAndGiftPojo.setZhushu(String.valueOf(betNums));
			betAndGiftPojo.setAmount("" + betNums * 200);
			betAndGiftPojo.setIsSellWays("1");
			
			if (lotno.equals(Constants.LOTNO_DLT)) {
				betAndGiftPojo.setZhui(true);
			}

			AddViewMiss addViewMiss = new AddViewMiss(this);
			CodeInfo codeInfo = addViewMiss.initCodeInfo(2, 1);
			codeInfo.setTouZhuCode(code);
			codeInfo.setZhuShu(Integer.valueOf(String.valueOf(betNums)));
			codeInfo.setAmt(Integer.valueOf(String.valueOf(betNums * 2)));
			codeInfo = setCodeInfoColor(codeInfo, reds, blues);
			addViewMiss.addCodeInfo(codeInfo);

			ApplicationAddview app = (ApplicationAddview) getApplicationContext();
			app.setPojo(betAndGiftPojo);
			app.setAddviewmiss(addViewMiss);
			Intent intent = new Intent(this, OrderDetails.class);
			intent.putExtra("isAlert", false);
			startActivity(intent);
		}
	}

	public void dialogExcessive(int maxNums) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(this.getString(R.string.toast_touzhu_title).toString());
		builder.setMessage("单笔投注不能大于" + maxNums + "注！");

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}

	private CodeInfo setCodeInfoColor(CodeInfo codeInfo, List<Integer> redList,
			List<Integer> blueList) {
		StringBuffer redString = new StringBuffer();
		StringBuffer blueString = new StringBuffer();

		int redSize = redList.size();
		int blueSize = blueList.size();

		for (int red = 0; red < redSize - 1; red++) {
			redString.append(integerToString(redList.get(red)) + ",");
		}

		redString.append(integerToString(redList.get(redSize - 1)));
		codeInfo.addAreaCode(redString.toString(), Color.RED);

		if (blueSize != 0) {
			for (int blue = 0; blue < blueSize - 1; blue++) {
				blueString.append(integerToString(blueList.get(blue)) + ",");
			}

			blueString.append(integerToString(blueList.get(blueSize - 1)));
			codeInfo.addAreaCode(blueString.toString(), Color.BLUE);
		}

		return codeInfo;
	}

	private String integerToString(Integer integer) {
		String result = null;
		if (integer < 10) {
			result = "0" + integer.toString();
		} else {
			result = integer.toString();
		}

		return result;
	}

	private long caculateBetNums(int redSize, int blueSize, int redNum,
			int blueNum) {
		long betNums = PublicMethod.zuhe(redNum, redSize)
				* PublicMethod.zuhe(blueNum, blueSize);
		return betNums;
	}

	private long caculateBetNums(int redSize, int redNum) {
		long betNums = PublicMethod.zuhe(redNum, redSize);
		return betNums;
	}

	/**
	 * 子列表中相应的数据
	 */
	protected static List<JSONObject> getSubInfoForListView(String lotno) {

		if (lotno == null || lotno.equals("")) {
			return null;
		}
		List<JSONObject> _list = new ArrayList<JSONObject>();
		if (lotno.equals(Constants.LOTNO_GD115)) {
			// 广东11-5信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_GD115, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.gd115List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject _ssq = (JSONObject) jsonArrayByLotno.get(i);
						_list.add(_ssq);
						Constants.gd115List.add(_ssq);
					}
				}
			} catch (Exception e) {
				// 获取双色球数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.gd115List.add(tempObj);
				}
			}

		} else if (lotno.equals(Constants.LOTNO_ten)) {
			// 广东快乐十分
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_ten, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.gd10List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject _ssq = (JSONObject) jsonArrayByLotno.get(i);
						_list.add(_ssq);
						Constants.gd10List.add(_ssq);
					}
				}
			} catch (Exception e) {
				// 获取双色球数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "0000000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.gd10List.add(tempObj);
				}
			}

		} else if (lotno.equals(Constants.LOTNO_SSQ)) {
			// 双色球信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_SSQ, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.ssqNoticeList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject _ssq = (JSONObject) jsonArrayByLotno.get(i);
						_list.add(_ssq);
						Constants.ssqNoticeList.add(_ssq);
					}
				}
			} catch (Exception e) {
				// 获取双色球数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.ssqNoticeList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_DLT)) {
			// 大乐透信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_DLT, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.dltList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.dltList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取大乐透数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.dltList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_FC3D)) {
			// 福彩3D信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_FC3D, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.fc3dList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.fc3dList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取福彩3D数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
							tempObj.put("tryCode", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.fc3dList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_PL3)) {
			// 排列3信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_PL3, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.pl3List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.pl3List.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取排列3数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.pl3List.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_PL5)) {
			// 排列5信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_PL5, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.pl5List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.pl5List.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取排列5数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.pl5List.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_QXC)) {
			// 七星彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_QXC, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.qxcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.qxcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取七星彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.qxcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_QLC)) {
			// 七乐彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_QLC, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.qlcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.qlcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取七乐彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.qlcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_SSC)) {
			// 时时彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_SSC, "100");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.sscList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.sscList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取时时彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.sscList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_SFC)) {
			// 足彩胜负彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_SFC, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.sfcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.sfcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取足彩胜负彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.sfcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_RX9)) {
			// 足彩任选9信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_RX9, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.rx9List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.rx9List.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取足彩任选9数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.rx9List.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_LCB)) {
			// 足彩六场半信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_LCB, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.half6List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.half6List.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取足彩六场半数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.half6List.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_JQC)) {
			// 进球彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_JQC, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.jqcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.jqcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取进球彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.jqcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_11_5)) {
			// 11_5信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_11_5, "100");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.dlcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.dlcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取11_5数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.dlcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_eleven)) {
			// 11运夺金信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_eleven, "100");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.ydjList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.ydjList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取11运夺金数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.ydjList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_22_5)) {
			// 22_5信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_22_5, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.twentylist.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.twentylist.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取22_5数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.twentylist.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_NMK3)) {
			// 内蒙快三获取信息
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_NMK3, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.nmk3List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject _nmk3 = (JSONObject) jsonArrayByLotno.get(i);
						_list.add(_nmk3);
						Constants.nmk3List.add(_nmk3);
					}
				}
			} catch (Exception e) {
				// 获取双色球数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.nmk3List.add(tempObj);
				}
			}
		}

		return _list;
	}

	private static JSONObject getJSONByLotno(String lotnoString,
			String maxresultString) {
		JSONObject jsonObjectByLotno = PrizeInfoInterface.getInstance()
				.getNoticePrizeInfo(lotnoString, "1", maxresultString);
		return jsonObjectByLotno;
	}

	/**
	 * 将获取到的开奖信息放到常量类中
	 */
	protected void onStart() {
		super.onStart();
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
	}

	protected void onStop() {
		super.onStop();
	}
}