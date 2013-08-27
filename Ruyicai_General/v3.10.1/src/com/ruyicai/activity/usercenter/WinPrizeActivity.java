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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
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
import com.ruyicai.activity.usercenter.detail.Betdetail;
import com.ruyicai.activity.usercenter.detail.Windetail;
import com.ruyicai.activity.usercenter.info.BetQueryInfo;
import com.ruyicai.activity.usercenter.info.WinPrizeQueryInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.BetDetailsInterface;
import com.ruyicai.net.newtransaction.WinQueryInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 中奖查询联网
 * 
 * @author Administrator
 * 
 */
public class WinPrizeActivity extends Activity {
	private BetAndGiftPojo betPojo = new BetAndGiftPojo();
	private LinearLayout usecenerLinear;
	private Button returnButton;
	private TextView titleTextView;
	String jsonString;
	ListView queryinfolist;
	final String BATCHCODE = "batchCode", BETCODE = "betCode",
			CASHTIME = "cashTime", LOTNAME = "lotName", LOTNO = "lotNo",
			WINAMOUNT = "prizeAmt", SELLTIME = "orderTime";
	private final int DIALOG1_KEY = 0;
	private String phonenum, sessionid, userno;
	List<WinPrizeQueryInfo> windatalist = new ArrayList<WinPrizeQueryInfo>();
	Context context = this;
	ProgressDialog dialog;
	String jsonobject;
	int allPage;
	int pageindex;
	boolean isfirst = false;
	WinPrizeAdapter adapter;
	private View view;
	private ProgressBar progressbar;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(WinPrizeActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				encodejson((String) msg.obj);
				adapter.notifyDataSetChanged();
				break;
			case 3:
				if (dialog != null) {
					dialog.dismiss();
				}
				detailsErrorCode(detailJson((WinPrizeQueryInfo) msg.obj));
				break;
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_mainlayoutold);

		returnButton = (Button) findViewById(R.id.layout_usercenter_img_return);
		titleTextView = (TextView) findViewById(R.id.usercenter_mainlayou_text_title);
		returnButton.setBackgroundResource(R.drawable.returnselecter);
		titleTextView.setText(R.string.usercenter_winningCheck);
		returnButton.setText(R.string.returnlastpage);
		returnButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		jsonobject = this.getIntent().getStringExtra("winjson");
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

