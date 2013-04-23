/**
 * 
 */
package com.ruyicai.activity.notice;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.ssq.SsqJiXuan;
import com.ruyicai.activity.buy.ssq.SsqZiDanTuo;
import com.ruyicai.activity.buy.ssq.SsqZiZhiXuan;
import com.ruyicai.util.Constants;

import android.app.Activity;
import android.os.Bundle;

/**
 * 开奖子列表
 * @author Administrator
 *
 */
public class NoticeActivityGroup extends BuyActivityGroup{

	public static int LOTNO=2;
	public final static int ID_SUB_SHUANGSEQIU_LISTVIEW = 2;
	public final static int ID_SUB_FUCAI3D_LISTVIEW = 3;
	public final static int ID_SUB_QILECAI_LISTVIEW = 4;
	public final static int ID_SUB_PAILIESAN_LISTVIEW = 5; // zlm 8.9 代码添加：排列三
	public final static int ID_SUB_DLC_LISTVIEW = 6; // zlm 8.9
	public final static int ID_SUB_SHISHICAI_LISTVIEW = 7; // zlm 8.9
	public final static int ID_SUB_SFC_LISTVIEW = 8; // zlm 8.9
	public final static int ID_SUB_RXJ_LISTVIEW = 9; // zlm 8.9
	public final static int ID_SUB_LCB_LISTVIEW = 10; // zlm 8.9
	public final static int ID_SUB_JQC_LISTVIEW = 11; // zlm 8.9
	public final static int ID_SUB_DLT_LISTVIEW = 12; // zlm 8.9
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView(LOTNO);
        isIssue(false);
	}
	/**
	 * 开奖公告里主列表与子列表之间的跳转
	 * @param listViewID
	 *            列表ID
	 */
	private void initView(int listViewID) {
		switch (listViewID) {
		case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
			String[] topTitles1 ={"双色球开奖公告","双色球开奖公告","双色球开奖公告"};
			String[] titles1 ={"红球走势","蓝球走势","开奖号码"};
			Class[] allId1 ={NoticeRedBallActivity.class,NoticeBlueBallActivity.class,NoticeInfoActivity.class};
			init(titles1, topTitles1, allId1);
			break;
		case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
			String[] topTitles2 ={"福彩3D开奖公告","福彩3D开奖公告"};
			String[] titles2 ={"开奖走势","开奖号码"};
			Class[] allId2 ={NoticeRedBallActivity.class,NoticeInfoActivity.class};
			init(titles2, topTitles2, allId2);
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			String[] topTitles3 ={"七乐彩开奖公告","七乐彩开奖公告","七乐彩开奖公告"};
			String[] titles3 ={"红球走势","蓝球走势","开奖号码"};
			Class[] allId3 ={NoticeRedBallActivity.class,NoticeBlueBallActivity.class,NoticeInfoActivity.class};
			init(titles3, topTitles3, allId3);
			break;
		case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
			String[] topTitles4 ={"排列三开奖公告","排列三开奖公告"};
			String[] titles4 ={"开奖走势","开奖号码"};
			Class[] allId4 ={NoticeRedBallActivity.class,NoticeInfoActivity.class};
			init(titles4, topTitles4, allId4);
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			String[] topTitles5 ={"大乐透开奖公告","大乐透开奖公告","大乐透开奖公告"};
			String[] titles5 ={"红球走势","蓝球走势","开奖号码"};
			Class[] allId5 ={NoticeRedBallActivity.class,NoticeBlueBallActivity.class,NoticeInfoActivity.class};
			init(titles5, topTitles5, allId5);
			break;
		case NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW:
			String[] topTitles6 ={"时时彩开奖公告"};
			String[] titles6 ={"开奖号码"};
			Class[] allId6 ={NoticeInfoActivity.class};
			init(titles6, topTitles6, allId6);
			break;
		case NoticeActivityGroup.ID_SUB_DLC_LISTVIEW:
			String[] topTitles7 ={"11选5开奖公告","11选5开奖公告"};
			String[] titles7 ={"开奖分布","开奖号码"};
			Class[] allId7 ={NoticeRedBallActivity.class,NoticeInfoActivity.class};
			init(titles7, topTitles7, allId7);
			break;
		case NoticeActivityGroup.ID_SUB_SFC_LISTVIEW:
			String[] topTitles8 ={"胜负彩开奖公告"};
			String[] titles8 ={"开奖号码"};
			Class[] allId8 ={NoticeInfoActivity.class};
			init(titles8, topTitles8, allId8);
			break;
		case NoticeActivityGroup.ID_SUB_RXJ_LISTVIEW:
			String[] topTitles9 ={"任选九开奖公告"};
			String[] titles9 ={"开奖号码"};
			Class[] allId9 ={NoticeInfoActivity.class};
			init(titles9, topTitles9, allId9);
			break;
		case NoticeActivityGroup.ID_SUB_LCB_LISTVIEW:
			String[] topTitles10 ={"六场半开奖公告"};
			String[] titles10 ={"开奖号码"};
			Class[] allId10 ={NoticeInfoActivity.class};
			init(titles10, topTitles10, allId10);
			break;
		case NoticeActivityGroup.ID_SUB_JQC_LISTVIEW:
			String[] topTitles11 ={"进球彩开奖公告"};
			String[] titles11 ={"开奖号码"};
			Class[] allId11 ={NoticeInfoActivity.class};
			init(titles11, topTitles11, allId11);
			break;
		}
	}
}
