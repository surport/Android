package com.ruyicai.activity.buy.guess;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import com.ruyicai.activity.buy.guess.bean.ItemInfoBean;
import com.ruyicai.activity.buy.guess.util.RuyiGuessConstant;
import com.ruyicai.activity.buy.guess.util.RuyiGuessUtil;
import com.ruyicai.activity.buy.guess.view.PullRefreshLoadListView;
import com.ruyicai.activity.buy.guess.view.PullRefreshLoadListView.IXListViewListener;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.controller.Controller;
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
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/***
 * @author yejc
 *
 */
public class RuyiGuessActivity extends Activity implements /*OnRefreshListener*/IXListViewListener, OnGestureListener{
	/**
	 * 用户名
	 */
	private String mUserNo = "";
	
	/**
	 * 当前列表显示了多少页数据
	 */
	private int mPageIndex = 0;
	
	/**
	 * 服务器端总共有多少页数据
	 */
	private int mTotalPage = 0;
	
	/**
	 * 选择的竞猜Id
	 */
	private int mSelectedId = 0;
	
	/**
	 * 是否登陆
	 */
	private boolean mIsLogin = false;
	
	/**
	 * true我竞猜过的问题
	 */
	private boolean mIsMySelected = false;
	
	/**
	 * 自定义listview 用于下拉刷新
	 */
	private PullRefreshLoadListView mPullListView = null;
	
	/**
	 * 存放竞猜问题的list
	 */
	private List<ItemInfoBean> mQuestionsList = new ArrayList<ItemInfoBean>();
	
	/**
	 * 把参与题目的状态保存到map中，不用再去请求后台来改变当前页面的显示状态状态
	 */
	private Map<Integer, Boolean> mLocalDataMap = new HashMap<Integer, Boolean>();
	
	/**
	 * 分页请求的题目数、默认是10条
	 */
	private String mItemCount = "10";
	
	/**
	 * 用于存放图片的地址
	 */
	private List<String> mImageUrlList = new ArrayList<String>();
	
	/**
	 * 用于存放图片的名字
	 */
	private List<String> mImageNameList = new ArrayList<String>();
	
	/**
	 * 用于自动循环播放logo图片
	 */
	private ScheduledExecutorService mScheduledExecutorService;
	
	/**
	 * 用于自动循环播放logo图片的ViewFlipper
	 */
	private ViewFlipper mViewFlipper;
	
	/**
	 * 用于存放展示logo的view
	 */
	private List<View> mViewList = new ArrayList<View>();
	
	/**
	 * 用于存放下载图片的路径
	 */
	private String LOCAL_DIR = "/ruyicai/";
	
	/**
	 * 当图片没有下载完成时显示的默认图片
	 */
	private ImageView mDefaultIcon = null;
	
	private ProgressDialog mProgressdialog = null;
	
	private GestureDetector mGestureDetector = null;
	
	private Context mContext = null;
	
	private LayoutInflater mInflater = null;
	
	private RWSharedPreferences mSharedPreferences = null;
	
	private MessageHandler mHandler = new MessageHandler();
	
	private ListViewAdapter mAdapter = new ListViewAdapter();
	
