/**
 * 
 */
package com.ruyicai.activity.join;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.CustomPopWindow;
import com.ruyicai.activity.usercenter.InquiryParentActivity;
import com.ruyicai.activity.usercenter.InquiryAdapter.OnChickItem;
import com.ruyicai.activity.usercenter.detail.Hemaidetail;
import com.ruyicai.activity.usercenter.info.BetQueryInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.QueryJoinCheckInterface;
import com.ruyicai.net.newtransaction.QueryJoinFollowInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 合买查询
 * 
 * @author Administrator
 * 
 */
public class JoinCheckActivity extends InquiryParentActivity {
	String phonenum, sessionid, userno;
	private LayoutInflater mInflater = null;
	private LinearLayout usecenercheck, usecenerLinearfollow;
	int[] linearId = { R.id.joincheck_query, R.id.joinfollow_query_ };
	private String[] titles = { "合买查询", "跟单查询" };
	private int joinType = 0;
	JSONObject json;
	JoinCheckAdapter adapter;
	JoinFollowAdapter adapter2;
	TabHost mTabHost;
	boolean isfollowfirst = true;
	boolean isrefresh = false;
	
	/**
	 * 用于存放合买各个彩种的当前数据
	 */
	protected ArrayList[] mJoinListArray = new ArrayList[12];
	
	/**
	 * 用于存放跟单各个彩种的当前数据
	 */
	protected ArrayList[] mFollowListArray = new ArrayList[12];
	
	/**
	 * 用于存放合买各个彩种的总页数
	 */
	protected int[] mJoinTotalPageArray = new int[12];
	
	/**
	 * 用于存放合买各个彩种的当前页的索引
	 */
	protected int[] mJoinPageIndexArray = new int[12];
	
	/**
	 * 用于存放跟单各个彩种的总页数
	 */
	protected int[] mFollowTotalPageArray = new int[12];
	
	/**
	 * 用于存放跟单各个彩种的当前页的索引
	 */
	protected int[] mFollowPageIndexArray = new int[12];
	
	
	/**
	 * 当前合买按彩种查询索引
	 */
	protected int mJoinCurrentLotnoIndex = 0;

	/**
	 * 当前合买按时间查询索引
	 */
	protected int mJoinCurrentTiemIndex = 0;
	
	/**
	 * 当前跟单按彩种查询索引
	 */
	protected int mFollowCurrentLotnoIndex = 0;

	/**
	 * 当前跟单按时间查询索引
	 */
	protected int mFollowCurrentTiemIndex = 0;
	
	/**
	 * 彩种数组
	 */
	private String[] mLotnoNoArray = {"", Constants.LOTNO_SSQ, Constants.LOTNO_FC3D, 
			Constants.LOTNO_DLT, Constants.LOTNO_QLC, Constants.LOTNO_QXC, 
			Constants.LOTNO_PL3, Constants.LOTNO_PL5, Constants.LOTNO_ZC, 
			Constants.LOTNO_JCL, Constants.LOTNO_JCZ, Constants.LOTNO_BJ_SINGLE};
	
	/**
	 * 按彩种显示按钮
	 */
	protected Button mSubLotnoBtn = null;
	
	/**
	 * 按时间显示按钮
	 */
	protected Button mSubTimeBtn = null;
	
