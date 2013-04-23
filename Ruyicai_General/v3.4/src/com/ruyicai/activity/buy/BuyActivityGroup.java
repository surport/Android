/**
 * 
 */
package com.ruyicai.activity.buy;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.more.LuckChoose;
import com.ruyicai.util.PublicMethod;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Administrator
 */
public class BuyActivityGroup extends ActivityGroup {
	protected TabHost mTabHost = null;
	protected LayoutInflater mInflater = null;
	private String[] titles;
	private String[] topTitles;
	private Class[] allId;
	protected TabHost.TabSpec firstSpec = null;
	public TextView title;// 标题
	protected TextView issue;// 期号
	protected TextView time;// 截止时间
	protected Button imgIcon;// 返回购彩大厅按钮
	private String lotNo;// 彩种
	protected RelativeLayout relativeLayout;
	private TextView topTitle;
	private PopupWindow popupwindow;
	protected Context context;
	private BuyGameDialog gameDialog;
	private Handler gameHandler = new Handler();
	TabWidget tabWidget = null;
	Field mBottomLeftStrip;
	Field mBottomRightStrip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_main_group);
		context = this;
		mTabHost = (TabHost) findViewById(R.id.tab_host);
		tabWidget = mTabHost.getTabWidget();
		mTabHost.setup(getLocalActivityManager());
		mInflater = LayoutInflater.from(this);
		initView();
		// 监听tab切换事件
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				for (int i = 0; i < titles.length; i++) {
					if (tabId.equals(titles[i])) {
						title.setText(topTitles[i]);
						return;
					}
				}
			}
		});
	}

	public void setLotno(String lotno) {
		this.lotNo = lotno;
	}

	/**
	 * 初始化数据
	 * 
	 */
	public void initInfo(String[] titles, String[] topTitles, Class[] allId) {
		this.titles = titles;
		this.topTitles = topTitles;
		this.allId = allId;
	}

	/**
	 * 初始化组件
	 */
	public void initView() {
		relativeLayout = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		issue = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);
		imgIcon = (Button) findViewById(R.id.layout_main_img_return);
		imgIcon.setVisibility(View.VISIBLE);
		// ImageView的返回事件
		imgIcon.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createDialog();
			}
		});
	}

	/**
	 * 创建下拉列表
	 */
	private void createDialog() {
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = (LinearLayout) inflate.inflate(
				R.layout.buy_group_window, null);
		popupwindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupwindow.setTouchable(true); // 设置PopupWindow可触摸
		popupwindow.setOutsideTouchable(true);
		popupView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}
		});
		popupwindow.showAsDropDown(imgIcon);
		final LinearLayout layoutGame = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout1);
		final LinearLayout layoutHosity = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout2);
		final LinearLayout layoutLuck = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout3);
		final LinearLayout layoutParentLuck = (LinearLayout) popupView
				.findViewById(R.id.buy_group_one_layout3);
		layoutGame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layoutGame.setBackgroundResource(R.drawable.buy_group_layout_b);
				if(gameDialog == null){
					gameDialog = new BuyGameDialog(context, lotNo,gameHandler);
				}
				gameDialog.showDialog();
				popupwindow.dismiss();
			}
		});
		layoutHosity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layoutHosity.setBackgroundResource(R.drawable.buy_group_layout_b);
				turnHosity();
				popupwindow.dismiss();
			}

		});
		if(getIsLuck()){
			layoutParentLuck.setVisibility(LinearLayout.VISIBLE);
			layoutLuck.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					layoutLuck.setBackgroundResource(R.drawable.buy_group_layout_b);
					Intent intent = new Intent(BuyActivityGroup.this, LuckChoose.class);
					startActivity(intent);
					popupwindow.dismiss();
				}
			});
		}else{
			layoutParentLuck.setVisibility(LinearLayout.GONE);
		}
	}
	public void turnHosity() {
		Intent intent = new Intent(BuyActivityGroup.this,NoticeHistroy.class);
		intent.putExtra("lotno", lotNo);
		startActivity(intent);
	}
	public boolean getIsLuck(){
		return false;
	}

	/**
	 * 是否隐藏期号
	 */
	public void isIssue(boolean isVisble) {
		if (isVisble) {
			relativeLayout.setVisibility(RelativeLayout.INVISIBLE);
		} else {
			relativeLayout.setVisibility(RelativeLayout.GONE);
		}
	}

	/**
	 * 赋值给当前期
	 * 
	 * @param type彩种编号
	 */
	public void setIssue(String type) {
		// 获取期号和截止时间
		JSONObject ssqLotnoInfo = PublicMethod.getCurrentLotnoBatchCode(type);
		if (ssqLotnoInfo != null && ssqLotnoInfo.has(type)) {
			// 成功获取到了期号信息
			try {
				String issueStr = ssqLotnoInfo.getString("batchCode");
				String timeStr = ssqLotnoInfo.getString("endTime");
				issue.setText("第" + issueStr + "期");
				time.setText("截止时间：" + timeStr);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			// 没有获取到期号信息,重新联网获取期号
			PublicMethod.getIssue(type, issue, time, new Handler());
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
		ImageView img = (ImageView) indicatorTab.findViewById(R.id.layout_nav_item);
		topTitle = (TextView) indicatorTab.findViewById(R.id.layout_nav_icon_title);
		img.setBackgroundResource(R.drawable.tab_buy_selector);
		topTitle.setText(titles[index]);
		Intent intent = new Intent(BuyActivityGroup.this, allId[index]);
		intent.putExtra("index", index);
		firstSpec = mTabHost.newTabSpec(titles[index]).setIndicator(indicatorTab).setContent(intent);
		mTabHost.addTab(firstSpec);
	}

	/**
	 * 设置标签字体大小
	 */
	public void setTextTop(int size) {
		topTitle.setTextSize(size);
	}

	/**
	 * 设置当前页
	 * 
	 * @param index
	 */
	public void setTab(int index) {
		mTabHost.setCurrentTab(index);
		title.setText(topTitles[index]);
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

	public void removeTabs() {
		mTabHost.clearAllTabs();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}
}
