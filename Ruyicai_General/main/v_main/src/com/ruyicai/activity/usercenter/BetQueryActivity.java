/**
 * 
 */
package com.ruyicai.activity.usercenter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.CustomPopWindow;
import com.ruyicai.activity.usercenter.InquiryAdapter.OnChickItem;
import com.ruyicai.activity.usercenter.detail.Betdetail;
import com.ruyicai.activity.usercenter.info.BetQueryInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetDetailsInterface;
import com.ruyicai.net.newtransaction.BetQueryInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 投注查询联网
 * 
 * @author Administrator
 * 
 */
public class BetQueryActivity extends Activity implements HandlerMsg {
	private LinearLayout usecenerLinear;
	private TextView titleTextView;
	private LinearLayout kind;// 按彩种查询
//	private String lotno = "";
	private boolean isbetkindall = false;// 下拉框选择查询全部彩种，默认为fasle,点击一次变为true;
	
	String jsonString;
	final String BATCHCODE = "batchCode", LOTMUTI = "lotMulti",
			ORDERTIME = "orderTime", PRIZEAMT = "prizeAmt",
			prizeState = "prizeState", WINCODE = "winCode",
			BETCODE = "betCode", LOTNO = "lotNo", AMOUNT = "amount",
			LOTNAME = "lotName", PLAY = "play", BET_CODE = "orderInfo";
	final String ISREPEATBUY = "isRepeatBuy";
	MyHandler touzhuhandler = new MyHandler(this);
	private final int DIALOG1_KEY = 0;
	ListView queryinfolist;
	private String userno;
	
//	private int typekind = 0;
	Context context = this;
	ProgressDialog dialog;
	String jsonobject = null;
	BetAdapter adapter;
	View view;
	ProgressBar progressbar;
	boolean isfirst = false;
	
	/**
	 * 用于存放各个彩种的总页数
	 */
	private int[] mTotalPageArray = new int[18];
	
	/**
	 * 用于存放各个彩种的当前页的索引
	 */
	private int[] mPageIndexArray = new int[18];
	
	/**
	 * 用于存放各个彩种的当前数据
	 */
	private ArrayList[] mListArray = new ArrayList[18];
	
	/**
	 * 彩种数组
	 */
	private String[] mLotnoArray = null;
	
	/**
	 * 所有状态数组
	 */
	private String[] mStateArray = null;
	
	/**
	 * 所有时间数组
	 */
	private String[] mTimeArray = null;
	
	/**
	 * 按彩种显示按钮
	 */
	private Button mLotnoBtn = null;
	
	/**
	 * 按中奖状态显示按钮
	 */
	private Button mAwardStateBtn = null;
	
	/**
	 * 按时间显示按钮
	 */
	private Button mTimeBtn = null;
	
	/**
	 * 当前按彩种查询索引
	 */
	private int mCurrentLotnoIndex = 0;
	
	/**
	 * 当前按中奖状态查询索引
	 */
	private int mCurrentAwardStateIndex = 0;
	
	/**
	 * 当前按时间查询索引
	 */
	private int mCurrentTiemIndex = 0;
	
	/**
	 * 全部彩种、全部状态、全部时间的切换窗口
	 */
	private CustomPopWindow mPopupWindow = null;
	
	/**
	 * 中奖状态查询请求参数数组
	 */
	private String[] mAwardStateType = {"0","1","2","3","4"};
	
	/**
	 * 按时间查询请求参数数组
	 */
	private String[] mTimeType = {"","1","2","3","4"};
	
	/**
	 * 彩种编号数组
	 */
	private String[] mLotnoNoArray = {"", Constants.LOTNO_SSQ, Constants.LOTNO_FC3D, 
			Constants.LOTNO_11_5,Constants.LOTNO_GD115, Constants.LOTNO_NMK3,
			Constants.LOTNO_DLT, Constants.LOTNO_SSC, Constants.LOTNO_QLC,
			Constants.LOTNO_QXC, Constants.LOTNO_PL3, Constants.LOTNO_PL5, 
			Constants.LOTNO_eleven, Constants.LOTNO_ten, Constants.LOTNO_ZC, 
			Constants.LOTNO_JCL, Constants.LOTNO_JCZ, Constants.LOTNO_BJ_SINGLE};

