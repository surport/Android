package com.ruyicai.activity.notice;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;
import android.os.Bundle;
import android.view.View;

/**
 * 红球走势图
 * 
 * @author Administrator
 * 
 */
public class NoticeRedBallActivity extends NoticeBallActivity implements
		MessageListener {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		noticeAllNet(true);
		MobclickAgent.onEvent(NoticeRedBallActivity.this, "kaijiangzoushi"); // BY贺思明
																				// 最新开奖页点击“开奖走势”tab切换。
	}

	@Override
	public void onMessageListener() {
		layout.removeAllViews();
//		selectlayout.removeAllViews();
		ballBlueView = null;
		ballRedView = null;
		addBallViewagain(true);
	}

	/**
	 * 开奖公告里主列表与子列表之间的跳转
	 * 
	 * @param listViewID
	 *            列表ID
	 */
	public void addBallViewagain(boolean isRed) {
		List<JSONObject> list = getballlist();

		Collections.reverse(list);
		switch (NoticeActivityGroup.LOTNO) {
		// 广东11-5
		case NoticeActivityGroup.ID_SUB_GD115_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballRedView.initNoticeBall(list.size(), 11, 1, list, true,
					"gd11-5", 1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			bottomlayout.setVisibility(View.VISIBLE);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		// 广东快乐十分
		case NoticeActivityGroup.ID_SUB_GD10_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
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
			ballRedView.initNoticeBall(list.size(), 33, 1, list, true, "ssq",
					1 * NoticeMainActivity.SCALE);
			ballBlueView.initNoticeBall(list.size(), 16, 1, list, false, "ssq",
					1 * NoticeMainActivity.SCALE);
			bottomlayout.setVisibility(View.VISIBLE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);
			
//			/** add by pengcx 20130808 start */
//			/** 创建红球和篮球选号面板 */
//			ballSelectedRedView = new NoticeBallView(this);
//			ballSelectedBlueView = new NoticeBallView(this);
//			ballSelectedRedView.initNoticeBall(3, 33, 1, null, true, "ssq",
//					1 * NoticeMainActivity.SCALE);
//			ballSelectedBlueView.initNoticeBall(3, 16, 1, null, false, "ssq",
//					1 * NoticeMainActivity.SCALE);
//			ballSelectedRedView.setTextCode(textRedCodeOne);
//			ballSelectedRedView.setTextCodeTow(textRedCodeTwo);
//			ballSelectedBlueView.setTextCode(textBlueCodeOne);
//			ballSelectedBlueView.setTextCodeTow(textBlueCodeTwo);
//			selectlayout.addView(ballSelectedRedView);
//			selectlayout.addView(ballSelectedBlueView);
//			/** add by pengcx 20130808 end */
			
			break;
		case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "fc3d",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			ballRedView.initNoticeBall(list.size(), 30, 1, list, true, "qlc",
					1 * NoticeMainActivity.SCALE);
			ballBlueView.initNoticeBall(list.size(), 30, 1, list, false, "qlc",
					1 * NoticeMainActivity.SCALE);
			bottomlayout.setVisibility(View.VISIBLE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);
			
//			/** add by pengcx 20130808 start */
//			/** 创建红球和篮球选号面板 */
//			ballSelectedRedView = new NoticeBallView(this);
//			ballSelectedBlueView = new NoticeBallView(this);
//			ballSelectedRedView.initNoticeBall(3, 30, 1, null, true, "qlc",
//					1 * NoticeMainActivity.SCALE);
//			ballSelectedBlueView.initNoticeBall(3, 30, 1, null, false, "qlc",
//					1 * NoticeMainActivity.SCALE);
//			ballSelectedRedView.setTextCode(textRedCodeOne);
//			ballSelectedRedView.setTextCodeTow(textRedCodeTwo);
//			ballSelectedBlueView.setTextCode(textBlueCodeOne);
//			ballSelectedBlueView.setTextCodeTow(textBlueCodeTwo);
//			selectlayout.addView(ballSelectedRedView);
//			selectlayout.addView(ballSelectedBlueView);
//			/** add by pengcx 20130808 end */
			break;
		case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
			// zlm 排列三
			ballRedView = new NoticeBallView(this);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "pl3",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_PL5_LISTVIEW:
			// zlm 排列五
			ballRedView = new NoticeBallView(this);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "pl5",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_QXC_LISTVIEW:
			// zlm 七星彩
			ballRedView = new NoticeBallView(this);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "qxc",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			// zlm 超级大乐透
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			ballRedView.initNoticeBall(list.size(), 35, 1, list, true, "cjdlt",
					1 * NoticeMainActivity.SCALE);
			ballBlueView.initNoticeBall(list.size(), 12, 1, list, false,
					"cjdlt", 1 * NoticeMainActivity.SCALE);
			bottomlayout.setVisibility(View.VISIBLE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);
			
//			/** add by pengcx 20130812 start */
//			/** 创建红球和篮球选号面板 */
//			ballSelectedRedView = new NoticeBallView(this);
//			ballSelectedBlueView = new NoticeBallView(this);
//			ballSelectedRedView.initNoticeBall(3, 35, 1, null, true, "cjdlt",
//					1 * NoticeMainActivity.SCALE);
//			ballSelectedBlueView.initNoticeBall(3, 12, 1, null, false, "cjdlt",
//					1 * NoticeMainActivity.SCALE);
//			ballSelectedRedView.setTextCode(textRedCodeOne);
//			ballSelectedRedView.setTextCodeTow(textRedCodeTwo);
//			ballSelectedBlueView.setTextCode(textBlueCodeOne);
//			ballSelectedBlueView.setTextCodeTow(textBlueCodeTwo);
//			selectlayout.addView(ballSelectedRedView);
//			selectlayout.addView(ballSelectedBlueView);
//			/** add by pengcx 20130812 end */
			break;
		case NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW:
			// zlm 时时彩
			ballRedView = new NoticeBallView(this);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "ssc",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_DLC_LISTVIEW:
			// zlm 11-5彩
			ballRedView = new NoticeBallView(this);
			ballRedView.initNoticeBall(list.size(), 11, 1, list, isRed, "11-5",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			bottomlayout.setVisibility(View.VISIBLE);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_YDJ_LISTVIEW:
			// zlm 11-5彩
			ballRedView = new NoticeBallView(this);
			ballRedView.initNoticeBall(list.size(), 11, 1, list, isRed,
					"11-ydj", 1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballRedView.initNoticeBall(list.size(), 22, 1, list, isRed, "22-5",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_NMK3_LISTVIEW:
			// 内蒙快三
			ballRedView = new NoticeBallView(this);
			ballRedView.initNoticeBall(list.size(), 6, 1, list, isRed, "nmk3",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_CQ11X5_LISTVIEW:
			// 重庆11选5
			ballRedView = new NoticeBallView(this);
			ballRedView.initNoticeBall(list.size(), 11, 1, list, isRed,
					"cq-11-5", 1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		}
	}

}
