package com.ruyicai.activity.buy;

import java.lang.reflect.Field;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.more.LuckChoose2;
import com.ruyicai.activity.notice.NoticeActivityGroup;
import com.ruyicai.activity.notice.NoticeZCActivity;
import com.ruyicai.activity.usercenter.BetQueryActivity;
import com.ruyicai.activity.usercenter.TrackQueryActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.net.newtransaction.PrizeInfoInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;;

/**
 * @author Administrator
 */
public class BuyActivityGroup extends ActivityGroup {
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
	protected String lotNo;// 彩种
	protected RelativeLayout relativeLayout;
	protected RelativeLayout relativeLayout1;
	protected TextView topTitle;
	private TextView[] titleTextViews;
	protected PopupWindow popupwindow;
	protected TextView lastcode;
	protected Button refreshBtn;
	protected Context context;
	private BuyGameDialog gameDialog;
	private Handler gameHandler = new Handler();
	TabWidget tabWidget = null;
	Field mBottomLeftStrip;
	Field mBottomRightStrip;
	Handler hanler = new Handler();
	protected boolean isRun = true;// 线程是否运行变量
	private long lesstime = 0;
	private String batchCode = "";
	/* Add by fansm 20130417 start */
	protected TextView lastCodeTxt;
	/* Add by fansm 20130417 end */

	/** add by yejc 20130422 start */
	protected String event;
	public static final String REQUEST_EVENT = "event";
	public boolean isFromTrackQuery = false;

