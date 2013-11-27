package com.ruyicai.activity.usercenter.detail;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.more.ActionActivity;
import com.ruyicai.activity.usercenter.TrackQueryActivity;
import com.ruyicai.activity.usercenter.info.TrackQueryInfo;
import com.ruyicai.activity.usercenter.info.TrackQueryInfo2;
import com.ruyicai.net.newtransaction.CancelTrackInterface;
import com.ruyicai.net.newtransaction.TrackDailInterface;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

public class Trackdetail extends ActionActivity {
	TrackQueryInfo info;
	List<TrackQueryInfo2> tracklist = new ArrayList<TrackQueryInfo2>();
	private String phonenum, sessionid, userno;
	ProgressDialog dialog;
	String jsonString;
	String jsontrack;
	private boolean isfaqi = false, isxiangqing = false, isleirong = false,
			isrengou = false, iscanyu = true;
	final String BETCODE = "betCode", BATCHNUM = "batchNum",
			ORDERTIME = "orderTime", ID = "id", LOTNO = "lotNo",
			LOTNAME = "lotName", AMOUNT = "amount", LASTNUM = "lastNum",
			BEGINBATCH = "beginBatch", STATE = "state",
			ERROR_CODE = "error_code", MESSAGE = "message",
			PRIZEEND = "prizeEnd", ISBUY = "isRepeatBuy",
			BET_CODE = "bet_code", LOTMULTI = "lotMulti";
	boolean hansdes = false;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 3:
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(Trackdetail.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				tracklist = encodejsontrack(jsontrack);
				lookDetail(tracklist);
				break;
			case 2:
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(Trackdetail.this, (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				finish();
				TrackQueryActivity.isRefresh = true;

			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.track_detail);
		getInfo();
		init();
		initButtonLayout();
	}

	/**
	 * 从上一个页面获取信息
	 */
	public void getInfo() {
		Intent intent = getIntent();
		byte[] bytes = intent.getByteArrayExtra("info");
		if (bytes != null) {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
			try {
				ObjectInputStream objStream = new ObjectInputStream(byteStream);
				info = (TrackQueryInfo) objStream.readObject();
			} catch (Exception e) {
			}
		}

	}

	void init() {
		TextView lotnametext = (TextView) findViewById(R.id.track_detail_text_lotno);
		TextView id = (TextView) findViewById(R.id.track_detail_text_id);
		TextView trackqitext = (TextView) findViewById(R.id.track_detail_text_qishu);
		TextView trackyitext = (TextView) findViewById(R.id.track_detail_text_qishu_yi);
		TextView trackchetext = (TextView) findViewById(R.id.track_detail_text_qishu_che);
		TextView batchcodestarttext = (TextView) findViewById(R.id.track_detail_text_qishu_startno);
		TextView amtzongtext = (TextView) findViewById(R.id.track_detail_text_atm_zong);
		TextView amtyitext = (TextView) findViewById(R.id.track_detail_text_atm_yi);
		TextView starttimetext = (TextView) findViewById(R.id.track_detail_text_starttime);
		TextView iswinaftertext = (TextView) findViewById(R.id.track_detail_iswinafter);
		TextView statetext = (TextView) findViewById(R.id.track_detail_text_state);
		TextView contenttext = (TextView) findViewById(R.id.track_detail_text_content);
		Button cannelid = (Button) findViewById(R.id.trackquery_cancle);
		final String lotName = (String) info.getLotName();
		final String betAmount = (String) info.getAmount();
		final String trackState = (String) info.getState();
		final String batchNums = (String) info.getBatchNum();
		final String lastNums = (String) info.getLastNums();
		final String ordertime = (String) info.getOrderTime();
		final String betcode = (String) info.getBetCode();
		final String beginBatch = (String) info.getBeginBatch();
		final String trackId = (String) info.getId();
		final String remainderAmount = (String) info.getRemainderAmount();
		String prizeEnd = (String) info.getPrizeEnd();
		lotnametext.append(lotName);
		id.append(trackId);
		trackqitext.append(batchNums);
		trackyitext.append(lastNums);
		if (trackState.equals("2")) {
			trackchetext.append((Integer.valueOf(batchNums) - Integer
					.valueOf(lastNums)) + "");
		} else {
			trackchetext.append("0");
		}
		amtyitext.append((Long.valueOf(CheckUtil.isNull(betAmount)) - Long
				.valueOf(CheckUtil.isNull(remainderAmount))) / 100 + "元");
		starttimetext.append(ordertime);
		if (prizeEnd.equals("0")) {
			iswinaftertext.append("否");
		} else {
			iswinaftertext.append("是");
		}
		batchcodestarttext.append(beginBatch);
		amtzongtext.append(Long.valueOf(CheckUtil.isNull(betAmount)) / 100 + "元");
		String lessnum = Integer.valueOf(CheckUtil.isNull(batchNums)) - Integer.valueOf(CheckUtil.isNull(lastNums))
				+ "";
		trackState(statetext, trackState, lessnum);
		if (statetext.getText().toString()
				.equals("当前状态：" + getString(R.string.usercenter_str_running))) {
			cannelid.setVisibility(View.VISIBLE);
			cannelid.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					cancleTrackDialog(trackId);
				}
			});
		}
		contenttext.append("\n" + betcode);
		Button cannel = (Button) findViewById(R.id.track_detail_img_cannle);
		cannel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
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
		text.append(getString(StringId));
	}

	public void initButtonLayout() {
		final Button xiangqing = (Button) findViewById(R.id.fangan);
		final LinearLayout fanganxiangqing = (LinearLayout) findViewById(R.id.fanganxiangqing);
		final Button leirong = (Button) findViewById(R.id.leirong);
		final LinearLayout fanganleirong = (LinearLayout) findViewById(R.id.fanganleirong);
		final Button canyu = (Button) findViewById(R.id.canyu);
		final ListView canyurenyuan = (ListView) findViewById(R.id.canyurenyuan);
		xiangqing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isxiangqing) {
					fanganxiangqing.setVisibility(View.VISIBLE);
					xiangqing
							.setBackgroundResource(R.drawable.joininfobuttonup);
					isxiangqing = false;
				} else {
					fanganxiangqing.setVisibility(View.GONE);
					xiangqing
							.setBackgroundResource(R.drawable.joninfobuttonoff);
					isxiangqing = true;
				}
			}
		});
		leirong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isleirong) {
					fanganleirong.setVisibility(View.VISIBLE);
					leirong.setBackgroundResource(R.drawable.joininfobuttonup);
					isleirong = false;
				} else {
					fanganleirong.setVisibility(View.GONE);
					leirong.setBackgroundResource(R.drawable.joninfobuttonoff);
					isleirong = true;
				}
			}
		});
		canyu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (iscanyu) {
					canyurenyuan.setVisibility(View.VISIBLE);
					canyu.setBackgroundResource(R.drawable.joininfobuttonup);
					iscanyu = false;
					if (tracklist.size() == 0) {
						lookTrackNet(info.getId());
					}
				} else {
					canyurenyuan.setVisibility(View.GONE);
					canyu.setBackgroundResource(R.drawable.joninfobuttonoff);
					iscanyu = true;
				}
			}
		});

	}

	private void lookDetail(List listdata) {
		/* 中奖查询的查看详情使用余额查询的布局 */
		ListView list = (ListView) findViewById(R.id.canyurenyuan);
		Tracklookadapter adapter = new Tracklookadapter(this, listdata);
		list.setAdapter(adapter);
		list.setVisibility(View.VISIBLE);
		setListViewHeightBasedOnChildren(list);
	}

	/**
	 * 中奖查询的适配器
	 */
	public class Tracklookadapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<TrackQueryInfo2> mList;

		public Tracklookadapter(Context context, List<TrackQueryInfo2> list) {
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
			String batchcode = mList.get(position).getBatchcode();
			String lotMulti = mList.get(position).getLotMulti();
			String amount = mList.get(position).getAmount();
			String winCode = mList.get(position).getWinCode();
			String state = mList.get(position).getState();
			String stateMemo = mList.get(position).getStateMemo();
			String prizeAmt = mList.get(position).getPrizeAmt();
			String desc = mList.get(position).getDesc();
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.high_frequencyrevenue_recovery_itme, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.shouyiitem);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			StringBuffer str = new StringBuffer();
			str.append("期号:").append(batchcode).append("\n").append("倍数:")
					.append(lotMulti).append("\n").append("当前投入:")
					.append(PublicMethod.toIntYuan(amount)).append("元");
			if (winCode.equals("")) {
				str.append("\n").append("开奖号码:").append("未开奖").append("\n");
			} else {
				str.append("\n").append("开奖号码:").append(winCode).append("\n");
			}
			str.append("状态:").append(stateMemo).append("\n");
			if (winCode.equals("")) {
				str.append("奖金:").append("未开奖").append("\n");
			} else {
				str.append("奖金:").append(PublicMethod.toIntYuan(prizeAmt))
						.append("元").append("\n");
			}

			if (!desc.equals("") && !desc.equals("null")) {
				String dstr = "";
				String descstr[] = desc.split("_");
				str.append("计划投入:").append(descstr[0]).append("元").append("\n")
						.append("计划收益:").append(descstr[1]).append("元")
						.append("\n").append("收益率:").append(descstr[2]);
			}
			holder.text.setText(str);
			return convertView;
		}

		class ViewHolder {
			TextView text;

		}
	}

	public List encodejsontrack(String json) {
		ArrayList<TrackQueryInfo2> dail = new ArrayList<TrackQueryInfo2>();
		try {
			JSONObject winprizejsonobj = new JSONObject(json);
			String winprizejsonstring = winprizejsonobj.getString("result");
			JSONArray winprizejson = new JSONArray(winprizejsonstring);
			for (int i = 0; i < winprizejson.length(); i++) {
				try {
					TrackQueryInfo2 winPrizeQueryinfo = new TrackQueryInfo2();
					winPrizeQueryinfo.setBatchcode(winprizejson
							.getJSONObject(i).getString("batchCode"));
					winPrizeQueryinfo.setAmount(winprizejson.getJSONObject(i)
							.getString("amount"));
					winPrizeQueryinfo.setState(winprizejson.getJSONObject(i)
							.getString("state"));
					winPrizeQueryinfo.setLotMulti(winprizejson.getJSONObject(i)
							.getString("lotMulti"));
					winPrizeQueryinfo.setWinCode(winprizejson.getJSONObject(i)
							.getString("winCode"));
					winPrizeQueryinfo.setStateMemo(winprizejson
							.getJSONObject(i).getString("stateMemo"));
					winPrizeQueryinfo.setPrizeAmt(winprizejson.getJSONObject(i)
							.getString("prizeAmt"));
					String desc = winprizejson.getJSONObject(i).getString(
							"desc");
					winPrizeQueryinfo.setDesc(desc);
					if (!desc.equals("") && !desc.equals("null")) {
						hansdes = true;
					} else {
						hansdes = false;
					}
					dail.add(winPrizeQueryinfo);
				} catch (Exception e) {
				}
			}
		} catch (JSONException e) {
			try {
				JSONObject winprizejson = new JSONObject(json);
			} catch (JSONException e1) {
			}
		}
		return dail;
	}

	private void initPojo() {
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		phonenum = shellRW.getStringValue("phonenum");
		sessionid = shellRW.getStringValue("sessionid");
		userno = shellRW.getStringValue("userno");
	}

	/**
	 * 取消追号联网
	 * 
	 * @param tsubscribeid
	 *            追号记录id
	 */
	private void lookTrackNet(final String tsubscribeNo) {
		showDialog(0);
		new Thread(new Runnable() {
			public void run() {
				initPojo();
				Message msg = new Message();
				jsontrack = TrackDailInterface.getInstance().looktrack(
						tsubscribeNo);
				try {
					JSONObject lookTrackBackreturn = new JSONObject(jsontrack);
					String errorCode = lookTrackBackreturn
							.getString("error_code");
					String message = lookTrackBackreturn.getString("message");
					if (errorCode.equals("0000")) {
						msg.what = 3;
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

	public void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter; // 取得listview绑定的适配器

		if (listView.getAdapter() == null) {

			return;

		}
		listAdapter = listView.getAdapter();

		ViewGroup.LayoutParams params = listView.getLayoutParams(); // 取得listview所在布局的参数
		if (hansdes) {
			params.height = PublicMethod.getPxInt(186, Trackdetail.this)
					* (listAdapter.getCount());
		} else {
			params.height = PublicMethod.getPxInt(150, Trackdetail.this)
					* (listAdapter.getCount());
		}

		listView.setLayoutParams(params); // 改变listview所在布局的参数
	}

	protected Dialog onCreateDialog(int id) {
		dialog = new ProgressDialog(this);
		switch (id) {
		case 0: {
			dialog.setTitle(R.string.usercenter_netDialogTitle);
			dialog.setMessage(getString(R.string.usercenter_netDialogRemind));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return dialog;
	}

}
