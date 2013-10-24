package com.ruyicai.activity.buy.guess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.guess.bean.ItemDetailInfoBean;
import com.ruyicai.activity.common.PullRefreshListView;
import com.ruyicai.activity.common.PullRefreshListView.OnRefreshListener;
import com.ruyicai.controller.Controller;
import com.ruyicai.util.PublicMethod;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RuyiGuessDetailActivity extends Activity  implements OnRefreshListener{

	private String mTitle = "";  //问题的标题
	private String mDetail = ""; //问题详情
	private String mId = "";  //竞猜Id
	private String mUserNo = ""; //用户名
	private boolean mIsEnd = false; //竞彩是否截止
	private boolean mIsMySelected = false; //是否从我的竞猜进入
	private boolean mIsSuccess = false; //参与成功标识
//	private boolean mIsFirst = true;
	private int mPageIndex = 0; //当前列表显示了多少页数据
//	private int mTotalPage = 0; //服务器端总共有多少页数据
//	private int mScreenWidth = 0; //屏幕的宽
	private int mPadValue = 0;
	private LayoutInflater mInflater = null;
	private LinearLayout[] mLinearLayouts = null;
	private Dialog mDialog = null;  //参与成功对话框
	private ProgressDialog mProgressdialog = null;
	private TextView mDescription = null;  //问题描述
//	private View mFooterView = null;
//	private ListView mListView = null;
	private PullRefreshListView mPullListView = null;
	private Button mSubmitBtn = null;
//	private String[] mStateArray = {"未参与", "已参与"};
	private MessageHandler mHandler = new MessageHandler();
	private List<ItemDetailInfoBean> mQuestionsList = new ArrayList<ItemDetailInfoBean>();
	//存放所要提交的参与问题
	private Map<Integer, String[]> mInfoMap = new HashMap<Integer, String[]>();
	private ListViewAdapter mAdapter = new ListViewAdapter();
	//问题前的序号
	private String[] mTitleSerial = {"A", "B", "C", "D", "E", "F", "G", "H",
	        "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
	        "V", "W", "X", "Y", "Z"};
	private int mViewIndex = 0;
	//存放问题序号的map
	private Map<Integer, String> mAnswerMap = new HashMap<Integer, String>();
	//保存参与的题目状态
	private Map<Integer, String[]> mLocalDataMap = new HashMap<Integer, String[]>();
	//存放没有选择的题目
//	private Map<Integer, Boolean> mNoSelectedMap = new HashMap<Integer, Boolean>();
	private int mNoSelectedCount = 0;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_ruyiguess_detail);
		mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPadValue = PublicMethod.getPxInt(15, this);
//		mScreenWidth = PublicMethod.getDisplayWidth(this);
		Intent intent = getIntent();
		mUserNo = intent.getStringExtra(RuyiGuessActivity.USER_NO);
		mId = intent.getStringExtra(RuyiGuessActivity.ITEM_ID);
		mTitle = intent.getStringExtra(RuyiGuessActivity.TITLE);
		mIsEnd = intent.getBooleanExtra(RuyiGuessActivity.ISEND, false);
		mIsMySelected = intent.getBooleanExtra(RuyiGuessActivity.MYSELECTED, false);
		mProgressdialog = PublicMethod.creageProgressDialog(this);
		Controller.getInstance(this).getRuyiGuessDetailList(mHandler, mUserNo, mId, "0", mPageIndex);
		initView();
	}
	
	private void initView(){
		TextView title = (TextView)findViewById(R.id.ruyi_guess_item_subtitle);
		mDescription = (TextView)findViewById(R.id.ruyi_guess_item_description);
		title.setText(mTitle);
		mPullListView = (PullRefreshListView)findViewById(R.id.ruyi_guess_listview);
//		mListView = (ListView)findViewById(R.id.ruyi_guess_listview);
//		mFooterView = mInflater.inflate(R.layout.lookmorebtn, null);
//		mListView.addFooterView(mFooterView);
//		mFooterView.setVisibility(View.GONE);
//		mFooterView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				addmore();
//			}
//		});
//		mListView.setAdapter(mAdapter);
		mPullListView.setAdapter(mAdapter);
		mPullListView.setonRefreshListener(this);
		mPullListView.setShowState(false);

		mSubmitBtn = (Button)findViewById(R.id.ruyi_guess_submit);
		if (mIsMySelected) {
			setSubmitBtnState(R.drawable.loginselecter, R.string.buy_ruyi_guess_go_work);
			mSubmitBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					List<Activity> activityList = Controller.getInstance(RuyiGuessDetailActivity.this).getActivityList();
					for (int i = 0; i < activityList.size(); i++) {
						Activity activity = activityList.get(i);
						activity.finish();
					}
					Intent intent = new Intent(RuyiGuessDetailActivity.this,
							RuyiGuessActivity.class);
					startActivity(intent);
					finish();
				}
			});
		} else {
			if (mIsEnd) {
				setSubmitBtnState(R.drawable.buy_ruyiguess_item_gray, R.string.buy_ruyi_guess_btn_end);
			}
		}
	}
	
