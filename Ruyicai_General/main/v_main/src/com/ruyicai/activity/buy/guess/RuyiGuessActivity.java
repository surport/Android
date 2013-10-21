package com.ruyicai.activity.buy.guess;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.guess.bean.ItemInfoBean;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.controller.Controller;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/***
 * @author yejc
 *
 */
public class RuyiGuessActivity extends Activity {

	public final static String ITEM_ID = "itemId";
	public final static String USER_NO = "userNo";
	public final static String TITLE = "title";
	public final static String ISEND = "isend";
	public final static String JUMP_FLAG = "jump_flag";
	public final static String MYSELECTED = "my_selected";
	private String mUserNo = ""; // 用户名
	private int mPageIndex = 0; //当前列表显示了多少页数据
	private int mTotalPage = 0; //服务器端总共有多少页数据
	private int mSelectedId = 0; //选择的竞猜Id
	private boolean mIsLogin = false; // 是否登陆
	private boolean mIsMySelected = false; //true我竞猜过的问题
	private boolean mIsFirst = true; 
	private boolean mIsSuccess = false;
	private LayoutInflater mInflater = null;
	private ProgressDialog mProgressdialog = null;
	private RWSharedPreferences mSharedPreferences = null;
	private ListView mListView = null;
	private View mFooterView = null;
	private List<ItemInfoBean> mQuestionsList = new ArrayList<ItemInfoBean>();
	private MessageHandler mHandler = new MessageHandler();
	private ListViewAdapter mAdapter = new ListViewAdapter();
	private String[] mStateArray = { "竞猜中", "参与", "已参" };
	private int[] mIconArray = { R.drawable.buy_ruyiguess_item_yellow,
			R.drawable.buy_ruyiguess_item_orange,
			R.drawable.buy_ruyiguess_item_pink };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_ruyiguess);
		mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		readUserInfo();
		initListView();
		mProgressdialog = PublicMethod.creageProgressDialog(this);
		if (JUMP_FLAG.equals(getIntent().getStringExtra(JUMP_FLAG))) {
			mIsMySelected = true;
			Controller.getInstance(this).getRuyiGuessList(mHandler, mUserNo, 1, "1", mPageIndex);
		} else {
			Controller.getInstance(this).getRuyiGuessList(mHandler, mUserNo, 1, "0", mPageIndex);
		}
		Controller.getInstance(this).addActivity(this);
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
		mListView = (ListView)findViewById(R.id.ruyi_guess_listview);
		mFooterView = mInflater.inflate(R.layout.lookmorebtn, null);
		mFooterView.setBackgroundColor(this.getResources().getColor(R.color.jczq_listview_item_bg));
		mListView.addFooterView(mFooterView);
		mFooterView.setVisibility(View.GONE);
		mFooterView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addmore();
			}
		});
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (!mIsLogin) {
					startActivityForResult(new Intent(RuyiGuessActivity.this,
							UserLogin.class), 1000);
				} else {
					Intent intent = new Intent(RuyiGuessActivity.this,
							RuyiGuessDetailActivity.class);
					mSelectedId = arg2;
					intent.putExtra(ITEM_ID, mQuestionsList.get(arg2).getId());
					intent.putExtra(USER_NO, mUserNo);
					intent.putExtra(TITLE, mQuestionsList.get(arg2).getTitle());
					intent.putExtra(MYSELECTED, mIsMySelected);
					if ("1".equals(mQuestionsList.get(arg2).getEndState())) {
						intent.putExtra(ISEND, true);
					} else {
						intent.putExtra(ISEND, false);
					}
					startActivityForResult(intent, 1001);
				}
			}
		});
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
	
	private void addmore() {
		mPageIndex++;
		if (mPageIndex > mTotalPage - 1) {
			mPageIndex = mTotalPage - 1;
			mFooterView.setVisibility(View.GONE);
//			mListView.removeFooterView(mFooterView);
			mIsFirst = true;
			Toast.makeText(this,
					R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();
		} else {
			mProgressdialog = PublicMethod.creageProgressDialog(this);
			if (mIsMySelected) {
				Controller.getInstance(this).getRuyiGuessList(mHandler, mUserNo, 1, "1", mPageIndex);
			} else {
				Controller.getInstance(this).getRuyiGuessList(mHandler, mUserNo, 1, "0", mPageIndex);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			mIsFirst = true;
			if (requestCode == 1000) {
				mPageIndex = 0;
				mIsLogin = true;
				mUserNo = mSharedPreferences.getStringValue(ShellRWConstants.USERNO);
				mProgressdialog = PublicMethod.creageProgressDialog(this);
				Controller.getInstance(this).getRuyiGuessList(mHandler, mUserNo, 2, "0", 0);
			} else {
				mIsSuccess = true;
				mAdapter.notifyDataSetChanged();
			}
			
		}
	}

	private String formatString(int resId, String args) {
		return String.format(getResources().getString(resId), args);
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
				holder.time = (TextView) convertView
						.findViewById(R.id.ruyi_guess_item_time);
				holder.participate = (TextView) convertView
						.findViewById(R.id.ruyi_guess_item_participate);
				holder.guessStop = (ImageView) convertView
						.findViewById(R.id.ruyi_guess_item_stop);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.integral.setText(formatString(
					R.string.buy_ruyi_guess_item_integral_two, info.getAward()));
			
			holder.title.setText(info.getTitle());
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append(getResources().getString(R.string.buy_ruyi_guess_item_time));
			strBuffer.append(info.getStartTime());
			strBuffer.append(" ~ ");
			strBuffer.append(info.getEndTime());
			holder.time.setText(strBuffer.toString());

			if ("0".equals(info.getEndState())) { //竞猜是否结束 0:未结束;1:已结束
				holder.guessStop.setVisibility(View.GONE);
				holder.integral.setBackgroundResource(mIconArray[position%3]);
				holder.participate.setBackgroundResource(R.drawable.buy_ruyiguess_item_participateing);
				holder.participate.setVisibility(View.VISIBLE);
				if (mIsLogin) {
					if (mSelectedId == position && mIsSuccess) {
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
			} else {
				holder.guessStop.setVisibility(View.VISIBLE);
				holder.integral.setBackgroundResource(R.drawable.buy_ruyiguess_item_gray);
				holder.participate.setBackgroundResource(R.drawable.buy_ruyiguess_item_participated);
				if (mIsLogin) {
					if ("0".equals(info.getParticipate())) { //是否参与竞猜 0:未参与;1:已参与
						holder.participate.setVisibility(View.GONE);
					} else {
						holder.participate.setVisibility(View.VISIBLE);
						holder.participate.setText(mStateArray[2]);//根据状态判断显示
					}
				} else {
					holder.participate.setVisibility(View.GONE);
				}
			}
			return convertView;
		}
		
		class ViewHolder {
			TextView integral;
			TextView title;
			TextView time;
			TextView participate; //参与状态
			ImageView guessStop; //截止
		}
		
	}
	
	private class MessageHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String str = (String)msg.obj;
			if (str == null || "".equals(null)) {
				Toast.makeText(RuyiGuessActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
			} else {
				try {
					JSONObject jsonObj = new JSONObject(str);
					String errorCode = jsonObj.getString("error_code");
					if ("0000".equals(errorCode)) {
						if (msg.what == 2) {
							mQuestionsList.clear();
//							mFooterView.setVisibility(View.VISIBLE);
						}
						JSONArray jsonArray = jsonObj.getJSONArray("result");
						mTotalPage = Integer.valueOf(jsonObj.getString("totalPage").trim());
						for (int i = 0; i < jsonArray.length(); i++) {
							ItemInfoBean info = new ItemInfoBean();
							JSONObject itemObj = jsonArray.getJSONObject(i);
							info.setId(itemObj.getString("id"));
							info.setTitle(itemObj.getString("title"));
							info.setAward(itemObj.getString("award"));
							info.setStartTime(itemObj.getString("startTime"));
							info.setEndTime(itemObj.getString("endTime"));
							info.setParticipate(itemObj.getString("participate"));
							info.setEndState(itemObj.getString("isEnd"));
							mQuestionsList.add(info);
						}
						mAdapter.notifyDataSetChanged();
						setMoreViewState();
//						initListView();
					} else {
						String message = jsonObj.getString("message");
						Toast.makeText(RuyiGuessActivity.this, message, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			dismissDialog();
		}
		
	}
}
