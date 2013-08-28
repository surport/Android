package com.ruyicai.activity.usercenter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.DigitsKeyListener;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.high.HghtOrderdeail;
import com.ruyicai.activity.buy.miss.AddViewMiss;
import com.ruyicai.activity.buy.miss.OrderDetails;
import com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfo;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.activity.usercenter.detail.Trackdetail;
import com.ruyicai.activity.usercenter.info.TrackQueryInfo;
import com.ruyicai.activity.usercenter.info.TrackQueryInfo2;
import com.ruyicai.code.ssq.SsqZiZhiXuanCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.CancelTrackInterface;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.net.newtransaction.GiftQueryInterface;
import com.ruyicai.net.newtransaction.SoftwareUpdateInterface;
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
public class TrackQueryActivity extends Activity implements HandlerMsg {

	private LinearLayout usecenerLinear;
	private Button returnButton;
	private TextView titleTextView;
	/**add by yejc 20130509 start*/
	public static final String FLAG_FROM_TRACK_QUERY = "flag_from_track_query";
	/**add by yejc 20130509 end*/

	String jsonString;
	String jsontrack;
	final String BETCODE = "betCode", BATCHNUM = "batchNum",
			ORDERTIME = "orderTime", ID = "id", LOTNO = "lotNo",
			LOTNAME = "lotName", AMOUNT = "amount", LASTNUM = "lastNum",
			BEGINBATCH = "beginBatch", STATE = "state",
			ERROR_CODE = "error_code", MESSAGE = "message",
			PRIZEEND = "prizeEnd", ISBUY = "isRepeatBuy",
			BET_CODE = "bet_code", LOTMULTI = "lotMulti",
			ONEAMOUNT = "oneAmount", BETNUM = "betNum";
	private final int DIALOG1_KEY = 0;
	AlertDialog remindCancleDialog;
	private String phonenum, sessionid, userno;
	List<TrackQueryInfo> windatalist = new ArrayList<TrackQueryInfo>();
	List<TrackQueryInfo2> tracklist = new ArrayList<TrackQueryInfo2>();

	Context context = this;
	ProgressDialog dialog;
	String jsonobject;
	int allPage;
	int pageindex;
	ListView queryinfolist;
	boolean isfirst = false, isCancleTrackNet = false;
	WinPrizeAdapter adapter;
	View view;
	ProgressBar progressbar;
	private MyHandler touzhuhandler = new MyHandler(this);
	private Dialog continueDialog = null;
	public static boolean isRefresh = false;
	private final int MAX_AMT = 10000000;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(TrackQueryActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				if (dialog != null) {
					dialog.dismiss();
				}
				encodejson((String) msg.obj);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(TrackQueryActivity.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				cancleTrackError000000();
				break;
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_mainlayoutold);

		returnButton = (Button) findViewById(R.id.layout_usercenter_img_return);
		returnButton.setBackgroundResource(R.drawable.returnselecter);
		returnButton.setText(R.string.returnlastpage);
		initReturn();

		// 设置追号查询
		titleTextView = (TextView) findViewById(R.id.usercenter_mainlayou_text_title);
		titleTextView.setText(R.string.usercenter_trackNumberInquiry);

		// 获取并解析Json数据
		jsonobject = this.getIntent().getStringExtra("trackjson");
		encodejson(jsonobject);

		isfirst = true;
		initLinear();
	}