//	private void setMoreViewState() {
//		if(mTotalPage > 1 && mIsFirst) {
//			mIsFirst = false;
//			mFooterView.setVisibility(View.VISIBLE);
//		}
//	}
	
	private void setSubmitState(boolean flag) {
		if (!mIsMySelected) {
			if (flag) {
				setSubmitBtnState(R.drawable.loginselecter, R.string.buy_ruyi_guess_submit_result);
				mSubmitBtn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mSubmitBtn.getText().toString()
								.equals(getResources()
										.getString(R.string.buy_ruyi_guess_btn_participate))) {
							return;
						}
						if (mInfoMap.size() > 0) {
							mProgressdialog = PublicMethod
									.creageProgressDialog(RuyiGuessDetailActivity.this);
							Controller.getInstance(RuyiGuessDetailActivity.this)
									.sendDateToService(mHandler, mUserNo, mId, getSelectedInfo());
						} else {
							Toast.makeText(RuyiGuessDetailActivity.this, 
									R.string.buy_ruyi_guess_please_select, 
									Toast.LENGTH_SHORT).show();
						}
					}
				});
			} else {
				if (!mIsEnd) {
					setSubmitBtnState(R.drawable.buy_ruyiguess_item_gray, R.string.buy_ruyi_guess_btn_participate);
				}
			}
		}
	}
	
	private void setSubmitBtnState(int picResId, int textResId) {
		mSubmitBtn.setVisibility(View.VISIBLE);
		mSubmitBtn.setBackgroundResource(picResId);
		mSubmitBtn.setText(textResId);
		mSubmitBtn.setPadding(mPadValue, 0, mPadValue, 0);
	}

	
