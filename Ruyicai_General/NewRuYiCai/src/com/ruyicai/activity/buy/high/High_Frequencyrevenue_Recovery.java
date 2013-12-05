package com.ruyicai.activity.buy.high;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.ssq.BettingSuccessActivity;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.ShouyiDataInterface;
import com.ruyicai.net.newtransaction.getRecoveryBatchCode;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.pojo.ShouyiPojo;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/* 高频彩收益追号
 * **/

public class High_Frequencyrevenue_Recovery extends Activity implements HandlerMsg  {
	String phonenum, sessionId, userno;
	TextView title;
	BetAndGiftPojo betAndGift;
	TextView zhumatext;
	TextView caizhong;
	Spinner startqi;
	EditText qishu;
	EditText beishu;
	EditText allshouyilv;
	EditText qianshouyiqi;
	EditText qianshouyilv;
	EditText houshouyilv;
	Button jisuan;
	RadioButton all;
	RadioButton qianhou;
	String textzhuma = "";
	String lotnostr;
	AlertDialog information;
	int hightball = 0;
	int zhushu;
	String jsonstring;
	ArrayList<String> batchcodes = new ArrayList<String>();
	ArrayList<RecoveryInfo> shouyidata = new ArrayList<RecoveryInfo>();
	private Context context;
	HighFrequencyHandler handler = new HighFrequencyHandler(this);
	private Controller controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.high_frequencyrevenue_recovery_main);
		context = this;
		getInfo();
		init();
		getbatchcodes();
		// setTitle();
		zhumatext.setText(textzhuma);

	}

	private void initSpiner() {
		// TODO Auto-generated method stub
		ArrayList<String> batchcodestr = new ArrayList<String>();
		for (int i = 0; i < batchcodes.size(); i++) {
			if (i == 0) {
				batchcodestr.add("第" + batchcodes.get(i) + "期" + "[当前期]");
			} else {
				batchcodestr.add("第" + batchcodes.get(i) + "期");
			}
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, batchcodestr);
		adapter.setDropDownViewResource(R.layout.myspinner_dropdown);
		startqi.setAdapter(adapter);

	}

	/*
	 * 初始化组件 *
	 */
	private void init() {
		zhumatext = (TextView) findViewById(R.id.shouyizhihaozhuma);
		startqi = (Spinner) findViewById(R.id.shouyiqihao);
		qishu = (EditText) findViewById(R.id.shouyiqishuEditext);
		beishu = (EditText) findViewById(R.id.shouyibeishuEditext);
		allshouyilv = (EditText) findViewById(R.id.shouyiquanchengEditext);
		qianshouyiqi = (EditText) findViewById(R.id.shouyiqianEditext);
		qianshouyilv = (EditText) findViewById(R.id.shouyizhihouEditext);
		houshouyilv = (EditText) findViewById(R.id.shouyizhihouEditext1);
		jisuan = (Button) findViewById(R.id.jisuan);
		all = (RadioButton) findViewById(R.id.shouyiquancheng);
		qianhou = (RadioButton) findViewById(R.id.shouyizhihou);
		caizhong = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_caizhong);
		caizhong.setText(PublicMethod.toLotno(betAndGift.getLotno()));
		all.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					allshouyilv.setEnabled(true);
					qianhou.setChecked(false);
					qianshouyiqi.setEnabled(false);
					qianshouyilv.setEnabled(false);
					houshouyilv.setEnabled(false);
				}
			}
		});
		qianhou.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					allshouyilv.setEnabled(false);
					all.setChecked(false);
					qianshouyiqi.setEnabled(true);
					qianshouyilv.setEnabled(true);
					houshouyilv.setEnabled(true);
				}
			}
		});
		jisuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String beish = beishu.getText().toString();
				String qi = qishu.getText().toString();
				String alledit = allshouyilv.getText().toString();
				String qianqi = qianshouyiqi.getText().toString();
				String qianlv = qianshouyilv.getText().toString();
				String houlv = houshouyilv.getText().toString();
				if (qi.equals("") || qi.equals("0")) {
					Toast.makeText(High_Frequencyrevenue_Recovery.this,
							"请输入有效期数", Toast.LENGTH_SHORT).show();
					return;
				}
				if (qi.length() > 3 || Integer.valueOf(qi) > 99) {
					Toast.makeText(High_Frequencyrevenue_Recovery.this,
							"超过期数上限99期,请输入有效期数", Toast.LENGTH_SHORT).show();
					return;
				}
				if (beish.equals("") || beish.equals("0")) {
					Toast.makeText(High_Frequencyrevenue_Recovery.this,
							"请输入有效倍数", Toast.LENGTH_SHORT).show();
					return;
				}
				if (beish.length() > 5 || Integer.valueOf(beish) > 9999) {
					Toast.makeText(High_Frequencyrevenue_Recovery.this,
							"超过倍数上限9999倍,请输入有效倍数", Toast.LENGTH_SHORT).show();
					return;
				}
				if (all.isChecked()) {
					if (alledit.equals("") || alledit.equals("0")) {
						Toast.makeText(High_Frequencyrevenue_Recovery.this,
								"请输入有效全程收益率", Toast.LENGTH_SHORT).show();
						return;
					}
				} else {
					if (qianqi.equals("") || qianqi.equals("0")) {
						Toast.makeText(High_Frequencyrevenue_Recovery.this,
								"请输入有效期数", Toast.LENGTH_SHORT).show();
						return;
					}
					if (qianqi.length() > 3 || Integer.valueOf(qi) > 99) {
						Toast.makeText(High_Frequencyrevenue_Recovery.this,
								"超过期数上限99期,请输入有效期数", Toast.LENGTH_SHORT).show();
						return;
					}

					if (qianlv.equals("") || qianlv.equals("0")) {
						Toast.makeText(High_Frequencyrevenue_Recovery.this,
								"请输入有效收益率", Toast.LENGTH_SHORT).show();
						return;
					}
					if (houlv.equals("") || houlv.equals("0")) {
						Toast.makeText(High_Frequencyrevenue_Recovery.this,
								"请输入有效收益率", Toast.LENGTH_SHORT).show();
						return;
					}
					if (Integer.valueOf(qi) < Integer.valueOf(qianqi)) {
						Toast.makeText(High_Frequencyrevenue_Recovery.this,
								"追号总期数应大于前期数", Toast.LENGTH_SHORT).show();
						return;
					}

				}
				getshouyidata();
			}
		});

	}

	// private void setTitle() {
	// if (lotnostr.equals(com.ruyicai.constant.Constants.LOTNO_SSC)) {
	// title.setText("时时彩-收益率追号");
	// } else if (lotnostr.equals(com.ruyicai.constant.Constants.LOTNO_11_5)) {
	// title.setText("江西11选5-收益率追号");
	// } else if (lotnostr.equals(com.ruyicai.constant.Constants.LOTNO_eleven))
	// {
	// title.setText("11夺金-收益率追号");
	// }else if(lotnostr.equals(com.ruyicai.constant.Constants.LOTNO_GD_11_5)){
	// title.setText("广东11选5-收益率追号");
	// }
	// }

	/*
	 * 创建收益dialog; *
	 */

	public void createinformationdialog() {
		Log.e("betcode", betAndGift.getBet_code());
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		information = new AlertDialog.Builder(this).create();
		View v = inflater.inflate(R.layout.high_frequencyrevenue_recovery_list,
				null);
		CheckBox checkPrize = (CheckBox) v
				.findViewById(R.id.alert_dialog_shouyi_check_prize);
		TextView title = (TextView) v.findViewById(R.id.titletext);
		TextView moenytext = (TextView) v.findViewById(R.id.moneytext);
		try {
			moenytext.setText("注数:"
					+ zhushu
					+ "注"
					+ "       "
					+ "金额:"
					+ PublicMethod.toIntYuan(shouyidata.get(
							shouyidata.size() - 1).getLeijitouru()) + "元");
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (lotnostr.equals(Constants.LOTNO_SSC)) {
			title.setText("重庆时时彩-收益率追号");
		} else if (lotnostr.equals(Constants.LOTNO_11_5)) {
			title.setText("江西11选5-收益率追号");
		} else if (lotnostr.equals(Constants.LOTNO_eleven)) {
			title.setText("11夺金-收益率追号");
		} else if (lotnostr.equals(Constants.LOTNO_GD_11_5)) {
			title.setText("广东11选5-收益率追号");
		} else if (lotnostr.equals(Constants.LOTNO_ten)) {
			title.setText("广东快乐十分-收益率追号");
		}else if(lotnostr.equals(Constants.LOTNO_NMK3)){
			title.setText("快三-收益率追号");
		}else if(lotnostr.equals(Constants.LOTNO_CQ_ELVEN_FIVE)){
			title.setText("重庆11选5-收益率追号");
		}
		ListView shouyilist = (ListView) v.findViewById(R.id.shouyilist);
		Yieldadapter adapter = new Yieldadapter(
				High_Frequencyrevenue_Recovery.this, shouyidata);
		shouyilist.setAdapter(adapter);
		checkPrize.setChecked(true);

		// 设置betAndGift.prizeend与checkPrize保持一致
		betAndGift.setPrizeend("1");

		checkPrize.setButtonDrawable(R.drawable.check_on_off);
		checkPrize.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					betAndGift.setPrizeend("1");
				} else {
					betAndGift.setPrizeend("0");
				}
			}
		});
		Button cancel = (Button) v.findViewById(R.id.cancel);
		Button ok = (Button) v.findViewById(R.id.ok);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				information.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RWSharedPreferences pre = new RWSharedPreferences(
						High_Frequencyrevenue_Recovery.this, "addInfo");
				sessionId = pre.getStringValue("sessionid");
				phonenum = pre.getStringValue("phonenum");
				userno = pre.getStringValue("userno");
				if (userno.equals("")) {
					Intent intentSession = new Intent(
							High_Frequencyrevenue_Recovery.this,
							UserLogin.class);
					startActivityForResult(intentSession, 0);
				} else {
					touZhuNet();

				}
			}

		});
		information.show();
		information.getWindow().setContentView(v);
	}

	/**
	 * s 从上一个页面获取信息
	 */
	public void getInfo() {
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		betAndGift = app.getPojo();
		textzhuma = app.getHtextzhuma();
		lotnostr = betAndGift.getLotno();
		hightball = app.getHightball();
		zhushu = app.getHzhushu();
	}

	public void getbatchcodes() {
		final Handler hand = new Handler();
		final ProgressDialog dialog = onCreateDialog();
		dialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String bathcode = getRecoveryBatchCode.getInstance().getCode(
						lotnostr, "10");

				try {
					JSONObject json = new JSONObject(bathcode);
					JSONArray array = json.getJSONArray("result");
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						String betcode = obj.getString("batchCode");
						batchcodes.add(betcode);
					}
					dialog.dismiss();
					hand.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							initSpiner();
						}
					});

				} catch (JSONException e) {
					// TODO: handle exception
					hand.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							dialog.dismiss();
							Toast.makeText(High_Frequencyrevenue_Recovery.this,
									"期号获取失败", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();

	}

	private String getsubscribeInfo() {
		StringBuffer str = new StringBuffer();
		if (shouyidata.size() != 0) {
			for (int i = 0; i < shouyidata.size(); i++) {
				str.append(shouyidata.get(i).getBatchcod())
						.append(",")
						.append(shouyidata.get(i).getDangqitouru())
						.append(",")
						.append(shouyidata.get(i).getBeishu())
						.append(",")
						.append(PublicMethod.toIntYuan(shouyidata.get(i)
								.getDangqitouru()))
						.append("_")
						.append(PublicMethod.toIntYuan(shouyidata.get(i)
								.getDangqishouyi())).append("_")
						.append(shouyidata.get(i).getShouyilv());
				if (i != shouyidata.size() - 1) {
					str.append("!");
				}
			}
		}
		return str.toString();
	}

	private String getAmoot() {
		String amt = "";
		int onceamt = 0;
		try {
			for (int i = 0; i < shouyidata.size(); i++) {
				onceamt += Double.valueOf(CheckUtil.isNull(shouyidata.get(i).getDangqitouru()));
			}
			amt = String.valueOf(onceamt);
		} catch (Exception ex) {
			PublicMethod.outLog(this.getClass().getSimpleName(), "getAmoot()", ex.getMessage());
		}
		return amt;
	}

	private void getshouyidata() {
		final ShouyiPojo pojo = new ShouyiPojo();
		String beish = beishu.getText().toString();
		String qi = qishu.getText().toString();
		String alledit = allshouyilv.getText().toString();
		String qianqi = qianshouyiqi.getText().toString();
		String qianlv = qianshouyilv.getText().toString();
		String houlv = houshouyilv.getText().toString();
		if (batchcodes.size() == 0) {
			return;
		}
		String batchcode = batchcodes.get(startqi.getSelectedItemPosition());
		pojo.setBatchcode(batchcode);
		pojo.setBatchnum(qi);
		pojo.setLotmulti(beish);
		pojo.setLotno(lotnostr);
		pojo.setBetcode(betAndGift.getBet_code());
		pojo.setBetNum(String.valueOf(zhushu));
		if (all.isChecked()) {
			pojo.setWholeYield(alledit);
		} else if (qianhou.isChecked()) {
			pojo.setBeforeBatchNum(qianqi);
			pojo.setBeforeYield(qianlv);
			pojo.setAfterYield(houlv);
		}
		final Handler hand = new Handler();
		final ProgressDialog dialog = onCreateDialog();
		dialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				jsonstring = ShouyiDataInterface.getInstance().getshouyidada(
						pojo);

				try {
					JSONObject json = new JSONObject(jsonstring);
					String errorcode = json.getString("error_code");
					final String message = json.getString("message");
					if (errorcode.equals("0000")) {
						encodejson(jsonstring);
						hand.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								dialog.dismiss();
								createinformationdialog();
							}
						});

					} else if (errorcode.equals("9999")) {
						hand.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								dialog.dismiss();
								Toast.makeText(
										High_Frequencyrevenue_Recovery.this,
										message, Toast.LENGTH_SHORT).show();
							}
						});
					}

				} catch (JSONException e) {
					// TODO: handle exception
					hand.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							dialog.dismiss();
							Toast.makeText(High_Frequencyrevenue_Recovery.this,
									"获取失败", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();

	}

	/**
	 * 投注联网
	 */
	public void touZhuNet() {
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setSubscribeInfo(getsubscribeInfo());
		if (all.isChecked()) {
			betAndGift.setDescription("全程收益率"
					+ allshouyilv.getText().toString() + "%");
		} else if (qianhou.isChecked()) {
			betAndGift.setDescription("前" + qianshouyiqi.getText().toString()
					+ "期收益率" + qianshouyilv.getText().toString() + "%之后收益率"
					+ houshouyilv.getText().toString() + "%");
		}
		betAndGift.setIsBetAfterIssue("1");
		betAndGift.setBatchcode(shouyidata.get(0).getBatchcod());
		betAndGift.setBatchnum(qishu.getText().toString());
		betAndGift.setLotmulti(beishu.getText().toString());
		betAndGift.setAmount(getAmoot());
		controller = Controller.getInstance(High_Frequencyrevenue_Recovery.this);
		if (controller != null) {
			controller.doBettingAction(handler, betAndGift);
		}
	}

	public void encodejson(String json) {
		if (shouyidata.size() != 0) {
			shouyidata.clear();
		}
		try {
			JSONObject shouyidatajson = new JSONObject(json);
			String data = shouyidatajson.getString("result");
			JSONArray dataarray = new JSONArray(data);
			for (int i = 0; i < dataarray.length(); i++) {
				try {
					RecoveryInfo info = new RecoveryInfo();
					info.setBatchcod(dataarray.getJSONObject(i).getString(
							"batchCode"));
					info.setBeishu(dataarray.getJSONObject(i).getString(
							"lotMulti"));
					info.setDangqitouru(dataarray.getJSONObject(i).getString(
							"currentIssueInput"));
					info.setDangqishouyi(dataarray.getJSONObject(i).getString(
							"currentIssueYield"));
					info.setLeijitouru(dataarray.getJSONObject(i).getString(
							"accumulatedInput"));
					info.setLeijishouyi(dataarray.getJSONObject(i).getString(
							"accumulatedYield"));
					info.setShouyilv(dataarray.getJSONObject(i).getString(
							"yieldRate"));
					shouyidata.add(info);
				} catch (Exception e) {
				}
			}

		} catch (JSONException e) {

		}
	}

	/**
	 * 收益数据
	 */
	public class Yieldadapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<RecoveryInfo> mList;

		public Yieldadapter(Context context, List<RecoveryInfo> list) {
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
			String bachcode = mList.get(position).getBatchcod();
			String danqibeishu = mList.get(position).getBeishu();
			String dangqitouru = PublicMethod.toIntYuan(mList.get(position)
					.getDangqitouru());
			String dangqishouyi = PublicMethod.toIntYuan(mList.get(position)
					.getDangqishouyi());
			String leijitouru = PublicMethod.toIntYuan(mList.get(position)
					.getLeijitouru());
			String leijishouyi = PublicMethod.toIntYuan(mList.get(position)
					.getLeijishouyi());
			String shouyilv = mList.get(position).getShouyilv();
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.high_frequencyrevenue_recovery_itme, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.shouyiitem);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			StringBuffer str = new StringBuffer();
			str.append(bachcode).append("期").append("              ")
					.append(danqibeishu).append("倍").append("\n");
			str.append("当期投入:").append("￥").append(dangqitouru)
					.append("              ");
			str.append("当期收益:").append("￥").append(dangqishouyi).append("\n")
					.append("累计投入:").append("￥").append(leijitouru)
					.append("              ");
			str.append("累计收益:").append("￥").append(leijishouyi)
					.append("              ").append("\n").append("收益率:")
					.append(shouyilv);
			holder.text.setText(str);
			convertView.setTag(holder);
			return convertView;
		}

		class ViewHolder {
			TextView text;
		}
	}

	public ProgressDialog onCreateDialog() {
		ProgressDialog progressDialog = new ProgressDialog(
				High_Frequencyrevenue_Recovery.this);
		progressDialog.setMessage("网络连接中...");
		progressDialog.setIndeterminate(true);
		return progressDialog;
	}

	class RecoveryInfo {
		String batchcod = "";
		String beishu = "";
		String dangqitouru = "";
		String dangqishouyi = "";
		String leijitouru = "";
		String leijishouyi = "";
		String shouyilv = "";

		public String getBatchcod() {
			return batchcod;
		}

		public void setBatchcod(String batchcod) {
			this.batchcod = batchcod;
		}

		public String getBeishu() {
			return beishu;
		}

		public void setBeishu(String beishu) {
			this.beishu = beishu;
		}

		public String getDangqitouru() {
			return dangqitouru;
		}

		public void setDangqitouru(String dangqitouru) {
			this.dangqitouru = dangqitouru;
		}

		public String getDangqishouyi() {
			return dangqishouyi;
		}

		public void setDangqishouyi(String dangqishouyi) {
			this.dangqishouyi = dangqishouyi;
		}

		public String getLeijitouru() {
			return leijitouru;
		}

		public void setLeijitouru(String leijitouru) {
			this.leijitouru = leijitouru;
		}

		public String getLeijishouyi() {
			return leijishouyi;
		}

		public void setLeijishouyi(String leijishouyi) {
			this.leijishouyi = leijishouyi;
		}

		public String getShouyilv() {
			return shouyilv;
		}

		public void setShouyilv(String shouyilv) {
			this.shouyilv = shouyilv;
		}

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
	
	class HighFrequencyHandler extends MyHandler {

		public HighFrequencyHandler(HandlerMsg msg) {
			super(msg);
			// TODO Auto-generated constructor stub
		}

		public void handleMessage(Message msg) {
			//super.handleMessage(msg);
			if (controller != null) {
				JSONObject obj = controller.getRtnJSONObject();
				String error;
				try {
					error = obj.getString("error_code");
				    final String returnMessage = obj.getString("message");
					if (error.equals("0000")) {
						handler.post(new Runnable() {
	
							@Override
							public void run() {
								// TODO Auto-generated method stub
								/**modify by pengcx 20130605 start*/
								Intent intent = new Intent(High_Frequencyrevenue_Recovery.this, BettingSuccessActivity.class);
								intent.putExtra("page", BettingSuccessActivity.HIGHTADDTO);
								intent.putExtra("lotno", betAndGift.getLotno());
								intent.putExtra("amount", betAndGift.getAmount());
								startActivity(intent);
								information.dismiss();
							}
						});
					} else {
						handler.post(new Runnable() {
	
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(
										High_Frequencyrevenue_Recovery.this,
										returnMessage, Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(High_Frequencyrevenue_Recovery.this,
					"获取失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
	}

	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}
}