	/**
	 * 创建pojo类实例存放请求参数
	 */
	BetAndWinAndTrackAndGiftQueryPojo mBetQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_mainlayoutold);
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		userno = shellRW.getStringValue("userno");
		mBetQueryPojo.setUserno(userno);
		mBetQueryPojo.setMaxresult("10");
		mBetQueryPojo.setType("betList");
		initView();
		getInfo();
		isfirst = true;
	}
	
	private void initView() {
		mLotnoArray = getResources().getStringArray(R.array.lotno_list);
		mStateArray = getResources().getStringArray(R.array.award_state_list);
		mTimeArray = getResources().getStringArray(R.array.time_state_list);
		titleTextView = (TextView) findViewById(R.id.usercenter_mainlayou_text_title);
		titleTextView.setText(R.string.usercenter_bettingDetails);
		mLotnoBtn = (Button) findViewById(R.id.lotno_change_state_title);
		mLotnoBtn.setText(mLotnoArray[0]);
		mAwardStateBtn = (Button) findViewById(R.id.award_change_state_title);
		mAwardStateBtn.setText(mStateArray[0]);
		mTimeBtn = (Button) findViewById(R.id.time_change_state_title);
		mTimeBtn.setText(mTimeArray[0]);
		StateChangeClickListener clickListener = new StateChangeClickListener();
		mLotnoBtn.setOnClickListener(clickListener);
		mAwardStateBtn.setOnClickListener(clickListener);
		mTimeBtn.setOnClickListener(clickListener);
		usecenerLinear = (LinearLayout) findViewById(R.id.usercenterContent);
		usecenerLinear.addView(initLinearView());
	}
	
	private class StateChangeClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			PopOnItemChick popClick = new PopOnItemChick();
			switch (v.getId()) {
			case R.id.lotno_change_state_title:
				mPopupWindow = new CustomPopWindow(BetQueryActivity.this,
						mLotnoArray, 3, popClick, R.id.lotno_change_state_title);
				mPopupWindow.setBackground(R.drawable.inquiry_state_bg_left);
				mPopupWindow.setItemSelect(mCurrentLotnoIndex);
				break;
			case R.id.award_change_state_title:
				mPopupWindow = new CustomPopWindow(BetQueryActivity.this,
						mStateArray, 4, popClick, R.id.award_change_state_title);
				mPopupWindow.setBackground(R.drawable.inquiry_state_bg_center);
				mPopupWindow.setItemSelect(mCurrentAwardStateIndex);
				break;
			case R.id.time_change_state_title:
				mPopupWindow = new CustomPopWindow(BetQueryActivity.this,
						mTimeArray, 4, popClick, R.id.time_change_state_title);
				mPopupWindow.setBackground(R.drawable.inquiry_state_bg_right);
				mPopupWindow.setItemSelect(mCurrentTiemIndex);
				break;
			}
			mPopupWindow.showAsDropDown(v);
		}
	}

	private View initLinearView() {
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewlist = (LinearLayout) inflate.inflate(
				R.layout.usercenter_listview_layout, null);
		queryinfolist = (ListView) viewlist
				.findViewById(R.id.usercenter_listview_queryinfo);
		LayoutInflater mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.lookmorebtn, null);
		progressbar = (ProgressBar) view.findViewById(R.id.getmore_progressbar);
		queryinfolist.addFooterView(view);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				view.setEnabled(false);
				addmore();
			}
		});
		if (mListArray[0] == null) {
			mListArray[0] = new ArrayList();
		}
		initListView(queryinfolist, mListArray[0]);
		return viewlist;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(BetQueryActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				encodejson((String) msg.obj);
				if (getNewPage() == 0) {
					if (dialog != null) {
						dialog.dismiss();
					}
					selecttypelist();
				} else {
					adapter.notifyDataSetChanged();
				}

				break;
			case 2:
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(BetQueryActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				if (mListArray[mCurrentLotnoIndex] != null) {
					mListArray[mCurrentLotnoIndex].clear();
				}
				selecttypelist();
				break;
			case 3:
				if (dialog != null) {
					dialog.dismiss();
				}
				detailsErrorCode(detailJson((BetQueryInfo) msg.obj));

				break;
			}
		}
	};

	private void setNewPage(int page) {
		mPageIndexArray[mCurrentLotnoIndex] = page;
	}

	private int getNewPage() {
		return mPageIndexArray[mCurrentLotnoIndex];
	}

	private void setAllPage(int page) {
		mTotalPageArray[mCurrentLotnoIndex] = page;
	}

	private int getAllPage() {
		return mTotalPageArray[mCurrentLotnoIndex];
	}

	public void selecttypelist() {
		initListView(queryinfolist, mListArray[mCurrentLotnoIndex]);
	}

	public void getInfo() {
		jsonobject = this.getIntent().getStringExtra("betjson");
		if (jsonobject == null || jsonobject.equals("")) {
			Intent intent = getIntent();
			String type = intent.getStringExtra("lotno");
			if (type == null) {
				mCurrentLotnoIndex = 0;
			} else {
				for (int i = 0; i < mLotnoNoArray.length; i++) {
					if(type.equals(mLotnoNoArray[i])){
						mCurrentLotnoIndex = i;
						return;
					}
				}
			}
		} else {
			encodejson(jsonobject);
		}
	}

	/**
	 * 获取投注查询联网
	 * 
	 * @param pageindex
	 */
	Handler thandler = new Handler();

	private void netting(final int pageindex) {
		progressbar.setVisibility(ProgressBar.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				mBetQueryPojo.setPageindex(String.valueOf(pageindex));
				Message msg = handler.obtainMessage();
				jsonString = BetQueryInterface.getInstance().betQuery(
						mBetQueryPojo);
				thandler.post(new Runnable() {

					@Override
					public void run() {
						progressbar.setVisibility(View.INVISIBLE);
						view.setEnabled(true);
					}
				});
				try {
					JSONObject aa = new JSONObject(jsonString);
					String errcode = aa.getString("error_code");
					String message = aa.getString("message");
					if (errcode.equals("0000")) {
						msg.what = 1;
						msg.obj = jsonString;
						setNewPage(pageindex);
						handler.sendMessage(msg);
					}
					if (errcode.equals("0047")) {
						msg = handler.obtainMessage();
						msg.what = 2;
						msg.obj = message;
						handler.sendMessage(msg);
					} else {
						msg = handler.obtainMessage();
						msg.what = 0;
						msg.obj = message;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void getWinDataNet(final int pageindex) {
		showDialog(0);
		netting(pageindex);
	}

	private void addmore() {
		int pageIndex = getNewPage();
		int allpagenum = getAllPage();
		isfirst = false;
		pageIndex++;
		if (pageIndex < allpagenum) {
			netting(pageIndex);
		} else {
			progressbar.setVisibility(View.INVISIBLE);
			pageIndex = allpagenum - 1;
			view.setEnabled(true);
			Toast.makeText(BetQueryActivity.this,
					R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();
		}
	}

	private void initListView(ListView listview, List list) {
		adapter = new BetAdapter(context, list);
		listview.setAdapter(adapter);
	}

	public void encodejson(String json) {
		try {
			JSONObject winprizejsonobj = new JSONObject(json);
			int allPage = Integer.parseInt(winprizejsonobj
					.getString("totalPage"));
			String winprizejsonstring = winprizejsonobj.getString("result");
			setAllPage(allPage);
			JSONArray winprizejson = new JSONArray(winprizejsonstring);
			if (mListArray[mCurrentLotnoIndex] == null) {
				mListArray[mCurrentLotnoIndex] = new ArrayList<BetQueryInfo>();
			}
			if (getNewPage() == 0) {
				mListArray[mCurrentLotnoIndex].clear();
			}
			for (int i = 0; i < winprizejson.length(); i++) {
				try {
					BetQueryInfo betQueryinfo = new BetQueryInfo();
					betQueryinfo.setBatchCode(winprizejson.getJSONObject(i)
							.getString(BATCHCODE));
					betQueryinfo.setOrdertime(winprizejson.getJSONObject(i)
							.getString(ORDERTIME));
					betQueryinfo.setLotNo(winprizejson.getJSONObject(i)
							.getString(LOTNO));
					betQueryinfo.setPrizeAmt(winprizejson.getJSONObject(i)
							.getString(PRIZEAMT));
					betQueryinfo.setLotName(winprizejson.getJSONObject(i)
							.getString(LOTNAME));
					betQueryinfo.setAmount(winprizejson.getJSONObject(i)
							.getString(AMOUNT));
					betQueryinfo.setLotMulti(winprizejson.getJSONObject(i)
							.getString(LOTMUTI));
					betQueryinfo.setBet_code(winprizejson.getJSONObject(i)
							.getString(BET_CODE));
					betQueryinfo.setPrizeState(winprizejson.getJSONObject(i)
							.getString(prizeState));
					if (winprizejson.getJSONObject(i).has(WINCODE)) {
						betQueryinfo.setWin_code(winprizejson.getJSONObject(i)
								.getString(WINCODE));
					}
					betQueryinfo.setRepeatBuy(winprizejson.getJSONObject(i)
							.getBoolean(ISREPEATBUY));
					betQueryinfo.setOrderId(winprizejson.getJSONObject(i)
							.getString("orderId"));
					betQueryinfo.setStateMemo(winprizejson.getJSONObject(i)
							.getString("stateMemo"));
					betQueryinfo.setBetNum(winprizejson.getJSONObject(i)
							.getString("betNum"));
					betQueryinfo.setOneAmount(winprizejson.getJSONObject(i)
							.getString("oneAmount"));
					/**add by pengcx 20130609 start*/
					betQueryinfo.setExpectPrizeAmt(winprizejson.getJSONObject(i)
							.getString("expectPrizeAmt"));
					/**add by pengcx 20130609 end*/
					
					mListArray[mCurrentLotnoIndex].add(betQueryinfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 投注查询的适配器
	 */
	public class BetAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<BetQueryInfo> mList;

		public BetAdapter(Context context, List<BetQueryInfo> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
		}

		public int getCount() {
			return mList.size();
		}

		public Object getItem(int position) {
			return mList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			int isJC = 0;
			// 如果是竞彩则彩种的期号是不能显示的，在这里要加上所有的竞彩彩种编号
			final BetQueryInfo info = mList.get(position);
			final boolean isRepeatBuy = (Boolean) mList.get(position)
					.isRepeatBuy();
			final String lotno = (String) mList.get(position).getLotNo();
			final String prizeqihao = (String) mList.get(position)
					.getBatchCode();
			final String amount = (String) mList.get(position).getAmount();
			final String fPayMoney = "￥" + Long.valueOf(CheckUtil.isNull(amount))/100;
			String payString = getString(R.string.usercenter_winprize_payMoney);// 投注金额字
			SpannableStringBuilder fPayMoneyStringBuilder = new SpannableStringBuilder(
					payString + fPayMoney);
			fPayMoneyStringBuilder.setSpan(new ForegroundColorSpan(
					Color.RED), 3, fPayMoneyStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			final String lotName = (String) mList.get(position).getLotName();
			final String prizemoney = (String) mList.get(position)
					.getPrizeAmt();
			final String prize_State = (String) mList.get(position)
					.getPrizeState();
			/**add by pengcx 20130731 start*/
			final String ordertime = mList.get(position).getOrdertime();
			/**add by pengcx 20130731 end*/
			if (lotno.equals("J00001") || lotno.equals("J00002")
					|| lotno.equals("J00003") || lotno.equals("J00004")
					|| lotno.equals("J00011")
					|| lotno.equals(Constants.LOTNO_JCLQ)
					|| lotno.equals(Constants.LOTNO_JCLQ_DXF)
					|| lotno.equals(Constants.LOTNO_JCLQ_RF)
					|| lotno.equals(Constants.LOTNO_JCLQ_SFC)
					|| lotno.equals(Constants.LOTNO_JCZQ_HUN)
					|| lotno.equals(Constants.LOTNO_JCLQ_HUN)
					|| lotno.equals(Constants.LOTNO_JCZQ_RQSPF)) {
				isJC = 1;
			} else {
				isJC = 0;
			}
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.usercenter_listitem_winprize_query, null);
				holder = new ViewHolder();
				holder.lotteryname = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_lotteryname);
				holder.prizeqihao = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_prizeqihao);
				holder.paymoney = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_paymoney);
				holder.prizemoney = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_prizemoney);
				holder.buyagain = (Button) convertView
						.findViewById(R.id.usercenter_winprize_buyagain);
				holder.lookdetail = (Button) convertView
						.findViewById(R.id.usercenter_winprize_querydetail);
				holder.predictmoney = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_predictmoney);
				/**add by pengcx 20130731 start*/
				holder.orderTime = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_ordertime);
				/**add by pengcx 20130731 end*/
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.prizeqihao.setText("期号:" + prizeqihao);
			noBuyAgain(holder.buyagain, holder.prizeqihao, isRepeatBuy, isJC);
			holder.lotteryname.setText(lotName);
			/**add by pengcx 20130731 start*/
			holder.orderTime.setText("认购时间：" + ordertime);
			/**add by pengcx 20130731 end*/
			holder.paymoney.setText(fPayMoneyStringBuilder);
			if (prize_State.equals("0")) {
				holder.prizemoney.setTextColor(Color.GRAY);
				/*add by pengcx 20130609 start*/
				if (Constants.LOTNO_JCZQ.equals(lotno)
						|| Constants.LOTNO_JCZQ_RQSPF.equals(lotno)
						|| Constants.LOTNO_JCZQ_ZQJ.equals(lotno)
						|| Constants.LOTNO_JCZQ_BF.equals(lotno)
						|| Constants.LOTNO_JCZQ_BF.equals(lotno)
						|| Constants.LOTNO_JCZQ_BQC.equals(lotno)
						|| Constants.LOTNO_JCZQ_HUN.equals(lotno)
						|| Constants.LOTNO_JCLQ.equals(lotno)
						|| Constants.LOTNO_JCLQ_RF.equals(lotno)
						|| Constants.LOTNO_JCLQ_SFC.equals(lotno)
						|| Constants.LOTNO_JCLQ_DXF.equals(lotno)
						|| Constants.LOTNO_JCLQ_HUN.equals(lotno)) {
					holder.predictmoney.setVisibility(View.VISIBLE);
					holder.prizemoney.setVisibility(View.GONE);
					String expectPrizeAmt = info.getExpectPrizeAmt()
							.replaceAll("元", "");
					SpannableStringBuilder fexpectPrizeAmtStringBuilder = new SpannableStringBuilder(
							"预计奖金：￥" + expectPrizeAmt);
					fexpectPrizeAmtStringBuilder.setSpan(
							new ForegroundColorSpan(Color.RED), 5,
							fexpectPrizeAmtStringBuilder.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					holder.predictmoney.setText(fexpectPrizeAmtStringBuilder);
				} else {
					holder.predictmoney.setVisibility(View.GONE);
					holder.prizemoney.setVisibility(View.VISIBLE);
					holder.prizemoney.setText("状态：未开奖");
				}
				/*add by pengcx 20130609 end*/
			} else if (prize_State.equals("3")) {
				holder.prizemoney.setVisibility(View.VISIBLE);
				holder.predictmoney.setVisibility(View.GONE);
				/**modify by yejc 20130418 start*/
//				holder.prizemoney.setTextColor(Color.GRAY);
				holder.prizemoney.setTextColor(getResources().getColor(R.color.bet_query_noaward_text_color));
				/**modify by yejc 20130418 end*/
				holder.prizemoney.setText("状态：未中奖");
			} else {
				holder.prizemoney.setVisibility(View.VISIBLE);
				holder.predictmoney.setVisibility(View.GONE);
				String prizeString = getString(R.string.usercenter_prizeMoney);// 中奖金额字
				String fprizemoney = "￥" + Long.valueOf(CheckUtil.isNull(prizemoney))/100;
				SpannableStringBuilder fprizemoneyStringBuilder = new SpannableStringBuilder(
						 prizeString + fprizemoney);
				fprizemoneyStringBuilder.setSpan(new ForegroundColorSpan(
						Color.RED), 0, fprizemoneyStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				holder.prizemoney.setText(fprizemoneyStringBuilder);
			}
			holder.buyagain.setVisibility(View.GONE);

			holder.lookdetail.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					betQueryDetails(info);
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView lotteryname;
			TextView prizeqihao;
			TextView paymoney;
			TextView prizemoney;
			Button lookdetail;
			Button buyagain;
			
			TextView predictmoney;
			/**add by pengcx 20130731 start*/
			TextView orderTime;
			/**add by pengcx 20130731 end*/
		}
	}

	protected Dialog onCreateDialog(int id) {
		dialog = new ProgressDialog(this);
		switch (id) {
		case DIALOG1_KEY: {
			dialog.setTitle(R.string.usercenter_netDialogTitle);
			dialog.setMessage(getString(R.string.usercenter_netDialogRemind));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return dialog;
	}

	private void noBuyAgain(Button a, TextView qihao, boolean isRepeatBuy,
			int isJC) {
		if (isRepeatBuy == false) {
			if (isJC == 1) {
				qihao.setVisibility(TextView.GONE);
			} else {
				qihao.setVisibility(TextView.VISIBLE);
			}
			a.setEnabled(false);
			a.setBackgroundResource(R.drawable.buyagainunenable);
		} else {
			qihao.setVisibility(TextView.VISIBLE);
			a.setBackgroundResource(R.drawable.usercenter_selector_buyagain);
			a.setEnabled(true);
		}
	}

	/**
	 * 投注详情
	 */
	public void betQueryDetails(final BetQueryInfo betQueryinfo) {
		showDialog(DIALOG1_KEY);
		Thread t = new Thread(new Runnable() {
			String str = "00";

			public void run() {
				str = BetDetailsInterface.getInstance().betDetails(
						betQueryinfo.getOrderId());
				try {
					JSONObject aa = new JSONObject(str);
					String errcode = aa.getString("error_code");
					String message = aa.getString("message");
					Message msg = handler.obtainMessage();
					if (errcode.equals("0000")) {
						betQueryinfo.setJson(str);
						msg.what = 3;
						msg.obj = betQueryinfo;
						handler.sendMessage(msg);
					}
					if (errcode.equals("0047")) {
						msg = handler.obtainMessage();
						msg.what = 2;
						msg.obj = message;
						handler.sendMessage(msg);
					} else {
						msg = handler.obtainMessage();
						msg.what = 0;
						msg.obj = message;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				dialog.dismiss();
			}

		});
		t.start();
	}

	/**
	 * 解析详情json
	 * 
	 * @param betQueryinfo
	 * @return
	 */
	public BetQueryInfo detailJson(BetQueryInfo betQueryinfo) {

		try {
			JSONObject winprizejsonobj = new JSONObject(betQueryinfo.getJson());
			JSONObject winprizejsonstring = winprizejsonobj
					.getJSONObject("result");
			betQueryinfo.setBetCodeHtml(winprizejsonstring
					.getString("betCodeHtml"));
			betQueryinfo.setJson(winprizejsonstring.getString("betCodeJson"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return betQueryinfo;
	}

	/**
	 * 投注查询详情跳转
	 * 
	 * @param betQueryinfo
	 */
	public void detailsErrorCode(BetQueryInfo betQueryinfo) {
		Intent intent = new Intent(BetQueryActivity.this, Betdetail.class);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
			objStream.writeObject(betQueryinfo);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		intent.putExtra("info", byteStream.toByteArray());
		BetQueryActivity.this.startActivity(intent);
	}

	public void errorCode_0000() {}

	public void errorCode_000000() {}

	public Context getContext() {
		return this;
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}
	
	public class PopOnItemChick implements OnChickItem {

		@Override
		public void onChickItem(int position, int type) {
			if (mPopupWindow != null && mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
			switch (type) {
			case R.id.lotno_change_state_title:
				isbetkindall = true;
				mCurrentLotnoIndex = position;
				mLotnoBtn.setText(mLotnoArray[mCurrentLotnoIndex]);
				break;

			case R.id.award_change_state_title:
				mCurrentAwardStateIndex = position;
				mAwardStateBtn.setText(mStateArray[position]);
				break;

			case R.id.time_change_state_title:
				mCurrentTiemIndex = position;
				mTimeBtn.setText(mTimeArray[position]);
				break;
			}
			mBetQueryPojo.setLotno(mLotnoNoArray[mCurrentLotnoIndex]);
			mBetQueryPojo.setAwardType(mAwardStateType[mCurrentAwardStateIndex]);
			mBetQueryPojo.setDateType(mTimeType[mCurrentTiemIndex]);
			if (mListArray[position] == null) {
				mListArray[position] = new ArrayList();
			}
			if ((R.id.lotno_change_state_title == type)
					&& (mListArray[position].size() > 0)) {
				selecttypelist();
			} else {
				getWinDataNet(0);
			}
		}
	}
	
}
