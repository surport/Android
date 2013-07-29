package com.ruyicai.activity.notice;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.util.PublicMethod;
import com.umeng.analytics.MobclickAgent;

/**
 * 开奖子列表
 * 
 * @author Administrator
 * 
 */
public class NoticeActivityGroup extends ActivityGroup {

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
	public final static int ID_SUB_NMK3_LISTVIEW = 19;// 内蒙快三
	public final static String PRIZE = "最新开奖";
	public final static String PRIZE_INFO = "最新开奖详情";
	public final static int SIZE = 17;

	protected TabHost mTabHost = null;
	protected LayoutInflater mInflater = null;
	protected String[] titles;
	protected String[] topTitles;
	protected Class[] allId;
	protected TabHost.TabSpec firstSpec = null;
	public TextView title;// 标题
	protected TextView issue;// 期号
	protected TextView time;// 截止时间
	protected Button imgIcon;// 返回购彩大厅按钮
	protected TextView topTitle;
	protected Context context;
	TabWidget tabWidget = null;
	Handler hanler = new Handler();
	Long upordown = 0L;
	boolean isPosition;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notice_main_group);
		context = this;
		mTabHost = (TabHost) findViewById(R.id.tab_host);
		tabWidget = mTabHost.getTabWidget();
		mTabHost.setup(getLocalActivityManager());
		mInflater = LayoutInflater.from(this);
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				for (int i = 0; i < titles.length; i++) {
					if (tabId.equals(titles[i])) {
						title.setText(topTitles[i]);
						if (tabId.equals("开奖走势") || tabId.equals("开奖分布")) {
							imgIcon.setVisibility(View.VISIBLE);
						} else {
							imgIcon.setVisibility(View.GONE);
						}
						return;
					}
				}

			}
		});
		initView();
		setScale();
		initView(LOTNO);

	}

	protected void onResume() {
		super.onResume();
		getInfo();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

	public void getInfo() {
		Intent intent = getIntent();
		isPosition = intent.getBooleanExtra("isPosition", true);
		if (isPosition) {
			int position = intent.getIntExtra("position", 0);
			setTab(position);
		}
	}

	/**
	 * 设置当前页
	 * 
	 * @param index
	 */
	public void setTab(int index) {
		mTabHost.setCurrentTab(index);
		try {
			title.setText(topTitles[index]);
		} catch (Exception e) {

		}

	}

	/**
	 * 根据手机屏幕设置球的大小和球的缩放比例
	 */
	public void setScale() {
		int screenWith = PublicMethod.getDisplayWidth(this);
		if (screenWith <= 240) {
			NoticeMainActivity.BALL_WIDTH = 46 * 120 / 240;
			NoticeMainActivity.SCALE = (float) 140 / 240;
		} else if (screenWith > 240 && screenWith <= 320) {
			NoticeMainActivity.BALL_WIDTH = 46 * 160 / 240;
			NoticeMainActivity.SCALE = (float) 180 / 240;
		} else if (screenWith == 480) {
			NoticeMainActivity.BALL_WIDTH = 46;
			NoticeMainActivity.SCALE = 1;
		} else if (screenWith > 480) {
			NoticeMainActivity.BALL_WIDTH = screenWith / 480
					* NoticeMainActivity.BALL_WIDTH;
			NoticeMainActivity.SCALE = (float) 1.5;
		}
	}

	/**
	 * 初始化组件
	 */
	public void initView() {
		final String[] buttontext = { "降序", "升序" };
		final int[] imageid = { R.drawable.notice_down, R.drawable.notice_up };
		// 标题文本
		title = (TextView) findViewById(R.id.layout_main_text_title);
		//升序降序按钮
		imgIcon = (Button) findViewById(R.id.layout_main_img_return);
		imgIcon.setText(buttontext[0]);
		imgIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMessgeToCurrentActivty();
				upordown++;
				imgIcon.setText(buttontext[upordown.intValue() % 2]);
				imgIcon.setBackgroundResource(imageid[upordown.intValue() % 2]);
			}
		});
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
			Class[] allId1 = { NewNoticeInfoActivity.class,
					NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titles1, topTitles1, allId1, SIZE);

			break;
		case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
			String[] topTitles2 = { PRIZE_INFO, "福彩3D开奖公告", "福彩3D开奖公告" };
			String[] titles2 = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allId2 = { NewNoticeInfoActivity.class,
					NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titles2, topTitles2, allId2);
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			String[] topTitles3 = { PRIZE_INFO, "七乐彩开奖公告", "七乐彩开奖公告" };
			String[] titles3 = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allId3 = { NewNoticeInfoActivity.class,
					NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titles3, topTitles3, allId3, SIZE);
			break;
		case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
			String[] topTitles4 = { PRIZE_INFO, "排列三开奖公告", "排列三开奖公告" };
			String[] titles4 = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allId4 = { NewNoticeInfoActivity.class,
					NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titles4, topTitles4, allId4);
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			String[] topTitles5 = { PRIZE_INFO, "大乐透开奖公告", "大乐透开奖公告" };
			String[] titles5 = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allId5 = { NewNoticeInfoActivity.class,
					NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titles5, topTitles5, allId5, SIZE);
			break;
		case NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW:
			String[] topTitles6 = { "时时彩开奖公告", "时时彩开奖公告" };
			String[] titles6 = { "开奖走势", "开奖号码" };
			Class[] allId6 = { NoticeRedBallActivity.class,
					NoticeInfoActivity.class };
			init(titles6, topTitles6, allId6, SIZE);
			break;
		case NoticeActivityGroup.ID_SUB_DLC_LISTVIEW:
			String[] topTitles7 = { "江西11选5开奖公告", "江西11选5开奖公告" };
			String[] titles7 = { "开奖分布", "开奖号码" };
			Class[] allId7 = { NoticeRedBallActivity.class,
					NoticeInfoActivity.class };
			init(titles7, topTitles7, allId7);
			break;
		case NoticeActivityGroup.ID_SUB_YDJ_LISTVIEW:
			String[] topTitles15 = { "11运夺金开奖公告", "11运夺金开奖公告" };
			String[] titles15 = { "开奖分布", "开奖号码" };
			Class[] allId15 = { NoticeRedBallActivity.class,
					NoticeInfoActivity.class };
			init(titles15, topTitles15, allId15);
			break;
		case NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW:
			String[] topTitles16 = { PRIZE_INFO, "22选5开奖分布开奖公告", "22选5开奖公告" };
			String[] titles16 = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allId16 = { NewNoticeInfoActivity.class,
					NoticeRedBallActivity.class, NoticeInfoActivity.class };
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
			Class[] allIdpl5 = { NewNoticeInfoActivity.class,
					NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titlespl5, topTitlespl5, allIdpl5);
			break;
		case NoticeActivityGroup.ID_SUB_QXC_LISTVIEW:
			String[] topTitlesqxc = { PRIZE_INFO, "七星彩开奖公告", "七星彩开奖公告" };
			String[] titlesqxc = { PRIZE, "开奖走势", "开奖号码" };
			Class[] allIdqxc = { NewNoticeInfoActivity.class,
					NoticeRedBallActivity.class, NoticeInfoActivity.class };
			init(titlesqxc, topTitlesqxc, allIdqxc);
			break;
		case NoticeActivityGroup.ID_SUB_GD115_LISTVIEW:
			String[] topTitlesGD115 = { "广东11选5开奖公告", "广东11选5开奖公告" };
			String[] titlesGD115 = { "开奖分布", "开奖号码" };
			Class[] allIdGD115 = { NoticeRedBallActivity.class,
					NoticeInfoActivity.class };
			init(titlesGD115, topTitlesGD115, allIdGD115);
			break;
		case NoticeActivityGroup.ID_SUB_GD10_LISTVIEW:
			String[] topTitlesGD10 = { "广东快乐十分开奖公告", "广东快乐十分开奖公告" };
			String[] titlesGD10 = { "开奖走势", "开奖号码" };
			Class[] allIdGD10 = { NoticeRedBallActivity.class,
					NoticeInfoActivity.class };
			init(titlesGD10, topTitlesGD10, allIdGD10);
			break;
		case NoticeActivityGroup.ID_SUB_NMK3_LISTVIEW:
			String[] topTitlesNMK3 = { "快三开奖公告", "快三开奖公告" };
			String[] titlesNMK3 = { "开奖走势", "开奖号码" };
			Class[] allIdNMK3 = { NoticeRedBallActivity.class,
					NoticeInfoActivity.class };
			init(titlesNMK3, topTitlesNMK3, allIdNMK3);
			break;
		}
	}

	/**
	 * 初始化开奖列表
	 * 
	 * @param titles
	 *            彩种列表名 e.g. {"大乐透开奖公告","大乐透开奖公告","大乐透开奖公告"};
	 * @param topTitles
	 *            彩种组列表名 e.g. {"红球走势","蓝球走势","开奖号码"};
	 * @param allId
	 *            类ID e.g.{NoticeRedBallActivity.class,NoticeBlueBallActivity.
	 *            class,NoticeInfoActivity.class}
	 */
	public void init(String titles[], String topTitles[], Class allId[]) {
		this.titles = titles;
		this.topTitles = topTitles;
		this.allId = allId;
		for (int i = 0; i < titles.length; i++) {
			addTab(i);
		}
	}

	/**
	 * 添加标签
	 * 
	 * @param index
	 * @param id
	 */
	public void addTab(int index) {
		View indicatorTab = mInflater.inflate(R.layout.layout_nav_item, null);
		ImageView img = (ImageView) indicatorTab
				.findViewById(R.id.layout_nav_item);
		topTitle = (TextView) indicatorTab
				.findViewById(R.id.layout_nav_icon_title);
		img.setBackgroundResource(R.drawable.tab_buy_selector);
		topTitle.setText(titles[index]);
		Intent intent = new Intent(NoticeActivityGroup.this, allId[index]);
		intent.putExtra("index", index);
		firstSpec = mTabHost.newTabSpec(titles[index])
				.setIndicator(indicatorTab).setContent(intent);
		mTabHost.addTab(firstSpec);
	}

	/**
	 * 初始化开奖列表
	 * 
	 * @param titles
	 *            彩种列表名 e.g. {"大乐透开奖公告","大乐透开奖公告","大乐透开奖公告"};
	 * @param topTitles
	 *            彩种组列表名 e.g. {"红球走势","蓝球走势","开奖号码"};
	 * @param allId
	 *            类ID e.g.{NoticeRedBallActivity.class,NoticeBlueBallActivity.
	 *            class,NoticeInfoActivity.class}
	 */
	public void init(String titles[], String topTitles[], Class allId[],
			int size) {
		this.titles = titles;
		this.topTitles = topTitles;
		this.allId = allId;
		for (int i = 0; i < titles.length; i++) {
			addTab(i);
			setTextTop(size);
		}
	}

	private void sendMessgeToCurrentActivty() {
		((MessageListener) getCurrentActivity()).onMessageListener();
	}

	/**
	 * 设置标签字体大小
	 */
	public void setTextTop(int size) {
		topTitle.setTextSize(size);
	}

}