	protected void initReturn() {
		returnButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initPojo() {
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		phonenum = shellRW.getStringValue("phonenum");
		sessionid = shellRW.getStringValue("sessionid");
		userno = shellRW.getStringValue("userno");
	}

	final Handler tHandler = new Handler();

	private void netting(final int pageindex) {
		tHandler.post(new Runnable() {
			@Override
			public void run() {
				progressbar.setVisibility(ProgressBar.VISIBLE);
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
				isCancleTrackNet = false;
				Message msg = new Message();
				jsonString = GiftQueryInterface.getInstance().giftQuery(
						winQueryPojo);
				tHandler.post(new Runnable() {
					@Override
					public void run() {
						progressbar.setVisibility(ProgressBar.INVISIBLE);
						view.setEnabled(true);
					}
				});
				try {
					JSONObject aa = new JSONObject(jsonString);
					String errcode = aa.getString(ERROR_CODE);
					String message = aa.getString(MESSAGE);
					if (errcode.equals("0000")) {
						msg.what = 1;
						msg.obj = jsonString;
						handler.sendMessage(msg);
					} else {
						msg.what = 0;
						msg.obj = message;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	private void nettingContinue(final int pageindex) {
		tHandler.post(new Runnable() {
			@Override
			public void run() {
				progressbar.setVisibility(ProgressBar.VISIBLE);
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
				isCancleTrackNet = false;
				Message msg = new Message();
				jsonString = GiftQueryInterface.getInstance().giftQuery(
						winQueryPojo);
				tHandler.post(new Runnable() {
					@Override
					public void run() {
						progressbar.setVisibility(ProgressBar.INVISIBLE);
						view.setEnabled(true);
					}
				});
				try {
					JSONObject aa = new JSONObject(jsonString);
					String errcode = aa.getString(ERROR_CODE);
					String message = aa.getString(MESSAGE);
					if (errcode.equals("0000")) {
						windatalist.clear();
						msg.what = 1;
						msg.obj = jsonString;
						handler.sendMessage(msg);
					} else {
						msg.what = 0;
						msg.obj = message;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	private void trackQueryNet(final int pageindex) {
		isRefresh = false;
		showDialog(DIALOG1_KEY);
		new Thread(new Runnable() {
			public void run() {
				nettingContinue(pageindex);
			}
		}).start();
	}

	private void initLinear() {
		// 向用户中心的内容布局中添加具体内容视图
		usecenerLinear = (LinearLayout) findViewById(R.id.usercenterContent);
		usecenerLinear.addView(initLinearView());
	}

	/**
	 * 初始化布局内容的显示
	 * 
	 * @return 内容布局视图
	 */
	private View initLinearView() {
		// 从内容布局中获取ListView，并初始化ListView的显示
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (LinearLayout) inflate.inflate(
				R.layout.usercenter_listview_layout, null);
		queryinfolist = (ListView) view
				.findViewById(R.id.usercenter_listview_queryinfo);
		initListView(queryinfolist, windatalist);

		return view;
	}

	/**
	 * 初始化列表
	 */
	private void addmore() {
		isfirst = false;
		pageindex++;
		if (pageindex < allPage) {

			netting(pageindex);
		} else {
			pageindex = allPage - 1;
			progressbar.setVisibility(view.INVISIBLE);
			queryinfolist.removeFooterView(view);
			Toast.makeText(TrackQueryActivity.this,
					R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();
		}
	}

	public void initList() {
		initListView(queryinfolist, windatalist);
	}

	/**
	 * 初始化内容列表的显示
	 * 
	 * @param listview
	 *            ListView对象
	 * @param list
	 */
	private void initListView(ListView listview, List list) {
		adapter = new WinPrizeAdapter(this, windatalist);
		LayoutInflater mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.lookmorebtn, null);
		progressbar = (ProgressBar) view.findViewById(R.id.getmore_progressbar);
		listview.addFooterView(view);
		// 数据源
		listview.setAdapter(adapter);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				view.setEnabled(false);
				addmore();

			}
		});
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
			allPage = Integer.parseInt(winprizejsonobj.getString("totalPage"));
			String winprizejsonstring = winprizejsonobj.getString("result");
			JSONArray winprizejson = new JSONArray(winprizejsonstring);
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
					windatalist.add(winPrizeQueryinfo);
				} catch (Exception e) {
				}
			}
		} catch (JSONException e) {
			try {
				JSONObject winprizejson = new JSONObject(json);
			} catch (JSONException e1) {
			}
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
						CodeInfo codeInfo = addViewMiss.initCodeInfo(2, 1);
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
		}
	}

	private BetAndGiftPojo betPojo = new BetAndGiftPojo();

	private void initBetPojo(/*String zhuma, */String issue, String lotMulti,
			/*String lotno,*/ String amount, /*String oneAmount,*/ TrackQueryInfo info) {
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
		initBetPojo(/*info.getBetTouCode(), */issueNum, beishu, /*info.getLotno(),*/
				oneAmt + "00", /*info.getOneAmount(),*/ info);// 初始化投注信息
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
					;

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
		t.start();
	}

	/**
	 * 格式化state串，将state串设置颜色付值给TextView
	 * 
	 * @param text
	 *            TextView
	 * @param state
	 */
	private String formatTrackState(String state, String lastnum) {
		String formatState = "";
		int stateInt = 0;
		stateInt = Integer.parseInt(state);
		switch (stateInt) {
		case 0:
			if (lastnum.equals("0")) {
				formatState = getString(R.string.usercenter_str_hasClosedNoParentheses);
			} else {
				formatState = this
						.getString(R.string.usercenter_str_runningNoParentheses);
			}
			break;
		case 2:
			formatState = getString(R.string.usercenter_str_hasCancledNoParentheses);
			break;
		case 3:
			formatState = getString(R.string.usercenter_str_hasClosedNoParentheses);
			break;
		}
		return formatState;
	}

	/**
	 * 如果取消追号返回的是“000000”时的处理方法
	 */
	private void cancleTrackError000000() {
		isCancleTrackNet = true;
		pageindex = 0;
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
}
