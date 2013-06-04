/**
 * 
 */
package com.ruyicai.activity.notice;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid168.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.util.PublicMethod;

/**
 * 开奖子列表
 * 
 * @author Administrator
 * 
 */
public class NoticeActivityGroup extends BuyActivityGroup {

	public static int LOTNO = 2;
	public static String ISSUE;// 当前期
	public final static int ID_SUB_SHUANGSEQIU_LISTVIEW = 2;// 双色球
	public final static int ID_SUB_FUCAI3D_LISTVIEW = 3;// 福彩3D
	public final static int ID_SUB_QILECAI_LISTVIEW = 4;// 七乐彩
	public final static int ID_SUB_PAILIESAN_LISTVIEW = 5; // zlm 8.9 代码添加：排列三
	public final static int ID_SUB_DLC_LISTVIEW = 6; // zlm 8.9 大乐透
	public final static int ID_SUB_SHISHICAI_LISTVIEW = 7; // zlm 8.9 时时彩
	public final static int ID_SUB_SFC_LISTVIEW = 8; // zlm 8.9 胜负彩
	public final static int ID_SUB_RXJ_LISTVIEW = 9; // zlm 8.9 任选九
	public final static int ID_SUB_LCB_LISTVIEW = 10; // zlm 8.9 六场半
	public final static int ID_SUB_JQC_LISTVIEW = 11; // zlm 8.9 进球彩
	public final static int ID_SUB_DLT_LISTVIEW = 12; // zlm 8.9 大乐透
	public final static int ID_SUB_PL5_LISTVIEW = 13; // 排列五
	public final static int ID_SUB_QXC_LISTVIEW = 14;// 七星彩
	public final static int ID_SUB_YDJ_LISTVIEW = 15;// 11运夺金
	public final static int ID_SUB_GD115_LISTVIEW = 17;// 广东11-5
	public final static int ID_SUB_GD10_LISTVIEW = 18;// 广东快乐十分
	public final static int ID_SUB_TWENTY_LISTVIEW = 16;// 22
	public final static String PRIZE = "最新开奖";
	public final static String PRIZE_INFO = "最新开奖详情";
	public final static int SIZE = 17;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setScale();
		isIssue(false);
		initView(LOTNO);
	}
    /**
     * 根据手机屏幕设置球的大小和球的缩放比例
     */
	public void setScale() {
		int screenWith=PublicMethod.getDisplayWidth(this);
		if(screenWith<=240){
			NoticeMainActivity.BALL_WIDTH=46*120/240;
			NoticeMainActivity.SCALE = (float)140/240;
		}else if(screenWith>240&&screenWith<=320){
			NoticeMainActivity.BALL_WIDTH=46*160/240;
			NoticeMainActivity.SCALE = (float)180/240;
		}else if(screenWith==480){
			NoticeMainActivity.BALL_WIDTH=46;
			NoticeMainActivity.SCALE = 1;
		}else if(screenWith>480){
			NoticeMainActivity.BALL_WIDTH=screenWith/480*NoticeMainActivity.BALL_WIDTH;
			NoticeMainActivity.SCALE = (float)1.5;
		}
	}
	/**
	 * 初始化组件
	 */
	public void initView() {
		relativeLayout = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		relativeLayout1=(RelativeLayout) findViewById(R.id.last_batchcode);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		issue = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);
	}

	/**
	 * 开奖公告里主列表与子列表之间的跳转
	 * 
	 * @param listViewID
	 *            列表ID
	 */
	private void initView(int listViewID) {
		switch (listViewID) {
		case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
			String[] topTitles1 = { PRIZE_INFO, "双色球开奖公告", "双色球开奖公告" };
			String[] titles1 = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allId1 = { NewNoticeInfoActivity.class,NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titles1, topTitles1, allId1, SIZE);

			break;
		case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
			String[] topTitles2 = { PRIZE_INFO, "福彩3D开奖公告", "福彩3D开奖公告" };
			String[] titles2 = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allId2 = { NewNoticeInfoActivity.class,NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titles2, topTitles2, allId2);
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			String[] topTitles3 = { PRIZE_INFO, "七乐彩开奖公告", "七乐彩开奖公告" };
			String[] titles3 = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allId3 = { NewNoticeInfoActivity.class,NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titles3, topTitles3, allId3, SIZE);
			break;
		case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
			String[] topTitles4 = { PRIZE_INFO, "排列三开奖公告", "排列三开奖公告" };
			String[] titles4 = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allId4 = { NewNoticeInfoActivity.class,NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titles4, topTitles4, allId4);
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			String[] topTitles5 = { PRIZE_INFO, "大乐透开奖公告", "大乐透开奖公告" };
			String[] titles5 = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allId5 = { NewNoticeInfoActivity.class,NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titles5, topTitles5, allId5, SIZE);
			break;
		case NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW:
			String[] topTitles6 = { "时时彩开奖公告", "时时彩开奖公告" };
			String[] titles6 = { "开奖走势", "开奖号码" };
			Class[] allId6 = { NoticeRedBallActivity.class,NoticeInfoActivity.class };
			init(titles6, topTitles6, allId6, SIZE);
			break;
		case NoticeActivityGroup.ID_SUB_DLC_LISTVIEW:
			String[] topTitles7 = { "江西11选5开奖公告", "江西11选5开奖公告" };
			String[] titles7 = { "开奖分布", "开奖号码" };
			Class[] allId7 = { NoticeRedBallActivity.class,NoticeInfoActivity.class };
			init(titles7, topTitles7, allId7);
			break;
		case NoticeActivityGroup.ID_SUB_YDJ_LISTVIEW:
			String[] topTitles15 = { "11运夺金开奖公告", "11运夺金开奖公告" };
			String[] titles15 = { "开奖分布", "开奖号码" };
			Class[] allId15 = { NoticeRedBallActivity.class,NoticeInfoActivity.class };
			init(titles15, topTitles15, allId15);
			break;
		case NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW:
			String[] topTitles16 = { PRIZE_INFO, "22选5开奖分布开奖公告", "22选5开奖公告" };
			String[] titles16 = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allId16 = { NewNoticeInfoActivity.class,NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titles16, topTitles16, allId16);
			break;
		case NoticeActivityGroup.ID_SUB_SFC_LISTVIEW:
			String[] topTitles8 = { "胜负彩开奖公告" };
			String[] titles8 = { "开奖号码" };
			Class[] allId8 = { NoticeInfoActivity.class };
			init(titles8, topTitles8, allId8);
			break;
		case NoticeActivityGroup.ID_SUB_RXJ_LISTVIEW:
			String[] topTitles9 = { "任选九开奖公告" };
			String[] titles9 = { "开奖号码" };
			Class[] allId9 = { NoticeInfoActivity.class };
			init(titles9, topTitles9, allId9);
			break;
		case NoticeActivityGroup.ID_SUB_LCB_LISTVIEW:
			String[] topTitles10 = { "六场半开奖公告" };
			String[] titles10 = { "开奖号码" };
			Class[] allId10 = { NoticeInfoActivity.class };
			init(titles10, topTitles10, allId10);
			break;
		case NoticeActivityGroup.ID_SUB_JQC_LISTVIEW:
			String[] topTitles11 = { "进球彩开奖公告" };
			String[] titles11 = { "开奖号码" };
			Class[] allId11 = { NoticeInfoActivity.class };
			init(titles11, topTitles11, allId11);
			break;
		case NoticeActivityGroup.ID_SUB_PL5_LISTVIEW:
			String[] topTitlespl5 = { PRIZE_INFO, "排列五开奖公告", "排列五开奖公告" };
			String[] titlespl5 = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allIdpl5 = { NewNoticeInfoActivity.class,NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titlespl5, topTitlespl5, allIdpl5);
			break;
		case NoticeActivityGroup.ID_SUB_QXC_LISTVIEW:
			String[] topTitlesqxc = { PRIZE_INFO, "七星彩开奖公告", "七星彩开奖公告" };
			String[] titlesqxc = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allIdqxc = { NewNoticeInfoActivity.class,NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titlesqxc, topTitlesqxc, allIdqxc);
			break;
		case NoticeActivityGroup.ID_SUB_GD115_LISTVIEW:
			String[] topTitlesGD115 = { "广东11选5开奖公告", "广东11选5开奖公告" };
			String[] titlesGD115 = { "开奖分布", "开奖号码" };
			Class[] allIdGD115 = { NoticeRedBallActivity.class,NoticeInfoActivity.class };
			init(titlesGD115, topTitlesGD115, allIdGD115);
			break;
		case NoticeActivityGroup.ID_SUB_GD10_LISTVIEW:
			String[] topTitlesGD10 = { "广东快乐十分开奖公告", "广东快乐十分开奖公告" };
			String[] titlesGD10 = { "开奖走势", "开奖号码" };
			Class[] allIdGD10 = { NoticeRedBallActivity.class,NoticeInfoActivity.class };
			init(titlesGD10, topTitlesGD10, allIdGD10);
			break;
		}
	}
}