	private int mClickType = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join_check_layout);
		usecenercheck = (LinearLayout)findViewById(R.id.joincheck_query);
		usecenerLinearfollow = (LinearLayout)findViewById(R.id.joinfollow_query_);
		mLotnoArray = getResources().getStringArray(R.array.join_lotno_list);
		mMainView = initLinearView();
		LinearLayout layout = (LinearLayout)mMainView.findViewById(R.id.usercenter_join_layout);
		layout.setVisibility(View.VISIBLE);
		StateChangeClickListener clickListener = new StateChangeClickListener();
		mSubLotnoBtn = (Button)mMainView.findViewById(R.id.lotno_change_state_title);
		mSubLotnoBtn.setText(mLotnoArray[0]);
		mSubLotnoBtn.setOnClickListener(clickListener);
		
		mSubTimeBtn = (Button)mMainView.findViewById(R.id.time_change_state_title);
		mSubTimeBtn.setText(mTimeArray[0]);
		mSubTimeBtn.setOnClickListener(clickListener);
		usecenercheck.addView(mMainView);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				detailDalog(position);
			}

		});
		initUserInfo();
		joinCheckNet();
		init();
	}

	private void initUserInfo() {
		RWSharedPreferences shellRW = new RWSharedPreferences(JoinCheckActivity.this, "addInfo");
		phonenum = shellRW.getStringValue("phonenum");
		sessionid = shellRW.getStringValue("sessionid");
		userno = shellRW.getStringValue("userno");
	}

	/**
	 * 初始化组件
	 */
	public void init() {
		mTabHost = (TabHost) findViewById(R.id.usercenter_tab_host);
		mTabHost.setup();
		mInflater = LayoutInflater.from(this);
		for (int i = 0; i < titles.length; i++) {
			addTab(i);
		}
		mTabHost.setOnTabChangedListener(giftTabChangedListener);
	}

	/**
	 * Tab切换事件
	 */
	TabHost.OnTabChangeListener giftTabChangedListener = new TabHost.OnTabChangeListener() {
		public void onTabChanged(String tabId) {
			if (tabId.equals(titles[0])) {
				joinType = 0;
				mSubLotnoBtn.setText(mLotnoArray[mJoinCurrentLotnoIndex]);
				mSubTimeBtn.setText(mTimeArray[mJoinCurrentTiemIndex]);
				initListView();
				initLinear();
			} else if (tabId.equals(titles[1])) {
				joinType = 1;
				mSubLotnoBtn.setText(mLotnoArray[mFollowCurrentLotnoIndex]);
				mSubTimeBtn.setText(mTimeArray[mFollowCurrentTiemIndex]);
				if (isfollowfirst) {
					joinCheckNet();
				} else {
					initListView();
				}
				initLinear();
			}
		}
	};

	/**
	 * 为TabHost添加TabSpec
	 * 
	 * @param index
	 */
	public void addTab(int index) {
		View indicatorTab = mInflater.inflate(R.layout.layout_nav_item, null);
		ImageView img = (ImageView) indicatorTab
				.findViewById(R.id.layout_nav_item);
		TextView title = (TextView) indicatorTab
				.findViewById(R.id.layout_nav_icon_title);
		img.setBackgroundResource(R.drawable.tab_buy_selector);
		title.setText(titles[index]);
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec(titles[index])
				.setIndicator(indicatorTab).setContent(linearId[index]);
		mTabHost.addTab(tabSpec);
	}

	protected void addmore() {
		int pageIndex = getNewPage();
		int allPage = getAllPage();
		pageIndex++;
		if (pageIndex < allPage) {
			setNewPage(pageIndex);
			joinCheckNet();
		} else {
			pageIndex = allPage - 1;
			setNewPage(pageIndex);
			mProgressbar.setVisibility(View.INVISIBLE);
			Toast.makeText(JoinCheckActivity.this, "已至尾页",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 初始化数据
	 */
	public void setValue() {
		try {
			JSONArray array = json.getJSONArray("result");
			int allPage = Integer.parseInt(json.getString("totalPage"));
			setAllPage(allPage);
			if (mJoinListArray[mJoinCurrentLotnoIndex] == null) {
				mJoinListArray[mJoinCurrentLotnoIndex] = new ArrayList<BetQueryInfo>();
			}
			if (getNewPage() == 0) {
				mJoinListArray[mJoinCurrentLotnoIndex].clear();
			}
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				JoinCheck checkInfo = new JoinCheck();
				checkInfo.setCaseLotId(obj.getString("caseLotId"));
				checkInfo.setStarter(obj.getString("starter"));
				checkInfo.setLotNo(obj.getString("lotNo"));
				checkInfo.setLotName(obj.getString("lotName"));
				checkInfo.setBuyAmt(obj.getString("buyAmt"));
				checkInfo.setPrizeAmt(obj.getString("prizeAmt"));
				checkInfo.setDisplayState(obj.getString("displayState"));
				checkInfo.setBuyTime(obj.getString("buyTime"));
				checkInfo.setPrizeState(obj.getString("prizeState"));
				mJoinListArray[mJoinCurrentLotnoIndex].add(checkInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化数据
	 */
	public void setValueFollow() {
		try {
			JSONArray array = json.getJSONArray("result");
			int allPage = Integer.parseInt(json.getString("totalPage"));
			setAllPage(allPage);
			if (mFollowListArray[mFollowCurrentLotnoIndex] == null) {
				mFollowListArray[mFollowCurrentLotnoIndex] = new ArrayList<BetQueryInfo>();
			}
			if (getNewPage() == 0) {
				mFollowListArray[mFollowCurrentLotnoIndex].clear();
			}
			for (int i = 0; i < array.length(); i++) {
				JoinFollow checkInfo = new JoinFollow();
				JSONObject obj = array.getJSONObject(i);
				try {
					checkInfo.setStarter(obj.getString("starter"));
					checkInfo.setLotNo(obj.getString("lotNo"));
					checkInfo.setTimes(obj.getString("times"));
					checkInfo.setJoinAmt(obj.getString("joinAmt"));
					checkInfo.setSafeAmt(obj.getString("safeAmt"));
					checkInfo.setMaxAmt(obj.getString("maxAmt"));
					checkInfo.setPercent(obj.getString("percent"));
					checkInfo.setJoinType(obj.getString("joinType"));
					checkInfo.setForceJoin(obj.getString("forceJoin"));
					checkInfo.setCreateTime(obj.getString("createTime"));
					checkInfo.setState(obj.getString("state"));
					checkInfo.setId(obj.getString("id"));
					checkInfo.setStarterno(obj.getString("starterUserNo"));
					checkInfo.setLotname(obj.getString("lotName"));
					JSONObject displayIcon = obj.getJSONObject("displayIcon");
					try {
						checkInfo.setCup(displayIcon.getString("cup"));
					} catch (Exception e) {
					}
					try {
						checkInfo.setGrayCup(displayIcon.getString("graycup"));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					try {
						checkInfo.setDiamond(displayIcon.getString("diamond"));
					} catch (Exception e) {
					}
					try {
						checkInfo.setGrayDiamond(displayIcon
								.getString("graydiamond"));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					try {
						checkInfo.setStarNum(displayIcon.getString("goldStar"));
					} catch (Exception e) {
					}
					try {
						checkInfo.setGrayStarNum(displayIcon
								.getString("graygoldStar"));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					try {
						checkInfo.setCrown(displayIcon.getString("crown"));
					} catch (Exception e) {
					}
					try {
						checkInfo.setGrayCrown(displayIcon
								.getString("graycrown"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				mFollowListArray[mFollowCurrentLotnoIndex].add(checkInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 合买详情框
	 */
	public void detailDalog(int position) {
		JoinCheck info = (JoinCheck) mJoinListArray[mJoinCurrentLotnoIndex].get(position);
		Intent intent = new Intent(JoinCheckActivity.this, Hemaidetail.class);
		intent.putExtra("caseLotId", info.getCaseLotId());
		intent.putExtra("lotno", info.getLotName());
		intent.putExtra("displaystate", info.getDisplayState());
		intent.putExtra("prizestate", info.getPrizeState());
		intent.putExtra("prizes", info.getPrizeAmt());
		startActivity(intent);
	}

	public void detaiDalogfollow(JoinFollow info) {
		LayoutInflater factory = LayoutInflater.from(this);
		/* 中奖查询的查看详情使用余额查询的布局 */
		View view = factory.inflate(R.layout.join_follow_detail, null);
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		TextView lotno = (TextView) view
				.findViewById(R.id.bet_detail_text_lotno);
		TextView starter = (TextView) view.findViewById(R.id.follow_man);
		TextView followkind = (TextView) view.findViewById(R.id.follow_kind);
		TextView cratetime = (TextView) view.findViewById(R.id.follow_time);
		TextView state = (TextView) view.findViewById(R.id.follow_state);
		TextView amt = (TextView) view.findViewById(R.id.follow_atm);
		TextView times = (TextView) view.findViewById(R.id.follow_times);
		TextView forceJoin = (TextView) view
				.findViewById(R.id.follow_forceJoin);
		LinearLayout starLayout = (LinearLayout) view
				.findViewById(R.id.join_detail_linear_record);
		PublicMethod.createStar(starLayout, info.getCrown(),
				info.getGrayCrown(), info.getCup(), info.getGrayCup(),
				info.getDiamond(), info.getGrayDiamond(), info.getStarNum(),
				info.getGrayStarNum(), JoinCheckActivity.this, 0);
		lotno.append(info.getLotname());
		if (info.getState().equals("0")) {
			state.append("无效");
		} else if (info.getState().equals("1")) {
			state.append("有效");
		}
		starter.append(info.getStarter());
		if (info.getJoinType().equals("0")) {
			followkind.append("按固定金额跟单");
		} else if (info.getJoinType().equals("1")) {
			followkind.append("按百分比跟单");
		}
		cratetime.append(info.getCreateTime());
		if (info.getJoinType().equals("0")) {
			amt.append("￥" + PublicMethod.toYuan(info.getJoinAmt()));
		} else if (info.getJoinType().equals("1")) {
			amt.append(info.getPercent() + "%,最大跟单金额" + "￥"
					+ PublicMethod.toYuan(info.getMaxAmt()));

		}
		if (info.getForceJoin().equals("1")) {
			forceJoin.append("是");
		} else if (info.getForceJoin().equals("0")) {
			forceJoin.append("否");
		}
		times.append(info.getTimes());
		Button cancleLook = (Button) view
				.findViewById(R.id.bet_detail_img_cannle);
		cancleLook.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();
		dialog.getWindow().setContentView(view);

	}

	private void initLinear() {
		if (joinType == 0) {
			if (usecenerLinearfollow.getChildCount() > 0) {
				usecenerLinearfollow.removeAllViews();
			}
			if (usecenercheck.getChildCount() == 0) {
				usecenercheck.addView(mMainView);
			}
		} else if (joinType == 1) {
			if (usecenercheck.getChildCount() > 0) {
				usecenercheck.removeAllViews();
			}
			if (usecenerLinearfollow.getChildCount() == 0) {
				usecenerLinearfollow.addView(mMainView);
			}
		}
	}

	protected void initListView() {
		if (joinType == 0) {
			if (adapter == null) {
				adapter = new JoinCheckAdapter(this, mJoinListArray[mJoinCurrentLotnoIndex]);
			} else {
				adapter.setList(mJoinListArray[mJoinCurrentLotnoIndex]);
			}
			mListView.setAdapter(adapter);
		} else if (joinType == 1) {
			if (adapter2 == null) {
				adapter2 = new JoinFollowAdapter(this, mFollowListArray[mFollowCurrentLotnoIndex]);
			} else {
				adapter2.setList(mFollowListArray[mFollowCurrentLotnoIndex]);
			}
			mListView.setAdapter(adapter2);
		}
	}

	/**
	 * 用户中心的适配器
	 */
	public class JoinCheckAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<JoinCheck> mList;
		int index;

		public JoinCheckAdapter(Context context, List<JoinCheck> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
		}
		
		public void setList(List<JoinCheck> list) {
			mList = list;
		}

		@Override
		public int getCount() {
			if (mList == null) {
				return 0;
			}
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			index = position;
			ViewHolder holder = null;
			JoinCheck info = (JoinCheck) mList.get(position);
			String lotName = info.getLotName();
			String displayState = info.getDisplayState();
			String buyTime = info.getBuyTime();
			String starter = info.getStarter();// 发起人
			String buyAmt = info.getBuyAmt();
			String prizeState = info.getPrizeState();
			/**add by pengcx 20130731 start*/
			String prizeAmt = info.getPrizeAmt();
			/**add by pengcx 20130731 end*/
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.join_check_listview_item, null);
				holder = new ViewHolder();
				holder.icon = (TextView) convertView
						.findViewById(R.id.join_check_item_text_icon);
				holder.starter = (TextView) convertView
						.findViewById(R.id.join_check_item_text_id);
				holder.result = (TextView) convertView
						.findViewById(R.id.join_check_item_text_result);
				holder.atm = (TextView) convertView
						.findViewById(R.id.join_check_item_text_amt);
				holder.time = (TextView) convertView
						.findViewById(R.id.join_check_item_text_time);
				/**add by pengcx 20130731 start*/
				holder.prizeamt = (TextView) convertView.findViewById(R.id.join_check_item_text_prizeamt);
				/**add by pengcx 20130731 end*/
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (starter.length() > 8) {
				starter = PublicMethod.getNewString(8, starter);
			}
			holder.icon.setText(lotName);
			/**add by pengcx 20130731 start*/
			if (Integer.valueOf(info.getprizeamt()) > 0) {
				holder.prizeamt.setVisibility(View.VISIBLE);
				SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
						"奖金：" + prizeAmt);
				spannableStringBuilder.setSpan(new ForegroundColorSpan(
						Color.RED), 3, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.prizeamt.setText(spannableStringBuilder);
			} else {
				holder.prizeamt.setVisibility(View.GONE);
			}
			
			/**add by pengcx 20130731 end*/
			if (displayState.equals("成功") || displayState.equals("认购中")
					|| displayState.equals("已中奖") | displayState.equals("满员")) {
				holder.result.setTextColor(Color.RED);
			} else {
				holder.result.setTextColor(Color.GRAY);
			}

			if (displayState.equals("成功") && prizeState.equals("3")) {
				holder.result.setText("未中奖");
				holder.result.setTextColor(Color.GRAY);
			} else {
				holder.result.setText("状态：" + displayState);
			}
			holder.starter.setText(starter);
			holder.time.setText(buyTime);
			holder.atm.setText("￥" + buyAmt);
			return convertView;
		}

		class ViewHolder {
			TextView icon;
			TextView result;
			TextView starter;
			TextView time;
			TextView atm;
			/**add by pengcx 20130731 start*/
			TextView prizeamt;
			TextView state;
			/**add by pengcx 20130731 end*/
		}
	}

	/**
	 * 用户中心的适配器
	 */
	public class JoinFollowAdapter extends BaseAdapter {
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<JoinFollow> mList;
		int index;

		public JoinFollowAdapter(Context context, List<JoinFollow> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
		}
		
		public void setList(List<JoinFollow> list) {
			mList = list;
		}

		@Override
		public int getCount() {
			if (mList == null) {
				return 0;
			}
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			index = position;
			ViewHolder holder = null;
			final JoinFollow info = (JoinFollow) mList.get(position);
			String icon = info.getLotname();
			String starter = info.getStarter();// 发起人
			String time = info.getCreateTime();
			String atm = info.getJoinAmt();
			if (atm.equals("")) {
				atm = "0";
			}
			String state = info.getState();
			String type = info.getJoinType();
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.join_check_listview_item, null);
				holder = new ViewHolder();
				holder.icon = (TextView) convertView
						.findViewById(R.id.join_check_item_text_icon);
				holder.starter = (TextView) convertView
						.findViewById(R.id.join_check_item_text_id);
				holder.result = (TextView) convertView
						.findViewById(R.id.join_check_item_text_result);
				holder.atm = (TextView) convertView
						.findViewById(R.id.join_check_item_text_amt);
				holder.time = (TextView) convertView
						.findViewById(R.id.join_check_item_text_time);
				holder.layout = (RelativeLayout) convertView
						.findViewById(R.id.join_follow);
				holder.detail = (Button) convertView
						.findViewById(R.id.usercenter_querydetail);
				holder.channelaggin = (Button) convertView
						.findViewById(R.id.usercenter_buyagain);
				holder.type = (TextView) convertView
						.findViewById(R.id.rengoutype);
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.join_check_listView_item_jump);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (starter.length() > 8) {
				starter = PublicMethod.getNewString(8, starter);
			}
			holder.icon.setText(icon);
			holder.imageView.setVisibility(View.GONE);
			if (state.equals("1")) {
				SpannableStringBuilder builder = new SpannableStringBuilder();
				String resultStr = "(" + "进行中" + ")";
				builder.append(resultStr);
				builder.setSpan(new ForegroundColorSpan(0xff006600), 1,
						resultStr.length() - 1, Spanned.SPAN_COMPOSING);
				holder.result.setText(builder, BufferType.EDITABLE);
				holder.channelaggin
						.setBackgroundResource(R.drawable.follow_channle);
				holder.channelaggin.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(JoinCheckActivity.this,
								JoinModifyActivity.class);
						intent.putExtra(JoinInfoActivity.USER_NO,
								info.getStarterno());
						intent.putExtra(Constants.LOTNO, info.getLotNo());
						intent.putExtra("id", info.getId());
						intent.putExtra("time", info.getCreateTime());
						intent.putExtra("times", info.getTimes());
						intent.putExtra("state", info.getState());
						intent.putExtra("joinamt", info.getJoinAmt());
						startActivity(intent);
						isrefresh = true;
					}
				});
			} else if (state.equals("0")) {
				SpannableStringBuilder builder = new SpannableStringBuilder();
				String resultStr = "(" + "无效" + ")";
				builder.append(resultStr);
				builder.setSpan(new ForegroundColorSpan(0xff666666), 1,
						resultStr.length() - 1, Spanned.SPAN_COMPOSING);
				holder.result.setText(builder, BufferType.EDITABLE);
				holder.channelaggin
						.setBackgroundResource(R.drawable.followagain);
				holder.channelaggin.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(JoinCheckActivity.this,
								JoinDingActivity.class);
						intent.putExtra(JoinInfoActivity.USER_NO,
								info.getStarterno());
						intent.putExtra(Constants.LOTNO, info.getLotNo());
						startActivity(intent);
						isrefresh = true;
					}
				});
			}
			holder.starter.setText(starter);
			holder.time.setText(time);
			if (type.equals("0")) {
				holder.type.setText("定制金额：");
				holder.atm.setText("￥" + PublicMethod.toYuan(atm));
			} else if (type.equals("1")) {
				holder.type.setText("定制比例：");
				holder.atm.setText(info.getPercent() + "%");
			}
			holder.layout.setVisibility(View.VISIBLE);
			holder.detail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					detaiDalogfollow(info);
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView icon;
			TextView result;
			TextView starter;
			TextView time;
			TextView atm;
			TextView type;
			RelativeLayout layout;
			Button detail;
			Button channelaggin;
			ImageView imageView;
		}
	}

	/**
	 * 设置颜色
	 */
	public void setResult(String state, String resultStr, TextView result,
			String prizeState) {
		SpannableStringBuilder builder = new SpannableStringBuilder();

		if (resultStr.equals("成功") && prizeState.equals("3")) {
			resultStr = "(未中奖)";
		} else {
			resultStr = "(" + resultStr + ")";
		}

		builder.append(resultStr);
		builder.setSpan(new ForegroundColorSpan(setColor(state)), 1,
				resultStr.length() - 1, Spanned.SPAN_COMPOSING);
		result.setText(builder, BufferType.EDITABLE);
	}

	public int setColor(String state) {
		int color = 0xff666666;
		if (state.equals("1")) {// 认购中
			color = 0xff006600;
		} else if (state.equals("2")) {// 满员
			color = 0xffcc0000;
		} else if (state.equals("3")) {// 成功
			color = 0xff006600;
		} else if (state.equals("4")) {// 撤单
			color = 0xff666666;
		} else if (state.equals("5")) {// 流单
			color = 0xff666666;
		} else if (state.equals("6")) {// 已中奖
			color = 0xffcc0000;
		}
		return color;
	}

	private void netting() {
		if (mProgressbar != null) {
			mProgressbar.setVisibility(ProgressBar.VISIBLE);
		}
		final Handler tHandler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String str = "00";
				if (joinType == 0) {
					str = QueryJoinCheckInterface.queryLotJoinCheck(userno,
							phonenum, "" + getNewPage(), Constants.PAGENUM
							, mLotnoNoArray[mJoinCurrentLotnoIndex], mTimeType[mJoinCurrentTiemIndex]);
				} else if (joinType == 1){
					str = QueryJoinFollowInterface.queryLotJoinfollow(userno,
							phonenum, "" + getNewPage(), Constants.PAGENUM, 
							mLotnoNoArray[mFollowCurrentLotnoIndex], mTimeType[mFollowCurrentTiemIndex]);
				}
				
				tHandler.post(new Runnable() {
					@Override
					public void run() {
						if (mProgressbar != null) {
							mProgressbar.setVisibility(ProgressBar.INVISIBLE);
							mView.setEnabled(true);
						}
						dismiss();
					}
				});
				try {
					json = new JSONObject(str);
					String message = json.getString("message");
					String error = json.getString("error_code");
					Message msg = mHandler.obtainMessage();
					if ("0000".equals(error)) {
						msg.what = 1;
					} /*else if ("0047".equals(error)) {
						msg.what = 2;
					}*/ else {
						msg.what = 2;
					}
					if (joinType == 1) {
						isfollowfirst = false;
					}
					msg.obj = message;
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 联网查询
	 */
	public void joinCheckNet() {
		if (joinType == 0) {
			if (mJoinListArray[mJoinCurrentLotnoIndex] == null) {
				mJoinListArray[mJoinCurrentLotnoIndex] = new ArrayList();
			}
			if ((R.id.lotno_change_state_title == mClickType)
					&& (mJoinListArray[mJoinCurrentLotnoIndex].size() > 0)) {
				initListView();
			} else {
				showDialog(0);
				setNewPage(0);
				netting();
			}
		} else if (joinType == 1) {
			if (mFollowListArray[mFollowCurrentLotnoIndex] == null) {
				mFollowListArray[mFollowCurrentLotnoIndex] = new ArrayList();
			}
			if ((R.id.lotno_change_state_title == mClickType)
					&& (mFollowListArray[mFollowCurrentLotnoIndex].size() > 0)) {
				initListView();
			} else {
				showDialog(0);
				setNewPage(0);
				netting();
			}
		}
	}

	class JoinCheck {
		String caseLotId = "";
		String starter = "";
		String lotName = "";
		String lotNo = "";
		String buyAmt = "";
		String prizeAmt = "";
		String displayState = "";
		String buyTime = "";
		String prizeState = "";

		public String getCaseLotId() {
			return caseLotId;
		}

		public void setCaseLotId(String caseLotId) {
			this.caseLotId = caseLotId;
		}

		public String getLotNo() {
			return lotNo;
		}

		public void setLotNo(String lotNo) {
			this.lotNo = lotNo;
		}

		public String getBuyAmt() {
			return buyAmt;
		}

		public void setBuyAmt(String buyAmt) {
			this.buyAmt = String.valueOf(Integer.valueOf(buyAmt) / 100);
		}

		public String getBuyTime() {
			return buyTime;
		}

		public void setBuyTime(String buyTime) {
			this.buyTime = buyTime;
		}

		public String getLotName() {
			return lotName;
		}

		public void setLotName(String lotName) {
			this.lotName = lotName;
		}

		public String getPrizeState() {
			return prizeState;
		}

		public void setPrizeState(String prizeState) {
			this.prizeState = prizeState;
		}

		public String getStarter() {
			return "发起人：" + starter;
		}

		public void setStarter(String starter) {
			this.starter = starter;
		}

		public String getPrizeAmt() {
			return "￥" + PublicMethod.toYuan(prizeAmt);
		}

		/**add by pengcx 20130731 start*/
		public String getprizeamt(){
			return prizeAmt;
		}
		/**add by pengcx 20130731 end*/
		public void setPrizeAmt(String prizeAmt) {
			this.prizeAmt = prizeAmt;
		}

		public JoinCheck() {

		}

		public String getDisplayState() {
			int state = Integer.valueOf(displayState);
			String result = "";
			switch (state) {
			case 1:
				result = "认购中";
				break;
			case 2:
				result = "满员";
				break;
			case 3:
				result = "成功";
				break;
			case 4:
				result = "撤单";
				break;
			case 5:
				result = "流单";
				break;
			case 6:
				result = "已中奖";
				break;
			}
			return result;
		}

		public void setDisplayState(String displayState) {
			this.displayState = displayState;
		}
	}

	class JoinFollow {
		public String starter = "";
		public String crown = "0";// 皇冠
		public String grayCrown = "0";
		public String cup = "0";// 奖杯
		public String grayCup = "0";
		public String diamond = "0";// 钻石
		public String grayDiamond = "0";
		public String starNum = "0";// 星
		public String grayStarNum = "0";

		public String getGrayCrown() {
			return grayCrown;
		}

		public void setGrayCrown(String grayCrown) {
			this.grayCrown = grayCrown;
		}

		public String getGrayCup() {
			return grayCup;
		}

		public void setGrayCup(String grayCup) {
			this.grayCup = grayCup;
		}

		public String getGrayDiamond() {
			return grayDiamond;
		}

		public void setGrayDiamond(String grayDiamond) {
			this.grayDiamond = grayDiamond;
		}

		public String getGrayStarNum() {
			return grayStarNum;
		}

		public void setGrayStarNum(String grayStarNum) {
			this.grayStarNum = grayStarNum;
		}

		public String getCrown() {
			return crown;
		}

		public void setCrown(String crown) {
			this.crown = crown;
		}

		public String getCup() {
			return cup;
		}

		public void setCup(String cup) {
			this.cup = cup;
		}

		public String getDiamond() {
			return diamond;
		}

		public void setDiamond(String diamond) {
			this.diamond = diamond;
		}

		public String getStarNum() {
			return starNum;
		}

		public void setStarNum(String starNum) {
			this.starNum = starNum;
		}

		public String getStarter() {
			return starter;
		}

		public void setStarter(String starter) {
			this.starter = starter;
		}

		public String getLotNo() {
			return lotNo;
		}

		public void setLotNo(String lotNo) {
			this.lotNo = lotNo;
		}

		public String getTimes() {
			return times;
		}

		public void setTimes(String times) {
			this.times = times;
		}

		public String getJoinAmt() {
			return joinAmt;
		}

		public void setJoinAmt(String joinAmt) {
			this.joinAmt = joinAmt;
		}

		public String getSafeAmt() {
			return safeAmt;
		}

		public void setSafeAmt(String safeAmt) {
			this.safeAmt = safeAmt;
		}

		public String getMaxAmt() {
			return maxAmt;
		}

		public void setMaxAmt(String maxAmt) {
			this.maxAmt = maxAmt;
		}

		public String getPercent() {
			return percent;
		}

		public void setPercent(String percent) {
			this.percent = percent;
		}

		public String getJoinType() {
			return joinType;
		}

		public void setJoinType(String joinType) {
			this.joinType = joinType;
		}

		public String getForceJoin() {
			return forceJoin;
		}

		public void setForceJoin(String forceJoin) {
			this.forceJoin = forceJoin;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String lotNo = "";
		public String times = "";
		public String joinAmt = "";
		public String safeAmt = "";
		public String maxAmt = "";
		public String percent = "";
		public String joinType = "";
		public String forceJoin = "";
		public String createTime = "";
		public String state = "";
		public String id = "";
		public String lotname = "";
		public String starterno = "";

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getLotname() {
			return lotname;
		}

		public void setLotname(String lotname) {
			this.lotname = lotname;
		}

		public String getStarterno() {
			return starterno;
		}

		public void setStarterno(String starterno) {
			this.starterno = starterno;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		if (isrefresh) {
//			newpagefollow = 0;
//			allpagefollow = 0;
			for (int i = 0; i < mFollowListArray.length; i++) {
				if (mFollowListArray[i] != null ) {
					mFollowListArray[i].clear();
				}
			}
			joinCheckNet();
			isrefresh = false;
		}
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void netting(int index) {}
	
	protected void setNewPage(int page) {
		if (joinType == 0) {
			mJoinPageIndexArray[mJoinCurrentLotnoIndex] = page;
		} else if (joinType == 1) {
			mFollowPageIndexArray[mFollowCurrentLotnoIndex] = page;
		}
	}

	protected int getNewPage() {
		if (joinType == 0) {
			return mJoinPageIndexArray[mJoinCurrentLotnoIndex];
		} else if (joinType == 1) {
			return mFollowPageIndexArray[mFollowCurrentLotnoIndex];
		}
		return 0;
	}

	protected void setAllPage(int page) {
		if (joinType == 0) {
			mJoinTotalPageArray[mJoinCurrentLotnoIndex] = page;
		} else if (joinType == 1) {
			mFollowTotalPageArray[mFollowCurrentLotnoIndex] = page;
		}
		
	}

	protected int getAllPage() {
		if (joinType == 0) {
			return mJoinTotalPageArray[mJoinCurrentLotnoIndex];
		} else if (joinType == 1) {
			return mFollowTotalPageArray[mFollowCurrentLotnoIndex];
		}
		return 0;
	}
	
	public class StateChangeClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			PopOnItemChick popClick = new PopOnItemChick();
			switch (v.getId()) {
			case R.id.lotno_change_state_title:
				mPopupWindow = new CustomPopWindow(JoinCheckActivity.this,
						mLotnoArray, 3, popClick, R.id.lotno_change_state_title);
				mPopupWindow.setBackground(R.drawable.inquiry_state_bg_left);
				if (joinType == 0) {
					mPopupWindow.setItemSelect(mJoinCurrentLotnoIndex);
				} else if (joinType == 1) {
					mPopupWindow.setItemSelect(mFollowCurrentLotnoIndex);
				}
				mSubLotnoBtn.setTextColor(mOrangeColor);
				break;
				
			case R.id.time_change_state_title:
				mPopupWindow = new CustomPopWindow(JoinCheckActivity.this,
						mTimeArray, 4, popClick, R.id.time_change_state_title);
				mPopupWindow.setBackground(R.drawable.inquiry_state_bg_right);
				if (joinType == 0) {
					mPopupWindow.setItemSelect(mJoinCurrentTiemIndex);
				} else if (joinType == 1) {
					mPopupWindow.setItemSelect(mFollowCurrentTiemIndex);
				}
				mSubTimeBtn.setTextColor(mOrangeColor);
				break;
			}
			mPopupWindow.showAsDropDown(v);
			mPopupWindow.getPopupWindow().setTouchInterceptor(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						mPopupWindow.dismiss();
						setTextColor();
						return true;
					}
					return false;
				}
			});
		}
	}
	
	public class PopOnItemChick implements OnChickItem {

		@Override
		public void onChickItem(int position, int type) {
			if (mPopupWindow != null && mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
				setTextColor();
			}
			mClickType = type;
			switch (type) {
			case R.id.lotno_change_state_title:
				if (joinType == 0) {
					mJoinCurrentLotnoIndex = position;
				} else if (joinType == 1) {
					mFollowCurrentLotnoIndex = position;
				}
				mSubLotnoBtn.setText(mLotnoArray[position]);
				break;

			case R.id.time_change_state_title:
				if (joinType == 0) {
					mJoinCurrentTiemIndex = position;
				} else if (joinType == 1) {
					mFollowCurrentTiemIndex = position;
				}
				mSubTimeBtn.setText(mTimeArray[position]);
				break;
			}
			joinCheckNet();
		}
	}
	
	protected void setTextColor() {
		mSubTimeBtn.setTextColor(mBlackColor);
		mSubLotnoBtn.setTextColor(mBlackColor);
	}
	
	Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				success();
				break;

			case 2:
				noRecords();
				Toast.makeText(JoinCheckActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	private void success() {
		if (joinType == 0) {
			setValue();
			if (getNewPage() == 0) {
				initLinear();
				initListView();
			} else {
				adapter.setList(mJoinListArray[mJoinCurrentLotnoIndex]);
				adapter.notifyDataSetChanged();
			}
		} else if (joinType == 1) {
			setValueFollow();
			if (getNewPage() == 0) {
				initLinear();
				initListView();
			} else {
				adapter2.setList(mFollowListArray[mFollowCurrentLotnoIndex]);
				adapter2.notifyDataSetChanged();
			}
		}
	}
	
	private void noRecords() {
		if (joinType == 0) {
			if (adapter != null) {
				adapter.setList(null);
			}
			mListView.setAdapter(adapter);
		} else if (joinType == 1) {
			if (adapter2 != null) {
				adapter2.setList(null);
			}
			mListView.setAdapter(adapter2);
		}
	}
	
	
	
}
