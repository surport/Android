package com.ruyicai.activity.buy.guess;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.guess.adapter.IndicatorsPageAdapter;
import com.ruyicai.activity.buy.guess.bean.ItemInfoBean;
import com.ruyicai.activity.buy.guess.util.RuyiGuessUtil;
import com.ruyicai.activity.common.PullRefreshListView;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.common.PullRefreshListView.OnRefreshListener;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.controller.Controller;
import com.ruyicai.custom.gallery.Lights;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/***
 * @author yejc
 *
 */
public class RuyiGuessActivity extends Activity   implements OnRefreshListener{

	public final static String ITEM_ID = "itemId";
	public final static String USER_NO = "userNo";
	public final static String TITLE = "title";
	public final static String ISEND = "isend";
	public final static String JUMP_FLAG = "jump_flag";
	public final static String MYSELECTED = "my_selected";
	/**用户名*/
	private String mUserNo = "";
	/**当前列表显示了多少页数据*/
	private int mPageIndex = 0;
	/**服务器端总共有多少页数据*/
	private int mTotalPage = 0;
	/**选择的竞猜Id*/
	private int mSelectedId = 0;
	/**是否登陆*/
	private boolean mIsLogin = false;
	/**true我竞猜过的问题*/
	private boolean mIsMySelected = false;
	private boolean mIsFirst = true;
//	/**是否点击了更多按钮*/
//	private boolean mIsClickMoreBtn = false; 
	private LayoutInflater mInflater = null;
	private ProgressDialog mProgressdialog = null;
	private RWSharedPreferences mSharedPreferences = null;
	/**自定义listview 用于下拉刷新*/
	private PullRefreshListView mPullListView = null;
	private View mFooterView = null;
	private List<ItemInfoBean> mQuestionsList = new ArrayList<ItemInfoBean>();
	private MessageHandler mHandler = new MessageHandler();
	private ListViewAdapter mAdapter = new ListViewAdapter();
	private String[] mStateArray = {"参与竞猜", "已参与"};
	private int[] mIconArray = { R.drawable.buy_ruyiguess_item_yellow,
			R.drawable.buy_ruyiguess_item_orange,
			R.drawable.buy_ruyiguess_item_pink };
	/**把参与题目的状态保存到map中，不用再去请求后台来改变当前页面的显示状态状态*/
	private Map<Integer, Boolean> mLocalDataMap = new HashMap<Integer, Boolean>();
	
	private String mItemCount = "10";
	private boolean mIsRun = true;
	
