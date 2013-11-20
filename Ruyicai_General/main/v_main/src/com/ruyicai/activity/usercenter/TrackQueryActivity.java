package com.ruyicai.activity.usercenter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.high.HghtOrderdeail;
import com.ruyicai.activity.buy.miss.AddViewMiss;
import com.ruyicai.activity.buy.miss.OrderDetails;
import com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfoMiss;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.activity.usercenter.detail.Trackdetail;
import com.ruyicai.activity.usercenter.info.BetQueryInfo;
import com.ruyicai.activity.usercenter.info.TrackQueryInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.CancelTrackInterface;
import com.ruyicai.net.newtransaction.TrackQueryInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 追号查询页面
 * 
 * @author miao
 */
public class TrackQueryActivity extends InquiryParentActivity implements HandlerMsg {
	/**add by yejc 20130509 start*/
	public static final String FLAG_FROM_TRACK_QUERY = "flag_from_track_query";
	/**add by yejc 20130509 end*/

	final String BETCODE = "betCode", BATCHNUM = "batchNum",
			ORDERTIME = "orderTime", ID = "id", LOTNO = "lotNo",
			LOTNAME = "lotName", AMOUNT = "amount", LASTNUM = "lastNum",
			BEGINBATCH = "beginBatch", STATE = "state",
			ERROR_CODE = "error_code", MESSAGE = "message",
			PRIZEEND = "prizeEnd", ISBUY = "isRepeatBuy",
			BET_CODE = "bet_code", LOTMULTI = "lotMulti",
			ONEAMOUNT = "oneAmount", BETNUM = "betNum";
	private String phonenum, sessionid, userno;
	boolean isCancleTrackNet = false;
	WinPrizeAdapter adapter;
	public static boolean isRefresh = false;
	
	/**
	 * 彩种编号数组
	 */
	private String[] mLotnoNoArray = {"", Constants.LOTNO_SSQ, Constants.LOTNO_FC3D, 
			Constants.LOTNO_11_5,Constants.LOTNO_GD115, Constants.LOTNO_NMK3,
			Constants.LOTNO_DLT, Constants.LOTNO_SSC, Constants.LOTNO_QLC,
			Constants.LOTNO_QXC, Constants.LOTNO_PL3, Constants.LOTNO_PL5, 
			Constants.LOTNO_eleven, Constants.LOTNO_ten, Constants.LOTNO_CQ_ELVEN_FIVE};
	
	protected String[] mStateType = {"","0","3","2"};
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTitleTextView.setText(R.string.usercenter_trackNumberInquiry);
		mLotnoArray = getResources().getStringArray(R.array.track_lotno_list);
		mStateArray = getResources().getStringArray(R.array.track_state_list);
		mAwardStateBtn.setText(mStateArray[0]);
		mLotnoBtn.setText(mLotnoArray[0]);