//	/**
//	 * 获取更多数据
//	 */
//	private void addmore() {
//		mPageIndex++;
//		if (mPageIndex > mTotalPage - 1) {
//			mPageIndex = mTotalPage - 1;
//			mFooterView.setVisibility(View.GONE);
//			mIsFirst = true;
//			Toast.makeText(this,
//					R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();
//		} else {
//			mProgressdialog = PublicMethod.creageProgressDialog(this);
//			if (mIsMySelected) {
//				Controller.getInstance(this).getRuyiGuessDetailList(mHandler, mUserNo, mId, "1", mPageIndex);
//			} else {
//				Controller.getInstance(this).getRuyiGuessDetailList(mHandler, mUserNo, mId, "0", mPageIndex);
//			}
//		}
//	}
	
	/**
	 * 获取选择的竞猜信息
	 * @return
	 */
	private String getSelectedInfo() {
		Iterator<Integer> iterator = mInfoMap.keySet().iterator();
		StringBuffer buffer = new StringBuffer();
		while (iterator.hasNext()) {
			String[] info = mInfoMap.get(iterator.next());
			buffer.append(info[1]);
			buffer.append("_");
			buffer.append(info[0]);
			buffer.append("!");
		}
		String strInfo = buffer.toString();
		if (strInfo.length() > 0) {
//			mInfoMap.clear();
			return strInfo.substring(0, strInfo.length()-1);
		} 
		return "";
	}
	
	/**
	 * 创建参与成功对话框
	 */
	private void createDialog() {
		mDialog = new AlertDialog.Builder(this).create();
		View view = LayoutInflater.from(this)
				.inflate(R.layout.buy_ruyiguess_success_dialog, null);
		Button okBtn = (Button)view.findViewById(R.id.ruyi_guess_ok);
		okBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
			}
		});
		mDialog.show();
		mDialog.getWindow().setContentView(view);
	}
	
	private void dismissDialog() {
		if (mProgressdialog != null && mProgressdialog.isShowing()) {
			mProgressdialog.dismiss();
		}
	}
	
	private class ListViewAdapter extends BaseAdapter {
		
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
			ItemDetailInfoBean info = mQuestionsList.get(position);
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.buy_ruyiguess_detail_listview_item, null);
				holder = new ViewHolder();
				holder.integral = (TextView) convertView
						.findViewById(R.id.ruyi_guess_item_integral);
				holder.title = (TextView) convertView
						.findViewById(R.id.ruyi_guess_item_title);
				holder.time = (TextView) convertView
						.findViewById(R.id.ruyi_guess_item_time);
				holder.participate = (TextView) convertView
						.findViewById(R.id.ruyi_guess_item_participate);
				holder.layout = (LinearLayout) convertView
						.findViewById(R.id.ruyi_guess_itme_layout);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.ruyi_guess_item_icon);
				holder.state = (ImageView) convertView
						.findViewById(R.id.ruyi_guess_detail_item_state);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			int titleId = position + 1;
			holder.title.setText(titleId+"、"+info.getQuestion());
			
			//动态添加竞猜问题
			createDynamicView(holder.layout, position, info.isSelected(), info.getEndState());
			
			if (/*mIsEnd*/"1".equals(info.getEndState())) { //如果竞猜已经截止
				if (info.isSelected()) { //我选择的
					if ("1".equals(info.getCorrect())) {//未开奖或没选择的时候为空;选择的时候(0:错误;1:正确)
						holder.icon.setImageResource(R.drawable.buy_ruyi_guess_right);
						holder.participate.setText(R.string.buy_ruyi_guess_right);
						holder.icon.setVisibility(View.VISIBLE);
						holder.participate.setVisibility(View.VISIBLE);
					} else if ("0".equals(info.getCorrect())){
						holder.icon.setImageResource(R.drawable.buy_ruyi_guess_error);
						holder.participate.setText(R.string.buy_ruyi_guess_error);
						holder.icon.setVisibility(View.VISIBLE);
						holder.participate.setVisibility(View.VISIBLE);
					} else {
						holder.icon.setVisibility(View.GONE);
						holder.participate.setVisibility(View.GONE);
					}
					holder.state.setBackgroundResource(R.drawable.ruyiguess_participate);
				} else {
					holder.participate.setVisibility(View.GONE);
					holder.icon.setVisibility(View.GONE);
					holder.state.setBackgroundResource(R.drawable.ruyiguess_stop);
				}
				
				if (!"".equals(info.getAnswer())/* && !"null".equals(info.getAnswer())*/) {
					String str = "";
					if (info.isSelected()) {
						str = getResources().getString(
								R.string.buy_ruyi_guess_answer);
					} else {
						str = getResources().getString(
								R.string.buy_ruyi_guess_right_answer);
					}
					String answer = "";
					if (mAnswerMap.containsKey(position)) {
						answer = mAnswerMap.get(position);
					}
					SpannableString span = getSpannableString(
							str + answer, str.length(),
							str.length() + answer.length());
					holder.time.setText(span);
				} else {
					String timeStr = getResources().getString(
							R.string.buy_ruyi_guess_lottery);
					SpannableString timeSpan = getSpannableString(
							timeStr + info.getDrawTime(), timeStr.length(),
							timeStr.length() + info.getDrawTime().length());
					holder.time.setText(timeSpan);
				}
				holder.layout.setBackgroundResource(R.drawable.buy_ruyi_guess_item_center_gray);
			} else {
				if (info.isSelected()) {//我选择的
					holder.state.setBackgroundResource(R.drawable.ruyiguess_participate);
					String timeStr = getResources().getString(
							R.string.buy_ruyi_guess_lottery);
					SpannableString timeSpan = getSpannableString(
							timeStr + info.getDrawTime(), timeStr.length(),
							timeStr.length() + info.getDrawTime().length());
					holder.time.setText(timeSpan);
					holder.layout.setBackgroundResource(R.drawable.buy_ruyi_guess_item_center_gray);
				} else {
					//参与完成后相应的问题改变状态
					if (mIsSuccess && mLocalDataMap.containsKey(position)) {
						holder.state.setBackgroundResource(R.drawable.ruyiguess_participate);
						String timeStr = getResources().getString(
								R.string.buy_ruyi_guess_lottery);
						SpannableString timeSpan = getSpannableString(timeStr
								+ info.getDrawTime(), timeStr.length(),
								timeStr.length() + info.getDrawTime().length());
						holder.time.setText(timeSpan);
						holder.layout.setBackgroundResource(R.drawable.buy_ruyi_guess_item_center_gray);
					} else {
						holder.state.setBackgroundResource(getResources().getColor(R.color.transparent));
						String timeStr = getResources().getString(
									R.string.buy_ruyi_guess_stop);
						SpannableString timeSpan = getSpannableString(
								timeStr + info.getEndTime(), timeStr.length(),
								timeStr.length() + info.getEndTime().length());
						holder.time.setText(timeSpan);
						holder.layout.setBackgroundResource(R.drawable.buy_ruyi_guess_item_center);
					}
				}
				
				holder.participate.setVisibility(View.GONE);
				holder.icon.setVisibility(View.GONE);
			}
			int value = PublicMethod.getPxInt(10, RuyiGuessDetailActivity.this);
			holder.layout.setPadding(value, value, value, value);
			
			//奖励积分
			String awardStr = getResources().getString(R.string.buy_ruyi_guess_item_integral);
			String award = "";
			if ("1".equals(info.getCorrect())) {
				award = "+"+info.getAward();
			} else {
				award = info.getAward();
			} 
			
			SpannableString awardSpan = getSpannableString(
					awardStr+award, awardStr.length(),
					awardStr.length() + award.length());
			holder.integral.setText(awardSpan);
			
			return convertView;
		}
		
		class ViewHolder {
			TextView integral;
			TextView title;
			TextView time;
			TextView participate;
			ImageView icon;
			ImageView state;
			LinearLayout layout;
		}
	}
	
	private SpannableString getSpannableString(String text, int start, int end) {
		SpannableString span = new SpannableString(text);
		span.setSpan(new ForegroundColorSpan(Color.RED),
				start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return span;
	}
	
	/**
	 * 动态创建选项视图
	 * @param layout 选项的父布局
	 * @param position 在listview中的位置
	 * @param isSelected 是否选择题目
	 * @param endState 结束状态
	 */
	private void createDynamicView(LinearLayout layout, int position, 
			boolean isSelected, String endState) {
		layout.removeAllViews();
		String[][] options = mQuestionsList.get(position).getOptions();
		if (options !=null && options.length > 0) {
			int length = options.length;
			mLinearLayouts = new LinearLayout[length];
			int lineNum = 2;// 每行个数
			int lastNum = length % lineNum;// 最后一行个数
			int line = 1;// 行数
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
			mViewIndex = 0;
			if (length >= lineNum) {
				line = length / lineNum;
				for (int i = 0; i < line; i++) {
					LinearLayout layoutOne = addLine(lineNum, i, mLinearLayouts, 
							lineNum, options, position, isSelected, endState);
					layout.addView(layoutOne, params);
				}
				if (lastNum > 0) {
					LinearLayout layoutOne = addLine(lastNum, line, mLinearLayouts, 
							lineNum, options, position, isSelected, endState);
					layout.addView(layoutOne, params);
				}
			} else {
				LinearLayout layoutOne = addLine(length, 0, mLinearLayouts, 
						lineNum, options, position, isSelected, endState);
				layout.addView(layoutOne, params);
			}
		}
	}
	
	/**
	 * 添加一行
	 * @param lastNum 每行的数量
	 * @param line 行数
	 * @param layouts 
	 * @param lineNum 最后一行的数量
	 * @param options
	 * @param position 在listview中的位置
	 * @param isSelected 是否选择题目
	 * @param endState 结束状态 1：结束； 0：为结束
	 * @return
	 */
	private LinearLayout addLine(int lastNum, int line, final LinearLayout[] layouts,
			int lineNum, String[][] options, final int position, final boolean isSelected,
			String endState) {
		LinearLayout layoutOne = new LinearLayout(this);
		for (int j = 0; j < lastNum; j++) {
			int index = line * lineNum + j;
			LinearLayout itemLayout = (LinearLayout)mInflater.inflate(
					R.layout.buy_ruyiguess_dynamic_layout, null);
			TextView icon = (TextView)itemLayout.findViewById(R.id.ruyi_guess_dynamic_icon);
			icon.setTag(options[index][0]);
			
			String info[] = null;
			if (mLocalDataMap.containsKey(position)) {
				info = (String[])(mLocalDataMap.get(position));
			} 
			TextView text = (TextView)itemLayout.findViewById(R.id.ruyi_guess_dynamic_text);
			/**add 20131022*/
			TextView number = (TextView)itemLayout.findViewById(R.id.ruyi_guess_dynamic_number);
			number.setText(mTitleSerial[mViewIndex]+" ");
			if ((options[index][0]).equals(mQuestionsList.get(position).getAnswer())) {
				mAnswerMap.put(position, mTitleSerial[mViewIndex]);
			}
			text.setText(options[index][1]);
			mViewIndex ++;
			/**add 20131022*/
			
			layouts[index] = itemLayout;
			
			if (isSelected || mIsEnd || ("1".equals(endState))) {
				itemLayout.setClickable(false);
				text.setTextColor(getResources().getColor(R.color.ruyi_guess_end_text_color));
				number.setTextColor(getResources().getColor(R.color.ruyi_guess_end_text_color));
				if ((info != null && (info[0]).equals(options[index][0]))
						|| "1".equals(options[index][2])) {
					icon.setBackgroundResource(R.drawable.buy_ruyi_guess_radio_selected);
				} else {
					icon.setBackgroundResource(R.drawable.buy_ruyi_guess_radio_gray);
				}
			} else {
				if (mIsSuccess && mLocalDataMap.containsKey(position)) {
					itemLayout.setClickable(false);
					text.setTextColor(getResources().getColor(R.color.ruyi_guess_end_text_color));
					number.setTextColor(getResources().getColor(R.color.ruyi_guess_end_text_color));
					if (info != null 
							&& (info[0]).equals(options[index][0])) {
						icon.setBackgroundResource(R.drawable.buy_ruyi_guess_radio_selected);
					} else {
						icon.setBackgroundResource(R.drawable.buy_ruyi_guess_radio_gray);
					}
				} else {
					if (info != null 
							&& (info[0]).equals(options[index][0])) {
						icon.setBackgroundResource(R.drawable.buy_ruyi_guess_radio_selected);
					} else {
						icon.setBackgroundResource(R.drawable.buy_ruyi_guess_radio_normal);
					}
					text.setTextColor(getResources().getColor(R.color.black));
					number.setTextColor(getResources().getColor(R.color.black));
					itemLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							TextView icon = (TextView)v.findViewById(R.id.ruyi_guess_dynamic_icon);
							String[] info = new String[2];
							info[0] = (String)icon.getTag();
							info[1] = mQuestionsList.get(position).getId();
							if ("true".equals((String)v.getTag())) {
								icon.setBackgroundResource(R.drawable.buy_ruyi_guess_radio_normal);
								mInfoMap.remove(position);
								mLocalDataMap.remove(position);
								v.setTag("false");
							} else {
								for (int i = 0; i < layouts.length; i++) {
									TextView tv = (TextView)layouts[i].findViewById(R.id.ruyi_guess_dynamic_icon);
									tv.setBackgroundResource(R.drawable.buy_ruyi_guess_radio_normal);
									layouts[i].setTag("false");
								}
								v.setTag("true");
								icon.setBackgroundResource(R.drawable.buy_ruyi_guess_radio_selected);
								mInfoMap.put(position, info);
								mLocalDataMap.put(position, info);
							}
						}
					});
				}
				
			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
			params.gravity = Gravity.LEFT;
			if (j%2 == 1) {
				int value = PublicMethod.getPxInt(6, this);
				itemLayout.setPadding(2*value, value, value, value);
			}
			itemLayout.setLayoutParams(params);
			layoutOne.addView(itemLayout);
			if (lastNum == 1) {
				LinearLayout emptyLayout = new LinearLayout(this); 
				emptyLayout.setLayoutParams(params);
				layoutOne.addView(emptyLayout);
			}
		}
		return layoutOne;
	}
	
	