	/** add by yejc 20130422 start */
	protected TextView betInfoTextView;
	private RWSharedPreferences rw;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Add by fansm 20130418 start */
		if (Constants.isDebug)
			PublicMethod.outLog("BuyActivityGroup", "onCreate()");
		/* Add by fansm 20130418 end */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_main_group);
		relativeLayout1 = (RelativeLayout) findViewById(R.id.last_batchcode);
		context = this;
		rw=new RWSharedPreferences(this,"addInfo");
		isFromTrackQuery = getIntent().getBooleanExtra(
				TrackQueryActivity.FLAG_FROM_TRACK_QUERY, false);
		mTabHost = (TabHost) findViewById(R.id.tab_host);
		tabWidget = mTabHost.getTabWidget();
		mTabHost.setup(getLocalActivityManager());
		mInflater = LayoutInflater.from(this);
		// 初始化标题组建的显示
		initView();
		betInfoTextView = (TextView) findViewById(R.id.bet_info);
		// 监听tab切换事件
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				if (lotNo == Constants.LOTNO_NMK3) {
					((ZixuanAndJiXuan)getCurrentActivity()).scrollView.smoothScrollTo(0, 0);
					for(int tab_i = 0; tab_i < titleTextViews.length; tab_i++){
						if(titleTextViews[tab_i] != null){
							if(tab_i == mTabHost.getCurrentTab()){
								titleTextViews[tab_i].setTextColor(Color.WHITE);
							}else{
								titleTextViews[tab_i].setTextColor(Color.BLACK);
							}
							
						}
					}
					
				}
				betInfoTextView.setText("请选择投注号码");
				for (int i = 0; i < titles.length; i++) {
					if (tabId.equals(titles[i])) {
						title.setText(topTitles[i]);
						updateAddMissViewNum();
						return;
					}
				}
			}
		});
	}

	private void updateAddMissViewNum() {

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		betInfoTextView.setText("请选择投注号码");
	}

	public void getInfo() {
		Intent intent = getIntent();
		boolean isPosition = intent.getBooleanExtra("isPosition", false);
		if(isPosition){
			int position = intent.getIntExtra("position", 0);
			setTab(position);
		}
	
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
		// 上期开奖号码布局
		relativeLayout1 = (RelativeLayout) findViewById(R.id.last_batchcode);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		// 期号
		issue = (TextView) findViewById(R.id.layout_main_text_issue);
		// 上期截止时间
		time = (TextView) findViewById(R.id.layout_main_text_time);
		// 标题按钮
		imgIcon = (Button) findViewById(R.id.layout_main_img_return);
		imgIcon.setVisibility(View.VISIBLE);
		/* Add by fansm 20130417 start */
		refreshBtn = (Button) findViewById(R.id.refresh_code);
		refreshBtn.setVisibility(View.VISIBLE);
		lastCodeTxt = (TextView) findViewById(R.id.last_batchcode_textlable);
		refreshBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				rw.putBooleanValue("isShowDialog",true);
				if (getCurrentActivity() instanceof com.ruyicai.activity.buy.high.ZixuanAndJiXuan) {
					((com.ruyicai.activity.buy.high.ZixuanAndJiXuan) getCurrentActivity())
							.initLatestLotteryList();
				} else {
					setlastbatchcode(lotNo);
				}

			}
		});
		/* Add by fansm 20130417 end */
		// 上期开奖号码
		lastcode = (TextView) findViewById(R.id.last_batchcode_textlable_red);
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
		// 如果是双色球，在popupwindow中添加模拟选号选项
		popupwindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupwindow.setTouchable(true); // 设置PopupWindow可触摸
		popupwindow.setOutsideTouchable(true);
		popupView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}
		});
		popupwindow.showAsDropDown(imgIcon);
		// 玩法介绍
		final LinearLayout layoutGame = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout1);
		layoutGame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				layoutGame.setBackgroundResource(R.drawable.buy_group_layout_b);
				if (gameDialog == null) {
					gameDialog = new BuyGameDialog(context, lotNo, gameHandler);
				}
				gameDialog.showDialog();
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
				}
			}
		});

		// 历史开奖
		final LinearLayout layoutHosity = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout2);
		layoutHosity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				layoutHosity
						.setBackgroundResource(R.drawable.buy_group_layout_b);
				turnHosity();
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
				}
			}

		});

		// 投注查询
		final LinearLayout layoutQuery = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout4);
		layoutQuery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RWSharedPreferences shellRW = new RWSharedPreferences(context,
						"addInfo");
				String userno = shellRW.getStringValue(ShellRWConstants.USERNO);
				if (userno == null || userno.equals("")) {
					Intent intentSession = new Intent(context, UserLogin.class);
					startActivity(intentSession);
				} else {
					Intent intent = new Intent(BuyActivityGroup.this,
							BetQueryActivity.class);
					intent.putExtra("lotno", lotNo);
					startActivity(intent);
				}
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
				}
			}

		});
		final LinearLayout layoutLuck = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout3);
		final LinearLayout layoutParentLuck = (LinearLayout) popupView
				.findViewById(R.id.buy_group_one_layout3);
		final LinearLayout layoutPicture = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout6);
		final LinearLayout layoutParentPicture = (LinearLayout) popupView
				.findViewById(R.id.buy_group_one_layout6);
		addSimulateSelectNumber(popupView);

		if (isPicture()) {
			layoutParentPicture.setVisibility(View.VISIBLE);
			layoutPicture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					layoutPicture
							.setBackgroundResource(R.drawable.buy_group_layout_b);
					pictureOnclik(false);
					if (popupwindow != null && popupwindow.isShowing()) {
						popupwindow.dismiss();
					}
				}

			});
		} else {
			layoutParentPicture.setVisibility(LinearLayout.GONE);
		}
		if (getIsLuck()) {
			layoutParentLuck.setVisibility(LinearLayout.VISIBLE);
			layoutLuck.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					layoutLuck
							.setBackgroundResource(R.drawable.buy_group_layout_b);
					Intent intent = new Intent(BuyActivityGroup.this,
							LuckChoose2.class);
					intent.putExtra("lotno", lotNo);

					// 时时彩加上玩法Id
					if (lotNo == Constants.LOTNO_SSC) {
						intent.putExtra("caipiaoWanfaIndex",
								mTabHost.getCurrentTab());
					}

					startActivity(intent);
					if (popupwindow != null && popupwindow.isShowing()) {
						popupwindow.dismiss();
					}
				}
			});
		} else {
			layoutParentLuck.setVisibility(LinearLayout.GONE);
		}
	}

	/**
	 * 添加模拟选号钩子函数
	 * 
	 * @param view
	 */
	public void addSimulateSelectNumber(View view) {

	}

	public void turnHosity() {
		pictureOnclik(true);
	}

	public boolean isPicture() {
		return true;
	}

	public boolean getIsLuck() {
		return false;
	}

	/**
	 * 跳转历史开奖页面
	 * 
	 * @param isHosity
	 */
	public void pictureOnclik(boolean isHosity) {
		NoticeActivityGroup.ISSUE = batchCode;
		// 点击福彩3D跳转到福彩3D子列表中
		if (lotNo == Constants.LOTNO_FC3D) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 2);
			} else {
				intent.putExtra("position", 1);
			}
			startActivity(intent);
		}
		// 点击七乐彩跳转到七乐彩子列表中
		if (lotNo == Constants.LOTNO_QLC) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 2);
			} else {
				intent.putExtra("position", 1);
			}
			startActivity(intent);
		}
		// 点击双色球跳转到双色球子列表中
		if (lotNo == Constants.LOTNO_SSQ) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 2);
			} else {
				intent.putExtra("position", 1);
			}
			startActivity(intent);
		}
		// 点击排列三跳转到排列三子列表中
		if (lotNo == Constants.LOTNO_PL3) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 2);
			} else {
				intent.putExtra("position", 1);
			}
			startActivity(intent);
		}
		// 点击超级大乐透跳转到超级大乐透子列表中
		if (lotNo == Constants.LOTNO_DLT) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_DLT_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 2);
			} else {
				intent.putExtra("position", 1);
			}
			startActivity(intent);
		}
		// 点击时时彩跳转到时时彩子列表中
		if (lotNo == Constants.LOTNO_SSC) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 1);
			} else {
				intent.putExtra("position", 0);
			}
			startActivity(intent);
		}
		// 点击11-5跳转到11-5子列表中
		if (lotNo == Constants.LOTNO_11_5) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_DLC_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 1);
			} else {
				intent.putExtra("position", 0);
			}
			startActivity(intent);
		}
		// 点击11-5跳转到11运夺金子列表中
		if (lotNo == Constants.LOTNO_eleven) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_YDJ_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 1);
			} else {
				intent.putExtra("position", 0);
			}
			startActivity(intent);
		}
		// 点击22选5子列表中
		if (lotNo == Constants.LOTNO_22_5) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 2);
			} else {
				intent.putExtra("position", 1);
			}
			startActivity(intent);
		}

		// 点击时时彩跳转到进球彩子列表中
		if (lotNo == Constants.LOTNO_PL5) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_PL5_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 2);
			} else {
				intent.putExtra("position", 1);
			}
			startActivity(intent);
		}
		// 点击时时彩跳转到进球彩子列表中
		if (lotNo == Constants.LOTNO_QXC) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_QXC_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 2);
			} else {
				intent.putExtra("position", 1);
			}
			startActivity(intent);
		}
		if (lotNo == Constants.LOTNO_GD115) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_GD115_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 1);
			} else {
				intent.putExtra("position", 0);
			}
			startActivity(intent);
		}
		// 广东快乐十分
		if (lotNo == Constants.LOTNO_ten) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_GD10_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 1);
			} else {
				intent.putExtra("position", 0);
			}
			startActivity(intent);
		}
		// 内蒙快三
		if (lotNo == Constants.LOTNO_NMK3) {
			NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_NMK3_LISTVIEW;
			Intent intent = new Intent(this, NoticeActivityGroup.class);
			if (isHosity) {
				intent.putExtra("position", 1);
			} else {
				intent.putExtra("position", 0);
			}
			startActivity(intent);
		}
	}

	/**
	 * 是否隐藏期号
	 */
	public void isIssue(boolean isVisble) {
		if (isVisble) {
			relativeLayout.setVisibility(RelativeLayout.INVISIBLE);
			relativeLayout1.setVisibility(RelativeLayout.INVISIBLE);

		} else {
			relativeLayout.setVisibility(RelativeLayout.GONE);
			relativeLayout1.setVisibility(RelativeLayout.GONE);

		}
	}

	/**
	 * 赋值给当前期
	 * 
	 * @param type彩种编号
	 */
	public void setIssue(final String type) {
		issue.setText("期号获取中....");
		time.setText("获取中...");
		// 获取期号和截止时间
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				String issueStr = "";
				issueStr = GetLotNohighFrequency.getInstance().getInfo(type);
				final ForegroundColorSpan span_RED = new ForegroundColorSpan(
						Color.RED);
				final SpannableStringBuilder builder = new SpannableStringBuilder();
				if (!issueStr.equalsIgnoreCase("")) {
					try {
						JSONObject allIssue = new JSONObject(issueStr);
						final String issueStr2 = allIssue
								.getString("batchcode");
						String timeran = allIssue.getString("time_remaining");
						if (!timeran.equals("")) {
							lesstime = Long.valueOf(timeran);
						}
						hanler.post(new Runnable() {

							@Override
							public void run() {
								issue.setText("第" + issueStr2 + "期");
							}
						});
						while (isRun) {
							if (isEnd(lesstime)) {
								hanler.post(new Runnable() {
									public void run() {
										builder.clear();
										String lasttime = "离截止还剩:"
												+ PublicMethod
														.formatLongToTimeStr(lesstime);
										builder.append(lasttime);
										builder.setSpan(span_RED, 6,
												lasttime.length(),
												Spanned.SPAN_COMPOSING);
										time.setText(builder);
									}
								});
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								lesstime--;
							} else {
								hanler.post(new Runnable() {
									public void run() {
										builder.clear();
										String lasttime = "离截止还剩:00:00:00";
										builder.append(lasttime);
										builder.setSpan(span_RED, 6,
												lasttime.length(),
												Spanned.SPAN_COMPOSING);
										time.setText(builder);
									}
								});
								break;
							}
						}

					} catch (JSONException e) {
						e.printStackTrace();
						hanler.post(new Runnable() {

							@Override
							public void run() {
								issue.setText("获取期号失败");
							}
						});
					}
				}
			}
		});
		thread.start();
	}

	private boolean isEnd(long time) {
		if (time > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查询上期开奖号码
	 * 
	 * @param type彩种编号
	 */

	public void setlastbatchcode(final String type) {
		/** add by fansm 20130417 start */
		if (Constants.isDebug)
			PublicMethod.outLog(this.getClass().getSimpleName(),
					"setlastbatchcode(" + type + ")");
		lastCodeTxt.setText(getString(R.string.refresh_lastCode_msg));
		lastcode.setText("");
		/** add by fansm 20130417 end */
		final Handler tHandler = new Handler();

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					final JSONObject prizemore = PrizeInfoInterface.getInstance()
							.getNoticePrizeInfo(type, "1", "1");
					final String msg = prizemore.getString("message");
					final String code = prizemore.getString("error_code");
					if (code.equals("0000")) {
						JSONArray prizeArray = prizemore.getJSONArray("result");
						JSONObject prizeJson = (JSONObject) prizeArray.get(0);
						final String wincode = prizeJson.getString("winCode");
						batchCode = prizeJson.getString("batchCode");
						tHandler.post(new Runnable() {
							@Override
							public void run() {
								lastcode.setText(parseStrforcode(type, wincode));
								/* Add by fansm 20130417 start */
								lastCodeTxt.setText("第" + batchCode + "期开奖：");
								/* Add by fansm 20130417 end */
							}
						});

					} else {
						tHandler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(BuyActivityGroup.this, msg,
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (JSONException e) {
					// TODO: handle exception
				}
			}
		});
		thread.start();
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
		titleTextViews[index] = topTitle;
		if (Constants.LOTNO_NMK3.equals(lotNo)) {
			if (index != (titles.length-1)) {
				RelativeLayout relative = (RelativeLayout) indicatorTab
						.findViewById(R.id.RelativeLayout01);
				relative.setPadding(0, 0, PublicMethod.getPxInt(3, this), 0);
			}
			img.setBackgroundResource(R.drawable.nmk3_tab_buy_selector);
		} else {
			img.setBackgroundResource(R.drawable.tab_buy_selector);
		}
		topTitle.setText(titles[index]);
		Intent intent = new Intent(BuyActivityGroup.this, allId[index]);
		intent.putExtra("index", index);
		/** add by yejc 20130422 start */
		intent.putExtra(REQUEST_EVENT, event);
		intent.putExtra(TrackQueryActivity.FLAG_FROM_TRACK_QUERY,
				isFromTrackQuery);
		/** add by yejc 20130422 end */
		firstSpec = mTabHost.newTabSpec(titles[index])
				.setIndicator(indicatorTab).setContent(intent);
		mTabHost.addTab(firstSpec);
	}

	/**
	 * 格式化上期开奖号码的显示格式
	 * 
	 * @param type
	 *            彩种类型
	 * @param str
	 *            上期开奖号码
	 * @return 格式化字符串
	 */
	public SpannableStringBuilder parseStrforcode(String type, String str) {
		StringBuffer strb = new StringBuffer();
		SpannableStringBuilder builder = new SpannableStringBuilder();
		if (!str.equals("")) {
			strb.append(str.replace(" ", ""));
			if (type.equals(Constants.LOTNO_SSQ)) {
				strb.insert(2, ",");
				strb.insert(5, ",");
				strb.insert(8, ",");
				strb.insert(11, ",");
				strb.insert(14, ",");
				strb.insert(17, ",");
				builder.append(strb);
				ForegroundColorSpan span_RED = new ForegroundColorSpan(
						Color.RED);
				ForegroundColorSpan span_BULE = new ForegroundColorSpan(
						Color.BLUE);
				builder.setSpan(span_RED, 0, strb.length() - 2,
						Spanned.SPAN_COMPOSING);
				builder.setSpan(span_BULE, strb.length() - 2, strb.length(),
						Spanned.SPAN_COMPOSING);
			}
			if (type.equals(Constants.LOTNO_DLT)) {
				strb.insert(2, ",");
				strb.insert(5, ",");
				strb.insert(8, ",");
				strb.insert(11, ",");
				strb.insert(14, ",");
				strb.insert(18, ",");
				builder.append(strb);
				ForegroundColorSpan span_RED = new ForegroundColorSpan(
						Color.RED);
				ForegroundColorSpan span_BULE = new ForegroundColorSpan(
						Color.BLUE);
				builder.setSpan(span_RED, 0, strb.length() - 6,
						Spanned.SPAN_COMPOSING);
				builder.setSpan(span_BULE, strb.length() - 5, strb.length(),
						Spanned.SPAN_COMPOSING);
			}
			if (type.equals(Constants.LOTNO_FC3D)) {
				strb.insert(2, ",");
				strb.insert(5, ",");
				builder.append(strb);
				ForegroundColorSpan span_RED = new ForegroundColorSpan(
						Color.RED);
				builder.setSpan(span_RED, 0, strb.length(),
						Spanned.SPAN_COMPOSING);
			}
			if (type.equals(Constants.LOTNO_PL3)) {
				strb.insert(1, ",");
				strb.insert(3, ",");
				builder.append(strb);
				ForegroundColorSpan span_RED = new ForegroundColorSpan(
						Color.RED);
				builder.setSpan(span_RED, 0, strb.length(),
						Spanned.SPAN_COMPOSING);

			}
			if (type.equals(Constants.LOTNO_PL5)) {
				strb.insert(1, ",");
				strb.insert(3, ",");
				strb.insert(5, ",");
				strb.insert(7, ",");
				builder.append(strb);
				ForegroundColorSpan span_RED = new ForegroundColorSpan(
						Color.RED);
				builder.setSpan(span_RED, 0, strb.length(),
						Spanned.SPAN_COMPOSING);

			}
			if (type.equals(Constants.LOTNO_QLC)) {
				strb.insert(2, ",");
				strb.insert(5, ",");
				strb.insert(8, ",");
				strb.insert(11, ",");
				strb.insert(14, ",");
				strb.insert(17, ",");
				strb.insert(20, ",");
				builder.append(strb);
				ForegroundColorSpan span_RED = new ForegroundColorSpan(
						Color.RED);
				ForegroundColorSpan span_BULE = new ForegroundColorSpan(
						Color.BLUE);
				builder.setSpan(span_RED, 0, strb.length() - 2,
						Spanned.SPAN_COMPOSING);
				builder.setSpan(span_BULE, strb.length() - 2, strb.length(),
						Spanned.SPAN_COMPOSING);
			}
			if (type.equals(Constants.LOTNO_QXC)) {
				strb.insert(1, ",");
				strb.insert(3, ",");
				strb.insert(5, ",");
				strb.insert(7, ",");
				strb.insert(9, ",");
				strb.insert(11, ",");
				builder.append(strb);
				ForegroundColorSpan span_RED = new ForegroundColorSpan(
						Color.RED);
				builder.setSpan(span_RED, 0, strb.length(),
						Spanned.SPAN_COMPOSING);
			}
			if (type.equals(Constants.LOTNO_22_5)) {
				strb.insert(2, ",");
				strb.insert(5, ",");
				strb.insert(8, ",");
				strb.insert(11, ",");
				builder.append(strb);
				ForegroundColorSpan span_RED = new ForegroundColorSpan(
						Color.RED);
				builder.setSpan(span_RED, 0, strb.length(),
						Spanned.SPAN_COMPOSING);
			}
			if (type.equals(Constants.LOTNO_SSC)) {
				strb.insert(1, ",");
				strb.insert(3, ",");
				strb.insert(5, ",");
				strb.insert(7, ",");
				builder.append(strb);
				ForegroundColorSpan span_RED = new ForegroundColorSpan(
						Color.RED);
				builder.setSpan(span_RED, 0, strb.length(),
						Spanned.SPAN_COMPOSING);
			}

			if (type.equals(Constants.LOTNO_NMK3)) {
				strb.insert(2, ",");
				strb.insert(5, ",");
				builder.append(strb);
				ForegroundColorSpan span_RED = new ForegroundColorSpan(
						Color.RED);
				builder.setSpan(span_RED, 0, strb.length(),
						Spanned.SPAN_COMPOSING);
			}
		}
		return builder;

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
		try {
			title.setText(topTitles[index]);
		} catch (Exception e) {

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
		titleTextViews = new TextView[titles.length];
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
		titleTextViews = new TextView[titles.length];
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
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		super.onResume();
		getInfo();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isRun = false;
	}

	public void showBetInfo(String text) {
		betInfoTextView.setText(text);
	}
}