		// 获取并解析Json数据
		String jsonobject = this.getIntent().getStringExtra("trackjson");
		encodejson(jsonobject);
	}
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(TrackQueryActivity.this, (String) msg.obj,
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
				Toast.makeText(TrackQueryActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				cancleTrackError000000();
				break;
				
			case 3:
				Toast.makeText(TrackQueryActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				if (mListArray[mCurrentLotnoIndex] != null) {
					mListArray[mCurrentLotnoIndex].clear();
				}
				initListView();
				break;
			}
			dismiss();
		}
	};

	private void initPojo() {
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		phonenum = shellRW.getStringValue("phonenum");
		sessionid = shellRW.getStringValue("sessionid");
		userno = shellRW.getStringValue("userno");
	}

	final Handler tHandler = new Handler();

	@Override
	protected void netting(final int pageindex) {
		tHandler.post(new Runnable() {
			@Override
			public void run() {
				mProgressbar.setVisibility(ProgressBar.VISIBLE);
			}
		});

		new Thread(new Runnable() {
			@Override
			public void run() {
				initPojo();
				BetAndWinAndTrackAndGiftQueryPojo winQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
				winQueryPojo.setUserno(userno);
				winQueryPojo.setSessionid(sessionid);
				winQueryPojo.setPhonenum(phonenum);
				winQueryPojo.setPageindex(String.valueOf(pageindex));
				winQueryPojo.setMaxresult("10");
				winQueryPojo.setType("track");
				winQueryPojo.setLotno(mLotnoNoArray[mCurrentLotnoIndex]);
				winQueryPojo.setState(mStateType[mCurrentAwardStateIndex]);
				winQueryPojo.setDateType(mTimeType[mCurrentTiemIndex]);
				isCancleTrackNet = false;
				Message msg = new Message();
				String jsonString = TrackQueryInterface.getInstance().trackQuery(
						winQueryPojo);
				tHandler.post(new Runnable() {
					@Override
					public void run() {
						mProgressbar.setVisibility(ProgressBar.INVISIBLE);
						mView.setEnabled(true);
					}
				});
				try {
					JSONObject aa = new JSONObject(jsonString);
					String errcode = aa.getString(ERROR_CODE);
					String message = aa.getString(MESSAGE);
					if (errcode.equals("0000")) {
						setNewPage(pageindex);
						msg.what = 1;
						msg.obj = jsonString;
						handler.sendMessage(msg);
					} else if (errcode.equals("0047")) {
						msg.what = 3;
						msg.obj = message;
						handler.sendMessage(msg);
					} else {
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

	private void trackQueryNet(final int pageindex) {
		isRefresh = false;
		showDialog(DIALOG1_KEY);
		new Thread(new Runnable() {
			public void run() {
				netting(pageindex);
			}
		}).start();
	}

	private AlertDialog cancleTrackDialog(final String id) {
		LayoutInflater factory = LayoutInflater.from(this);
		/* 中奖查询的查看详情使用余额查询的布局 */
		View view = factory.inflate(R.layout.usercenter_balancequery, null);
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		TextView title = (TextView) view
				.findViewById(R.id.usercenter_balancequery_title);
		TextView remind = (TextView) view
				.findViewById(R.id.usercenter_remind_text);
		remind.setVisibility(TextView.GONE);
		title.setText(R.string.cancel_add_num);
		TextView detailTextView = (TextView) view
				.findViewById(R.id.balanceinfo);
		detailTextView.setTextSize(18);
		detailTextView.setText(R.string.usercenter_cancleTrackRemind);
		Button cancleLook = (Button) view
				.findViewById(R.id.usercenter_balancequery_back);
		cancleLook.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		Button okBtn = (Button) view
				.findViewById(R.id.usercenter_balancequery_ok);
		okBtn.setVisibility(Button.VISIBLE);
		okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				cancleTrackNet(id);
			}
		});
		dialog.show();
		dialog.getWindow().setContentView(view);
		return dialog;
	}

	/**
	 * 取消追号联网
	 * 
	 * @param tsubscribeid
	 *            追号记录id
	 */
	private void cancleTrackNet(final String tsubscribeNo) {
		showDialog(0);
		new Thread(new Runnable() {
			public void run() {
				initPojo();
				Message msg = new Message();
				String cancleTrackBack = CancelTrackInterface.getInstance()
						.canceltrack(userno, sessionid, tsubscribeNo, phonenum);
				try {
					JSONObject cancleTrackReturn = new JSONObject(
							cancleTrackBack);
					String errorCode = cancleTrackReturn.getString(ERROR_CODE);
					String message = cancleTrackReturn.getString(MESSAGE);
					if (errorCode.equals("0000")) {
						msg.what = 2;
						msg.obj = message;
						handler.sendMessage(msg);
					} else {
						msg.what = 0;
						msg.obj = message;
						handler.sendMessage(msg);
					}
					;
				} catch (JSONException e) {
				}
			}
		}).start();
	}

	/**
	 * 解析Json数据
	 * 
	 * @param json
	 *            Json字符串
	 */
	public void encodejson(String json) {
		try {
			JSONObject winprizejsonobj = new JSONObject(json);
			int allPage = Integer.parseInt(winprizejsonobj.getString("totalPage"));
			setAllPage(allPage);
			String winprizejsonstring = winprizejsonobj.getString("result");
			JSONArray winprizejson = new JSONArray(winprizejsonstring);
			if (mListArray[mCurrentLotnoIndex] == null) {
				mListArray[mCurrentLotnoIndex] = new ArrayList<BetQueryInfo>();
			}
			if (getNewPage() == 0) {
				mListArray[mCurrentLotnoIndex].clear();
			}
			for (int i = 0; i < winprizejson.length(); i++) {
				try {
					TrackQueryInfo winPrizeQueryinfo = new TrackQueryInfo();
					winPrizeQueryinfo.setBetCode(winprizejson.getJSONObject(i)
							.getString(BETCODE));
					winPrizeQueryinfo.setAmount(winprizejson.getJSONObject(i)
							.getString(AMOUNT));
					winPrizeQueryinfo.setState(winprizejson.getJSONObject(i)
							.getString(STATE));
					winPrizeQueryinfo.setBatchNum(winprizejson.getJSONObject(i)
							.getString(BATCHNUM));
					winPrizeQueryinfo.setLotName(winprizejson.getJSONObject(i)
							.getString(LOTNAME));
					winPrizeQueryinfo.setOrderTime(winprizejson
							.getJSONObject(i).getString(ORDERTIME));
					winPrizeQueryinfo.setBeginBatch(winprizejson.getJSONObject(
							i).getString(BEGINBATCH));
					winPrizeQueryinfo.setLastNums(winprizejson.getJSONObject(i)
							.getString(LASTNUM));
					winPrizeQueryinfo.setId(winprizejson.getJSONObject(i)
							.getString(ID));
					winPrizeQueryinfo.setPrizeEnd(winprizejson.getJSONObject(i)
							.getString(PRIZEEND));
					winPrizeQueryinfo.setLotno(winprizejson.getJSONObject(i)
							.getString(LOTNO));
					winPrizeQueryinfo.setIsRepeatBuy(winprizejson
							.getJSONObject(i).getString(ISBUY));
					winPrizeQueryinfo.setBetTouCode(winprizejson.getJSONObject(
							i).getString(BET_CODE));
					winPrizeQueryinfo.setLotMulti(winprizejson.getJSONObject(i)
							.getString(LOTMULTI));
					winPrizeQueryinfo.setOneAmount(winprizejson
							.getJSONObject(i).getString(ONEAMOUNT));
					winPrizeQueryinfo.setBetNum(winprizejson.getJSONObject(i)
							.getString(BETNUM));
					winPrizeQueryinfo.setRemainderAmount(winprizejson
							.getJSONObject(i).getString("remainderAmount"));
					mListArray[mCurrentLotnoIndex].add(winPrizeQueryinfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 追号查询列表适配器
	 */
	public class WinPrizeAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<TrackQueryInfo> mList;

		public WinPrizeAdapter(Context context, List<TrackQueryInfo> list) {
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
			final TrackQueryInfo info = mList.get(position);
			final String lotName = (String) mList.get(position).getLotName();
			final String betAmount = (String) mList.get(position).getAmount();
			final String trackState = (String) mList.get(position).getState();
			final String batchNums = (String) mList.get(position).getBatchNum();
			final String lastNums = (String) mList.get(position).getLastNums();
			final String trackId = (String) mList.get(position).getId();
			/**add by pengcx 20130731 start*/
			final String orderTime = mList.get(position).getOrderTime();
			/**add by pengcx 20130731 end*/
			String prizeEnd = (String) mList.get(position).getPrizeEnd();
			final String isPrizeEnd;

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.usercenter_trackquery_listitem, null);
				holder = new ViewHolder();

				// 彩种名称
				holder.lotName = (TextView) convertView
						.findViewById(R.id.usercenter_trackquery_lotteryname);
				// 追号状态
				holder.trackState = (TextView) convertView
						.findViewById(R.id.usercenter_trackquery_trackstate);
				// 追号期数
				holder.batchNums = (TextView) convertView
						.findViewById(R.id.usercenter_trackquery_tracknum);
				// 追号金额
				holder.trackAmount = (TextView) convertView
						.findViewById(R.id.usercenter_trackquery_money);
				// 查看详情按钮
				holder.lookdetail = (Button) convertView
						.findViewById(R.id.usercenter_trackquery_lookdetail);
				// 取消追号按钮
				holder.cancleTrack = (Button) convertView
						.findViewById(R.id.usercenter_trackquery_cancle);
				/**add by pengcx 20130731 start*/
				//发起时间
				holder.orderTime = (TextView) convertView.findViewById(R.id.usercenter_trackquery_ordertime);
				/**add by pengcx 20130731 end*/
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (prizeEnd.equals("0")) {
				isPrizeEnd = "中奖后停止：否";
			} else {
				isPrizeEnd = "中奖后停止：是";
			}

			holder.lotName.setText(lotName);

			final String lastBatch = ""
					+ (Integer.valueOf(CheckUtil.isNull(batchNums)) - Integer.valueOf(CheckUtil.isNull(lastNums)));
			trackState(holder.trackState, trackState, lastBatch);
			cancleTrackVisible(holder.cancleTrack, trackState, lastBatch,
					trackId, mList.get(position));

			holder.trackAmount.setText(PublicMethod.formatMoney(betAmount));
			/**add by pengcx 20130731 start*/
			holder.orderTime.setText(orderTime);
			/**add by pengcx 20130731 end*/
			// 设置追号期数和已追期数内容
			holder.batchNums.setText(lastNums + "/" + batchNums);

			holder.lookdetail.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(TrackQueryActivity.this,
							Trackdetail.class);
					ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
					try {
						ObjectOutputStream objStream = new ObjectOutputStream(
								byteStream);
						objStream.writeObject(info);
					} catch (IOException e) {
						return;
					}
					intent.putExtra("info", byteStream.toByteArray());
					TrackQueryActivity.this.startActivity(intent);
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView lotName;
			TextView trackState;
			TextView batchNums;
			TextView batchNumed;
			TextView trackAmount;
			/**add by pengcx 20130731 start*/
			TextView orderTime;
			/**add by pengcx 20130731 end*/
			Button cancleTrack;
			Button lookdetail;
		}
	}

	/**
	 * 格式化state串，将state串设置颜色付值给TextView
	 * 
	 * @param text
	 *            TextView
	 * @param state
	 */
	private void trackState(TextView text, String state, String lastnum) {
		int stateInt = 0;
		stateInt = Integer.parseInt(state);
		int StringId = 0;
		switch (stateInt) {
		case 0:
			if (lastnum.equals("0")) {
				StringId = R.string.usercenter_str_hasClosed;
				text.setTextColor(0xffd4d4d4);
			} else {
				StringId = R.string.usercenter_str_running;
				text.setTextColor(0xff006600);
			}
			break;
		case 2:

			StringId = R.string.usercenter_str_hasCancled;
			text.setTextColor(0xffcc0000);
			break;
		case 3:
			StringId = R.string.usercenter_str_hasClosed;
			text.setTextColor(0xffd4d4d4);
			break;
		}
		text.setText(StringId);
	}

	private void cancleTrackVisible(Button btn, String state, String lastnum,
			final String trackId, TrackQueryInfo info) {
		int stateInt = 0;
		stateInt = Integer.parseInt(state);
		switch (stateInt) {
		case 0:
			if (lastnum.equals("0")) {
				setContinueBtn(btn, info);
			} else {
				btn.setBackgroundResource(R.drawable.usercenter_cancletrack_selector);
				btn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						cancleTrackDialog(trackId);
					}
				});
			}
			break;
		case 2:
			setContinueBtn(btn, info);
			break;
		case 3:
			setContinueBtn(btn, info);
			break;
		}
	}

	private void setContinueBtn(Button btn, final TrackQueryInfo info) {
		if (info.getIsRepeatBuy().equals("true")) {
			btn.setBackgroundResource(R.drawable.user_continue_issue_selector);
			btn.setClickable(true);
			btn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					/**add by yejc 20130510 start*/
					ApplicationAddview app = (ApplicationAddview) getApplicationContext();
					String amount = Integer.parseInt(info.getOneAmount()) / 100
							* Integer.parseInt(info.getBetNum()) + "00";
					
					initBetPojo("1", "1", amount, info);
					app.setPojo(betPojo);
					Intent intent = null;
					if (Constants.LOTNO_SSC.equals(betPojo.getLotno())
							|| Constants.LOTNO_11_5.equals(betPojo.getLotno())
							|| Constants.LOTNO_eleven.equals(betPojo.getLotno())
							|| Constants.LOTNO_GD_11_5.equals(betPojo.getLotno())
							|| Constants.LOTNO_ten.equals(betPojo.getLotno())
							||Constants.LOTNO_NMK3.equals(betPojo.getLotno())) {
						AddView addView = new AddView(TrackQueryActivity.this);
						addView.addCodeInfo(addView.initCodeInfo(2, 1));
						app.setAddview(addView);
						intent = new Intent(TrackQueryActivity.this, HghtOrderdeail.class);
					} else {
						AddViewMiss addViewMiss = new AddViewMiss(TrackQueryActivity.this);
						CodeInfoMiss codeInfo = addViewMiss.initCodeInfo(2, 1);
						addViewMiss.addCodeInfo(codeInfo);
						app.setAddviewmiss(addViewMiss);
						intent = new Intent(TrackQueryActivity.this, OrderDetails.class);
					}
					
					intent.putExtra("position", 1);
					intent.putExtra(FLAG_FROM_TRACK_QUERY, true);
					startActivity(intent);
					/**add by yejc 20130510 end*/
				}
			});
		} else {
			btn.setBackgroundResource(R.drawable.btn_qxzq_stop);
			btn.setFocusable(false); //add by yejc 20130903
			btn.setClickable(false);
		}
	}

	private BetAndGiftPojo betPojo = new BetAndGiftPojo();

	private void initBetPojo(String issue, String lotMulti,
			String amount, TrackQueryInfo info) {
		initPojo();
		betPojo.setPhonenum(phonenum);
		betPojo.setSessionid(sessionid);
		betPojo.setUserno(userno);
		betPojo.setBet_code(info.getBetTouCode());
		betPojo.setLotno(info.getLotno());
		betPojo.setBatchnum(issue);
		betPojo.setLotmulti(lotMulti);
		betPojo.setBettype("bet");
		betPojo.setAmount(amount);
		betPojo.setAmt(2);
		betPojo.setIsSellWays("1");
		betPojo.setOneAmount(info.getOneAmount());
		betPojo.setZhushu(info.getBetNum());
		if (Constants.LOTNO_DLT.equals(info.getLotno())) {
			betPojo.setZhui(true);
		} else {
			betPojo.setZhui(false);
		}
	}

	/**
	 * 继续追号对话框
	 */
	public void createContinueInfo(TrackQueryInfo info, String issueNum,
			String issue, String beishu) {
		int oneAmt = Integer.parseInt(info.getOneAmount()) / 100
				* Integer.parseInt(info.getBetNum()) * Integer.parseInt(beishu);
		int amount = oneAmt * PublicMethod.toInt(issueNum);
		StringBuffer continueStr = new StringBuffer();
		continueStr.append(
				getString(R.string.usercenter_winningCheck_lotteryCategory))
				.append(info.getLotName() + "\n\n");
		continueStr.append(getString(R.string.usercenter_trackbatchnums))
				.append(issueNum + "期\n\n");
		continueStr.append(getString(R.string.continue_start_issue)).append(
				issue + "期\n\n");
		continueStr.append(getString(R.string.continue_amt)).append(
				amount + "元\n\n");
		initBetPojo(issueNum, beishu, oneAmt + "00", info);// 初始化投注信息
		continueInfoDialog("\n" + continueStr, issue);
	}

	AlertDialog continueDialogInfo;

	private AlertDialog continueInfoDialog(String detail, final String issue) {
		LayoutInflater factory = LayoutInflater.from(this);
		/* 中奖查询的查看详情使用余额查询的布局 */
		View view = factory.inflate(R.layout.usercenter_zhuihaodingdan, null);
		continueDialogInfo = new AlertDialog.Builder(this).create();
		TextView title = (TextView) view
				.findViewById(R.id.usercenter_balancequery_title);
		TextView remind = (TextView) view
				.findViewById(R.id.usercenter_remind_text);
		Button lookDail = (Button) view
				.findViewById(R.id.usercenter_balancequery_ok);
		Button cancleLook = (Button) view
				.findViewById(R.id.usercenter_balancequery_back);
		TextView detailTextView = (TextView) view
				.findViewById(R.id.balanceinfo);

		remind.setVisibility(TextView.GONE);
		title.setText(R.string.continue_title);
		detailTextView.setText(detail);
		cancleLook.setText(getString(R.string.cancel));
		lookDail.setText(getString(R.string.ok));
		cancleLook.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				continueDialogInfo.cancel();
			}
		});
		lookDail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				continueDialogInfo.cancel();
				betPojo.setBatchcode(issue);
				touZhuNet();
			}
		});
		continueDialogInfo.show();
		continueDialogInfo.getWindow().setContentView(view);
		return continueDialogInfo;
	}

	/**
	 * 投注联网
	 */
	public void touZhuNet() {
		showDialog(DIALOG1_KEY);
		Thread t = new Thread(new Runnable() {
			String str = "00";

			public void run() {
				str = BetAndGiftInterface.getInstance().betOrGift(betPojo);
				try {
					JSONObject obj = new JSONObject(str);
					String message = obj.getString("message");
					String error = obj.getString("error_code");
					Message msg = new Message();
					if (error.equals("0000")) {
						msg.what = 2;
						msg.obj = message;
						handler.sendMessage(msg);
					} else {
						msg.what = 0;
						msg.obj = message;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	/**
	 * 如果取消追号返回的是“000000”时的处理方法
	 */
	private void cancleTrackError000000() {
		isCancleTrackNet = true;
		setNewPage(0);
		trackQueryNet(0);
	}

	@Override
	public void errorCode_0000() {
		Toast.makeText(this, R.string.betSuccess, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void errorCode_000000() {

	}

	@Override
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
		if (isRefresh) {
			cancleTrackError000000();
		}
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void initListView() {
		adapter = new WinPrizeAdapter(this, mListArray[mCurrentLotnoIndex]);
		mListView.setAdapter(adapter);
	}
}