	private ScheduledExecutorService mScheduledExecutorService;
	private int mCurrentItem = 0; // 当前图片的索引号
	private ViewPager mViewPager;
	private ViewFlipper mViewFlipper;
	private List<String> mImageUrlList = new ArrayList<String>();
	private List<String> mImageNameList = new ArrayList<String>();
	private List<View> mViewList = new ArrayList<View>();
	private String LOCAL_DIR = "/ruyicai/";
//	private String[] mImageUrl = null;
//	private String[] mImageName = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_ruyiguess);
		LOCAL_DIR = LOCAL_DIR + getPackageName() + "/ruyijc/";
		Controller.getInstance(this).getRuyiGuessImage(mHandler, 3);
		mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		readUserInfo();
		initListView();
		mProgressdialog = PublicMethod.creageProgressDialog(this);
		if (JUMP_FLAG.equals(getIntent().getStringExtra(JUMP_FLAG))) {
			mIsMySelected = true;
			Controller.getInstance(this).getRuyiGuessList(mHandler, mUserNo, 1, "1", mPageIndex, mItemCount);
		} else {
			Controller.getInstance(this).getRuyiGuessList(mHandler, mUserNo, 1, "0", mPageIndex, mItemCount);
		}
		Controller.getInstance(this).addActivity(this); //添加到Activity list用于管理
	}
	
	private void readUserInfo() {
		mSharedPreferences = new RWSharedPreferences(RuyiGuessActivity.this,
				"addInfo");
		String sessionId = mSharedPreferences.getStringValue(ShellRWConstants.SESSIONID);
		if (!"".equals(sessionId)) {
			mIsLogin = true;
			mUserNo = mSharedPreferences.getStringValue(ShellRWConstants.USERNO);
		}
	}
	
	private void initListView(){
		mPullListView = (PullRefreshListView)findViewById(R.id.ruyi_guess_listview);
		mFooterView = mInflater.inflate(R.layout.lookmorebtn, null);
		mFooterView.setBackgroundColor(this.getResources().getColor(R.color.jczq_listview_item_bg));
		mPullListView.addHeaderView(getHeaderView());
		mPullListView.addFooterView(mFooterView);
		mFooterView.setVisibility(View.GONE);
		mFooterView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addmore();
			}
		});
		mPullListView.setAdapter(mAdapter);
		mPullListView.setonRefreshListener(this);
		mPullListView.setShowState(false);
		mPullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (!mIsLogin) {
					startActivityForResult(new Intent(RuyiGuessActivity.this,
							UserLogin.class), 1000);
				} else {
					Intent intent = new Intent(RuyiGuessActivity.this,
							RuyiGuessDetailActivity.class);
					mSelectedId = arg2 - mPullListView.getHeaderViewsCount();
					intent.putExtra(ITEM_ID, mQuestionsList.get(mSelectedId).getId());
					intent.putExtra(USER_NO, mUserNo);
					intent.putExtra(TITLE, mQuestionsList.get(mSelectedId).getTitle());
					intent.putExtra(MYSELECTED, mIsMySelected);
					if ("1".equals(mQuestionsList.get(mSelectedId).getEndState())) {
						intent.putExtra(ISEND, true);
					} else {
						intent.putExtra(ISEND, false);
					}
					startActivityForResult(intent, 1001);
				}
			}
		});
	}
	
	private View getHeaderView() {
		View view = mInflater.inflate(
				R.layout.buy_ruyiguess_viewpaper, null);
//		Lights lights = new Lights(this);
//		lights.setIsAnim(false);
//		LinearLayout layout = (LinearLayout)view.findViewById(R.id.guess_image_indicator);
//		layout.removeAllViews();
//		int[] images = { R.drawable.guess_image_indicator_normal, 
//				R.drawable.guess_image_indicator_selected };
//		lights.createViews(3, images, layout);
//		lights.isDefault(0);
//		mViewFlipper = (ViewFlipper)view.findViewById(R.id.guess_viewflipper);
//		initViewFlipper();
		mViewPager = (ViewPager)view.findViewById(R.id.guess_viewpager);
		mViewPager.setAdapter(new IndicatorsPageAdapter(mViewList));
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		return view;
	}
	
	private View[] getViewArray() {
//		List<View> list = new ArrayList<View>();
		int[] inages = {R.drawable.ryjc_20131120_1, 
				R.drawable.ryjc_20131120_2,
				R.drawable.ryjc_20131120_3,};
		View[] views = new View[inages.length];
		for (int i = 0; i < inages.length; i++) {
			ImageView view = (ImageView)mInflater.inflate(
					R.layout.buy_ruyiguess_imageview, null);
			view.setImageResource(inages[i]);
			views[i] = view;
		}
		return views;
	}
	
	private void setMoreViewState() {
		if (mTotalPage > 1 && mIsFirst) {
			mIsFirst = false;
			mFooterView.setVisibility(View.VISIBLE);
		}
	}
	
	private void dismissDialog() {
		if (mProgressdialog != null && mProgressdialog.isShowing()) {
			mProgressdialog.dismiss();
		}
	}
	
	/**
	 * 获取更多数据
	 */
	private void addmore() {
		mPageIndex++;
		if (mPageIndex > mTotalPage - 1) {
			mPageIndex = mTotalPage - 1;
			mFooterView.setVisibility(View.GONE);
			mIsFirst = true;
			Toast.makeText(this,
					R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();
		} else {
			mProgressdialog = PublicMethod.creageProgressDialog(this);
			if (mIsMySelected) {
				Controller.getInstance(this).getRuyiGuessList(mHandler, mUserNo, 1, "1", mPageIndex, mItemCount);
			} else {
				Controller.getInstance(this).getRuyiGuessList(mHandler, mUserNo, 1, "0", mPageIndex, mItemCount);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			mIsFirst = true;
			if (requestCode == 1000) {//登陆成功后重新加载当前账户的参与状态
				mPageIndex = 0;
				mIsLogin = true;
				mUserNo = mSharedPreferences.getStringValue(ShellRWConstants.USERNO);
				mProgressdialog = PublicMethod.creageProgressDialog(this);
				String count = String.valueOf(mQuestionsList.size());
				Controller.getInstance(this).getRuyiGuessList(mHandler, mUserNo, 2, "0", 0, count);
			} else if (requestCode == 1001){ //如果参与成功后把参与题目的状态保存到map对象中并重新加载
				mLocalDataMap.put(mSelectedId, true);
				mAdapter.notifyDataSetChanged();
			}
			
		}
	}

	private String formatString(int resId, String args) {
		return String.format(getResources().getString(resId), args);
	}

	private class ListViewAdapter extends BaseAdapter {
		/**存放剩余秒数*/
		Map<Integer, Long> remainTimeMap = Collections.synchronizedMap(new HashMap<Integer, Long>());
		
		/**存放是否计时完成的状态*/
		Map<Integer, Boolean> flagMap = Collections.synchronizedMap(new HashMap<Integer, Boolean>());
		
		/**存放计时线程*/
		Map<Integer, Thread> threadMap = Collections.synchronizedMap(new HashMap<Integer, Thread>());
		
		/**存放少于一分钟的秒数*/
		Map<Integer, Long> lessMinuteMap = Collections.synchronizedMap(new HashMap<Integer, Long>());
		
		/**存放少于一分钟的秒数的状态*/
		Map<Integer, Boolean> lessMinuteFlagMap = Collections.synchronizedMap(new HashMap<Integer, Boolean>());
		public ListViewAdapter() {
			
		}

		@Override
		public int getCount() {
			if (mQuestionsList != null) {
				return mQuestionsList.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return mQuestionsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ItemInfoBean info = mQuestionsList.get(position);
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.buy_ruyiguess_listview_item, null);
				holder = new ViewHolder();
				holder.integral = (TextView) convertView
						.findViewById(R.id.ruyi_guess_item_integral);
				holder.title = (TextView) convertView
						.findViewById(R.id.ruyi_guess_item_title);
				holder.detail = (TextView) convertView
						.findViewById(R.id.ruyi_guess_item_detail);
				holder.time = (TextView) convertView
						.findViewById(R.id.ruyi_guess_item_time);
				holder.participate = (TextView) convertView
						.findViewById(R.id.ruyi_guess_item_participate);
				holder.guessStop = (ImageView) convertView
						.findViewById(R.id.ruyi_guess_item_stop);
				holder.itemLayout = (LinearLayout) convertView
						.findViewById(R.id.ruyi_guess_item_layout);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.integral.setText(formatString(
					R.string.buy_ruyi_guess_item_integral_two, info.getPrizePoolScore()));
			holder.title.setText(info.getTitle());
			holder.detail.setText(info.getDetail());
			setTimeStyle(position, info.getTimeRemaining(), holder.time);
//			info.setParticipate(itemObj.getString("isParticipate"));//竞猜参与状态 0:未参与;1:已参与
//			info.setPrizePoolScore(itemObj.getString("prizePoolScore"));//竞猜奖池积分
//			info.setEndState(itemObj.getString("isEnd"));//竞猜是否截止 0:未结束;1:已结束
//			info.setLotteryState(itemObj.getString("state"));//竞猜开奖状态 0:未开奖;1:开奖中;2:已开奖
			
			if ("0".equals(info.getEndState())) { //竞猜是否结束 0:未结束;1:已结束
				holder.guessStop.setVisibility(View.GONE);
				holder.integral.setBackgroundResource(mIconArray[position%3]);
				holder.participate.setBackgroundResource(R.drawable.buy_ruyiguess_item_state_green);
				if (mIsLogin) {
					if (mLocalDataMap.containsKey(position) 
							&& mLocalDataMap.get(position)) {
						holder.participate.setText(mStateArray[1]);//根据状态判断显示
					} else {
						if ("0".equals(info.getParticipate())) { //是否参与竞猜 0:未参与;1:已参与
							holder.participate.setText(mStateArray[0]);//根据状态判断显示
						} else {
							holder.participate.setText(mStateArray[1]);//根据状态判断显示
						}
					}
				} else {
					holder.participate.setText(mStateArray[0]);//根据状态判断显示
				}
				holder.itemLayout.setBackgroundResource(R.drawable.buy_ruyiguess_item_bg);
			} else {
				if ("0".equals(info.getLotteryState())
						|| "1".equals(info.getLotteryState())) {
					holder.guessStop.setImageResource(R.drawable.guess_wait_lottery);
				} else {
					holder.guessStop.setImageResource(R.drawable.guess_finish_lottery);
				}
				holder.guessStop.setVisibility(View.VISIBLE);
				holder.integral.setBackgroundResource(R.drawable.buy_ruyiguess_item_gray);
				holder.participate.setBackgroundResource(R.drawable.buy_ruyiguess_item_state_gray);
				if (mIsLogin) {
					if ("0".equals(info.getParticipate())) { //是否参与竞猜 0:未参与;1:已参与
						holder.participate.setText(mStateArray[0]);//根据状态判断显示
					} else {
						holder.participate.setText(mStateArray[1]);//根据状态判断显示
					}
				} else {
					holder.participate.setText(mStateArray[0]);//根据状态判断显示
				}
				holder.itemLayout.setBackgroundResource(R.drawable.buy_ruyiguess_item_bg_yellow);
			}
			int padValue = PublicMethod.getPxInt(2, RuyiGuessActivity.this);
			holder.participate.setPadding(3*padValue, padValue, padValue, padValue);
			int layoutPad = 2*padValue;
			holder.itemLayout.setPadding(layoutPad, layoutPad, layoutPad, layoutPad);
			return convertView;
		}
		
		class ViewHolder {
			TextView integral; //积分
			TextView title; //竞猜题目
			TextView detail; //竞猜题目详情
			TextView time; //竞猜剩余时间
			TextView participate; //参与状态
			ImageView guessStop; //截止
			LinearLayout itemLayout;
		}
		
		public void clearData() {
			remainTimeMap.clear();
			flagMap.clear();
			lessMinuteFlagMap.clear();
			lessMinuteMap.clear();
			if (threadMap.size() > 0) {
				for (int key : threadMap.keySet()) {
					if (threadMap.get(key) != null){
						threadMap.get(key).interrupt();
					}
				}
				threadMap.clear();
			}
		}
		
		private void setTimeStyle(final int position, Long time, 
				final TextView timeTv) {
			if (time > 0) {
				if (!remainTimeMap.containsKey(position)) {
					remainTimeMap.put(position, time);
					timeTv.setText(formatLongToString(position, time));
					flagMap.put(position, true);
					if (!threadMap.containsKey(position)) {
						threadMap.put(position, new TimeThread(timeTv, position));
						Thread thread = threadMap.get(position);
						if (!(thread.isAlive())) {
							thread.start();
						}
					}
				} else {
					if (remainTimeMap.containsKey(position)) {
						timeTv.setText(formatLongToString(position, remainTimeMap.get(position)));
					} else {
						timeTv.setText("");
					}
				}
			} else {
				timeTv.setText("");
			}
		}
		
		class TimeThread extends Thread {
			
			TextView timeTv;
			int position;
			public TimeThread(TextView timeTv, int position/*, Handler handler*/) {
				this.timeTv = timeTv;
				this.position = position;
			}

			@Override
			public void run() {
				boolean flag = false;
				if (flagMap != null && flagMap.containsKey(position)) {
					flag = flagMap.get(position);
				}
				while (flag) {
					if (!(remainTimeMap.containsKey(position))
							&& !(flagMap.containsKey(position))) {
						return;
					}
					long currentTime = remainTimeMap.get(position);
					if (currentTime > 0) {
						int sleep = 60 * 1000;
						if (lessMinuteFlagMap.containsKey(position) && lessMinuteFlagMap.get(position)) {
							lessMinuteFlagMap.put(position, false);
							long tempTime = lessMinuteMap.get(position);
							sleep = (int)tempTime* 1000;
						}
						if (isInterrupted()) {
							return;
						}
						try {
							Thread.sleep(sleep);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						long newTime = currentTime - sleep/1000;
						if (!remainTimeMap.containsKey(position)) {
							return;
						}
						remainTimeMap.put(position, newTime);
						runOnUiThread(new Runnable() {
							public void run() {
								if ((flagMap.containsKey(position))) {
									timeTv.setText(formatLongToString(position, remainTimeMap.get(position)));
									mAdapter.notifyDataSetChanged();
								}
							}
						});
						if (!(newTime > 0)) {
							flagMap.put(position, false);
						}
						
						if ((flagMap.get(position) == null)
								|| !(flagMap.containsKey(position))) {
							flag = false;
						}
					}
				}
			}
		}
		
		public String formatLongToString(int position, long time) {
			StringBuffer buffer = new StringBuffer();
			int day = 0;
			int hour = 0;
			long minute = 0;
			if (time > 60) {
				minute = time / 60;
				time = time % 60;
				if (time > 0) {
					minute = minute + 1;
					lessMinuteMap.put(position, time);
					if (!lessMinuteFlagMap.containsKey(position)) {
						lessMinuteFlagMap.put(position, true);
					}
				}
			} else if (time > 0 && time <= 60){
				minute = 1;
			}
			
			if (minute > 0) {
				buffer.append("剩");
			} else {
				return "";
			}
			
			if (minute >= 60) {
				hour = (int)(minute / 60);
				minute = minute % 60;
			}
			
			if (hour >= 24) {
				day = hour / 24;
				hour = hour % 24;
			}
			
			if (day > 0) {
				buffer.append(day).append("天");
			}
			
			if (hour > 0) {
				buffer.append(hour).append("时");
			} else {
				if (day > 0) {
					buffer.append("0").append("时");
				}
			}
			
			if (minute  > 0) {
				buffer.append(minute).append("分");
			} else {
				buffer.append("0").append("分");
			}
			return buffer.toString();
		}
	}
	
	private class MessageHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String str = (String)msg.obj;
			switch (msg.what) {
			case 1:
			case 2:
				parserJSON(str, msg.what);
				break;
				
			case 3:
				initImageArray(str);
//				LOCAL_DIR = 
				break;
			}
			
		}
	}
	
	private void parserJSON(String str, int type) {
		if (str == null || "".equals(null)) {
			Toast.makeText(RuyiGuessActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
			mPullListView.onRefreshComplete();
			dismissDialog();
		} else {
			try {
				JSONObject jsonObj = new JSONObject(str);
				String errorCode = jsonObj.getString("error_code");
				if ("0000".equals(errorCode)) {
					if (type == 2) {
						mPageIndex = 0;
						mQuestionsList.clear();
						mLocalDataMap.clear();
						if (mAdapter != null) {
							mAdapter.clearData();
						}
					}
					
					JSONArray jsonArray = jsonObj.getJSONArray("result");
					mTotalPage = Integer.valueOf(jsonObj.getString("totalPage").trim());
					for (int i = 0; i < jsonArray.length(); i++) {
						ItemInfoBean info = new ItemInfoBean();
						JSONObject itemObj = jsonArray.getJSONObject(i);
						info.setId(itemObj.getString("id"));  //竞猜Id
						info.setTitle(itemObj.getString("title")); //竞猜题目
						info.setDetail(itemObj.getString("detail"));//竞猜题目详情
						info.setParticipate(itemObj.getString("isParticipate"));//竞猜参与状态 0:未参与;1:已参与
						info.setPrizePoolScore(itemObj.getString("prizePoolScore"));//竞猜奖池积分
						info.setEndState(itemObj.getString("isEnd"));//竞猜是否截止 0:未结束;1:已结束
						info.setLotteryState(itemObj.getString("state"));//竞猜开奖状态 0:未开奖;1:开奖中;2:已开奖
						String remainTime = itemObj.getString("time_remaining").trim();//竞猜剩余时间
						try {
							Long time = Long.parseLong(remainTime);
							info.setTimeRemaining(time);
						} catch(NumberFormatException e) {
							e.printStackTrace();
						}
						mQuestionsList.add(info);
					}
					mAdapter.notifyDataSetChanged();
					setMoreViewState();
				} else if ("0047".equals(errorCode)) {
					TextView tv = (TextView) findViewById(R.id.ruyi_guest_no_record);
					tv.setVisibility(View.VISIBLE);
					mPullListView.setVisibility(View.GONE);
				} else {
					String message = jsonObj.getString("message");
					Toast.makeText(RuyiGuessActivity.this, message, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				if (type == 2) {
					mPullListView.onRefreshComplete();
				}
				dismissDialog();
			}
		}
	}
	
	private void initImageArray(String str) {
		try {
			JSONObject jsonObj = new JSONObject(str);
			String errorCode = jsonObj.getString("error_code");
			if ("0000".equals(errorCode)) {
				JSONArray jsonArray = jsonObj.getJSONArray("result");
				int length = jsonArray.length();
//				mImageUrl = new String[length];
//				mImageName = new String[length];
				String path = RuyiGuessUtil.getSaveFilePath(LOCAL_DIR);
				File directory = new File(path);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				for (int i = 0; i < length; i++) {
					JSONObject itemObj = jsonArray.getJSONObject(i);
					String url = itemObj.getString("url");
					mImageUrlList.add(url);
//					ImageView iv
					ImageView imageView = (ImageView)mInflater.inflate(
							R.layout.buy_ruyiguess_imageview, null);
					imageView.setImageResource(R.drawable.ruyiguess_default_bg);
					mViewList.add(imageView);
					int index = url.lastIndexOf("/");
					if (index >= 0) {
						String imageName = url.substring(index+1, url.length());
						mImageNameList.add(imageName);
						File file = new File(path + imageName);
						if (file.exists()) {
							Bitmap bitmap = RuyiGuessUtil.decodeFile(file);
							if (bitmap != null) {
								imageView.setImageBitmap(bitmap);
							}
						} else {
							RuyiGuessUtil.downLoadImage(file, url, imageView);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		mProgressdialog = PublicMethod.creageProgressDialog(this);
		int maxResult = 0;
		if (mQuestionsList.size() > 10) {
			maxResult = mQuestionsList.size();
		} else {
			maxResult = 10;
		}
		mItemCount = String.valueOf(maxResult);
		Controller.getInstance(this).getRuyiGuessList(mHandler, mUserNo, 2, "0", 0, mItemCount);
	}
	
	@Override
	protected void onStart() {
		mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		mScheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 5, TimeUnit.SECONDS);
//		mScheduledExecutorService.execute(new ScrollTask());
//		mScheduledExecutorService.submit(new ScrollTask());
		super.onStart();
	}
	
	
	
	@Override
	protected void onStop() {
		super.onStop();
//		mScheduledExecutorService.shutdown();
	}

	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (mViewPager) {
//				while(mIsRun) {
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					runOnUiThread(new Runnable() {
//						public void run() {
//							mViewFlipper.showNext();
//						}
//					});
//				}
				mCurrentItem = (mCurrentItem + 1) % 3;
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}
	}
	
	ImageHandler handler = new ImageHandler();
	private class ImageHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mViewPager.setCurrentItem(mCurrentItem);// 切换当前显示的图片
		}
	}
	
//	class MyOnTouchListener implements View.OnTouchListener {
//
//		private float myStartX = -1;
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			Log.i("yejc", "===event.getAction()="+event.getAction());
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				// 获得第一个点的x坐标
//				myStartX = event.getX();
//				break;
//
//			case MotionEvent.ACTION_MOVE:
//				//得到最后一个点的坐标
//				float myLastX = event.getX() - myStartX;
//				if (myLastX > 50) {
//					mViewFlipper.showNext();
//				} else if (myLastX < -50) {
//					mViewFlipper.showPrevious();
//				}
//				Log.i("yejc", "=====myLastX="+myLastX);
//				// 可以滑动删除的条件：横向滑动大于100个单位坐标，竖直差小于50个单位坐标
//				break;
//
//			case MotionEvent.ACTION_UP:
//				myLastX = -1 ;
//				break;
//			}
//			return false;
//		}
//	}
	
	private class MyPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			mCurrentItem = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
		
	}
}
