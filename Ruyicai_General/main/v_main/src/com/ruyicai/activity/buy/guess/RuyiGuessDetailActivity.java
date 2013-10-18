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
import com.ruyicai.controller.Controller;
import com.ruyicai.util.PublicMethod;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class RuyiGuessDetailActivity extends Activity {

	private String mTitle = "";  //问题的标题
	private String mDetail = ""; //问题详情
	private String mId = "";  //竞猜Id
	private String mUserNo = ""; //用户名
	private boolean mIsEnd = false; //竞彩是否截止
	private boolean mIsMySelected = false; //竞彩是否截止
	private boolean mIsFirst = true;
	private int mPageIndex = 0; //当前列表显示了多少页数据
	private int mTotalPage = 0; //服务器端总共有多少页数据
	private int mScreenWidth = 0; //屏幕的宽
	private LayoutInflater mInflater = null;
	private LinearLayout[] mLinearLayouts = null;
	private Dialog mDialog = null;
	private ProgressDialog mProgressdialog = null;
//	private ProgressBar progressbar = null;
	private TextView mDescription = null;
	private View mFooterView = null;
	private ListView mListView = null;
	private String[] mStateArray = {"未参与", "已参与"};
	private MessageHandler mHandler = new MessageHandler();
	private List<ItemDetailInfoBean> mQuestionsList = new ArrayList<ItemDetailInfoBean>();
	private Map<Integer, String[]> mInfoMap = new HashMap<Integer, String[]>();
	private ListViewAdapter mAdapter = new ListViewAdapter();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_ruyiguess_detail);
		mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mScreenWidth = PublicMethod.getDisplayWidth(this);
		Intent intent = getIntent();
		mUserNo = intent.getStringExtra(RuyiGuessActivity.USER_NO);
		mId = intent.getStringExtra(RuyiGuessActivity.ITEM_ID);
		mTitle = intent.getStringExtra(RuyiGuessActivity.TITLE);
		mIsEnd = intent.getBooleanExtra(RuyiGuessActivity.ISEND, false);
		mIsMySelected = intent.getBooleanExtra(RuyiGuessActivity.MYSELECTED, false);
		mProgressdialog = PublicMethod.creageProgressDialog(this);
		Controller.getInstance(this).getRuyiGuessDetailList(mHandler, mUserNo, mId, "0");
		initView();
	}
	
	private void initView(){
		TextView title = (TextView)findViewById(R.id.ruyi_guess_item_subtitle);
		mDescription = (TextView)findViewById(R.id.ruyi_guess_item_description);
		title.setText(mTitle);
//		mDescription.setText(mDetail);
		mListView = (ListView)findViewById(R.id.ruyi_guess_listview);
		mListView.setAdapter(mAdapter);
//		mFooterView = mInflater.inflate(R.layout.lookmorebtn, null);
//		mListView.addFooterView(mFooterView);
//		mFooterView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				addmore();
//			}
//		});
//		if (mTotalPage > 1 && mIsFirst) {
//			mIsFirst = false;
//			mFooterView = mInflater.inflate(R.layout.lookmorebtn, null);
//			mListView.addFooterView(mFooterView);
//			mFooterView.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					addmore();
//				}
//			});
//		}
		Button submitBtn = (Button)findViewById(R.id.ruyi_guess_submit);
		submitBtn.setVisibility(View.VISIBLE);
		if (mIsMySelected) {
			submitBtn.setText(R.string.buy_ruyi_guess_go_work);
			submitBtn.setOnClickListener(new View.OnClickListener() {
				
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
				submitBtn.setBackgroundResource(R.drawable.buy_ruyiguess_item_gray);
				submitBtn.setText(R.string.buy_ruyi_guess_btn_end);
				int padding = PublicMethod.getPxInt(10, RuyiGuessDetailActivity.this);
				submitBtn.setPadding(padding, 0, padding, 0);
			} else {
				submitBtn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mProgressdialog = PublicMethod.creageProgressDialog(RuyiGuessDetailActivity.this);
						Controller.getInstance(RuyiGuessDetailActivity.this).sendDateToService(mHandler, mUserNo, mId, getSelectedInfo());
					}
				});
			}
		}
		
	}
	
	private void setMoreViewState() {
		if(mTotalPage >= 1 && mIsFirst) {
			mIsFirst = false;
			mFooterView = mInflater.inflate(R.layout.lookmorebtn, null);
			mListView.addFooterView(mFooterView);
			mFooterView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					addmore();
				}
			});
		}
	}

	
	/**
	 * 获取更多数据
	 */
	private void addmore() {
		mPageIndex++;
		if (mPageIndex > mTotalPage - 1) {
			mPageIndex = mTotalPage - 1;
//			mFooterView.setVisibility(View.GONE);
			mListView.removeFooterView(mFooterView);
			mIsFirst = true;
			Toast.makeText(this,
					R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();
		} else {
			if (mIsMySelected) {
				Controller.getInstance(this).getRuyiGuessList(mHandler, mUserNo, 1, "1", mPageIndex);
			} else {
				Controller.getInstance(this).getRuyiGuessList(mHandler, mUserNo, 1, "0", mPageIndex);
			}
		}
	}
	
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
				setResult(RESULT_OK);
				finish();
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
//				holder.radioGroup = (RadioGroup) convertView
//						.findViewById(R.id.ruyi_guess_item_radiogroup);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.ruyi_guess_item_icon);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			int titleId = position + 1;
			holder.title.setText(titleId+"、"+info.getQuestion());
			String[][] options = info.getOptions();
			boolean isSelected = false;
			int optionId = 0;
			//每一个item是否被选择过
			for (int i = 0; i < options.length; i++) {
				if ("1".equals(options[i][2])) {
					isSelected = true;
					optionId = i;
					break;
				}
			}
			
			if (mIsEnd) { //如果竞猜已经截止
				if (isSelected) {
					if (info.getAnswer().equals(options[optionId][1])) {
						holder.icon.setImageResource(R.drawable.buy_ruyi_guess_right);
						holder.participate.setText(R.string.buy_ruyi_guess_right);
					} else {
						holder.icon.setImageResource(R.drawable.buy_ruyi_guess_error);
						holder.participate.setText(R.string.buy_ruyi_guess_error);
					}
					String str = getResources().getString(
							R.string.buy_ruyi_guess_answer);
					SpannableString span = getSpannableString(
							str + info.getAnswer(), str.length(),
							str.length() + info.getAnswer().length());
					holder.time.setText(span);
					holder.icon.setVisibility(View.VISIBLE);
					holder.participate.setVisibility(View.VISIBLE);
				} else {
					holder.participate.setVisibility(View.GONE);
					holder.icon.setVisibility(View.GONE);
					
					String str = getResources().getString(
							R.string.buy_ruyi_guess_right_answer);
					SpannableString span = getSpannableString(
							str + info.getAnswer(), str.length(),
							str.length() + info.getAnswer().length());
					holder.time.setText(span);
				}
			} else {
				//根据选择情况来显示
				if (isSelected) {
					holder.participate.setText(mStateArray[1]);
				} else {
					holder.participate.setText(mStateArray[0]);
				}
				holder.participate.setVisibility(View.VISIBLE);
				holder.icon.setVisibility(View.GONE);
				String timeStr = getResources().getString(
						R.string.buy_ruyi_guess_stop);
				SpannableString timeSpan = getSpannableString(
						timeStr + info.getDrawTime(), timeStr.length(),
						timeStr.length() + info.getDrawTime().length());
				holder.time.setText(timeSpan);
			}
			//奖励积分
			String awardStr = getResources().getString(R.string.buy_ruyi_guess_item_integral);
			SpannableString awardSpan = getSpannableString(
					awardStr+info.getAward(), awardStr.length(),
					awardStr.length() + info.getAward().length());
			holder.integral.setText(awardSpan);
			
//			addViewForRadioGroup(holder.radioGroup, position);
			createDynamicView(holder.layout, position, isSelected, info.getEndState());
			
			return convertView;
		}
		
		class ViewHolder {
			TextView integral;
			TextView title;
			TextView time;
			TextView participate;
			ImageView icon;
			LinearLayout layout;
//			RadioGroup radioGroup;
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
	 * @param layout
	 * @param position
	 */
	private void createDynamicView(LinearLayout layout, int position, 
			boolean isSelected, String endState) {
		layout.removeAllViews();
		String[][] options = mQuestionsList.get(position).getOptions();
		if (options !=null && options.length > 0) {
			int length = options.length;
			mLinearLayouts = new LinearLayout[length];
			int lineNum = 3;// 每行个数
			int lastNum = length % lineNum;// 最后一行个数
			int line = 1;// 行数
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
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
			String info[] = (String[])(mInfoMap.get(position));
			TextView text = (TextView)itemLayout.findViewById(R.id.ruyi_guess_dynamic_text);
			text.setText(options[index][1]);
			layouts[index] = itemLayout;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			itemLayout.setLayoutParams(params);
			
			if ((mInfoMap.containsKey(position)
					&& (info[0]).equals(options[index][0]))
					|| "1".equals(options[index][2])) {
				icon.setBackgroundResource(R.drawable.buy_ruyi_guess_radio_selected);
			} else {
				icon.setBackgroundResource(R.drawable.buy_ruyi_guess_radio_normal);
			}
			
			if (isSelected || mIsEnd || ("1".equals(endState))) {
				itemLayout.setClickable(false);
			} else {
				itemLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						for (int i = 0; i < layouts.length; i++) {
							TextView tv = (TextView)layouts[i].findViewById(R.id.ruyi_guess_dynamic_icon);
							tv.setBackgroundResource(R.drawable.buy_ruyi_guess_radio_normal);
						}
						TextView icon = (TextView)v.findViewById(R.id.ruyi_guess_dynamic_icon);
						icon.setBackgroundResource(R.drawable.buy_ruyi_guess_radio_selected);
						
						String[] info = new String[2];
						info[0] = (String)icon.getTag();
						info[1] = mQuestionsList.get(position).getId();
						mInfoMap.put(position, info);
					}
				});
			}
			
			layoutOne.addView(itemLayout);
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
							createDialog();
						} else {
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
	
	private void parserDetailJSON(String data) {
		try {
			JSONObject jsonObj = new JSONObject(data);
			String errorCode = jsonObj.getString("error_code");
			if ("0000".equals(errorCode)) {
				mDetail = jsonObj.getJSONObject("quiz").getString("detail");
				mDescription.setText(mDetail);
				JSONArray jsonArray = jsonObj.getJSONArray("result");
				mTotalPage = Integer.valueOf(jsonObj.getString("totalPage").trim());//
				for (int i = 0; i < jsonArray.length(); i++) {
					boolean isAdd = false;
					ItemDetailInfoBean info = new ItemDetailInfoBean();
					JSONObject itemObj = jsonArray.getJSONObject(i);
					info.setId(itemObj.getString("id"));
					info.setQuestion(itemObj.getString("question"));
					info.setAward(itemObj.getString("award"));
					info.setDrawTime(itemObj.getString("drawTime"));
					info.setEndState(itemObj.getString("isEnd"));
					info.setAnswer(itemObj.getString("answer"));
					JSONArray optionsArray = itemObj.getJSONArray("options");
					String[][] options = new String[optionsArray.length()][3];
					for (int j = 0; j < optionsArray.length(); j++) {
						JSONObject item = optionsArray.getJSONObject(j);
						options[j][0] = item.getString("id");
						options[j][1] = item.getString("option");
						options[j][2] = item.getString("selected");
						if ("1".equals(item.getString("selected"))) {
							isAdd = true;
						}
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
				setMoreViewState();
//				initView();
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

}