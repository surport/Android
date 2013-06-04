/**
 * 
 */
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
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.explain.zq.ExplainListActivity;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.more.LuckChoose2;
import com.ruyicai.activity.notice.NoticeActivityGroup;
import com.ruyicai.activity.notice.NoticeZCActivity;
import com.ruyicai.activity.usercenter.BetQueryActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.net.newtransaction.PrizeInfoInterface;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

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
	protected PopupWindow popupwindow;
	protected TextView lastcode;
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
	private int index;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_main_group);
		relativeLayout1 = (RelativeLayout) findViewById(R.id.last_batchcode);
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
						index = i;
						title.setText(topTitles[i]);
						return;
					}
				}
			}
		});
	}

	public void getInfo() {
		Intent intent = getIntent();
		int position = intent.getIntExtra("position", 0);
		setTab(position);
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
		relativeLayout1 = (RelativeLayout) findViewById(R.id.last_batchcode);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		issue = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);
		imgIcon = (Button) findViewById(R.id.layout_main_img_return);
		lastcode = (TextView) findViewById(R.id.last_batchcode_textlable_red);
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
		// 如果是双色球，在popupwindow中添加模拟选号选项
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
		final LinearLayout layoutQuery = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout4);
		final LinearLayout layoutParentLuck = (LinearLayout) popupView
				.findViewById(R.id.buy_group_one_layout3);
		final LinearLayout layoutPicture = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout6);
		final LinearLayout layoutParentPicture = (LinearLayout) popupView
				.findViewById(R.id.buy_group_one_layout6);
		addSimulateSelectNumber(popupView);
		layoutGame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layoutGame.setBackgroundResource(R.drawable.buy_group_layout_b);
				if (gameDialog == null) {
					gameDialog = new BuyGameDialog(context, lotNo, gameHandler);
				}
				gameDialog.showDialog();
				popupwindow.dismiss();
			}
		});
		layoutQuery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
		if (isPicture()) {
			layoutParentPicture.setVisibility(View.VISIBLE);
			layoutPicture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					layoutPicture
							.setBackgroundResource(R.drawable.buy_group_layout_b);
					pictureOnclik(false);
					popupwindow.dismiss();
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
					// TODO Auto-generated method stub
					layoutLuck
							.setBackgroundResource(R.drawable.buy_group_layout_b);
					// Intent intent = new Intent(BuyActivityGroup.this,
					// LuckChoose.class);
					Intent intent = new Intent(BuyActivityGroup.this,
							LuckChoose2.class);
					intent.putExtra("lotno", lotNo);

					// 时时彩加上玩法Id
					if (lotNo == Constants.LOTNO_SSC) {
						intent.putExtra("caipiaoWanfaIndex",
								mTabHost.getCurrentTab());
					}

					startActivity(intent);
					popupwindow.dismiss();
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
		// 点击时时彩跳转到胜负彩子列表中
		if (lotNo == Constants.LOTNO_SFC) {
			Intent intent = new Intent(this, NoticeZCActivity.class);
			intent.putExtra("position", 0);
			startActivity(intent);
		}
		if (lotNo == Constants.LOTNO_RX9) {
			Intent intent = new Intent(this, NoticeZCActivity.class);
			intent.putExtra("position", 1);
			startActivity(intent);
		}
		if (lotNo == Constants.LOTNO_JQC) {
			Intent intent = new Intent(this, NoticeZCActivity.class);
			intent.putExtra("position", 2);
			startActivity(intent);
		}
		if (lotNo == Constants.LOTNO_LCB) {
			Intent intent = new Intent(this, NoticeZCActivity.class);
			intent.putExtra("position", 3);
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
		// 获取期号和截止时间
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
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
						// final String timeStr = allIssue.getString("endtime");
						String timeran = allIssue.getString("time_remaining");
						if (!timeran.equals("")) {
							lesstime = Long.valueOf(timeran);
						}
						hanler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								issue.setText("第" + issueStr2 + "期");
							}
						});
						while (isRun) {
							if (isEnd(lesstime)) {
								hanler.post(new Runnable() {
									public void run() {
										builder.clear();
										String lasttime = "离截止还剩:"
												+ formatLongToTimeStr(lesstime);
										builder.append(lasttime);
										// Log.e("time", lesstime+"");
										builder.setSpan(span_RED, 6,
												lasttime.length(),
												Spanned.SPAN_COMPOSING);
										time.setText(builder);
									}
								});
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
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
										// nextIssue();
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
								// TODO Auto-generated method stub
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

	public String formatLongToTimeStr(Long l) {
		int hour = 0;
		int minute = 0;
		int second = 0;
		second = l.intValue();
		if (second > 60) {
			minute = second / 60;
			second = second % 60;
		}
		if (minute > 60) {
			hour = minute / 60;
			minute = minute % 60;
		}
		return (String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String
				.valueOf(second));
	}

	/**
	 * 查询上期开奖号码
	 * 
	 * @param type彩种编号
	 */

	public void setlastbatchcode(final String type) {
		final Handler tHandler = new Handler();

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final JSONObject prizemore = PrizeInfoInterface.getInstance()
						.getNoticePrizeInfo(type, "1", "1");
				try {
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
								// TODO Auto-generated method stub
								lastcode.setText(parseStrforcode(type, wincode));
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
		img.setBackgroundResource(R.drawable.tab_buy_selector);
		topTitle.setText(titles[index]);
		Intent intent = new Intent(BuyActivityGroup.this, allId[index]);
		intent.putExtra("index", index);
		firstSpec = mTabHost.newTabSpec(titles[index])
				.setIndicator(indicatorTab).setContent(intent);
		mTabHost.addTab(firstSpec);
	}

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
		getInfo();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isRun = false;
	}
}
