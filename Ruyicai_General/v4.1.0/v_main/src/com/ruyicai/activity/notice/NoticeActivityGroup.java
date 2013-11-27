package com.ruyicai.activity.notice;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.palmdream.RuyicaiAndroid.wxapi.WXEntryActivity;
import com.ruyicai.activity.notice.LotnoDetail.LotnoDetailView;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.tencent.weibo.oauthv1.OAuthV1;
import com.tencent.weibo.oauthv1.OAuthV1Client;
import com.tencent.weibo.webview.OAuthV1AuthorizeWebView;
import com.third.share.ShareActivity;
import com.third.share.Token;
import com.third.share.Weibo;
import com.third.share.WeiboDialogListener;
import com.third.tencent.TencentShareActivity;
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
	public final static int ID_SUB_DLC_LISTVIEW = 6; // zlm 8.9 多乐彩
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
	public final static int ID_SUB_CQ11X5_LISTVIEW = 20;// 重庆11选5
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
	private Button notice_share;
	private OAuthV1 tenoAuth;
	private PopupWindow popupWindow;
	private RWSharedPreferences RW;
	private LinearLayout parent;
	private Button  tosinaweibo, totengxunweibo,toweixin,topeingyouquan, tocancel;
    String token, expires_in;
	
	String tencent_token;
	String tencent_access_token_secret;
	private RWSharedPreferences shellRW;
	private boolean isSinaTiaoZhuan = true;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notice_main_group);
		context = this;
		mTabHost = (TabHost) findViewById(R.id.tab_host);
		tabWidget = mTabHost.getTabWidget();
		mTabHost.setup(getLocalActivityManager());
		mInflater = LayoutInflater.from(this);
		
		initSharePopWindow();
		RW=new RWSharedPreferences(context, "shareweixin");
		parent = (LinearLayout)findViewById(R.id.notice_linear);
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
						if(tabId.equals(PRIZE)){
							notice_share.setVisibility(View.VISIBLE);
						}else{
							notice_share.setVisibility(View.GONE);
						}
						return;
					}
				}

			}
		});
		initView();
		setScale();
		initView(LOTNO);
		shellRW = new RWSharedPreferences(context, "addInfo");
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
		} else if (screenWith > 480 && screenWith < 1080) {
			NoticeMainActivity.BALL_WIDTH = screenWith / 480 * 46;
			NoticeMainActivity.SCALE = (float) 1.5;
		} else if (screenWith >= 1080) { //add by yejc 20130909
			NoticeMainActivity.BALL_WIDTH = screenWith / 480 * 46;
			NoticeMainActivity.SCALE = 2.0f;
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
		// 升序降序按钮
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
		
		
		notice_share=(Button) findViewById(R.id.notice_share);
		notice_share.setVisibility(View.VISIBLE);
		tenoAuth = new OAuthV1("null");
		tenoAuth.setOauthConsumerKey(Constants.kAppKey);
		tenoAuth.setOauthConsumerSecret(Constants.kAppSecret);
	    notice_share.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (popupWindow != null) {
				popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			}
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
			String[] topTitles7 = { "江西11选5开奖公告", "江西11选5开奖公告", "江西11选5开奖公告" };
			String[] titles7 = { "开奖分布", "前三走势", "开奖号码" };
			Class[] allId7 = { NoticeRedBallActivity.class,
					BeforThreeNoticeBallActivity.class,
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
			String[] topTitlesGD115 = { "广东11选5开奖公告", "广东11选5开奖公告",
					"广东11选5开奖公告" };
			String[] titlesGD115 = { "开奖分布", "前三走势", "开奖号码" };
			Class[] allIdGD115 = { NoticeRedBallActivity.class,
					BeforThreeNoticeBallActivity.class,
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
			
		case NoticeActivityGroup.ID_SUB_CQ11X5_LISTVIEW:
			String[] topTitlesCQ11X5 = { "开奖走势", "开奖号码" };
			String[] titlesCQ11X5 = { "开奖走势", "开奖号码" };
			Class[] allIdCQ11X5 = { NoticeRedBallActivity.class,
					NoticeInfoActivity.class };
			init(topTitlesCQ11X5, titlesCQ11X5, allIdCQ11X5);
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
	
	private void initSharePopWindow() {
		View contentView=NoticeActivityGroup.this.getLayoutInflater().inflate(R.layout.share_popwindow, null);
		tosinaweibo=(Button) contentView.findViewById(R.id.tosinaweibo);
		totengxunweibo=(Button) contentView.findViewById(R.id.totengxunweibo);
		toweixin=(Button) contentView.findViewById(R.id.toweixin);
		topeingyouquan=(Button) contentView.findViewById(R.id.topengyouquan);
		tocancel=(Button) contentView.findViewById(R.id.tocancel);
		
		
   	    popupWindow=new PopupWindow(contentView, ViewGroup.LayoutParams.FILL_PARENT,   //得到pop对象,并设置该pop的样子和宽高
   			ViewGroup.LayoutParams.WRAP_CONTENT);
   	    popupWindow.setFocusable(true);
   	    popupWindow.setBackgroundDrawable(new BitmapDrawable());//当点击空白处时，pop会关掉
   	    //popupWindow.setAnimationStyle(R.style.share_animation);//通过此方法从styles.xml中得到pop的进入和退出效果	
   	   
   	    tosinaweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				oauthOrShare();
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				
			}
		});
		totengxunweibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tenoauth();
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		toweixin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toWeiXin();
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		topeingyouquan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toPengYouQuan();
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
		tocancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}
		});
	}
	
	protected void tenoauth() {
		tencent_token = shellRW.getStringValue("tencent_token");
		tencent_access_token_secret = shellRW
				.getStringValue("tencent_access_token_secret");
		if (tencent_token.equals("") && tencent_access_token_secret.equals("")) {
			try {
				tenoAuth = OAuthV1Client.requestToken(tenoAuth);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Intent intent = new Intent(context, OAuthV1AuthorizeWebView.class);// 创建Intent，使用WebView让用户授权
			intent.putExtra("oauth", tenoAuth);
			NoticeActivityGroup.this.startActivityForResult(intent, 1);
		} else {
			tenoAuth.setOauthToken(tencent_token);
			tenoAuth.setOauthTokenSecret(tencent_access_token_secret);
			Intent intent = new Intent(NoticeActivityGroup.this, TencentShareActivity.class);
			intent.putExtra("tencent", ((NewNoticeInfoActivity)getCurrentActivity()).lotnoDetailView.getShareString());
			intent.putExtra("oauth", tenoAuth);
			NoticeActivityGroup.this.startActivity(intent);
		}
	}
	
	

	protected void toPengYouQuan() {
		RW.putStringValue("weixin_pengyou", "topengyouquan");
		Intent intent = new Intent(NoticeActivityGroup.this,
				WXEntryActivity.class);
		;
		intent.putExtra("sharecontent",((NewNoticeInfoActivity)getCurrentActivity()).lotnoDetailView.getShareString());
		NoticeActivityGroup.this.startActivity(intent);
		
	}
   
	protected void toWeiXin() {
		RW.putStringValue("weixin_pengyou", "toweixin");
		
		Intent intent = new Intent(NoticeActivityGroup.this,
				WXEntryActivity.class);
		intent.putExtra("sharecontent",((NewNoticeInfoActivity)getCurrentActivity()).lotnoDetailView.getShareString());
		NoticeActivityGroup.this.startActivity(intent);	
	}
	
	/**
	 * 分享
	 * **/
	private void oauthOrShare() {
		token = shellRW.getStringValue("token");
		expires_in = shellRW.getStringValue("expires_in");
		if (token.equals("")) {
			oauth();
		} else {
			isSinaTiaoZhuan = true;
			initAccessToken(token, expires_in);
		}
	}
	
	private void oauth() {

		Weibo weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);
		// Oauth2.0
		// 隐式授权认证方式
		weibo.setRedirectUrl(Constants.CONSUMER_URL);// 此处回调页内容应该替换为与appkey对应的应用回调页
		// 对应的应用回调页可在开发者登陆新浪微博开发平台之后，
		// 进入我的应用--应用详情--应用信息--高级信息--授权设置--应用回调页进行设置和查看，
		// 应用回调页不可为空
		weibo.authorize(NoticeActivityGroup.this, new AuthDialogListener());
	}

	
	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			PublicMethod.myOutLog("token111",
					"zhiqiande" + shellRW.getStringValue("token"));
			PublicMethod.myOutLog("onComplete", "12131321321321");
			String token = values.getString("access_token");
			PublicMethod.myOutLog("token", token);
			String expires_in = values.getString("expires_in");
			shellRW.putStringValue("token", token);
			shellRW.putStringValue("expires_in", expires_in);
			initAccessToken(token, expires_in);
		}
		@Override
		public void onCancel() {
			Toast.makeText(context.getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}
}
	
	private void initAccessToken(String token, String expires_in) {
		Token accessToken = new Token(token, Weibo.getAppSecret());
		accessToken.setExpiresIn(expires_in);
		Weibo.getInstance().setAccessToken(accessToken);
		share2weibo(((NewNoticeInfoActivity)getCurrentActivity()).lotnoDetailView.getShareString());
		if (isSinaTiaoZhuan) {
			Intent intent = new Intent();
			intent.setClass(context, ShareActivity.class);
			context.startActivity(intent);
		}
	}
	private void share2weibo(String content) {
		Weibo weibo = Weibo.getInstance();
		weibo.share2weibo(NoticeActivityGroup.this, weibo.getAccessToken().getToken(), weibo
				.getAccessToken().getSecret(), content, "");
	}
     
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == OAuthV1AuthorizeWebView.RESULT_CODE) {
				// 从返回的Intent中获取验证码
				tenoAuth = (OAuthV1) data.getExtras().getSerializable("oauth");
				try {
					tenoAuth = OAuthV1Client.accessToken(tenoAuth);
					/*
					 * 注意：此时oauth中的Oauth_token和Oauth_token_secret将发生变化，用新获取到的
					 * 已授权的access_token和access_token_secret替换之前存储的未授权的request_token
					 * 和request_token_secret.
					 */
					tencent_token = tenoAuth.getOauthToken();
					tencent_access_token_secret = tenoAuth
							.getOauthTokenSecret();
					shellRW.putStringValue("tencent_token", tencent_token);
					shellRW.putStringValue("tencent_access_token_secret",
							tencent_access_token_secret);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Intent intent = new Intent(NoticeActivityGroup.this,
						TencentShareActivity.class);
				intent.putExtra("tencent", ((NewNoticeInfoActivity)getCurrentActivity()).lotnoDetailView.getShareString());
				intent.putExtra("oauth", tenoAuth);
				startActivity(intent);
			}
		}
	}
}