	private int[] mIconArray = { R.drawable.textview_red_style,
			R.drawable.textview_orange_style,
			R.drawable.textview_yellow_style };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_ruyiguess);
		mContext = this;
		LOCAL_DIR = LOCAL_DIR + getPackageName() + "/ruyijc/";
		Controller.getInstance(this).getRuyiGuessImage(mHandler, 3);
		mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mGestureDetector = new GestureDetector(this);
		readUserInfo();
		initView();
		mProgressdialog = PublicMethod.creageProgressDialog(this);
		if (RuyiGuessConstant.JUMP_FLAG.equals(getIntent().getStringExtra(RuyiGuessConstant.JUMP_FLAG))) {
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
	
	private void initView(){
		mViewFlipper = (ViewFlipper)findViewById(R.id.guess_viewflipper);
		mDefaultIcon = (ImageView)findViewById(R.id.ruyiguess_default_icon);
		mPullListView = (PullRefreshLoadListView)findViewById(R.id.ruyi_guess_listview);
		mPullListView.setAdapter(mAdapter);
		mPullListView.setPullLoadEnable(true);
		mPullListView.setXListViewListener(this);
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
					intent.putExtra(RuyiGuessConstant.ITEM_ID, mQuestionsList.get(mSelectedId).getId());
					intent.putExtra(RuyiGuessConstant.USER_NO, mUserNo);
					intent.putExtra(RuyiGuessConstant.TITLE, mQuestionsList.get(mSelectedId).getTitle());
					intent.putExtra(RuyiGuessConstant.MYSELECTED, mIsMySelected);
					intent.putExtra(RuyiGuessConstant.ISLOTTERY, mQuestionsList.get(mSelectedId).getLotteryState());
					if ("1".equals(mQuestionsList.get(mSelectedId).getEndState())) {
						intent.putExtra(RuyiGuessConstant.ISEND, true);
					} else {
						intent.putExtra(RuyiGuessConstant.ISEND, false);
					}
					startActivityForResult(intent, 1001);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
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

	private class ListViewAdapter extends BaseAdapter {
		/**
		 * 存放剩余秒数
		 */
		Map<Integer, Long> remainTimeMap = Collections.synchronizedMap(new HashMap<Integer, Long>());
		
		/**
		 * 存放是否计时完成的状态
		 */
		Map<Integer, Boolean> flagMap = Collections.synchronizedMap(new HashMap<Integer, Boolean>());
		
		/**
		 * 存放计时线程
		 */
		Map<Integer, Thread> threadMap = Collections.synchronizedMap(new HashMap<Integer, Thread>());
		
		/**
		 * 存放少于一分钟的秒数
		 */
		Map<Integer, Long> lessMinuteMap = Collections.synchronizedMap(new HashMap<Integer, Long>());
		
		/**
		 * 存放少于一分钟的秒数的状态
		 */
		Map<Integer, Boolean> lessMinuteFlagMap = Collections.synchronizedMap(new HashMap<Integer, Boolean>());

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
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.integral.setText(PublicMethod.formatString(mContext, 
					R.string.buy_ruyi_guess_item_integral_two, info.getPrizePoolScore()));
			holder.title.setText(info.getTitle());
			holder.detail.setText(info.getDetail());
			boolean isLottery = false;
			if ("2".equals(info.getLotteryState())) {
				isLottery = true;
			} else {
				isLottery = false;
			}
			setTimeStyle(position, info.getTimeRemaining(), holder.time, isLottery);
			holder.integral.setBackgroundResource(mIconArray[position%3]);
			if ("0".equals(info.getEndState())) { //竞猜是否结束 0:未结束;1:已结束
				if (mIsLogin) {
					if (mLocalDataMap.containsKey(position) 
							&& mLocalDataMap.get(position)) {
						holder.participate.setText(R.string.buy_ruyi_guess_btn_participate);//根据状态判断显示
						holder.participate.setBackgroundResource(R.drawable.buy_ruyiguess_item_state_gray);
					} else {
						if ("0".equals(info.getParticipate())) { //是否参与竞猜 0:未参与;1:已参与
							holder.participate.setText(R.string.buy_ruyi_guess_btn_participate_jc);//根据状态判断显示
							holder.participate.setBackgroundResource(R.drawable.buy_ruyiguess_item_state_green);
						} else {
							holder.participate.setText(R.string.buy_ruyi_guess_btn_participate);//根据状态判断显示
							holder.participate.setBackgroundResource(R.drawable.buy_ruyiguess_item_state_gray);
						}
					}
				} else {
					holder.participate.setText(R.string.buy_ruyi_guess_btn_participate_jc);//根据状态判断显示
					holder.participate.setBackgroundResource(R.drawable.buy_ruyiguess_item_state_green);
				}
			} else {
				holder.participate.setBackgroundResource(R.drawable.buy_ruyiguess_item_state_gray);
				if (mIsLogin) {
					if ("0".equals(info.getParticipate())) { //是否参与竞猜 0:未参与;1:已参与
						holder.participate.setText(R.string.buy_ruyi_guess_btn_participate_jc);//根据状态判断显示
					} else {
						holder.participate.setText(R.string.buy_ruyi_guess_btn_participate);//根据状态判断显示
					}
				} else {
					holder.participate.setText(R.string.buy_ruyi_guess_btn_participate_jc);//根据状态判断显示
				}
			}
			return convertView;
		}
		
		class ViewHolder {
			TextView integral; //积分
			TextView title; //竞猜题目
			TextView detail; //竞猜题目详情
			TextView time; //竞猜剩余时间
			TextView participate; //参与状态
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
				final TextView timeTv, boolean isLottery) {
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
				timeTv.setTextColor(getResources().getColor(R.color.ruyi_guess_progress_red_color));
			} else {
				if (isLottery) {
					timeTv.setText("已开奖");
					timeTv.setTextColor(getResources().getColor(R.color.ruyi_guess_progress_red_color));
				} else {
					timeTv.setText("待公布");
					timeTv.setTextColor(getResources().getColor(R.color.ruyi_guess_progress_green_color));
				}
				
			}
		}
		
		private class TimeThread extends Thread {
			
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
					if (remainTimeMap == null || 
							(!(remainTimeMap.containsKey(position))
							&& !(flagMap.containsKey(position)))) {
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
			buffer.append(day).append("天");
			buffer.append(hour).append("时");
			buffer.append(minute).append("分");
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
				break;
			}
			
		}
	}
	
	private void parserJSON(String str, int type) {
		if (str == null || "".equals(null)) {
			Toast.makeText(RuyiGuessActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
			onLoad();
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
				} else if ("0047".equals(errorCode)) {
					TextView tv = (TextView) findViewById(R.id.ruyi_guest_no_record);
					tv.setVisibility(View.VISIBLE);
					mPullListView.setVisibility(View.GONE);
				} else {
					String message = jsonObj.getString("message");
					if (message == null || "null".equals(message) ||"".equals(message)) {
						message = "网络异常";
					}
					Toast.makeText(RuyiGuessActivity.this, message, Toast.LENGTH_SHORT).show();
				}
				onLoad();
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				if (type == 2) {
					onLoad();
				}
				dismissDialog();
			}
		}
	}
	
	private void dismissDialog() {
		if (mProgressdialog != null && mProgressdialog.isShowing()) {
			mProgressdialog.dismiss();
		}
	}
	
	private void initImageArray(String str) {
		try {
			JSONObject jsonObj = new JSONObject(str);
			String errorCode = jsonObj.getString("error_code");
			if ("0000".equals(errorCode)) {
				JSONArray jsonArray = jsonObj.getJSONArray("result");
				int length = jsonArray.length();
				String path = RuyiGuessUtil.getSaveFilePath(LOCAL_DIR);
				File directory = new File(path);
				if (!directory.exists()) {
					directory.mkdirs();
				}
				for (int i = 0; i < length; i++) {
					JSONObject itemObj = jsonArray.getJSONObject(i);
					String url = itemObj.getString("url");
					mImageUrlList.add(url);
					ImageView imageView = (ImageView)mInflater.inflate(
							R.layout.buy_ruyiguess_imageview, null);
					imageView.setImageResource(R.drawable.ruyiguess_default_bg);
					mViewFlipper.addView(imageView);
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
							imageView.setImageResource(R.drawable.ruyiguess_default_bg);
							RuyiGuessUtil.downLoadImage(file, url, imageView);
						}
					}
				}
				mDefaultIcon.setVisibility(View.GONE);
				mViewFlipper.setVisibility(View.VISIBLE);
				if (mScheduledExecutorService == null) {
					mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
					mScheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 5, TimeUnit.SECONDS);
				} else {
					if (mScheduledExecutorService.isShutdown()) {
						mScheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 5, TimeUnit.SECONDS);
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
	protected void onDestroy() {
		super.onDestroy();
		if (mScheduledExecutorService != null && !mScheduledExecutorService.isShutdown()) {
			mScheduledExecutorService.shutdown();
		}
	}

	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (mViewFlipper) {
				runOnUiThread(new Runnable() {
					public void run() {
						mViewFlipper.setInAnimation(mContext, R.anim.left_in);
						mViewFlipper.setOutAnimation(mContext, R.anim.left_out);
						mViewFlipper.showNext();
					}
				});
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(e1.getX()-e2.getX()>120){
			mViewFlipper.setInAnimation(mContext, R.anim.left_in);
			mViewFlipper.setOutAnimation(mContext, R.anim.left_out);
			mViewFlipper.showNext();//向右滑动
			return true;
		}else if(e1.getX()-e2.getX()<-120){
			mViewFlipper.setInAnimation(mContext, R.anim.right_in);
			mViewFlipper.setOutAnimation(mContext, R.anim.right_out);
			mViewFlipper.showPrevious();//向左滑动
			return true;
		}
		return false;
	}

	@Override
	public void onLoadMore() {
		addmore();
	}
	
	/**
	 * 获取更多数据
	 */
	private void addmore() {
		mPageIndex++;
		if (mPageIndex > mTotalPage - 1) {
			mPageIndex = mTotalPage - 1;
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
	
	private void onLoad() {
		mPullListView.stopRefresh();
		mPullListView.stopLoadMore();
		mPullListView.setRefreshTime(PublicMethod.dateToStrLong(
				new Date(System.currentTimeMillis())));
	}
	
}