	private void netting(final int pageindex) {
		progressbar.setVisibility(ProgressBar.VISIBLE);
		final Handler tHandler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				initPojo();
				BetAndWinAndTrackAndGiftQueryPojo winQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
				winQueryPojo.setUserno(userno);
				winQueryPojo.setPageindex(String.valueOf(pageindex));
				winQueryPojo.setMaxresult("10");
				winQueryPojo.setType("winList");

				Message msg = new Message();
				jsonString = WinQueryInterface.getInstance().winQuery(
						winQueryPojo);
				tHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						progressbar.setVisibility(ProgressBar.INVISIBLE);
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

	private void getWinDataNet(final int pageindex) {
		showDialog(0);
		new Thread(new Runnable() {
			public void run() {
				netting(pageindex);
			}
		}).start();
	}

	private void initLinear() {
		usecenerLinear = (LinearLayout) findViewById(R.id.usercenterContent);
		usecenerLinear.addView(initLinearView());
	}

	private View initLinearView() {
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (LinearLayout) inflate.inflate(
				R.layout.usercenter_listview_layout, null);
		queryinfolist = (ListView) view
				.findViewById(R.id.usercenter_listview_queryinfo);
		initListView(queryinfolist, windatalist);
		return view;
	}

	private void initListView(ListView listview, List<WinPrizeQueryInfo> list) {
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
				progressbar.setVisibility(ProgressBar.VISIBLE);
				view.setEnabled(false);
				addmore();

			}
		});

	}

	private void addmore() {
		isfirst = false;

		pageindex++;
		if (pageindex < allPage) {
			netting(pageindex);
		} else {
			pageindex = allPage - 1;
			progressbar.setVisibility(view.INVISIBLE);
			queryinfolist.removeFooterView(view);
			Toast.makeText(WinPrizeActivity.this,
					R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();
		}

	}

	public AlertDialog lookDetailDialog(final WinPrizeQueryInfo info) {
		LayoutInflater factory = LayoutInflater.from(this);
		/* 中奖查询的查看详情使用余额查询的布局 */
		View view = factory.inflate(R.layout.win_detail, null);
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		TextView lotkind = (TextView) view
				.findViewById(R.id.gift_detail_text_lotno);
		TextView batchcode = (TextView) view
				.findViewById(R.id.gift_detail_text_batchcode);
		TextView dingdanno = (TextView) view
				.findViewById(R.id.gift_detail_text_dingdan);
		TextView beishu = (TextView) view
				.findViewById(R.id.gift_detail_text_beishu);
		TextView zhushu = (TextView) view
				.findViewById(R.id.gift_detail_text_zhushu);
		TextView atm = (TextView) view.findViewById(R.id.gift_detail_text_atm);
		TextView statetext = (TextView) view
				.findViewById(R.id.gift_detail_text_state);
		TextView bettime = (TextView) view
				.findViewById(R.id.gift_detail_tex_gifttime);
		TextView content = (TextView) view
				.findViewById(R.id.gift_detail_text_content);
		TextView person = (TextView) view
				.findViewById(R.id.gift_detail_text_person);
		TextView kaijianghao = (TextView) view
				.findViewById(R.id.gift_detail_text_kaijianghao);
		TextView atmz = (TextView) view
				.findViewById(R.id.gift_detail_text_atmzhong);
		TextView cashtime = (TextView) view
				.findViewById(R.id.gift_detail_tex_time);
		final String lotno = info.getLotNo();
		lotkind.append(info.getLotName());
		if (lotno.equals("J00001") || lotno.equals("J00002")
				|| lotno.equals("J00003") || lotno.equals("J00004")
				|| lotno.equals(Constants.LOTNO_JCLQ)
				|| lotno.equals(Constants.LOTNO_JCLQ_DXF)
				|| lotno.equals(Constants.LOTNO_JCLQ_RF)
				|| lotno.equals(Constants.LOTNO_JCLQ_SFC)) {
			batchcode.setVisibility(View.GONE);
		} else {
			batchcode.setVisibility(TextView.VISIBLE);
			batchcode.append(info.getBatchCode());
		}
		dingdanno.append(info.getOrderId());
		beishu.append(info.getLotMulti());
		zhushu.append(info.getBetNum());
		final String FormatAmount = PublicMethod.toYuan(info.getAmount());
		atm.append(FormatAmount + "元");
		atmz.append(PublicMethod.toYuan(info.getWinAmount()) + "元");
		cashtime.append(info.getCashTime());
		person.setVisibility(View.GONE);
		statetext.setVisibility(View.GONE);
		kaijianghao.setVisibility(View.GONE);
		bettime.append(info.getSellTime());
		content.setText(Html.fromHtml("方案内容：<br>" + info.getBetCodeHtml()));
		Button cancleLook = (Button) view
				.findViewById(R.id.gift_detail_img_cannle);
		cancleLook.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();
		dialog.getWindow().setContentView(view);
		return dialog;

	}

	public void encodejson(String json) {

		try {
			JSONObject winprizejsonobj = new JSONObject(json);
			allPage = Integer.parseInt(winprizejsonobj.getString("totalPage"));
			String maxpage = "";
			String winprizejsonstring = winprizejsonobj.getString("result");

			JSONArray winprizejson = new JSONArray(winprizejsonstring);
			for (int i = 0; i < winprizejson.length(); i++) {
				try {
					WinPrizeQueryInfo winPrizeQueryinfo = new WinPrizeQueryInfo();
					winPrizeQueryinfo.setBatchCode(winprizejson
							.getJSONObject(i).getString(BATCHCODE));
					winPrizeQueryinfo.setCashTime(winprizejson.getJSONObject(i)
							.getString(CASHTIME));
					winPrizeQueryinfo.setLotNo(winprizejson.getJSONObject(i)
							.getString(LOTNO));
					winPrizeQueryinfo.setLotName(winprizejson.getJSONObject(i)
							.getString(LOTNAME));
					winPrizeQueryinfo.setSellTime(winprizejson.getJSONObject(i)
							.getString(SELLTIME));
					winPrizeQueryinfo.setWinAmount(winprizejson
							.getJSONObject(i).getString(WINAMOUNT));
					winPrizeQueryinfo.setLotMulti(winprizejson.getJSONObject(i)
							.getString("lotMulti"));
					winPrizeQueryinfo.setOrderId(winprizejson.getJSONObject(i)
							.getString("orderId"));
					winPrizeQueryinfo.setBetNum(winprizejson.getJSONObject(i)
							.getString("betNum"));
					winPrizeQueryinfo.setAmount(winprizejson.getJSONObject(i)
							.getString("amount"));
					winPrizeQueryinfo.setWincode(winprizejson.getJSONObject(i)
							.getString("winCode"));
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
	 * 中奖查询的适配器
	 */
	public class WinPrizeAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<WinPrizeQueryInfo> mList;

		public WinPrizeAdapter(Context context, List<WinPrizeQueryInfo> list) {
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
			final WinPrizeQueryInfo info = mList.get(position);
			final String lotno = (String) mList.get(position).getLotNo();
			final String lotName = (String) mList.get(position).getLotName();
			final String prizeqihao = (String) mList.get(position)
					.getBatchCode();
			String winAmount = (String) mList.get(position).getWinAmount();
			final String prizemoney = PublicMethod.formatMoney(winAmount);
			final String sellTime = (String) mList.get(position).getSellTime();
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.usercenter_listitem_winprize_query, null);
				holder = new ViewHolder();
				holder.lotteryname = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_lotteryname);
				holder.prizeqihao = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_prizeqihao);
				holder.prizemoney = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_paymoney);
				holder.paytime = (TextView) convertView
						.findViewById(R.id.usercenter_winprize_prizemoney);
				holder.lookdetail = (Button) convertView
						.findViewById(R.id.usercenter_winprize_querydetail);
				holder.buyagain = (Button) convertView
						.findViewById(R.id.usercenter_winprize_buyagain);
				/**add by yejc 20130806 start*/
				holder.linearLyaout = (LinearLayout) convertView
						.findViewById(R.id.usercenter_winprize_linear);
				/**add by yejc 20130806 end*/
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.linearLyaout.setOrientation(LinearLayout.VERTICAL); //add by yejc 20130806 start
			String prizeString = getString(R.string.usercenter_prizeMoney);// 中奖金额字
			holder.lotteryname.setText(lotName);
			if (lotno.equals("J00001") || lotno.equals("J00002")
					|| lotno.equals("J00003") || lotno.equals("J00004")
					|| lotno.equals(Constants.LOTNO_JCLQ)
					|| lotno.equals(Constants.LOTNO_JCLQ_DXF)
					|| lotno.equals(Constants.LOTNO_JCLQ_RF)
					|| lotno.equals(Constants.LOTNO_JCLQ_SFC)
					|| lotno.equals(Constants.LOTNO_JCLQ_HUN)
					|| lotno.equals(Constants.LOTNO_JCZQ_HUN)
					|| lotno.equals(Constants.LOTNO_JCZQ_RQSPF)) {
				holder.prizeqihao.setVisibility(TextView.GONE);
			} else {
				holder.prizeqihao.setVisibility(TextView.VISIBLE);
				holder.prizeqihao.setText("(期号:" + prizeqihao + ")");
			}
			holder.paytime.setText(sellTime);
			holder.prizemoney.setText(Html.fromHtml(prizeString
					+ "<font color=\"red\">" + prizemoney + "</font>"));
			holder.buyagain.setVisibility(Button.GONE);
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
			TextView paytime;// 购彩时间
			TextView prizemoney;
			Button lookdetail;
			Button buyagain;
			LinearLayout linearLyaout; //add by yejc 20130806 start
		}
	}

	/**
	 * 中奖详情
	 */
	public void betQueryDetails(final WinPrizeQueryInfo betQueryinfo) {
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
	public WinPrizeQueryInfo detailJson(WinPrizeQueryInfo betQueryinfo) {

		try {
			JSONObject winprizejsonobj = new JSONObject(betQueryinfo.getJson());
			JSONObject winprizejsonstring = winprizejsonobj
					.getJSONObject("result");
			betQueryinfo.setBetCodeHtml(winprizejsonstring
					.getString("betCodeHtml"));
			betQueryinfo.setJson(winprizejsonstring.getString("betCodeJson"));

		} catch (JSONException e) {

		}
		return betQueryinfo;
	}

	/**
	 * 投注查询详情跳转
	 * 
	 * @param betQueryinfo
	 */
	public void detailsErrorCode(WinPrizeQueryInfo betQueryinfo) {
		Intent intent = new Intent(this, Windetail.class);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
			objStream.writeObject(betQueryinfo);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		intent.putExtra("info", byteStream.toByteArray());
		startActivity(intent);
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}
}