//	/***
//	 * RadioGroup 方式创建动态视图
//	 * @param group
//	 * @param position
//	 */
//	private void addViewForRadioGroup(RadioGroup group, int position){
//		group.removeAllViews();
//		String[][] options = mQuestionsList.get(position).getOptions();
//		for(int i=0; i<options.length; i++) { 
////			RadioButton button = (RadioButton)mInflater.inflate(
////					R.layout.buy_ruyiguess_radio_button_layout, null);
//		    RadioButton button = new RadioButton(this);
//		    button.setText(options[i][1]);
//		    button.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));  
//		    button.setCompoundDrawablesWithIntrinsicBounds(
//		    		getResources().getDrawable(R.drawable.radio_select), null, null, null);
//		    button.setTextColor(getResources().getColor(R.color.black));
//		    group.addView(button);  
//		}  
//	}
	
	private class MessageHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String data = (String)msg.obj;
			int type = msg.what;
			if (data == null || "".equals(data)) {
				Toast.makeText(RuyiGuessDetailActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
			} else {
				if (type == 1) {
					parserDetailJSON(data);
				} else if (type == 2){
					dismissDialog();
					try {
						JSONObject jsonObj = new JSONObject(data);
						String errorCode = jsonObj.getString("error_code");
						if ("0000".equals(errorCode)) {
							mIsSuccess = true;
							Log.i("yejc", "=========mNoSelectedMap.size()="+mInfoMap.size()
									+" ==========mNoSelectedCount="+mNoSelectedCount);
							if ((mNoSelectedCount - mInfoMap.size()) == 0) {
								setSubmitBtnState(R.drawable.buy_ruyiguess_item_gray, R.string.buy_ruyi_guess_btn_participate);
							}
							mInfoMap.clear();
							mAdapter.notifyDataSetChanged();
							createDialog();
						} else {
							Iterator<Integer> iterator = mInfoMap.keySet().iterator();
							while (iterator.hasNext()) {
								int index = iterator.next();
								if (mLocalDataMap.containsKey(index)) {
									mLocalDataMap.remove(index);
								}
							}
							mProgressdialog = PublicMethod.creageProgressDialog(RuyiGuessDetailActivity.this);
							Controller.getInstance(RuyiGuessDetailActivity.this).getRuyiGuessDetailList(mHandler, mUserNo, mId, "0", mPageIndex);
							String message = jsonObj.getString("message");
							Toast.makeText(RuyiGuessDetailActivity.this, message, Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 解析json串
	 * @param data json串
	 */
	private void parserDetailJSON(String data) {
		try {
			JSONObject jsonObj = new JSONObject(data);
			String errorCode = jsonObj.getString("error_code");
			if ("0000".equals(errorCode)) {
				mQuestionsList.clear(); //20131023
				mNoSelectedCount = 0;
				mDetail = jsonObj.getJSONObject("quiz").getString("detail");
				mDescription.setText(mDetail);
				JSONArray jsonArray = jsonObj.getJSONArray("result");
//				mTotalPage = Integer.valueOf(jsonObj.getString("totalPage").trim());//
				boolean isSubmit = false;
				for (int i = 0; i < jsonArray.length(); i++) {
					boolean isAdd = false;
					ItemDetailInfoBean info = new ItemDetailInfoBean();
					JSONObject itemObj = jsonArray.getJSONObject(i);
					info.setId(itemObj.getString("id"));
					info.setQuestion(itemObj.getString("question"));
					info.setAward(itemObj.getString("award"));
					info.setDrawTime(itemObj.getString("drawTime"));
					info.setEndTime(itemObj.getString("endTime"));
					info.setEndState(itemObj.getString("isEnd"));
					info.setAnswer(itemObj.getString("answerId"));
					info.setCorrect(itemObj.getString("correct"));
					JSONArray optionsArray = itemObj.getJSONArray("options");
					String[][] options = new String[optionsArray.length()][3];
					for (int j = 0; j < optionsArray.length(); j++) {
						JSONObject item = optionsArray.getJSONObject(j);
						options[j][0] = item.getString("id");
						options[j][1] = item.getString("option");
						options[j][2] = item.getString("selected");
						if ("1".equals(item.getString("selected"))) {
							isAdd = true;
							info.setSelected(true);
						}
					}
					if (!isAdd && "0".equals(itemObj.getString("isEnd"))) {
						isSubmit = true;
						mNoSelectedCount++;
						Log.i("yejc", "===========i="+i);
					}
					info.setOptions(options);
					if (mIsMySelected) {
						if (isAdd) {
							mQuestionsList.add(info);
						}
					} else {
						mQuestionsList.add(info);
					}
					
				}
				mAdapter.notifyDataSetChanged();
				mPullListView.onRefreshComplete();
//				setMoreViewState();
				setSubmitState(isSubmit);
			} else {
				String message = jsonObj.getString("message");
				Toast.makeText(RuyiGuessDetailActivity.this, message, Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			dismissDialog();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mIsSuccess && !mIsMySelected 
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
		}
		finish();
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onRefresh() {
		mProgressdialog = PublicMethod.creageProgressDialog(RuyiGuessDetailActivity.this);
		Controller.getInstance(RuyiGuessDetailActivity.this).getRuyiGuessDetailList(mHandler, mUserNo, mId, "0", mPageIndex);
	}

}