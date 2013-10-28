package com.ruyicai.activity.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.android.secure.AlixId;
import com.alipay.android.secure.BaseHelper;
import com.alipay.android.secure.MobileSecurePayHelper;
import com.alipay.android.secure.MobileSecurePayer;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.net.newtransaction.recharge.DirectPayInterface;
import com.ruyicai.util.PublicMethod;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 直接支付页面
 * 
 * @author PengCX
 * 
 */
public class DirectPayActivity extends Activity implements HandlerMsg {
	private Context context;

	private ListView payMethodListView;
	private ProgressDialog mProgress;
	private List<Map<String, Object>> listviewContentList;

	private String TITLE = "title";
	private String ISHANDINGFREE = "isHandingFree";
	private String PICTURE = "";

	private BetAndGiftPojo betAndGift;
	private MyHandler handler = new MyHandler(this);

	private String url;
	private boolean isInstall = true;
	private boolean isAlipaySecure = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.account_directpay);
		context = this;

		initScreenShow();
		processOperation();
	}

	@Override
	protected void onPause() {
		super.onPause();
		isInstall = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isInstall = true;
	}

	private void initScreenShow() {
		payMethodListView = (ListView) findViewById(R.id.directpay_listview_paymethod);
		listviewContentList = getListViewContent();

		payMethodAdapter adapter = new payMethodAdapter(this,
				listviewContentList);
		payMethodListView.setAdapter(adapter);
	}

	private List<Map<String, Object>> getListViewContent() {
		List<Map<String, Object>> listMaps = new ArrayList<Map<String, Object>>(
				2);
		Map<String, Object> map;

		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.zhfb_cards_secure_recharge));
		map.put(PICTURE, R.drawable.recharge_alipay_safe);
		map.put(ISHANDINGFREE, getString(R.string.account_zfb_secure));
		listMaps.add(map);

		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.zhfb_cards_recharge));
		map.put(PICTURE, R.drawable.recharge_alipay);
		map.put(ISHANDINGFREE, getString(R.string.account_zfb_alert));
		listMaps.add(map);

		return listMaps;
	}

	class payMethodAdapter extends BaseAdapter {
		private LayoutInflater layoutInflater;
		private List<Map<String, Object>> list;

		public payMethodAdapter(Context context, List<Map<String, Object>> list) {
			super();
			this.layoutInflater = LayoutInflater.from(context);
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			String title = (String) list.get(position).get(TITLE);
			Integer iconId = (Integer) list.get(position).get(PICTURE);
			String alertString = (String) list.get(position).get(ISHANDINGFREE);

			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.account_listviw_item, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.account_recharge_listview_text);
				holder.isfreeHanding = (TextView) convertView
						.findViewById(R.id.ishandingfree);
				holder.lefticon = (ImageView) convertView
						.findViewById(R.id.account_recharge_type);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			SpannableStringBuilder builder1 = new SpannableStringBuilder();
			String str1 = title;
			builder1.append(str1);
			String alertStr1 = "(免手续费)";
			builder1.append(alertStr1);
			builder1.setSpan(new ForegroundColorSpan(Color.RED),
					builder1.length() - alertStr1.length(), builder1.length(),
					Spanned.SPAN_COMPOSING);
			holder.title.setText(builder1);

			SpannableStringBuilder builder = new SpannableStringBuilder();
			String str = alertString;
			builder.append(str);
			holder.isfreeHanding.setHint(builder);

			holder.lefticon.setBackgroundResource(iconId);

			return convertView;
		}

		class ViewHolder {
			TextView title;
			ImageView lefticon;
			TextView isfreeHanding;
		}

	}

	private void processOperation() {
		payMethodListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView AccountType = (TextView) view
						.findViewById(R.id.account_recharge_listview_text);
				String textString = AccountType.getText().toString();
				if ("支付宝安全支付(免手续费)".equals(textString)) {
					isAlipaySecure = true;
					if (isInstall) {
						isInstall = false;
						isInstallSecurePay();
					}

				} else if ("支付宝充值(免手续费)".equals(textString)) {
					isAlipaySecure = false;
					beginAlipayRecharge();
				}
			}
		});
	}

	private void isInstallSecurePay() {
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(context);
		boolean isMobile_spExist = mspHelper.detectMobile_sp(Constants.ALIPAY_PLUGIN_NAME,
				Constants.ALIPAY_PACK_NAME);

		if (!isMobile_spExist) {
			isInstall = true;
			return;
		} else {
			getOrderInfo();
		}
	}

	private void beginAlipayRecharge() {
		ConnectivityManager ConnMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = ConnMgr.getActiveNetworkInfo();
		if (info.getExtraInfo() != null
				&& info.getExtraInfo().equalsIgnoreCase("3gwap")) {
			Toast.makeText(this, "提醒：检测到您的接入点为3gwap，可能无法正确充值,请切换到3gnet！",
					Toast.LENGTH_LONG).show();
		}

		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		betAndGift = app.getPojo();

		new Thread(new Runnable() {

			@Override
			public void run() {
				String error_code = "";
				String message = "";

				String result = DirectPayInterface.getInstance().directPay(
						betAndGift, isAlipaySecure);

				try {
					JSONObject obj = new JSONObject(result);

					error_code = obj.getString("error_code");
					message = obj.getString("message");

					if (error_code.equals("0000")) {
						url = obj.getString("return_url");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				handler.handleMsg(error_code, message);
			}
		}).start();
	}

	private void getOrderInfo() {
		(new Handler()).post(new Runnable() {

			@Override
			public void run() {
				ApplicationAddview app = (ApplicationAddview) getApplicationContext();
				betAndGift = app.getPojo();

				judgeLoterryType(betAndGift);

				String directPayResult = DirectPayInterface.getInstance()
						.directPay(betAndGift, isAlipaySecure);

				try {
					JSONObject jsonObject = new JSONObject(directPayResult);
					String error_code = jsonObject.getString("error_code");
					if (error_code.equals("0000")) {
						MobileSecurePayer msp = new MobileSecurePayer();
						String info = jsonObject.getString("value");
						boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY,
								DirectPayActivity.this);
						if (bRet) {
							closeProgress();
							mProgress = BaseHelper.showProgress(
									DirectPayActivity.this, null, "正在支付",
									false, true);
						}
					} else {
						DirectPayActivity.this.finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}

	private void judgeLoterryType(BetAndGiftPojo betAndGift) {

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			String strRet = (String) msg.obj;
			Log.e("strRet==", strRet);
			switch (msg.what) {
			case AlixId.RQF_PAY: {
				closeProgress();
				try {
					String resultStatus = "resultStatus={";
					int iresultStart = strRet.indexOf("resultStatus={");
					iresultStart += resultStatus.length();
					int iresultEnd = strRet.indexOf("};memo=");
					resultStatus = strRet.substring(iresultStart, iresultEnd);
					String memo = "memo=";
					int imemoStart = strRet.indexOf("memo=");
					imemoStart += memo.length();
					int imemoEnd = strRet.indexOf(";result=");
					memo = strRet.substring(imemoStart, imemoEnd);
					if (resultStatus.equals("9000")) {
						PublicMethod.showDialogOfDirectPay(context, betAndGift);
					}
				} catch (Exception e) {
					e.printStackTrace();
					BaseHelper.showDialog(DirectPayActivity.this, "提示", strRet,
							R.drawable.info);
				}
			}
				break;
			}

			super.handleMessage(msg);
		}
	};

	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void errorCode_0000() {
		if (!isAlipaySecure) {
			Intent intent = new Intent(DirectPayActivity.this,
					AccountDialog.class);
			intent.putExtra("accounturl", url);
			intent.putExtra("isDirectPay", true);
			intent.putExtra("loteNo", betAndGift.getLotno().toString());
			startActivity(intent);
		}
	}

	@Override
	public void errorCode_000000() {

	}

	@Override
	public Context getContext() {
		return this;
	}
}
