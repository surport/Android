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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.detail.Betdetail;
import com.ruyicai.activity.usercenter.info.BetQueryInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetDetailsInterface;
import com.ruyicai.net.newtransaction.BetQueryInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.CheckUtil;
import com.umeng.analytics.MobclickAgent;
import com.umpay.creditcard.android.cu;

/**
 * 投注查询联网
 * 
 * @author Administrator
 * 
 */
public class BetQueryActivity extends InquiryParentActivity implements HandlerMsg {
	final String BATCHCODE = "batchCode", LOTMUTI = "lotMulti",
			ORDERTIME = "orderTime", PRIZEAMT = "prizeAmt",
			prizeState = "prizeState", WINCODE = "winCode",
			BETCODE = "betCode", LOTNO = "lotNo", AMOUNT = "amount",
			LOTNAME = "lotName", PLAY = "play", BET_CODE = "orderInfo";
	final String ISREPEATBUY = "isRepeatBuy";
	MyHandler touzhuhandler = new MyHandler(this);
	BetAdapter adapter;
	
	/**
	 * 彩种编号数组
	 */
	private String[] mLotnoNoArray = {"", Constants.LOTNO_SSQ, Constants.LOTNO_FC3D, 
			Constants.LOTNO_11_5,Constants.LOTNO_GD115, Constants.LOTNO_NMK3,
			Constants.LOTNO_DLT, Constants.LOTNO_SSC, Constants.LOTNO_QLC,
			Constants.LOTNO_QXC, Constants.LOTNO_PL3, Constants.LOTNO_PL5, 
			Constants.LOTNO_eleven, Constants.LOTNO_ten, Constants.LOTNO_ZC, 
			Constants.LOTNO_JCL, Constants.LOTNO_JCZ, Constants.LOTNO_BJ_SINGLE,
			Constants.LOTNO_CQ_ELVEN_FIVE};
	
	/**
	 * 中奖状态查询请求参数数组
	 */
	protected String[] mAwardStateType = {"0","1","2","3","4"};

	/**
	 * 创建pojo类实例存放请求参数
	 */
	BetAndWinAndTrackAndGiftQueryPojo mBetQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBetQueryPojo.setUserno(mUserNo);
		mBetQueryPojo.setMaxresult("10");
		mBetQueryPojo.setType("betList");
		getInfo();
		mTitleTextView.setText(R.string.usercenter_bettingDetails);
		mLotnoArray = getResources().getStringArray(R.array.lotno_list);
		mStateArray = getResources().getStringArray(R.array.award_state_list);
		mAwardStateBtn.setText(mStateArray[0]);
		mLotnoBtn.setText(mLotnoArray[0]);
	}

	protected void onStart() {
		super.onStart();
		mLotnoBtn.setText(mLotnoArray[mCurrentLotnoIndex]);
	};
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(BetQueryActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				encodejson((String) msg.obj);
				if (getNewPage() == 0) {
					initListView();
				} else {
					adapter.notifyDataSetChanged();
				}

				break;
			case 2:
				Toast.makeText(BetQueryActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				if (mListArray[mCurrentLotnoIndex] != null) {
					mListArray[mCurrentLotnoIndex].clear();
				}
				initListView();
				break;
			case 3:
				detailsErrorCode(detailJson((BetQueryInfo) msg.obj));
				break;
			}
			dismiss();
		}
	};

	public void getInfo() {
		String jsonobject = this.getIntent().getStringExtra("betjson");
		if (jsonobject == null || jsonobject.equals("")) {
			Intent intent = getIntent();
			String type = intent.getStringExtra("lotno");
			if (type == null) {
				mCurrentLotnoIndex = 0;
			} else {
				for (int i = 0; i < mLotnoNoArray.length; i++) {
					if(type.equals(mLotnoNoArray[i])){
						mCurrentLotnoIndex = i;
						getData(0);
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

	@Override
	protected void netting(final int pageindex) {
		mProgressbar.setVisibility(ProgressBar.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				mBetQueryPojo.setLotno(mLotnoNoArray[mCurrentLotnoIndex]);
				mBetQueryPojo.setState(mAwardStateType[mCurrentAwardStateIndex]);
				mBetQueryPojo.setDateType(mTimeType[mCurrentTiemIndex]);
				mBetQueryPojo.setPageindex(String.valueOf(pageindex));
				Message msg = handler.obtainMessage();
				String jsonString = BetQueryInterface.getInstance().betQuery(
						mBetQueryPojo);
				thandler.post(new Runnable() {

					@Override
					public void run() {
						mProgressbar.setVisibility(View.INVISIBLE);
						mView.setEnabled(true);
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
			final String fPayMoney = "￥" + String.format("%.2f", Double.valueOf(CheckUtil.isNull(amount))/100);
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
					holder.prizemoney.setVisibility(View.VISIBLE);
					holder.prizemoney.setText("状态：未开奖");
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
				mProgressDialog.dismiss();
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
	
//	@Override
//	protected void getData(int type) {
//		if (mListArray[mCurrentLotnoIndex] == null) {
//			mListArray[mCurrentLotnoIndex] = new ArrayList();
//		}
//		if ((R.id.lotno_change_state_title == type)
//				&& (mListArray[mCurrentLotnoIndex].size() > 0)) {
//			initListView();
//		} else {
//			showDialog(0);
//			netting(0);
//		}
//	}

	@Override
	protected void initListView() {
		adapter = new BetAdapter(this, mListArray[mCurrentLotnoIndex]);
		mListView.setAdapter(adapter);
	}
	
}
